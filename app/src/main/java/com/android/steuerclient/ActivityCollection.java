package com.android.steuerclient;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： zh
 * @Date： 20/12/20
 * @description remove all Activity, when exit the app
 */
public class ActivityCollection {

        private static List<Activity> activityList = new ArrayList<>();

        public static void addActivity(Activity activity) {
            activityList.add(activity);
        }

        public static void removeActivity(Activity activity) {
            activityList.remove(activity);
        }

        public static void removeAll()
        {
            for (Activity activity :activityList)
            {
                if (!activity.isFinishing())
                {
                    activity.finish();
                }
            }
        }

}
