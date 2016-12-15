package itutorgroup.h2h;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static Stack<Activity> activityStack = new Stack<>();

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        return Holder.instance;
    }

    public int getActivitySize() {
        return activityStack.size();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 获取指定activity，没有返回null
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束指定的Activity
     */
    private void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
	/*public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}*/

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Activity temp = null;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                temp = activity;
                break;
            }
        }
        finishActivity(temp);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishOtherActivity(Class<?> cls) {
        Activity activity;
        for (int i = 0; i < activityStack.size(); i++) {
            activity = activityStack.get(i);
            if (null != activity && !activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     * 需要权限android.permission.KILL_BACKGROUND_PROCESSES
     */
    public void AppExit(Context context) {
        try {
//			MobclickAgent.onKillProcess(mContext);
//            UserPF.getInstance().logout();
            finishAllActivity();
//			ActivityManager activityMgr = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//			activityMgr.killBackgroundProcesses(mContext.getPackageName());
//			android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Holder {
        static AppManager instance = new AppManager();
    }
}