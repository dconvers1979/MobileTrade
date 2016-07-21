package co.com.firefly.daviviendatrade.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import co.com.firefly.daviviendatrade.R;
import co.com.firefly.daviviendatrade.StockListingActivity;

/**
 * Created by toshiba on 14/07/2016.
 */
public class ServiceInvoker extends AsyncTask<Void, Void, JSONObject> {

    AppCompatActivity activity;

    //http://localhost:8080/FiretradeBackEndDemo-war/webresources/firetrade/getUser

    public ServiceInvoker(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try{

            return getClientName();
        }catch(Exception e){
            return null;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        if(response == null) {
            //TODO send error
        }else{

            try{
                Intent intent = new Intent(activity, StockListingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FCM Message")
                        .setContentText(response.getString("name"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    public JSONObject getClientName() throws Exception{
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://192.168.1.123:8080/FiretradeBackEndDemo-war/webresources/firetrade/getUser/?email=dclaverde@hotmail.com");
            urlConnection = (HttpURLConnection) url.openConnection();


                // handle issues
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    //TODO send error
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    //TODO send error
                }

                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                JSONObject response = new JSONObject(getResponseText(in));

                return response;

        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally{
            if(urlConnection!=null){
                urlConnection.disconnect();
            }

        }



    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }



}
