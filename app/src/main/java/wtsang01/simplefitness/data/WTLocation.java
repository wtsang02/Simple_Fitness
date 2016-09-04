package wtsang01.simplefitness.data;

import android.location.Location;

import java.util.Iterator;
import java.util.List;

import wtsang01.util.WTLogger;

/**
 * Created by wtsang01 on 9/1/2016.
 */

public class WTLocation {
    private int mId;
    private int mFkUserId;
    private double mLocationLong;
    private double mLocationLat;
    private long mUnixTimestamp;

    public static final float METER_TO_FEET_RATIO = 0.305f;

    public static float getTotalDistance(List<WTLocation> locationList){
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

    /**
     *
     * @param source
     * @param dest
     * @return distance in feet
     */
    public static float getDistanceBetween(WTLocation source,WTLocation dest){
        float[] results = new float[]{0};
        Location.distanceBetween(source.getLocationLat(),source.getLocationLong(),dest.getLocationLat(),dest.getLocationLong(),results);
        return results[0]/METER_TO_FEET_RATIO;
    }
    public Location asLocation(){
        Location location = new Location("");
        location.setLatitude(this.getLocationLat());
        location.setLongitude(this.getLocationLong());
        location.setTime(this.getUnixTimestamp());
        return location;
    }

    @Override
    public String toString() {
        return "WTLocation{" +
                "mFkUserId=" + mFkUserId +
                ", mLocationLong=" + mLocationLong +
                ", mLocationLat=" + mLocationLat +
                ", mUnixTimestamp=" + mUnixTimestamp +
                '}';
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getFkUserId() {
        return mFkUserId;
    }

    public void setFkUserId(int fkUserId) {
        mFkUserId = fkUserId;
    }

    public double getLocationLong() {
        return mLocationLong;
    }

    public void setLocationLong(double locationLong) {
        mLocationLong = locationLong;
    }

    public double getLocationLat() {
        return mLocationLat;
    }

    public void setLocationLat(double locationLat) {
        mLocationLat = locationLat;
    }

    public long getUnixTimestamp() {
        return mUnixTimestamp;
    }

    public void setUnixTimestamp(long unixTimestamp) {
        mUnixTimestamp = unixTimestamp;
    }
}
