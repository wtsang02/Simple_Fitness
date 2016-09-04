package wtsang01.simplefitness.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wtsang01 on 9/1/2016.
 *
 * This dao should encapsulate the helper and the db. THen provide base methods for subclass dao.
 */

public class WTFitnessDAO {
    static final String ID_COL = "_id";
    static final String EMAIL_COL = "email";
    static final String PWD_COL = "password";
    static final String TW_KEY_COL = "tw_key";
    static final String FK_USER_ID_COL = "fk_user_id";
    static final String LOCATION_LONG_COL = "location_long";
    static final String LOCATION_LAT_COL = "location_lat";
    static final String TIMESTAMP_COL = "timestamp";
    static final String USER_TB = "User";
    static final String LOCATION_TB="Location";
    static final String USER_EXTERNAL_BINDING_TB="UserExternalBinding";

    protected SQLiteOpenHelper mHelper;
    protected Context mContext;
    public WTFitnessDAO(Context context){
        mContext = context;
        mHelper = new WTSimpleFitnessDbHelper(mContext);
    }

    public void close(){
        mHelper.close();
    }



}
