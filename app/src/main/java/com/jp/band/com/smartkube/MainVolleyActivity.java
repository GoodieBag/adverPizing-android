package com.jp.band.com.smartkube;

/**
 * Created by kai on 6/4/16.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainVolleyActivity extends AppCompatActivity implements Response.Listener,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";

    private Button mButton;
    private RequestQueue mQueue;

    ProgressDialog loading = null;

    private List<Item> items;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        items = new ArrayList<>();

        //Calling method to get data
        //getData();



    }

    @Override
    protected void onStart() {
        //Showing a progress dialog
         loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "http://192.168.0.102/TEST/getdata.php";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

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
    }

    @Override
    public void onResponse(Object response) {
        loading.dismiss();
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
        try {
            JSONObject jObj = new JSONObject(rpiResponse);
            JSONArray jArr = jObj.getJSONArray("MegaMart");
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject arJ = jArr.getJSONObject(i);
//                        String im = arJ.getString("image");
//                        String head = arJ.getString("heading");
//                        String desc = arJ.getString("description");
//                        String oldPrice = arJ.getString("oldprice");
//                        String newPrice = arJ.getString("newprice");

                Item item = new Item();

                Log.d("Header", arJ.getString("header"));

                item.setHeader(arJ.getString("header"));
                item.setDescription(arJ.getString("description"));
                item.setNew_price(arJ.getString("newprice"));
                item.setOld_price(arJ.getString("oldprice"));
                item.setImage(arJ.getString("url"));
                items.add(item);

//                        Log.d("ARRAY ", "" + im + "\n" + head + "\n" + desc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Finally initializing our adapter
        adapter = new CardAdapter(items, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
