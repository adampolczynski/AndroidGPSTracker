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

/**
 * Created by beat on 2017-01-06.
 */

public class TracksAdapter extends BaseAdapter {
    String distance;
    String points;
    String location;
    String date;
    int id;
    ViewHolder vh;
    JSONObject track;
    private final Activity context;

    static class ViewHolder {
        ImageView iv;
        TextView tv_loc;
        TextView tv_dist;
        TextView tv_pts;
        TextView tv_date;
    }
    public TracksAdapter(Activity context, JSONObject obj) {
        this.context = context;
        track = obj;
    }

    @Override
    public int getCount() {
        int counted =0;
        try {
            counted = track.getJSONArray("loc").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return counted;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
            vh = new ViewHolder();
            vh.tv_loc = (TextView) view.findViewById(R.id.tv_loc);
            vh.tv_dist = (TextView) view.findViewById(R.id.tv_dist);
            vh.tv_pts = (TextView) view.findViewById(R.id.tv_pts);
            vh.tv_date = (TextView) view.findViewById(R.id.tv_date);
            view.setTag(vh);

        } else {
            vh = (ViewHolder) view.getTag();
        }
        try {
            distance = track.getJSONArray("dist").get(position).toString();
            points = track.getJSONArray("points").get(position).toString();
            location = track.getJSONArray("loc").get(position).toString();
            date = track.getJSONArray("date").get(position).toString();
            id = track.getJSONArray("ids").getInt(position);
            vh.tv_loc.setText(location);
            vh.tv_dist.setText(distance);
            vh.tv_pts.setText(points);
            vh.tv_date.setText(date);
            TracksFragment.actualItemsIds.put(position, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}

