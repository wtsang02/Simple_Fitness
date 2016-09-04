package wtsang01.util;

import android.util.Log;

import static android.R.id.list;

/**
 * Created by wtsang01 on 9/1/2016.
 */

public class WTLogger{
    private static final String TAG = WTLogger.class.getSimpleName();
    public static  <M> void l(M obj){
        Log.i(TAG,obj.toString());
    }
    public static  <M> void l(Iterable<M> list){
        for (Object aObj:list) {
            Log.i(TAG,aObj.toString());
        }
    }
}
