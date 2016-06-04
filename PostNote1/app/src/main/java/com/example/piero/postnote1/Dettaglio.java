package com.example.piero.postnote1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
    boolean mStartRecording = true;
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
    private int audio = 0;
    private String myID;
    private PostItem postItem;
    private TextView date;
    private DatabaseHelper myDB;
    private ImageButton listen;
    private static String CorrectData;
    private TextView detailTitolo;
    private boolean mStartPlaying = true;
    private String posizione;
    private static String mFileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private SeekBar seekbar;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    private Handler myHandler = new Handler();
    private RelativeLayout player;
    private int flag = 0;

    public Dettaglio(){
        posizione =  Environment.getExternalStorageDirectory() + File.separator + "PostNoteAudio" + "/audioRecord";
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
            postItem.setAudio(audio = 1);
            refreshPlayer();
        }
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0, new Intent(Dettaglio.this, MainActivity.class));
        finish();
    }

    @SuppressLint("WrongViewCast")
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
        listen = (ImageButton) findViewById(R.id.listen);
        player=(RelativeLayout)findViewById(R.id.player);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
        if(savedInstanceState != null) {
            postItem = (PostItem) savedInstanceState.getSerializable(POST);
            id = savedInstanceState.getInt(ID);
        }
        if(getIntent().getSerializableExtra("MyPost") != null) {
            postItem = (PostItem)getIntent().getSerializableExtra("MyPost");
            id = getIntent().getExtras().getInt("ID");
            audio = getIntent().getExtras().getInt("AUDIO");
        }
        if(getIntent().getExtras().getString("NUOVO") != null){
            id = getIntent().getExtras().getInt("ID") + 1;
        }

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(true);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (fromUser)
                    mPlayer.seekTo((progress));

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + CorrectData + ".jpg"), "image/*");
                startActivity(intent);
            }
        });


        if(postItem == null){
            postItem = new PostItem();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df  = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            CorrectData = formattedDate.replaceAll("/", "").replaceAll(":","").replaceAll(" ","");
            postItem.setCreationDate(CorrectData);
            Log.d("WTF?", CorrectData);
            String data = ("Data: " + formattedDate);
            date.setText(data);
            postItem.setCreationDate(CorrectData);
        } else {

            titolo.setText("" + postItem.getTitolo());
            text1.setText("" + postItem.getTesto());
            CorrectData = postItem.getcreationDate();
            String testoTW = postItem.getcreationDate();
            String testoMod = "Data: " + testoTW.substring(0,2) + "/" + testoTW.substring(2,4)+ "/" + testoTW.substring(4,6) + " " + testoTW.substring(6,8) + ":" + testoTW.substring(8,10);
            Log.d("MACOMEEEE", testoMod.toString());
            Toast.makeText(this, "UAFID" + String.valueOf(postItem.getAudio()), Toast.LENGTH_SHORT).show();
            date.setText(testoMod);
            String nome = postItem.getcreationDate();
            Log.d("FILECREATO", nome);
            myID = String.valueOf(postItem.getId());
            String fixedCreationDate = Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + postItem.getcreationDate().replaceAll("/", "").replaceAll(":","").replaceAll(" ", "");
            Log.d("WTF?", fixedCreationDate);
            imageView.setImageBitmap(loadBitmap(getApplicationContext(), fixedCreationDate + ".jpg"));

        }
        refreshPlayer();
        mFileName = posizione + CorrectData + ".mp3";
        Log.d("FILENAM", mFileName);
        mPlayer = new MediaPlayer().create(getApplicationContext(), Uri.parse(mFileName));
        ImageButton save = (ImageButton) findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStartRecording){
                    stopRecording();
                    postItem.setAudio(audio = 1);
                    refreshPlayer();
                }

                Log.d("cliccato", "cliccato");
                if(getIntent().getExtras().getString("NUOVO") != null){
                    AddData();
                }else{
                    UpdateDate();
                }
                Log.d("Detail + ", "" + id);
                bitmap = null;
                finish();
            }
        });

        final ImageButton recordAudio = (ImageButton) findViewById(R.id.audio);
        recordAudio.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    recordAudio.setColorFilter(Color.RED);
                } else {
                    recordAudio.setColorFilter(Color.BLACK);
                    postItem.setAudio(1);
                }
                mStartRecording = !mStartRecording;
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                File file = new File(mFileName);
                if (file == null) {
                    Toast.makeText(getApplicationContext(), "Audio Inesistente", Toast.LENGTH_LONG).show();
                }else{
                    if (mStartPlaying) {
                        if (mPlayer == null)
                            mPlayer = new MediaPlayer().create(getApplicationContext(), Uri.parse(mFileName));
                        mPlayer.start();

                        finalTime = mPlayer.getDuration();
                        Log.d("FINALTIME", String.valueOf(finalTime));
                        startTime = mPlayer.getCurrentPosition();

                        if (oneTimeOnly == 0) {
                            seekbar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }
                        seekbar.setProgress((int) startTime);
                        myHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (mPlayer != null && mPlayer.isPlaying()) {
                                    startTime = mPlayer.getCurrentPosition();
                                    seekbar.setProgress((int) startTime);
                                    myHandler.postDelayed(this, 100);
                                }
                            }
                        }, 100);
                    } else {
                        mPlayer.pause();
                    }
                    mStartPlaying = !mStartPlaying;
                }
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
            }
        });

        detailTitolo = (TextView) findViewById(R.id.detailTitolo);
        detailTitolo.setText("" + detailTitolo.getText());
        setTitle("");
    }

    public void AddData(){
        boolean isInserted = myDB.insertData(titolo.getText().toString(), text1.getText().toString(), CorrectData, postItem.getAudio(), postItem.getImmagine(), flag);
        if(isInserted){
            Toast.makeText(Dettaglio.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Dettaglio.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void UpdateDate(){
        Log.d("MMMMMMMMMM", ""+postItem.getAudio());
        boolean isUpdate = myDB.updateData(myID, titolo.getText().toString(), text1.getText().toString(), postItem.getAudio(), postItem.getImmagine(), flag);
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
        String selectedFilePath = (Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + CorrectData + ".jpg");
        File file = new File(selectedFilePath);
        if(file.exists())
            file.delete();
        String selectedFilePathAudio = (Environment.getExternalStorageDirectory() + File.separator + "PostNoteAudio" + File.separator +  "audioRecord" + CorrectData + ".mp3");
        File fileAudio = new File(selectedFilePathAudio);
        if(fileAudio.exists())
            fileAudio.delete();

        AllFragment.UpdateList();
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
                saveFile(getApplicationContext(), bitmap, Environment.getExternalStorageDirectory() +  File.separator + "PostNoteImage" + File.separator +  CorrectData + ".jpg");
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        if(b == null){
            return;
        }
        try {
            fos = new FileOutputStream(new File(picName));
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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
        postItem.setImmagine(1);
    }

    private void refreshPlayer(){
        Log.d("AUDIOPLS", ""+audio);
        if(postItem == null){
            if(postItem.getAudio()==0){
                player.setVisibility(View.INVISIBLE);
            } else {
                player.setVisibility(View.VISIBLE);
            }
        } else {
            if(audio==0){
                player.setVisibility(View.INVISIBLE);
            } else {
                player.setVisibility(View.VISIBLE);
            }
        }
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
        outState.putInt("audio", postItem.getAudio());
        outState.putInt("img", postItem.getImmagine());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
