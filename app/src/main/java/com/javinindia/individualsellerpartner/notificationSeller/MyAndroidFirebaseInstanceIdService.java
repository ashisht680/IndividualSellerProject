package com.javinindia.individualsellerpartner.notificationSeller;


import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javinindia.individualsellerpartner.preferenceSeller.SharedPreferencesManager;


public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(refreshedToken)) {
            SharedPreferencesManager.setDeviceToken(getApplicationContext(), refreshedToken);
        } else {

        }


    }

}