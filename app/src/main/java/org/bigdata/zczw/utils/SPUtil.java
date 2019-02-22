package org.bigdata.zczw.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SPUtil {

	public static String VERSION ="version";

private static SharedPreferences getSharedPreferences(Context context){
	return context.getSharedPreferences("config123", Context.MODE_PRIVATE);
}
public static String getString(Context context,String key){
	SharedPreferences sp=getSharedPreferences(context);
	return sp.getString(key, "");
}
public static Boolean getBoolean(Context context,String key){
	SharedPreferences sp=getSharedPreferences(context);
	return sp.getBoolean(key, false);
}
public static int getInt(Context context,String key){
	SharedPreferences sp=getSharedPreferences(context);
	return sp.getInt(key, 0);
}
	public static Long getLog(Context context,String key){
		SharedPreferences sp=getSharedPreferences(context);
		return  sp.getLong(key, 0L);
	}
public static void put(Context context,String key,Object value){
	SharedPreferences sp=getSharedPreferences(context);
	Editor editor=sp.edit();
	if(value instanceof Boolean){
		editor.putBoolean(key, (Boolean) value);
	}else if(value instanceof String){
		editor.putString(key, (String) value);
	}else if(value instanceof Integer){
		editor.putInt(key, (Integer) value);
	}else if(value instanceof Long){
		editor.putLong(key, (Long) value);
	}
	editor.commit();
}
public static  void remove(Context context,String key){
	SharedPreferences sp=getSharedPreferences(context);
	Editor editor=sp.edit();
	editor.remove(key);
	editor.commit();
	}
public static int getInt(Context context,String key,int defValue){
	SharedPreferences sp=getSharedPreferences(context);
	return sp.getInt(key, defValue);
}
public static String getString(Context context,String key,String defValue){
	SharedPreferences sp=getSharedPreferences(context);
	return sp.getString(key, defValue);
}
public static void writeInts(Context context, String key,
		Collection<Integer> integers) {
	StringBuilder sb = new StringBuilder();
	for (Integer integer : integers) {
		sb.append(integer).append(':');
	}
	// 把最后一个字符删掉
	if (sb.length() > 0) {
		sb.deleteCharAt(sb.length() - 1);
	}
	put(context, key, sb.toString());

}
public static List<Integer> readInts(Context context, String key) {
	List<Integer> result = new ArrayList<Integer>();
	String saveText = getString(context, key, "");
	if(saveText.length()>0){
		String[] splitStrings = saveText.split(":");
		for (String intString : splitStrings) {
			result.add(Integer.valueOf(intString));
		}
	}
	return result;
}
}
