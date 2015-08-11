package mx.com.linkapp.hectorgarcia.albertos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mx.com.linkapp.hectorgarcia.albertos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class user_logged extends Fragment {


    public user_logged() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_logged, container, false);
        Context context = view.getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt("UserId", 0);
        String userName = sharedpreferences.getString("Name", null);

        if(userId > 0){
            EditText Nombre = (EditText) view.findViewById(R.id.nombreText);
            Nombre.setText(userName);
        }

        Button buttonLogOut = (Button) view.findViewById(R.id.btnLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                logout(v);
            }
        });

        return view;
    }

    private void logout(final View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder
            .setMessage("¿Está seguro que desea Cerrar Sesión?")
            .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Context context = v.getContext();
                    SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
            })
            .show();
    }
}
