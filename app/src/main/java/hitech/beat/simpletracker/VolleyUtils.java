package hitech.beat.simpletracker;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beat on 2017-01-11.
 */

public class VolleyUtils {

    private static Context mContext;
    private static final String TAG = "Utils";
    private static String base = "http://zupelnieniepotrzebnie.comxa.com/runners/";

    interface Callback {
        public void onSuccess (JSONObject obj);
    }
    public VolleyUtils (Context context) {
        mContext = context;
    }

    public static void sendTrack (final Callback callback) {

        String url = base + "put_track.php?user="+DataHolder.user_Id;
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        try {
            for (int i=0; i<DataHolder.currentTrackLatLng.size(); i++) {
                jo.put("latitude"+i, DataHolder.currentTrackLatLng.get(i).latitude);
                jo.put("longitude"+i, DataHolder.currentTrackLatLng.get(i).longitude);
            }
            jo.put("points", DataHolder.currentTrackPd);
            jo.put("distance", DataHolder.currentTrackDistance);
            jo.put("location", DataHolder.currentTrackLocation);
            jo.put("date", dateFormat.format(new Date()));
            ja.put(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST,  url, ja, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccess(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR: "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("not-typical-header0", "value0123");
                return headers;
            }

        };
        Singleton.getInstance(mContext).addToRequestQueue(jsonRequest);
    }

    public static void getTracks (final Callback callback) {

        String url = base + "get_tracks.php?user="+DataHolder.user_Id;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject track = response.getJSONObject("data");
                    callback.onSuccess(track);

                    //DataHolder.setUpTrack(jaLats, jaLongs, distance, points, location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR: "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("not-typical-header0", "value0123");
                return headers;
            }

        };
        Singleton.getInstance(mContext).addToRequestQueue(jsonObjRequest);

    }
    public static void sendMarker (MarkerOptions markOpt, String category) {

        String url = base + "put_marker.php?user="+DataHolder.user_Id;
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        try {
            jo.put("latitude", markOpt.getPosition().latitude);
            jo.put("longitude", markOpt.getPosition().longitude);
            jo.put("title", markOpt.getTitle());
            jo.put("category", category);
            ja.put(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST,  url, ja, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i(TAG, "RESPONSE: "+response.getJSONObject(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR: "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("not-typical-header0", "value0123");
                return headers;
            }

        };
        Singleton.getInstance(mContext).addToRequestQueue(jsonRequest);
    }
    public static void getWorldMarkers (final Callback callback, final boolean allOfThem) {

        String url;
        if (allOfThem) {
            url = base + "get_worldMarkers.php";
        } else {
            url = base + "get_worldMarkers.php?user="+DataHolder.user_Id;
        }


        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject markers = response.getJSONObject("data");
                    callback.onSuccess(markers);

                    //DataHolder.setUpTrack(jaLats, jaLongs, distance, points, location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR: "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("not-typical-header0", "value0123");
                return headers;
            }

        };
        Singleton.getInstance(mContext).addToRequestQueue(jsonObjRequest);

    }
    public static void getUserTotal (final Callback callback) {

        String url = base +"get_userTotal.php?user="+DataHolder.user_Id;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject userInfo = response.getJSONObject("data");
                    callback.onSuccess(userInfo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR: "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("not-typical-header0", "value0123");
                return headers;
            }

        };
        Singleton.getInstance(mContext).addToRequestQueue(jsonObjRequest);

    }
    public static void ereaseRecord (final Callback callback, int id, boolean track) {

        String url = base +"erease_record.php?user="+DataHolder.user_Id+"&id="+id+"track="+track;

        JsonArrayRequest jsonArrRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jo = new JSONObject().put("ARR", response);
                    callback.onSuccess(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR: "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("not-typical-header0", "value0123");
                return headers;
            }

        };
        Singleton.getInstance(mContext).addToRequestQueue(jsonArrRequest);

    }
}
