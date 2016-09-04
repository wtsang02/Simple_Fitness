package wtsang01.simplefitness.process;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import wtsang01.simplefitness.R;
import wtsang01.simplefitness.WTSimpleFitnessActivity;
import wtsang01.util.WTLogger;

import static com.google.android.gms.analytics.internal.zzy.F;
import static com.google.android.gms.analytics.internal.zzy.m;

/**
 * Created by wtsang01 on 9/3/2016.
 */

public class WTStaleLocationNotifier extends WTNotifier{

    protected static final float STALE_LOCATION_THRESHOLD= 10F;
    private static final String TAG =  WTStaleLocationNotifier.class.getSimpleName();


    public WTStaleLocationNotifier(Context context, int userID){
        super(context,userID);
    }

    @Override
    public boolean shouldNotify(int userID) {
        WTLogger.l("userID: "+userID+"shouldNotify  "+ (mCurrentTotal-mLastTotal) +" compared to "+ STALE_LOCATION_THRESHOLD);
        return (mCurrentTotal-mLastTotal)<STALE_LOCATION_THRESHOLD;

    }
    @Override
    public void sendNotification(int userID) {
        String userEmail = mUserDAO.getUserEmail(userID);
        mTitle = "Stale detected!";
        mContent =userEmail + "  hasn't moved in a while... ";
        notificationID = 45678;
        super.sendNotification(userID);
    }
}
