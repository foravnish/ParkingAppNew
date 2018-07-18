package com.newwebinfotech.rishabh.parkingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.Getseter;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.newwebinfotech.rishabh.parkingapp.database.DatabaseHandler;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;
import com.newwebinfotech.rishabh.parkingapp.utils.L;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DemoFragment extends Fragment {
    DatabaseHandler db;
    List<Getseter> DataList = new ArrayList<Getseter>();
    Adapter adapter;
    GridView gridview;
    Button click;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    AdView adView;
    JSONArray jsonArray;
    FloatingActionButton floating_action;
    private SetLocation setLocation;

    public DemoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(setLocation!=null){
            setLocation.onAttach(context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(setLocation!=null){
            setLocation.onDetach();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        // db = new DatabaseHandler(getActivity());
        //DataList=db.getAllCatagory();
        gridview = (GridView) view.findViewById(R.id.gridview);
        progressDialog = new ProgressDialog(getActivity());
        floating_action = (FloatingActionButton) view.findViewById(R.id.floating_action);


        adView = (AdView) view.findViewById(R.id.search_ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);


        sp = getActivity().getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
        Log.d("gdfhfddhfghfg", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));

        //progressDialog.show();

//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("dfgdffgdfgdhgd",DataList.get)
//            }
//        });

        floating_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Update later",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MultiuserTracking.class);
                intent.putExtra("latitude", "");
                intent.putExtra("longitude", "");
                intent.putExtra("time", "");
                intent.putExtra("name", "");
                intent.putExtra("json", jsonArray != null ? jsonArray.toString() : "");
                intent.putExtra("type", "1");
                intent.putExtra("image", "");
                startActivity(intent);
            }
        });

        executeLogger();
//        setLocation.execute();


//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://parkingeye.in/app/parking_eye/api/track_multiple_users.php?user_phoneno="+sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""),  new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Log.d("gdfhfddhfghfg", response.toString());
//                DataList.clear();
//                progressDialog.dismiss();
//                try {
//                    //JSONObject jsonObject=response.getJSONObject("Count_List_Brand");
//                    if (response.optString("status").equals("1")){
//                        Log.d("gfgfdhfhggh","true");
//                        JSONArray jsonArray=response.getJSONArray("message");
//                        for (int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                            DataList.add(new Getseter(jsonObject1.optString("userName"),jsonObject1.optString("mobile"),jsonObject1.optString("vehicleNo"),jsonObject1.optString("latitude"),jsonObject1.optString("longitude"),null,null,null));
//                        }
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                adapter=new Adapter();
//                gridview.setAdapter(adapter);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        App.getInstance().addToRequestQueue(jsonObjectRequest);


//        gridview.setAdapter(adapter);

//                db.deleteCatogory();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), Maps.class);
                Intent intent = new Intent(getActivity(), UserTracking.class);
                intent.putExtra("latitude", DataList.get(position).getCount());
                intent.putExtra("longitude", DataList.get(position).getImg());
                intent.putExtra("cMobile", DataList.get(position).getName());
                intent.putExtra("time", "");
                intent.putExtra("name", DataList.get(position).getID() + " " + DataList.get(position).getDesc());
                intent.putExtra("json", "");
                intent.putExtra("type", "");
                intent.putExtra("image", DataList.get(position).getUdate());


                startActivity(intent);
                Log.d("gdfgdfgdfd", DataList.get(position).getCount());
                Log.d("gdfgdfgdfd", DataList.get(position).getImg());
//                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void executeLogger() {
        if (setLocation != null && setLocation.getStatus() != AsyncTask.Status.FINISHED) {
            L.m("return");
            return;
        }
        setLocation = new SetLocation(getActivity());
        if (Build.VERSION.SDK_INT >= 16)
            setLocation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            setLocation.execute();
    }

    class Viewholder {
        TextView time;
    }

    class Adapter extends BaseAdapter {

        NetworkImageView image;
        LayoutInflater inflater;
        TextView userName, userCurrentStatus, number, distance, mobile;
        int bal;
        int np1;
        public int counter;
        CardView card_view;

        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int position) {
            return DataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.custonlistview_cart, parent, false);

            final Viewholder viewholder = new Viewholder();
            userName = (TextView) convertView.findViewById(R.id.userName);
            userCurrentStatus = (TextView) convertView.findViewById(R.id.userCurrentStatus);
            number = (TextView) convertView.findViewById(R.id.number);
            distance = (TextView) convertView.findViewById(R.id.distance);
            mobile = (TextView) convertView.findViewById(R.id.mobile);
            viewholder.time = (TextView) convertView.findViewById(R.id.time);

