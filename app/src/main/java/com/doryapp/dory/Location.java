package com.doryapp.dory;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;


public class Location {
    String currentCityName;
    LatLng currentCityPosition = new LatLng(0,0);
    String currentCityFbId;
    String currentCityCountry;


    public static Location FromJSON(JSONObject location) throws JSONException {
        Location loc = new Location();

        JSONObject subLocation = (JSONObject)location.get("location");
        loc.currentCityName = subLocation.getString("city");
        loc.currentCityFbId = location.getString("id");
        loc.currentCityCountry = subLocation.getString("country");
        loc.currentCityPosition = new LatLng(subLocation.getDouble("latitude"),subLocation.getDouble("longitude"));

        return loc;
    }
}
