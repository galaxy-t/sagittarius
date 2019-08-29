package com.galaxyt.sagittarius.client.utils;

import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取本地 IP 地址
 * @author zhouqi
 * @date 2019-07-15 16:19
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-15 16:19     zhouqi          v1.0.0           Created
 *
 */
public class IPUtil {


    /**
     * 获取本地 IP 地址
     * @return
     */
    public static String getLocalIp() {


        Enumeration<NetworkInterface> allNetInterfaces = null;
        String resultIP=null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    if(resultIP==null){
                        resultIP= ip.getHostAddress();
                    }

                }
            }
        }

        return resultIP;
    }



}
