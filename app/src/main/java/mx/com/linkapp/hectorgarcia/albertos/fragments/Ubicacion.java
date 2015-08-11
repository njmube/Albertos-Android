package mx.com.linkapp.hectorgarcia.albertos.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

import mx.com.linkapp.hectorgarcia.albertos.R;

/**
 * Created by hectorgarcia on 03/07/15.
 */
public class Ubicacion extends Fragment {

    GoogleMap googleMap;
    MapView mapView;

    /*@Override
    public void onResume(){
        super.onResume();
        //mapView.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
        //mapView.onPause();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*mapView = (MapView) getView().findViewById(R.id.mi_mapa);
        mapView.onCreate(savedInstanceState);

        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ubicacion, container, false);
    }

}
