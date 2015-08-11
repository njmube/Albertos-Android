package mx.com.linkapp.hectorgarcia.albertos.fragments;


//import android.app.Fragment;
//import android.app.FragmentTransaction;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import mx.com.linkapp.hectorgarcia.albertos.API_LinkApp;
import mx.com.linkapp.hectorgarcia.albertos.DateDialog;
import mx.com.linkapp.hectorgarcia.albertos.OnTaskCompleted;
import mx.com.linkapp.hectorgarcia.albertos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registro extends Fragment{

    //EditText txtNombre;

    public Registro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_registro, container, false);



        Button buttonAceptar = (Button) view.findViewById(R.id.btnAceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RegistrarUsuario(view);
            }
        });

        TextView textLeerTerminos = (TextView) view.findViewById(R.id.linkLeerTerminos);
        textLeerTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://paneldecontrol.linkapp.com.mx/API/albertos/terminos"));
                startActivity(intent);
            }
        });

        EditText fechaNacimientoText = (EditText) view.findViewById(R.id.fechaNacimientoText);

        fechaNacimientoText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog();
                    dialog.setView(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        fillColoniasFromAPI(view);

        return view;

    }



    public void fillColoniasFromAPI(View view){
        API_LinkApp API = new API_LinkApp();
        API.setContext(view.getContext());
        List<String> listaColonias = API.getColonias();
        Spinner spinnerColonia = (Spinner) view.findViewById(R.id.coloniaSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,listaColonias);
        spinnerColonia.setAdapter(adapter);

        if(listaColonias.size() <= 1){
            Toast.makeText(getActivity().getBaseContext(),"Ocurrió un error al cargar las colonias.",Toast.LENGTH_SHORT).show();
        }
    }



    private void RegistrarUsuario(final View v){
        if(this.validarFormulario(v)) {
            String data = this.getFormData(v);
            API_LinkApp API = new API_LinkApp();
            API.setContext(v.getContext());
            API.registerUser(data, new RegistroCallBack() {
                @Override
                public void onRegisterDone(String response, Context context) {
                    try {
                        JSONObject json = new JSONObject(response);
                        Boolean success = json.getBoolean("success");
                        Integer code = json.getInt("code");
                        String message = json.getString("message");
                        if (success) {
                            if (code == 0) {
                                Looper.prepare();
                                Toast.makeText(context, "El registro se realizó con exito.", Toast.LENGTH_LONG).show();
                                Looper.loop();
                                //Toast.makeText(getActivity().getBaseContext(),"El registro se realizó con exito.",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Ocurrió un error al Registrar sus datos, inténtelo de nuevo más tarde", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.v("Error Log", e.getMessage());
                    }
                }
            });

        }
    }

    private String getFormData(View v){
        EditText nombre = (EditText) v.findViewById(R.id.nombreText);
        EditText apellidos = (EditText) v.findViewById(R.id.apellidosText);
        Spinner sexo = (Spinner) v.findViewById(R.id.sexoSpinner);
        EditText fechaNacimiento = (EditText) v.findViewById(R.id.fechaNacimientoText);
        Spinner colonia = (Spinner) v.findViewById(R.id.coloniaSpinner);
        EditText email = (EditText) v.findViewById(R.id.emailText);
        EditText password = (EditText) v.findViewById(R.id.passwordText);
        EditText passwordConfirm = (EditText) v.findViewById(R.id.confirmarPasswordText);

        String data = "nombre="+nombre.getText().toString().trim();
                data += "&apellidos="+apellidos.getText().toString().trim();
                data += "&sexo="+sexo.getSelectedItem().toString();
                data += "&fecha_nacimiento="+fechaNacimiento.getText().toString().trim();
                data += "&colonia="+colonia.getSelectedItem().toString();
                data += "&Email="+email.getText().toString().trim();
                data += "&Password="+password.getText().toString().trim();

        return data;
    }

    private Boolean validarFormulario(View v){

        Boolean avanzar = true;

        EditText nombre = (EditText) v.findViewById(R.id.nombreText);
        EditText apellidos = (EditText) v.findViewById(R.id.apellidosText);
        Spinner sexo = (Spinner) v.findViewById(R.id.sexoSpinner);
        EditText fechaNacimiento = (EditText) v.findViewById(R.id.fechaNacimientoText);
        Spinner colonia = (Spinner) v.findViewById(R.id.coloniaSpinner);
        EditText email = (EditText) v.findViewById(R.id.emailText);
        EditText password = (EditText) v.findViewById(R.id.passwordText);
        EditText passwordConfirm = (EditText) v.findViewById(R.id.confirmarPasswordText);
        CheckBox chkTerminos = (CheckBox) v.findViewById(R.id.chkTerminos);

        if(nombre.getText().toString().trim().isEmpty()){
            avanzar = false;
        }else if(apellidos.getText().toString().trim().isEmpty()){
            avanzar = false;
        }else if(sexo.getSelectedItem().toString().contentEquals("Selecciona un Sexo")){
            avanzar = false;
        }else if(fechaNacimiento.getText().toString().trim().isEmpty()){
            avanzar = false;
        }else if(colonia.getSelectedItem().toString().contentEquals("Selecciona una Colonia")){
            avanzar = false;
        }else if(email.getText().toString().trim().isEmpty()){
            avanzar = false;
        }else if(password.getText().toString().trim().isEmpty()){
            avanzar = false;
        }else if(passwordConfirm.getText().toString().trim().isEmpty()){
            avanzar = false;
        }

        if(!avanzar){
            this.mostrarAlerta(v, "Datos Incompletos","Favor de completar toda la información");
        }else{
            if(!isEmailValid(email.getText().toString().trim())){
                avanzar = false;
                this.mostrarAlerta(v, "Datos Incompletos","Favor de capturar un Email Válido");
            }else if(!password.getText().toString().trim().contentEquals(passwordConfirm.getText().toString().trim())){
                avanzar = false;
                this.mostrarAlerta(v, "Datos Incompletos","El password capturado no coincide.");
            }else if(password.getText().toString().trim().length() < 6){
                avanzar = false;
                this.mostrarAlerta(v, "Datos Incompletos","El password capturado debe contener mínimo 6 dígitos.");
            }else if(!chkTerminos.isChecked()){
                avanzar = false;
                this.mostrarAlerta(v, "Datos Incompletos", "Para continuar debe aceptar los términos y condiciones.");
            }
        }
        return avanzar;
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void mostrarAlerta(View v, String titulo, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(message);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // aquí puedes añadir funciones
            }
        });
        //alertDialog.setIcon(R.drawable.common_signin_btn_icon_dark);
        alertDialog.show();
    }

    public interface RegistroCallBack {
        public void onRegisterDone(String response, Context context);
    }



}
