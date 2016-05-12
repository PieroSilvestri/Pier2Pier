package com.example.piero.postnote1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Dettaglio extends AppCompatActivity {

    private EditText text1;
    private EditText titolo;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);



        if(getIntent().getSerializableExtra("MyPost") != null) {
            postItem = (PostItem)getIntent().getSerializableExtra("MyPost");
            id = getIntent().getExtras().getInt("ID");
        }
        titolo = (EditText)findViewById(R.id.postTitle);
        titolo.setText(postItem.getTitolo());
        text1 = (EditText)findViewById(R.id.editText);
        text1.setText(postItem.getTesto);

        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab2);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.update(new PostItem(text1.getText(), titolo.getText(), postItem.getId()), id);

                //cambiaTesto(text1.getText().toString(), id);
            }
        });
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
