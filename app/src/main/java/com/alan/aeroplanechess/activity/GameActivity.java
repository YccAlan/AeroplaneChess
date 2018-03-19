package com.alan.aeroplanechess.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.model.game.ChessAnimation;
import com.alan.aeroplanechess.model.game.impl.ChessActionImpl;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.view.ChessmanDrawer;
import com.alan.aeroplanechess.view.Dice;
import com.alan.aeroplanechess.view.Message;
import com.alan.aeroplanechess.view.PlayerView;
import com.alan.aeroplanechess.viewModel.game.GameSetting;
import com.alan.aeroplanechess.viewModel.game.GameViewModel;
import com.alan.aeroplanechess.viewModel.game.impl.GameViewModelImpl;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    int playerViewId[]={R.id.player0,R.id.player1,R.id.player2,R.id.player3};

    GameViewModel gameViewModel;
    MutableLiveData<GameViewModel.GameState> gameState;
    LiveData<Integer> currentPlayer;
    LiveData<GameSetting> gameSetting;
    LiveData<Integer> dice;
    LiveData<Integer> countDown;
    MutableLiveData<List<ChessAnimation>> animations;
    List<LiveData<PlayerInfo>> playersInfo;

    SparseArray<PlayerView> playerViews=new SparseArray<>(4);
    ChessmanDrawer chessmanDrawer;
    Dice diceView;
    Message messageView;
    Button aiView;
    TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameViewModel= ViewModelProviders.of(this).get(GameViewModelImpl.class);
        gameState=gameViewModel.getGameState();
        currentPlayer=gameViewModel.getCurrentPlayer();
        gameSetting=gameViewModel.getGameSetting();
        dice=gameViewModel.getDice();
        countDown=gameViewModel.getCountDown();
        animations=gameViewModel.getAnimations();
        playersInfo=gameViewModel.getPlayersInfo();

        aiView=findViewById(R.id.ai);
        chessmanDrawer=new ChessmanDrawer(findViewById(R.id.map), gameViewModel.getStates(),this);
        diceView=new Dice(findViewById(R.id.dice),this);
        messageView=new Message(findViewById(R.id.message),this);
        for (LiveData<PlayerInfo> i:playersInfo){
            PlayerView playerView=new PlayerView(i.getValue(),findViewById(playerViewId[i.getValue().getPlayerId()]),this);
            i.observe(this,playerInfo -> playerView.setStatus(playerInfo.getState()));
            playerViews.put(i.getValue().getPlayerId(),playerView);
        }

        aiView.setOnClickListener(view -> gameViewModel.host());
        gameState.observe(this, gameState -> {
            if (gameState== GameViewModel.GameState.ROLL_DICE)
                diceView.row(()->{this.gameState.postValue(GameViewModel.GameState.DICE_ROLLED);});
        });
        currentPlayer.observe(this, new Observer<Integer>() {
            int lastPlayer=-1;
            @Override
            public void onChanged(@Nullable Integer currentPlayer) {
                if (lastPlayer!=-1)
                    playerViews.get(lastPlayer).setActive(false);
                lastPlayer=currentPlayer;
                playerViews.get(currentPlayer).setActive(true);
            }
        });
        gameSetting.observe(this,gameSetting -> {
            chessmanDrawer.setMovableChessman(gameSetting.movableChess,id->{
                gameSetting.movableChess=null;
                chessmanDrawer.setMovableChessman(null,null);
                gameViewModel.takeAction(new ChessActionImpl(id,dice.getValue()));
            });
            if (gameSetting.diceState== GameSetting.DiceState.CLICKABLE){
                    diceView.setClickable(true,()->gameState.postValue(GameViewModel.GameState.DICE_ROLLED));
            }
            else
                diceView.setClickable(false,null);
            switch (gameSetting.aiState){
                case ON:
                    aiView.setClickable(true);
                    aiView.setVisibility(View.VISIBLE);
                    break;
                case OFF:
                    aiView.setClickable(false);
                    aiView.setVisibility(View.VISIBLE);
                    break;
                case DISABLED:
                    aiView.setVisibility(View.GONE);
                    break;
            }
        });
        dice.observe(this,diceValue -> diceView.setValue(diceValue));
        countDown.observe(this,countDown ->timeView.setText(String.valueOf(countDown)) );
        animations.observe(this,chessAnimations -> {
                if (animations!=null)
                    chessmanDrawer.showAnimation(chessAnimations,()->animations.setValue(null));
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameState.postValue(GameViewModel.GameState.BACKGROUND);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chessmanDrawer.updateStates(gameViewModel.getStates());
    }
}
