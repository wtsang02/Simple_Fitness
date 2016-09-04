package wtsang01.simplefitness.mock;

import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import wtsang01.util.WTLogger;

/**
 * Created by wtsang01 on 9/1/2016.
 */

public class WTAuthentication {

    /**
     * No salt here.. make it simple hash.
     */
    public static String getSaltedHash(String password){
        return md5(password);
    }

    public static boolean checkPassword(String inputPassword,String hash){
        WTLogger.l(getSaltedHash(inputPassword)+" is comparing to "+hash);
        return getSaltedHash(inputPassword).equals(hash);
    }


    private static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

}
