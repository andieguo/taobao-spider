package com.andieguo.taobao.common;

import java.io.File;
import java.io.IOException;

public class Constants {
	public static final String HOMEPATH = System.getProperty("user.home");
	public static final String PROJECTNAME = "taobao-spider";
	/**
	 * eg. /home/hadoop/zcloud-education
	 */
	public static final String PROJECTPATH = HOMEPATH+File.separator+PROJECTNAME;
	public static final String SPIDERCOUNTFILE = PROJECTPATH + File.separator + "count.properties";

	static{
		mkdir(PROJECTPATH);
		mkfile(SPIDERCOUNTFILE);
	} 
	
	public static void mkdir(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public static void mkfile(String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
