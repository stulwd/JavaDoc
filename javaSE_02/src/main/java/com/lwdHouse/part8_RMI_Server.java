package com.lwdHouse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * RMI远程调用
 */
public class part8_RMI_Server {
    public static void main(String[] args) throws RemoteException {
        System.out.println("create World clock remote service...");
        WorldClock worldClock = new WorldClockService();
        // 将此服务转换为远程服务接口
        WorldClock skeleton = (WorldClock) UnicastRemoteObject.exportObject(worldClock, 0);
        // 将RMI服务注册到1099 端口
        Registry registry = LocateRegistry.createRegistry(1099);
        // 注册服务，服务名为WorldClock
        registry.rebind("WorldClock", skeleton);
    }

}

interface WorldClock extends Remote{
    LocalDateTime getLocalDataTime(String zoneId) throws RemoteException;
}

class WorldClockService implements WorldClock{
    @Override
    public LocalDateTime getLocalDataTime(String zoneId) throws RemoteException {
        return LocalDateTime.now(ZoneId.of(zoneId)).withNano(0);
    }
}
