package com.fhs.redis.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fhs.redis.util.ztree.ZNode;
import org.springframework.data.redis.core.RedisTemplate;

@SuppressWarnings("rawtypes")
public interface Constant {


	public static final Map<String, RedisTemplate> 					redisTemplatesMap 		= 	new HashMap<String, RedisTemplate>();
	public static final Map<String, CopyOnWriteArrayList<RKey>> 	redisKeysListMap 		= 	new HashMap<String, CopyOnWriteArrayList<RKey>>();
	public static final Map<RKey, Object> 							redisVMCache 			= 	new ConcurrentHashMap<RKey, Object>();
	public static final CopyOnWriteArrayList<ZNode> 				redisNavigateZtree 		= 	new CopyOnWriteArrayList<ZNode>();
	public static final CopyOnWriteArrayList<Map<String, Object>>	redisServerCache 		=   new CopyOnWriteArrayList<Map<String, Object>>();

	public static final int DEFAULT_ITEMS_PER_PAGE											= 10;
	public static final String DEFAULT_REDISKEY_SEPARATOR		 							= ":";

	public static final int REDIS_DEFAULT_DB_SIZE 											= 0;
	public static final String DEFAULT_SEPARATOR											= "_";
	public static final String UTF_8 														= "utf-8";
	
	/** redis properties key **/
	public static final String REDISPROPERTIES_SERVER_NUM_KEY								= 	"redis.server.num";
	public static final String REDISPROPERTIES_LANGUAGE_KEY									= 	"redis.language";
	
	public static final String REDISPROPERTIES_HOST_PROFIXKEY								= 	"redis.host.";
	public static final String REDISPROPERTIES_NAME_PROFIXKEY								= 	"redis.name.";
	public static final String REDISPROPERTIES_PORT_PROFIXKEY								= 	"redis.port.";
	public static final String REDISPROPERTIES_PASSWORD_PROFIXKEY							= 	"redis.password.";
	public static final String REDISPROPERTIES_DBINDEX_PROFIXKEY							= 	"redis.database.";
	
	
	/** default **/
	//public static final String DEFAULT_REDISSERVERNAME 										= "default";
	public static final Map<String, Integer> DEFAULT_DBINDEX 						     = new HashMap<>();

	/** query key **/
	public static final String MIDDLE_KEY													= "middle";
	public static final String HEAD_KEY 													= "head";
	public static final String TAIL_KEY 													= "tail";
	public static final String EMPTY_STRING 												= "";
	
	/** operator for log **/
	public static final String GETKV 														= "GETKV";

}
