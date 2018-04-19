package com.alan.aeroplanechess.model.room;

/**
 * Created by yccalan on 2018/3/19.
 */

public interface RoomMatcher {
    void matchRoom(RoomInfo roomInfo,OnMatchedListener onMatchedListener);
    void cancelMatch();
    interface OnMatchedListener{
        void onMatched(RoomInfo roomInfo);
        void onFailed();
    }
}
