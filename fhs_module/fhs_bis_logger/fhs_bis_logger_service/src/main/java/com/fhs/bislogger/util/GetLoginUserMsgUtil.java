package com.fhs.bislogger.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fhs.core.trans.service.impl.AutoTransService.LOGGER;

public class GetLoginUserMsgUtil {
    /**
     * 根据IP地址获取详细的地域信息
     *
     * @project:personGocheck
     * @class:AddressUtils.java
     * @author:heguanhua E-mail:37809893@qq.com
     * @date：Nov 14, 2012 6:38:25 PM
     */

        /**
         * @param content        请求的参数 格式为：name=xxx&pwd=xxx
         * @param encodingString 服务器端请求编码。如GBK,UTF-8等
         * @return
         * @throws UnsupportedEncodingException
         */
        public  String getAddresses(String content, String encodingString)
                throws UnsupportedEncodingException {
            // 这里调用pconline的接口
            String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
            // 从http://whois.pconline.com.cn取得IP所在的省市区信息
            String returnStr = this.getResult(urlStr, content, encodingString);
            if (returnStr != null) {
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
         * @param urlStr   请求的地址
         * @param content  请求的参数 格式为：name=xxx&pwd=xxx
         * @param encoding 服务器端请求编码。如GBK,UTF-8等
         * @return
         */
        private String getResult(String urlStr, String content, String encoding) {
            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();// 新建连接实例
                connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
                connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
                connection.setDoOutput(true);// 是否打开输出流 true|false
                connection.setDoInput(true);// 是否打开输入流true|false
                connection.setRequestMethod("POST");// 提交方法POST|GET
                connection.setUseCaches(false);// 是否缓存true|false
                connection.connect();// 打开连接端口
                DataOutputStream out = new DataOutputStream(connection
                        .getOutputStream());// 打开输出流往对端服务器写数据
                out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
                out.flush();// 刷新
                out.close();// 关闭输出流
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
                // ,以BufferedReader流来读取
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();// 关闭连接
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
                address = addressUtils.getAddresses("ip=" + ip, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(address);
            // 输出结果为：广东省,广州市,越秀区

    }

    /**
     * 获取当前的登录人ip地址
     * @param request
     * @return
     * @throws IOException
     */
    public  String getIpAddress(HttpServletRequest request) throws IOException {
        String ip = request.getHeader("X-Forwarded-For");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取当前登录人浏览器信息
     * @param req
     * @return
     */
    public Map<String,String> getUserAgent(HttpServletRequest req){
        Map<String,String> Sys= new HashMap<String, String>();
        String ua = req.getHeader("User-Agent").toLowerCase();
        String s;
        String msieP = "msie ([\\d.]+)";
        String firefoxP = "firefox\\/([\\d.]+)";
        String chromeP = "chrome\\/([\\d.]+)";
        String operaP = "opera.([\\d.]+)/)";
        String safariP = "version\\/([\\d.]+).*safari";

        Pattern pattern = Pattern.compile(msieP);
        Matcher mat = pattern.matcher(ua);
        if(mat.find()){
            s=mat.group();
            Sys.put("type", "ie");
            Sys.put("version", s.split(" ")[1]);
            return Sys;
        }
        pattern = Pattern.compile(firefoxP);
        mat=pattern.matcher(ua);
        if(mat.find()){
            s=mat.group();
            System.out.println(s);
            Sys.put("type", "firefox");
            Sys.put("version", s.split("/")[1]);
            return Sys;
        }
        pattern = Pattern.compile(chromeP);
        mat=pattern.matcher(ua);
        if(mat.find()){
            s=mat.group();
            Sys.put("type", "chrome");
            Sys.put("version", s.split("/")[1]);
            return Sys;
        }
        pattern = Pattern.compile(operaP);
        mat=pattern.matcher(ua);
        if(mat.find()){
            s=mat.group();
            Sys.put("type", "opera");
            Sys.put("version", s.split("\\.")[1]);
            return Sys;
        }
        pattern = Pattern.compile(safariP);
        mat=pattern.matcher(ua);
        if(mat.find()){
            s=mat.group();
            Sys.put("type", "safari");
            Sys.put("version", s.split("/")[1].split(".")[0]);
            return Sys;
        }
        return Sys;
    }

    /**
     * 获取当前登录人操作系统信息
     * @param request
     * @return
     */
    public  String getOsInfo(HttpServletRequest request) {
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
