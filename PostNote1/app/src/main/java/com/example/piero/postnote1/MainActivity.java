package com.example.piero.postnote1;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Dettaglio.IOChangeList {
    private static final String ID = "ID";
    private static final String VALORE = "VALORE";
    private ArrayList<PostItem> postList = new ArrayList<PostItem>();
    private AllFragment fragment;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //codice di piero per caricamento lista
    }

    @Override
    protected void onResume() {
        super.onResume();

        //codice di piero per caricamento lista

    }

    @Override
    protected void onStop() {
        super.onStop();
        //put element to DB, codice di piero

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


//        for(int i = 0; i < postList.size(); i++){
//            Log.d("TAG", postList.get(i).toString());
//        }
        if(savedInstanceState != null){
            postList = (ArrayList<PostItem>) savedInstanceState.getSerializable("POSTLIST");
            Log.d("LISTA CARICATA", postList.toString());
        }else{
            for(int i = 0; i<30;i++){
                PostItem post = new PostItem("Test " + i, "Contenuto " + i, i);
                postList.add(post);
            }
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = AllFragment.getIstance();
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("postList", postList);
        if(getFragmentManager().findFragmentByTag("ALLFRAG") == null) {
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, "ALLFRAG");
            fragmentTransaction.commit();
        }



        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dettaglio("Come stai?", postList.size());
                goToDetailFromButtonNew(postList.size(), true);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();


        if(resultCode == RESULT_OK){
            PostItem post = (PostItem)bundle.getSerializable("POST");
            int id = bundle.getInt(ID);
            Log.d("MAIN" , " " +id );
            Log.d("PRIMA ", postList.toString());
            fragment.addToList(post, id);
            Log.d("Dopo ", postList.toString());
            Log.d("MAIN", " " + id);
            fragment.UpdateList();
        }
        if(resultCode == 99){
            fragment.deleteElement(bundle.getInt("ID"));
            fragment.UpdateList();
        }

    }

    public void goToDetailFromButtonNew(int size, boolean nuovo){
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyPost", new PostItem());
        bundle.putInt(ID, size);
        startActivityForResult(new Intent(MainActivity.this, Dettaglio.class).putExtras(bundle), 10);

    }

    public void dettaglio(String value, int id){
        Intent openPage1 = new Intent(MainActivity.this,Dettaglio.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putString(VALORE, value);
        openPage1.putExtras(bundle);
        startActivity(openPage1);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("POSTLIST", postList);
        Log.d("LIST salvaa ", postList.toString() );
    }

    @Override
    public void update(PostItem post, int id) {
    /*
        Log.d("UPDATE", "UPDATE");
        if(postList.isEmpty())
            postList.add(post);
        else {
            if (postList.get(id) != null)
                postList.set(id, post);
            else {
                if (id < 0)
                    id = postList.size();
                postList.add(post);
            }
        }*/



    }
}
