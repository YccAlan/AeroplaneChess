package com.alan.aeroplanechess.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.model.room.PlayerInfo;

/**
 * Created by yccalan on 2018/3/18.
 */

public class PlayerView {
    PlayerInfo playerInfo;
    ConstraintLayout playerView;
    Context context;
    ImageView avatar;
    TextView name;
    TextView status;
    ObjectAnimator spinning=new ObjectAnimator();

    public PlayerView(PlayerInfo playerInfo,ConstraintLayout playerView, Context context){
        this.playerInfo=playerInfo;
        this.playerView = playerView;
        this.context=context;
        avatar= playerView.findViewById(R.id.avatar);
        name= playerView.findViewById(R.id.name);
        status= playerView.findViewById(R.id.status);
        name.setText(playerInfo.getName());
        status.setText(String.valueOf(playerInfo.getState()));

        spinning.setTarget(avatar);
        spinning.setPropertyName("rotation");
        spinning.setFloatValues(0,360);
        spinning.setRepeatMode(ObjectAnimator.RESTART);
        spinning.setRepeatCount(ObjectAnimator.INFINITE);
    }

    public void setActive(boolean isActive){
        if (isActive)
            spinning.start();
        else
            spinning.cancel();
    }

    public void updateStatus(){
        status.setText(String.valueOf(playerInfo.getState()));
    }
}
