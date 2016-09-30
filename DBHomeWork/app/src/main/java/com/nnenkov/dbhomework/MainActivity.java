package com.nnenkov.dbhomework;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mCtx = this;
    private RecyclerView mRecyclerView;
    private MyRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mRvItemsTextView;
    public static ArrayList<Item> itemsList;

    DBHelper myDBHelper;


    Button mAddItemsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDBHelper = new DBHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mAddItemsBtn = (Button) findViewById(R.id.addItemsBtn);
        mAddItemsBtn.setOnClickListener(this);
        mRvItemsTextView = (TextView) findViewById(R.id.rvItemsTextView);

        itemsList = myDBHelper.getAllItems();
        if (itemsList != null)
            mRvItemsTextView.setText(String.valueOf(itemsList.size()));
        else
            mRvItemsTextView.setText("0");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyRecycleViewAdapter(itemsList);

        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addItemsBtn) {
            for (int count = 0; count < 100; count++)
                myDBHelper.InsertItem(new Item(count, "Item Name - " + generateRandomName()));
            itemsList = myDBHelper.getAllItems();
            mAdapter.setAdapterData(itemsList);
            mRvItemsTextView.setText(String.valueOf(itemsList.size()));
            mAdapter.notifyDataSetChanged();
        }
    }

    private String generateRandomName(){
        String randName = "";
        do {
            Random r = new Random();
            int charNum = r.nextInt(122 - 47) + 47; ///32 48 122
            if ( charNum == 47 ) charNum = 32;
            randName += (char)charNum;
        } while (randName.length() < 10);
        return randName;
    }

}
