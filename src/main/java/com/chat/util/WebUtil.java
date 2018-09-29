package com.chat.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @description 获取ip地址
 * @author loveyouso
 * @create 2018-09-19 21:34
 **/
public class WebUtil {

    /**
     * @description：获取服务器IP
     * @param request
     * @return
     */
    public String getServerIpAdder(HttpServletRequest request){
        String addr = request.getScheme() + "//" + request.getServerName();
        if (request.getServerPort() == 80){
            return addr;
        }else{
            return addr + ":" + request + request.getServerPort();
        }
    }

    /**
     * @description：获取客户端真实IP
     * @param request
     * @return
     */
    public String getClientIpAddr(HttpServletRequest request){
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }

}
