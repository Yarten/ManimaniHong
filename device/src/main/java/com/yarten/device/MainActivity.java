package com.yarten.device;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yarten.device.Communication.WifiNetwork;

public class MainActivity extends AppCompatActivity {
    private WifiNetwork wifiNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiNetwork = new WifiNetwork(this, 7259, new WifiNetwork.Listener() {
            @Override
            public void onReceive(String ip, String msg) {
                Log.i("Network", ip + " " + msg);
            }

            @Override
            public void onError(Exception e, State state, String who) {
                Log.i("Network", who + " " + state.toString());
                Log.i("Network", e.toString());
            }
        }).connect();
    }
}
