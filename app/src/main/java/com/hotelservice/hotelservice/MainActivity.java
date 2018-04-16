package com.hotelservice.hotelservice;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private dbConexion con = new dbConexion();
    private EditText txtNH;
    private generales gen = new generales();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNH   =   (EditText)findViewById(R.id.txtNH);

        con.start(this);
        Cursor cursor = con.readData();
        if(cursor.moveToFirst()){
            if(cursor.getInt(1)==1)
                entrar(cursor.getString(0));
        }
        con.db.close();
    }

    public void guardar(View v){
        List<String> errors = new ArrayList<>();

        if(txtNH.getText().toString().isEmpty())    errors.add("Ingrese el número de la habitación.");

        if(errors.size()<=0){
            con.start(this);
            ContentValues reg = new ContentValues();
            reg.put("numeroh",txtNH.getText().toString());
            if(con.InsertTable("tabletdata",reg)==-1)
                gen.alerta("¡Algo salió mal!","No fue posible registrar la habitación",this);
            else
                entrar(txtNH.getText().toString());
            con.db.close();
        }else
            gen.alerta("¡ERROR!",gen.setListErrors(errors),this);

    }

    public void entrar(String identificador){
        Intent log=new Intent(this,inicio.class);
        log.putExtra("nh",identificador);
        startActivity(log);
        finish();
    }
}
