package com.yarten.device.Communication;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.yarten.device.Communication.Interface.Communication;
import com.yarten.device.Communication.Interface.Receiver;
import com.yarten.device.Communication.Interface.Sender;
import com.yarten.utils.LoopThread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by yfic on 2017/12/29.
 */

public class WifiNetwork implements Communication
{
    private Context context;
    private Listener listener;

    public WifiNetwork(Context context, Listener listener)
    {
        this.context = context;
        this.listener = listener;
    }

    public boolean checkState()
    {
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    private Singlecaster singlecaster;
    private Multicaster multicaster;

    public WifiNetwork connect(int singlecastPort, int multicastPort, String groupIP)
    {
        try
        {
            singlecaster = new Singlecaster(singlecastPort, listener);
            multicaster = new Multicaster(context, multicastPort, groupIP, listener);
            singlecaster.startListen();
        }
        catch (Exception e)
        {
            listener.onError(e, Listener.State.NetworkBreak, "");
        }

        return this;
    }

    @Override
    public void startListen()
    {
        multicaster.startListen();
    }

    @Override
    public void stopListen()
    {
        multicaster.stopListen();
    }

    @Override
    public void send(String ip, String msg)
    {
        singlecaster.send(ip, msg);
    }

    public WifiNetwork ping(String ip)
    {
        try
        {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 10 -w 1 " + ip);
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

    public static class Singlecaster implements Communication
    {
        private UDPReceiver udpReceiver;
        private UDPSender udpSender;

        public Singlecaster(int port, Listener listener) throws Exception
        {
            DatagramSocket socket = new DatagramSocket(port);
            DatagramPacket packet1 = new DatagramPacket(new byte[1024], 1024);
            packet1.setPort(port);
            DatagramPacket packet2 = new DatagramPacket(new byte[0], 0);
            packet2.setPort(port);
            udpReceiver = new UDPReceiver(socket, packet1, listener);
            udpSender = new UDPSender(socket, packet2, listener);
        }

        @Override
        public void send(String ip, String msg)
        {
            udpSender.send(ip, msg);
        }

        @Override
        public void startListen()
        {
            udpReceiver.startListen();
        }

        @Override
        public void stopListen()
        {
            udpReceiver.stopListen();
        }
    }

    public static class Multicaster implements Communication
    {
        private UDPSender udpSender;
        private UDPReceiver udpReceiver;
        private WifiManager.MulticastLock multicastLock;

        public Multicaster(Context context, int port, String groupIP, Listener listener) throws Exception
        {
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(InetAddress.getByName(groupIP));
            DatagramPacket packet1 = new DatagramPacket(new byte[1024], 1024);
            DatagramPacket packet2 = new DatagramPacket(new byte[0], 0);
            udpReceiver = new UDPReceiver(socket, packet1, listener);
            udpSender = new UDPSender(socket, packet2, listener);

            WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            multicastLock = wifiManager.createMulticastLock("multicast.test");
        }

        @Override
        public void startListen()
        {
            multicastLock.acquire();
            udpReceiver.startListen();
        }

        @Override
        public void stopListen()
        {
            udpReceiver.stopListen();
            multicastLock.release();
        }

        @Override
        public void send(String host, String msg) {

        }
    }

    public static class UDPSender implements Sender
    {
        private DatagramSocket socket;
        private DatagramPacket packet;
        private Listener listener;

        public UDPSender(DatagramSocket socket, DatagramPacket packet, Listener listener)
        {
            this.socket = socket;
            this.packet = packet;
            this.listener = listener;
        }

        @Override
        protected void finalize() throws Throwable
        {
            socket.close();
            super.finalize();
        }

        public void send(String ip, String msg)
        {
            byte[] data = msg.getBytes();
            packet.setData(data);

            try
            {
                packet.setAddress(InetAddress.getByName(ip));
                socket.send(packet);
            }
            catch (Exception e)
            {
                listener.onError(e, Listener.State.SendFail, ip);
            }
        }
    }

    public static class UDPReceiver implements Receiver
    {
        private DatagramSocket socket;
        private DatagramPacket packet;
        private Listener listener;

        public UDPReceiver(DatagramSocket socket, DatagramPacket packet, Listener listener)
        {
            this.socket = socket;
            this.packet = packet;
            this.listener = listener;
        }

        @Override
        protected void finalize() throws Throwable
        {
            socket.close();
            super.finalize();
        }

        public void startListen(){listenThread.start();}
        public void stopListen(){listenThread.quit();}

        private LoopThread listenThread = new LoopThread() {
            @Override
            public void onRun() {
                try
                {
                    socket.setSoTimeout(500);
                    socket.receive(packet);
                }
                catch (java.net.SocketTimeoutException e)
                {
                    return;
                }
                catch (Exception e)
                {
                    if(listener != null)
                        listener.onError(e, Listener.State.NetworkBreak, "");
                    return;
                }

                byte[] data = packet.getData();

                try
                {
                    String msg = new String(data,"utf-8").trim();
                    listener.onReceive(packet.getAddress().getHostAddress(), msg);
                }
                catch (Exception e)
                {
                    listener.onError(e, Listener.State.BadPackage, packet.getAddress().getHostAddress());
                }
            }
        };
    }
}
