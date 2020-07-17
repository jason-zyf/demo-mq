package com.websocket;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zyting
 * @sinne 2020-06-16
 */
public class WebSocketMapUtil {

    public static ConcurrentMap<String,MyWebsocketServer> webSocketMap = new ConcurrentHashMap<>();

    public static void put(String key,MyWebsocketServer myWebsocketServer){
        webSocketMap.put(key, myWebsocketServer);
    }

    public static MyWebsocketServer get(String key){
        return webSocketMap.get(key);
    }

    public static void remove(String key){
        webSocketMap.remove(key);
    }

    public static Collection<MyWebsocketServer> getValues(){
        return webSocketMap.values();
    }

}


