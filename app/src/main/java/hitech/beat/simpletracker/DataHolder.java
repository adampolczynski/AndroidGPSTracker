package hitech.beat.simpletracker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by beat on 2017-01-09.
 */

public class DataHolder {

    public static String user_Id = "";
    public static String currentTrackLocation = "currentTrackLoc";
    public static String currentTrackDistance = "0.0";
    public static String currentTrackPd = "0.0";
    public static PolylineOptions polyOpt = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);;
    public static ArrayList<LatLng> currentTrackLatLng = new ArrayList<LatLng>();
    public static float[] results = new float[1];
    public static String userName = "";
    public static Bitmap userProfileBmp = null;

    public static void updateData (LatLng latLng, String loc) {
        currentTrackLatLng.add(latLng);
        currentTrackLocation = loc;
        setDistance();
        polyOpt.add(latLng);
    }
    public static void setDistance () {

        Location.distanceBetween(currentTrackLatLng.get(0).latitude, currentTrackLatLng.get(0).longitude,
                currentTrackLatLng.get(currentTrackLatLng.size()-1).latitude, currentTrackLatLng.get(currentTrackLatLng.size()-1).longitude, results);
        Log.i("Dystans", results[0] + ":");
        currentTrackDistance = String.format(Locale.US,"%.2f", results[0]);
        currentTrackPd = String.format(Locale.US,"%.2f", results[0]*(2.3+15)/1.3);

    }
    public static void clearData() {
        polyOpt = null;
        polyOpt = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        results = new float[1];
        currentTrackLatLng = new ArrayList<LatLng>();
    }
    public static boolean isTrackRecording () {
        if (currentTrackLatLng.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
