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
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);

        Bundle datipassati = getIntent().getExtras();
        String dato1 =  datipassati.getString("VALORE");
        id = datipassati.getInt("ID");



        TextView text = (TextView)findViewById(R.id.textView2);
        text.setText(dato1);

         text1 = (EditText)findViewById(R.id.editText);
        text1.setText(dato1 + " 100 ");

        FloatingActionButton btnLetter = (FloatingActionButton)findViewById(R.id.fab2);
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaTesto(text1.getText().toString(), id);
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
