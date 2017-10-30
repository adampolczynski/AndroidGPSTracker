package hitech.beat.simpletracker;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by beat on 2017-01-09.
 */

public class LocationLoggerServiceManager extends BroadcastReceiver{
    private SharedPreferences mPrefs;
    public static final String TAG = "LLoggerServiceManager";
    private static ComponentName service;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure we are getting the right intent
        if( "closeService".equals(intent.getAction())) {
            if (null != service) {
                Log.i(TAG, "BroadcastReceiver: "+intent.getAction()+", Service: "+service);
                ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());
                context.stopService(new Intent().setComponent(comp));
            }

        }
        if( "openService".equals(intent.getAction())) {

            if (null == service) {
                Log.i(TAG, "BroadcastReceiver: "+intent.getAction()+", Service: "+service);
                ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());
                service = context.startService(new Intent().setComponent(comp));
                return;
            }
            boolean mUpdatesRequested = false;
            // Open the shared preferences
            mPrefs = context.getSharedPreferences(
                    Constants.APP_PACKAGE_NAME, Context.MODE_PRIVATE);
	        /*
	         * Get any previous setting for location updates
	         * Gets "false" if an error occurs
	         */
            if (mPrefs.contains(Constants.RUNNING)) {
                mUpdatesRequested = mPrefs.getBoolean(Constants.RUNNING, false);
            }
            Log.i(TAG, "BroadcastReceiver: "+intent.getAction()+", Service: "+service);
            Log.i(TAG, "Broadcast: "+mUpdatesRequested);
            if(!mUpdatesRequested) {
                ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());
                context.startService(new Intent().setComponent(comp));

            }
            if(mUpdatesRequested){
                ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());
                ComponentName service = context.startService(new Intent().setComponent(comp));

                if (null == service){
                    // something really wrong here
                    Log.e(TAG, "Could not start service " + comp.toString());
                }
            }

        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}
