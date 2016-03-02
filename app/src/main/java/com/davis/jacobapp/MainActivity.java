package com.davis.jacobapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = getClass().getSimpleName() + " findme ";
    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("1095ABC");

        final ListView listView = (ListView) findViewById(R.id.layout_videos);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Video");

        try {
            query.orderByAscending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objectsList, ParseException e) {
                    if (e == null) {
                        // no errors, create imageadapter with all the images, send to listview
                        // runs in UI thread
                        listView.setAdapter(new ImageAdapter(objectsList, getApplicationContext(), activity));
                    } else {
                        Log.e(LOG_TAG, "Error: " + e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ParseObject object = (ParseObject) listView.getAdapter().getItem(position);
                    String videoPath = object.getString("videoURL");

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
                    intent.setDataAndType(Uri.parse(videoPath), "video/*");
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error: " + e.getMessage());
                }

            }
        });

        Button callButton = (Button) findViewById(R.id.buttonCall);
        callButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> phoneQuery = ParseQuery.getQuery("PhoneNumber");
                phoneQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            // nada
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            String phoneNum = "tel:" + object.getString("numberString");
                            callIntent.setData(Uri.parse(phoneNum));
                            startActivity(callIntent);
                        }
                    }
                });
            }
        });

        Button surveyButton = (Button) findViewById(R.id.button_survey);
        surveyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                FragmentSurvey fragmentSurvey = new FragmentSurvey();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.fragment_container, fragmentSurvey);
                fragmentTransaction.addToBackStack(
                        null);

                fragmentSurvey.initFragmentSurvey(activity);

                RelativeLayout layoutAbout = (RelativeLayout) activity.findViewById(R.id.layout_about_us_master);
                layoutAbout.setVisibility(RelativeLayout.INVISIBLE);

                fragmentTransaction.commit();

                android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
                try {
                    mActionBar.hide();
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        });

        Button subscribeButton = (Button) findViewById(R.id.buttonSubscribe);
        subscribeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscribeDialogFragment sdf = new SubscribeDialogFragment();
                sdf.initSubscribeDialogFragment(getApplicationContext());
                sdf.show(getFragmentManager(), "subscribe");
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();

            android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
            try {
                mActionBar.show();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            RelativeLayout aboutUsLayout = (RelativeLayout) (activity.findViewById(R.id.layout_about_us_master));
            aboutUsLayout.setVisibility(RelativeLayout.VISIBLE);

        } else {
            super.onBackPressed();
        }
    }

    public void hasInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            // has internet connection
            onRestart();
        } else {
            Context context = activity.getApplicationContext();
            CharSequence text = "No Internet Connection :(";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                }
            };
            handler.postDelayed(r, 3000);

            hasInternetConnection();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RelativeLayout videosLayout = (RelativeLayout) (findViewById(R.id.layout_videos_master));
        RelativeLayout supportLayout = (RelativeLayout) (findViewById(R.id.layout_support_master));
        RelativeLayout aboutUsLayout = (RelativeLayout) (findViewById(R.id.layout_about_us_master));

        switch (item.getItemId()) {
            case R.id.item_video:
                videosLayout.setVisibility(RelativeLayout.VISIBLE);
                supportLayout.setVisibility(RelativeLayout.INVISIBLE);
                aboutUsLayout.setVisibility(RelativeLayout.INVISIBLE);
                return true;

            case R.id.item_support:
                videosLayout.setVisibility(RelativeLayout.INVISIBLE);
                supportLayout.setVisibility(RelativeLayout.VISIBLE);
                aboutUsLayout.setVisibility(RelativeLayout.INVISIBLE);
                return true;

            case R.id.item_about:
                videosLayout.setVisibility(RelativeLayout.INVISIBLE);
                supportLayout.setVisibility(RelativeLayout.INVISIBLE);
                aboutUsLayout.setVisibility(RelativeLayout.VISIBLE);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
