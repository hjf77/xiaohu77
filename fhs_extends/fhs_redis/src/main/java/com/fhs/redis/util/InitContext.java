package com.fhs.redis.util;

import javax.annotation.PostConstruct;

import com.fhs.redis.config.RedisConfigs;
import com.fhs.redis.exception.RedisInitException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@SuppressWarnings("rawtypes")
public class InitContext extends RedisApplication implements Constant  {

	private static Log log = LogFactory.getLog(InitContext.class);

	@Autowired
	private RedisConfigs redisConfigs;


	@Autowired
	private Environment env;

	@Autowired
	private RedisConnectionFactory factory;

	@PostConstruct
	public void initRedisServers() {


		String currentServerName = "";
		try {

			Properties properties = redisConfigs.propertiesConfig();
			String host = properties.getProperty(REDISPROPERTIES_HOST_PROFIXKEY);
			String name = properties.getProperty(REDISPROPERTIES_NAME_PROFIXKEY);
			Integer port = Integer.valueOf(properties.getProperty(REDISPROPERTIES_PORT_PROFIXKEY));
			String possword = properties.getProperty(REDISPROPERTIES_PASSWORD_PROFIXKEY);
			currentServerName = host;
			//redisTemplate反序列化报错、
            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(factory);
            //定义key的序列化方式
            StringRedisSerializer keySerializer = new StringRedisSerializer();
            redisTemplate.setValueSerializer(keySerializer);
            redisTemplate.setKeySerializer(keySerializer);
            redisTemplate.afterPropertiesSet();
			createRedisConnection(name, host, port, possword,redisTemplate);

			//runUpdateLimit();
		} catch (NumberFormatException e) {
			log.error("initRedisServers: " + currentServerName+" occur NumberFormatException :" + e.getMessage());
			throw new RedisInitException(e);
		} catch (Throwable e1) {
			log.error("initRedisServers: " + currentServerName+" occur Throwable :" + e1.getMessage());
			throw new RedisInitException(currentServerName + " init failed", e1);
		}
	}
	
}
