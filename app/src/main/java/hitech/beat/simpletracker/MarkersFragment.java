package hitech.beat.simpletracker;

/**
 * Created by beat on 2017-01-14.
 */

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;


public class MarkersFragment extends Fragment {

    RelativeLayout tv_slide;
    GridView gv_markers;
    ImageButton ib_delete;
    int mPosition = -1;
    public static HashMap<Integer, Integer> actualItemsIds = new HashMap<Integer, Integer>();

    public static MarkersFragment newInstance() {
        return new MarkersFragment();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.markers_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        tv_slide = (RelativeLayout) view.findViewById(R.id.slideMarkers_panel);
        gv_markers = (GridView) view.findViewById(R.id.gv_markers);
        ib_delete = (ImageButton) view.findViewById(R.id.ib_deleteMarker);
        tv_slide.setOnTouchListener(onSwipeTouchListener);
        gv_markers.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        gv_markers.setSelector(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.runners1)));
        gv_markers.setOnItemClickListener(onItemClickListener);
        ib_delete.setOnClickListener(onClickListener);
        getMarkers();
    }
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

            if (mPosition == position) {
                gv_markers.setItemChecked(position, false);
                mPosition = -1;
            }
            mPosition = position;

        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPosition == -1) {
                return;
            }
            new VolleyUtils(getActivity()).ereaseRecord(new VolleyUtils.Callback() {
                @Override
                public void onSuccess(JSONObject obj) {
                    getMarkers();
                    Toast.makeText(getActivity(), "Marker " +actualItemsIds.get(mPosition)+" ereased.", Toast.LENGTH_SHORT).show();
                }
            }, actualItemsIds.get(mPosition), false);

        }
    };
    OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(getActivity()) {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("tagf", "TOUCH");
            return super.onTouch(v, event);
        }

        @Override
        public void onSwipeBottom() {
            //dismiss();
            super.onSwipeBottom();
        }
        @Override
        public void onSwipeTop() {
            //dismiss();
            super.onSwipeBottom();
        }
        @Override
        public void onSwipeLeft() {

        }
        @Override
        public void onSwipeRight() {
            Log.d("tagf", "TOUCHRIGHT");

            getFragmentManager().popBackStack();
            Background.slidepanel.startAnimation(Background.slidepanel_in_anim);
            Background.slidepanel.setVisibility(View.VISIBLE);

        }
    };
    private void getMarkers () {
        new AsyncTask(getActivity(), gv_markers).execute();
    }
}
