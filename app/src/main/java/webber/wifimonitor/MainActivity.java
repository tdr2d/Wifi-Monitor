package webber.wifimonitor;

import android.app.usage.NetworkStatsManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import webber.wifimonitor.network.ClientScanner;
import webber.wifimonitor.network.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String ipv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView2);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(NETWORK_STATS_SERVICE);

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager == null)
            return;

        WifiInfo info =  wifiManager.getConnectionInfo();

        textView.append("SSID: " + info.getSSID());
        textView.append("\nBSSID: " + info.getBSSID());
        textView.append("\nspeed: " + info.getLinkSpeed());
        textView.append("\ngateway IP: " + NetworkUtils.getIpFromIntSigned(wifiManager.getDhcpInfo().gateway));
        ipv4 = NetworkUtils.getIPV4Address();
        textView.append("\nLocal Ipv4 IP: " + ipv4);
        textView.append("\nReachable ips:");

        ClientScanner clientScannerTask = new ClientScanner(ipv4);
        ArrayList<String> reachableIps = null;
        try {
            reachableIps = clientScannerTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for (String s : reachableIps){
            textView.append("\n\t" + s);
        }
    }

}
