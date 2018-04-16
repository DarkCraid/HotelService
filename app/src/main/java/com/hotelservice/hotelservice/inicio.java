package com.hotelservice.hotelservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class inicio extends AppCompatActivity {
    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Bundle b=getIntent().getExtras();
        id=b.getString("nh");

        Toast.makeText(this,id,Toast.LENGTH_LONG).show();
    }
}
