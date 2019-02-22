package org.bigdata.zczw.entity;

public class CustomGallery {

	public String sdcardPath;
	public boolean isSeleted = false;


	@Override
	public String toString() {
		return "sdcardPath="+sdcardPath+",isSeleted="+isSeleted;
	}
}
