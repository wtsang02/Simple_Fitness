package wtsang01.simplefitness.mock;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.Random;

import wtsang01.util.WTLogger;
import wtsang01.util.WTRandom;

import static com.google.android.gms.analytics.internal.zzy.d;

/**
 * Created by wtsang01 on 9/3/2016.
 */

public class WTSQLiteTestDataInsertion {

    /**
     * Add a bunch more user to display better in leader board
     */
    public static final String INSERT_TEST_USER3_QUERY ="INSERT INTO User (\"email\",\"password\") values (\"jpmc@gmail.edu\",\""+WTAuthentication.getSaltedHash("6634")+"\")";
    public static final String INSERT_TEST_USER4_QUERY ="INSERT INTO User (\"email\",\"password\") values (\"icecream@gmail.edu\",\""+WTAuthentication.getSaltedHash("543")+"\")";
    public static final String INSERT_TEST_USER5_QUERY ="INSERT INTO User (\"email\",\"password\") values (\"keyboard@gmail.edu\",\""+WTAuthentication.getSaltedHash("522678")+"\")";
    public static final String INSERT_TEST_USER6_QUERY ="INSERT INTO User (\"email\",\"password\") values (\"helloworld@gmail.edu\",\""+WTAuthentication.getSaltedHash("3554")+"\")";
    public static final int TWELVE_HR_IN_SEC = 43200;
    public static final String INSERT_TEST_LOCATION_QUERY ="INSERT INTO \"main\".\"Location\" (\"fk_user_id\",\"location_long\",\"location_lat\",\"timestamp\") VALUES (?,?,?,?)";

    private static final double MOCK_LAT_BASE = 31.24346d;
    private static final double MOCK_LONG_BASE= -124.4125d;
    private static final double MOCK_MAX_LOCATION_OFFSET=0.01d;
    SQLiteDatabase mDB;
    public WTSQLiteTestDataInsertion(SQLiteDatabase db){
        mDB=db;

    }

    public void addTestUsers(){
        mDB.execSQL(INSERT_TEST_USER3_QUERY);
        mDB.execSQL(INSERT_TEST_USER4_QUERY);
        mDB.execSQL(INSERT_TEST_USER5_QUERY);
        mDB.execSQL(INSERT_TEST_USER6_QUERY);
    }
    public void addTestLocations( int amount){
        for(int i=0;i<amount;i++){
            addTestLocations();
        }
    }
    void addTestLocations(){
        SQLiteStatement statement= mDB.compileStatement(INSERT_TEST_LOCATION_QUERY);
        statement.bindDouble(1, WTRandom.getRandomIntWithRange(1,7));
        statement.bindDouble(2,WTRandom.getRandomDoubleWithRange(MOCK_LONG_BASE,MOCK_MAX_LOCATION_OFFSET));
        statement.bindDouble(3,WTRandom.getRandomDoubleWithRange(MOCK_LAT_BASE,MOCK_MAX_LOCATION_OFFSET));
        statement.bindLong(4,getTestUnixTime());
        statement.executeInsert();

    }
    long getTestUnixTime(){
        return (System.currentTimeMillis()/1000L-TWELVE_HR_IN_SEC)- WTRandom.getRanNumber(4);
    }
}
