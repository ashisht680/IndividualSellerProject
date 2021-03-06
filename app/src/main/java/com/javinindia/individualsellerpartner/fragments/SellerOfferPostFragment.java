package com.javinindia.individualsellerpartner.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.javinindia.individualsellerpartner.R;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapBoldSingleTonClass;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapRegularSingleTonClass;
import com.javinindia.individualsellerpartner.preferenceSeller.SharedPreferencesManager;
import com.javinindia.individualsellerpartner.utilitySeller.Utility;


public class SellerOfferPostFragment extends SellerBaseFragment implements View.OnClickListener {
    ImageView imgBrand, imgOffer;
    RatingBar ratingBar;
    AppCompatTextView txtOfferBrandNamePost, txtRating, txtMallNamePost, txtOffers, txtOfferPercentage, txtSubcategory,
            txtOfferDate, txtShopTiming, txtOfferDiscription, txtOfferActualPrice, txtOfferTitle, txtFavCount, txtOfferDiscountPrice,txtAddress;
    AppCompatButton btnRate;
    // CheckBox chkImageMall;
    ProgressBar progressBar;

    String brandName, brandPic, shopName, mallName, offerRating, offerPic, offerTitle, offerCategory, offerSubCategory, offerPercentType, shopPic,
            offerPercentage, offerActualPrice, offerDiscountPr, offerStartDate, offerCloseDate, offerDescription, shopOpenTime, shopCloseTime, favCount,shopNewAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        shopPic = getArguments().getString("shopPic");
        favCount = getArguments().getString("favCount");
        brandName = getArguments().getString("brandName");
        brandPic = getArguments().getString("brandPic");
        shopName = getArguments().getString("shopName");
        mallName = getArguments().getString("mallName");
        offerRating = getArguments().getString("offerRating");
        offerPic = getArguments().getString("offerPic");
        offerTitle = getArguments().getString("offerTitle");
        offerCategory = getArguments().getString("offerCategory");
        offerSubCategory = getArguments().getString("offerSubCategory");
        offerPercentType = getArguments().getString("offerPercentType");
        offerPercentage = getArguments().getString("offerPercentage");
        offerActualPrice = getArguments().getString("offerActualPrice");
        offerDiscountPr = getArguments().getString("offerDiscountPrice");
        offerStartDate = getArguments().getString("offerStartDate");
        offerCloseDate = getArguments().getString("offerCloseDate");
        offerDescription = getArguments().getString("offerDescription");
        shopOpenTime = getArguments().getString("shopOpenTime");
        shopCloseTime = getArguments().getString("shopCloseTime");
        shopNewAddress = getArguments().getString("shopNewAddress");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null){
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        setDataOnView();
        return view;
    }


    private void initToolbar(View view) {
        if (!TextUtils.isEmpty(brandName)){
            setToolbarTitle(brandName);
        }else if (!TextUtils.isEmpty(shopName)){
            setToolbarTitle(shopName);
        }else {
            setToolbarTitle("Offer");
        }
    }

    private void setDataOnView() {
        if (!TextUtils.isEmpty(brandName))
            txtOfferBrandNamePost.setText(Utility.fromHtml(brandName));

        if (!TextUtils.isEmpty(offerRating))
            txtRating.setText("Rating:" + offerRating + "/5");
        ratingBar.setRating(Float.valueOf(offerRating));

        if (!TextUtils.isEmpty(SharedPreferencesManager.getOwnerName(activity)))
            txtMallNamePost.setText(Utility.fromHtml(SharedPreferencesManager.getOwnerName(activity)));

        if (!TextUtils.isEmpty(offerTitle))
            txtOfferTitle.setText(Utility.fromHtml(offerTitle));

        if (!TextUtils.isEmpty(offerPercentType) && !TextUtils.isEmpty(offerPercentage)) {
            txtOfferPercentage.setText(offerPercentType + " " + offerPercentage + "% off");
        } else if (!TextUtils.isEmpty(offerPercentType) && !TextUtils.isEmpty(offerActualPrice) && !TextUtils.isEmpty(offerDiscountPr) && TextUtils.isEmpty(offerPercentage)) {
            double actual = Double.parseDouble(offerActualPrice);
            double discount = Double.parseDouble(offerDiscountPr);
            int percent = (int) (100 - (discount * 100.0f) / actual);
            txtOfferPercentage.setText(offerPercentType + " " + percent + "% off");
        } else if (TextUtils.isEmpty(offerPercentType) && TextUtils.isEmpty(offerPercentage)) {
            if (!TextUtils.isEmpty(offerActualPrice) && !TextUtils.isEmpty(offerDiscountPr)) {
                double actual = Double.parseDouble(offerActualPrice);
                double discount = Double.parseDouble(offerDiscountPr);
                int percent = (int) (100 - (discount * 100.0f) / actual);
                txtOfferPercentage.setText(percent + "% off");
            }
        } else if (TextUtils.isEmpty(offerPercentType) && !TextUtils.isEmpty(offerPercentage)) {
            txtOfferPercentage.setText(offerPercentage + "% off");
        }

        if (!TextUtils.isEmpty(offerActualPrice) && !TextUtils.isEmpty(offerDiscountPr)) {
            txtOfferActualPrice.setText(Utility.fromHtml("\u20B9" + offerActualPrice + "/-"));
            txtOfferActualPrice.setPaintFlags(txtOfferActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtOfferDiscountPrice.setText(Utility.fromHtml("\u20B9" + offerDiscountPr + "/-"));
        }


        if (!TextUtils.isEmpty(offerCategory))
            txtSubcategory.setText(Utility.fromHtml("on " + offerCategory + "(" + offerSubCategory + ")"));

        if (!TextUtils.isEmpty(offerStartDate) && !TextUtils.isEmpty(offerCloseDate))
            txtOfferDate.setText("Valid from "+offerStartDate + " till " + offerCloseDate);

        if (!TextUtils.isEmpty(shopOpenTime) && !TextUtils.isEmpty(shopCloseTime))
            txtShopTiming.setText(shopOpenTime + " to " + shopCloseTime);

        if (!TextUtils.isEmpty(offerDescription))
            txtOfferDiscription.setText(Utility.fromHtml("Description: " + offerDescription));

        if (!TextUtils.isEmpty(offerPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgOffer, offerPic);
        } else if (!TextUtils.isEmpty(brandPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgOffer, brandPic);
        } else {

        }

        if (!TextUtils.isEmpty(shopPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgBrand, shopPic);
        } else if (!TextUtils.isEmpty(brandPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgBrand, brandPic);
        }
        if (!TextUtils.isEmpty(favCount)) {
            txtFavCount.setText(favCount);
        } else {
            txtFavCount.setText("0");
        }
        if (!TextUtils.isEmpty(shopNewAddress)){
            txtAddress.setText(shopNewAddress);
        }


    }

    private void initialize(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        // chkImageMall = (CheckBox) view.findViewById(R.id.chkImageMall);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        imgBrand = (ImageView) view.findViewById(R.id.imgBrand);
        imgOffer = (ImageView) view.findViewById(R.id.imgOffer);
        txtOfferBrandNamePost = (AppCompatTextView) view.findViewById(R.id.txtOfferBrandNamePost);
        txtOfferBrandNamePost.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtRating = (AppCompatTextView) view.findViewById(R.id.txtRating);
        txtRating.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMallNamePost = (AppCompatTextView) view.findViewById(R.id.txtMallNamePost);
        txtMallNamePost.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddress = (AppCompatTextView) view.findViewById(R.id.txtAddress);
        txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferDiscountPrice = (AppCompatTextView) view.findViewById(R.id.txtOfferDiscountPrice);
        txtOfferDiscountPrice.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferPercentage = (AppCompatTextView) view.findViewById(R.id.txtOfferPercentage);
        txtOfferPercentage.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtSubcategory = (AppCompatTextView) view.findViewById(R.id.txtSubcategory);
        txtSubcategory.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferDate = (AppCompatTextView) view.findViewById(R.id.txtOfferDate);
        txtOfferDate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtShopTiming = (AppCompatTextView) view.findViewById(R.id.txtShopTiming);
        txtShopTiming.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferDiscription = (AppCompatTextView) view.findViewById(R.id.txtOfferDiscription);
        txtOfferDiscription.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnRate = (AppCompatButton) view.findViewById(R.id.btnRate);
        btnRate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferActualPrice = (AppCompatTextView) view.findViewById(R.id.txtOfferActualPrice);
        txtOfferActualPrice.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferTitle = (AppCompatTextView) view.findViewById(R.id.txtOfferTitle);
        txtOfferTitle.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtFavCount = (AppCompatTextView) view.findViewById(R.id.txtFavCount);
        txtFavCount.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        imgBrand.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        imgOffer.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.offer_detail_layout;
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
            case R.id.btnRate:
                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.imgBrand:
                activity.onBackPressed();
                break;
            case R.id.imgOffer:
                ZoomImageFragment zoomImageFragment = new ZoomImageFragment();
                Bundle bundle = new Bundle();

                if (!TextUtils.isEmpty(offerPic)) {
                    bundle.putString("img", offerPic);
                } else if (!TextUtils.isEmpty(brandPic)) {
                    bundle.putString("img", brandPic);
                } else {
                    bundle.putString("img", "");
                }
                zoomImageFragment.setArguments(bundle);
                callFragmentMethod(zoomImageFragment, this.getClass().getSimpleName(), R.id.container);
                break;
        }
    }

}
