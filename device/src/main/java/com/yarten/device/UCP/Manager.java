package com.yarten.device.UCP;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yarten.device.Communication.WifiNetwork;
import com.yarten.utils.LoopThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by yfic on 2017/12/30.
 */

public class Manager
{
    //region 构造器
    private Context context;
    private WifiNetwork wifiNetwork;
    private Converter converter;
    private String selfName;

    public static Manager instance;

    public Manager(Context context)
    {
        instance = this;
        this.context = context;
        this.wifiNetwork = new WifiNetwork(context, communicationListener);
        this.wifiNetwork.connect(7259, 9527, "239.0.0.1");
        this.converter = new Converter();
    //    this.sendThread.start();
    }

    public void setSelfName(String name){selfName = name;}

    public interface DiscoverListener
    {
        /**
         * 接收到受控端的Hello报文时回调
         * @param host
         * @param position 被改变的位置
         */
        void onHello(Host host, int position);

        /**
         * 接收不到受控端的Hello报文时回调
         * @param position
         */
        void onGoodbye(int position);
    }

    public interface ConnectListener
    {
        /**
         * 成功连接上时回调
         * @param host
         */
        void onConnected(Host host);

        /**
         * 断开连接时回调
         * @param host
         * @param isTimeout 是否因为超时而断开的连接
         */
        void onDisconnected(Host host, boolean isTimeout);
    }

    public interface ControlListListener
    {
        /**
         * 获取受控端的列表
         * @param signals
         */
        void onList(List<Signal> signals);
    }

    private DiscoverListener discoverListener;
    private ConnectListener connectListener;
    private ControlListListener controlListListener;

    public Manager setDiscoverListener(DiscoverListener discoverListener){this.discoverListener = discoverListener; return this;}
    public Manager setConnectListener(ConnectListener connectListener){this.connectListener = connectListener; return this;}
    public Manager setControlListListener(ControlListListener controlListListener){this.controlListListener = controlListListener; return this;}
    //endregion

    //region 事件处理
    private WifiNetwork.Listener communicationListener = new WifiNetwork.Listener() {
        @Override
        public void onReceive(String ip, String msg) {
            try
            {
                Host host = find(ip);
                Package pkg = converter.toPackage(msg);
                switch (pkg.getType())
                {
                    case Hello:
                        onHello(ip, host, pkg);
                        break;
                    case Connect:
                        onConnect(host, pkg);
                        break;
                    case Reply:
                        onReply(host);
                        break;
                    case List:
                        onList(host, pkg);
                        break;
                }
            }
            catch (Exception e)
            {
                onError(e, State.Successful, ip);
            }
        }

        @Override
        public void onError(Exception e, State state, String who) {
            Log.i("Network", who + " " + state.toString());
            if(e != null)
                e.printStackTrace();
            else Log.i("Network", "No Exception Occur");
        }
    };

    synchronized private void onHello(String ip, Host host, Package pkg) throws Package.IncompatibleType
    {
        if(host == null) // 在AllHost中找不到时，再从newHosts中找，避免添加重复
        {
            for(Host i : newHosts)
                if(i.host.equals(ip))
                {
                    host = i;
                    break;
                }
        }

        if(host == null)
            newHosts.add(new Host(ip, pkg.getName()));
        else if(host.state == Host.State.Discovered)
            host.discoveredTime = System.currentTimeMillis();
    }

    synchronized private void onConnect(Host host, Package pkg) throws Package.IncompatibleType
    {
        boolean state = pkg.isConnected();
        if(host == null) return;

        if(state) // 主机非空且密码正确
        {
            host.watchDog = 0;

            if(host.state != Host.State.Connected)
            {
                host.state = Host.State.Connected;
                connectedHosts.add(host);
            }

            if(connectListener != null)
                connectListener.onConnected(host);
        }
        else
        {
            disconnect(host);
            if(connectListener != null)
                connectListener.onDisconnected(host, false);
        }
    }

    private void onReply(Host host)
    {
        if(host != null)
        {
            host.watchDog = 0;
            Log.i("Reply", host.host + " " + host.name);
        }
        else Log.i("Reply", "Null");
    }

    private void onList(Host host, Package pkg) throws Package.IncompatibleType
    {
        if(host == null)
        {
            if(controlListListener != null)
                controlListListener.onList(null);
            return;
        }

        List<Signal> signals = pkg.getList();
        this.signals.put(host, signals);
        if(controlListListener != null)
            controlListListener.onList(getSignals());
    }
    //endregion

    //region 受控端发现管理
    private final List<Host> connectedHosts = new ArrayList<>();
    private final List<Host> allHosts = new ArrayList<>();
    private List<Host> newHosts;

    /**
     * 开启组播监听，主机发现状态回调生效
     * @return 初始列表（已连接的受控端，不管它们是否处于可发现状态）
     */
    public List<Host> startListenCast()
    {
        newHosts = new ArrayList<>();
        hostCheckThread.start();
        wifiNetwork.startListen();
        return allHosts;
    }

    public void stopListenCast()
    {
        wifiNetwork.stopListen();
        hostCheckThread.quit();
        allHosts.clear();
        allHosts.addAll(connectedHosts);
    }

