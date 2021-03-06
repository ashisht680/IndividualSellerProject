package com.javinindia.individualsellerpartner.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.individualsellerpartner.R;
import com.javinindia.individualsellerpartner.activity.LoginActivitySeller;
import com.javinindia.individualsellerpartner.Sellerapiparsing.loginsignupparsing.LoginSignupResponseParsing;
import com.javinindia.individualsellerpartner.Sellerapiparsing.shopmalllistparsing.ShopMallListResponseParsing;
import com.javinindia.individualsellerpartner.Sellerapiparsing.stateparsing.CityMasterParsing;
import com.javinindia.individualsellerpartner.Sellerapiparsing.stateparsing.CountryMasterApiParsing;
import com.javinindia.individualsellerpartner.constantSeller.Constants;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapRegularSingleTonClass;
import com.javinindia.individualsellerpartner.preferenceSeller.SharedPreferencesManager;
import com.javinindia.individualsellerpartner.utilitySeller.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 08-09-2016.
 */
public class SellerSignUpAddressFragment extends SellerBaseFragment implements View.OnClickListener {

    private AppCompatEditText et_State, et_City, et_PinCode, etStoreNum;
    //, et_Mall, etFloor
    CheckBox radioButton;
    TextView txtTermCondition;
    private RequestQueue requestQueue;
    private SellerBaseFragment fragment;
    String storeName, owner, email, mobileNum, landline, password, mallId;
    public ArrayList<String> stateList = new ArrayList<>();
    String stateArray[] = null;
    public ArrayList<String> cityList = new ArrayList<>();
    String cityArray[] = null;
    public ArrayList<String> mallList = new ArrayList<>();
    String mallArray[] = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeName = getArguments().getString("storeName");
        owner = getArguments().getString("owner");
        email = getArguments().getString("email");
        mobileNum = getArguments().getString("mobileNum");
        landline = getArguments().getString("landline");
        password = getArguments().getString("password");
        getArguments().remove("storeName");
        getArguments().remove("owner");
        getArguments().remove("email");
        getArguments().remove("mobileNum");
        getArguments().remove("landline");
        getArguments().remove("password");
        Log.e("address", storeName + "\t" + owner + "\t" + email + "\t" + mobileNum + "\t" + landline + "\t" + password + "");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getFragmentLayout(), container, false);

        initialize(view);

        return view;
    }

    private void initialize(View view) {
        ImageView imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        AppCompatButton btn_regester = (AppCompatButton) view.findViewById(R.id.btn_regester);
        btn_regester.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_State = (AppCompatEditText) view.findViewById(R.id.et_State);
        et_State.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_City = (AppCompatEditText) view.findViewById(R.id.et_City);
        et_City.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_PinCode = (AppCompatEditText) view.findViewById(R.id.et_PinCode);
        et_PinCode.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        //   et_Address = (AppCompatEditText) view.findViewById(R.id.et_Address);
        //   et_Address.setTypeface(FontRalewayRegularSingleTonClass.getInstance(activity).getTypeFace());
       /* et_Mall = (AppCompatEditText) view.findViewById(R.id.et_Mall);
        et_Mall.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());*/
        etStoreNum = (AppCompatEditText) view.findViewById(R.id.etStoreNum);
        etStoreNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        /*etFloor = (AppCompatEditText) view.findViewById(R.id.etFloor);
        etFloor.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());*/
        radioButton = (CheckBox) view.findViewById(R.id.radioButton);
        txtTermCondition = (TextView) view.findViewById(R.id.txtTermCondition);
        txtTermCondition.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTermCondition.setText(Html.fromHtml("<font color=#ffffff>" + "I accept the" + "</font>" + "\t" + "<font color=#0d7bbf>" + "terms and conditions." + "</font>"));
        txtTermCondition.setOnClickListener(this);

        et_State.setOnClickListener(this);
        et_City.setOnClickListener(this);
       // et_Mall.setOnClickListener(this);
        radioButton.setOnClickListener(this);
        btn_regester.setOnClickListener(this);

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.sign_up_address_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_regester:
                Utility.hideKeyboard(activity);
                registrationMethod();
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtTermCondition:
                SellerBaseFragment termFragment = new SellerTermAndConditionFragment();
                callFragmentMethod(termFragment, this.getClass().getSimpleName(), R.id.container);
                break;
            case R.id.et_State:
                methodState();
                break;
            case R.id.et_City:
                if (!TextUtils.isEmpty(et_State.getText())) {
                    methodCity();
                } else {
                    Toast.makeText(activity, "Select State first", Toast.LENGTH_LONG).show();
                }
                break;
           /* case R.id.et_Mall:
                if (!TextUtils.isEmpty(et_City.getText())) {
                    methodMallList();
                } else {
                    Toast.makeText(activity, "Select City first", Toast.LENGTH_LONG).show();
                }
                break;*/
          /*  case R.id.radioButton:
                if (radioButton.isChecked()) {
                    radioButton.setChecked(false);
                }else {
                    radioButton.setChecked(false);
                }
                break;*/
        }
    }

    private void methodMallList() {
        mallList.removeAll(mallList);
        mallArray = null;

        sendRequestMallList();
    }

    private void sendRequestMallList() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MALL_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("mall", response);
                        final ShopMallListResponseParsing shopMallListResponseParsing = new ShopMallListResponseParsing();
                        shopMallListResponseParsing.responseParseMethod(response);
                        if (shopMallListResponseParsing.getStatus().equals("true")) {
                            if (shopMallListResponseParsing.getMallDetailsArrayList().size() > 0) {
                                for (int i = 0; i < shopMallListResponseParsing.getMallDetailsArrayList().size(); i++) {
                                    mallList.add(shopMallListResponseParsing.getMallDetailsArrayList().get(i).getMallName());
                                }
                                if (mallList.size() > 0) {
                                    mallArray = new String[mallList.size()];
                                    mallList.toArray(mallArray);

                                    if (mallList != null && mallList.size() > 0) {
                                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                        builder.setTitle("Select Mall");
                                        builder.setNegativeButton(android.R.string.cancel, null);
                                        builder.setItems(mallArray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                // Do something with the selection
                                                //et_Mall.setText(mallArray[item]);

                                                // Log.e("mall", shopMallListResponseParsing.getMallDetailsArrayList().get(1).getId());
                                                int index = Arrays.asList(mallArray).indexOf(mallArray[item]);
                                                ;
                                                mallId = shopMallListResponseParsing.getMallDetailsArrayList().get(index).getId();
                                                et_PinCode.setText(shopMallListResponseParsing.getMallDetailsArrayList().get(index).getPincode());
                                                Log.e("mallId", mallId + "\t" + index);
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create();
                                        builder.show();
                                    }
                                }
                            } else {
                                showDialogMethod("Mall not found");
                            }
                        } else {
                            showDialogMethod("Mall not found");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state", et_State.getText().toString().trim());
                params.put("city", et_City.getText().toString().trim());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodCity() {
        cityList.removeAll(cityList);
        cityArray = null;

        sendRequestOnCity();
    }

    private void sendRequestOnCity() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //  progressDialog.dismiss();
                        Log.e("MasterTags", response);
                        CityMasterParsing cityMasterParsing = new CityMasterParsing();
                        cityMasterParsing.responseParseMethod(response);
                        for (int i = 0; i < cityMasterParsing.getCountryDetails().getCityDetails().size(); i++) {
                            cityList.add(cityMasterParsing.getCountryDetails().getCityDetails().get(i).getCity());
                        }
                        if (cityList.size() > 0) {
                            cityArray = new String[cityList.size()];
                            cityList.toArray(cityArray);

                            if (cityList != null && cityList.size() > 0) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                //  builder.setTitle("Select City");
                                builder.setNegativeButton(android.R.string.cancel, null);
                                builder.setItems(cityArray, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        // Do something with the selection
                                        et_City.setText(cityArray[item]);
                                        //et_Mall.setText("");
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country", "india");
                params.put("state", et_State.getText().toString());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodState() {
        stateList.removeAll(stateList);
        stateArray = null;
        sendRequestOnState();
    }

    private void sendRequestOnState() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("MasterTags", response);
                        CountryMasterApiParsing countryMasterApiParsing = new CountryMasterApiParsing();
                        countryMasterApiParsing.responseParseMethod(response);
                        if (countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size() > 0) {
                            for (int i = 0; i < countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size(); i++) {
                                stateList.add(countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().get(i).getState());
                            }
                            if (stateList.size() > 0) {
                                stateArray = new String[stateList.size()];
                                stateList.toArray(stateArray);

                                if (stateList != null && stateList.size() > 0) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                    //  builder.setTitle("Select State");
                                    builder.setNegativeButton(android.R.string.cancel, null);
                                    builder.setItems(stateArray, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // Do something with the selection
                                            et_State.setText(stateArray[item]);
                                            et_City.setText("");
                                            //et_Mall.setText("");
                                        }
                                    });
                                    builder.create();
                                    builder.show();
                                }
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country", "india");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void registrationMethod() {
        String state = et_State.getText().toString().trim();
        String city = et_City.getText().toString().trim();
        String pinCode = et_PinCode.getText().toString().trim();
        String storeNum = etStoreNum.getText().toString().trim();
        //String mallname = et_Mall.getText().toString().trim();
       // String floor = etFloor.getText().toString().trim();
        String mallname = "mall";
        String floor = "floor";

        if (registerValidation(state, city, pinCode, storeNum, mallname, floor)) {
            sendDataOnRegistrationApi(state, city, pinCode, storeNum, "mallId", storeName, owner, email, mobileNum, landline, password, floor);
        }

    }

    private void sendDataOnRegistrationApi(final String statehit, final String cityhit, final String pinCodehit, final String storeNumshit, final String mallhit, final String storeNamehit, final String ownerhit, final String emailhit, final String mobileNumhit, final String landlinehit, final String passwordhit, final String floorhit) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SIGN_UP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String status = null, sID = null, msg = null, sPic = null;
                        String sName, oName, sEmail, sMobileNum, sLandline, sState, sCity, sAddress, mName, mAddress, mLat, mLong;
                        loading.dismiss();
                        LoginSignupResponseParsing loginSignupResponseParsing = new LoginSignupResponseParsing();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus().trim();
                        msg = loginSignupResponseParsing.getMsg();

                        if (status.equalsIgnoreCase("true") && !status.isEmpty()) {
                            sID = loginSignupResponseParsing.getShopid();
                            sPic = loginSignupResponseParsing.getProfilepic();
                            sName = loginSignupResponseParsing.getStoreName();
                            oName = loginSignupResponseParsing.getOwnerName();
                            sEmail = loginSignupResponseParsing.getEmail();
                            sMobileNum = loginSignupResponseParsing.getMobile();
                            sLandline = loginSignupResponseParsing.getLandline();
                            sState = loginSignupResponseParsing.getState();
                            sCity = loginSignupResponseParsing.getCity();
                            mName = loginSignupResponseParsing.getMallName();
                            mAddress = loginSignupResponseParsing.getMallAddress();
                            mLat = loginSignupResponseParsing.getMallLat();
                            mLong = loginSignupResponseParsing.getMallLong();
                            saveDataOnPreference(sEmail, sName, mLat, mLong, sID, sPic,oName);
                            Intent refresh = new Intent(activity, LoginActivitySeller.class);
                            startActivity(refresh);//Start the same Activity
                            activity.finish();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod("Sorry, this email account already exists. Please enter a different email id.");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //  String completeAddress = addresshit + ",\t" + pinCodehit;
                Map<String, String> params = new HashMap<String, String>();
                params.put("storeName", storeNamehit);
                params.put("ownerName", ownerhit);
                params.put("email", emailhit);
                params.put("password", passwordhit);
                params.put("mobile", mobileNumhit);
                params.put("landline", landlinehit);
                params.put("state", statehit);
                params.put("city", cityhit);
                params.put("mall", "1");
                params.put("shopNo", storeNumshit);
                params.put("floor", pinCodehit);
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))){
                    params.put("deviceToken",SharedPreferencesManager.getDeviceToken(activity));
                }else {
                    params.put("deviceToken","deviceToken");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private boolean registerValidation(String state, String city, String pinCode, String storeNum, String mallname, String floor) {
        if (TextUtils.isEmpty(state)) {
            Toast.makeText(activity, "You have not entered state", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(activity, "You have not entered city", Toast.LENGTH_LONG).show();
            return false;
        }/* else if (TextUtils.isEmpty(mallname)) {
            Toast.makeText(activity, "You have not entered mall", Toast.LENGTH_LONG).show();
            return false;
        }*/ else if (TextUtils.isEmpty(storeNum)) {
            etStoreNum.setError("You have not entered address");
            etStoreNum.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(pinCode)) {
            et_PinCode.setError("You have not entered pincode");
            et_PinCode.requestFocus();
            return false;
        } else if (!radioButton.isChecked()) {
            Toast.makeText(activity, "You have not accepted the terms and conditions.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveDataOnPreference(String sEmail, String sName, String mLat, String mLong, String sID, String profilepic,String owName) {
        SharedPreferencesManager.setUserID(activity, sID);
        SharedPreferencesManager.setEmail(activity, sEmail);
        SharedPreferencesManager.setUsername(activity, sName);
        SharedPreferencesManager.setLatitude(activity, mLat);
        SharedPreferencesManager.setLongitude(activity, mLong);
        SharedPreferencesManager.setProfileImage(activity, profilepic);
        SharedPreferencesManager.setOwnerName(activity,owName);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getSimpleName());
        }
    }
}