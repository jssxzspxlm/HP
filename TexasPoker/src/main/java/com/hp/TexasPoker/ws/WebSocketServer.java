package com.hp.TexasPoker.ws;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ServerEndpoint("/texasPoker")
@Data
public class WebSocketServer {
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String message) {

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
