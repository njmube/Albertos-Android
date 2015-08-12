package mx.com.linkapp.hectorgarcia.albertos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;
import org.w3c.dom.Text;

import mx.com.linkapp.hectorgarcia.albertos.API_LinkApp;
import mx.com.linkapp.hectorgarcia.albertos.MainActivity;
import mx.com.linkapp.hectorgarcia.albertos.PideTuPizza;
import mx.com.linkapp.hectorgarcia.albertos.R;

/**
 * Created by hectorgarcia on 03/07/15.
 */
public class Premios extends Fragment {
    View view;
    LinearLayout layoutPizzaIncompleta;
    LinearLayout layoutPizzaCompleta;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    int contadorVisitas = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_premios, container, false);
        this.view = view;
        final Context context = view.getContext();

        layoutPizzaIncompleta = (LinearLayout)view.findViewById(R.id.layoutPizzaIncompleta);
        layoutPizzaCompleta = (LinearLayout)view.findViewById(R.id.layoutPizzaCompleta);

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Button btnCapturaCodigo = (Button)view.findViewById(R.id.btnCapturaTuCodigo);
        btnCapturaCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PedirCodigo(view);
            }
        });

        Button btnPideTuPizza = (Button)view.findViewById(R.id.btnPideTuPizza);
        btnPideTuPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MostrarPideTuPizza(view);

            }
        });

        if(isLogged()){
            btnCapturaCodigo.setVisibility(LinearLayout.VISIBLE);
            traerVisitasDelCliente();
        }else{
            layoutPizzaIncompleta.setVisibility(LinearLayout.VISIBLE);
            layoutPizzaCompleta.setVisibility(LinearLayout.GONE);
            btnCapturaCodigo.setVisibility(LinearLayout.GONE);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        traerVisitasDelCliente();
    }

    private void MostrarPideTuPizza(final View view){
        try {
            //startActivity(new Intent(getActivity(),PideTuPizza.class));
            Intent intent = new Intent(getActivity(),PideTuPizza.class);
            startActivityForResult(intent, 0);
        }catch (Exception e){
            Log.d("Error aqui", e.getLocalizedMessage());
        }
    }

    private void PedirCodigo(final View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        final EditText edittext= new EditText(view.getContext());
        alert.setTitle("Captura de Código");
        alert.setMessage("Capture el código que se le entregó junto con su ticket de compra.");


        alert.setView(edittext);

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                Editable codigoCapturado = edittext.getText();
                validarCodigo(codigoCapturado.toString());
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void validarCodigo(String codigo){
        int userId = sharedpreferences.getInt("UserId", 0);
        String data = "codigo="+codigo.trim()+"&cliente_id="+userId;
        API_LinkApp API = new API_LinkApp();
        API.setContext(view.getContext());
        API.ValidarCodigo(data, new ValidarCodigoCallBack() {
            @Override
            public void onValidarDone(String response, Context context) {
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean success = json.getBoolean("success");
                    Integer code = json.getInt("code");
                    String message = json.getString("message");
                    if (success) {
                        if (code == 0) {
                            mostrarMensaje("El registro del código se ha realizado con éxito. Gracias por su visita.");
                            contadorVisitas++;
                            cambiarVisitas();
                        } else {
                            mostrarMensaje("Error: " + message);
                        }
                    } else {
                        mostrarMensaje("Ocurrió un error al validar el código, inténtelo de nuevo más tarde");
                    }
                } catch (Exception e) {
                    Log.v("Error Log", e.getMessage());
                }
            }
        });
    }

    private void mostrarMensaje(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(view.getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cambiarVisitas(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int imagePizza;
                TextView contador = (TextView) view.findViewById(R.id.textNumberVisits);
                ImageView imgPizza = (ImageView) view.findViewById(R.id.img_pizza);

                contador.setText(String.valueOf(contadorVisitas));
                switch (contadorVisitas) {
                    case 1:
                        imagePizza = R.drawable.pizza_1;
                        break;
                    case 2:
                        imagePizza = R.drawable.pizza_2;
                        break;
                    case 3:
                        imagePizza = R.drawable.pizza_3;
                        break;
                    case 4:
                        imagePizza = R.drawable.pizza_4;
                        break;
                    case 5:
                        imagePizza = R.drawable.pizza_5;
                        break;
                    case 6:
                        imagePizza = R.drawable.pizza_6;
                        break;
                    case 7:
                        imagePizza = R.drawable.pizza_7;
                        break;
                    case 8:
                        imagePizza = R.drawable.pizza_8;
                        break;
                    default:
                        imagePizza = R.drawable.pizza_0;
                        break;
                }

                imgPizza.setImageResource(imagePizza);
                estaLaPizzaCompleta();
            }
        });

    }

    private void traerVisitasDelCliente(){
        int userId = sharedpreferences.getInt("UserId", 0);
        String data = "cliente_id="+userId;
        API_LinkApp API = new API_LinkApp();
        API.setContext(view.getContext());
        API.VisitasPorCliente(data, new NumberoVisitasCallBack() {
            @Override
            public void onNumeroVisitasDone(String response, Context context) {
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean success = json.getBoolean("success");
                    Integer code = json.getInt("code");
                    String message = json.getString("message");
                    Integer visitas = json.getInt("visitas");
                    if (success) {
                        if (code == 0) {
                            contadorVisitas = visitas;
                            cambiarVisitas();
                        } else {
                            mostrarMensaje("Error: " + message);
                        }
                    } else {
                        mostrarMensaje("Ocurrió un error al traer el número de visitas, inténtelo de nuevo más tarde");
                    }
                } catch (Exception e) {
                    Log.v("Error Log", e.getMessage());
                }
            }
        });


    }

    private void estaLaPizzaCompleta() {
        if (contadorVisitas == 8) {
            layoutPizzaIncompleta.setVisibility(LinearLayout.GONE);
            layoutPizzaCompleta.setVisibility(LinearLayout.VISIBLE);
        } else {
            layoutPizzaIncompleta.setVisibility(LinearLayout.VISIBLE);
            layoutPizzaCompleta.setVisibility(LinearLayout.GONE);
        }


    }

    private Boolean isLogged(){
        int userId = sharedpreferences.getInt("UserId", 0);
        if(userId == 0){
            return false;
        }else {
            return true;
        }
    }

    public interface ValidarCodigoCallBack {
        public void onValidarDone(String response, Context context);
    }

    public interface NumberoVisitasCallBack {
        public void onNumeroVisitasDone(String response, Context context);
    }

}
