package com.example.adi.futurebank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class ShopsActivity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        /*mToolbar = (Toolbar) findViewById(R.id.shops_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Best Offers Today");*/

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Pool club have 15% discount this night. Maybe you want to check it.");
        animalNames.add("Cinema City has released a new film by Marvel.");
        animalNames.add("Oktoberfest has today the BeerDay. ");
        animalNames.add("Elefant.ro have a 15% discount on books.");
        animalNames.add("Zara have a 20% disccount for the summer collection.");


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, animalNames);
        recyclerView.setAdapter(adapter);

    }


}
