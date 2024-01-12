package com.venta.appmedica.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.venta.appmedica.models.GoogleSheetsResponse;
import com.venta.appmedica.models.IGoogleSheets;

public class Common {
    public static String BASE_URL = "https://script.google.com/macros/s/AKfycbz_0u_z9Ig_9q2j9rzlFgt1hfdRIy6sV5rbV773KGfMYKWesIYUwlIe5oAlV6Bs_q6c/";
    public static String GOOGLE_SHEET_ID = "1yJmJeAKDgXqzlv13PdOotERyqJp82qGahwYGyPiXFRc";
    public static String SHEET_NAME = "Maternas";

    public static IGoogleSheets iGSGetMethodClient(String baseUrl) {
        return GoogleSheetsResponse.getClientGetMethod(baseUrl)
                .create(IGoogleSheets.class);
    }

    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
