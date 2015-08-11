package mx.com.linkapp.hectorgarcia.albertos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import mx.com.linkapp.hectorgarcia.albertos.fragments.Registro;


public class Register extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt("UserId", 0);
        String userName = sharedpreferences.getString("Name",null);

        EditText fechaNacimientoText = (EditText) findViewById(R.id.fechaNacimientoText);

        fechaNacimientoText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog();
                    dialog.setView(v);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        fillColoniasFromAPI();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void fillColoniasFromAPI(){
        API_LinkApp API = new API_LinkApp();
        //API.setContext(getApplicationContext());
        List<String> listaColonias = API.getColonias();
        Spinner spinnerColonia = (Spinner) findViewById(R.id.coloniaSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listaColonias);
        spinnerColonia.setAdapter(adapter);

        if(listaColonias.size() <= 1){
            Toast.makeText(getApplicationContext(),"Ocurrió un error al cargar las colonias.",Toast.LENGTH_SHORT).show();
        }
    }

    public void CancelarRegistro (View v) {
        super.onBackPressed();
    }

    public void leerTerminos(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://paneldecontrol.linkapp.com.mx/API/albertos/terminos"));
        startActivity(intent);
    }

    public void RegistrarUsuario(View v){
        if(this.validarFormulario()) {
            String data = this.getFormData();
            API_LinkApp API = new API_LinkApp();
            API.setContext(v.getContext());
            API.registerUser(data, new Registro.RegistroCallBack() {
                @Override
                public void onRegisterDone(String response, Context context) {
                    try {
                        JSONObject json = new JSONObject(response);
                        Boolean success = json.getBoolean("success");
                        Integer code = json.getInt("code");
                        String message = json.getString("message");
                        if (success) {
                            if (code == 0) {
                                Toast.makeText(context, "El registro se realizó con exito. Usa tus datos para iniciar sesión.", Toast.LENGTH_LONG).show();
                                finish();
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

    private String getFormData(){
        EditText nombre = (EditText) findViewById(R.id.nombreText);
        EditText apellidos = (EditText) findViewById(R.id.apellidosText);
        Spinner sexo = (Spinner) findViewById(R.id.sexoSpinner);
        EditText fechaNacimiento = (EditText) findViewById(R.id.fechaNacimientoText);
        Spinner colonia = (Spinner) findViewById(R.id.coloniaSpinner);
        EditText email = (EditText) findViewById(R.id.emailText);
        EditText password = (EditText) findViewById(R.id.passwordText);
        EditText passwordConfirm = (EditText) findViewById(R.id.confirmarPasswordText);

        String data = "nombre="+nombre.getText().toString().trim();
        data += "&apellidos="+apellidos.getText().toString().trim();
        data += "&sexo="+sexo.getSelectedItem().toString();
        data += "&fecha_nacimiento="+fechaNacimiento.getText().toString().trim();
        data += "&colonia="+colonia.getSelectedItem().toString();
        data += "&Email="+email.getText().toString().trim();
        data += "&Password="+password.getText().toString().trim();

        return data;
    }

    private Boolean validarFormulario(){

        Boolean avanzar = true;

        EditText nombre = (EditText) findViewById(R.id.nombreText);
        EditText apellidos = (EditText) findViewById(R.id.apellidosText);
        Spinner sexo = (Spinner) findViewById(R.id.sexoSpinner);
        EditText fechaNacimiento = (EditText) findViewById(R.id.fechaNacimientoText);
        Spinner colonia = (Spinner) findViewById(R.id.coloniaSpinner);
        EditText email = (EditText) findViewById(R.id.emailText);
        EditText password = (EditText) findViewById(R.id.passwordText);
        EditText passwordConfirm = (EditText) findViewById(R.id.confirmarPasswordText);
        CheckBox chkTerminos = (CheckBox) findViewById(R.id.chkTerminos);

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
            this.mostrarAlerta("Datos Incompletos","Favor de completar toda la información");
        }else{
            if(!isEmailValid(email.getText().toString().trim())){
                avanzar = false;
                this.mostrarAlerta("Datos Incompletos","Favor de capturar un Email Válido");
            }else if(!password.getText().toString().trim().contentEquals(passwordConfirm.getText().toString().trim())){
                avanzar = false;
                this.mostrarAlerta("Datos Incompletos","El password capturado no coincide.");
            }else if(password.getText().toString().trim().length() < 6){
                avanzar = false;
                this.mostrarAlerta("Datos Incompletos","El password capturado debe contener mínimo 6 dígitos.");
            }else if(!chkTerminos.isChecked()){
                avanzar = false;
                this.mostrarAlerta("Datos Incompletos", "Para continuar debe aceptar los términos y condiciones.");
            }
        }
        return avanzar;
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void mostrarAlerta(String titulo, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
