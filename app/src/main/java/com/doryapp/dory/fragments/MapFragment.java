package com.doryapp.dory.fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.doryapp.dory.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment  implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private boolean mapIsInitialized = false;


    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupGoogleMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_map, container, false);
        setupGoogleMap();
        return v;
    }

    private void setupGoogleMap() {
        if(mapIsInitialized)
            return;
        FragmentManager manager = getChildFragmentManager();
        com.google.android.gms.maps.MapFragment f = (com.google.android.gms.maps.MapFragment) manager.findFragmentById(R.id.gmapfragment);
        f.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapIsInitialized = true;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
    }

}
