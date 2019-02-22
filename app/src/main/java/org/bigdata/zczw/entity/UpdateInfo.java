package org.bigdata.zczw.entity;

/**
 * 版本更新信息
 */
public class UpdateInfo {
	private String name;
	private String url;
	private String versionName;
	
	public UpdateInfo() {
	}

	public String getDescription() {
		return name;
	}

	public void setName(String name) {
		this.name =name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	
}
