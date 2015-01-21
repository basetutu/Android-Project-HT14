package com.malmo_university.mylists;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * This is a BroadcastReceiver class that is responsible for all broadcasts and receiving of
 * broadcasts within the app. This class needs to be initialized once and every object of this
 * class will be merged into one and the same object of this class. (see getActualReference())
 * <p/>
 * Created by Saeed Ghasemi on 30-12-2014.
 */
public class MyBroadcastController extends BroadcastReceiver {
    private static MyBroadcastController mInstance;
    private static Context mAppContext;

    // Not yet tested
    public MyBroadcastController(Context context) {
        if (mAppContext == null) {
            mAppContext = context.getApplicationContext();
            mInstance = new MyBroadcastController(context);
        }
    }

    /**
     * This function will return the actual reference to the class-object instead of using the
     * 'new' operator each time an instance is needed. This requires that this class is already
     * instantiated at least once.
     *
     * @return The actual instance of this class
     */
    public static MyBroadcastController getActualReference() {
        return mInstance;
    }

    /**
     * This function will set the application-context needed internally for this class.
     *
     * @param context Any context will do
     */
    protected static void init(Context context) {
        mAppContext = context.getApplicationContext();
    }

    /**
     * Use this function to make a simple broadcast with a message and action.
     * If more data needs to be transmitted to the receiver, then use the optional
     * broadcastIntent on the overloaded function.
     *
     * @param action  The action to identify the receivers
     * @param message The message to be given the receivers
     */
    public static void sendBroadcast(String action, String message) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra("message", message);
        mAppContext.sendBroadcast(broadcastIntent);
    }

    /**
     * Use this function to make a simple broadcast with a message and action.
     * If more data needs to be transmitted to the receiver, then use the optional
     * broadcastIntent on the overloaded function.
     *
     * @param action  The action to identify the receivers
     * @param message The message to be given the receivers
     */
    public static void sendBroadcast(String action, int message) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra("message", message);
        mAppContext.sendBroadcast(broadcastIntent);
    }

    /**
     * Use this function to make a simple broadcast with a message and action.
     * If more data needs to be transmitted to the receiver, then use the optional
     * broadcastIntent on the overloaded function.
     *
     * @param action          The action to identify the receivers
     * @param message         The message to be given the receivers
     * @param broadcastIntent Extra data can be pu here. (Cannot be null but may be an empty intent)
     */
    public static void sendBroadcast(String action, String message, Intent broadcastIntent) {
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra("message", message);
        mAppContext.sendBroadcast(broadcastIntent);
    }

    /**
     * The actual handling of the intent is done here. This function can be modified as needed.
     *
     * @param context The context making the broadcast
     * @param intent  The broadcast intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Toast.makeText(context, "Received the Intent's message: " + message, Toast.LENGTH_LONG).show();
    }
}
