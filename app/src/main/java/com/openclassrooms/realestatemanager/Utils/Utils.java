package com.openclassrooms.realestatemanager.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.room.TypeConverter;

import com.openclassrooms.realestatemanager.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.912);
    }

    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros * 1.10);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context) {
        //    WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        //    return wifi.isWifiEnabled();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = false;

        if (activeNetwork != null) {
            isConnected = activeNetwork.isConnected();
        }
        return isConnected;
    }

    /**
     * Retourne Le chemin complet d'une Uri
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Retourne la mensualité en fonction du montant du crédit et sa durée
     * @param amountBorrowed
     * @param loanRate
     * @param loanDuration
     * @return
     */
    public static Double getMonthlyPayment(int amountBorrowed, double loanRate, int loanDuration) {
        double monthlyRate = (loanRate / 100) /12;
        double duration = (loanDuration * - 1);
        double result = (amountBorrowed * monthlyRate) / (1 - (Math.pow((1 + monthlyRate), duration)));
        return Double.valueOf(String.format("%.2f", result));
    }

    /**
     * Retourne le chemin du ficher
     * @param context
     * @return
     * @throws IOException
     */
   public static File createFilePath(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
    }

    /**
     * Retourne la date au format "d MMM yyyy HH:mm" ex: 8 Sep 2019 11:54
     * @param date
     * @return
     */
    public static String getFormattedDate(Date date){
       return new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault()).format(date);
    }

    /**
     * Affiche un message Toast pour avertir l'utilisateur de vérifier sa connection internet
     * @param context
     */
    public static void noInternetConnectionToast(Context context){
        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
    }
}
