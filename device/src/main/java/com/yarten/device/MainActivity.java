package com.yarten.device;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yarten.device.Communication.WifiNetwork;
import com.yarten.device.UCP.Host;
import com.yarten.device.UCP.Signal;
import com.yarten.device.UCP.Manager;
import com.yarten.utils.LoopThread;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   testWifi();
        testUCPManager();
    }

    private Manager manager;

    void testUCPManager()
    {
        manager = new Manager(this);

        manager.setDiscoverListener(new Manager.DiscoverListener() {
            @Override
            public void onHello(Host host, int position) {
                Log.i("UCP-Hello", host.toString() + " " + position);
                manager.connect(host, "123456");
            }

            @Override
            public void onGoodbye(int position) {
                Log.i("UCP-Goodbye", "" + position);
            }
        }).setConnectListener(new Manager.ConnectListener() {
            @Override
            public void onConnected(Host host) {
                Log.i("UCP-Connected", host.toString());
            }

            @Override
            public void onDisconnected(Host host, boolean isTimeout) {
                Log.i("UCP-Disconnceted", host.toString() + (isTimeout ? " Timout" : " Break"));
            }
        }).setControlListListener(new Manager.ControlListListener() {
            @Override
            public void onList(List<Signal> signals) {
                for(int i = 0, size = signals.size(); i < size; i++)
                    Log.i("UCP-List", signals.get(i).toString());
            }
        });

        manager.startListenCast();
    }

    private WifiNetwork wifiNetwork;
    private LoopThread sendThread;

    void testWifi()
    {
        wifiNetwork = new WifiNetwork(this,  new WifiNetwork.Listener() {
            @Override
            public void onReceive(String ip, String msg) {
                Log.i("Network", ip + " " + msg);
            }

            @Override
            public void onError(Exception e, State state, String who) {
                Log.i("Network", who + " " + state.toString());
                if(e != null)
                    e.printStackTrace();
                else Log.i("Network", "No Exception Occur");
            }
        }).connect(7259, 9527, "239.0.0.1");
        wifiNetwork.startListen();

        sendThread = new LoopThread()
        {
            @Override
            public void onRun() {
                wifiNetwork.send("192.168.137.1", "CLIENT");
            }
        }.setPeriod(1000);
        sendThread.start();
    }
}
