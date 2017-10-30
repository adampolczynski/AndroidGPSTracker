package hitech.beat.simpletracker;

/**
 * Created by beat on 2017-01-05.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class PanelFragment extends Fragment implements View.OnClickListener {

    private static ImageButton wheel_panel;
    private static Button but_user;
    private static Button but_maps;
    private static Button but_tracks;
    private static Button but_markers;
    private static Button but_settings;


    public static PanelFragment newInstance() {
        return new PanelFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.panel_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wheel_panel = (ImageButton) view.findViewById(R.id.wheel_panel);
        wheel_panel.setOnClickListener(this);
        view.findViewById(R.id.button).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button3).setOnClickListener(this);
        view.findViewById(R.id.button4).setOnClickListener(this);
        view.findViewById(R.id.button5).setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        android.app.FragmentTransaction ft;
        if (DataHolder.user_Id == "") {
            Toast.makeText(getActivity(), "Have to logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {

            case R.id.button:
                Log.d("Tag", "UserFragment");
                getFragmentManager().popBackStack("panel", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.animator.anim_in, R.animator.anim_in, R.animator.anim_out, R.animator.anim_out);
                ft.replace(R.id.frame_layout, UserFragment.newInstance());
                ft.addToBackStack(null);
                ft.commit();

                break;
            case R.id.button2:
                Log.d("Tag", "MapFragment");
                getFragmentManager().popBackStack("panel", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.animator.anim_in, R.animator.anim_in, R.animator.anim_out, R.animator.anim_out);
                    ft.replace(R.id.frame_layout, MapFragment.newInstance());
                ft.addToBackStack("maps");
                    ft.commit();

                break;
            case R.id.button3:
                Log.d("Tag", "TracksFragment");
                getFragmentManager().popBackStack("panel", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.animator.anim_in, R.animator.anim_in, R.animator.anim_out, R.animator.anim_out);
                ft.replace(R.id.frame_layout, TracksFragment.newInstance());
                ft.addToBackStack(null);
                ft.commit();

                break;
            case R.id.button4:
                Log.d("Tag", "MarkersFragment");
                getFragmentManager().popBackStack("panel", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.animator.anim_in, R.animator.anim_in, R.animator.anim_out, R.animator.anim_out);
                ft.replace(R.id.frame_layout, MarkersFragment.newInstance());
                ft.addToBackStack(null);
                ft.commit();

                break;
            case R.id.button5:
                Log.d("Tag", "SettingsFragment");
                getFragmentManager().popBackStack("panel", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.animator.anim_in, R.animator.anim_in, R.animator.anim_out, R.animator.anim_out);
                ft.replace(R.id.frame_layout, SettingsFragment.newInstance());
                ft.addToBackStack(null);
                ft.commit();

                break;
        }
    }
}
