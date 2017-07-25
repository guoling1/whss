package com.jkm.hss.version;

import java.util.HashMap;
import java.util.Map;

public class VersionMapper {
	public static Map<String,Map<String,String[]>> versionMap=new HashMap<String,Map<String,String[]>>();
	static{
		versionMap.put("v1.0.0", V1o1o1Mapper.bizMapper);
	}
}
