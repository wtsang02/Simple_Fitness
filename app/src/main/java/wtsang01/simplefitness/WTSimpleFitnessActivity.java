package wtsang01.simplefitness;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import wtsang01.simplefitness.process.WTFitnessLocationService;
import wtsang01.simplefitness.process.WTStaleLocationBroadcastReceiver;
import wtsang01.util.WTLogger;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.google.android.gms.analytics.internal.zzy.C;
import static com.google.android.gms.internal.zznk.fm;
import static java.security.AccessController.getContext;
import static wtsang01.simplefitness.process.WTFitnessLocationService.REQUEST_CODE;


/**
 * A login screen that offers login via email/password.
 */
public class WTSimpleFitnessActivity extends AppCompatActivity implements WTLoginFragment.OnFragmentLoginSuccessListener, WTFitnessFragment.OnLogoutListener {
    Fragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            mFragment = fm.findFragmentByTag(WTLoginFragment.TAG);
            if (mFragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                mFragment = WTLoginFragment.newInstance();
                ft.add(android.R.id.content, mFragment, WTLoginFragment.TAG);
                ft.commit();
            }
        }
    }

    @Override
    public void onLoginSuccess(int userID) {
        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentByTag(WTFitnessFragment.TAG);
        if (mFragment == null) {
            cleanServices();
            FragmentTransaction ft = fm.beginTransaction();
            mFragment = WTFitnessFragment.newInstance(userID);
            ft.replace(android.R.id.content, mFragment, WTFitnessFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public void onLogout() {
        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentByTag(WTLoginFragment.TAG);
        if (mFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            mFragment = WTLoginFragment.newInstance();
            ft.replace(android.R.id.content, mFragment, WTLoginFragment.TAG);
            ft.commit();
        }
    }

    public void cleanServices(){
        if(isFitnessLocationServiceRunning(this)) {
            final Intent intent = new Intent(getBaseContext(), WTFitnessLocationService.class);
            stopService(intent);
        }
        stopStaleLocationAlarm();
    }
    private void stopStaleLocationAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(getBaseContext(), WTStaleLocationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE,i,PendingIntent.FLAG_NO_CREATE);
        boolean alarmUp = pendingIntent != null;
        if(alarmUp){
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }
    }

    public static boolean isFitnessLocationServiceRunning(Activity activity){
        return isServiceRunning(activity,WTFitnessLocationService.class);
    }
    public static boolean isServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

