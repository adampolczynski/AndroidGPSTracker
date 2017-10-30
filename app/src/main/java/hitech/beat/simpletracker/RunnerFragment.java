package hitech.beat.simpletracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

/**
 * Created by beat on 2017-01-06.
 */

public class RunnerFragment extends Fragment {

    public static RunnerFragment newInstance() {
        return new RunnerFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.runner_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        TableLayout inflatedLay = (TableLayout) view.findViewById(R.id.table_tracks);
        int i;
        for (i=0; i<6; i++) {
            View inflate = View.inflate(getActivity(), R.layout.single_row, null);
            inflate.setTag(i);
            inflatedLay.addView(inflate);
        }
        view.setOnTouchListener(onSwipeTouchListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
