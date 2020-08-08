package com.example.broadcasttest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

//工具类,可以统一来管理所有的Activity
public class ActiyityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity: activities){
            if (!activity.isFinishing())
                activity.finish();
        }
    }
}

