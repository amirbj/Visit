package com.andc.slidingmenu.ExceptionViewer;

import android.app.AlertDialog;
import android.content.Context;

public class ShowExceptions extends Exception {
    private static final long serialVersionUID = 1L;
    private String message = null;


    public ShowExceptions() {
        super();
    }


    public ShowExceptions(String message) {
        this.message = message;
    }


    /**
     *
     * @param context
     * show the exception dialog to user when an exception occured
     *
     */
    public void alertUser(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("WARNING");
        dialog.setMessage(message);
        dialog.setNeutralButton("Ok", null);
        dialog.create().show();

    }

    public String getMessages() {
        return message;
    }
}