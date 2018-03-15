package com.alan.aeroplanechess.viewModel.game.impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.model.game.Ai;
import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessAnimation;
import com.alan.aeroplanechess.model.game.ChessState;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.game.GroupResult;
import com.alan.aeroplanechess.model.game.Player;
import com.alan.aeroplanechess.model.game.impl.AIPlayer;
import com.alan.aeroplanechess.model.game.impl.LocalPlayer;
import com.alan.aeroplanechess.model.game.impl.RemotePlayer;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.model.room.RoomInfo;
import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.viewModel.game.GameSetting;
import com.alan.aeroplanechess.viewModel.game.GameViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yccalan on 2018/3/14.
 */

public class GameViewModelImpl extends ViewModel implements GameViewModel {
    MutableLiveData<GameState> gameState=new MutableLiveData<>();
    MutableLiveData<Integer> currentPlayer=new MutableLiveData<>();
    MutableLiveData<GameSetting> gameSetting=new MutableLiveData<>();
    MutableLiveData<Integer> dice=new MutableLiveData<>();
    MutableLiveData<Integer> countDown=new MutableLiveData<>();
    MutableLiveData<List<ChessAnimation>> animation=new MutableLiveData<>();

    RoomInfo roomInfo;
    ArrayList<Player> players=new ArrayList<>(4);
    ChessState chessState;
    Random random;
    Player current;
    int currentDice;

    Ai ai;
    Timer timer=new Timer();
    UserInputObserver userInputObserver=new UserInputObserver();
    GameStateObserver gameStateObserver =new GameStateObserver();
    ChessmanMovedObserver chessmanMovedObserver=new ChessmanMovedObserver();

    NetworkService networkService;

    @Override
    public MutableLiveData<GameState> getGameState() {
        return gameState;
    }

    @Override
    public LiveData<GameSetting> getGameSetting() {
        return gameSetting;
    }

    @Override
    public LiveData<Integer> getCountDown() {
        return countDown;
    }

    @Override
    public LiveData<Integer> getDice() {
        return dice;
    }

    public void GameViewModel(RoomInfo roomInfo, NetworkService networkService){
        this.roomInfo=roomInfo;
        this.random=new Random(roomInfo.getRandomSeed());
        chessState=new ChessStateImpl(roomInfo);
        ai=new AiImpl(chessState);
        gameState.observeForever(gameStateObserver);
        gameSetting.observeForever(userInputObserver);
        animation.observeForever(chessmanMovedObserver);

        this.networkService=networkService;
        for (PlayerInfo i:roomInfo.getPlayerInfo()){
            Player player = null;
            switch (i.getType()){
                case LOCAL_PLAYER:
                    player=new LocalPlayer(this,i,ai);
                    break;
                case AI_PLAYER:
                    player=new AIPlayer(this,i,ai);
                    break;
                case REMOTE_PLAYER:
                    player=new RemotePlayer(this,i,ai,networkService);
                    break;
            }
            players.add(player);
        }
        current=players.get(0);
        turnStart();
    }



    void turnStart(){
        if (roomInfo.isOnline())
            timer.schedule(new UpdateCountDown(Constants.TURN_TIME),0,1000);
        currentPlayer.setValue(current.getPlayerInfo().getPlayerId());
        current.turnStart();
    }

    class UpdateCountDown extends TimerTask{
        int time;
        UpdateCountDown(int time){
            this.time=time;
        }
        @Override
        public void run() {
            countDown.postValue(time);
            if (time==0){
                current.hosted();
                timer.cancel();
            }
            time--;
        }
    }

    @Override
    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    @Override
    public Map<ChessmanId, ChessmanState> getStates() {
        return chessState.getStates();
    }

    @Override
    public void rollDice() {
        if (gameState.getValue()==GameState.BACKGROUND)  //跳过动画
            diceRolled();
        else
            gameState.setValue(GameState.ROLL_DICE);
    }

    void diceRolled(){
        GameViewModelImpl.this.gameState.setValue(GameState.VACANT);
        currentDice=random.nextInt();
        dice.postValue(currentDice);
        current.diceRolled(currentDice);
    }

    @Override
    public void takeAction(ChessAction action) {
        chessState.addAction(action);
        for (Player i:players)
            if (i!=current)
                i.notifyAction(action);
        if (gameState.getValue()==GameState.BACKGROUND)  //跳过动画
            actionTaken();
        else
            animation.postValue(chessState.getAnimations(chessState.getCurrentActionCount()-1));
    }

    void actionTaken(){
        current.turnFinished();
        if (chessState.isFinished()){
            gameState.postValue(GameState.GAME_OVER);
            return;
        }
        if (currentDice!=6){
            int next=players.indexOf(current)+1;
            if (next==players.size())
                next=0;
            current=players.get(next);
        }
        turnStart();
    }

    class UserInputObserver implements Observer<GameSetting>{
        @Override
        public void onChanged(@Nullable GameSetting gameSetting) {
            if (gameState.getValue()==GameState.BACKGROUND&&roomInfo.isOnline()&&(gameSetting.diceState== GameSetting.DiceState.CLICKABLE||gameSetting.movableChess.size()>0)){
                networkService.notifyTurn();
            }
        }
    }

    class GameStateObserver implements Observer<GameState>{
        @Override
        public void onChanged(@Nullable GameState gameState) {
            if (gameState==GameState.DICE_ROLLED){
                diceRolled();
            }
            if (gameState==GameState.BACKGROUND){
                if (roomInfo.isOnline()){
                    networkService.runInBackground();
                }
            }
        }
    }

    class ChessmanMovedObserver implements Observer<List<ChessAnimation>>{
        @Override
        public void onChanged(@Nullable List<ChessAnimation> chessAnimations) {
            if (chessAnimations==null){
                actionTaken();
            }
        }
    }

    @Override
    public void host() {
        current.hosted();
    }

    @Override
    public void exit() {
        for (Player i:players)
            i.othersExited(current.getPlayerInfo().getPlayerId());
    }

    @Override
    public MutableLiveData<GameSetting> getMutableGameSetting() {
        return gameSetting;
    }

    @Override
    public LiveData<Integer> getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public MutableLiveData<List<ChessAnimation>> getAnimation() {
        return animation;
    }

    @Override
    public List<GroupResult> getResults() {
        return chessState.getResults();
    }
}
