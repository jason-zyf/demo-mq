package com.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author zyting
 * @sinne 2020-06-16
 */
public class MyWebSocketClient extends WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(MyWebSocketClient.class);

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("----- MyWebSocket onOpen ----");
    }

    @Override
    public void onMessage(String s) {
        log.info("----- 接受到服务端数据："+ s +" ----");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        log.info("----- MyWebSocket onClose ----");
    }

    @Override
    public void onError(Exception e) {
        log.info("----- MyWebSocket onError ----");
    }

}
