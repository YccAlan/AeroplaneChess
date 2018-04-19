package com.alan.aeroplanechess.activity.room;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.model.room.WifiUser;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zzj on 2018/4/13.
 */

public class WifiDialogFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    RoomViewModel roomViewModel;
    List<WifiUser> selectedWifiUsers;
    HashSet<WifiUser> tempUsers;
    HashMap<WifiUser,View> items;
    View view;
    //    ArrayList<PlayerInfo> playerInfoArrayList=null;
//
    List<WifiUser> old_WifiUser;
    public WifiDialogFragment newInstance(RoomViewModel roomViewModel){
        WifiDialogFragment wifiDialogFragment =new WifiDialogFragment();
        wifiDialogFragment.roomViewModel=roomViewModel;
        return wifiDialogFragment;
    }
    final Observer<List<WifiUser>> wifiUserObserver=new Observer<List<WifiUser>>() {
        @Override
        public void onChanged(@Nullable List<WifiUser> wifiUserList) {
            LinearLayout linearLayout=new LinearLayout(getContext());
            if(old_WifiUser.size()<roomViewModel.getWifiUsers().getValue().size()) {
                for (int i=old_WifiUser.size();i<roomViewModel.getWifiUsers().getValue().size();i++) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    LayoutInflater inflater = getLayoutInflater();
                    View subView = inflater.inflate(R.layout.wifiuser_item, null);
                    TextView wifiuser = (TextView) subView.findViewById(R.id.user);
                    wifiuser.setText(roomViewModel.getWifiUsers().getValue().get(i).name);
                    subView.setOnClickListener(new onClickListern_check(roomViewModel.getWifiUsers().getValue().get(i)));
                    subView.setLayoutParams(lp);
                    linearLayout.addView(subView);
                    items.put(roomViewModel.getWifiUsers().getValue().get(i),subView);
                }

            }
            else {
                for (int i=0;i<roomViewModel.getWifiUsers().getValue().size();i++){
                    if(old_WifiUser.get(i)!=roomViewModel.getWifiUsers().getValue().get(i)){
                        linearLayout.removeView(items.get(old_WifiUser.get(i)));
                        old_WifiUser=roomViewModel.getWifiUsers().getValue();
                        break;
                    }
                    linearLayout.removeView(items.get(old_WifiUser.get(old_WifiUser.size()-1)));
                    old_WifiUser=roomViewModel.getWifiUsers().getValue();
                    break;
                }
            }
            Button ack = (Button) view.findViewById(R.id.ack);
            ack.setOnClickListener(new onClickListerner_addwifiplayer());
        }
    };
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(), R.layout.addwifi_dialogfragment, null);
        dialog.setContentView(view);

        roomViewModel.getWifiUsers().observe(this,wifiUserObserver);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        roomViewModel.discoverWifiUser();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void doclick(View v)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    class onClickListerner_addwifiplayer implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            for (Iterator it = tempUsers.iterator(); it.hasNext();){
                selectedWifiUsers.add((WifiUser)it.next());
            }
            roomViewModel.inviteWifiPlayer(selectedWifiUsers);
        }
    }

    class onClickListern_check implements View.OnClickListener{
        WifiUser w;
        public onClickListern_check(WifiUser w){
            this.w=w;
        }
        @Override
        public void onClick(View v) {
            CheckBox checkBox=(CheckBox)v.findViewById(R.id.check);
            if(!checkBox.isChecked()){
                tempUsers.add(w);
                checkBox.setChecked(true);
            }
            if(checkBox.isChecked())
            {
                tempUsers.remove(w);
                checkBox.setChecked(false);
            }
        }
    }

}
