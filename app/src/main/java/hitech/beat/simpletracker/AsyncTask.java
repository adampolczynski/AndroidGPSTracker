package hitech.beat.simpletracker;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 2016-11-17.
 */
// sciaga dla nas obrazy przy pomocy GetGrid.java, nastepnie sciaga bitmapy przekazujac do adaptera jako liste bitmap
public class AsyncTask extends android.os.AsyncTask<Void, Integer, Void> {

    private Context mContext;
    private int length;
    private List<Address> adr;
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> locs = new ArrayList<String>();
    private ArrayList<String> ids = new ArrayList<String>();
    private ArrayList<String> cats = new ArrayList<String>();
    private JSONArray jaLats;
    private JSONArray jaLongs;
    private JSONArray jaCats;
    private JSONArray jaIds;
    private JSONArray jaTitles;
    Geocoder geocoder;
    JSONObject markersInfo;
    GridView gv_markers;

    public AsyncTask (Context context, GridView gv) {
        mContext = context;
        gv_markers = gv;
        //title = info.get(1);
        //loc = info.get(0);
    }
    @Override
    protected Void doInBackground(Void... params) {
        markersInfo = new JSONObject();
        geocoder = new Geocoder(mContext, Locale.getDefault());


        new VolleyUtils(mContext).getWorldMarkers(new VolleyUtils.Callback() {
            @Override
            public void onSuccess(JSONObject obj) {

                try {
                    jaTitles = obj.getJSONArray("titles");
                    jaLats = obj.getJSONArray("lats");
                    jaLongs = obj.getJSONArray("longs");
                    jaCats = obj.getJSONArray("categories");
                    jaIds = obj.getJSONArray("ids");
                    for (int i=0;i<jaLats.length();i++) {
                        adr = geocoder.getFromLocation(jaLats.getDouble(i), jaLongs.getDouble(i), 1);
                        titles.add(jaTitles.get(i).toString());
                        locs.add(adr.get(0).getAddressLine(1)+",\n"+adr.get(0).getAddressLine(0));
                        cats.add(jaCats.getString(i));
                        ids.add(String.valueOf(jaIds.get(i)));
                    }
                    markersInfo.put("titles", titles);
                    markersInfo.put("locs", locs);
                    markersInfo.put("ids", ids);
                    markersInfo.put("categories", cats);
                    length = locs.size();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onPostExecute(null);
                //JSONArray jaCats = obj.getJSONArray("cats");

            }
        }, false);


        return null; // zwraca pusta liste przed response
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        gv_markers.setAdapter(new MarkersAdapter(
                (Activity)mContext, markersInfo, length));
        super.onPostExecute(aVoid);
    }

}
