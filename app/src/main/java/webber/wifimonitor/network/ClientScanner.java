package webber.wifimonitor.network;

/**
 * Created by tducrot on 17.10.17.
 */

import java.util.ArrayList;
import android.util.Log;
import android.os.AsyncTask;

/**
 * Scan local network and find clients to the same access point
 */
public class ClientScanner extends AsyncTask<String, Void, ArrayList<String>>{

    private long startIp;
    private long endIp;

    public ClientScanner(String localIpv4Address) {

        String[] tmp = localIpv4Address.split("\\.");
        String startIpAddress = tmp[0] + "." + tmp[1] + "." + tmp[2] + ".0";
        this.startIp = NetworkUtils.getLongFromIp(startIpAddress);
        this.endIp = this.startIp + 255;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> reachableIps = new ArrayList<>();
        ArrayList<PingThread> threads = new ArrayList<>();
        for (long i = this.startIp; i <= this.endIp; i++) {
            PingThread ping = new PingThread(NetworkUtils.getIpFromLong(i), reachableIps);
            threads.add(ping);
            ping.run();
        }

        try {
            for (PingThread p : threads){
                p.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return reachableIps;
    }

    private class PingThread extends Thread {

        String ipv4;
        ArrayList<String> reachableIps;

        public PingThread(String ipv4, ArrayList<String> reachableIps){
            this.ipv4 = ipv4;
            this.reachableIps = reachableIps;
        }

        @Override
        public void run() {
            String hostname = NetworkUtils.isAddressReachable(ipv4);
            if (hostname != null){
                Log.d("Reachable ip", ipv4);
                synchronized (reachableIps){
                    reachableIps.add(hostname);
                }
            }
        }
    }
}
