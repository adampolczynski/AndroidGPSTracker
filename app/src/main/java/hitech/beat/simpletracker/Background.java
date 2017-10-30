package hitech.beat.simpletracker;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Background extends AppCompatActivity implements View.OnClickListener{

    public FloatingActionButton userImg_fab;
    private static Animation slidepanel_out_anim;
    public static Animation slidepanel_in_anim;
    public static RelativeLayout slidepanel;
    private RelativeLayout slidearea;
    private ImageButton playBut;
    private ImageButton saveBut;
    private ImageButton cancelBut;
    public static TextView tv_distance;
    public static TextView tv_experience;
    private TextView tv_totalDistance;
    private TextView tv_userName;
    private TextView tv_totalPoints;
    private ImageView wheel_slidepanel;

    private static boolean PLAY_CLICKED = false;
    CallbackManager cm;
    LoginButton lb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_background);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        wheel_slidepanel = (ImageView) findViewById(R.id.wheel_panelmid);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_experience = (TextView) findViewById(R.id.tv_experience);
        tv_totalDistance = (TextView) findViewById(R.id.tv_totalDistance);
        tv_totalPoints = (TextView) findViewById(R.id.tv_totalPoints);
        tv_userName = (TextView) findViewById(R.id.tv_user);
        playBut = (ImageButton) findViewById(R.id.play_but);
        saveBut = (ImageButton) findViewById(R.id.save_but);
        cancelBut = (ImageButton) findViewById(R.id.cancel_but);
        saveBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        playBut.setOnClickListener(this);
        userImg_fab = (FloatingActionButton) findViewById(R.id.fab);
        slidepanel_out_anim = AnimationUtils.loadAnimation(this, R.anim.slidepanel_out);
        slidepanel_in_anim = AnimationUtils.loadAnimation(this, R.anim.slidepanel_in);
        slidepanel = (RelativeLayout) findViewById(R.id.slide_panel);
        slidearea = (RelativeLayout) findViewById(R.id.relative_main);
        slidearea.setOnTouchListener(onSwipeTouchListener);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "hitech.beat.simpletracker",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }

        } catch (NoSuchAlgorithmException e ) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        lb = (LoginButton) findViewById(R.id.login_button);
        lb.setReadPermissions("public_profile");
        cm = CallbackManager.Factory.create();
        registerFacebookCallbacks();
        updateWithToken(AccessToken.getCurrentAccessToken());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK},
                    1);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("PERM", "GIT");
                } else {
                    Log.i("PERM", "DENIED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWithToken(AccessToken.getCurrentAccessToken());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateWithToken(AccessToken.getCurrentAccessToken());

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("DISPATCH", "dispatch");
        if (null != getFragmentManager().findFragmentById(R.id.frame_layout)) {
            slidepanel.startAnimation(Background.slidepanel_in_anim);
            slidepanel.setVisibility(View.VISIBLE);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cm.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onClick(View v) {
        if (DataHolder.user_Id == "") {
            if (PLAY_CLICKED) {
                pauseRecord();
            }
            Toast.makeText(getApplicationContext(), "Have to be logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.play_but:
                if (PLAY_CLICKED == false) {
                    startRecord();
                } else {
                    pauseRecord();
                }
                break;
            case R.id.save_but:
                if (DataHolder.currentTrackLatLng.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Track data does not exist.", Toast.LENGTH_SHORT).show();
                } else {
                    saveRace();
                    pauseRecord();
                    setDefaultTexts(true);
                    destroyActualRace();
                }
                break;
            case R.id.cancel_but:
                if (PLAY_CLICKED == true) {
                    pauseRecord();
                    return;
                }
                setDefaultTexts(true);
                destroyActualRace();
                Toast.makeText(getApplicationContext(), "Track data ereased.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void startRecord () {
        Intent i=new Intent("openService");
        i.setClass(this, LocationLoggerServiceManager.class);
        this.sendBroadcast(i);
        animTheWheel();
        Log.i("TAG:", i.toString());
        playBut.setImageResource(android.R.drawable.ic_media_pause);
        PLAY_CLICKED = true;
    }
    private void pauseRecord () {
        Intent x=new Intent("closeService");
        x.setClass(this, LocationLoggerServiceManager.class);
        this.sendBroadcast(x);
        wheel_slidepanel.clearAnimation();
        playBut.setImageResource(android.R.drawable.ic_media_play);
        PLAY_CLICKED = false;

    }
    private void destroyActualRace () {
        DataHolder.clearData();
    }
    private void saveRace () {

        new VolleyUtils(getApplicationContext()).sendTrack(new VolleyUtils.Callback() {
            @Override
            public void onSuccess(JSONObject obj) {
                setDefaultTexts(true);
                Toast.makeText(getApplicationContext(),"Track saved.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(getApplication()) {
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
            if (slidepanel.getVisibility() == View.INVISIBLE) {
                getFragmentManager().popBackStack("panel", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                slidepanel.startAnimation(slidepanel_in_anim);
                slidepanel.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onSwipeRight() {
            Log.d("tagf", "TOUCHRIGHT");
            if (null == getFragmentManager().findFragmentById(R.id.frame_layout)) {
                slidepanel.startAnimation(slidepanel_out_anim);
                slidepanel.setVisibility(View.INVISIBLE);
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.anim_in_panel, R.animator.anim_in_panel, R.animator.anim_out_panel, R.animator.anim_out_panel);
                ft.replace(R.id.frame_layout, PanelFragment.newInstance());
                ft.addToBackStack("panel");
                ft.commit();
            }
        }
    };
    private void registerFacebookCallbacks () {
        new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (null == currentProfile) {
                    return;
                }
                setUserData(currentProfile);
            }
        }.startTracking();

        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        }.startTracking();
    }

    private void setTotalDistance () {
        Log.i("TAG", "SETTOTALDSTANCE"+DataHolder.user_Id);
        new VolleyUtils(getApplicationContext()).getUserTotal(new VolleyUtils.Callback() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    Log.i("TAG", "SETTOTALDSTANCE:"+obj.getDouble("distance"));
                    tv_totalDistance.setText(String.format(Locale.US,"%.2f", obj.getDouble("distance")));
                    tv_totalPoints.setText(String.format(Locale.US,"%.2f", obj.getDouble("points")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void animTheWheel () {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1500);
        anim.setRepeatCount(Animation.INFINITE);
        wheel_slidepanel.startAnimation(anim);
    }

    private void updateWithToken (AccessToken token) {
        if (null == token) {
            if (PLAY_CLICKED) {
                pauseRecord();
                destroyActualRace();
            }
            unsetUserData();
        } else {
            setUserData(Profile.getCurrentProfile());
        }
    }
    private void unsetUserData () {
        Log.i("UNUSERDATA", "ID");
        DataHolder.user_Id = "";
        DataHolder.userName = getString(R.string.not_logged);
        tv_userName.setText(getString(R.string.not_logged));
        userImg_fab.setImageDrawable(null);
        setDefaultTexts(false);
    }
    private void setUserData (Profile profile) {
        if (null == profile) {
            return;
        }
        if (DataHolder.user_Id == "") {
            Log.i("USERDATA", "ID:"+profile.getId());
            DataHolder.user_Id = profile.getId();
            DataHolder.userName = profile.getName();
            tv_userName.setText(profile.getName());
            getProfilePicture();
            setTotalDistance();
        }
    }
    private void getProfilePicture () {
        Log.i("TAG", DataHolder.user_Id+"<=ID");
        if (DataHolder.user_Id != "") {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try { // http://graph.facebook.com/1355934961113038/picture?type=large

                        Log.i("TAG","http://graph.facebook.com/"+ Profile.getCurrentProfile().getId()+"/picture?type=large");

                        //final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        URL url = new URL("https://graph.facebook.com/"+DataHolder.user_Id+"/picture?type=large");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setInstanceFollowRedirects(true);
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        final Bitmap bmp = BitmapFactory.decodeStream(input);
                        DataHolder.userProfileBmp = bmp;
                        userImg_fab.post(new Runnable() {
                            @Override
                            public void run() {
                                RoundedBitmapDrawable roundedBmp = RoundedBitmapDrawableFactory.create(getResources(), DataHolder.userProfileBmp);
                                roundedBmp.setCircular(true);
                                userImg_fab.setImageDrawable(roundedBmp);
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private void setDefaultTexts (boolean onlyCurrentTrack) {
        if (onlyCurrentTrack) {
            tv_distance.setText(getString(R.string.current_distance)+"\n"+String.format(Locale.US,"%.2f", 0.00));
            tv_experience.setText(getString(R.string.current_points)+"\n"+String.format(Locale.US,"%.2f", 0.00));
        } else {
            tv_distance.setText(getString(R.string.current_distance)+"\n"+String.format(Locale.US,"%.2f", 0.00));
            tv_experience.setText(getString(R.string.current_points)+"\n"+String.format(Locale.US,"%.2f", 0.00));
            tv_totalDistance.setText(String.format(Locale.US,"%.2f", 0.00));
            tv_totalPoints.setText(String.format(Locale.US,"%.2f", 0.00));
        }


    }
}
