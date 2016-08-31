package wtsang01.simplefitness.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wtsang01 on 8/30/2016.
 */

public class WTSimpleFitnessDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SimpleFitnessDat.db";
    public static final String CREATE_DB_QUERY =
            "CREATE  TABLE \"main\".\"User\" (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 0, \"email\" TEXT NOT NULL  UNIQUE , \"password\" TEXT NOT NULL )";
    public static final String CREATE_USER_EXTERNAL_TABLE_QUERY =
            "CREATE  TABLE \"main\".\"UserExternalBinding\" (\"_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  DEFAULT 0, \"tw_key\" INTEGER NOT NULL )";
    public WTSimpleFitnessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public WTSimpleFitnessDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

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

    }
}
