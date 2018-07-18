package com.newwebinfotech.rishabh.parkingapp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.newwebinfotech.rishabh.parkingapp.ChatRoomActivity;
import com.newwebinfotech.rishabh.parkingapp.R;
import com.newwebinfotech.rishabh.parkingapp.adapter.CustomListAdapter;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.model.RecentChatList;

import java.util.ArrayList;
import java.util.List;


public class RecentChatFragment extends ListFragment {
    View view;
    ListFragmentItemClickListener ifaceItemClickListener;
    ArrayList<RecentChatList> recentItems;
    List<String> list_names;
    List<String> list_chats;
    List<String> list_pics;
    List<String> list_phone_nos;
    List<String> list_entity_types;
    List<String> list_read_unread_sts;
    List<String> list_last_chat_time;
    List<String> delivery_status;
    DBOperation dbOperation;
    DBOperation.DatabaseHelper dbhelper;
    RecentChatList recentchats;
    boolean isREAD = true;
    public CustomListAdapter adapter;
    AdView adView;

    public RecentChatFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_recent_chat,null);
        adView = (AdView) view.findViewById(R.id.search_ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            /** This statement ensures that the hosting activity implements ListFragmentItemClickListener */
            ifaceItemClickListener = (ListFragmentItemClickListener) activity;
        }catch(Exception e){
            Toast.makeText(activity.getBaseContext(), "Exception",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /** Invokes the implementation of the method onListFragmentItemClick in the hosting activity */
        ifaceItemClickListener.onListFragmentItemClick(position);
        //Toast.makeText(getActivity(), "aaaaaa", Toast.LENGTH_LONG).show();
        RecentChatList r = recentItems.get(position);

        String chattingToName = r.getCHAT_PERSON_NAME().toString();
        String chattingToPhNo = r.getPERSON_PHONE_NO().toString();
        String chattingToEntity_Type = r.getRECENT_PERSON_ENTITY_TYPE().toString();
        String chattingToPic = r.getCHAT_PERSON_PIC().toString();
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra("chattingToName", chattingToName);
        if(!chattingToEntity_Type.equalsIgnoreCase("group")){
            intent.putExtra("chattingToPhone", chattingToPhNo);
        }else{
            intent.putExtra("chattingToPhone", chattingToPhNo);
        }
        intent.putExtra("chattingToPic", chattingToPic);
        intent.putExtra("chattingToEntityType", chattingToEntity_Type);
        startActivity(intent);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public interface ListFragmentItemClickListener
    {
        /** This method will be invoked when an item in the ListFragment is clicked */
        void onListFragmentItemClick(int position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("RECENT_TEXT_CHAT_MESSAGE_RECEIVED"));
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("RECENT_IMAGE_RECEIVED"));
        recentItems = new ArrayList<RecentChatList>();
        recentchats = new RecentChatList();

        dbOperation = new DBOperation(getActivity());
        dbhelper =  dbOperation.new DatabaseHelper(getActivity());

        registerForContextMenu(this.getListView());

        list_names = new ArrayList<String>();
        list_chats = new ArrayList<String>();
        list_pics = new ArrayList<String>();
        list_phone_nos = new ArrayList<String>();
        list_entity_types = new ArrayList<String>();
        list_read_unread_sts = new ArrayList<String>();
        list_last_chat_time = new ArrayList<String>();
        delivery_status = new ArrayList<String>();

        list_names = dbhelper.getAllNames();
        list_chats = dbhelper.getAllUserChats();
        list_pics = dbhelper.getAllUserPics();
        list_phone_nos = dbhelper.getAllUserPhoneNos();
        list_entity_types = dbhelper.getAllUserEntityTypes();
        list_read_unread_sts = dbhelper.getAllUserReadUnreadStatus();
        list_last_chat_time = dbhelper.getAllUserLastChatTime();
        delivery_status = dbhelper.getAllUserDeliveryStatus();


        String[] unames = list_names.toArray(new String[list_names.size()]);
        String[] uchats = list_chats.toArray(new String[list_chats.size()]);
        String[] upics = list_pics.toArray(new String[list_pics.size()]);
        String[] uphone_nos = list_phone_nos.toArray(new String[list_phone_nos.size()]);
        String[] uentity_types = list_entity_types.toArray(new String[list_entity_types.size()]);
        String[] uread_unread_sts = list_read_unread_sts.toArray(new String[list_read_unread_sts.size()]);
        String[] ulast_chat_time = list_last_chat_time.toArray(new String[list_last_chat_time.size()]);
        String[] udelivery_status = delivery_status.toArray(new String[delivery_status.size()]);


        int usrNameCount = unames.length;


        for (int i = 0; i < usrNameCount; i++) {
            if(uentity_types[i].equalsIgnoreCase("one_to_one")){
                if(upics[i]==null){
                    recentchats = new RecentChatList(unames[i],"",uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }else{
                    recentchats = new RecentChatList(unames[i],upics[i],uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }
            }
            if(uentity_types[i].equalsIgnoreCase("broadcast")){
                if(upics[i]==null){
                    recentchats = new RecentChatList(unames[i],"",uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }else{
                    recentchats = new RecentChatList(unames[i],upics[i],uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }
            }
            if(uentity_types[i].equalsIgnoreCase("group")){
                if(upics[i]==null){
                    recentchats = new RecentChatList(unames[i],"",uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }else{
                    recentchats = new RecentChatList(unames[i],upics[i],uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }
            }

            if(uentity_types[i].equalsIgnoreCase("bigbroadcast")){
                if(upics[i]==null){
                    recentchats = new RecentChatList(unames[i],"",uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }else{
                    recentchats = new RecentChatList(unames[i],"",uchats[i],uphone_nos[i],uentity_types[i],uread_unread_sts[i],ulast_chat_time[i],udelivery_status[i]);
                }
            }

            recentItems.add(0,recentchats);
        }
        recentItems.clear();
        recentItems = (ArrayList<RecentChatList>) dbhelper.getRefreshedRecentChatListByTimestamp();
        //adapter.notifyDataSetChanged();
        adapter = new CustomListAdapter(getActivity(), recentItems);
        setListAdapter(adapter);
    }



    public void onIncomingMessageRefreshRecentChats()
    {
        recentItems.clear();
        recentItems = (ArrayList<RecentChatList>) dbhelper.getRefreshedRecentChatListByTimestamp();
        adapter.notifyDataSetChanged();
        adapter = new CustomListAdapter(getActivity(), recentItems);
        setListAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position;
        switch (item.getItemId()) {

            case R.id.delete_item: // <-- your custom menu item id here
                position = (int) info.position;
                RecentChatList recentObjDel = (RecentChatList)getListView().getAdapter().getItem(position);
                int i = dbhelper.deleteFromRecentChatListByPhone(recentObjDel.getPERSON_PHONE_NO());
                int j = dbhelper.deleteChatsByPhone(recentObjDel.getPERSON_PHONE_NO());


                if(i!=-1 && j!=-1){
                    isREAD = true;

                    Log.i("RECENT CHAT LIST", "ITEM DELETED FROM THE LIST");
                    recentItems.clear();
                    recentItems = (ArrayList<RecentChatList>) dbhelper.getRefreshedRecentChatListByTimestamp();
                    adapter.notifyDataSetChanged();
                    adapter = new CustomListAdapter(getActivity(), recentItems);
                    setListAdapter(adapter);

                }
                break;


            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle b = intent.getExtras();

            if(intent.hasExtra("message")){
                String message = b.getString("message");
                String sender_phoneno = b.getString("sender_phone");

                onIncomingMessageRefreshRecentChats();
            }



        }

    };


}
