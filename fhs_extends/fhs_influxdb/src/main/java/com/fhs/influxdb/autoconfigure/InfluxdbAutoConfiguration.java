package com.fhs.influxdb.autoconfigure;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fhs.influxdb.autoconfigure.properties.InfluxdbProperties;
import com.fhs.influxdb.core.InfluxdbTemplate;


@Configuration
@EnableConfigurationProperties({InfluxdbProperties.class})
@ConditionalOnProperty(prefix = "influxdb", value = "enable", matchIfMissing = true)
public class InfluxdbAutoConfiguration {

    public InfluxdbAutoConfiguration() {

    }


    @Bean
    @ConditionalOnMissingBean
    public InfluxDB influxdb(InfluxdbProperties influxdbProperties) {
        InfluxDB influxDB = InfluxDBFactory.connect(influxdbProperties.getUrl(), influxdbProperties.getUsername(), influxdbProperties.getPassword());
        influxDB.setDatabase(influxdbProperties.getDatabase());
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        return influxDB;
    }


    @Bean
    public InfluxdbTemplate influxdbTemplate(InfluxdbProperties influxdbProperties) {
        return new InfluxdbTemplate(influxdbProperties);
    }


}
