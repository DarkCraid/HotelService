package com.hotelservice.hotelservice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by DarkCraid on 06/04/2018.
 */

public class generales {
    //--------------------------------------------------------------------------------- Alerta para información
    public void alerta(String title, String cont, Context v){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v);
        dialogo1.setTitle(title);
        dialogo1.setMessage(cont);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        dialogo1.show();
    }

    public String setListErrors(List<String> errors){
        String cont = "";
        for(int i = 0; i<errors.size(); i++){
            cont += errors.get(i)+"\n";
        }
        return cont;
    }

}
