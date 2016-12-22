package com.example.asdf.test.attached;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Useradmin on 2016/11/8.
 */
public class activityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }
    public static void finishAll() {
        for (Activity activity : activities) {
              if (!activity.isFinishing()) {
                  activity.finish();
              }
        }
  }
}
