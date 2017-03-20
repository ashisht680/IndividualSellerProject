package com.javinindia.individualsellerpartner.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.javinindia.individualsellerpartner.R;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapRegularSingleTonClass;

/**
 * Created by Ashish on 16-01-2017.
 */

public class Seller_looking_for_fragment extends SellerBaseFragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        RelativeLayout ll_employee,ll_web,ll_graphic,ll_financial;
        AppCompatTextView txt_employee,txt_web,txt_graphic,txt_financial;
        txt_employee = (AppCompatTextView)view.findViewById(R.id.txt_employee);
        txt_employee.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txt_web = (AppCompatTextView)view.findViewById(R.id.txt_web);
        txt_web.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txt_graphic = (AppCompatTextView)view.findViewById(R.id.txt_graphic);
        txt_graphic.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txt_financial = (AppCompatTextView)view.findViewById(R.id.txt_financial);
        txt_financial.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        ll_employee = (RelativeLayout)view.findViewById(R.id.ll_employee);
        ll_web = (RelativeLayout)view.findViewById(R.id.ll_web);
        ll_graphic = (RelativeLayout)view.findViewById(R.id.ll_graphic);
        ll_financial = (RelativeLayout)view.findViewById(R.id.ll_financial);
        ll_employee.setOnClickListener(this);
        ll_web.setOnClickListener(this);
        ll_graphic.setOnClickListener(this);
        ll_financial.setOnClickListener(this);
    }

    private void initToolbar(View view) {
        setToolbarTitle("Upcoming Features");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.seller_looking_for_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_employee:
                dialogBox();
                break;
            case R.id.ll_web:
                dialogBox();
                break;
            case R.id.ll_graphic:
                dialogBox();
                break;
            case R.id.ll_financial:
                dialogBox();
                break;
        }
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("These features will be launched soon. You may check again shortly. Thanks!");
        alertDialogBuilder.setNegativeButton("Got it!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
