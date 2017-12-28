package com.yarten.device.Communication;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by yfic on 2017/12/28.
 */

public class WifiNetwork
{
    private Context context;
    private WifiManager wifiManager = null;
    private Listener listener;
    private int port;

    public WifiNetwork(Context context, int port, Listener listener)
    {
        this.context = context;
        wifiManager = (WifiManager)context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
        this.listener = listener;
        this.port = port;
    }

    public boolean checkState()
    {
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    private DatagramSocket severSocket = null;
    private UDPReceiver receiver;
    private UDPSender sender;

    public WifiNetwork connect()
    {
        try
        {
            severSocket = new DatagramSocket(port);
            sender = new UDPSender(severSocket, port, listener);
            receiver = new UDPReceiver(severSocket, port, listener);
            receiver.start();
        }
        catch (Exception e)
        {
            listener.onError(e, Listener.State.NetworkBreak, "");
        }

        return this;
    }

    public WifiNetwork send(String ip, String msg)
    {
        sender.send(ip, msg);
        return this;
    }

    public WifiNetwork ping(String ip)
    {
        try
        {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 " + ip);
            int status = process.waitFor();
            if(status == 0)
                listener.onReceive(ip, "Ping Successfully");
            else listener.onError(null, Listener.State.NetworkBreak, ip);
        }
        catch (Exception e)
        {
            listener.onError(e, Listener.State.NetworkBreak, ip);
        }
        return this;
    }

    public interface Listener
    {
        enum State
        {
            Successful, NetworkBreak, SendFail, TimeOut, BadPackage
        }

        void onReceive(String ip, String msg);

        void onError(Exception e, State state, String who);
    }

    //region 网络发送器
    class UDPSender
    {
        private DatagramSocket socket;
        private Listener listener;
        private DatagramPacket packet;
        private int port;

        UDPSender(DatagramSocket socket, int port, Listener listener)
        {
            this.socket = socket;
            this.listener = listener;
            this.port = port;
            packet = new DatagramPacket(null, 0);
        }

        public void send(String ip, String msg)
        {
            byte[] data = msg.getBytes();
            packet.setData(data);

            try
            {
                packet.setAddress(InetAddress.getByName(ip));
                packet.setPort(port);
                socket.send(packet);
            }
            catch (Exception e)
            {
                listener.onError(e, Listener.State.SendFail, ip);
            }
        }
    }
    //endregion

    //region 网络接收器
    class UDPReceiver extends Thread
    {
        private byte[] data;
        private DatagramPacket packet;
        private int port;
        private Listener listener;
        private DatagramSocket socket;

        UDPReceiver(DatagramSocket socket, int port, Listener listener)
        {
            this.socket = socket;
            this.port = port;
            this.listener = listener;
            data = new byte[1024];
            packet = new DatagramPacket(data, data.length);
        }

        private boolean requireFinish = false;
        private boolean isFinish = false;

        public void quit()
        {
            synchronized (this)
            {
                requireFinish = true;
            }

            if(this == Thread.currentThread()) return;

            while (true)
            {
                synchronized (this)
                {
                    if(isFinish) break;
                }
                try{Thread.sleep(1);}
                catch (Exception e){}
            }
        }

        @Override
        public void run()
        {
            while(true)
            {
                synchronized (this)
                {
                    if(requireFinish) break;
                }

                try
                {
                    socket.receive(packet);
                }
                catch (Exception e)
                {
                    if(listener != null)
                        listener.onError(e, Listener.State.NetworkBreak, "");
                    continue;
                }

                byte[] data = packet.getData();
                try
                {
                    String msg = new String(data, 0, data.length, "utf-8");
                    listener.onReceive(packet.getAddress().getHostAddress(), msg);
                }
                catch (Exception e)
                {
                    listener.onError(e, Listener.State.BadPackage, packet.getAddress().getHostAddress());
                }
            }

            synchronized (this)
            {
                isFinish = true;
            }
        }

        @Override
        public synchronized void start()
        {
            requireFinish = false;
            isFinish = false;
            super.start();
        }
    }
    //endregion
}
