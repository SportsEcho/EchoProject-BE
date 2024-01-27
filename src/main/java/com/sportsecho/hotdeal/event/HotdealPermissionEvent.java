package com.sportsecho.hotdeal.event;

import com.sportsecho.hotdeal.entity.Hotdeal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HotdealPermissionEvent extends ApplicationEvent {

    private final Hotdeal hotdeal;
    private final Long memberId;
    private final int quantity;
    private final String address;
    private final String phone;

    public HotdealPermissionEvent(Object source, Long memberId, int quantity, String address,
        String phone) {
        super(source);
        this.hotdeal = (Hotdeal) source;
        this.memberId = memberId;
        this.quantity = quantity;
        this.address = address;
        this.phone = phone;
    }
}
