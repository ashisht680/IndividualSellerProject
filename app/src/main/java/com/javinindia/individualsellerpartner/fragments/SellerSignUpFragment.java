package com.javinindia.individualsellerpartner.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.javinindia.individualsellerpartner.R;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapRegularSingleTonClass;
import com.javinindia.individualsellerpartner.utilitySeller.Utility;


public class SellerSignUpFragment extends SellerBaseFragment implements View.OnClickListener {

    private AppCompatEditText  et_owner, et_email, et_MobileNum, et_Landline, et_password, et_ConfirmPassword;
    //et_StoreNum,
    private RequestQueue requestQueue;
    private SellerBaseFragment fragment;
    private CheckBox checkShowPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        ImageView imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        AppCompatButton btnNext = (AppCompatButton) view.findViewById(R.id.btnNext);
        btnNext.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnNext.setOnClickListener(this);
       /* et_StoreNum = (AppCompatEditText) view.findViewById(R.id.et_StoreNum);
        et_StoreNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());*/
        et_owner = (AppCompatEditText) view.findViewById(R.id.et_owner);
        et_owner.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_email = (AppCompatEditText) view.findViewById(R.id.et_email);
        et_email.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_MobileNum = (AppCompatEditText) view.findViewById(R.id.et_MobileNum);
        et_MobileNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_Landline = (AppCompatEditText) view.findViewById(R.id.et_Landline);
        et_Landline.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_password = (AppCompatEditText) view.findViewById(R.id.et_password);
        et_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_ConfirmPassword = (AppCompatEditText) view.findViewById(R.id.et_ConfirmPassword);
        et_ConfirmPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        checkShowPassword = (CheckBox)view.findViewById(R.id.checkShowPassword);
        checkShowPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        checkShowPassword.setOnClickListener(this);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.sign_up_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNext:
                registrationMethod();
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.checkShowPassword:
                if (checkShowPassword.isChecked()){
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }

    private void registrationMethod() {
       // String storeName = et_StoreNum.getText().toString().trim();
        String storeName ="store name";
        String owner = et_owner.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String mobileNum = et_MobileNum.getText().toString().trim();
        String landline = et_Landline.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirmPassword = et_ConfirmPassword.getText().toString().trim();

        if (registerValidation(storeName, owner, email, mobileNum, landline, password, confirmPassword)) {
            sendDataOnRegistrationApi(storeName, owner, email, mobileNum, landline, password, confirmPassword);
        }

    }

    private void sendDataOnRegistrationApi(String storeName, String owner, String email, String mobileNum, String landline, String password, String confirmPassword) {
        SellerBaseFragment signUpFragment = new SellerSignUpAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("storeName", storeName);
        bundle.putString("owner", owner);
        bundle.putString("email", email);
        bundle.putString("mobileNum", mobileNum);
        bundle.putString("landline", landline);
        bundle.putString("password", password);
        signUpFragment.setArguments(bundle);
        callFragmentMethod(signUpFragment, this.getClass().getSimpleName(), R.id.container);
    }

    private boolean registerValidation(String storeName, String owner, String email, String mobileNum, String landline, String password, String confirmPassword) {
       /* if (TextUtils.isEmpty(storeName)) {
            et_StoreNum.setError("You have not entered any store name.");
            et_StoreNum.requestFocus();
            return false;
        } else*/ if (TextUtils.isEmpty(owner)) {
            et_owner.setError("You have not entered owner's name");
            et_owner.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            et_email.setError("Email id entered is invalid");
            et_email.requestFocus();
            return false;
        } else if (mobileNum.length() != 10) {
            et_MobileNum.setError("Mobile number entered is invalid");
            et_MobileNum.requestFocus();
            return false;
        } else if (password.length() < 6) {
            et_password.setError("Password should be more than 6 characters");
            et_password.requestFocus();
            return false;
        } else if (!confirmPassword.equals(password)) {
            et_ConfirmPassword.setError("Password does not match");
            et_ConfirmPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getSimpleName());
        }
    }
}
