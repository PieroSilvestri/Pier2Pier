package com.example.piero.postnote1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Dettaglio extends AppCompatActivity {
    private static final String POST = "POST";
    private static final String ID = "ID";
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int CAMERA_REQUEST=1;

    private Toolbar toolbar;
    private ImageView imageView;
    private static Bitmap bitmap;
    private ImageButton addFoto;
    private Button eliminaFoto;
    private EditText text1;
    private EditText titolo;
    private int id = -1;
    private String myID;
    private PostItem postItem;
    private TextView date;
    private DatabaseHelper myDB;
    private Button listen;
    private static String CorrectData;
    private TextView detailTitolo;
    private boolean mStartPlaying = true;
    private String posizione;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;
    public Dettaglio(){
        posizione =  Environment.getExternalStorageDirectory() + File.separator + "PostNoteAudio" + "/audioRecord";
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();

        }
    }

    private void onPlay(boolean start) {
        if (start) {
            stopPlaying();
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        double now = System.currentTimeMillis();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();

            /*if(System.currentTimeMillis() - now > mPlayer.getDuration()){
                mPlayer.release();
                mPlayer = null;
                mStartPlaying = true;
            }*/

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if(mPlayer != null)
             mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        try{
            mRecorder.stop();
        }catch(RuntimeException stopException){
            stopException.printStackTrace();
        }
        mRecorder.release();
        mRecorder = null;
        listen.setVisibility(View.VISIBLE);
    }

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


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);
        imageView = (ImageView) findViewById(R.id.ivImage);
        eliminaFoto = (Button) findViewById(R.id.eliminaFoto);
        titolo = (EditText)findViewById(R.id.postTitle);
        text1 = (EditText)findViewById(R.id.editText);
        date = (TextView)findViewById(R.id.date);
        listen = (Button) findViewById(R.id.listen);
        listen.setVisibility(View.INVISIBLE);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
        if(savedInstanceState != null) {
            postItem = (PostItem) savedInstanceState.getSerializable(POST);
            id = savedInstanceState.getInt(ID);
            if (postItem.getPosizioneAudio() != null)
                listen.setVisibility(View.VISIBLE);
        }
        if(getIntent().getSerializableExtra("MyPost") != null) {
            postItem = (PostItem)getIntent().getSerializableExtra("MyPost");
            id = getIntent().getExtras().getInt("ID");
            if (postItem.getPosizioneAudio() != null)
                 listen.setVisibility(View.VISIBLE);
        }
        if(getIntent().getExtras().getString("NUOVO") != null){
            id = getIntent().getExtras().getInt("ID") + 1;
        }



        if(postItem == null){
            titolo.setHint("Inserisci qua il titolo");
            text1.setHint("Inserisci qua il contenuto");
            mFileName = posizione + id + ".mp3";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df  = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            CorrectData = formattedDate.replaceAll("/", "").replaceAll(":","").replaceAll(" ","");
            Log.d("WTF?", CorrectData);
            String data = ("Data: " + formattedDate);
            date.setText(data);
        } else {
            titolo.setText("" + postItem.getTitolo());
            text1.setText("" + postItem.getTesto());
            CorrectData = postItem.getcreationDate();
            if (postItem.getPosizioneAudio() == null) {
                mFileName = posizione + postItem.getId() + ".mp3";
                listen.setVisibility(View.INVISIBLE);
            } else {
                mFileName = postItem.getPosizioneAudio();
                listen.setVisibility(View.VISIBLE);
            }
            String testoTW = postItem.getcreationDate();
            String testoMod = "Data: " + testoTW.substring(0,2) + "/" + testoTW.substring(2,4)+ "/" + testoTW.substring(4,6) + " " + testoTW.substring(6,8) + ":" + testoTW.substring(8,10);
            Log.d("MACOMEEEE", testoMod);
            date.setText(testoMod);
            String nome = postItem.getcreationDate();
            Log.d("FILECREATO", nome);
            myID = String.valueOf(postItem.getId());
            String fixedCreationDate = Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + postItem.getcreationDate().replaceAll("/", "").replaceAll(":","").replaceAll(" ", "");
            Log.d("WTF?", fixedCreationDate);
            imageView.setImageBitmap(loadBitmap(getApplicationContext(), fixedCreationDate));
        }

        ImageButton save = (ImageButton) findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cliccato", "cliccato");

                if(getIntent().getExtras().getString("NUOVO") != null){
                    AddData();
                }
                else{
                    UpdateDate();
                }

