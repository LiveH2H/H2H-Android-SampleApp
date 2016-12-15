package com.meetingroom.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 *
 *
 * 获取手机系统信息 添加权限
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 */
public class SystemUtil {

    private static boolean isTablet = false;

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics outMetrics = getDisplayMetrics(activity);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics outMetrics = getDisplayMetrics(activity);
        return outMetrics.heightPixels;
    }

    /**
     *
     * 获取应用签名Hash
     *
     * @param context
     * @return
     */
    public static String getSignatureHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getApplicationLabel(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
    }

    /**
     * 判断是否安装了某app
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkAppExist(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0, size = pinfo.size(); i < size; i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
//		try {
//			mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
//		} catch (PackageManager.NameNotFoundException e) {
//			return false;
//		}
        return false;
    }

    /**
     * 打开应用程序,如果应用未安装,将在应用商店下载页显示
     *
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 是否安装了微信
     *
     * @param mContext
     * @return
     */
    /*public static boolean checkWX(Context mContext) {
		return checkApkExist(mContext, "com.tencent.mm");
	}*/

    /**
     * 获取 app版本号
     *
     * @param mContext
     * @param packageName
     *            包名 如"cn.testgethandsetinfo"
     * @return
     */
	/*public static String getVersion(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}*/

    /**
     * 显示在应用商店
     *
     * @param context
     * @param packageName
     */
    public static boolean showAppStore(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前应用开发的版本号
     */
    public static int getVersionCode(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
	}

    /**
     * 获取当前应用开发的版本名称
     */
    public static String getVersionName(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取系统版版号
     *
     * @return
     */
	/*public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}*/

    // /**
    // * 获取应用图标
    // * @param mContext
    // * @return
    // */
    // public static Drawable getAppIcon(Context mContext){
    //
    // PackageManager packageManager = null;
    // ApplicationInfo applicationInfo = null;
    // try {
    // packageManager =mContext. getApplicationContext().getPackageManager();
    // applicationInfo =
    // packageManager.getApplicationInfo(mContext.getPackageName(), 0);
    //
    // } catch (PackageManager.NameNotFoundException e) {
    // e.printStackTrace();
    // }
    //
    // return applicationInfo.loadIcon(packageManager);
    //
    //
    // }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 获取imei
     *
     * @param mContext
     * @return
     */
	/*public static String getIMEI(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}*/

    /** 获取本机ip */
	/*public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
						Matcher m = p.matcher(inetAddress.getHostAddress().toString());
						if (m.matches())
							return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("slg", "获取本机ip失败：" + ex.toString());
		}
		return "";
	}*/

    /**
     * 退出程序
     */
	/*public static void exitApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}*/

    /**
     * .获取手机MAC地址 只有手机开启wifi才能获取到mac地址 <!-- 获取mac地址权限 -->
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
	/*public static String getMacAddress(Context mContext) {
		String result = "";
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}*/

    /**
     * 判断当前activity是否在前台运行
     *
     * @return
     */
	/*public static boolean isRunningForeground(Context mContext) {
		String packageName = getPackageName(mContext);
		String topActivityClassName = getTopActivityName(mContext);
		if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
			return true;
		} else {
			return false;
		}
	}*/

    /**
     * 获取当前显示activity名字
     */
	/*public static String getTopActivityName(Context mContext) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (mContext
				.getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}*/

    /**
     * 启动默认浏览器打开连接
     *
     * @param mContext
     * @param url
     */
    public static void openBrowser(Context mContext, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 判断App是否在前台运行
     *
     * @param mContext
     * @return
     */
	/*public static boolean isAppRunningForeground(Context mContext) {
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName(mContext))) {
			return true;
		}

		return false;
	}*/

    /**
     * 拨打电话
     *
     * @param mContext
     * @param phoneNum
     */
	/*public static void callPhone(Context mContext, String phoneNum) {
		phoneNum = phoneNum.trim();// 删除字符串首部和尾部的空格
		if (phoneNum != null && !phoneNum.equals("")) {
			// 调用系统的拨号服务实现电话拨打功能
			// 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
			mContext.startActivity(intent);// 内部类

		}

	}*/

    /**
     * 拨打电话
     *
     * @param mContext
     * @param tels
     *            已“，”分割的tel
     */
	/*public static void callPhones(final Context mContext, String tels) {
		if (tels != null && !tels.equals("")) {
			tels = tels.replace("'", "");
			final String[] ts = tels.split(",");
			if (ts.length > 1) {
				Dialog alertDialog = new AlertDialog.Builder(mContext).setTitle("请选择：")
						.setItems(ts, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								callPhone(mContext, ts[which]);
							}
						}).setNegativeButton("取消", null).create();
				alertDialog.show();
			} else {
				callPhone(mContext, tels);
			}
		}

	}*/

    /**
     * 发送短信
     */
	/*public static void sendSMS(Context mContext, String tel, String text) {
		if (tel != null && !tel.equals("")) {
			Uri uri = Uri.parse("smsto:" + tel);
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			it.putExtra("sms_body", text);
			mContext.startActivity(it);
		}
	}*/

    /**
     * 是否为当前程序进程
     *
     * @param context
     * @return
     */
    public static boolean isCurProcess(Context context) {
        int pid = android.os.Process.myPid();
        String packgeName = context.getPackageName();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
                if (appProcess.pid == pid && TextUtils.equals(appProcess.processName, packgeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 隐藏系统键盘
     *
     * @param a
     * @return
     */
    public static boolean hideSoftInput(Activity a) {
        try {
            View view = a.getCurrentFocus();
            return view == null || ((InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 显示系统键盘
     *
     * @param editText
     * @param context
     */
    public static void showSoftInput(final EditText editText, final Context context) {
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
            }
        }, 100);
    }

    /**
     * 判断是否为pad
     *
     * @param context      如果context == null, 默认返回上次调用时的判断
     * @return true为pad
     */
    public static boolean isTablet(Context context) {
        if(context == null){
            LogUtils.e("context is null!!! default return last isTablet value.");
            return isTablet;
        }
        isTablet = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        return isTablet;
    }

    /**
     * 当前调用是否在UI线程
     *
     * @return
     */
    public static boolean isUiThread(){
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 在UI线程执行一个任务
     *
     * @param activity
     * @param task
     */
    public static void runOnUiThread(Activity activity, Runnable task){
        if(isUiThread()){
            task.run();
        }else{
            activity.runOnUiThread(task);
        }
    }

    /**
     * 分享一段文本
     *
     * @param context
     * @param title
     * @param subject
     * @param text
     */
    public static void share(Context context, String title, String subject, String text){
        share(context, title, subject, text, null);
    }
    /**
     * 分享一段文本
     *
     * @param context
     * @param title
     * @param subject
     * @param text
     */
    public static void share(Context context, String title, String subject, String text, String[] receivers){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (receivers != null) {
            shareIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        }
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, title));

    }

    /**
     * 获取sha1
     * @param context
     * @return
     */
    public static String getCertificateSHA1Fingerprint(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
        String packageName = context.getPackageName();
        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        //X509证书，X.509是一种非常通用的证书格式
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            //加密算法的类，这里的参数可以使MD4,MD5等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");
            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());
            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString;
    }
    //这里是将获取到得编码进行16进制转换
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }
}
