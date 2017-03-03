package com.javinindia.individualsellerpartner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javinindia.individualsellerpartner.R;
import com.javinindia.individualsellerpartner.fragments.SellerBaseFragment;
import com.javinindia.individualsellerpartner.fragments.SellerCheckConnectionFragment;
import com.javinindia.individualsellerpartner.fragments.SellerLoginFragment;
import com.javinindia.individualsellerpartner.fragments.SellerNavigationAboutFragment;
import com.javinindia.individualsellerpartner.preferenceSeller.SharedPreferencesManager;
import com.javinindia.individualsellerpartner.utilitySeller.CheckConnection;

/**
 * Created by Ashish on 26-09-2016.
 */
public class LoginActivitySeller extends SellerBaseActivity implements SellerCheckConnectionFragment.OnCallBackInternetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        new MyAndroidFirebaseInstanceIdService();
        if (CheckConnection.haveNetworkConnection(this)) {
            String username = SharedPreferencesManager.getUsername(getApplicationContext());
            if (TextUtils.isEmpty(username)) {
                SellerBaseFragment sellerBaseFragment = new SellerLoginFragment();
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                fragmentTransaction.add(R.id.container, sellerBaseFragment);
                fragmentTransaction.commit();
            } else {
                SellerBaseFragment sellerBaseFragment = new SellerNavigationAboutFragment();
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                fragmentTransaction.add(R.id.container, sellerBaseFragment);
                fragmentTransaction.commit();
            }
        } else {
            SellerCheckConnectionFragment baseFragment = new SellerCheckConnectionFragment();
            baseFragment.setMyCallBackInternetListener(this);
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
            fragmentTransaction.add(R.id.container, baseFragment);
            fragmentTransaction.commit();
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void OnCallBackInternet() {
        Intent refresh = new Intent(this, LoginActivitySeller.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }

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
}
