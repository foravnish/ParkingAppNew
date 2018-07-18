package com.newwebinfotech.rishabh.parkingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Getseter;
import com.newwebinfotech.rishabh.parkingapp.database.DBOperation;
import com.newwebinfotech.rishabh.parkingapp.fragment.RecentChatFragment;
import com.newwebinfotech.rishabh.parkingapp.model.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainScreenActivity extends AppCompatActivity implements RecentChatFragment.ListFragmentItemClickListener {

    SharedPreferences sp;
    DBOperation dbOperation;
    DBOperation.DatabaseHelper dbhelper;
    Boolean deletedb;
    JSONArray jsonArray;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        dbOperation = new DBOperation(this);
        dbhelper = dbOperation.new DatabaseHelper(getApplicationContext());

        checkVersion();

        sp = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(1);


///////
        if (getIntent().getStringExtra("logoutcheck").equals("1")){

        }
        else {
            new CheckLogoutOrNot().execute();
        }
//////

    }

//    private class SetLocation extends AsyncTask<String, Void, String>{
//        Context context;
//        ProgressDialog progressDialog;
//        String url;
//        boolean isValidation;
//        String time;
//
//        public SetLocation(Context context) {
//            this.context = context;
//            progressDialog = new ProgressDialog(context);
//          //  progressDialog.show();
//            this.time=time;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setCancelable(false);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            HashMap<String, String> params = new HashMap<>();
//            params.put("user_phoneno", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));
//
//
//            JSONParser jsonParser = new JSONParser();
//            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/track_multiple_users.php", "POST", params);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressDialog.dismiss();
//
//
//            JSONObject jsonObject = null;
//            Log.d("dfgdfhcbcgbfdddfd",s.toString());
//            try {
//                jsonObject= new JSONObject(s);
//                if (jsonObject.optString("status").equals("1")){
//                    Log.d("gfgfdhfhggh","true");
//
//                    jsonArray=jsonObject.getJSONArray("message");
//                    for (int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//
//
//                        Log.d("fdgdfdggdfgdfdg",jsonObject1.optString("remainTime"));
//
////                        if (!jsonObject1.optString("remainTime").toString().equals("0")) {
////                            Log.d("dfgdgdfggdfg","inside");
////                            DataList.add(new Getseter(jsonObject1.optString("userName"), jsonObject1.optString("mobile"), jsonObject1.optString("vehicleNo"), jsonObject1.optString("latitude"), jsonObject1.optString("longitude"), jsonObject1.optString("city") + ", " + jsonObject1.optString("country"), jsonObject1.optString("userProfilePicture"), jsonObject1.optString("distance"), jsonObject1.optString("remainTime")));
////                        }
////                        else{
////                            Log.d("dfgdgdfggdfg","outside");
////                         //   Toast.makeText(getActivity(), "There are no user here...", Toast.LENGTH_SHORT).show();
////                            break;
////                        }
//
//                    }
//
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            //  startActivity(new Intent(getActivity(),MainScreenActivity.class) );
//
//        }
//
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(MainScreenActivity.this, SettingsActivity.class);
            i.putExtra("logoutcheck","");
            startActivity(i);
            return true;
        } else if (id == R.id.action_help) {
            Intent i = new Intent(MainScreenActivity.this, HelpActivity.class);
            i.putExtra("logoutcheck","");
            startActivity(i);
            return true;
        } else if (id == R.id.action_logout) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
            builder.setCancelable(false);
            //builder.setMessage("Are you sure you want to log out?");
            builder.setMessage("Do you want to delete previous chats from your phone before logout.");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    deletedb = true;
                    logout();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

