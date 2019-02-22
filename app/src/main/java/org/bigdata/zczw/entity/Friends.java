package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;


public class Friends implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8484528022822128196L;

	private int status;
	private String msg;
	private List<User> data;

	public Friends(int status, String msg, List<User> data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public Friends() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<User> getData() {
		return data;
	}

	public void setData(List<User> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Friends{" +
				"status=" + status +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}
