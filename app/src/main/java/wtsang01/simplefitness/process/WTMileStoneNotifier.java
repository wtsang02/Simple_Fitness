package wtsang01.simplefitness.process;

import android.content.Context;

import wtsang01.util.WTLogger;


/**
 * Created by wtsang01 on 9/3/2016.
 */

public class WTMileStoneNotifier extends WTNotifier {

    public WTMileStoneNotifier(Context context, int userID) {
        super(context, userID);
    }
    @Override
    public boolean shouldNotify(int userID) {
        WTLogger.l("shouldNotify  Current: "+getMilestone(mCurrentTotal) +" Last: "+ getMilestone(mLastTotal));
        return  getMilestone(mCurrentTotal )>getMilestone(mLastTotal);
    }

    @Override
    public void sendNotification(int userID) {
        String userEmail = mUserDAO.getUserEmail(userID);
        mTitle = "Milestone Reached!";
        mContent = userEmail + "  has reached  " + mUserDAO.getTotalDistanceInFeet(mUserDAO.getUserId(userEmail));
        notificationID = 12345;
        super.sendNotification(userID);
    }


    private int getMilestone(float distance){
        return (int) Math.floor(distance/1000F);
    }

}
