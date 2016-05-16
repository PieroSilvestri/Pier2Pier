package com.example.piero.postnote1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class Dettaglio extends AppCompatActivity {
    private static final String POST = "POST";
    private static final String ID = "ID";
    private EditText text1;
    private EditText titolo;
    private TextView date;
    private int id;
    private PostItem postItem;
    public interface IOChangeList{
        void update(PostItem post, int id);
    }

    private IOChangeList mListener = new IOChangeList() {
        @Override
        public void update(PostItem post, int id) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYdialog("Sicuro di voler uscire?");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);

        id = -1;

        if(savedInstanceState != null) {
            postItem = (PostItem) savedInstanceState.getSerializable(POST);
            id = savedInstanceState.getInt(ID);
        }
        if(getIntent().getSerializableExtra("MyPost") != null) {
            postItem = (PostItem)getIntent().getSerializableExtra("MyPost");
            id = getIntent().getExtras().getInt("ID");
        }

        titolo = (EditText)findViewById(R.id.postTitle);
        text1 = (EditText)findViewById(R.id.editText);
        date = (TextView)findViewById(R.id.date);

        if(postItem == null){
            titolo.setHint("Inserisci qua il titolo");
            text1.setHint("Inserisci qua il contenuto");

        } else {
            titolo.setText("" + postItem.getTitolo());
            text1.setText("" + postItem.getTesto());
            date.setText("" + postItem.getcreationDate());
        }


//        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab2);
//        btnLetter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.update(new PostItem("" + titolo.getText(), "" + text1.getText(), postItem.getId()), id);
//
//                //cambiaTesto(text1.getText().toString(), id);
//            }
//        });

        Button delete = (Button)findViewById(R.id.detailDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button annulla = (Button) findViewById(R.id.detailAnnulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYdialog("Sicuro di voler annullare?");
            }
        });
    }

    private void mYdialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder.setMessage(message)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(POST, (Serializable) postItem);
        outState.putInt(ID, id);

    }

    public void cambiaTesto(String value, int id){
        Intent openPage1 = new Intent(Dettaglio.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("VALORENUOVO", value);
        bundle.putInt("ID", id);
        openPage1.putExtras(bundle);
        startActivity(openPage1);
    }
}
