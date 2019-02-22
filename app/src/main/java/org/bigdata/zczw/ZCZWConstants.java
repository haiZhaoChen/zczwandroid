package org.bigdata.zczw;

import android.os.Environment;

public class ZCZWConstants {
	/**
	 * 发布动态已经选择的图片张数
	 */
	public static int SELECTED_IAMGE = 0;

	/**
	 * 拍照图片保存路径
	 */
	public static final String STORAGE_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/zczw/images";
	/**
	 * 发布动态已经选择的图片张数
	 */
	public static final int IMAGE_ALLOW = 9;

	/**
	 * 无网络连接，请检查网络
	 */
	public static final String NETWORK_INVALID = "无网络连接，请检查网络";

	/**
	 * 服务器地址
	 */
//	public static final String NAME_PATH = "http://202.206.64.199:8080/rongyun_server_v3";
	/**发布动态拍照上传图片的最大像素*/
	public static final int MAX_NUM_OF_PIXELS = 600*900;
	/**回主页*/
	public static final String BACK_TO_PERSONAL_CENTER = "回主页";
	/**动态已存在*/
	public static final String ORDER_EXIST_MESSAGE = "动态已发布成功，请回主页查看。";
	/**
	 * 版本信息文件再服务器上的位置
	 */
	public static final String VERSION = "version.xml";
}
