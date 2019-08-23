package com.ailiaovideo.videoline.audiorecord;

public interface IRecordAudioListener {
        boolean onRecordPrepare();
        String onRecordStart();
        boolean onRecordStop();
        boolean onRecordCancel();
        void onSlideTop();
        void onFingerPress();
    }