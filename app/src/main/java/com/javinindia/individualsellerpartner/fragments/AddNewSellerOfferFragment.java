package com.javinindia.individualsellerpartner.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.javinindia.individualsellerpartner.Sellerapiparsing.brandparsing.Brandresponse;
import com.javinindia.individualsellerpartner.Sellerapiparsing.offerCategoryParsing.OfferCategoryresponse;
import com.javinindia.individualsellerpartner.constantSeller.Constants;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapBoldSingleTonClass;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapRegularSingleTonClass;
import com.javinindia.individualsellerpartner.preferenceSeller.SharedPreferencesManager;
import com.javinindia.individualsellerpartner.utilitySeller.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javinindia.individualsellerpartner.utilitySeller.Utility.getOutputMediaFile;
import static com.javinindia.individualsellerpartner.utilitySeller.Utility.getResizedBitmap;
import static com.javinindia.individualsellerpartner.utilitySeller.Utility.scaleImage;

/**
 * Created by Ashish on 15-09-2016.
 */
public class AddNewSellerOfferFragment extends SellerBaseFragment implements View.OnClickListener,AddSellerSubCatFragment.OnCallBackCAtegoryListener {
    private String day = "";
    private String month = "";
    private String year = "";
    private String hour = "";
    private String min = "";
    private String sec = "";


    // CheckBox checkboxPercent, checkboxPrise;
    AppCompatTextView txtChooseCategory, txtAddProductImages, btnStartTime, btnEndTime, txtChooseSubCategory,
            txtChoosePercent, txtAdditional, txtEnterPercentTitle, txtOr, txtEnterPriceTitle, txtTitle, txtTitleDisc;
    AppCompatEditText etProductTitle, etProductDescription, etPercentage, etActualPrice, etDiscountPrice, txtChooseBrand;
    AppCompatButton btnSubmitOffer;

    private RequestQueue requestQueue;
    private int tempId = 0;

    public ArrayList<String> categoryList = new ArrayList<>();
    String categoryArray[] = null;
    public ArrayList<String> suCatList = new ArrayList<>();
    String suCatArray[] = null;
    public ArrayList<String> brandList = new ArrayList<>();
    String brandArray[] = null;
    String catId;
    String subCatId;
    String brandId;

    String DefaultBrand="Brands";

    private Uri mImageCaptureUri;
    private ImageView mImageView;
   // private android.app.AlertDialog dialog;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap photo = null;
    int size = 0;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    private File outPutFile = null;

    private OnCallBackAddOfferListener callbackAddOffer;

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
    public void onCallBackCat() {

    }

    public interface OnCallBackAddOfferListener {
        void OnCallBackAddOffer();
    }

    public void setMyCallBackOfferListener(OnCallBackAddOfferListener callback) {
        this.callbackAddOffer = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        tempId = Utility.getRandomNumberInRange(10000000, 999999999);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        return view;
    }

