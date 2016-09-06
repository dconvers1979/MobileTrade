package co.com.firefly.daviviendatrade.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by toshiba on 06/07/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = MyFirebaseInstanceIDService.class.getName();

    @Override
    public void onTokenRefresh(){

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG , "Refreshed token: " + refreshedToken);
    }

}
