package hitech.beat.simpletracker;

/**
 * Created by beat on 2017-01-05.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class UserFragment extends Fragment implements View.OnClickListener {

    private CheckBox checkBox_agree;
    private ImageView iv_mainImg;
    private EditText et_userName;
    final private String TAG = "UserFragment";

    private static final int DOUBLE_CLICK_DEFENDER = 1000;
    private long LAST_CLICK_TIME = 0;


    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        view.findViewById(R.id.user_linear_layout).setOnTouchListener(onSwipeTouchListener);
        super.onViewCreated(view, savedInstanceState);
        iv_mainImg = (ImageView) view.findViewById(R.id.imgV_user);
        et_userName = (EditText) view.findViewById(R.id.et_userName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        iv_mainImg.setImageBitmap(DataHolder.userProfileBmp);
        et_userName.setText(DataHolder.userName);

    }


    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - LAST_CLICK_TIME < DOUBLE_CLICK_DEFENDER) {
            return;
        }
        LAST_CLICK_TIME = SystemClock.elapsedRealtime();

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
    OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(getActivity()) {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("tagf", "TOUCH");
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

}