//                mListener.update(new PostItem("" + titolo.getText(), "" + text1.getText(), postItem.getcreationDate() ,"" ,  postItem.getId()), id);
                Log.d("Detail + ", "" + id);
               /* Intent intent = new Intent(Dettaglio.this, MainActivity.class);
                Bundle bundle = new Bundle();
                PostItem postItem1 = new PostItem();
                postItem1.setTesto("" + text1.getText());
                postItem1.setTitolo("" + titolo.getText());
                postItem1.setCreationDate("" + new Date());
                postItem1.setId(id);
                postItem1.setPosizioneAudio(mFileName);
                bundle.putSerializable(POST, postItem1);
                bundle.putInt(ID, id);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);*/
                bitmap = null;
                finish();
            }
        });

        final ImageButton recordAudio = (ImageButton) findViewById(R.id.audio);
        recordAudio.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;

            @Override
            public void onClick(View v) {

                onRecord(mStartRecording);
                if (mStartRecording) {
                    recordAudio.setColorFilter(Color.RED);
                } else {
                    recordAudio.setColorFilter(Color.BLACK);
                }
                mStartRecording = !mStartRecording;


            }
        });

        listen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onPlay(mStartPlaying);
                mStartPlaying = !mStartPlaying;
            }

        });

        ImageButton annulla = (ImageButton) findViewById(R.id.detailAnnulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("annulla", "annulla");
                setResult(0, new Intent(Dettaglio.this, MainActivity.class));
                bitmap = null;
                finish();
            }
        });

        addFoto = (ImageButton) findViewById(R.id.foto);
        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage();
                Log.d("PRESSED", "Premut");
            }
        });

        detailTitolo = (TextView) findViewById(R.id.detailTitolo);
        detailTitolo.setText("" + detailTitolo.getText());
        setTitle("");
    }

    public void AddData(){
        boolean isInserted = myDB.insertData(titolo.getText().toString(), text1.getText().toString(), CorrectData);
        if(isInserted){
            Toast.makeText(Dettaglio.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Dettaglio.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void UpdateDate(){
        boolean isUpdate = myDB.updateData(myID, titolo.getText().toString(), text1.getText().toString());
        if(isUpdate){
            Toast.makeText(Dettaglio.this, "Data Updated", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(Dettaglio.this, "Data Not Updated", Toast.LENGTH_LONG).show();
        }

    }

    public void DeleteData(){
        Integer deleteRows = myDB.deleteData(myID);
        if(deleteRows > 0){
            Toast.makeText(Dettaglio.this, "Data Delete", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(Dettaglio.this, "Data Not Delete", Toast.LENGTH_LONG).show();
        }
    }
    public void DeleteDataFragment(String id){
        Integer deleteRows = myDB.deleteData(id);
        if(deleteRows > 0){
            Toast.makeText(Dettaglio.this, "Data Delete", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(Dettaglio.this, "Data Not Delete", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CAMERA_REQUEST:
                bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                imageView.setImageBitmap(bitmap);
                saveFile(getApplicationContext(), bitmap, Environment.getExternalStorageDirectory() +  File.separator + "PostNoteImage" + File.separator +  CorrectData);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        if(b ==null){
            return;
        }
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("CANCELLATO", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("CANCELLATO", "io exception");
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
           // fis = context.openFileInput(picName);
            fis = new FileInputStream (new File(picName));
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d("CARICATO", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("CARICATO", "io exception");
            e.printStackTrace();
        }
        return b;
    }




    public void onPickImage() {
        Toast.makeText(Dettaglio.this, "ID: " + id, Toast.LENGTH_LONG).show();
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, CAMERA_REQUEST);
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
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(POST, postItem);
        outState.putInt(ID, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.detail_share) {
            return true;
        }

        if (id == R.id.detail_delete) {
            DeleteData();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
