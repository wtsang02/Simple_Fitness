package wtsang01.simplefitness.process;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import wtsang01.simplefitness.R;
import wtsang01.simplefitness.WTSimpleFitnessActivity;
import wtsang01.simplefitness.data.WTUserDAO;
import wtsang01.util.WTLogger;

/**
 * Created by wtsang01 on 9/3/2016.
 */

public abstract class WTNotifier {
    protected float mLastTotal = -1f;
    protected float mCurrentTotal = -1f;
    protected Context mContext;
    protected WTUserDAO mUserDAO;

    protected String mTitle;
    protected String mContent;
    protected int notificationID;

    protected WTNotifier(Context context, int userID) {
        mContext = context;
        mUserDAO = new WTUserDAO(context);
        updateTotals(mUserDAO.getTotalDistanceInFeet(userID));
    }

    abstract boolean shouldNotify(int userID);
    public void updateTotals( int userID) {
        mLastTotal = mCurrentTotal;
        mCurrentTotal = mUserDAO.getTotalDistanceInFeet(userID);
    }
    public void updateTotals(float currentTotal) {
        mLastTotal = mCurrentTotal;
        mCurrentTotal = currentTotal;
    }
    public void sendNotification(int userID) {
        WTLogger.l(this.getClass().getSimpleName()+"sendNotification");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_plusone_medium_off_client)
                        .setContentTitle(mTitle)
                        .setContentText(mContent);
        int NOTIFICATION_ID = notificationID;
        Intent targetIntent = new Intent(mContext, WTSimpleFitnessActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }
}
