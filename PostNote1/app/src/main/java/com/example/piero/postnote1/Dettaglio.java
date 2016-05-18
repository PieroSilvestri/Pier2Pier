package com.example.piero.postnote1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dettaglio extends AppCompatActivity {
    private static final String POST = "POST";
    private static final String ID = "ID";
    private EditText text1;
    private EditText titolo;
    private int id = -1;
    private PostItem postItem;
    private TextView date;
    private Button addFoto;
    private Button eliminaFoto;

    private static final int CAMERA_REQUEST=1;
    private ImageView imageView;
    private static Bitmap bitmap;

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
        setResult(0, new Intent(Dettaglio.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);

        imageView = (ImageView) findViewById(R.id.ivImage);

        eliminaFoto = (Button) findViewById(R.id.eliminaFoto);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            Log.d("NOTICEMESENPAI", "Diverso da null");
        }

        if(savedInstanceState != null) {
            postItem = (PostItem) savedInstanceState.getSerializable(POST);
            id = savedInstanceState.getInt(ID);
        }
        if(getIntent().getSerializableExtra("MyPost") != null) {
            postItem = (PostItem)getIntent().getSerializableExtra("MyPost");
            id = getIntent().getExtras().getInt("ID");
        }
        if(getIntent().getExtras().getString("NUOVO") != null){
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
        }
        setTitle("" + titolo.getText());

        /*FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab2);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.update(new PostItem("" + titolo.getText(), "" + text1.getText(), postItem.getcreationDate(), postItem.getId()), id);
                Log.d("Detail + ", "" + id);
                Intent intent = new Intent(Dettaglio.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(POST, new PostItem("" + titolo.getText(), "" + text1.getText(), postItem.getcreationDate(), postItem.getId()));
                bundle.putInt(ID, id);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

                //cambiaTesto(text1.getText().toString(), id);
            }
        });*/
        Button delete;
        delete = (Button)findViewById(R.id.detailDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("delete", "delete");
                Intent i = new Intent(Dettaglio.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", id);
                setResult(99, i.putExtras(bundle));
                finish();
            }
        });

        Button annulla = (Button) findViewById(R.id.detailAnnulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("annulla", "annulla");
                setResult(0, new Intent(Dettaglio.this, MainActivity.class));
                bitmap = null;
                finish();
            }
        });

        addFoto = (Button) findViewById(R.id.foto);
        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage();
                //galleryAddPic();
                Log.d("PRESSED", "Premut");
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CAMERA_REQUEST:
                bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                imageView.setImageBitmap(bitmap);
                eliminaFoto.setVisibility(View.VISIBLE);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }




    public void onPickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, CAMERA_REQUEST);
    }

    /*
    * @String message
    * Questo metodo permette di  annullare o tornare indietro durante la detail activity
    * */
    private void mYdialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder.setMessage(message)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dettaglio.super.onBackPressed();
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
        super.onSaveInstanceState(outState);
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
