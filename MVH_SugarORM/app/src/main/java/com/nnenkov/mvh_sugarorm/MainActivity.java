package com.nnenkov.mvh_sugarorm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MVHApplication mMVHApp;
    Context mCtx = this;
    private static final Integer SETTINGS_RESULT = 1212;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mMVHApp = ((MVHApplication) getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name_mvh);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
        MainActivityViewPagerAdapter adapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EventsFragment(), "Events");
        adapter.addFrag(new UsefulInfoFragment(), "Useful Info");

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainActivityTabLayout);
        tabLayout.setupWithViewPager(viewPager);


        //fab lib Start
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddEditEvent.class);
                intent.putExtra("EventType", (long) 1);
                startActivity(intent);
                materialDesignFAM.close(true);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddEditEvent.class);
                intent.putExtra("EventType", (long) 4);
                startActivity(intent);
                materialDesignFAM.close(true);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddEditEvent.class);
                intent.putExtra("EventType", (long) 3);
                startActivity(intent);
                materialDesignFAM.close(true);

            }
        });
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddEditEvent.class);
                intent.putExtra("EventType", (long) 2);
                startActivity(intent);

                materialDesignFAM.close(true);
            }
        });
        //fab lib end

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(getApplicationContext(), UserSettingActivity.class);
            startActivityForResult(settingIntent, SETTINGS_RESULT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.manage_vehicles) {
            Intent intent = new Intent(this, ManageVehicles.class);
            startActivity(intent);
        } else if (id == R.id.manage_events) {
            Toast.makeText(this,"Manage events...",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.manage_insurances) {
            Toast.makeText(this,"Manage insurances...",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"Nav share...",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.manage_fuels) {
            Toast.makeText(this,"Manage fuels...",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"Nav send...",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_RESULT) {
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        } else {
            EventsFragment mEventsfragment = (EventsFragment) getSupportFragmentManager().getFragments().get(0);
            if (mEventsfragment != null)
                mEventsfragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
