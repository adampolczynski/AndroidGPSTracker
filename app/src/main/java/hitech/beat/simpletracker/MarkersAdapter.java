package hitech.beat.simpletracker;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by beat on 2017-01-06.
 */

public class MarkersAdapter extends BaseAdapter {

    ViewHolder vh;
    JSONObject markersInfo;
    int len;


    private final Activity context;
    static class ViewHolder {

        TextView tv_markerTitle;
        TextView tv_markerLoc;
        ImageView iv_marker;
    }
    public MarkersAdapter(Activity context, JSONObject obj, int length) {
        this.context = context;
        markersInfo = obj;
        len = length;
    }

    @Override
    public int getCount() {
        return len;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.i("Adapter", "TracksCounter: "+position);

        if (view == null) {
            Log.i("ADAPTER: VIEW NULL:", "NULL");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gridrow, parent, false);
            vh = new ViewHolder();
            vh.tv_markerTitle = (TextView) view.findViewById(R.id.tv_markerTitle);
            vh.tv_markerLoc = (TextView) view.findViewById(R.id.tv_markerLoc);
            vh.iv_marker = (ImageView) view.findViewById(R.id.iv_markerImg);
            view.setTag(vh);
        } else {
            Log.i("ADAPTER: VIEW NULL:", "NOTNULL");
            vh = (ViewHolder) view.getTag();
        }


        try {
            ArrayList<String> locs = (ArrayList<String>) markersInfo.get("locs");
            ArrayList<String> titles = (ArrayList<String>) markersInfo.get("titles");
            ArrayList<String> cats = (ArrayList<String>) markersInfo.get("categories");
            ArrayList<String> ids = (ArrayList<String>) markersInfo.get("ids");
            vh.tv_markerLoc.setText(locs.get(position));
            vh.tv_markerTitle.setText(titles.get(position));
            Log.i("TAG", cats.get(position));
            if (cats.get(position).equals("Information")) {
                vh.iv_marker.setBackgroundResource(R.drawable.marker_blue);
            } else {
                vh.iv_marker.setBackgroundResource(R.drawable.marker_red);
            }
            int id = Integer.valueOf(ids.get(position));
            MarkersFragment.actualItemsIds.put(position, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }
}

