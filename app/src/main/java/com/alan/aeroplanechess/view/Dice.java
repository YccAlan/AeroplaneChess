package com.alan.aeroplanechess.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.SoundPool;
import android.widget.ImageButton;

import com.alan.aeroplanechess.R;

import java.util.Random;

/**
 * Created by yccalan on 2018/3/17.
 */

public class Dice {
    int diceImageId[]={R.drawable.dice3droll,R.drawable.dice_1,R.drawable.dice_2,R.drawable.dice_3,R.drawable.dice_4,R.drawable.dice_5,R.drawable.dice_6};
    ImageButton diceView;
    Context context;
    Random random=new Random();
    SoundPool soundPool;
    int diceSound;

    public Dice(ImageButton diceView, Context context){
        this.diceView=diceView;
        this.context=context;
        soundPool=new SoundPool.Builder().setMaxStreams(1).build();
        diceSound=soundPool.load(context,R.raw.dice,1);
    }

    public void row(Runnable finishedListener){
        AnimationDrawable animationDrawable=new AnimationDrawable();
        for(int i=1;i<=20;i++)
            animationDrawable.addFrame(context.getDrawable(diceImageId[random.nextInt()%6+1]),50);
        animationDrawable.setOneShot(true);
        diceView.setImageDrawable(animationDrawable);
        animationDrawable.start();
        soundPool.play(diceSound,1,1,1,0,1);
        diceView.postDelayed(finishedListener,1000);
    }

    public void setValue(int value){
        diceView.setImageDrawable(context.getDrawable(diceImageId[value]));
    }

    public void setClickable(boolean isClickable,Runnable finishedListener) {
        if (isClickable){
            diceView.setClickable(true);
            diceView.setOnClickListener(view -> {
                diceView.setClickable(false);
                finishedListener.run();
            });
        }
        else{
            diceView.setClickable(false);
        }
    }
}
