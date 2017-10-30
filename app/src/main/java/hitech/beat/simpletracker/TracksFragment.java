package hitech.beat.simpletracker;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by beat on 2017-01-06.
 */

public class TracksFragment extends Fragment {

    ListView lv_tracks;
    RelativeLayout tv_slide;
    ImageButton ib_delete;
    int mPosition = -1;
    TracksAdapter runAdapter;
    public static HashMap<Integer, Integer> actualItemsIds = new HashMap<Integer, Integer>();

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public void onStart() {
        super.onStart();



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tracks_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        lv_tracks = (ListView) view.findViewById(R.id.lv_tracks);
        ib_delete = (ImageButton) view.findViewById(R.id.ib_delete);
        tv_slide = (RelativeLayout) view.findViewById(R.id.slideRunners_panel);
        tv_slide.setOnTouchListener(onSwipeTouchListener);
        ib_delete.setOnClickListener(onClickListener);
        lv_tracks.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv_tracks.setSelector(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.colorAccent)));
        lv_tracks.setOnItemClickListener(onItemClickListener);
        getTracks();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            if (mPosition == position) {
                lv_tracks.setItemChecked(position, false);
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
                    getTracks();
                    Toast.makeText(getActivity(), "Track " +actualItemsIds.get(mPosition)+" ereased.", Toast.LENGTH_SHORT).show();
                }
            }, actualItemsIds.get(mPosition), true);

        }
    };
    OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(getActivity()) {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return super.onTouch(v, event);
        }
        @Override
        public void onSwipeBottom() {
        }
        @Override
        public void onSwipeTop() {
        }
        @Override
        public void onSwipeLeft() {
        }
        @Override
        public void onSwipeRight() {
            getFragmentManager().popBackStack();
            Background.slidepanel.startAnimation(Background.slidepanel_in_anim);
            Background.slidepanel.setVisibility(View.VISIBLE);
        }
    };
    private void getTracks () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new VolleyUtils(getActivity()).getTracks(new VolleyUtils.Callback() {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        runAdapter = new TracksAdapter(getActivity(), obj);
                        lv_tracks.setAdapter(runAdapter);
                    }
                });
            }
        }).start();
    }
}
