package com.flatrock.gateway.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GatewayEventListener {

    @EventListener
    public void handleGatewayEvent(ApplicationEvent event) {
        System.out.println(event);
    }

}

