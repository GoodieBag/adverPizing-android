package com.goodiebag.adverPizing.activity;

/**
 * Created by kai on 6/4/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.goodiebag.adverPizing.R;
import com.goodiebag.adverPizing.adapters.CardAdapter;
import com.goodiebag.adverPizing.Credits;
import com.goodiebag.adverPizing.networks.CustomJSONObjectRequest;
import com.goodiebag.adverPizing.networks.CustomVolleyRequestQueue;
import com.goodiebag.adverPizing.models.Item;
import com.goodiebag.adverPizing.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainVolleyActivity extends AppCompatActivity implements Response.Listener<JSONArray>,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";

    private Button mButton;
    private RequestQueue mQueue;
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
        initialize();
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
        loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, false);
        super.onStart();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = Constants.IP + Constants.noticeboards + Constants.firstTenNotices;
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
        waitForSomeTimeAndCloseTheDialog();

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
                if (dataOrNot == false) {
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
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //  mTextView.setText(error.getMessage());
        Log.d("error", error.toString());
    }

    @Override
    public void onResponse(JSONArray response) {
        loading.dismiss();
        dataOrNot = true;

       /* mTextView.setText("Response is: " + response);
        try {
            mTextView.setText(mTextView.getText() + "\n\n" + ((JSONObject) response).getString
                    ("company"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        parseData(response);
    }

    private void parseData(Object response) {
        String rpiResponse = response.toString();
        Log.d("response", rpiResponse);
        try {
            JSONArray jArr = new JSONArray(rpiResponse);
            for(int i = 0; i < jArr.length(); i++){
                JSONObject arJ = jArr.getJSONObject(i);
                Item item = new Item();
                if (arJ.has("date"))
                    item.setDate("" + arJ.getString("date"));
                if (arJ.has("title"))
                    item.setName("" + arJ.getString("title"));
                if (arJ.has("description"))
                    item.setDescription("" + arJ.getString("description"));
                if (arJ.has("deadline"))
                    item.setLastDate("" + arJ.getString("deadline"));
                if (arJ.has("teacher"))
                    item.setPlace("" + arJ.getString("teacher"));
                Log.d("offers", item.getDescription());
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
