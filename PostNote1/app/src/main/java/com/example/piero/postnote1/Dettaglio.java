package com.example.piero.postnote1;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.Date;

public class Dettaglio extends AppCompatActivity {
    private static final String POST = "POST";
    private static final String ID = "ID";
    private EditText text1;
    private EditText titolo;
    private int id = -1;
    private PostItem postItem;
    private TextView date;
    private TextView listening;
    private String posizione;
    private String posizioneTemp;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;
    public Dettaglio(){
        posizione =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest";
        posizioneTemp = posizione + "Temp.mp3";
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
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        listening.setText("Start playing");
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
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

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

        listening = (TextView) findViewById(R.id.listening);
        titolo = (EditText)findViewById(R.id.postTitle);
        text1 = (EditText)findViewById(R.id.editText);
        date = (TextView)findViewById(R.id.date);

        if(postItem == null){
            titolo.setHint("Inserisci qua il titolo");
            text1.setHint("Inserisci qua il contenuto");
            mFileName = posizione + id + ".mp3";
        } else {
            titolo.setText("" + postItem.getTitolo());
            text1.setText("" + postItem.getTesto());
            if (postItem.getPosizioneAudio() == null)
                mFileName = posizione + postItem.getId() + ".mp3";
            else
                mFileName = postItem.getPosizioneAudio();
        }
        setTitle("" + titolo.getText());

        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab2);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mListener.update(new PostItem("" + titolo.getText(), "" + text1.getText(), postItem.getcreationDate(), postItem.getId()), id);
                Log.d("Detail + ", "" + id);
                Intent intent = new Intent(Dettaglio.this, MainActivity.class);
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
                setResult(RESULT_OK, intent);
                finish();

                //cambiaTesto(text1.getText().toString(), id);
            }
        });

        Button recordAudio = (Button) findViewById(R.id.audio);
        recordAudio.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;
            @Override
            public void onClick(View v) {

                onRecord(mStartRecording);
                if (mStartRecording) {
                    date.setText("Stop recording");
                } else {
                    date.setText("Start recording");

                }
                mStartRecording = !mStartRecording;


            }
        });

        Button listen = (Button) findViewById(R.id.listen);
        listen.setOnClickListener(new View.OnClickListener() {
            boolean mStartPlaying = true;

            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    listening.setText("Stop playing");
                } else {
                    listening.setText("Start playing");
                   // postItem.setAudio(mRecorder);
                }
                mStartPlaying = !mStartPlaying;
            }

        });


        Button delete = (Button)findViewById(R.id.detailDelete);
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
                finish();
            }
        });
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

    private  static void saveAudio(String oldAudio, String newAudio) throws IOException {
        //postItem.getPosizioneAudio(), posizioneTemp
        long timeStart = System.currentTimeMillis();
        FileInputStream fistream1 = null;
        try {
            fistream1 = new FileInputStream(oldAudio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileInputStream fistream2 = new FileInputStream(newAudio);
        SequenceInputStream sistream = new SequenceInputStream(fistream1, fistream2);
        FileOutputStream fostream = new FileOutputStream(oldAudio);

        int temp;

        while( ( temp = sistream.read() ) != -1)
        {

            fostream.write(temp);
        }
        fostream.close();
        sistream.close();
        fistream1.close();
        fistream2.close();
        long timeEnd= System.currentTimeMillis();

        Log.e("merge timer:", "milli seconds:" + (timeEnd - timeStart));

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
