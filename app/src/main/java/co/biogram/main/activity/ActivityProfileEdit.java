package co.biogram.main.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import com.bumptech.glide.Glide;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class ActivityProfileEdit extends FragmentActivity
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private ImageView ImageViewCover;
    private ImageViewCircle ImageViewCircleProfile;

    private EditText EditTextUsername;
    private EditText EditTextDescription;
    private EditText EditTextLink;
    private EditText EditTextPhone;
    private EditText EditTextLocation;
    private EditText EditTextEmail;
    private String Position = "";

    private Uri CaptureUri;
    private final int FromFile = 1;
    private final int FromCamera = 2;

    private boolean IsCover = false;

    private File FileCover;
    private File FileProfile;

    private PermissionHandler _PermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final Context context = this;

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setFocusableInTouchMode(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        Root.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                finish();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewHeader = new TextView(context);
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewHeader.setText(getString(R.string.ActivityProfileTitle));
        TextViewHeader.setTypeface(null, Typeface.BOLD);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewHeader);

        RelativeLayout.LayoutParams LoadingViewSaveParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LoadingViewSaveParam.addRule(RelativeLayout.CENTER_VERTICAL);
        LoadingViewSaveParam.setMargins(0, 0, MiscHandler.ToDimension(context, 15), 0);

        final LoadingView LoadingViewSave = new LoadingView(context);
        LoadingViewSave.setLayoutParams(LoadingViewSaveParam);
        LoadingViewSave.SetColor(R.color.BlueLight);

        RelativeLayoutHeader.addView(LoadingViewSave);

        RelativeLayout.LayoutParams TextViewSaveParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextViewSaveParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewSaveParam.setMargins(0, 0, MiscHandler.ToDimension(context, 15), 0);

        final TextView TextViewSave = new TextView(context);
        TextViewSave.setLayoutParams(TextViewSaveParam);
        TextViewSave.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewSave.setText(getString(R.string.ActivityProfileSave));
        TextViewSave.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextViewSave.setVisibility(View.GONE);
                LoadingViewSave.Start();

                Map<String, File> UploadFile = new HashMap<>();

                if (FileProfile != null || FileCover != null)
                {
                    if (FileProfile != null)
                        UploadFile.put("Avatar", FileProfile);

                    if (FileCover != null)
                        UploadFile.put("Cover", FileCover);
                }
                else
                    UploadFile = null;

                final ProgressDialog Progress = new ProgressDialog(ActivityProfileEdit.this);
                Progress.setMessage(getString(R.string.ActivityProfileUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setMax(100);
                Progress.setProgress(0);

                if (UploadFile != null)
                    Progress.show();

                AndroidNetworking.upload(MiscHandler.GetRandomServer("ProfileSetEdit"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addMultipartParameter("Username", EditTextUsername.getText().toString())
                .addMultipartParameter("Description", EditTextDescription.getText().toString())
                .addMultipartParameter("Link", EditTextLink.getText().toString())
                .addMultipartParameter("Phone", EditTextPhone.getText().toString())
                .addMultipartParameter("Position", Position)
                .addMultipartParameter("Location", EditTextLocation.getText().toString())
                .addMultipartParameter("Email", EditTextEmail.getText().toString())
                .addMultipartFile(UploadFile)
                .setTag("ActivityProfileEdit")
                .build().setUploadProgressListener(new UploadProgressListener()
                {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes)
                    {
                        Progress.setProgress((int) (100 * bytesUploaded / totalBytes));
                    }
                }).getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        TextViewSave.setVisibility(View.VISIBLE);
                        LoadingViewSave.Stop();
                        Progress.cancel();

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                Intent intent = new Intent(getBaseContext(), ActivityMain.class);
                                intent.putExtra("TAB", 5);

                                startActivity(intent);
                                finish();
                            }

                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                        Progress.cancel();
                    }
                });
            }
        });

        RelativeLayoutHeader.addView(TextViewSave);

        RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewBlankLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewBlankLine = new View(context);
        ViewBlankLine.setLayoutParams(ViewBlankLineParam);
        ViewBlankLine.setBackgroundResource(R.color.Gray2);
        ViewBlankLine.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewBlankLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

        ScrollView ScrollViewMain = new ScrollView(context);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setVerticalScrollBarEnabled(false);
        ScrollViewMain.setHorizontalScrollBarEnabled(false);
        ScrollViewMain.setFillViewport(true);

        Root.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain);

        RelativeLayout RelativeLayoutCover = new RelativeLayout(context);
        RelativeLayoutCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        RelativeLayoutCover.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutCover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogCover = new Dialog(ActivityProfileEdit.this);
                DialogCover.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCover.setCancelable(true);

                LinearLayout Root = new LinearLayout(context);
                Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Root.setBackgroundResource(R.color.White);
                Root.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(context);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitle.setText(getString(R.string.ActivityProfileDialogTitle));
                TextViewTitle.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                TextViewTitle.setTypeface(null, Typeface.BOLD);

                Root.addView(TextViewTitle);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                Root.addView(ViewLine);

                TextView TextViewCamera = new TextView(context);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCamera.setText(getString(R.string.ActivityProfileDialogCamera));
                TextViewCamera.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCamera.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        _PermissionHandler = new PermissionHandler(Manifest.permission.CAMERA, 100, ActivityProfileEdit.this, new PermissionHandler.PermissionEvent()
                        {
                            @Override
                            public void OnGranted()
                            {
                                boolean IsCreated = true;
                                File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Temp/Picture");

                                if (!Root.exists())
                                    IsCreated = Root.mkdirs();

                                if (IsCreated)
                                {
                                    IsCover = true;
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    CaptureUri = Uri.fromFile(new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, CaptureUri);
                                    intent.putExtra("return-data", true);
                                    startActivityForResult(intent, FromCamera);
                                }
                            }

                            @Override
                            public void OnFailed()
                            {
                                MiscHandler.Toast(context, getString(R.string.GeneralPermissionCamera));
                            }
                        });

                        DialogCover.dismiss();
                    }
                });

                Root.addView(TextViewCamera);

                View ViewLine2 = new View(context);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                Root.addView(ViewLine2);

                TextView TextViewGallery = new TextView(context);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewGallery.setText(getString(R.string.ActivityProfileDialogGallery));
                TextViewGallery.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewGallery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        IsCover = true;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileCompleteAction)), FromFile);

                        DialogCover.dismiss();
                    }
                });

                Root.addView(TextViewGallery);

                View ViewLine3 = new View(context);
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                Root.addView(ViewLine3);

                TextView TextViewRemove = new TextView(context);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewRemove.setText(getString(R.string.ActivityProfileDialogRemove));
                TextViewRemove.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileCoverDelete"))
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("ActivityProfileEdit")
                        .build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                ImageViewCover.setImageResource(R.color.BlueLight);
                                MiscHandler.Toast(context, getString(R.string.ActivityProfileImageCover));
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogCover.dismiss();
                    }
                });

                Root.addView(TextViewRemove);

                DialogCover.setContentView(Root);
                DialogCover.show();
            }
        });

        RelativeLayoutMain.addView(RelativeLayoutCover);

        ImageViewCover = new ImageView(context);
        ImageViewCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCover.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayoutCover.addView(ImageViewCover);

        View ViewBlackCover = new View(context);
        ViewBlackCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewBlackCover.setBackgroundResource(R.color.Black);
        ViewBlackCover.setAlpha(0.25f);

        RelativeLayoutCover.addView(ViewBlackCover);

        RelativeLayout.LayoutParams ImageViewCoverAddParam =  new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        ImageViewCoverAddParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewCoverAdd = new ImageView(context);
        ImageViewCoverAdd.setLayoutParams(ImageViewCoverAddParam);
        ImageViewCoverAdd.setAlpha(0.50f);
        ImageViewCoverAdd.setImageResource(R.drawable.ic_camera_white);

        RelativeLayoutCover.addView(ImageViewCoverAdd);

        RelativeLayout.LayoutParams RelativeLayoutProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90));
        RelativeLayoutProfileParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 110), 0, 0);

        RelativeLayout RelativeLayoutProfile = new RelativeLayout(context);
        RelativeLayoutProfile.setLayoutParams(RelativeLayoutProfileParam);
        RelativeLayoutProfile.setId(MiscHandler.GenerateViewID());
        RelativeLayoutProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogProfile = new Dialog(ActivityProfileEdit.this);
                DialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogProfile.setCancelable(true);

                LinearLayout Root = new LinearLayout(context);
                Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Root.setBackgroundResource(R.color.White);
                Root.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(context);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitle.setText(getString(R.string.ActivityProfileDialogTitle));
                TextViewTitle.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                TextViewTitle.setTypeface(null, Typeface.BOLD);

                Root.addView(TextViewTitle);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                Root.addView(ViewLine);

                TextView TextViewCamera = new TextView(context);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCamera.setText(getString(R.string.ActivityProfileDialogCamera));
                TextViewCamera.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCamera.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        _PermissionHandler = new PermissionHandler(Manifest.permission.CAMERA, 100, ActivityProfileEdit.this, new PermissionHandler.PermissionEvent()
                        {
                            @Override
                            public void OnGranted()
                            {
                                boolean IsCreated = true;
                                File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Temp/Picture");

                                if (!Root.exists())
                                    IsCreated = Root.mkdirs();

                                if (IsCreated)
                                {
                                    IsCover = false;
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    CaptureUri = Uri.fromFile(new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, CaptureUri);
                                    intent.putExtra("return-data", true);
                                    startActivityForResult(intent, FromCamera);
                                }
                            }

                            @Override
                            public void OnFailed()
                            {
                                MiscHandler.Toast(context, getString(R.string.GeneralPermissionCamera));
                            }
                        });

                        DialogProfile.dismiss();
                    }
                });

                Root.addView(TextViewCamera);

                View ViewLine2 = new View(context);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                Root.addView(ViewLine2);

                TextView TextViewGallery = new TextView(context);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewGallery.setText(getString(R.string.ActivityProfileDialogGallery));
                TextViewGallery.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewGallery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        IsCover = false;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileCompleteAction)), FromFile);

                        DialogProfile.dismiss();
                    }
                });

                Root.addView(TextViewGallery);

                View ViewLine3 = new View(context);
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                Root.addView(ViewLine3);

                TextView TextViewRemove = new TextView(context);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewRemove.setText(getString(R.string.ActivityProfileDialogRemove));
                TextViewRemove.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileAvatarDelete"))
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("ActivityProfileEdit")
                        .build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                ImageViewCircleProfile.setImageResource(R.color.BlueLight);
                                MiscHandler.Toast(context, getString(R.string.ActivityProfileImageCover));
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogProfile.dismiss();
                    }
                });

                Root.addView(TextViewRemove);

                DialogProfile.setContentView(Root);
                DialogProfile.show();
            }
        });

        RelativeLayoutMain.addView(RelativeLayoutProfile);

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCircleProfile.SetBorderColor(R.color.White);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setImageResource(R.color.BlueLight);

        RelativeLayoutProfile.addView(ImageViewCircleProfile);

        GradientDrawable ShapeViewProfile = new GradientDrawable();
        ShapeViewProfile.setShape(GradientDrawable.OVAL);
        ShapeViewProfile.setCornerRadius(MiscHandler.ToDimension(context, 15));
        ShapeViewProfile.setColor(Color.BLACK);
        ShapeViewProfile.setStroke(MiscHandler.ToDimension(context, 3), Color.WHITE);

        View ViewBlackProfile = new View(context);
        ViewBlackProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewBlackProfile.setBackground(ShapeViewProfile);
        ViewBlackProfile.setAlpha(0.25f);

        RelativeLayoutProfile.addView(ViewBlackProfile);

        RelativeLayout.LayoutParams ImageViewProfileAddParam =  new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 35), MiscHandler.ToDimension(context, 35));
        ImageViewProfileAddParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewProfileAdd = new ImageView(context);
        ImageViewProfileAdd.setLayoutParams(ImageViewProfileAddParam);
        ImageViewProfileAdd.setAlpha(0.50f);
        ImageViewProfileAdd.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewProfileAdd.setImageResource(R.drawable.ic_camera_white);

        RelativeLayoutProfile.addView(ImageViewProfileAdd);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.BELOW, RelativeLayoutProfile.getId());
        TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewUsername = new TextView(context);
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewUsername.setText(getString(R.string.ActivityProfileUsername));
        TextViewUsername.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewUsername);

        RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
        EditTextUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextUsername = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextUsername.setLayoutParams(EditTextUsernameParam);
        EditTextUsername.setMaxLines(1);
        EditTextUsername.setId(MiscHandler.GenerateViewID());
        EditTextUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextUsername.setHint(R.string.ActivityProfileUsernameHint);
        EditTextUsername.setFilters(new InputFilter[]
        {
            new InputFilter.LengthFilter(32), new InputFilter()
            {
                @Override
                public CharSequence filter(CharSequence Source, int Start, int End, Spanned Dest, int DestStart, int DestEnd)
                {
                    if (End > Start)
                    {
                        char[] AcceptedChars = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                        for (int I = Start; I < End; I++)
                        {
                            if (!new String(AcceptedChars).contains(String.valueOf(Source.charAt(I))))
                            {
                                return "";
                            }
                        }
                    }

                    return null;
                }
            }
        });
        EditTextUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        RelativeLayoutMain.addView(EditTextUsername);

        RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());
        TextViewDescriptionParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewDescription = new TextView(context);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setText(getString(R.string.ActivityProfileDescription));
        TextViewDescription.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());
        EditTextDescriptionParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextDescription = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextDescription.setLayoutParams(EditTextDescriptionParam);
        EditTextDescription.setMaxLines(5);
        EditTextDescription.setId(MiscHandler.GenerateViewID());
        EditTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        RelativeLayoutMain.addView(EditTextDescription);

        RelativeLayout.LayoutParams TextViewLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLinkParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());
        TextViewLinkParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewLink = new TextView(context);
        TextViewLink.setLayoutParams(TextViewLinkParam);
        TextViewLink.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLink.setText(getString(R.string.ActivityProfileWebsite));
        TextViewLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewLink);

        RelativeLayout.LayoutParams EditTextLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextLinkParam.addRule(RelativeLayout.BELOW, TextViewLink.getId());
        EditTextLinkParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextLink = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextLink.setLayoutParams(EditTextLinkParam);
        EditTextLink.setMaxLines(1);
        EditTextLink.setId(MiscHandler.GenerateViewID());
        EditTextLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextLink.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextLink.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        RelativeLayoutMain.addView(EditTextLink);

        RelativeLayout.LayoutParams TextViewPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPhoneParam.addRule(RelativeLayout.BELOW, EditTextLink.getId());
        TextViewPhoneParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewPhone = new TextView(context);
        TextViewPhone.setLayoutParams(TextViewPhoneParam);
        TextViewPhone.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPhone.setText(getString(R.string.ActivityProfilePhone));
        TextViewPhone.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewPhone);

        RelativeLayout.LayoutParams EditTextPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextPhoneParam.addRule(RelativeLayout.BELOW, TextViewPhone.getId());
        EditTextPhoneParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextPhone = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextPhone.setLayoutParams(EditTextPhoneParam);
        EditTextPhone.setMaxLines(1);
        EditTextPhone.setId(MiscHandler.GenerateViewID());
        EditTextPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
        EditTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);

        RelativeLayoutMain.addView(EditTextPhone);

        RelativeLayout.LayoutParams TextViewLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLocationParam.addRule(RelativeLayout.BELOW, EditTextPhone.getId());
        TextViewLocationParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewLocation = new TextView(context);
        TextViewLocation.setLayoutParams(TextViewLocationParam);
        TextViewLocation.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLocation.setText(getString(R.string.ActivityProfileLocation));
        TextViewLocation.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewLocation);

        RelativeLayout.LayoutParams EditTextLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextLocationParam.addRule(RelativeLayout.BELOW, TextViewLocation.getId());
        EditTextLocationParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextLocation = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextLocation.setLayoutParams(EditTextLocationParam);
        EditTextLocation.setMaxLines(1);
        EditTextLocation.setId(MiscHandler.GenerateViewID());
        EditTextLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextLocation.setFocusable(false);
        EditTextLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (getCurrentFocus() != null)
                    getCurrentFocus().clearFocus();

                getFragmentManager().beginTransaction().add(R.id.ActivityProfileContainer, new FragmentMap()).addToBackStack("FragmentMap").commit();
            }
        });

        RelativeLayoutMain.addView(EditTextLocation);

        RelativeLayout.LayoutParams TextViewEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailParam.addRule(RelativeLayout.BELOW, EditTextLocation.getId());
        TextViewEmailParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewEmail = new TextView(context);
        TextViewEmail.setLayoutParams(TextViewEmailParam);
        TextViewEmail.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewEmail.setText(getString(R.string.ActivityProfileEmail));
        TextViewEmail.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewEmail);

        RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewEmail.getId());
        EditTextEmailParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextEmail = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextEmail.setLayoutParams(EditTextEmailParam);
        EditTextEmail.setMaxLines(1);
        EditTextEmail.setHint(R.string.ActivityProfileEmailHint);
        EditTextEmail.setId(MiscHandler.GenerateViewID());
        EditTextEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        RelativeLayoutMain.addView(EditTextEmail);

        FrameLayout FrameLayoutTab = new FrameLayout(context);
        FrameLayoutTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutTab.setId(R.id.ActivityProfileContainer);

        Root.addView(FrameLayoutTab);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewData = new LoadingView(context);
        LoadingViewData.setLayoutParams(LoadingViewDataParam);

        RelativeLayoutLoading.addView(LoadingViewData);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTry = new TextView(context);
        TextViewTry.setLayoutParams(TextViewTryParam);
        TextViewTry.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTry.setText(getString(R.string.GeneralTryAgain));
        TextViewTry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(); } });

        RelativeLayoutLoading.addView(TextViewTry);
        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);

        RetrieveDataFromServer();

        setContentView(Root);
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        if (_PermissionHandler != null)
            _PermissionHandler.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        AndroidNetworking.forceCancel("ActivityProfileEdit");
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (ResultCode != RESULT_OK)
            return;

        switch (RequestCode)
        {
            case FromFile:
                CaptureUri = Data.getData();
                DoCrop();
                break;
            case FromCamera:
                _PermissionHandler = new PermissionHandler(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101, ActivityProfileEdit.this, new PermissionHandler.PermissionEvent()
                {
                    @Override
                    public void OnGranted()
                    {
                        ContentResolver _ContentResolver = getContentResolver();
                        ContentValues _ContentValues = new ContentValues();

                        _ContentValues.put(MediaStore.Images.Media.DATA, CaptureUri.getPath());
                        _ContentValues.put(MediaStore.Images.Media.IS_PRIVATE, 0);
                        _ContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, _ContentValues);

                        String[] SelectionArgs = { CaptureUri.getPath() };

                        Cursor _Cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " = ?", SelectionArgs, null);

                        if (_Cursor != null)
                        {
                            _Cursor.moveToFirst();
                            CaptureUri = Uri.parse("content://media/external/images/media/" + _Cursor.getInt(_Cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                            _Cursor.close();
                        }

                        DoCrop();
                    }

                    @Override
                    public void OnFailed()
                    {
                        MiscHandler.Toast(ActivityProfileEdit.this, getString(R.string.GeneralPermissionStorage));
                    }
                });
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                Uri ResultURI = CropImage.getActivityResult(Data).getUri();

                if (IsCover)
                {
                    try
                    {
                        boolean IsCreated = true;
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Temp/Picture");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (IsCreated)
                        {
                            FileCover = new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            FileOutputStream FileStream = new FileOutputStream(FileCover);
                            ByteArrayOutputStream ImageByteArray = new ByteArrayOutputStream();
                            MediaStore.Images.Media.getBitmap(getContentResolver(), ResultURI).compress(Bitmap.CompressFormat.JPEG, 70, ImageByteArray);
                            ImageByteArray.writeTo(FileStream);
                            FileStream.close();

                            ImageViewCover.setImageURI(ResultURI);
                        }
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
                else
                {
                    try
                    {
                        boolean IsCreated = true;
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram/Temp/Picture");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (IsCreated)
                        {
                            FileProfile = new File(Root, ("BIO_IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            FileOutputStream FileStream = new FileOutputStream(FileProfile);
                            ByteArrayOutputStream ImageByteArray = new ByteArrayOutputStream();
                            MediaStore.Images.Media.getBitmap(getContentResolver(), ResultURI).compress(Bitmap.CompressFormat.JPEG, 70, ImageByteArray);
                            ImageByteArray.writeTo(FileStream);
                            FileStream.close();

                            ImageViewCircleProfile.setImageURI(ResultURI);
                        }
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
                break;
        }
    }

    public static class FragmentMap extends Fragment
    {
        private TextView TextViewName;
        private TextView TextViewPosition;

        private MapView _MapView;
        private GoogleMap _GoogleMap;
        private MapThreadClass MapThread;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setImageResource(R.drawable.ic_back_blue);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().onBackPressed(); } });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewHeader.setText(getString(R.string.ActivityProfileLocation));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewSearch = new ImageView(context);
            ImageViewSearch.setLayoutParams(ImageViewSearchParam);
            ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
            ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
            ImageViewSearch.setId(MiscHandler.GenerateViewID());
            ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getFragmentManager().beginTransaction().add(R.id.ActivityProfileContainer, new FragmentSearch()).addToBackStack("FragmentSearch").commit(); } });

            RelativeLayoutHeader.addView(ImageViewSearch);

            RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewBlankLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewBlankLine = new View(context);
            ViewBlankLine.setLayoutParams(ViewBlankLineParam);
            ViewBlankLine.setBackgroundResource(R.color.Gray2);
            ViewBlankLine.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
            RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
            RelativeLayoutBottom.setBackgroundResource(R.color.White5);
            RelativeLayoutBottom.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutBottom);

            ImageView ImageViewSend = new ImageView(context);
            ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewSend.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));
            ImageViewSend.setImageResource(R.drawable.ic_location_blue);
            ImageViewSend.setId(MiscHandler.GenerateViewID());
            ImageViewSend.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ActivityProfileEdit Parent = (ActivityProfileEdit) getActivity();

                    if (_GoogleMap != null)
                    {
                        double Lat = _GoogleMap.getCameraPosition().target.latitude;
                        double Lon = _GoogleMap.getCameraPosition().target.longitude;

                        String Name = TextViewName.getText().toString();

                        if (Name.length() > 25)
                            Name = Name.substring(0, 25) + " ...";

                        Parent.Position = (float) Lat + ":" + (float) Lon;
                        Parent.EditTextLocation.setText(Name);
                    }

                    Parent.onBackPressed();
                }
            });

            RelativeLayoutBottom.addView(ImageViewSend);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewNameParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));

            TextViewName = new TextView(context);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewName.setId(MiscHandler.GenerateViewID());

            RelativeLayoutBottom.addView(TextViewName);

            RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
            TextViewPositionParam.setMargins(MiscHandler.ToDimension(context, 5),0, 0, 0);

            TextViewPosition = new TextView(context);
            TextViewPosition.setLayoutParams(TextViewPositionParam);
            TextViewPosition.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            RelativeLayoutBottom.addView(TextViewPosition);

            RelativeLayout.LayoutParams ViewBlankLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewBlankLine2Param.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());

            View ViewBlankLine2 = new View(context);
            ViewBlankLine2.setLayoutParams(ViewBlankLine2Param);
            ViewBlankLine2.setBackgroundResource(R.color.Gray2);
            ViewBlankLine2.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine2);

            RelativeLayout.LayoutParams ViewCenterParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            ViewCenterParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            View ViewCenter = new View(context);
            ViewCenter.setLayoutParams(ViewCenterParam);
            ViewCenter.setBackgroundResource(R.color.Gray2);
            ViewCenter.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewCenter);

            RelativeLayout.LayoutParams ImageViewPinParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 42));
            ImageViewPinParam.addRule(RelativeLayout.ABOVE, ViewCenter.getId());
            ImageViewPinParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            ImageViewPinParam.setMargins(0, MiscHandler.ToDimension(context, 21), 0, 0);

            ImageView ImageViewPin = new ImageView(context);
            ImageViewPin.setLayoutParams(ImageViewPinParam);
            ImageViewPin.setImageResource(R.drawable.ic_location_pin);

            Root.addView(ImageViewPin);

            _MapView = new MapView(context)
            {
                @Override
                public boolean onInterceptTouchEvent(MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        if (_GoogleMap != null)
                        {
                            double Lat = _GoogleMap.getCameraPosition().target.latitude;
                            double Lon = _GoogleMap.getCameraPosition().target.longitude;

                            TextViewPosition.setText("(" + (float) Lat + " , " + (float) Lon + ")");
                            SetLocationName(Lat, Lon);
                        }
                    }

                    return super.onInterceptTouchEvent(m);
                }
            };
            _MapView.onCreate(savedInstanceState);
            _MapView.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    _MapView.onResume();
                    _GoogleMap = googleMap;
                    _GoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    _GoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    _GoogleMap.getUiSettings().setCompassEnabled(true);

                    double Lat = 0, Lon = 0;
                    String[] Position = ((ActivityProfileEdit) getActivity()).Position.split(":");

                    if (Position.length > 1 && !Position[0].equals("") && !Position[1].equals(""))
                    {
                        Lat = Double.parseDouble(Position[0]);
                        Lon = Double.parseDouble(Position[1]);

                        CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Lon), _GoogleMap.getMaxZoomLevel() - 15);
                        _GoogleMap.moveCamera(MyPosition);
                        _GoogleMap.animateCamera(MyPosition);
                    }
                    else if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        _GoogleMap.setMyLocationEnabled(true);

                        LocationManager Manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        Location MyLocation = Manager.getLastKnownLocation(Manager.getBestProvider(new Criteria(), true));

                        if (MyLocation != null)
                        {
                            Lat = MyLocation.getLatitude();
                            Lon = MyLocation.getLongitude();

                            CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Lon), _GoogleMap.getMaxZoomLevel() - 15);
                            _GoogleMap.moveCamera(MyPosition);
                            _GoogleMap.animateCamera(MyPosition);
                        }
                    }

                    String Name = "Unknown Place";
                    TextViewName.setText(Name);
                    TextViewPosition.setText("(" + (float) Lat + " , " + (float) Lon + ")");
                    SetLocationName(Lat, Lon);
                }
            });

            RelativeLayout.LayoutParams MapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            MapParams.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());
            MapParams.addRule(RelativeLayout.ABOVE, ViewBlankLine2.getId());
            _MapView.setLayoutParams(MapParams);

            Root.addView(_MapView);
            ImageViewPin.bringToFront();

            return Root;
        }

        @Override
        public void onStop()
        {
            super.onStop();
            MapThread = null;
        }

        private void SetLocationName(double Lat, double Lon)
        {
            if (MapThread == null)
            {
                MapThread = new MapThreadClass();
                MapThread.start();
            }

            MapThread.FindLocationName(Lat, Lon);
        }

        private class MapThreadClass extends Thread
        {
            private double Latitude, Longitude;
            private Handler MapHandler;
            private final Runnable MapRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String LocationName = "";
                        Geocoder geocoder = new Geocoder(getActivity());
                        List<Address> Address = geocoder.getFromLocation(Latitude, Longitude, 1);

                        if (Address.size() > 0)
                        {
                            String AddressLine = Address.get(0).getAddressLine(0);
                            String City = Address.get(0).getLocality();
                            String KnownName = Address.get(0).getFeatureName();

                            if (City != null && !City.equals(""))
                                LocationName += City;
                            else if (AddressLine != null && !AddressLine.equals(""))
                                LocationName += AddressLine;

                            if (KnownName != null && !KnownName.equals(""))
                                LocationName += " - " + KnownName;

                            if (LocationName.length() > 32)
                                LocationName = LocationName.substring(0, 32) + " ...";
                        }

                        if (LocationName.equals(""))
                            LocationName = "Unknown Place";

                        final String LocationName2 = LocationName;

                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                TextViewName.setText(LocationName2);
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
            };

            void FindLocationName(double latitude, double longitude)
            {
                Latitude = latitude;
                Longitude = longitude;

                if (MapHandler != null)
                {
                    MapHandler.removeCallbacks(MapRunnable);
                    MapHandler.post(MapRunnable);
                }
            }

            @Override
            public void run()
            {
                Looper.prepare();

                if (MapHandler == null)
                    MapHandler = new Handler();

                MapHandler.post(MapRunnable);

                Looper.loop();
            }
        }
    }

    public static class FragmentSearch extends Fragment
    {
        private EditText EditTextSearch;
        private SearchAdapter _SearchAdapter;
        private final List<SearchStruct> SearchList = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setImageResource(R.drawable.ic_back_blue);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { MiscHandler.HideSoftKey(getActivity()); getActivity().onBackPressed(); } });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewSearch = new ImageView(context);
            ImageViewSearch.setLayoutParams(ImageViewSearchParam);
            ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
            ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
            ImageViewSearch.setId(MiscHandler.GenerateViewID());
            ImageViewSearch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String URL = "https://api.foursquare.com/v2/venues/search/?v=20170101&locale=en&limit=25&client_id=YXWKVYF1XFBW3XRC4I0EIVALENCVNVEX5ARMQA4TP5ZUAB41&client_secret=QX2MNDH4GGFYSQCLFE1NVXDFJBNTNWKT3IDX0OUJD54TNKJ2&near=" + EditTextSearch.getText().toString();

                    AndroidNetworking.get(URL).setTag("FragmentSearch").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                SearchList.clear();

                                JSONArray Result = new JSONObject(Response).getJSONObject("response").getJSONArray("venues");

                                for (int A = 0; A < Result.length(); A++)
                                {
                                    try
                                    {
                                        JSONObject object = Result.getJSONObject(A);
                                        JSONObject location = object.getJSONObject("location");

                                        String Name;
                                        double Lat = location.getDouble("lat");
                                        double Lon = location.getDouble("lng");

                                        if (object.has("name"))
                                            Name = object.getString("name");
                                        else if (location.has("address"))
                                            Name = location.getString("address");
                                        else if (location.has("city"))
                                            Name = location.getString("city");
                                        else if (location.has("state"))
                                            Name = location.getString("state");
                                        else if (location.has("country"))
                                            Name = location.getString("country");
                                        else
                                            continue;

                                        SearchList.add(new SearchStruct(Name, Lat, Lon));
                                    }
                                    catch (Exception e)
                                    {
                                        // Leave Me Alone
                                    }
                                }

                                _SearchAdapter.notifyDataSetChanged();
                                MiscHandler.HideSoftKey(getActivity());
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError) { }
                    });
                }
            });

            RelativeLayoutHeader.addView(ImageViewSearch);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.LEFT_OF, ImageViewSearch.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            EditTextSearch = new EditText(context);
            EditTextSearch.setLayoutParams(TextViewHeaderParam);
            EditTextSearch.setBackgroundResource(R.color.White5);
            EditTextSearch.setTextColor(ContextCompat.getColor(context, R.color.Black));
            EditTextSearch.setHint("Search");
            EditTextSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextSearch.requestFocus();

            RelativeLayoutHeader.addView(EditTextSearch);

            RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewBlankLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewBlankLine = new View(context);
            ViewBlankLine.setLayoutParams(ViewBlankLineParam);
            ViewBlankLine.setBackgroundResource(R.color.Gray2);
            ViewBlankLine.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine);

            RelativeLayout.LayoutParams ListViewSearchParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ListViewSearchParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

            _SearchAdapter = new SearchAdapter(context, SearchList);

            ListView ListViewSearch = new ListView(context);
            ListViewSearch.setLayoutParams(ListViewSearchParam);
            ListViewSearch.setAdapter(_SearchAdapter);
            ListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                {
                    SearchStruct Search = SearchList.get(position);

                    if (Search == null)
                        return;

                    String Name = Search.Name;

                    if (Name.length() > 25)
                        Name = Name.substring(0, 25) + " ...";

                    ActivityProfileEdit Parent = (ActivityProfileEdit) getActivity();
                    Parent.Position = (float) Search.Latitude + ":" + (float) Search.Longitude;
                    Parent.EditTextLocation.setText(Name);
                    Parent.onBackPressed();
                }
            });

            Root.addView(ListViewSearch);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return Root;
        }

        private class SearchStruct
        {
            final String Name;
            final double Latitude;
            final double Longitude;

            SearchStruct(String name, double latitude, double longitude)
            {
                Name = name;
                Latitude = latitude;
                Longitude = longitude;
            }
        }

        private class SearchAdapter extends ArrayAdapter<SearchStruct>
        {
            final Context context;

            SearchAdapter(Context c, List<SearchStruct> SearchList)
            {
                super(c, -1, SearchList);
                context = c;
            }

            @Override
            public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
            {
                SearchStruct Search = SearchList.get(position);

                RelativeLayout MainView = new RelativeLayout(context);
                MainView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));

                if (Search == null)
                    return MainView;

                ImageView ImageViewIcon = new ImageView(context);
                ImageViewIcon.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
                ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
                ImageViewIcon.setImageResource(R.drawable.ic_location_blue);
                ImageViewIcon.setId(MiscHandler.GenerateViewID());

                MainView.addView(ImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewNameParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

                TextView TextViewName = new TextView(context);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setText(Search.Name);
                TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewName.setId(MiscHandler.GenerateViewID());

                MainView.addView(TextViewName);

                RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
                TextViewPositionParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

                TextView TextViewPosition = new TextView(context);
                TextViewPosition.setLayoutParams(TextViewPositionParam);
                TextViewPosition.setText("(" + (float) Search.Latitude + " , " + (float) Search.Longitude + ")");

                MainView.addView(TextViewPosition);

                return MainView;
            }
        }
    }

    private void RetrieveDataFromServer()
    {
        final Context context = this;
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileGetEdit"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("ActivityProfileEdit")
        .build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                RelativeLayoutLoading.setVisibility(View.GONE);
                TextViewTry.setVisibility(View.GONE);
                LoadingViewData.Stop();

                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONObject Data = new JSONObject(Result.getString("Result"));

                        EditTextUsername.setText(Data.getString("Username"));
                        EditTextDescription.setText(Data.getString("Description"));
                        EditTextLink.setText(Data.getString("Link"));
                        EditTextLocation.setText(Data.getString("Location"));
                        EditTextEmail.setText(Data.getString("Email"));
                        Position = Data.getString("Position");

                        Glide.with(context).load(Data.getString("Cover")).dontAnimate().into(ImageViewCover);
                        Glide.with(context).load(Data.getString("Avatar")).override(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90)).dontAnimate().into(ImageViewCircleProfile);
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }
            }

            @Override
            public void onError(ANError anError)
            {
                TextViewTry.setVisibility(View.VISIBLE);
                LoadingViewData.Stop();
            }
        });
    }

    private void DoCrop()
    {
        Context context = this;

        if (IsCover)
        {
            try
            {
                String URL = null;
                String[] Pro = { MediaStore.Images.Media.DATA };
                Cursor _Cursor = getContentResolver().query(CaptureUri, Pro, null, null, null);

                if (_Cursor != null && _Cursor.moveToFirst())
                {
                    URL = _Cursor.getString(_Cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    _Cursor.close();
                }

                if (URL == null)
                    return;

                File IMG = new File(URL);

                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(IMG), null, o);

                int Scale = 1;

                while ((o.outWidth / Scale / 2) >= (int) (MiscHandler.ToDimension(context, 160) * 2.45f) && (o.outHeight / Scale / 2) >= MiscHandler.ToDimension(context, 160))
                {
                    Scale *= 2;
                }

                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = Scale;
                Bitmap ResizeBitmap = BitmapFactory.decodeStream(new FileInputStream(IMG), null, o2);

                Matrix matrix = new Matrix();
                int Orientation = new ExifInterface(URL).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                if (Orientation == 6)
                    matrix.postRotate(90);
                else if (Orientation == 3)
                    matrix.postRotate(180);
                else if (Orientation == 8)
                    matrix.postRotate(270);

                ResizeBitmap = Bitmap.createBitmap(ResizeBitmap, 0, 0, ResizeBitmap.getWidth(), ResizeBitmap.getHeight(), matrix, true);
                String ResizeBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), ResizeBitmap, "BioGram Crop Image", null);

                CropImage.activity(Uri.parse(ResizeBitmapPath))
                .setAllowRotation(true)
                .setActivityTitle(getString(R.string.ActivityProfileCrop))
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(2, 1)
                .setFixAspectRatio(true)
                .setRequestedSize((int)(MiscHandler.ToDimension(context, 160) * 2.45f), MiscHandler.ToDimension(context, 160), CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .start(this);
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }

            return;
        }

        CropImage.activity(CaptureUri).setAllowRotation(true).setActivityTitle(getString(R.string.ActivityProfileCrop)).setGuidelines(CropImageView.Guidelines.ON_TOUCH).setFixAspectRatio(true).start(this);
    }
}
