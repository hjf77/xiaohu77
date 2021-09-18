package com.fhs.bislogger.util;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 用户登陆信息工具类
 *
 * @author user
 * @date 2020-05-18 14:35:22
 */
@Slf4j
public class GetLoginUserMsgUtil {

    /**
     * @param ip
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getAddresses(String ip)
            throws UnsupportedEncodingException {
        // 这里调用pconline的接口
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php?ip=";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = HttpUtils.doGet(urlStr + ip);
        if (returnStr != null) {
            if (returnStr.contains("the request over max qps for user")) {
                return "";
            }
            // 处理返回的省市区信息
            System.out.println(returnStr);
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "0";//无效IP，局域网测试
            }
            String region = (temp[5].split(":"))[1].replaceAll("\"", "");
            region = decodeUnicode(region);// 省份

            String country = "";
            String area = "";
            String
                    city = "";
            String county = "";
            String isp = "";
            for (int i = 0; i < temp.length; i++) {
                switch (i) {
                    case 1:
                        country =
                                (temp[i].split(":"))[2].replaceAll("\"", "");
                        country =
                                decodeUnicode(country);//国家 break; case 3:area =
                        (temp[i].split(":"))[1].replaceAll("\"", "");
                        area =
                                decodeUnicode(area);//地区 break; case 5:region =
                        (temp[i].split(":"))[1].replaceAll("\"", "");
                        region =
                                decodeUnicode(region);//省份 break; case 7:city =
                        (temp[i].split(":"))[1].replaceAll("\"", "");
                        city =
                                decodeUnicode(city);//市区 break; case 9:county =
                        (temp[i].split(":"))[1].replaceAll("\"", "");
                        county =
                                decodeUnicode(county);//地区 break; case 11:isp =
                        (temp[i].split(":"))[1].replaceAll("\"", "");
                        isp =
                                decodeUnicode(isp);//ISP公司 break; } }

                        System.out.println(country + "=" + area + "=" + region + "=" + city + "=" + county + "=" + isp);
                        return country + "," + area + "," + region + "," + city;
                }
            }
        }
        return null;
    }


    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     * @author fanhui 2007-3-15
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    // 测试
    public static void main(String[] args) {
        GetLoginUserMsgUtil addressUtils = new GetLoginUserMsgUtil();
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        String ip = "125.76.177.214";
        String address = "";
        try {
            address = addressUtils.getAddresses(ip);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(address);
        // 输出结果为：广东省,广州市,越秀区

    }


    /**
     * 获取当前登录人浏览器信息
     *
     * @param req
     * @return
     */
    public Map<String, String> getUserAgent(HttpServletRequest req) {
        Map<String, String> sys = new HashMap<String, String>();
        String ua = req.getHeader("User-Agent").toLowerCase();
        if (CheckUtils.isNullOrEmpty(ua)) {
            return sys;
        }
        String s;
        String msieP = "msie ([\\d.]+)";
        String firefoxP = "firefox\\/([\\d.]+)";
        String chromeP = "chrome\\/([\\d.]+)";
        String safariP = "version\\/([\\d.]+).*safari";
        Pattern pattern = Pattern.compile(msieP);
        Matcher mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            sys.put("type", "ie");
            sys.put("version", s.split(" ")[1]);
            return sys;
        }
        pattern = Pattern.compile(firefoxP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            System.out.println(s);
            sys.put("type", "firefox");
            sys.put("version", s.split("/")[1]);
            return sys;
        }
        pattern = Pattern.compile(chromeP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            sys.put("type", "chrome");
            sys.put("version", s.split("/")[1]);
            return sys;
        }
        pattern = Pattern.compile(safariP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            s = mat.group();
            sys.put("type", "safari");
            sys.put("version", s.split("/")[1].split(".")[0]);
            return sys;
        }
        return sys;
    }

    /**
     * 获取当前登录人操作系统信息
     *
     * @param request
     * @return
     */
    public String getOsInfo(HttpServletRequest request) {
        String browserDetails = request.getHeader("User-Agent");
        String userAgent = browserDetails;
        String os = "";
        if (userAgent.toLowerCase().indexOf("windows") >= 0) {
            os = "Windows";
        } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
            os = "Mac";
        } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
            os = "Unix";
        } else if (userAgent.toLowerCase().indexOf("android") >= 0) {
            os = "Android";
        } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
            os = "IPhone";
        } else {
            os = "UnKnown, More-Info: " + userAgent;
        }
        return os;
    }
}
