package com.hotelservice.hotelservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;

public class inicio extends AppCompatActivity {
    private String id="",ruta="";
    private Socket msocket;
    private dbConexion con = new dbConexion();
    private generales gen = new generales();
    private WebView page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        page=(WebView)findViewById(R.id.web);

        Bundle b=getIntent().getExtras();
        id=b.getString("nh");
        ruta=b.getString("ruta");

        {
            try{
                msocket= IO.socket("http://192.168.10.118:90");
            }catch(URISyntaxException e){ }
        }

        msocket.on("connect",connect);
        msocket.on("cambiarStatus",changeStatus);
        msocket.connect();

        WebSettings webSettings = page.getSettings();
        webSettings.setJavaScriptEnabled(true);

        page.setWebViewClient(new WebViewClient());
        page.loadUrl(ruta);
    }

    public Emitter.Listener connect=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socketData cont= new socketData();
                    cont.numHabitacion        = id;
                    Gson gson=new Gson();
                    msocket.emit("iniciarDispositivo",gson.toJson(cont));
                }
            });
        }
    };

    public Emitter.Listener changeStatus=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startConS();
                    if(! con.changeStatus(id,Integer.parseInt(args[0].toString())))
                        ThreadAlert("¡ERROR!","No fue posible activar o desactivar el dispositivo.");
                    else{
                        if(Integer.parseInt(args[0].toString())==0)
                            intentUnable();
                        else
                            intentInicio();

                    }
                    con.db.close();
                }
            });
        }
    };

    public void ver(String ver){
        Toast.makeText(this, ver, Toast.LENGTH_SHORT).show();
    }

    public void startConS(){   con.start(this);   }
    public void ThreadAlert(String title, String cont){  gen.alerta(title,cont,this);   }
    public void intentUnable(){
        Intent un=new Intent(this,Unable.class);
        startActivity(un);
        finish();
    }
    public void intentInicio(){
        Intent log=new Intent(this,inicio.class);
        log.putExtra("nh",id);
        log.putExtra("ruta",ruta);
        startActivity(log);
        finish();
    }
}
