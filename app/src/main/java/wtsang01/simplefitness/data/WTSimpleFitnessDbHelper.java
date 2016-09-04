package wtsang01.simplefitness.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wtsang01.simplefitness.mock.WTAuthentication;
import wtsang01.simplefitness.mock.WTSQLiteTestDataInsertion;

/**
 * Created by wtsang01 on 8/30/2016.
 */

public class WTSimpleFitnessDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SimpleFitnessDat5.sqlite";
    public static final String CREATE_USER_TABLE_QUERY =
            "CREATE  TABLE User (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 0, \"email\" TEXT NOT NULL  UNIQUE , \"password\" TEXT NOT NULL )";
    public static final String CREATE_USER_EXTERNAL_TABLE_QUERY =
            "CREATE  TABLE UserExternalBinding (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  DEFAULT 0, \"fk_user_id\"  NOT NULL , \"tw_key\" INTEGER NOT NULL )";
    public static final String CREATE_LOCATION_TABLE_QUERY =
            "CREATE  TABLE Location (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"fk_user_id\" INTEGER NOT NULL , \"location_long\" DOUBLE NOT NULL , \"location_lat\" DOUBLE NOT NULL , \"timestamp\" INTEGER NOT NULL )";

    public static final String INSERT_TEST_USER_QUERY ="INSERT INTO User (\"email\",\"password\") values (\"wtsang01@gmail.com\",\""+WTAuthentication.getSaltedHash("1234")+"\")";
    public static final String INSERT_TEST_USER2_QUERY ="INSERT INTO User (\"email\",\"password\") values (\"wtsang02@nyit.edu\",\""+WTAuthentication.getSaltedHash("5678")+"\")";



    public WTSimpleFitnessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_USER_EXTERNAL_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_LOCATION_TABLE_QUERY);
        sqLiteDatabase.execSQL(INSERT_TEST_USER_QUERY);
        sqLiteDatabase.execSQL(INSERT_TEST_USER2_QUERY);
        WTSQLiteTestDataInsertion test= new WTSQLiteTestDataInsertion(sqLiteDatabase);
        test.addTestUsers();
        test.addTestLocations(100);

    }

    /**
     * Nothing to do here... for now.
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Some logic here if upgrading
        onCreate(sqLiteDatabase);
    }

}
