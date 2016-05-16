package com.example.piero.postnote1;

;import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Dettaglio.IOChangeList {
    private static final String ID = "ID";
    private static final String VALORE = "VALORE";
    DatabaseHelper myDB;
    String formattedDate;
    public static ArrayList<PostItem> postList = new ArrayList<PostItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df  = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formattedDate = df.format(c.getTime());

        viewAll();

        /*
        for(int i=0; i<30; i++){
            PostItem post = new PostItem("Test " + i, "Contenuto " + i, formattedDate, i);
            postList.add(post);
        }
        */

        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dettaglio("Come stai?", postList.size());
//                PostItem post = new PostItem("New", "nuova", "wewe", 2);
//                postList.add(post);
                AddData();
                AllFragment.UpdateList();
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

    public void AddData(){
        boolean isInserted = myDB.insertData("Titolo1".toString(), "Testo di prova".toString(), "12 Dicembre 20126".toString());

        if(isInserted){
            Toast.makeText(MainActivity.this, "Dati inseriti", Toast.LENGTH_LONG).show();
            viewAll();
        }
        else {
            Toast.makeText(MainActivity.this, "Dati NON inseriti", Toast.LENGTH_LONG).show();
        }
    }

    public void viewAll(){
        PostItem post;
        postList.clear();
        Cursor res = myDB.getAllData();
        if(res.getCount() == 0){
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();

        while(res.moveToNext()){
            post = new PostItem(res.getString(1), res.getString(2), formattedDate, 1);
            /*
            buffer.append("ID" + res.getString(0) + "\n");
            buffer.append("TITOLO" + res.getString(1) + "\n");
            buffer.append("TESTO" + res.getString(2) + "\n");
            buffer.append("DATE" + res.getString(3) + "\n");
            */
            postList.add(post);
        }

        /*
        for(int i=0; i<30; i++){
            PostItem post = new PostItem("Test " + i, "Contenuto " + i, formattedDate, i);
            postList.add(post);
        }
        */

        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder buldier = new AlertDialog.Builder(this);
        buldier.setCancelable(true);
        buldier.setTitle(title);
        buldier.setMessage(message);
        buldier.show();
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
        if(postList.isEmpty())
            postList.add(0, post);
        else {
            if (postList.get(id) != null)
                postList.set(id, post);
            else {
                if (id < 0)
                    id = postList.size();
                postList.add(id, post);
            }
        }

    }
}
