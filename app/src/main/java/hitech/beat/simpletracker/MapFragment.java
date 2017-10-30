package hitech.beat.simpletracker;

/**
 * Created by beat on 2017-01-05.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleMap googleMap;
    private LocationRequest mLocationRequest;
    final private String TAG = "MapsFragment";
    public static int REQ_CODE = 1;
    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_maps_fragment, container, false);
        final MapView mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerDialog dialog = new MarkerDialog();
                        Bundle args = new Bundle();
                        args.putDoubleArray("latLng", new double[] {latLng.latitude, latLng.longitude});
                        dialog.setArguments(args);
                        dialog.setTargetFragment(MapFragment.this, REQ_CODE);
                        dialog.show(getFragmentManager(), "dialog");
                    }
                });
                redrawLine(null);
                getWorldMarkers();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void makeToast (final String content) {
        final Activity activity = getActivity();
        if (activity !=null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQ_CODE) {
            addMarker(data);
        }
        Log.i(TAG, data.getStringArrayExtra("markerData")[0]+" : "+resultCode);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (DataHolder.isTrackRecording()) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(24).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        redrawLine(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient,
                //mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void addMarker (Intent data) {
        MarkerOptions newMarkOpt;
        LatLng newMarkerLatLng = new LatLng(data.getDoubleArrayExtra("markerLatLng")[0], data.getDoubleArrayExtra("markerLatLng")[1]);
        String title = data.getStringArrayExtra("markerData")[0];
        String category = data.getStringArrayExtra("markerData")[1];
        if (category.equals("Information")) {
            newMarkOpt = new MarkerOptions().position(newMarkerLatLng).draggable(false).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.marker_blue)));
        } else {
            newMarkOpt = new MarkerOptions().position(newMarkerLatLng).draggable(false).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.marker_red)));
        }
        newMarkOpt.title(title);
        googleMap.addMarker(newMarkOpt);
        new VolleyUtils(getActivity()).sendMarker(newMarkOpt, category);
    }

    private void redrawLine(Location loc){
        if (null == loc) {
            if (DataHolder.isTrackRecording()) {
                googleMap.addPolyline(DataHolder.polyOpt);
            }
        } else {
            DataHolder.updateData(new LatLng(loc.getLatitude(), loc.getLongitude()), DataHolder.currentTrackLocation);
        }
    }
    private void getWorldMarkers () {
        new VolleyUtils(getActivity()).getWorldMarkers(new VolleyUtils.Callback() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    JSONArray jaTitles = obj.getJSONArray("titles");
                    JSONArray jaLats = obj.getJSONArray("lats");
                    JSONArray jaLongs = obj.getJSONArray("longs");
                    JSONArray jaCats = obj.getJSONArray("categories");
                    String category;
                    for (int i=0;i<jaTitles.length();i++) {
                        category = jaCats.getString(i);
                        if (category.equals("Information")) {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(jaLats.getDouble(i), jaLongs.getDouble(i)))
                                    .draggable(false).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.marker_blue)))
                                    .title(jaTitles.getString(i)));
                        } else {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(jaLats.getDouble(i), jaLongs.getDouble(i)))
                                    .draggable(false).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.marker_red)))
                                    .title(jaTitles.getString(i)));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, true);
    }
    public boolean isGooglePlayServicesAvailable(Context context){
        if (null == context) {
            return false;
        }
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }
}
