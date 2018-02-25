package com.goodiebag.adverPizing.activity;

/**
 * Created by kai on 6/4/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.goodiebag.adverPizing.R;
import com.goodiebag.adverPizing.adapters.CardAdapter;
import com.goodiebag.adverPizing.Credits;
import com.goodiebag.adverPizing.models.Item;
import com.goodiebag.adverPizing.networks.UdpAsyncTask;
import com.goodiebag.adverPizing.networks.rest.AdverPizingRetroServer;
import com.goodiebag.adverPizing.networks.rest.AdverPizingService;
import com.goodiebag.adverPizing.networks.rest.models.NoticeBoardRespnose;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class MainVolleyActivity extends AppCompatActivity{
    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String TAG = MainVolleyActivity.class.getSimpleName();
    private Button mButton;
    ProgressDialog loading = null;

    private List<Item> items;
    private Boolean dataOrNot = false;

    //Creating Views
    private RecyclerView recyclerView;
    private RelativeLayout relativeL;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, false);
        super.onStart();
        //Throw an ip search async
        UdpAsyncTask udpAsyncTask = new UdpAsyncTask();
        udpAsyncTask.setListener(new UdpAsyncTask.UdpAsyncTaskInteraction() {
            @Override
            public void onIpObtained(String ip) {
                Log.d(TAG, "Ip is : " + ip);
                initialize();
                waitForSomeTimeAndCloseTheDialog();
            }

            @Override
            public void onIpNull() {
                Log.d(TAG, "Ip is null");
                waitForSomeTimeAndCloseTheDialog();
            }
        });
        udpAsyncTask.execute("");


        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        relativeL = (RelativeLayout) findViewById(R.id.relativeL);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        items = new ArrayList<>();

        //Finally initializing our adapter
        adapter = new CardAdapter(items, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        //Calling method to get data
        //getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i = new Intent(MainVolleyActivity.this, Credits.class);
                startActivity(i);
                return true;
            default:
                return true;

        }
    }

    public void initialize() {
        SharedPreferences prefs = getSharedPreferences("PREF", MODE_PRIVATE);
        String ip = prefs.getString("ip", null);
        Retrofit retroServer = AdverPizingRetroServer.getRetroServer(ip);
        AdverPizingService service = retroServer.create(AdverPizingService.class);

        Call<List<NoticeBoardRespnose>> responses = service.listNotices();
        responses.enqueue(new Callback<List<NoticeBoardRespnose>>() {
            @Override
            public void onResponse(Call<List<NoticeBoardRespnose>> call, retrofit2.Response<List<NoticeBoardRespnose>> response) {
                Log.d("onResponse : ", response.toString());
                loading.dismiss();
                dataOrNot = true;
                populateData(response);
            }

            @Override
            public void onFailure(Call<List<NoticeBoardRespnose>> call, Throwable t) {
                Log.d("onFailure : ", "Failure");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void waitForSomeTimeAndCloseTheDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                if (!dataOrNot) {
                    loading.dismiss();
                    relativeL.setBackgroundResource(R.mipmap.notavail);
                    Toast.makeText(getApplicationContext(), "No offers available at this time", Toast.LENGTH_LONG).show();
                }
            }
        }, 5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void populateData(retrofit2.Response<List<NoticeBoardRespnose>> responses) {
        try {
            String rpiResponse = responses.body().toString();
            Log.d("response", rpiResponse);
            for (int i = 0; i < responses.body().size(); i++) {
                NoticeBoardRespnose response = responses.body().get(i);
                Item item = new Item();
                    item.setDate(response.getDate());
                    item.setName(response.getTitle());
                    item.setDescription(response.getDescription());
                    item.setLastDate(response.getDeadline());
                    item.setPlace(response.getTeacher());
                Log.d("offers", item.getDescription());
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
