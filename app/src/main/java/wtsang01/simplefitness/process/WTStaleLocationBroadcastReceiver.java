package wtsang01.simplefitness.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import wtsang01.simplefitness.WTFitnessFragment;
import wtsang01.util.WTLogger;

import static com.google.android.gms.analytics.internal.zzy.i;

/**
 * Created by wtsang01 on 9/3/2016.
 */

public class WTStaleLocationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int userID=-1;

        if(intent.hasExtra(WTFitnessFragment.USER_ID_EXTRA)){
            WTLogger.l("StaleLocation receiver got user: "+userID);
            userID = intent.getIntExtra(WTFitnessFragment.USER_ID_EXTRA,-1);
        }else{
            throw new RuntimeException("Someone sending this should put their name on it...");
        }
        WTLogger.l("StaleLocation receiver got user: "+userID);
        WTStaleLocationNotifier notifier = new WTStaleLocationNotifier(context,userID);
        WTLogger.l("userID after creating notifier :"+userID);
        notifier.updateTotals(userID);
        if(notifier.shouldNotify(userID)){
            notifier.sendNotification(userID);
        }
    }
}
