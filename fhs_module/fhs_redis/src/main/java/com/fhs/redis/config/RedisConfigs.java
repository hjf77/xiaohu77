package com.fhs.redis.config;

import java.util.Properties;
import com.fhs.redis.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisConfigs implements Constant {

	@Value("${spring.redis.host}")
	public  String redisHost;

	@Value("${spring.redis.port}")
	public   String redisPort;

	@Value("${spring.redis.password}")
	public  String redisPassword;

	@Value("${spring.redis.database}")
	public String defaultDbindex;

	/*@Bean(name="redisProperties")*/
	//not-in-use now
	public  Properties propertiesConfig() {
		Properties prop = new Properties();
		prop.put(REDISPROPERTIES_NAME_PROFIXKEY,redisHost);
		prop.put(REDISPROPERTIES_HOST_PROFIXKEY,redisHost);
		prop.put(REDISPROPERTIES_PORT_PROFIXKEY,redisPort);
		prop.put(REDISPROPERTIES_PASSWORD_PROFIXKEY,redisPassword);
		prop.put(REDISPROPERTIES_DBINDEX_PROFIXKEY,defaultDbindex);
		return prop;
	}
	
	
	
}
