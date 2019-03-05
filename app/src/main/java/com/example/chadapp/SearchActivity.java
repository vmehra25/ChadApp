package com.example.chadapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Toolbar toolbar;
    ViewPager pager;
    TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        InitializeFields();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        

    }

    private void InitializeFields() {
        toolbar = findViewById(R.id.Search_Toolbar);
        pager = findViewById(R.id.Search_Pager);
        tab = findViewById(R.id.Search_Tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
