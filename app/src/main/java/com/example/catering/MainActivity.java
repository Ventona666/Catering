package com.example.catering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listItems;
    private ArrayList<String> listRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        listItems = this.findViewById(R.id.list_item);

        listRestaurants.add("Rest 1");
        listRestaurants.add("Rest 2");
        listRestaurants.add("Rest 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listRestaurants);
        listItems.setAdapter(adapter);
    }
}