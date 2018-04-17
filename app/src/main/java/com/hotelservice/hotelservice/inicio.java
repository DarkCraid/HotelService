package com.hotelservice.hotelservice;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    private RelativeLayout lyweb;
    private TextView lbUnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_inicio);

        page=(WebView)findViewById(R.id.web);

        lyweb=(RelativeLayout)findViewById(R.id.lyweb);
        lbUnable=(TextView)findViewById(R.id.lbUnable);

        con.start(this);
        Cursor cursor = con.readData();
        if(cursor.moveToFirst()){
            id = cursor.getString(0);
            ruta = cursor.getString(2);

            {
                try{
                    msocket= IO.socket("http://"+cursor.getString(3)+":90");
                }catch(URISyntaxException e){ }
            }

            msocket.on("connect",connect);
            msocket.on("cambiarStatus",changeStatus);
            msocket.connect();

            if(cursor.getInt(1)==1) {
                lyweb.setVisibility(View.VISIBLE);
                lbUnable.setVisibility(View.GONE);
            }
            else{
                lyweb.setVisibility(View.GONE);
                lbUnable.setVisibility(View.VISIBLE);
            }
        }
        con.db.close();

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
                            activeOrUnable(0);
                        else
                            activeOrUnable(1);

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

    public void activeOrUnable(int status){
        if(status == 1) {
            lyweb.setVisibility(View.VISIBLE);
            lbUnable.setVisibility(View.GONE);
        }
        else{
            lyweb.setVisibility(View.GONE);
            lbUnable.setVisibility(View.VISIBLE);
        }
    }
}
