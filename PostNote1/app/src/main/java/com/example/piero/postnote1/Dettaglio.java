package com.example.piero.postnote1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Dettaglio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);

        Bundle datipassati = getIntent().getExtras();
        String dato1 =  getIntent().getExtras().getString("VALORE");

        TextView text = (TextView)findViewById(R.id.textView2);
        text.setText(dato1);

    }
}
