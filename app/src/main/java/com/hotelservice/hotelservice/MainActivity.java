package com.hotelservice.hotelservice;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private dbConexion con = new dbConexion();
    private EditText txtNH,txtruta,txtIP;
    private generales gen = new generales();
    private Spinner lst;
    private TextView lbTitle;
    private Button btnsave;
    private String titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lbTitle =   (TextView)findViewById(R.id.lbTitle);
        txtNH   =   (EditText)findViewById(R.id.txtNH);
        txtruta =   (EditText)findViewById(R.id.txtruta);
        txtIP   =   (EditText)findViewById(R.id.txtIP);
        lst     =   (Spinner)findViewById(R.id.lst);
        btnsave =   (Button)findViewById(R.id.btnsave);

        String[] idiomas={"Español","English"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,idiomas);
        lst.setAdapter(adapter);

        con.start(this);
        Cursor cursor = con.readData();
        if(cursor.moveToFirst())
            entrar();
        con.db.close();

        lst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (lst.getSelectedItem().toString()){
                    case "English":
                        lbTitle.setText("INITIAL SETUP");
                        txtruta.setHint("Web url*");
                        txtNH.setHint("Room number*");
                        txtIP.setHint("Server IP*");
                        btnsave.setText("SAVE");
                        break;
                    default:
                        lbTitle.setText("CONFIGURACIÓN INICIAL");
                        txtruta.setHint("Ruta de la página*");
                        txtNH.setHint("Número de habitación*");
                        txtIP.setHint("IP srvidor*");
                        btnsave.setText("GUARDAR");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void guardar(View v){
        List<String> errors = new ArrayList<>();

        switch (lst.getSelectedItem().toString()){
            case "English":
                if(txtNH.getText().toString().isEmpty())    errors.add("Type room number.");
                if(txtruta.getText().toString().isEmpty())  errors.add("Type web url.");
                if(txtIP.getText().toString().isEmpty())    errors.add("Type server IP.");
                break;
            default:
                if(txtNH.getText().toString().isEmpty())    errors.add("Ingrese el número de la habitación.");
                if(txtruta.getText().toString().isEmpty())  errors.add("Ingrese la ruta de la web.");
                if(txtIP.getText().toString().isEmpty())    errors.add("Ingrese la ip del servidor.");
                break;
        }

        if(errors.size()<=0){
            con.start(this);
            ContentValues reg = new ContentValues();
            reg.put("numeroh",txtNH.getText().toString());
            reg.put("ruta",txtruta.getText().toString());
            reg.put("ipserver",txtIP.getText().toString());
            if(con.InsertTable("tabletdata",reg)==-1){
                String cont = "";
                switch (lst.getSelectedItem().toString()){
                    case "English":
                        titles = "¡Something is wrong!";
                        cont = "Was impossible to register the room.";
                        break;
                    default:
                        titles = "¡Algo va mal!";
                        cont = "No fue posible registrar la habitación.";
                        break;
                }
                gen.alerta(titles,cont,this);
            }else
                entrar();
            con.db.close();
        }else
            gen.alerta("¡ERROR!",gen.setListErrors(errors),this);

    }

    public void entrar(){
        Intent log=new Intent(this,inicio.class);
        startActivity(log);
        finish();
    }

}
