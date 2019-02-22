package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

public class Groups implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3379481059093227884L;

	private int status;
	private String msg;
	private List<GroupInfo> data;

	public Groups() {
	}

	public Groups(int status, String msg, List<GroupInfo> data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
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

	public List<GroupInfo> getData() {
		return data;
	}

	public void setData(List<GroupInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Groups{" +
				"status=" + status +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}

