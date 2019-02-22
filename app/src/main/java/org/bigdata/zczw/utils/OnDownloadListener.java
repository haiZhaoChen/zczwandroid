package org.bigdata.zczw.utils;

import java.io.File;

/**
 * 下载监听
 */
public interface OnDownloadListener {
	/**
	 * 下载成功
	 * @param file
	 */
	void onDownloadSuccess(File file);
	/**
	 * 下载失败
	 */
	void onDownloadFail();
}
