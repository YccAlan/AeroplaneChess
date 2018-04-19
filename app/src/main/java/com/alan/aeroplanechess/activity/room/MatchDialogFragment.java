package com.alan.aeroplanechess.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

/**
 * Created by zzj on 2018/4/13.
 */

public class MatchDialogFragment extends DialogFragment {
    @Nullable
    RoomViewModel roomViewModel=null;

    public MatchDialogFragment newInstance(RoomViewModel roomViewModel){
        MatchDialogFragment matchDialogFragment=new MatchDialogFragment();
        matchDialogFragment.roomViewModel=roomViewModel;
        return matchDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialogfragment_match,container,false);
        Button cancel=(Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new onClickListernCancel());
        return view;
    }
    class onClickListernCancel implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.cancelMatch();
        }
    }
}
