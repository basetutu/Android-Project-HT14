package com.malmo_university.mylists;

/**
 * Created by Martin on 12-01-2015.
 */
public class ThreadController {

    protected static void delay(long millisecond){
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
