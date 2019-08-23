package com.ailiaovideo.videoline.event;

public class VoiceRecordEvent extends BusEvent {
    public VoiceRecordEvent(int what) {
        super(what);
    }

    public VoiceRecordEvent(int what, String message) {
        super(what, message);
    }

    public VoiceRecordEvent(int what, Object obj) {
        super(what, obj);
    }


}
