package mx.com.linkapp.hectorgarcia.albertos;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import mx.com.linkapp.hectorgarcia.albertos.fragments.Login;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Premios;
import mx.com.linkapp.hectorgarcia.albertos.fragments.Registro;

/**
 * Created by hectorgarcia on 06/07/15.
 */
public class API_LinkApp{
    private Registro.RegistroCallBack registroCallBack;
    private Login.LoginCallBack loginCallBack;
    private PasswordReminder.PasswordReminderCallBack passwordCallBack;
    private Premios.ValidarCodigoCallBack   valCodigoCallBack;
    private Premios.NumberoVisitasCallBack numeroVisitasCallBack;
    private RegistrarPizza.RegPizzaCallBack premioCobradoCallBack;

    private static final String urlAPI = "http://paneldecontrol.linkapp.com.mx/API";
    //let urlAPI: String = "http://localhost/panellinkapp/public/API"
    //private static final String appKey = "A65TmKz#!98";
    private static final String appKey = "A65TmKz";
    private static final Integer puntoVentaId = 1;
    private static final Integer idRandom = 0;

    private View rootView;
    private Context context;

    public void setContext(Context c){
        //Toast.makeText(c, "El registro se realizó con exito.", Toast.LENGTH_LONG).show();
        context = c;
    }



    private String generateToken(){
        String toHash = this.appKey+this.puntoVentaId+this.idRandom;
        return toHash;
    }

    public List<String> getColonias() {
        String method = "/colonias";
        String url = urlAPI+method;
        //this.invoke(url, 1);
        return this.getColoniasSinc(url);
    }

