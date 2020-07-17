package com.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyting
 * @sinne 2020-06-16
 */
@ServerEndpoint(value="/websocket")
public class MyWebsocketServer {

    private static final Logger log = LoggerFactory.getLogger(MyWebsocketServer.class);

    private Session session;

    /**
     * 连接建立后出发的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        log.info("onOpen"+session.getId());
        WebSocketMapUtil.put(session.getId(), this);
    }

    /**
     * 连接关闭后出发的方法
     */
    @OnClose
    public void onClose(){

        WebSocketMapUtil.remove(session.getId());
        log.info("===== onClose:"+session.getId()+" =====");
    }

    /**
     * 接受到客户端消息时出发的方法
     * @param params
     * @param session
     * @throws Exception
     */
    @OnMessage
    public void onMessage(String params,Session session) throws Exception {
        // 获取服务端到客户端的通道
        MyWebsocketServer myWebsocket = WebSocketMapUtil.get(session.getId());
        log.info("收到来自"+session.getId()+"的消息："+params);
        String result = "收到来自"+session.getId()+"的消息："+params;
        // 返回消息给 websocket 客户端（浏览器）
        myWebsocket.sendMessage(1,"成功",result);
    }

    /**
     * 发生错误时触发的方法
     * @param session
     * @param error
     */
    @OnError
    public void OnError(Session session,Throwable error){
        log.info(session.getId()+"连接发生错误："+error.getMessage());
        error.printStackTrace();
    }

    private void sendMessage(int status, String message, Object datas) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("datas", datas);
        this.session.getBasicRemote().sendText(map.toString());
    }
}