//                    deletedb = false;
//                    logout();

                }
            });

            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            final AlertDialog alert = builder.create();

            alert.show();


        } else if (id == R.id.action_share) {

            String shareBody = "https://play.google.com/store/apps/details?id=com.newwebinfotech.rishabh.parkingapp";

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Parking Eye (Download this app to solve your parking problems.)");

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            if (position == 0) {
                Fragment frag = new ProfileFragment();
                //Fragment frag = new PlaceholderFragment();
                //Bundle args = new Bundle();
                //args.putString(Config.BUNDLE_KEY_URL, Config.FEVER_UPCOMING_EVENT_URL);
                //frag.setArguments(args);
                return frag;
            } else if (position == 1) {
                Fragment frag = new SearchFragment();
                return frag;
            } else if (position == 2) {
                Fragment frag = new RecentChatFragment();
                return frag;
            }
            else {
//                Bundle bundle=new Bundle();
//                bundle.putString("json",jsonArray.toString());
                Fragment frag = new DemoFragment();
//                frag.setArguments(bundle);
                //Fragment frag = new DemoFragment();
                return frag;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Search";
                case 2:
                    return "Chat";
                case 3:
                    return "Tracker";
            }
            return null;
        }
    }


    @Override
    public void onListFragmentItemClick(int position) {


    }

    private void checkVersion() {
        ConnectivityManager cm = (ConnectivityManager) MainScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            new VersionCheckAsyncTask().execute();
        } else {
            AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
            build.setCancelable(false);
            build.setMessage("Error connecting to internet. Please, check network connection.");

            build.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = build.create();
            alert.show();

        }
    }

    private class VersionCheckAsyncTask extends AsyncTask<Void, Void, String> {

        String s;
        private ProgressDialog progress;

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Config.KEY_VERSION, Config.APP_VERSION);

            String result = QueryUtils.getVersion(map, Config.VERSION_CHECK_URL);

            //  Log.v("rrrrrr", result);


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainScreenActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(final String str) {

            progress.dismiss();

            String success = QueryUtils.extractVersionCheckSuccessFromJson(str);
            // Log.v("vvvvvv", success);

            if (success.equals("1")) {
                s = QueryUtils.extractVersionCheckFromJson(str);


                //   Log.v("ssssss", s);


                final AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
                build.setCancelable(false);

                if (s.equals("-1")) {
                    build.setMessage(getResources().getString(R.string.alert_app_maintenance));
                } else if (s.equals("0")) {
                    build.setMessage(getResources().getString(R.string.alert_app_update));
                } else if (s.equals("5")) {
                    build.setMessage(getResources().getString(R.string.alert_something_went_wrong));
                }

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                                if (s.equals("-1") || s.equals("5")) {

                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                    editor.apply();

                                    finishAffinity();
                                } else if (s.equals("0")) {

                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                    editor.apply();

                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.newwebinfotech.rishabh.parkingapp")));
                                /*final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }*/
                                }
                            }
                        });

                if (!s.equals("1")) {
                    AlertDialog alert = build.create();
                    alert.show();
                }

            } else {
                final AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
                build.setCancelable(false);
                build.setMessage(getResources().getString(R.string.alert_something_went_wrong));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();

            }
        }
    }

    private void logout() {
        ConnectivityManager cm = (ConnectivityManager) MainScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            new LogoutkAsyncTask().execute();
        } else {
            AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
            build.setCancelable(false);
            build.setMessage("Error connecting to internet. Please, check network connection.");

            build.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = build.create();
            alert.show();

        }
    }

    private class LogoutkAsyncTask extends AsyncTask<Void, Void, String> {

        String s;
        private ProgressDialog progress;

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mobile", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));

            String result = QueryUtils.logout(map);

            //  Log.v("rrrrrr", result);


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainScreenActivity.this);
            progress.setMessage("Signing out...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(String str) {

            progress.dismiss();

            String success = QueryUtils.extractLogoutSuccessFromJson(str);
            Log.v("vvvvvv", str);

            if (success.equals("1")) {

                if (deletedb) {
                    dbhelper.deleteChatFromDB();
                    dbhelper.deleteRecentChatFromDB();


                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                    editor.apply();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("You have been logged out.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishAffinity();
                            //Intent i = new Intent(MainScreenActivity.this, SplashScreen.class);
                            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //startActivity(i);
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                } else {
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                    editor.apply();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("You have been logged out.");
                    builder.setPositiveButton(getResources().getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishAffinity();
//                            Intent i = new Intent(MainScreenActivity.this, SplashScreen.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(i);
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();
                }

            } else if (success.equals("0")) {
                String error = QueryUtils.extractLogoutErrorFromJson(str);
                final AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
                build.setCancelable(false);
                build.setMessage(error);

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();
            }
            else if (success.equals("-1")) {
                String error = QueryUtils.extractLogoutErrorFromJson(str);
                final AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
                build.setCancelable(false);
                build.setMessage(getResources().getString(R.string.token_expired));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                SharedPreferences s = getSharedPreferences(Config.PREF_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = s.edit();

                                editor.putString(Config.PREF_KEY_PRIMARY_MOBILE, "");
                                editor.apply();
                                finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();
            }else {
                final AlertDialog.Builder build = new AlertDialog.Builder(MainScreenActivity.this);
                build.setCancelable(false);
                build.setMessage(getResources().getString(R.string.alert_something_went_wrong));

                build.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finishAffinity();
                            }
                        });

                AlertDialog alert = build.create();
                alert.show();

            }
        }
    }

    private class CheckLogoutOrNot extends AsyncTask<Void, Void, String> {

        private ProgressDialog progress;

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mobileNo", sp.getString(Config.PREF_KEY_PRIMARY_MOBILE,""));
            map.put("tokenNo", sp.getString(Config.PREF_KEY_TOKEN,""));

            Log.d("dgfdgfdgdfgdhdbgfdgd",sp.getString(Config.PREF_KEY_TOKEN,""));
            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest("http://parkingeye.in/app/parking_eye/api/check_token_for_logout.php", "POST", map);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String str) {

            JSONObject jsonObject = null;
            Log.d("fgdfgdfdhfdgd",str.toString());
            try {
                jsonObject= new JSONObject(str);
                if (jsonObject.optString("Success").equals("1")){
                    Log.d("jkhkhjk","true");
                }
                else {
                    Log.d("jkhkhjk","false");
                    deletedb = true;
                    logout();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
