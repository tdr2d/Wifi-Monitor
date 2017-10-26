package webber.wifimonitor.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/**
 * Created by tducrot on 17.10.17.
 */


// https://github.com/rorist/android-network-discovery/tree/master/src/info/lamatricexiste/network/Network

public class NetworkUtils {

    private final String TAG = "NetworkUtils";
    private static final int TIMEOUT_PING = 100;

    public static String getIpFromIntSigned(int ip_int) {
        String ip = "";
        for (int k = 0; k < 4; k++) {
            ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }

    public static long getLongFromIp(String ip_string){
        String[] ipAddressInArray = ip_string.split("\\.");
        long result = Long.parseLong(ipAddressInArray[3]) +
                    (Long.parseLong(ipAddressInArray[2]) << 8 )+
                    (Long.parseLong(ipAddressInArray[1]) << 16 )+
                    (Long.parseLong(ipAddressInArray[0]) << 24 );
        return result;
    }

    public static String getIpFromLong(long ip_long){
        return ((ip_long & 0xff000000) >> 24) + "." +
                    ((ip_long & 0x00ff0000) >> 16) + "." +
                    ((ip_long & 0x0000ff00) >> 8 ) + "." +
                    ((ip_long & 0x000000ff));
    }

    public static String isAddressReachable(String ip){
        try {
            InetAddress inetAddress = Inet4Address.getByName(ip);
            boolean reachable = inetAddress.isReachable(TIMEOUT_PING);
            if (reachable)
                return inetAddress.getCanonicalHostName();
        } catch (UnknownHostException e){
//            e.printStackTrace();
        } catch (IOException e){
//            e.printStackTrace();
        }
        return null;
    }

    public static String getIPV4Address() {
        try {

            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            NetworkInterface wlan0 = null;
            for (NetworkInterface networkInterface : interfaces){
                String name = networkInterface.getName();
                if (networkInterface.getName().equals("wlan0")){
                    wlan0 = networkInterface;
                    break;
                }
            }

            List<InetAddress> addrs = Collections.list(wlan0.getInetAddresses());
            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                    String ipv4Addr = addr.toString();

                    return addr.getHostAddress();
                }
            }

        } catch (Exception ex) { }
        return "<IPv4 not found>";
    }

}
