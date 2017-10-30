package hitech.beat.simpletracker;

/**
 * Created by beat on 2017-01-14.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class SettingsFragment extends Fragment {

    ListView lv_tracks;
    RelativeLayout tv_slide;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

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
}
