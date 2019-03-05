package com.example.chadapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.InvalidMarkException;

public class MainActivity extends AppCompatActivity {

    //Firebase Auth
    static FirebaseAuth  mAuth;
    //Toolbar
    Toolbar toolbar;
    //TabLayout
    ViewPager viewPager;
    TabLayout tabLayout;
    myPagerAdapter adapter;
   //Navigation Bar
    static ImageView UserImg;
    NavigationView navigationView;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeFields();

        //Setting Image on FUll Screen
        ImageView img = findViewById(R.id.Main_Ige);
        img.setScaleType(ImageView.ScaleType.FIT_XY);

        //Toolbar
               setSupportActionBar(toolbar);

        //Navigation Bar
       UserImg.setImageDrawable(getResources().getDrawable(R.drawable.user));
        UserImg.setScaleType(ImageView.ScaleType.FIT_XY);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.Open,R.string.Close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.Menu_Account :
                        startActivity(new Intent(MainActivity.this,Account2Activity.class));
                        break;
                    case R.id.Menu_Logout :
                        Account2Activity.backupUri =null;
                                Account2Activity.imageUri =null;
                        ChatFragment.DobUser = ChatFragment.NameUser = null;

                        ChatFragment.bitmap = null;
                        ChatFragment.list.clear();
                        Account2Activity.Url = null;
                        mAuth.signOut();
                        //onStart();
                       // mAuth =null;
                        Intent signOutIntent = new Intent(MainActivity.this,LoginActivity.class);
                        signOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(signOutIntent);
                        break;

                }
                return true;
            }
        });

        //TabLayout
        mAuth = FirebaseAuth.getInstance();
        if(mAuth == null)
            return;
        else if(mAuth.getCurrentUser()==null)
            return;

            adapter = new myPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager, true);
            tabLayout.setSelectedTabIndicatorColor(0xFF26968C);
            tabLayout.setSelectedTabIndicatorGravity(2);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_black_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);

        //Uploading image
        Uri image ;
                if(getIntent().getParcelableExtra("Uri")!= null)
                {

                }
    }

    private void InitializeFields() {

        context = MainActivity.this;
        toolbar = findViewById(R.id.Main_Toolbar);
        viewPager = findViewById(R.id.Main_Pager);
        tabLayout = findViewById(R.id.Main_Tab);
        navigationView = findViewById(R.id.Main_Navigation);
        UserImg = navigationView.getHeaderView(0).findViewById(R.id.ImageNavigation_Image);
        mDrawer = findViewById(R.id.Main_Drawer);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        //Check login status
        if (mAuth==null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(mAuth.getCurrentUser() == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Toolbar Menu
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    static void SetImage(int i)
    {
            UserImg.setImageURI(Account2Activity.imageUri);
    }

}

