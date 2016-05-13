package com.example.piero.postnote1;

import android.support.v7.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Dettaglio.IOChangeList {
    private static final String VALORENUOVO = "VALORENUOVO";
    private static final String ID = "ID";
    private static final String VALORE = "VALORE";
    private static final String TITLE = "TITLE";
    private ArrayList<PostItem> postList = new ArrayList<PostItem>();
    public static PostAdapter mAdapter;
    TextView text;
    ArrayList<PostItem> arrayMio = new ArrayList<>();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // arrayMio.add(0, "CIAO");


//        PostItem post = new PostItem("Titolo 1","Contenuto 1",1);
//        postList.add(post);
//        post = new PostItem("Titolo 2", "Contenuto 2", 1);
//        postList.add(post);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        for(int i = 0; i < arrayMio.size(); i++){
            Log.d("TAG", arrayMio.get(i).toString());
        }

//        TextView text3 = (TextView)findViewById(R.id.textView3);
//        text3.setText(dato1);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("postList", postList);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AllFragment fragment = new AllFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();


        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modificare arrayMio con nome array di meligio
                dettaglio("Come stai?", arrayMio.size());
                //dettaglio nuovo
                // dettaglio(new PostItem(null, null, arrayMio.size()));
                Intent i = new Intent(MainActivity.this, Dettaglio.class);
                Bundle bundle1 = new Bundle();
                PostItem post = new PostItem();
                bundle.putSerializable("POST", (PostItem) post);
                bundle.putInt("ID", -1);
                startActivity(i.putExtras(bundle1));

            }
        });

        text = (TextView)findViewById(R.id.textView3);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sostituire 0 con posizione nell'array
                dettaglio(text.getText().toString(), 0);
            }
        });

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
    public void update(PostItem post, int id) {
        if(arrayMio.isEmpty())
            arrayMio.add(0, post);
        else {
            if (arrayMio.get(id) != null)
                arrayMio.set(id, post);
            else {
                if (id < 0)
                    id = arrayMio.size();

                arrayMio.add(id, post);
            }
        }

    }
}