    private LoopThread hostCheckThread = new LoopThread()
    {
        @Override
        public void onRun() {
            //region 清除长时间没有响应的主机
            long time = System.currentTimeMillis();

            synchronized (allHosts)
            {
                for(int i = 0, size = allHosts.size(); i < size; i++)
                {
                    Host host = allHosts.get(i);
                    if(host.state == Host.State.Discovered)
                    {
                        if(time - host.discoveredTime > 10000)
                        {
                            allHosts.remove(i);
                            if(discoverListener != null)
                                discoverListener.onGoodbye(i);
                            size = allHosts.size();
                        }
                    }

                    if(host.state == Host.State.Connecting)
                    {
                        if(time - host.discoveredTime > 5000)
                        {
                            if(connectListener != null)
                                connectListener.onDisconnected(host, true);
                            host.state = Host.State.Discovered;
                        }
                    }
                }
            }

            //endregion

            //region 增加新发现的主机
            synchronized (Manager.this)
            {
                for(int i = 0, size = newHosts.size(); i < size; i++)
                {
                    Host newHost = newHosts.get(i);

                    int j = 0;
                    for(int allSize = allHosts.size(); j < allSize; j++)
                    {
                        Host oldHost = allHosts.get(j);
                        if(newHost.compareTo(oldHost) == -1)
                            break;
                    }

                    allHosts.add(j, newHost);
                    if(discoverListener != null)
                        discoverListener.onHello(newHost, j);
                }

                newHosts.clear();
            }
            //endregion
        }
    }.setPeriod(500);
    //endregion

    //region 受控端连接管理
    synchronized public void connect(Host host)
    {
        if(host.state != Host.State.Connected)
            host.state = Host.State.Connecting;

        if(host.state == Host.State.Discovered)
            host.discoveredTime = System.currentTimeMillis();

        try
        {
            send(host.host, new Package(Package.Type.Connect).setConnect(selfName, true, host.password));
        }
        catch (Exception e){}
    }

    public void connect(Host host, String password)
    {
        host.password = password;
        connect(host);
    }

    public void disconnect(Host host)
    {
        synchronized (connectedHosts)
        {
            connectedHosts.remove(host);
            host.state = Host.State.Discovered;
        }

        try
        {
            send(host.host, new Package(Package.Type.Connect).setConnect(selfName,false));
        }
        catch (Exception e){}
    }

    public List<Host> getConnectedHosts(){return connectedHosts;}

    public List<Host> getAllHosts(){return allHosts;}
    //endregion

    //region 指令发送管理
    public void requireList()
    {
        for(Host host : connectedHosts)
            requireList(host);
    }

    public void requireList(Host host)
    {
        try
        {
            send(host.host, new Package(Package.Type.List));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<Package> commands = new LinkedList<>();
    private int bufferSize = 10;

    public Manager setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
        return this;
    }

    synchronized public Manager push(String signal, boolean value)
    {
        try
        {
            Package pkg = new Package(Package.Type.Boolean)
                    .setSignal(signal)
                    .setBoolean(value);
        }
        catch (Exception e){}

        while(commands.size() > bufferSize)
            commands.remove(0);

        return this;
    }

    synchronized public Manager push(String signal, Vector<Float> values)
    {
        try
        {
            Package pkg = new Package(Package.Type.Vector)
                    .setSignal(signal)
                    .setVector(values);
        }
        catch (Exception e){}

        while(commands.size() > bufferSize)
            commands.remove(0);

        return this;
    }

    private LoopThread sendThread = new LoopThread() {
        @Override
        public void onRun()
        {
            String msg;

            //region 序列化指令并清空缓存
            synchronized (Manager.this)
            {
                msg = converter.toString(commands);
                commands.clear();
            }

            //endregion
            try
            {
                Package nop = new Package(Package.Type.Reply).setReply(0);
                synchronized (connectedHosts)
                {
                    for(Host host : connectedHosts)
                    {
                        Log.e("Watch Dog", host.watchDog + "");
                        //region 超时处理：直接断开
                        if(host.watchDog / 5 >= 4)
                        {
                            if(connectListener != null)
                                connectListener.onDisconnected(host, true);
                            disconnect(host);
                            continue;
                        }
                        //endregion

                        //region 发包
                        if(host.watchDog != 0 && host.watchDog % 5 == 0) // 尝试重连
                        {
                            connect(host);
                        }
                        else // 正常发包
                        {
                            if(msg.length() != 0)
                                send(host.host, msg);
                            else send(host.host, nop);
                        }
                        //endregion

                        host.watchDog++;
                    }
                }

            }
            catch (Exception e){e.printStackTrace();}
        }
    }.setRate(100);
    //endregion

    //region 控制列表管理
    private Map<Host, List<Signal>> signals = new HashMap<>();

    public List<Signal> getSignals()
    {
        List<Signal> ls = new ArrayList<>();

        if(signals == null) return ls;

        for(Map.Entry<Host, List<Signal>> i : signals.entrySet())
        {
            ls.addAll(i.getValue());
        }

        return ls;
    }

    public Manager resetSignalList()
    {
        signals = new HashMap<>();
        return this;
    }
    //endregion

    //region 工具函数
    private void send(String ip, Package pkg)
    {
        wifiNetwork.send(ip, converter.toString(pkg));
    }

    private void send(String ip, String msg)
    {
        wifiNetwork.send(ip, msg);
    }

    @Nullable
    private Host find(String ip)
    {
        synchronized (allHosts)
        {
            for(Host host : allHosts)
            {
                if(host.host.equals(ip))
                {
                    return host;
                }
            }
        }

        return null;
    }
    //endregion
}
