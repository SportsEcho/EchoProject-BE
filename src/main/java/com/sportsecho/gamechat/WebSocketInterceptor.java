package com.sportsecho.gamechat;

import java.util.Map;
import lombok.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class WebSocketInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(
            ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Map<String, Object> attributes
        ) {
            String path = request.getURI().getPath();

            String roomId = path.substring(path.lastIndexOf('/') + 1);

            //여기서 socketHandler session에 전달
            attributes.put("roomId", roomId);

            return true;
        }

        @Override
        public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception
        ) {
            // do nothing
        }
}
