package com.jkm.hss.version;

import java.util.HashMap;
import java.util.Map;

public class VersionMapper {
	public static Map<String,Map<String,String[]>> versionMap=new HashMap<String,Map<String,String[]>>();
	static{
		versionMap.put("v1.0", V1BizMapper.bizMapper);
		versionMap.put("v1.5", V1BizMapper.bizMapper);
		versionMap.put("v1.6", V1o6BizMapper.bizMapper);
	}
}
