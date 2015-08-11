package mx.com.linkapp.hectorgarcia.albertos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;


public class RegistrarPizza extends ActionBarActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pizza);
        sharedpreferences = getBaseContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        leerQR();
    }

    private void leerQR(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("SCAN_WIDTH", 640);
        integrator.addExtra("SCAN_HEIGHT", 480);
        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
        //customize the prompt message before scanning
        integrator.addExtra("PROMPT_MESSAGE", "Buscando Código QR...");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            final String contents = result.getContents();
            if (contents != null) {
                //showDialog(R.string.result_succeeded, result.toString());
                confirmPizzaCobrada(contents);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Escaneo Incorrecto!", Toast.LENGTH_SHORT);
                toast.show();
                cerrarVentana();
                //showDialog(R.string.result_failed, getString(R.string.result_failed_why));
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Escaneo Incorrecto!", Toast.LENGTH_SHORT);
            toast.show();
            cerrarVentana();
        }
    }

    private void confirmPizzaCobrada(final String contentQR){
        registrarPizzaCobrada(contentQR);
        /*AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder
                .setMessage("El código ha sido detectado, ¿Desea continuar con el registro de la pizza?.")
                .setPositiveButton("SI",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        registrarPizzaCobrada(contentQR);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        cerrarVentana();
                    }
                })
                .show();
                */
    }

    private void registrarPizzaCobrada(String contentQR){
        int userId = sharedpreferences.getInt("UserId", 0);
        String data = "supervisorId="+userId+"&"+contentQR.trim();
        API_LinkApp API = new API_LinkApp();
        API.setContext(getBaseContext());
        API.ponerPizzaCobrada(data, new RegPizzaCallBack() {
            @Override
            public void onRegPizzaDone(String response, Context context) {
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean success = json.getBoolean("success");
                    Integer code = json.getInt("code");
                    String message = json.getString("message");
                    if (success) {
                        if (code == 0) {
                            cerrarVentana();
                        } else {
                            //Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "Ocurrió un error al registrar el premio, inténtelo de nuevo más tarde", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.v("Error Log", e.getMessage());
                }
            }
        });

    }

    private void cerrarVentana(){
        Intent intent = new Intent();
        setResult(RESULT_OK,intent );
        finish();
    }

    public interface RegPizzaCallBack {
        public void onRegPizzaDone(String response, Context context);
    }
}