//            card_view=(CardView)convertView.findViewById(R.id.card_view);

            CircleImageView image = (CircleImageView) convertView.findViewById(R.id.profile_image);
            Picasso.with(getActivity()).load(DataList.get(position).getUdate().toString()).error(R.drawable.ic_launcher).resize(200, 200).centerCrop().into(image);
//            Picasso.with(getActivity()).load(DataList.get(position).getUdate().toString()).resize(200,200).centerCrop().into(image);


            userName.setText(DataList.get(position).getID().toString());

//             viewholder.name.setText(DataList.get(position).getName().toString());

            userCurrentStatus.setText(DataList.get(position).getDesc().toString());
            number.setText(DataList.get(position).getCdate().toString());
            mobile.setText(DataList.get(position).getName().toString());
            distance.setText(DataList.get(position).getUdate3().toString() + " km");


            long timer = Long.parseLong(DataList.get(position).getDelTime().toString());
            timer = timer * 1000 * 60;
////
            new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    int day = (int) ((millisUntilFinished / (1000 * 60 * 60 * 24)) % 30);
                    viewholder.time.setText("Left Time: " + String.format("%dD %d:%d:%d", day, hours, minutes, seconds));
                }

                public void onFinish() {
                    viewholder.time.setText("00:00:00");
                }
            }.start();

//
//            viewholder.time.setText("Left Time: " +DataList.get(position).getDelTime().toString());

            return convertView;
        }
    }


    private class SetLocation extends AsyncTask<String, Void, String> {
        Context context;
        ProgressDialog progressDialog;
        String url;
        boolean isValidation;
        String time;

        public void onAttach(Context context) {
            this.context = context;
        }

        public void onDetach() {
            this.context = null;
        }

        public SetLocation(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            this.time = time;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE, ""));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/track_multiple_users.php", "POST", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(context!=null){
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                Log.e("response", ": " + s);
                Log.d("dfgdfhgbdfhdgdfdgh123", s.toString());
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.optString("status").equals("1")) {
                        Log.d("gfgfdhfhggh", "true");
                        DataList.clear();
                        jsonArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            DataList.add(new Getseter(jsonObject1.optString("userName"), jsonObject1.optString("mobile"), jsonObject1.optString("vehicleNo"), jsonObject1.optString("latitude"), jsonObject1.optString("longitude"), jsonObject1.optString("city") + ", " + jsonObject1.optString("country"), jsonObject1.optString("userProfilePicture"), jsonObject1.optString("distance"), jsonObject1.optString("remainTime")));

                            Log.d("fdgdfdggdfgdfdg", jsonObject1.optString("remainTime"));

//                        if (!jsonObject1.optString("remainTime").toString().equals("0")) {
//                            Log.d("dfgdgdfggdfg","inside");
//                            DataList.add(new Getseter(jsonObject1.optString("userName"), jsonObject1.optString("mobile"), jsonObject1.optString("vehicleNo"), jsonObject1.optString("latitude"), jsonObject1.optString("longitude"), jsonObject1.optString("city") + ", " + jsonObject1.optString("country"), jsonObject1.optString("userProfilePicture"), jsonObject1.optString("distance"), jsonObject1.optString("remainTime")));
//                        }
//                        else{
//                            Log.d("dfgdgdfggdfg","outside");
//                         //   Toast.makeText(getActivity(), "There are no user here...", Toast.LENGTH_SHORT).show();
//                            break;
//                        }

                        }

                        adapter = new Adapter();
                        gridview.setAdapter(adapter);
                    } else if (jsonObject.optString("status").equals("0")) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            //  startActivity(new Intent(getActivity(),MainScreenActivity.class) );

        }


    }
}
