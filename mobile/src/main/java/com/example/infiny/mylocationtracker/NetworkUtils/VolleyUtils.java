package com.example.infiny.mylocationtracker.NetworkUtils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.infiny.mylocationtracker.ConfigApp.AppActivity;
import com.example.infiny.mylocationtracker.ConfigApp.Config;
import com.example.infiny.mylocationtracker.Interfaces.NetworkResponse;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by infiny on 21/12/16.
 */

public class VolleyUtils {

    Context context;

    public VolleyUtils(Context context){
        this.context=context;

    }
    public VolleyUtils(){

    }


    public void getDetailedLocation(LatLng location, final NetworkResponse callback, final ErrorVolleyUtils errorCall) {

        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+location.latitude+","+location.longitude+ "&sensor=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response);
                        callback.receiveResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("surroundingPartiesV=", String.valueOf(error));

                        errorCall.onErrorResponse(error);
                    }
                });

        AppActivity.getInstance().addToRequestQueue(request);
    }

    public void sendLocationDetails(final double latit, final double longi, final String timezone_str, final String timezone_id, final NetworkResponse callback, final ErrorVolleyUtils errorCall){


        String url= Config.BASE_URL + "api_store_location";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.receiveResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCall.onErrorResponse(error);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("tracker_id","5");
                params.put("latitude",String.valueOf(latit));
                params.put("longitude", String.valueOf(longi));
                params.put("timezone_str",timezone_str);
                params.put("timezone_id",timezone_id);

                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> mHeaders = new android.support.v4.util.ArrayMap<String, String>();
                mHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                mHeaders.put("Accept", "application/json");

                return mHeaders;
            }

        };

        AppActivity.getInstance().addToRequestQueue(request);
    }



    public void check(final NetworkResponse callback, final ErrorVolleyUtils errorCall){


        String url= Config.BASE_URL + "get_details_of_user";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.receiveResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorCall.onErrorResponse(error);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("tracker_id","5");

                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> mHeaders = new android.support.v4.util.ArrayMap<String, String>();
                mHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                mHeaders.put("Accept", "application/json");

                return mHeaders;
            }

        };

        AppActivity.getInstance().addToRequestQueue(request);
    }



}
