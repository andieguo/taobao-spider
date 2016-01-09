package com.andieguo.taobao.common;

import java.io.File;

public class Constants {
	public static final String HOMEPATH = System.getProperty("user.home");
	public static final String PROJECTNAME = "taobao-spider";
	/**
	 * eg. /home/hadoop/zcloud-education
	 */
	public static final String PROJECTPATH = HOMEPATH+File.separator+PROJECTNAME;

	static{
		mkdir(PROJECTPATH);
	} 
	
	public static void mkdir(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	}
}
