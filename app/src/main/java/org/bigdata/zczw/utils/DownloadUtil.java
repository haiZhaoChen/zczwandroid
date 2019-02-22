package org.bigdata.zczw.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 下载工具类
 *
 */
public class DownloadUtil {
	protected static final int DOWNLOAD_SUCCESS = 0;
	protected static final int DOWNLOAD_FAIL = 1;
	/**
	 * 下载
	 * @param url
	 * @param dir
	 */
	public static void download(final String url,final File dir,final OnDownloadListener listener){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case DOWNLOAD_SUCCESS:
					//下载成功
					if(listener!=null){
						listener.onDownloadSuccess((File) msg.obj);
					}
					
					break;
				case DOWNLOAD_FAIL:
					//下载失败
					if(listener!=null){
						listener.onDownloadFail();
					}
					break;

				default:
					break;
				}
			}
		};
		new Thread(new Runnable() {
			InputStream inputStream ;
			@Override
			public void run() {
				HttpClient client=new DefaultHttpClient();
				HttpGet httpGet=new HttpGet(url);
				try {
					HttpResponse response = client.execute(httpGet);
					if(response.getStatusLine().getStatusCode()==200){
						HttpEntity entity = response.getEntity();
						inputStream= entity.getContent();
						File file=new File(dir,getFileNameFromURL(url));

						FileOutputStream fos=new FileOutputStream(file);
						int len=0;
						byte[] buffer=new byte[1024];
						while((len=inputStream.read(buffer))!=-1){
							fos.write(buffer,0,len);
						}
						fos.close();
						handler.obtainMessage(DOWNLOAD_SUCCESS, file).sendToTarget();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("download", "Exception: "+e);
					handler.sendEmptyMessage(DOWNLOAD_FAIL);
				}
			}
		}).start();
	}
	/**
	 * 根据url获取文件名
	 * @param url
	 * @return
	 */
	private static String getFileNameFromURL(String url) {
		return url.substring(url.lastIndexOf("/")+1);
	}
}
