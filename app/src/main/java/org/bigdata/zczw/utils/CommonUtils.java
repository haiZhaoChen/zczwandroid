package org.bigdata.zczw.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 一些常用方法
 * 
 * @author zhuangbaojun
 *
 */
public class CommonUtils {

	/**
	 * 获得当前时间 格式为yyMMddhhmmss
	 */
	public static String getCurrentTime() {

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		return format.format(date).toString();
	}

	/**
	 * 根据经纬度计算距离
	 * 
	 * @param LonA
	 *            经度A
	 * @param LatA
	 *            纬度A
	 * @param LonB
	 *            经度B
	 * @param LatB
	 *            纬度B
	 * @return 距离
	 */
	public static double getDistance(double LonA, double LatA, double LonB,
			double LatB) {
		// 东西经，南北纬处理，只在国内可以不处理(假设都是北半球，南半球只有澳洲具有应用意义)
		double MLonA = LonA;
		double MLatA = LatA;
		double MLonB = LonB;
		double MLatB = LatB;
		// 地球半径（千米）
		double R = 6371.004;
		double C = Math.sin(rad(LatA)) * Math.sin(rad(LatB))
				+ Math.cos(rad(LatA)) * Math.cos(rad(LatB))
				* Math.cos(rad(MLonA - MLonB));
		return (R * Math.acos(C));
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 保存bitmap到文件
	 * 
	 * @param filePath
	 * @param mBitmap
	 */
	public static void saveMyBitmap(String filePath, Bitmap mBitmap) {
		File f = new File(filePath);
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
				try {
					f.createNewFile();
				} catch (IOException e) {
					Log.e("CommonUtils", "创建文件失败");
					e.printStackTrace();
				}
			}
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				// boolean a = mNetworkInfo.isAvailable();

				return mNetworkInfo.isAvailable();
				// System.out.prinln();

			}
		}
		return false;
	}

	/**
	 * 生成一个唯一的随机字符串（目前并发量下应该是唯一）。 由一个随机的大写字母、当前时间毫秒数、随机的六位数拼接而成。
	 */
	public static String generateUid() {
		int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 6; i++) {
			result = result * 10 + array[i];
		}
		StringBuilder builder = new StringBuilder();
		builder.append(chars.charAt((int) (Math.random() * 26)))
				.append(System.currentTimeMillis()).append(result);
		System.out.print(builder);
		return builder.toString();
	}

	public static boolean isSDCardExist() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/***
	 * 获取缩放比例
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 缩放比例 返回4则宽高缩小为原图的4分之一
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}
