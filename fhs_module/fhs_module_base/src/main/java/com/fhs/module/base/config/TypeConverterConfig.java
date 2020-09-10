package com.fhs.module.base.config;


import com.fhs.common.utils.CheckUtils;
import com.github.liangbaika.validate.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class TypeConverterConfig {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    /**
     * 增加字符串转日期的功能
     */
    @PostConstruct
    public void initEditableAvlidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer)handlerAdapter.getWebBindingInitializer();
        if(initializer.getConversionService()!=null) {
            GenericConversionService genericConversionService = (GenericConversionService)initializer.getConversionService();
            genericConversionService.addConverter(new StringToDateConverter());
        }
    }

    /**
     * 字符串日期转换器
     */
    public static class StringToDateConverter  implements Converter<String,Date> {
        private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
        private static final String shortDateFormat = "yyyy-MM-dd";
        @Override
        public Date convert(String value) {
            if(CheckUtils.isNullOrEmpty(value)) {
                return null;
            }
            value = value.trim();
            try {
                if(value.contains("-")) {
                    SimpleDateFormat formatter;
                    if(value.contains(":")) {
                        formatter = new SimpleDateFormat(dateFormat);
                    }else {
                        formatter = new SimpleDateFormat(shortDateFormat);
                    }
                    Date dtDate = formatter.parse(value);
                    return dtDate;
                }else if(value.matches("^\\d+$")) {
                    Long lDate = new Long(value);
                    return new Date(lDate);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("parser %s to Date fail", value));
            }
            throw new RuntimeException(String.format("parser %s to Date fail", value));
        }
    }
}
