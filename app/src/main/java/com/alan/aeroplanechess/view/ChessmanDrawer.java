package com.alan.aeroplanechess.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.model.game.ChessAnimation;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.game.impl.ChessmanIdImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by yccalan on 2018/3/18.
 */

public class ChessmanDrawer {
    int chessmanImage[]={};
    Context context;
    FrameLayout map;
    List<ChessmanState> chessmanStates;
    HashMap<Integer,ImageView> chessmen;
    SoundPool soundPool;
    int startSound,collisionSound,finishSound;

    public ChessmanDrawer(FrameLayout map, List<ChessmanState> chessmanStates, Context context){
        this.map=map;
        updateStates(chessmanStates);
        soundPool=new SoundPool.Builder().setMaxStreams(10).build();
        startSound=soundPool.load(context,R.raw.plane_up,1);
        collisionSound=soundPool.load(context,R.raw.plane_fall,1);
        finishSound=soundPool.load(context,R.raw.win_fly_back_home,1);
    }

    public void updateStates(List<ChessmanState> chessmanStates){
        this.chessmanStates=chessmanStates;
        for (ChessmanState i:chessmanStates){
            ImageView c= (ImageView) LayoutInflater.from(context).inflate(R.layout.chessman_view,map);
            c.setImageDrawable(context.getDrawable(chessmanImage[i.getChessmanId().getPlaneId()]));
            c.setTranslationX(i.getX());
            c.setTranslationY(i.getY());
            c.setRotation(i.getAngle());
            chessmen.put(i.getChessmanId().getId(),c);
        }
    }

    public void showAnimation(List<ChessAnimation> chessAnimations,Runnable onFinished){
        if (chessAnimations.size()==0){
            onFinished.run();
            return;
        }
        AnimatorSet animatorSet=new AnimatorSet();
        ChessAnimation animation=chessAnimations.get(0);
        chessAnimations.remove(0);
        switch (animation.event){
            case START:
                soundPool.play(startSound,1,1,1,0,1);
                break;
            case COLLISION:
                soundPool.play(collisionSound,1,1,1,0,1);
                break;
            case FINISH:
                soundPool.play(finishSound,1,1,1,0,1);
                break;
        }

        ImageView chessman=chessmen.get(animation.id.getId());
        animatorSet.play(ObjectAnimator.ofFloat(chessman,"translationX",chessman.getTranslationX(),animation.state.getX()))
                .with(ObjectAnimator.ofFloat(chessman,"translationY",chessman.getTranslationY(),animation.state.getY()))
                .with(ObjectAnimator.ofFloat(chessman,"rotation",chessman.getRotation(),animation.state.getAngle()));
        animatorSet.setDuration(100).start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                showAnimation(chessAnimations,onFinished);
            }
        });
    }

    public void setMovableChessman(List<ChessmanId> movableChessmen, OnClickChessmanLisener onClickChessmanLisener){
        HashSet<Integer> movableId=new HashSet<>();
        if (movableChessmen!=null)
            for (ChessmanId i:movableChessmen)
                movableId.add(i.getId());
        for (Map.Entry<Integer,ImageView> i:chessmen.entrySet()){
            ImageView iv=i.getValue();
            if (movableId.contains(i.getKey())){
                ObjectAnimator spinning=new ObjectAnimator();
                spinning.setTarget(iv);
                spinning.setPropertyName("rotation");
                spinning.setFloatValues(0,360);
                spinning.setRepeatMode(ObjectAnimator.RESTART);
                spinning.setRepeatCount(ObjectAnimator.INFINITE);
                iv.setOnClickListener(view->onClickChessmanLisener.onClickChessman(new ChessmanIdImpl(i.getKey())));
            }
            else {
                iv.clearAnimation();
                iv.setOnClickListener(null);
            }
        }
    }

    public  interface OnClickChessmanLisener{
        void onClickChessman(ChessmanId chessmanId);
    }

}
