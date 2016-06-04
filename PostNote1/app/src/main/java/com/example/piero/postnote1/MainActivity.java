package com.example.piero.postnote1;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String ID = "ID";
    private static final String VALORE = "VALORE";
    private ArrayList<PostItem> postList = new ArrayList<PostItem>();
    private ArrayList<PostItem> customList = new ArrayList<PostItem>();
    private DatabaseHelper myDB1;
    private String myID;
    private AllFragment fragment;
    private int count;
    int backButtonCount =0;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("IO ARRIVO", "QUI!!");
        viewAll();
        fragment.UpdateList();

    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File audioFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "PostNoteAudio");
        if (!audioFolder.exists()) {
            if(audioFolder.mkdir()){
                Toast.makeText(getApplicationContext(), "AudioFolder Created" + audioFolder.getName(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "AudioFolder Not Created", Toast.LENGTH_LONG).show();
            }
        }
        File imageFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "PostNoteImage");
        if (!imageFolder.exists()) {
            if(imageFolder.mkdir()){
                Toast.makeText(getApplicationContext(), "ImageFolder Created" + imageFolder.getName(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "ImageFolder Not Created", Toast.LENGTH_LONG).show();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB1 = new DatabaseHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState != null){
            postList = (ArrayList<PostItem>) savedInstanceState.getSerializable("POSTLIST");
            Log.d("LISTA CARICATA", postList.toString());
            count = savedInstanceState.getInt("COUNT");
        }else{

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("postList", postList);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = AllFragment.getIstance();

        bundle.putParcelableArrayList("postList", postList);
        if(getFragmentManager().findFragmentByTag("ALLFRAG") == null) {
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, "ALLFRAG");
            fragmentTransaction.commit();
        }


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df  = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = df.format(c.getTime());

        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postList.size() != 0){
                    goToDetailFromButtonNew(postList.get(postList.size()-1).getId(), true);
                }else{
                    goToDetailFromButtonNew(postList.size(), true);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
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
    }


    public void viewAll(){
        PostItem post;
        postList.clear();
        Cursor res = myDB1.getAllData();

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            post = new PostItem(res.getString(1), res.getString(2), res.getString(3), res.getInt(0), res.getInt(4), res.getInt(5), res.getInt(6));

            postList.add(post);
        }
    }


    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();
    }

    public void goToDetailFromButtonNew(int size, boolean nuovo){
        Bundle bundle = new Bundle();
        bundle.putInt(ID, size);
        bundle.putString("NUOVO", "NUOVO");
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
            if(backButtonCount >= 1)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
            }
            else
            {
                Toast.makeText(this, "Premi ancora il tasto back per uscire dalla migliore app 2015/16.", Toast.LENGTH_SHORT).show();
                backButtonCount++;
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_all) {

            final Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("postList", postList);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = AllFragment.getIstance();

            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, "ALLFRAG");
            fragmentTransaction.commit();

            viewAll();

        } else if (id == R.id.nav_flag) {

            customList.clear();

            for (int i=0;i<postList.size();i++){
                if(postList.get(i).isFlagged()==1){
                    customList.add(postList.get(i));
                }
            }

            final Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("postList", customList);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = AllFragment.getIstance();

            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, "ALLFRAG");
            fragmentTransaction.commit();



        } else if (id == R.id.nav_camera) {

            customList.clear();

            for (int i=0;i<postList.size();i++){
                if(postList.get(i).getImmagine()==1){
                    customList.add(postList.get(i));
                }
            }

            final Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("postList", customList);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = AllFragment.getIstance();

            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, "ALLFRAG");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_audio) {

            customList.clear();

            for (int i=0;i<postList.size();i++){
                if(postList.get(i).getAudio()==1){
                    customList.add(postList.get(i));
                }
            }

            final Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("postList", customList);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = AllFragment.getIstance();

            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, "ALLFRAG");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this,Login.class);
            Toast.makeText(getApplicationContext(), "Hai effettuato correttamente il logout", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("POSTLIST", postList);
        outState.putInt("COUNT", count);
        Log.d("LIST salvaa ", postList.toString());
    }

}
