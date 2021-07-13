package com.nerothtr.githubrepo.app;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
