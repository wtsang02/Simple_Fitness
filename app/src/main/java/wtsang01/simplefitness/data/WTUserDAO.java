package wtsang01.simplefitness.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wtsang01.simplefitness.mock.WTAuthentication;
import wtsang01.util.WTLogger;


/**
 * Created by wtsang01 on 9/1/2016.
 */

public class WTUserDAO extends WTFitnessDAO {

    protected static final String INSERT_LOCATION_QUERY="INSERT INTO "+LOCATION_TB+" ("
            +"\""+FK_USER_ID_COL+"\", "
            +"\""+LOCATION_LAT_COL+"\", "
            +"\""+LOCATION_LONG_COL+"\", "
            +"\""+TIMESTAMP_COL+"\") VALUES (?,?,?,?)";
    static final String SELECT_All_LOCATION_QUERY="SELECT * FROM "+LOCATION_TB+" WHERE "+EMAIL_COL+" = ?";
    protected static final int NEG_SEC_OFFSET_REPORTING = 24 * 60 * 60; //1 DAY in seconds.
    public WTUserDAO(Context context){
        super(context);
    }

    public boolean login(String userEmail,String password){
        return WTAuthentication.checkPassword(password,getPasswordHash(userEmail));
    }
    private String getPasswordHash(String userEmail){
       Cursor c = super.mHelper.getReadableDatabase().query(USER_TB, null,
                EMAIL_COL+"= ?",
                new String[] {userEmail},
                null, null, null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c.getString(c.getColumnIndex(PWD_COL));
        }else{
            //Serves the purpose of this simple app.
            // Certainly can do more exception handling instead of returning empty string.
        return "";
        }
    }

    public void logLocation(int userID,Location location,long unixTimestamp){
        WTLogger.l("logLocation");
        SQLiteStatement statement= mHelper.getReadableDatabase().compileStatement(INSERT_LOCATION_QUERY);
        statement.bindDouble(1,userID);
        statement.bindDouble(2,location.getLatitude());
        statement.bindDouble(3,location.getLongitude());
        statement.bindLong(4,unixTimestamp);
        statement.executeInsert();
    }
    public float getFeetWalkToday(int userID){
        long unixTimeReporting = System.currentTimeMillis() / 1000L -NEG_SEC_OFFSET_REPORTING;
        List<WTLocation> list = getLocationsBasedOnUnixTime(userID,unixTimeReporting);
        return getTotalDistanceInFeet(list);
    }
    public float getTotalDistanceInFeet(int userID){
        return getTotalDistanceInFeet(getLocations(userID));
    }
    public float getTotalDistanceInFeet(List<WTLocation> locationList){
        Iterator<WTLocation> it = locationList.iterator();
        WTLocation previous;
        float totalFeet = 0f;
        if(it.hasNext()){
            previous = it.next();
            while(it.hasNext()){
                WTLocation current = it.next();
                totalFeet += WTLocation.getDistanceBetween(previous,current);
                previous = current;

            }
        }

        return totalFeet;
    }
    protected List<WTLocation> getLocationsBasedOnUnixTime(int userID,float unixTimeReporting){
        List<WTLocation> list = getLocations(userID);
        Iterator<WTLocation> it= list.iterator();
        while(it.hasNext()){
            WTLocation location = it.next();
            if(location.getUnixTimestamp() < unixTimeReporting){
                it.remove();
            }
        }
        return list;
    }

    public List<WTLocation> getLocations(int userID){
        List<WTLocation> returnList = new ArrayList<WTLocation>();
        Cursor c = super.mHelper.getReadableDatabase().query(LOCATION_TB, null,
                FK_USER_ID_COL+"=?",
                new String[] {Integer.toString(userID)},
                null,null, null);
        if(c.getCount()>0){
            while(c.moveToNext()){
                WTLocation location = new WTLocation();
                location.setId(c.getInt(c.getColumnIndex(ID_COL)));
                location.setFkUserId(c.getInt(c.getColumnIndex(FK_USER_ID_COL)));
                location.setLocationLong(c.getDouble(c.getColumnIndex(LOCATION_LONG_COL)));
                location.setLocationLat(c.getDouble(c.getColumnIndex(LOCATION_LAT_COL)));
                location.setUnixTimestamp(c.getLong(c.getColumnIndex(TIMESTAMP_COL)));
                returnList.add(location);
            }

        }
        c.close();
        return returnList;
    }
    public List<Integer> getAllUserId(){
        List<Integer> userIDList = new ArrayList<Integer>();
        Cursor c = super.mHelper.getReadableDatabase().query(USER_TB, null,
                null,
                null,
                null, null, null);
        if(c.moveToFirst()){
            do{
               userIDList.add(c.getInt(c.getColumnIndex(ID_COL)));
            } while(c.moveToNext());
        }
        return userIDList;

    }
    public String getUserEmail(int userID){
        Cursor c = super.mHelper.getReadableDatabase().query(USER_TB, null,
                ID_COL+"= ?",
                new String[] {Integer.toString(userID)},
                null, null, null);
        if(c.getCount()==1){
            c.moveToFirst();
            String userEmail = c.getString(c.getColumnIndex(EMAIL_COL));
            c.close();
            return userEmail;
        }else {
            c.close();
            throw new IllegalArgumentException("Not user id match. Args:userID:"+userID);


        }

    }
    public int getUserId(String userEmail){

            Cursor c = super.mHelper.getReadableDatabase().query(USER_TB, null,
                    EMAIL_COL+"= ?",
                    new String[] {userEmail},
                    null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                int ID = c.getInt(c.getColumnIndex(ID_COL));
                c.close();
                return ID;
            }else {
                c.close();
                //Serves the purpose of this simple app.
                // Certainly can do more exception handling instead of returning -1.
                return -1;

            }

    }




}
