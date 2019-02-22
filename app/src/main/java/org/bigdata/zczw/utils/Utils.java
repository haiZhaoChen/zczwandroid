package org.bigdata.zczw.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.bigdata.zczw.Singleton;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import io.rong.imlib.model.Group;

public class Utils {

	/**声明各种类型文件的dataType**/
	private static final String DATA_TYPE_ALL = "*/*";
	private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
	private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
	private static final String DATA_TYPE_WORD = "application/msword";
	private static final String DATA_TYPE_TXT = "text/plain";
	private static final String DATA_TYPE_PDF = "application/pdf";
	private static final String DATA_TYPE_RAR = "application/x-rar-compressed";
	private static final String DATA_TYPE_ZIP = "application/zip";

	/**
	 * 打开文件
	 */
	public static void openFile(Context mContext,File file,Uri uri) {

		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
		/* 依扩展名的类型决定MimeType */
		Intent intent = null;
		if (end.equals("ppt")|| end.equals("pptx")) {
			intent = generateCommonIntent(uri,DATA_TYPE_PPT);
		} else if (end.equals("xls")|| end.equals("xlsx")) {
			intent = generateCommonIntent(uri,DATA_TYPE_EXCEL);
		} else if (end.equals("doc")|| end.equals("docx")) {
			intent = generateCommonIntent(uri,DATA_TYPE_WORD);
		} else if (end.equals("pdf")) {
			intent = generateCommonIntent(uri,DATA_TYPE_PDF);
		} else if (end.equals("txt")) {
			intent = generateCommonIntent(uri, DATA_TYPE_TXT);
		} else if (end.equals("zip")) {
			intent = generateCommonIntent(uri, DATA_TYPE_ZIP);
		} else if (end.equals("rar")) {
			intent = generateCommonIntent(uri, DATA_TYPE_RAR);
		} else {
			intent = generateCommonIntent(uri,DATA_TYPE_ALL);
		}
		mContext.startActivity(intent);
	}

	/**
	 * 产生除了视频、音频、网页文件外，打开其他类型文件的Intent
	 * @return
	 */
	public static Intent generateCommonIntent(Uri uri, String dataType) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, dataType);
		return intent;
	}





	public static void fitsSystemWindows(boolean isTranslucentStatus, View view) {
		if (isTranslucentStatus) {
			view.getLayoutParams().height = calcStatusBarHeight(view.getContext());
		}
	}

	public static int calcStatusBarHeight(Context context) {
		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 根据群名称获得群信息
	 */
	public static Group getGroupByName(String groupName) {
		ArrayList<Group> groupList = (ArrayList<Group>) Singleton.getInstance().getGrouplist();
		Group g = null;
		for(Group group:groupList){
			if(group.getName().equals(groupName)){
				g=group;
				return g;
			}
		}
		return g;
	}

	public static boolean checkPaw(String paw) {
		String regex = "^[A-Za-z0-9]+$";
		if (TextUtils.isEmpty(paw)) return false;
		return paw.matches(regex);
	}


	/**
	 * 判断网络连接状态
	 * @param context
	 * @return
	 */
	public static boolean judgeNetConnected(Context context) {
		// 判断网络连接需要连接管理者
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取可用的网络信息
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo == null) {
			return false;
		}
		int type = activeNetworkInfo.getType();
		switch (type) {
			case ConnectivityManager.TYPE_WIFI:
			case ConnectivityManager.TYPE_MOBILE:
				return true;

			default:
				return false;
		}
	}
	/**
	 * 携带参数的activity 跳转
	 * @param context
	 * @param clazz
	 * @param name   key值
	 * @param value  携带的参数
	 */
	public static void gotoNewActivity(Context context,Class clazz,String name,Object value){
		Intent intent = new Intent(context, clazz);
		if (value instanceof String) {
			intent.putExtra(name, (String)value);
		}else if (value instanceof Integer) {
			intent.putExtra(name, (Integer)value);
		}else if (value instanceof Long) {
			intent.putExtra(name, (Long)value);
		}else if(value instanceof Serializable){
			intent.putExtra(name, (Serializable)value);
		}else if(value instanceof Bundle){
			intent.putExtra(name,(Bundle)value);
		}
		context.startActivity(intent);
	}

	/**
	 * 跳转activity
	 * @param context
	 * @param clazz
	 */
	public static void startActivity(Context context, Class<? extends Activity> clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	
	/**
	 * dp转像素
	 * @param dp
	 * @param context
	 * @return
	 */
	public static float dpConvertPx(float dp,Context context){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
	
public static String getTime() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return format.format(new Date());
	}

	public static void runOnThread(Runnable task) {

		new Thread(task).start();
	}

	private static Handler mHandler = new Handler(Looper.getMainLooper());

	public static void runOnUIThread(Runnable task) {

		mHandler.post(task);
	}

	public static void showToast(final Context context, final String notice) {

		runOnUIThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(context, notice, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 版本更新
	 * @param context
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog createProgressDialog(Context context,CharSequence title,CharSequence message){
ProgressDialog dialog=new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(false);
		//点击后退能够退出ProgressDialog
		dialog.setCancelable(true);
		return dialog;
	}
	/**
	 * 将Integer类型的set转成 int[]
	 * @param integerSet
	 */
	public  static int [] parseSetToInt(Set<Integer> integerSet) {
		if(integerSet!=null&&integerSet.size()>0){
			Integer [] integers= (Integer [])(integerSet.toArray(new Integer[integerSet.size()]));
			int [] in = new int[integers.length];
			for (int i = 0; i < integers.length; i++) {
				in[i] = integers[i];
			}
			return in;
		}
		return  null;
	}

}
