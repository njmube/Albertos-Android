package mx.com.linkapp.hectorgarcia.albertos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import mx.com.linkapp.hectorgarcia.albertos.API_LinkApp;
import mx.com.linkapp.hectorgarcia.albertos.MainActivity;
import mx.com.linkapp.hectorgarcia.albertos.PasswordReminder;
import mx.com.linkapp.hectorgarcia.albertos.R;
import mx.com.linkapp.hectorgarcia.albertos.Register;
import mx.com.linkapp.hectorgarcia.albertos.RegistrarPizza;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    LinearLayout layoutLogin;
    LinearLayout layoutUserLogged;
    View view;


    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.view = view;
        Context context = view.getContext();
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

        layoutLogin = (LinearLayout)view.findViewById(R.id.layoutLogin);
        layoutUserLogged = (LinearLayout)view.findViewById(R.id.layoutUserLogged);

        int userId = sharedpreferences.getInt("UserId", 0);
        String userName = sharedpreferences.getString("Name", null);
        String userEmail = sharedpreferences.getString("Email", null);

        if(userId == 0){
            mostrarLayoutLogin();
        }else{
            mostrarLayoutUserLogged();
            Button btnRegPizza = (Button) view.findViewById(R.id.btnRegPizza);

            int perfil = sharedpreferences.getInt("Perfil", 0);
            if(perfil == 2){ //ES SUPERVISOR
                btnRegPizza.setVisibility(LinearLayout.VISIBLE);
            }else{
                btnRegPizza.setVisibility(LinearLayout.GONE);
            }

            layoutLogin.setVisibility(LinearLayout.VISIBLE);

            TextView NombreUsuario = (TextView) this.view.findViewById(R.id.nombreTextLogged);
            TextView EmailUsuario = (TextView) this.view.findViewById(R.id.emailTextLogged);

            NombreUsuario.setText(userName);
            EmailUsuario.setText(userEmail);
        }

        Button buttonRegistrate = (Button) view.findViewById(R.id.btnRegistrar);
        buttonRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mostrarActivityRegister();
            }
        });

        Button buttonPasswordReminder = (Button) view.findViewById(R.id.btnPasswordRemember);
        buttonPasswordReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mostrarActivityPasswordReminder();
            }
        });

        Button buttonLogin = (Button) view.findViewById(R.id.btnAceptar);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                iniciar_sesion(view);
            }
        });

        Button buttonLogOut = (Button) view.findViewById(R.id.btnLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CierraSesionConfirm();
            }
        });

        Button btnRegPizza = (Button) view.findViewById(R.id.btnRegPizza);
        btnRegPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RegistrarPizza();
            }
        });

        return view;
    }

    private void RegistrarPizza(){
        Intent intent = new Intent(getActivity(),RegistrarPizza.class);
        startActivityForResult(intent, 0);
    }

    private void mostrarLayoutLogin(){
        layoutLogin.setVisibility(LinearLayout.VISIBLE);
        layoutUserLogged.setVisibility(LinearLayout.GONE);
    }

    private void mostrarLayoutUserLogged(){
        layoutLogin.setVisibility(LinearLayout.GONE);
        layoutUserLogged.setVisibility(LinearLayout.VISIBLE);
    }

    private void iniciar_sesion(final View v){
        if(this.validarFormulario(v)) {
            String data = this.getFormData(v);
            API_LinkApp API = new API_LinkApp();
            API.setContext(v.getContext());
            API.login(data, new LoginCallBack() {
                @Override
                public void onLoginDone(String response, Context context) {
                    try {
                        JSONObject json = new JSONObject(response);
                        Boolean success = json.getBoolean("success");
                        Integer code = json.getInt("code");
                        String message = json.getString("message");
                        if (success) {
                            if (code == 0) {
                                int userId = json.getInt("clienteId");
                                String userNombre = json.getString("nombre");
                                String userApellidos = json.getString("apellidos");
                                String userEmail = json.getString("email");
                                int userPerfil = json.getInt("perfil");
                                String NombreCompleto = userNombre+" "+userApellidos;

                                creaSesion(userId, NombreCompleto, userEmail, userPerfil);
                                mostrarMensaje("Bienvenido: " + NombreCompleto);
                            } else {
                                mostrarMensaje("Error: " + message);
                            }
                        } else {
                            mostrarMensaje("Ocurrió un error al Registrar sus datos, inténtelo de nuevo más tarde");
                        }
                    } catch (Exception e) {
                        Log.v("Error Log", e.getMessage());
                    }
                }
            });
        }
    }

    private void creaSesion(final int userId, final String nombre, final String email, final int perfil){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("UserId", userId);
        editor.putString("Name", nombre);
        editor.putString("Email", email);
        editor.putInt("Perfil", perfil);
        editor.commit();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                EditText Email = (EditText) view.findViewById(R.id.emailText);
                EditText Password = (EditText) view.findViewById(R.id.passwordText);
                TextView NombreUsuario = (TextView) view.findViewById(R.id.nombreTextLogged);
                TextView EmailUsuario = (TextView) view.findViewById(R.id.emailTextLogged);

                Email.setText("");
                Password.setText("");
                NombreUsuario.setText(nombre);
                EmailUsuario.setText(email);

                mostrarLayoutUserLogged();
            }
        });

    }

    private void CierraSesionConfirm(){
        final View v = this.view;
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder
                .setMessage("¿Está seguro que desea Cerrar Sesión?")
                .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CierraSesion();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void CierraSesion(){
        Context context = this.view.getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();

        TextView NombreUsuario = (TextView) this.view.findViewById(R.id.nombreTextLogged);
        TextView EmailUsuario = (TextView) this.view.findViewById(R.id.emailTextLogged);

        NombreUsuario.setText("");
        EmailUsuario.setText("");

        mostrarLayoutLogin();
    }

    private void mostrarActivityRegister(){
        try {
            startActivity(new Intent(getActivity(),Register.class));
        }catch (Exception e){
            Log.d("Error aqui", e.getLocalizedMessage());
        }

    }

    private void mostrarActivityPasswordReminder(){
        try {
            startActivity(new Intent(getActivity(),PasswordReminder.class));
        }catch (Exception e){
            Log.d("Error aqui", e.getLocalizedMessage());
        }
    }

    private String getFormData(View v){
        EditText email = (EditText) v.findViewById(R.id.emailText);
        EditText password = (EditText) v.findViewById(R.id.passwordText);

        String data = "email="+email.getText().toString().trim();
        data += "&password="+password.getText().toString().trim();

        return data;
    }

    private Boolean validarFormulario(View v){

        Boolean avanzar = true;

        EditText email = (EditText) v.findViewById(R.id.emailText);
        EditText password = (EditText) v.findViewById(R.id.passwordText);

        if(email.getText().toString().trim().isEmpty()){
            avanzar = false;
        }else if(password.getText().toString().trim().isEmpty()){
            avanzar = false;
        }

        if(!avanzar){
            this.mostrarAlerta(v, "Datos Incompletos","Favor de completar toda la información");
        }else {
            if (!isEmailValid(email.getText().toString().trim())) {
                avanzar = false;
                this.mostrarAlerta(v, "Datos Incompletos", "Favor de capturar un Email Válido");
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

    private void mostrarMensaje(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(view.getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface LoginCallBack {
        public void onLoginDone(String response, Context context);
    }

}
