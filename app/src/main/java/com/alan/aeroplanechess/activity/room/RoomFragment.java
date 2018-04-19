package com.alan.aeroplanechess.activity.room;

//import android.app.Fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.model.room.RoomInfo;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zzj on 2018/4/12.
 */

public class RoomFragment extends Fragment {
    @Nullable
    RoomViewModel roomViewModel;
//    Context context;  //Fragment不用持有Context，getContext()即可
    MatchDialogFragment matchDialogFragment;
    ArrayList<ImageButton> imageButtons;
    ArrayList<CircleImageView> circleImageViews;
    WifiDialogFragment wifiDialogFragment;



    //    ArrayList<PlayerFragment> playerFragmentList=new ArrayList<>();
//    Activity activity;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_room,container,false);
        imageButtons=new ArrayList<>(4);
        circleImageViews=new ArrayList<>(4);
//        old_WifiUser=new ArrayList<>();
        return view;
    }
    final Observer<RoomInfo> roomInfoObserver=new Observer<RoomInfo>() {
        @Override
        public void onChanged(@Nullable RoomInfo roomInfo) {
            int i=0;
            for(PlayerInfo p:roomInfo.getPlayerInfoList()){
                imageButtons.get(i).setVisibility(View.VISIBLE);
                circleImageViews.get(i).setImageBitmap(p.getAvatar());
                if(p.getGroupId()==0) {
                    circleImageViews.get(i).setBorderColor(getResources().getColor(R.color.colorGroup1));
                    circleImageViews.get(i).setOnClickListener(new onClickChangeGroup1());
                }
                if(p.getGroupId()==1) {
                    circleImageViews.get(i).setBorderColor(getResources().getColor(R.color.colorGroup2));
                    circleImageViews.get(i).setOnClickListener(new onClickChangeGroup2());
                }
                if(p.getGroupId()==2) {
                    circleImageViews.get(i).setBorderColor(getResources().getColor(R.color.colorGroup3));
                    circleImageViews.get(i).setOnClickListener(new onClickChangeGroup3());
                }
                if(p.getGroupId()==3) {
                    circleImageViews.get(i).setBorderColor(getResources().getColor(R.color.colorGroup4));
                    circleImageViews.get(i).setOnClickListener(new onClickChangeGroup4());
                }

                i++;
            }
            while (i<4){
                imageButtons.get(i).setVisibility(View.INVISIBLE);
                circleImageViews.get(i).setImageBitmap(null);
                i++;
            }

//            FragmentManager fragmentManager=getFragmentManager();
//            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//            int i=0;
//            for(PlayerInfo p:roomInfo.getPlayerInfo()){
//                if(i==0)
//                fragmentTransaction.replace(R.id.playerfragment1,new PlayerFragment().newInstance(p,roomViewModel));
//                if(i==1)
//                    fragmentTransaction.replace(R.id.playerfragment2,new PlayerFragment().newInstance(p,roomViewModel));
//                if(i==2)
//                    fragmentTransaction.replace(R.id.playerfragment3,new PlayerFragment().newInstance(p,roomViewModel));
//                if(i==3)
//                    fragmentTransaction.replace(R.id.playerfragment4,new PlayerFragment().newInstance(p,roomViewModel));
//                i++;
//            }
//            while (i<4){
//                if(i==1)
//                    fragmentTransaction.replace(R.id.playerfragment2,new PlayerFragment());
//                if(i==2)
//                    fragmentTransaction.replace(R.id.playerfragment3,new PlayerFragment());
//                if(i==3)
//                    fragmentTransaction.replace(R.id.playerfragment4,new PlayerFragment());
//                i++;
//            }
//            fragmentTransaction.commit();
        }
    };
   final Observer<RoomViewModel.RoomState> roomStateObserver=new Observer<RoomViewModel.RoomState>() {
       @Override
       public void onChanged(@Nullable RoomViewModel.RoomState roomState) {
           if(roomState== RoomViewModel.RoomState.MATCHING)
           {

               matchDialogFragment.show(getFragmentManager(),"matchDialog");
           }
           if(roomState== RoomViewModel.RoomState.GROUPING){
               if (matchDialogFragment.isVisible())
                    matchDialogFragment.dismiss();
           }
           if(roomState== RoomViewModel.RoomState.DISMISSED)
               getActivity().finish();
       }
   };



    @Override
    public void onStart() {
        super.onStart();
//        context= getActivity();
//
////        MatchDialogFragment =new MatchDialogFragment();
//        roomViewModel= ViewModelProviders.of((FragmentActivity) context).get(RoomViewModelImpl.class);


//        FragmentManager fragmentManager=getFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        PlayerFragment playerFragment1=new PlayerFragment().newInstance(roomViewModel.getRoomInfo().getValue().getPlayerInfo().get(0),roomViewModel);
//        fragmentTransaction.add(R.id.playerfragment1,playerFragment1);
//        PlayerFragment playerFragment2=new PlayerFragment();
//        fragmentTransaction.add(R.id.playerfragment2,playerFragment2);
//        PlayerFragment playerFragment3=new PlayerFragment();
//        fragmentTransaction.add(R.id.playerfragment3,playerFragment3);
//        PlayerFragment playerFragment4=new PlayerFragment();
//        fragmentTransaction.add(R.id.playerfragment4,playerFragment4);
//        fragmentTransaction.commit();







//        playerFragmentList.add(playerFragment1);
//        playerFragmentList.add(playerFragment2);
//        playerFragmentList.add(playerFragment3);
//        playerFragmentList.add(playerFragment4);


//        ImageButton imageButton2=playerFragment2.getview().findViewById(R.id.clear);
//        imageButton2.setOnClickListener(new onClickClear2());
//        ImageButton imageButton3=playerFragment3.getview().findViewById(R.id.clear);
//        imageButton3.setOnClickListener(new onClickClear3());
//        ImageButton imageButton4=playerFragment4.getview().findViewById(R.id.clear);
//        imageButton4.setOnClickListener(new onClickClear4());
//        ImageButton imageButton=playerFragment.getview().findViewById(R.id.clear);
//        CircleImageView circleImageView=playerFragment.getview().findViewById(R.id.avatar);
//        roomViewModel.getWifiUsers().observe(this,wifiUserObserver);


    }
    public void bindViewModel(RoomViewModel viewModel){
        this.roomViewModel=viewModel;
        matchDialogFragment =new MatchDialogFragment().newInstance(roomViewModel);
        Button addLocalPlayer=(Button)view.findViewById(R.id.addLocalPlayer);
        Button addPendingPlayer=(Button)view.findViewById(R.id.addPendingPlayer);
        Button addWifiPlayer=(Button)view.findViewById(R.id.addWifiPlayer);
        Button startLocal=(Button)view.findViewById(R.id.startLocal);
        Button startOnline=(Button)view.findViewById(R.id.startOnline);
        Button exit=(Button)view.findViewById(R.id.exit);
        addLocalPlayer.setOnClickListener(new onClickListerner_LocalPlayer() );
        addPendingPlayer.setOnClickListener(new onClickListerner_PendingPlayer());
        addWifiPlayer.setOnClickListener(new onClickListerner_WifiPlayer());
        startLocal.setOnClickListener(new onClickListerner_startLocal());
        startOnline.setOnClickListener(new onClickListerner_startOnline());
        exit.setOnClickListener(new onClickListerner_exit());

        imageButtons.add((ImageButton)view.findViewById(R.id.player1).findViewById(R.id.clear));
        imageButtons.get(0).setOnClickListener(new onClickClear1());
        circleImageViews.add((CircleImageView)view.findViewById(R.id.player1).findViewById(R.id.avatar));
        circleImageViews.get(0).setOnClickListener(new onClickChangeGroup1());
        circleImageViews.get(0).setImageBitmap(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(0).getAvatar());
        imageButtons.add((ImageButton)view.findViewById(R.id.player2).findViewById(R.id.clear));
        imageButtons.get(1).setOnClickListener(new onClickClear2());
        circleImageViews.add((CircleImageView)view.findViewById(R.id.player2).findViewById(R.id.avatar));
        circleImageViews.get(1).setOnClickListener(new onClickChangeGroup2());
        imageButtons.add((ImageButton)view.findViewById(R.id.player3).findViewById(R.id.clear));
        imageButtons.get(2).setOnClickListener(new onClickClear3());
        circleImageViews.add((CircleImageView)view.findViewById(R.id.player3).findViewById(R.id.avatar));
        circleImageViews.get(2).setOnClickListener(new onClickChangeGroup3());
        imageButtons.add((ImageButton)view.findViewById(R.id.player4).findViewById(R.id.clear));
        imageButtons.get(3).setOnClickListener(new onClickClear4());
        circleImageViews.add((CircleImageView)view.findViewById(R.id.player4).findViewById(R.id.avatar));
        circleImageViews.get(3).setOnClickListener(new onClickChangeGroup4());

        imageButtons.get(1).setVisibility(View.INVISIBLE);
        imageButtons.get(2).setVisibility(View.INVISIBLE);
        imageButtons.get(3).setVisibility(View.INVISIBLE);

        roomViewModel.getRoomInfo().observe(this,roomInfoObserver);
        roomViewModel.getRoomState().observe(this,roomStateObserver);
    }

    class onClickListerner_LocalPlayer implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.addLocalPlayer();
        }
    }
    class onClickListerner_PendingPlayer implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.addPendingPlayer();
        }
    }
    class onClickListerner_WifiPlayer implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            wifiDialogFragment=new WifiDialogFragment().newInstance(roomViewModel);
            wifiDialogFragment.show(getFragmentManager(),"addwifiuser");
        }
    }
    class onClickListerner_startLocal implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.startLocal(getContext());
        }
    }class onClickListerner_startOnline implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.startOnline(getContext());
        }
    }
    class onClickListerner_exit implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.exit();
        }
    }


    class onClickClear1 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(0));
        }
    }
    class onClickChangeGroup1 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.changeGroup(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(0));
        }
    }
    class onClickClear2 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(1));
        }
    }
    class onClickChangeGroup2 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.changeGroup(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(1));
        }
    }
    class onClickClear3 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(2));
        }
    }
    class onClickChangeGroup3 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.changeGroup(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(2));
        }
    }
    class onClickClear4 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(3));
        }
    }
    class onClickChangeGroup4 implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            roomViewModel.changeGroup(roomViewModel.getRoomInfo().getValue().getPlayerInfoList().get(3));
        }
    }


//    class onClickClear2 implements View.OnClickListener{
//        @Override
//        public void onClick(View v) {
//            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfo().get(2));
//        }
//    }
//    class onClickClear3 implements View.OnClickListener{
//        @Override
//        public void onClick(View v) {
//            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfo().get(3));
//        }
//    }
//    class onClickClear4 implements View.OnClickListener{
//        @Override
//        public void onClick(View v) {
//            roomViewModel.removePlayer(roomViewModel.getRoomInfo().getValue().getPlayerInfo().get(4));
//        }
//    }

}
