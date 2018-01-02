package com.yarten.device.UCP;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yarten.device.Communication.WifiNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfic on 2017/12/31.
 */

public class Manager
{
    public interface Listener
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

        /**
         * 获取受控端的列表
         * @param signals
         */
        void onList(List<Signal> signals);
    }

    //region 构造器
    private Context context;
    private WifiNetwork wifiNetwork;
    private Converter converter;
    private Listener listener;

    public Manager(Context context, Listener listener)
    {
        this.context = context;
        this.wifiNetwork = new WifiNetwork(context, communicationListener)
                .connect(7259, 9527, "239.0.0.1");
        this.converter = new Converter();
        this.listener = listener;
    }
    //endregion

    //region 系统配置
    private String selfName = "DEFAULT";
    public Manager setSelfName(String name)
    {
        selfName = name;
        return this;
    }
    public String getSelfName()
    {
        return selfName;
    }
    //endregion

    //region 事件监听
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
                      //  onHello(host, pkg);
                        break;
                    case Connect:
                   //     onConnect(host, pkg);
                        break;
                    case Reply:
                     //   onReply(host);
                        break;
                    case List:
                    //    onList(pkg);
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


    //endregion

    //region 受控端管理
    private final List<Host> allHosts = new ArrayList<>();

    @Nullable
    synchronized private Host find(String ip)
    {
        for(Host host : allHosts)
        {
            if(host.host.equals(ip))
            {
                return host;
            }
        }

        return null;
    }
    //endregion
}
