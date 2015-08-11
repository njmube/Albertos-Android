package mx.com.linkapp.hectorgarcia.albertos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


public class PasswordReminder extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reminder);
    }

    public void Enviar(View v){
        if(this.validarFormulario()) {
            String data = this.getFormData();
            API_LinkApp API = new API_LinkApp();
            API.setContext(v.getContext());
            API.PasswordReminder(data, new PasswordReminderCallBack() {
                @Override
                public void onPasswordDone(String response, Context context) {
                    try {
                        JSONObject json = new JSONObject(response);
                        Boolean success = json.getBoolean("success");
                        Integer code = json.getInt("code");
                        String message = json.getString("message");
                        if (success) {
                            if (code == 0) {
                                Toast.makeText(context, "Te hemos enviado un correo electrónico, sigue los pasos descritos para cambiar tu contraseña.", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(context, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Ocurrió un error al revisar tu email, inténtelo de nuevo más tarde", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.v("Error Log", e.getMessage());
                    }
                }
            });
        }
    }

    private Boolean validarFormulario(){
        Boolean avanzar = true;
        EditText email = (EditText) findViewById(R.id.emailText);
        if(email.getText().toString().trim().isEmpty()){
            avanzar = false;
        }

        if(!avanzar){
            this.mostrarAlerta("Datos Incompletos","Favor de completar toda la información");
        }else{
            if(!isEmailValid(email.getText().toString().trim())){
                avanzar = false;
                this.mostrarAlerta("Datos Incompletos","Favor de capturar un Email Válido");
            }
        }
        return avanzar;
    }

    private String getFormData(){
        EditText email = (EditText) findViewById(R.id.emailText);

        String data = "email="+email.getText().toString().trim();

        return data;
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

    public void Cancelar (View v) {
        super.onBackPressed();
    }

    public interface PasswordReminderCallBack {
        public void onPasswordDone(String response, Context context);
    }
}
