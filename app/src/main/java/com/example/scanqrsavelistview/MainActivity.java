package com.example.scanqrsavelistview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.scanqrsavelistview.Adapter.MyAdapter;
import com.example.scanqrsavelistview.Databases.DBHelper;
import com.example.scanqrsavelistview.Model.ListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ListItem> arrayList;
    MyAdapter myAdapter;

    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new DBHelper(this);

//        fetch the data from database... if it is available, show it using recyclerview adapter

        arrayList = helper.getAllInformation();

        if (arrayList.size()>0){
//            ok neh datanya disini
            myAdapter = new MyAdapter(arrayList,this);
            recyclerView.setAdapter(myAdapter);
        }
        else {
            Toast.makeText(getApplicationContext(),"datanya ngga ada bor",Toast.LENGTH_LONG).show();
        }

//        on swipe left or right to remove  the data

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                ListItem listItem = arrayList.get(position);

//                remove the data
                helper.deleteRow(listItem.getId());

                arrayList.remove(position);
                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyItemRangeChanged(position,arrayList.size());

            }
        }).attachToRecyclerView(recyclerView);

        final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setCameraId(0);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result!=null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(getApplicationContext(),"ngga ada hasil",Toast.LENGTH_SHORT).show();
            }
            else
                {
            boolean isInserted = helper.insertData(result.getFormatName(),result.getContents());

                    if (isInserted){
                        arrayList.clear();
                        arrayList = helper.getAllInformation();
                        myAdapter = new MyAdapter(arrayList,this);
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    }
                }
        }
        else
        {
        super.onActivityResult(requestCode,resultCode,data);
        }
    }
}