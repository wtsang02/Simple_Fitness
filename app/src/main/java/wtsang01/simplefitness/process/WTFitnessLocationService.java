package wtsang01.simplefitness.process;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import wtsang01.simplefitness.WTFitnessFragment;
import wtsang01.simplefitness.WTLoginFragment;
import wtsang01.simplefitness.data.WTUserDAO;
import wtsang01.util.WTLogger;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by wtsang01 on 9/2/2016.
 */

public class WTFitnessLocationService extends Service {
    private static final String TAG = WTFitnessLocationService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 5000;
    private WTUserDAO mUserDAO;
    private int mUserID;
    private WTMileStoneNotifier mNotifier;
    private float mPreviousDistance =0;
    private static final int REQUEST_CODE=685643345;
    private static final float LOCATION_DISTANCE = 10f;
    /**
     * This setting should really be under shared preferences, and updatable by user...
     * but lets just set it here to save time. ALso this is in FPS = feet per second.
     */
    public static final float WALK_SPEED_MAX = 7.33f;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {

            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            mUserDAO.logLocation(mUserID,mLastLocation, System.currentTimeMillis()/1000L);
            mNotifier.updateTotals(mUserDAO.getTotalDistanceInFeet(mUserID));
            if(mNotifier.shouldNotify(mUserID)){
                mNotifier.sendNotification(mUserID);
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WTLogger.l("onStartCommand");
        if(intent == null){
            Log.wtf(TAG,"intent is null ?!?");
        }else{
            mUserID=intent.getIntExtra(WTFitnessFragment.USER_ID_EXTRA,-1);
        }
        mUserDAO = new WTUserDAO(getBaseContext());
        mNotifier = new WTMileStoneNotifier(getBaseContext(),mUserID);
        mNotifier.updateTotals(mUserDAO.getTotalDistanceInFeet(mUserID));
        registerStaleAlarmManager();

        WTLogger.l(mUserID+"   has started the service");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    @Override
    public void onCreate() {
        WTLogger.l("onCreate");

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, WALK_SPEED_MAX,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        unregisterStaleAlarmManager();
        mUserDAO.close();
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (SecurityException se) {
                    se.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }
    private PendingIntent getStaleLocationAlarmPendingIntent(int flag){
        Intent i = new Intent(getBaseContext(), WTStaleLocationBroadcastReceiver.class);
        i.putExtra(WTFitnessFragment.USER_ID_EXTRA,mUserID);
        WTLogger.l("Putting userid to boardcast reciever intent , userid: "+mUserID);
       return PendingIntent.getBroadcast(getBaseContext(),REQUEST_CODE, i, flag);

    }
    private void registerStaleAlarmManager(){
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 3 * 1000;
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                1000*10, getStaleLocationAlarmPendingIntent(FLAG_UPDATE_CURRENT));//10sec interval
    }
    private void unregisterStaleAlarmManager(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = getStaleLocationAlarmPendingIntent(FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}
