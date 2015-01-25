package com.malmo_university.mylists.Controllers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Meant to keep all functions related to the SharedPreference in one place.
 *
 * Created by Saeed on 08-01-15.
 */
public class SharedPreferencesController {
    private static Context mAppContext;
    private static String mFilename;
    private static SharedPreferences mPrefs;

    public static void init(Context context, String filename){
        mAppContext = context.getApplicationContext();
        mFilename = filename;
        mPrefs = mAppContext.getSharedPreferences(mFilename, 0);
    }

    protected static SharedPreferences getSharedPreferencesPrivate(){
        return mAppContext.getSharedPreferences(mFilename, 0);
    }

    // Simplified methods ///////////////////////////////////////////////////////////////////////

    // Integer

    protected static void simpleDeletePersistentInt(String key){
        mPrefs.edit().remove(key).commit();
    }

    protected static void simpleWritePersistentInt(String key, int nbr){
        mPrefs.edit().putInt(key, nbr).commit();
    }

    protected static int simpleReadPersistentInt(String key, int defValue){
        return mPrefs.getInt(key, defValue);
    }

    // String

    public static String simpleDeletePersistentString(String key){
        String temp = mPrefs.getString(key, null);
        mPrefs.edit().remove(key).commit();
        return temp;
    }

    public static void simpleWritePersistentString(String key, String data){
        mPrefs.edit().putString(key, data).commit();
    }

    public static String simpleReadPersistentString(String key){
        return mPrefs.getString(key, null);
    }

    // Boolean

    protected static void simpleDeletePersistentBoolean(String key){
        mPrefs.edit().remove(key).commit();
    }

    protected static void simpleWritePersistentBoolean(String key, boolean state){
        mPrefs.edit().putBoolean(key, state).commit();
    }

    protected static boolean simpleReadPersistentBoolean(String key, boolean defValue){
        return mPrefs.getBoolean(key, defValue);
    }

}
