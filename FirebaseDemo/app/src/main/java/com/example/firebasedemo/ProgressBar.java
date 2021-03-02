package com.example.firebasedemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

public class ProgressBar {
    private static ProgressDialog progress;

    public static void showProgressBar(Activity activity, String title, String message) {
        try {
            if (progress == null) {
                progress = new ProgressDialog(activity);
            } else {
                progress.hide();
                progress = new ProgressDialog(activity);
            }
            progress.setTitle(title);
            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        } catch (Exception ex) {
            Log.e("Progress Bar Error", ex.toString());
        }
    }

    public static void hideProgressBar() {
        if (progress != null && progress.isShowing()) {
            progress.hide();
            progress.dismiss();
            progress = null;
        }
    }
}