    public void registerUser(String data, Registro.RegistroCallBack regCallBack){
        this.registroCallBack = regCallBack;

        String params = "token="+this.generateToken()+"&r="+this.idRandom+"&punto_venta_id="+this.puntoVentaId+"&"+data;
        try {
            params = URLEncoder.encode(params, "utf-8");
            params = params.replaceAll("\\+", "%20");
            params = params.replaceAll("%3D", "=");
            params = params.replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String method = "/register";
        String url = urlAPI+method+"?"+params;
        this.invoke(url,2);
    }

    public void login(String data, Login.LoginCallBack loginCallBack){
        this.loginCallBack = loginCallBack;

        String params = "token="+this.generateToken()+"&r="+this.idRandom+"&punto_venta_id="+this.puntoVentaId+"&"+data;
        try {
            params = URLEncoder.encode(params, "utf-8");
            params = params.replaceAll("\\+", "%20");
            params = params.replaceAll("%3D", "=");
            params = params.replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String method = "/login";
        String url = urlAPI+method+"?"+params;
        this.invoke(url,3);
    }

    public void PasswordReminder(String data, PasswordReminder.PasswordReminderCallBack passwordCallBack){
        this.passwordCallBack = passwordCallBack;

        String params = "token="+this.generateToken()+"&r="+this.idRandom+"&punto_venta_id="+this.puntoVentaId+"&"+data;
        try {
            params = URLEncoder.encode(params, "utf-8");
            params = params.replaceAll("\\+", "%20");
            params = params.replaceAll("%3D", "=");
            params = params.replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String method = "/passwordReminder";
        String url = urlAPI+method+"?"+params;
        this.invoke(url,4);
    }

    public void ValidarCodigo(String data, Premios.ValidarCodigoCallBack callBack){
        this.valCodigoCallBack = callBack;

        String params = "token="+this.generateToken()+"&r="+this.idRandom+"&punto_venta_id="+this.puntoVentaId+"&"+data;
        try {
            params = URLEncoder.encode(params, "utf-8");
            params = params.replaceAll("\\+", "%20");
            params = params.replaceAll("%3D", "=");
            params = params.replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String method = "/validarcodigo";
        String url = urlAPI+method+"?"+params;
        this.invoke(url,5);
    }

    public void VisitasPorCliente(String data, Premios.NumberoVisitasCallBack callBack){
        this.numeroVisitasCallBack = callBack;

        String params = "token="+this.generateToken()+"&r="+this.idRandom+"&punto_venta_id="+this.puntoVentaId+"&"+data;
        try {
            params = URLEncoder.encode(params, "utf-8");
            params = params.replaceAll("\\+", "%20");
            params = params.replaceAll("%3D", "=");
            params = params.replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String method = "/numeroDevisitas";
        String url = urlAPI+method+"?"+params;
        this.invoke(url,6);
    }

    public void ponerPizzaCobrada(String data, RegistrarPizza.RegPizzaCallBack callBack){
        this.premioCobradoCallBack = callBack;

        String params = "token="+this.generateToken()+"&r="+this.idRandom+"&punto_venta_id="+this.puntoVentaId+"&"+data;
        try {
            params = URLEncoder.encode(params, "utf-8");
            params = params.replaceAll("\\+", "%20");
            params = params.replaceAll("%3D", "=");
            params = params.replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String method = "/premioCobrado";
        String url = urlAPI+method+"?"+params;
        this.invoke(url,7);
    }

    private List<String> getColoniasSinc(String uri){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line = reader.readLine()) != null){
                sb.append(line +"\n");
            }
            return this.procesarColonias(sb.toString());
        }catch (Exception e){
            Log.e("Mi app", "Error al leer: " + e.getMessage());
            List<String> listaColonias = new ArrayList<String>();
            return listaColonias;
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Mi app", "Error al leer 2: " + e.getMessage());
                    List<String> listaColonias = new ArrayList<String>();
                    return listaColonias;
                }
            }
        }
    }

    private List<String> procesarColonias(String responseColonias) {
        List<String> listaColonias = new ArrayList<String>();
        listaColonias.add("Selecciona una Colonia");
        try {
            JSONObject json = new JSONObject(responseColonias);
            //String success = json.getJSONObject("message").toString();
            Boolean success = json.getBoolean("success");
            if(success){
                JSONArray arrayColonias = json.getJSONArray("colonias");
                if (arrayColonias != null) {
                    for (int i=0;i<arrayColonias.length();i++){
                        JSONObject jsonColonia = arrayColonias.optJSONObject(i);
                        listaColonias.add(jsonColonia.getString("Colonia"));
                    }
                }
            }
        }catch (Exception e){
            Log.e("MYAPP", "unexpected JSON exception", e);
        }

        return listaColonias;
    }

    private void invoke(final String url, final Integer tipoCallBack){
        HttpAsyncTask asyncTask = new HttpAsyncTask(new FragmentCallback() {
            @Override
            public void onTaskDone(String response, Context context) {
                //Aqui procesar callBack
                switch (tipoCallBack){
                    case 1:
                        procesarColonias(response);
                        break;
                    case 2:
                        registroCallBack.onRegisterDone(response, context);
                        break;
                    case 3:
                        loginCallBack.onLoginDone(response, context);
                        break;
                    case 4:
                        passwordCallBack.onPasswordDone(response,context);
                        break;
                    case 5:
                        valCodigoCallBack.onValidarDone(response, context);
                        break;
                    case 6:
                        numeroVisitasCallBack.onNumeroVisitasDone(response, context);
                        break;
                    case 7:
                        premioCobradoCallBack.onRegPizzaDone(response, context);
                        break;
                }
            }
        }, context);
        asyncTask.execute(url);
    }

    private void procesarRegistro(String response, Context context){
        try {
            JSONObject json = new JSONObject(response);
            Boolean success = json.getBoolean("success");
            Integer code = json.getInt("code");
            String message = json.getString("message");
            if(success){
                if(code == 0){
                    //Looper.prepare();
                    Toast.makeText(context, "El registro se realizó con exito.", Toast.LENGTH_LONG).show();
                    //Looper.loop();
                    //Toast.makeText(getActivity().getBaseContext(),"El registro se realizó con exito.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"Error: "+message,Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context,"Ocurrió un error al Registrar sus datos, inténtelo de nuevo más tarde",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.v("Error Log",e.getMessage());
        }
    }



    /*public boolean isOnline_downloadList() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            //sending request for login
            new MyAsyncTask().execute(getFromPreference("student_code"));



            return true;
        }

        //alert box to show internet connection error
        AlertDialog.Builder Internet_Alert = new AlertDialog.Builder(SpeedTestExamNameActivity.this);
        // set title
        Internet_Alert.setCancelable(false);
        Internet_Alert.setTitle("Attention!");
        Internet_Alert.setMessage("This application requires internet connectivity, no internet connection detected");
        Internet_Alert.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                onQuitPressed();
            }
        });

        Internet_Alert.create().show();
        return false;
    }*/

    public interface FragmentCallback {
        public void onTaskDone(String response, Context context);
    }
}
