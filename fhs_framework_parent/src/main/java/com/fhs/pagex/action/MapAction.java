package com.fhs.pagex.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fhs.common.utils.FileUtils;
import com.fhs.common.utils.HttpUtils;
import com.fhs.core.config.EConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 地图接口
 */
@Slf4j
@RestController
@RequestMapping("/ms/map")
public class MapAction {

    /**
     * @param longitude 经度 108.84063
     * @param latitude  纬度 34.21922
     */
    @RequestMapping("/getAddressComponent")
    public Map<String, String> getAddressComponent(String latitude, String longitude) {
        Map<String, String> resultMap = new HashMap<>();
        String ak = EConfig.getOtherConfigPropertiesValue("baidu.api.ak");
        String res = HttpUtils.doGet("http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + ak + "&output=json&coordtype=wgs84ll" +
                "&location=" + latitude + "," + longitude);
        log.info("getAddressComponent info = {}", res);
        if (!StringUtils.isBlank(res)) {
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getInteger("status") == 0) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                resultMap.put("country", addressComponent.getString("country"));
                resultMap.put("province", addressComponent.getString("province"));
                resultMap.put("city", addressComponent.getString("city"));
                resultMap.put("district", addressComponent.getString("district"));
            }
        }
        return resultMap;
    }

}
