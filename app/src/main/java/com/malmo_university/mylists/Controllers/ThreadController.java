package com.malmo_university.mylists.Controllers;

/**
 * Created by Martin on 12-01-2015.
 */
public class ThreadController {

    public static void delay(long millisecond){
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
