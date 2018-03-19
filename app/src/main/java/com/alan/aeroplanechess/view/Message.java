package com.alan.aeroplanechess.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.widget.TextView;

import com.alan.aeroplanechess.R;

/**
 * Created by yccalan on 2018/3/18.
 */

public class Message {
    TextView messageView;
    Animator animator;

    public Message(TextView messageView,Context context){
        this.messageView=messageView;
        animator = AnimatorInflater.loadAnimator(context, R.animator.message_animation);
        animator.setTarget(messageView);
    }

    public void show(String message){
        messageView.setText(message);
        animator.start();
    }
}
