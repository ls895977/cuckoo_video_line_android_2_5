package com.ailiaovideo.videoline.event;

public class RefreshMessageEvent {
    private String message;

    public RefreshMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