    private void initToolbar(View view) {
        setToolbarTitle("Add offer");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        mImageView = (ImageView) view.findViewById(R.id.ivImage);
        //  checkboxPercent = (CheckBox) view.findViewById(R.id.checkboxPercent);
        //  checkboxPrise = (CheckBox) view.findViewById(R.id.checkboxPrise);
        txtAdditional = (AppCompatTextView) view.findViewById(R.id.txtAdditional);
        txtAdditional.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEnterPercentTitle = (AppCompatTextView) view.findViewById(R.id.txtEnterPercentTitle);
        txtEnterPercentTitle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOr = (AppCompatTextView) view.findViewById(R.id.txtOr);
        txtOr.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEnterPriceTitle = (AppCompatTextView) view.findViewById(R.id.txtEnterPriceTitle);
        txtEnterPriceTitle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTitle = (AppCompatTextView) view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtTitleDisc = (AppCompatTextView) view.findViewById(R.id.txtTitleDisc);
        txtTitleDisc.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        //horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.scroll1);
        //  lnrImages = (LinearLayout) view.findViewById(R.id.lnrImages);
        txtChooseCategory = (AppCompatTextView) view.findViewById(R.id.txtChooseCategory);
        txtChooseCategory.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtChooseSubCategory = (AppCompatTextView) view.findViewById(R.id.txtChooseSubCategory);
        txtChooseSubCategory.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtChooseBrand = (AppCompatEditText) view.findViewById(R.id.txtChooseBrand);
        txtChooseBrand.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddProductImages = (AppCompatTextView) view.findViewById(R.id.txtAddProductImages);
        txtAddProductImages.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etProductTitle = (AppCompatEditText) view.findViewById(R.id.etProductTitle);
        etProductTitle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etProductDescription = (AppCompatEditText) view.findViewById(R.id.etProductDiscription);
        etProductDescription.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnStartTime = (AppCompatTextView) view.findViewById(R.id.etStartTime);
        btnStartTime.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnEndTime = (AppCompatTextView) view.findViewById(R.id.etEndTime);
        btnEndTime.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtChoosePercent = (AppCompatTextView) view.findViewById(R.id.txtChoosePercent);
        txtChoosePercent.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etPercentage = (AppCompatEditText) view.findViewById(R.id.etPercentage);
        etPercentage.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etActualPrice = (AppCompatEditText) view.findViewById(R.id.etActualPrice);
        etActualPrice.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDiscountPrice = (AppCompatEditText) view.findViewById(R.id.etDiscountPrice);
        etDiscountPrice.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnSubmitOffer = (AppCompatButton) view.findViewById(R.id.btnSubmitOffer);
        btnSubmitOffer.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnSubmitOffer.setOnClickListener(this);
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        txtAddProductImages.setOnClickListener(this);
        txtChooseCategory.setOnClickListener(this);
        txtChooseSubCategory.setOnClickListener(this);
        txtChooseBrand.setOnClickListener(this);
        txtChoosePercent.setOnClickListener(this);
        //  checkboxPercent.setOnClickListener(this);
        //  checkboxPrise.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        //  checkboxPrise.setChecked(false);
        //   checkboxPercent.setChecked(true);

      /*  etPercentage.setEnabled(true);
        etPercentage.setBackgroundResource(R.drawable.button_border_red_fill_white);

        etActualPrice.setEnabled(false);
        etActualPrice.setBackgroundColor(Color.GRAY);

        etDiscountPrice.setEnabled(false);
        etDiscountPrice.setBackgroundColor(Color.GRAY);*/
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.add_new_offer_layout;
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
            case R.id.btnSubmitOffer:
                methodSubmit();
                break;
            case R.id.etStartTime:
                int flag1 = 0;
                methodOpenDatePicker(flag1);
                break;
            case R.id.etEndTime:
                if (!btnStartTime.getText().equals("Start date")) {
                    int flag2 = 1;
                    methodOpenDatePicker(flag2);
                } else {
                    Toast.makeText(activity, "Select Start date first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtChooseCategory:
                methodCategory();
                break;
            case R.id.txtChooseSubCategory:
                if (!txtChooseCategory.getText().equals("Choose category")) {
                    methodSubcat();
                } else {
                    Toast.makeText(activity, "Select category first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtChooseBrand:
                if (!txtChooseSubCategory.getText().equals("Choose subcategory")) {
                    methodBrands();
                } else {
                    Toast.makeText(activity, "Select Subcategory first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtChoosePercent:

                persentType();

                break;
            case R.id.ivImage:
                photo = null;
                mImageView.setVisibility(View.GONE);
                break;
            case R.id.txtAddProductImages:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                }else {
                    methodAddImages();
                }
                break;
        }

    }

    private void methodSubmit() {
        if (!txtChooseCategory.getText().equals("Choose category")) {
            if (!txtChooseSubCategory.getText().equals("Choose subcategory")) {
                if (!TextUtils.isEmpty(txtChooseBrand.getText().toString())) {
                    if (!etProductTitle.getText().toString().trim().equals("")) {
                        if (!etProductDescription.getText().toString().trim().equals("")) {
                            if (!btnStartTime.getText().equals("Start date")) {
                                if (!btnEndTime.getText().equals("End date")) {
                                    if (!etActualPrice.getText().toString().trim().equals("")) {
                                        if (!etDiscountPrice.getText().toString().trim().equals("")) {
                                            int act = Integer.parseInt(etActualPrice.getText().toString().trim());
                                            int dct = Integer.parseInt(etDiscountPrice.getText().toString().trim());
                                            if (act-dct>0){
                                                methodAddOffer(catId, subCatId, brandId, etProductTitle.getText().toString(), etProductDescription.getText().toString(), btnStartTime.getText().toString(), btnEndTime.getText().toString(), txtChoosePercent.getText().toString(), etPercentage.getText().toString(), etActualPrice.getText().toString(), etDiscountPrice.getText().toString());
                                            }else {
                                                Toast.makeText(activity, "The Discounted Price should always be lesser than the Actual Price.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(activity, "Please enter discounted price of your product.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(activity, "Please enter actual price of your product.", Toast.LENGTH_LONG).show();
                                    }
                                    //  }
                                } else {
                                    Toast.makeText(activity, "Please select end date.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(activity, "Please select start date.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(activity, "Please write a description for your offer.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(activity, "Please write a suitable title for your offer.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "Select "+DefaultBrand+" first", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, "Select Subcategory first", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity, "Select category first", Toast.LENGTH_LONG).show();
        }
    }

    private void methodAddOffer(final String catId, final String subCatId, final String brandId, final String title, final String desc, final String sDate, final String edate, final String percentType, final String percent, final String actualPrice, final String discountPrice) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INSERT_OFFER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        JSONObject jsonObject = null;
                        String status = null, userId = null, socialId = null, msg = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.has("status"))
                                status = jsonObject.optString("status");
                            if (jsonObject.has("msg"))
                                msg = jsonObject.optString("msg");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status.equals("true") && !TextUtils.isEmpty(status)) {
                            callbackAddOffer.OnCallBackAddOffer();
                            activity.onBackPressed();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod(msg);
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
                params.put("shopId", SharedPreferencesManager.getUserID(activity));
                params.put("offerTitle", title);
                params.put("offerDescription", desc);
                params.put("offerPercentage", "20%");
                params.put("offerCategory", catId);
                params.put("offerSubCategory", subCatId);
                params.put("brandId", brandId);
                params.put("openDate", sDate);
                params.put("closeDate", edate);
                if (txtChoosePercent.getText().toString().equals("Select Percentage Type")) {
                    params.put("selectPercentage", " ");
                } else {
                    params.put("selectPercentage", percentType);
                }
                params.put("enterPercentage", percent);
                params.put("actualPrice", actualPrice);
                params.put("discountedPrice", discountPrice);
                params.put("tempId", SharedPreferencesManager.getUserID(activity).concat("@").concat(String.valueOf(tempId)));
                //  params.put("banner","");
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("bimg", encodedImage + "image/jpeg");
                } else {
                    params.put("bimg", "");
                }


                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodBrands() {
        brandList.removeAll(brandList);
        brandArray = null;
        sendRequestOnBrand();
    }

    private void sendRequestOnBrand() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OFFER_BRAND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        final Brandresponse cityMasterParsing = new Brandresponse();
                        cityMasterParsing.responseImplement(response);
                        if (cityMasterParsing.getStatus().equals("true")) {
                            if (cityMasterParsing.getBrandDetailArrayList().size() > 0) {
                                for (int i = 0; i < cityMasterParsing.getBrandDetailArrayList().size(); i++) {
                                    brandList.add(cityMasterParsing.getBrandDetailArrayList().get(i).getName().trim());
                                }
                                if (brandList.size() > 0) {
                                    brandArray = new String[brandList.size()];
                                    brandList.toArray(brandArray);

                                    if (brandList != null && brandList.size() > 0) {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                        builder.setTitle("Select Brand");
                                        builder.setNegativeButton(android.R.string.cancel, null);
                                        builder.setItems(brandArray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                // Do something with the selection
                                                txtChooseBrand.setText(brandArray[item]);
                                                int index = Arrays.asList(brandArray).indexOf(brandArray[item]);
                                                brandId = cityMasterParsing.getBrandDetailArrayList().get(index).getId();
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create();
                                        builder.show();
                                    }
                                }
                            } else {
                                showDialogMethod("There are no Brands under this Subcategory.");
                            }
                        } else {
                            showDialogMethod("There are no Brands under this Subcategory.");
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
                params.put("shopCatId", catId);
                params.put("shopSubCatId", subCatId);
                params.put("shopId", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodCategory() {
        categoryList.removeAll(categoryList);
        categoryArray = null;
        sendRequestOnCategory();
    }

    private void sendRequestOnCategory() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OFFER_CATEGORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        final OfferCategoryresponse countryMasterApiParsing = new OfferCategoryresponse();
                        countryMasterApiParsing.responseImplement(response);
                        if (countryMasterApiParsing.getStatus().equals("true")) {
                            if (countryMasterApiParsing.getSetShopCategoryDetailsArrayList().size() > 0) {
                                for (int i = 0; i < countryMasterApiParsing.getSetShopCategoryDetailsArrayList().size(); i++) {
                                    categoryList.add(countryMasterApiParsing.getSetShopCategoryDetailsArrayList().get(i).getOfferCategory().trim());
                                }
                                if (categoryList.size() > 0) {
                                    categoryArray = new String[categoryList.size()];
                                    categoryList.toArray(categoryArray);

                                    if (categoryList != null && categoryList.size() > 0) {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                        builder.setTitle("Select Category");
                                        builder.setPositiveButton("ADD NEW", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                AddSellerSubCatFragment fragment1 = new AddSellerSubCatFragment();
                                                fragment1.setMyCallBackCategoryListener(AddNewSellerOfferFragment.this);
                                                callFragmentMethod(fragment1, this.getClass().getSimpleName(),R.id.container);
                                            }
                                        });
                                        builder.setNegativeButton(android.R.string.cancel, null);
                                        builder.setItems(categoryArray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                // Do something with the selection
                                                txtChooseCategory.setText(categoryArray[item]);
                                                txtChooseSubCategory.setText("Choose subcategory");
                                                if (categoryArray[item].equals("Books")){
                                                    txtChooseBrand.setHint("Choose Authors");
                                                    DefaultBrand = "Authors";
                                                }else {
                                                    txtChooseBrand.setHint("Choose Brand");
                                                    DefaultBrand = "Brands";
                                                }
                                                txtChooseBrand.setText("");
                                                int index = Arrays.asList(categoryArray).indexOf(categoryArray[item]);
                                                catId = countryMasterApiParsing.getSetShopCategoryDetailsArrayList().get(index).getId();
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create();
                                        builder.show();
                                    }
                                }
                            } else {
                                dialogBox();
                            }
                        } else {
                            dialogBox();
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
                params.put("shopid", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("You have not added any categories. Please add!");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AddSellerSubCatFragment fragment1 = new AddSellerSubCatFragment();
                        fragment1.setMyCallBackCategoryListener(AddNewSellerOfferFragment.this);
                        callFragmentMethod(fragment1, this.getClass().getSimpleName(),R.id.container);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void methodSubcat() {
        suCatList.removeAll(suCatList);
        suCatArray = null;
        sendRequestOnSubcat();
    }

    private void sendRequestOnSubcat() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OFFER_SUBCATEGORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        final OfferCategoryresponse cityMasterParsing = new OfferCategoryresponse();
                        cityMasterParsing.responseImplement(response);
                        if (cityMasterParsing.getStatus().equals("true")) {
                            if (cityMasterParsing.getSetShopCategoryDetailsArrayList().size() > 0) {
                                for (int i = 0; i < cityMasterParsing.getSetShopCategoryDetailsArrayList().size(); i++) {
                                    suCatList.add(cityMasterParsing.getSetShopCategoryDetailsArrayList().get(i).getOfferCategory().trim());
                                }
                                if (suCatList.size() > 0) {
                                    suCatArray = new String[suCatList.size()];
                                    suCatList.toArray(suCatArray);

                                    if (suCatList != null && suCatList.size() > 0) {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                        builder.setTitle("Select Subcategory");
                                        builder.setNegativeButton(android.R.string.cancel, null);
                                        builder.setItems(suCatArray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                txtChooseSubCategory.setText(suCatArray[item]);
                                                if (txtChooseCategory.getText().equals("Books")){
                                                    txtChooseBrand.setHint("Choose Authors");
                                                }else {
                                                    txtChooseBrand.setHint("Choose Brand");
                                                }
                                                txtChooseBrand.setText("");
                                                int index = Arrays.asList(suCatArray).indexOf(suCatArray[item]);
                                                subCatId = cityMasterParsing.getSetShopCategoryDetailsArrayList().get(index).getId().trim();
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create();
                                        builder.show();
                                    }
                                }
                            } else {
                                showDialogMethod("There are no Subcategory under this category.");
                            }
                        } else {
                            showDialogMethod("There are no Subcategory under this category.");
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
                params.put("id", catId);
                params.put("shopid", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodOpenDatePicker(final int flag) {
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (flag == 0) {
                            btnStartTime.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        } else {
                            btnEndTime.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        day = Integer.toString(dayOfMonth);
                        month = Integer.toString(monthOfYear + 1);
                        AddNewSellerOfferFragment.this.year = Integer.toString(year);
                    }
                }, mYear, mMonth, mDay);
        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMinDate(now);
        // c.add(c.DAY_OF_MONTH, 31);
        //  datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void methodAddImages() {
        final CharSequence[] options = {"Take from camera", "Select from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take from camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mImageCaptureUri = FileProvider.getUriForFile(activity,
                                "com.javinindia.individualsellerpartner.fileprovider",
                                getOutputMediaFile());


                    } else {
                        mImageCaptureUri = Uri.fromFile(getOutputMediaFile());
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else if (options[item].equals("Select from gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_FROM_FILE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != activity.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:

               // doCrop();

                if (outPutFile.exists()) {
                    try {

                        InputStream imageStream = activity.getContentResolver().openInputStream(mImageCaptureUri);
                        photo = BitmapFactory.decodeStream(imageStream);
                        photo = getResizedBitmap(photo, 1000);
                       // photo = scaleImage(photo);
                        mImageView.setVisibility(View.VISIBLE);
                        mImageView.setImageBitmap(photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                }

                break;

            case PICK_FROM_FILE:

                // After selecting image from files, save the selected path
                mImageCaptureUri = data.getData();
                try {

                    InputStream imageStream = activity.getContentResolver().openInputStream(mImageCaptureUri);
                    photo = BitmapFactory.decodeStream(imageStream);
                    photo = getResizedBitmap(photo, 1000);
                   // photo = scaleImage(photo);
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
              //  doCrop();
                break;

            case CROP_FROM_CAMERA:
                try {
                    if (outPutFile.exists()) {
                        photo = decodeFile(outPutFile.getAbsolutePath());

                        mImageView.setVisibility(View.VISIBLE);
                        mImageView.setImageBitmap(photo);
                    } else {
                        Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();
        if (size == 0) {
            Toast.makeText(activity, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 768);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 2);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = activity.getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = activity.getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        activity, cropOptions);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                builder.setTitle("Choose Crop App");
                builder.setCancelable(false);
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            activity.getContentResolver().delete(mImageCaptureUri, null,
                                    null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void persentType() {
        final String percentArray[] = {"UPTO", "FLAT"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select Type");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(percentArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                txtChoosePercent.setText(percentArray[item]);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    methodAddImages();
                    //dialog.show();
                    return;
                }else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't add image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
