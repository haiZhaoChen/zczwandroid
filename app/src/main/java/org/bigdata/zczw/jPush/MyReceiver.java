package org.bigdata.zczw.jPush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.bigdata.zczw.activity.CheckListActivity;
import org.bigdata.zczw.activity.MainActivity;
import org.bigdata.zczw.activity.StartActivity;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {



	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		processCustomMessage(context,bundle);
		boolean back =  isBackground(context);

		if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			if (back) {//系统没有启动;
				//启动app
				Intent i = new Intent(context, StartActivity.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);
			}else{
				//跳到主Activity
				Intent i = new Intent(context, CheckListActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);
			}
		}
	}


	
//	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
		msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);

		context.sendBroadcast(msgIntent);
	}

	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
                    /*
                    BACKGROUND=400 EMPTY=500 FOREGROUND=100
                    GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                     */
				if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台"
							+ appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
}
