package com.hotelservice.hotelservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                msocket= IO.socket("http://192.168.1.67:90");
            }catch(URISyntaxException e){ }
        }

        msocket.on("connect",connect);
        msocket.on("cambiarStatus",changeStatus);
        msocket.connect();

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
                    ver(args[0].toString());
                }
            });
        }
    };

    public void ver(String ver){
        Toast.makeText(this, ver, Toast.LENGTH_SHORT).show();
    }
}
