package com.lwdHouse;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;

public class part9_RMI_Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        // 连接到服务器localhost的1099端口
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        // 查找名为WorldClock的服务，并转换为WorldClock接口
        // RMI要求服务器和客户端共享同一个WorldClock接口, 这里暂时使用了服务器的接口，正常情况下，要把WorldClock这个接口文件复制到客户端
        WorldClock worldClock2 = (WorldClock) registry.lookup("WorldClock");
        // 正常调用接口方法即可
        LocalDateTime now = worldClock2.getLocalDataTime("Asia/Shanghai");
        System.out.println("now: " + now);
    }

}
