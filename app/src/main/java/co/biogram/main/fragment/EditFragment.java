package co.biogram.main.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

import static android.content.Context.LOCATION_SERVICE;

public class EditFragment extends Fragment
{
    private PermissionHandler PermissionHandler;

    private Uri CaptureUri;
    private final int FromCamera = 2;

    private File CoverFile;
    private File ProfileFile;
    private boolean IsCover = false;

    private ImageView ImageViewCover;
    private ImageViewCircle ImageViewCircleProfile;

    private EditText EditTextUsername;
    private EditText EditTextDescription;
    private EditText EditTextLink;
    private EditText EditTextPhone;
    private EditText EditTextLocation;
    private EditText EditTextEmail;
    private String Position = "";

    private RelativeLayout RelativeLayoutCrop;
    private CropImageView CropImageViewMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setFocusableInTouchMode(true);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { MiscHandler.HideSoftKey(getActivity()); getActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.EditFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams LoadingViewSaveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
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
        TextViewSave.setText(getString(R.string.EditFragmentSave));
        TextViewSave.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextViewSave.setVisibility(View.GONE);
                LoadingViewSave.Start();

                Map<String, File> UploadFile = new HashMap<>();

                if (ProfileFile != null || CoverFile != null)
                {
                    if (ProfileFile != null)
                        UploadFile.put("Avatar", ProfileFile);

                    if (CoverFile != null)
                        UploadFile.put("Cover", CoverFile);
                }
                else
                    UploadFile = null;

                final ProgressDialog Progress = new ProgressDialog(getActivity());
                Progress.setMessage(getString(R.string.EditFragmentUpload));
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
                .setTag("EditFragment")
                .build()
                .setUploadProgressListener(new UploadProgressListener()
                {
                    @Override
                    public void onProgress(long Uploaded, long Total)
                    {
                        Progress.setProgress((int) (100 * Uploaded / Total));
                    }
                })
                .getAsString(new StringRequestListener()
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
                                SharedHandler.SetString(context, "Username", EditTextUsername.getText().toString());

                                MiscHandler.HideSoftKey(getActivity());
                                getActivity().onBackPressed();
                                return;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("EditFragment-Save: " + e.toString());
                        }

                        MiscHandler.Toast(context, getString(R.string.EditFragmentWrong));
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        Progress.cancel();
                        LoadingViewSave.Stop();
                        TextViewSave.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        RelativeLayoutHeader.addView(TextViewSave);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(context);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);
        ScrollViewMain.setClickable(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
        RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain2);

        RelativeLayout RelativeLayoutCover = new RelativeLayout(context);
        RelativeLayoutCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        RelativeLayoutCover.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutCover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogCover = new Dialog(getActivity());
                DialogCover.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCover.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(context);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(context);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitle.setText(getString(R.string.EditFragmentDialogTitle));
                TextViewTitle.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                TextViewTitle.setTypeface(null, Typeface.BOLD);

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewCamera = new TextView(context);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCamera.setText(getString(R.string.EditFragmentDialogCamera));
                TextViewCamera.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCamera.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PermissionHandler = new PermissionHandler(Manifest.permission.CAMERA, 100, EditFragment.this, new PermissionHandler.PermissionEvent()
                        {
                            @Override
                            public void OnGranted()
                            {
                                File CacheFolder = new File(context.getCacheDir(), "BioGram");

                                if (CacheFolder.exists() || CacheFolder.mkdir())
                                {
                                    IsCover = true;
                                    CaptureUri = Uri.fromFile(new File(CacheFolder, ("COVER." + String.valueOf(System.currentTimeMillis()) + ".jpg")));

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, CaptureUri);
                                    intent.putExtra("return-data", true);
                                    startActivityForResult(intent, FromCamera);
                                }
                            }

                            @Override
                            public void OnFailed()
                            {
                                MiscHandler.Toast(context, getString(R.string.PermissionCamera));
                            }
                        });

                        DialogCover.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewCamera);

                View ViewLine2 = new View(context);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewGallery = new TextView(context);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewGallery.setText(getString(R.string.EditFragmentDialogGallery));
                TextViewGallery.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewGallery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, EditFragment.this, new PermissionHandler.PermissionEvent()
                        {
                            @Override
                            public void OnGranted()
                            {
                                IsCover = true;
                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, GalleryFragment.NewInstance(1, false)).addToBackStack("GalleryFragment").commitAllowingStateLoss();
                                DialogCover.dismiss();
                            }

                            @Override
                            public void OnFailed()
                            {
                                MiscHandler.Toast(context, getString(R.string.PermissionStorage));
                                DialogCover.dismiss();
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewGallery);

                View ViewLine3 = new View(context);
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine3);

                TextView TextViewRemove = new TextView(context);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewRemove.setText(getString(R.string.EditFragmentDialogRemove));
                TextViewRemove.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileCoverDelete"))
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("EditFragment")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                ImageViewCover.setImageResource(R.color.BlueLight);
                                MiscHandler.Toast(context, getString(R.string.EditFragmentImageCover));
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogCover.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewRemove);

                DialogCover.setContentView(LinearLayoutMain);
                DialogCover.show();
            }
        });

        RelativeLayoutMain2.addView(RelativeLayoutCover);

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

        GradientDrawable ShapeViewProfile = new GradientDrawable();
        ShapeViewProfile.setShape(GradientDrawable.OVAL);
        ShapeViewProfile.setCornerRadius(MiscHandler.ToDimension(context, 15));
        ShapeViewProfile.setColor(0xFFFFFFFF);
        ShapeViewProfile.setStroke(MiscHandler.ToDimension(context, 3), 0xFFFFFFFF);

        RelativeLayout RelativeLayoutProfile = new RelativeLayout(context);
        RelativeLayoutProfile.setLayoutParams(RelativeLayoutProfileParam);
        RelativeLayoutProfile.setId(MiscHandler.GenerateViewID());
        RelativeLayoutProfile.setBackground(ShapeViewProfile);
        RelativeLayoutProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogProfile = new Dialog(getActivity());
                DialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogProfile.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(context);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(context);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitle.setText(getString(R.string.EditFragmentDialogTitle));
                TextViewTitle.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                TextViewTitle.setTypeface(null, Typeface.BOLD);

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewCamera = new TextView(context);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCamera.setText(getString(R.string.EditFragmentDialogCamera));
                TextViewCamera.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCamera.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PermissionHandler = new PermissionHandler(Manifest.permission.CAMERA, 100, EditFragment.this, new PermissionHandler.PermissionEvent()
                        {
                            @Override
                            public void OnGranted()
                            {
                                File CacheFolder = new File(context.getCacheDir(), "BioGram");

                                if (CacheFolder.exists() || CacheFolder.mkdir())
                                {
                                    IsCover = false;
                                    CaptureUri = Uri.fromFile(new File(CacheFolder, ("PROFILE." + String.valueOf(System.currentTimeMillis()) + ".jpg")));

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, CaptureUri);
                                    intent.putExtra("return-data", true);
                                    startActivityForResult(intent, FromCamera);
                                }
                            }

                            @Override
                            public void OnFailed()
                            {
                                MiscHandler.Toast(context, getString(R.string.PermissionCamera));
                            }
                        });

                        DialogProfile.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewCamera);

                View ViewLine2 = new View(context);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewGallery = new TextView(context);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewGallery.setText(getString(R.string.EditFragmentDialogGallery));
                TextViewGallery.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewGallery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, EditFragment.this, new PermissionHandler.PermissionEvent()
                        {
                            @Override
                            public void OnGranted()
                            {
                                IsCover = false;
                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, GalleryFragment.NewInstance(1, false)).addToBackStack("GalleryFragment").commitAllowingStateLoss();
                                DialogProfile.dismiss();
                            }

                            @Override
                            public void OnFailed()
                            {
                                MiscHandler.Toast(context, getString(R.string.PermissionStorage));
                                DialogProfile.dismiss();
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewGallery);

                View ViewLine3 = new View(context);
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine3);

                TextView TextViewRemove = new TextView(context);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewRemove.setText(getString(R.string.EditFragmentDialogRemove));
                TextViewRemove.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileAvatarDelete"))
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("EditFragment")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                ImageViewCircleProfile.setImageResource(R.color.BlueLight);
                                MiscHandler.Toast(context, getString(R.string.EditFragmentImageProfile));
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogProfile.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewRemove);

                DialogProfile.setContentView(LinearLayoutMain);
                DialogProfile.show();
            }
        });

        RelativeLayoutMain2.addView(RelativeLayoutProfile);

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCircleProfile.SetBorderColor(R.color.White);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setImageResource(R.color.BlueLight);

        RelativeLayoutProfile.addView(ImageViewCircleProfile);

        GradientDrawable ShapeViewProfile2 = new GradientDrawable();
        ShapeViewProfile2.setShape(GradientDrawable.OVAL);
        ShapeViewProfile2.setCornerRadius(MiscHandler.ToDimension(context, 15));
        ShapeViewProfile2.setColor(0xFF000000);
        ShapeViewProfile2.setStroke(MiscHandler.ToDimension(context, 3), 0xFFFFFFFF);

        View ViewBlackProfile = new View(context);
        ViewBlackProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewBlackProfile.setBackground(ShapeViewProfile2);
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
        TextViewUsername.setText(getString(R.string.EditFragmentUsername));
        TextViewUsername.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewUsername);

        RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
        EditTextUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
        EditTextUsername.setLayoutParams(EditTextUsernameParam);
        EditTextUsername.setMaxLines(1);
        EditTextUsername.setId(MiscHandler.GenerateViewID());
        EditTextUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextUsername.setHint(R.string.EditFragmentUsernameHint);
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

        RelativeLayoutMain2.addView(EditTextUsername);

        RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());
        TextViewDescriptionParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewDescription = new TextView(context);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setText(getString(R.string.EditFragmentDescription));
        TextViewDescription.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());
        EditTextDescriptionParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextDescription = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
        EditTextDescription.setLayoutParams(EditTextDescriptionParam);
        EditTextDescription.setMaxLines(5);
        EditTextDescription.setId(MiscHandler.GenerateViewID());
        EditTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        RelativeLayoutMain2.addView(EditTextDescription);

        RelativeLayout.LayoutParams TextViewLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLinkParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());
        TextViewLinkParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewLink = new TextView(context);
        TextViewLink.setLayoutParams(TextViewLinkParam);
        TextViewLink.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLink.setText(getString(R.string.EditFragmentWebsite));
        TextViewLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewLink);

        RelativeLayout.LayoutParams EditTextLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextLinkParam.addRule(RelativeLayout.BELOW, TextViewLink.getId());
        EditTextLinkParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextLink = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
        EditTextLink.setLayoutParams(EditTextLinkParam);
        EditTextLink.setMaxLines(1);
        EditTextLink.setId(MiscHandler.GenerateViewID());
        EditTextLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextLink.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextLink.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        RelativeLayoutMain2.addView(EditTextLink);

        RelativeLayout.LayoutParams TextViewPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPhoneParam.addRule(RelativeLayout.BELOW, EditTextLink.getId());
        TextViewPhoneParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewPhone = new TextView(context);
        TextViewPhone.setLayoutParams(TextViewPhoneParam);
        TextViewPhone.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPhone.setText(getString(R.string.EditFragmentPhone));
        TextViewPhone.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewPhone);

        RelativeLayout.LayoutParams EditTextPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextPhoneParam.addRule(RelativeLayout.BELOW, TextViewPhone.getId());
        EditTextPhoneParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextPhone = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
        EditTextPhone.setLayoutParams(EditTextPhoneParam);
        EditTextPhone.setMaxLines(1);
        EditTextPhone.setId(MiscHandler.GenerateViewID());
        EditTextPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
        EditTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);

        RelativeLayoutMain2.addView(EditTextPhone);

        RelativeLayout.LayoutParams TextViewLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLocationParam.addRule(RelativeLayout.BELOW, EditTextPhone.getId());
        TextViewLocationParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewLocation = new TextView(context);
        TextViewLocation.setLayoutParams(TextViewLocationParam);
        TextViewLocation.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLocation.setText(getString(R.string.EditFragmentLocation));
        TextViewLocation.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewLocation);

        RelativeLayout.LayoutParams EditTextLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextLocationParam.addRule(RelativeLayout.BELOW, TextViewLocation.getId());
        EditTextLocationParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextLocation = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
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
                if (getActivity().getCurrentFocus() != null)
                    getActivity().getCurrentFocus().clearFocus();

                PermissionHandler = new PermissionHandler(Manifest.permission.ACCESS_FINE_LOCATION, 100, EditFragment.this, new PermissionHandler.PermissionEvent()
                {
                    @Override public void OnGranted() { }
                    @Override public void OnFailed() { }
                });

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, new MapFragment(), "MapFragment").addToBackStack("MapFragment").commitAllowingStateLoss();
            }
        });

        RelativeLayoutMain2.addView(EditTextLocation);

        RelativeLayout.LayoutParams TextViewEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailParam.addRule(RelativeLayout.BELOW, EditTextLocation.getId());
        TextViewEmailParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), 0);

        TextView TextViewEmail = new TextView(context);
        TextViewEmail.setLayoutParams(TextViewEmailParam);
        TextViewEmail.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewEmail.setText(getString(R.string.EditFragmentEmail));
        TextViewEmail.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewEmail);

        RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewEmail.getId());
        EditTextEmailParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextEmail = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
        EditTextEmail.setLayoutParams(EditTextEmailParam);
        EditTextEmail.setMaxLines(1);
        EditTextEmail.setHint(R.string.EditFragmentEmailHint);
        EditTextEmail.setId(MiscHandler.GenerateViewID());
        EditTextEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        RelativeLayoutMain2.addView(EditTextEmail);

        RelativeLayoutCrop = new RelativeLayout(context);
        RelativeLayoutCrop.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutCrop.setBackgroundResource(R.color.White);
        RelativeLayoutCrop.setVisibility(View.GONE);
        RelativeLayoutCrop.setClickable(true);

        RelativeLayoutMain.addView(RelativeLayoutCrop);

        RelativeLayout RelativeLayoutCropHeader = new RelativeLayout(context);
        RelativeLayoutCropHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutCropHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutCropHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutCrop.addView(RelativeLayoutCropHeader);

        ImageView ImageViewCropBack = new ImageView(context);
        ImageViewCropBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCropBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewCropBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewCropBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewCropBack.setId(MiscHandler.GenerateViewID());
        ImageViewCropBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RelativeLayoutCrop.setVisibility(View.GONE); } });

        RelativeLayoutCropHeader.addView(ImageViewCropBack);

        RelativeLayout.LayoutParams TextViewCropTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCropTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCropBack.getId());
        TextViewCropTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewCropTitle = new TextView(context);
        TextViewCropTitle.setLayoutParams(TextViewCropTitleParam);
        TextViewCropTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewCropTitle.setText(getString(R.string.EditFragmentCrop));
        TextViewCropTitle.setTypeface(null, Typeface.BOLD);
        TextViewCropTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutCropHeader.addView(TextViewCropTitle);

        RelativeLayout.LayoutParams LoadingViewCropParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewCropParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LoadingViewCropParam.addRule(RelativeLayout.CENTER_VERTICAL);
        LoadingViewCropParam.setMargins(0, 0, MiscHandler.ToDimension(context, 15), 0);

        final LoadingView LoadingViewCrop = new LoadingView(context);
        LoadingViewCrop.setLayoutParams(LoadingViewCropParam);
        LoadingViewCrop.SetColor(R.color.BlueLight);

        RelativeLayoutCropHeader.addView(LoadingViewCrop);

        RelativeLayout.LayoutParams ImageViewCropParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        ImageViewCropParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewCropParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final ImageView ImageViewCrop = new ImageView(context);
        ImageViewCrop.setLayoutParams(ImageViewCropParam);
        ImageViewCrop.setImageResource(R.drawable.ic_send_blue2);
        ImageViewCrop.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));
        ImageViewCrop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageViewCrop.setVisibility(View.GONE);
                LoadingViewCrop.Start();

                Bitmap Cropped = CropImageViewMain.getCroppedImage();

                try
                {
                    File CacheFolder = new File(context.getCacheDir(), "BioGram");

                    if (CacheFolder.exists() || CacheFolder.mkdir())
                    {
                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                        Cropped.compress(Bitmap.CompressFormat.JPEG, 85, BAOS);

                        if (IsCover)
                        {
                            CoverFile = new File(CacheFolder, ("cover." + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            ImageViewCover.setImageBitmap(Cropped);

                            FileOutputStream FileStream = new FileOutputStream(CoverFile);
                            FileStream.write(BAOS.toByteArray());
                            FileStream.flush();
                            FileStream.close();
                        }
                        else
                        {
                            ProfileFile = new File(CacheFolder, ("profile." + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                            ImageViewCircleProfile.setImageBitmap(Cropped);

                            FileOutputStream FileStream = new FileOutputStream(ProfileFile);
                            FileStream.write(BAOS.toByteArray());
                            FileStream.flush();
                            FileStream.close();
                        }
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("EditFragment-Crop: " + e.toString());
                }

                LoadingViewCrop.Stop();
                ImageViewCrop.setVisibility(View.VISIBLE);
                RelativeLayoutCrop.setVisibility(View.GONE);
            }
        });

        RelativeLayoutCropHeader.addView(ImageViewCrop);

        RelativeLayout.LayoutParams ViewLineCropParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineCropParam.addRule(RelativeLayout.BELOW, RelativeLayoutCropHeader.getId());

        View ViewLineCrop = new View(context);
        ViewLineCrop.setLayoutParams(ViewLineCropParam);
        ViewLineCrop.setBackgroundResource(R.color.Gray2);
        ViewLineCrop.setId(MiscHandler.GenerateViewID());

        RelativeLayoutCrop.addView(ViewLineCrop);

        RelativeLayout.LayoutParams CropImageViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        CropImageViewMainParam.addRule(RelativeLayout.BELOW, ViewLineCrop.getId());

        CropImageViewMain = new CropImageView(context);
        CropImageViewMain.setLayoutParams(CropImageViewMainParam);
        CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        CropImageViewMain.setFixedAspectRatio(true);
        CropImageViewMain.setAutoZoomEnabled(true);

        RelativeLayoutCrop.addView(CropImageViewMain);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final RelativeLayout RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);
        RelativeLayoutLoading.setClickable(true);

        RelativeLayoutMain.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);

        RelativeLayoutLoading.addView(LoadingViewMain);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryParam);
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain); } });

        RelativeLayoutLoading.addView(TextViewTryAgain);

        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);

        RetrieveDataFromServer(context, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        if (PermissionHandler != null)
            PermissionHandler.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (ProfileFile != null)
            ImageViewCircleProfile.setImageURI(Uri.parse(ProfileFile.getAbsolutePath()));

        if (CoverFile != null)
            ImageViewCover.setImageURI(Uri.parse(CoverFile.getAbsolutePath()));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("EditFragment");
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (ResultCode != -1)
            return;

        final Context context = getActivity();

        switch (RequestCode)
        {
            case FromCamera:
                PermissionHandler = new PermissionHandler(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101, EditFragment.this, new PermissionHandler.PermissionEvent()
                {
                    @Override
                    public void OnGranted()
                    {
                        ContentResolver _ContentResolver = context.getContentResolver();
                        ContentValues _ContentValues = new ContentValues();

                        _ContentValues.put(MediaStore.Images.Media.DATA, CaptureUri.getPath());
                        _ContentValues.put(MediaStore.Images.Media.IS_PRIVATE, 0);
                        _ContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, _ContentValues);

                        String[] SelectionArgs = { CaptureUri.getPath() };

                        Cursor _Cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " = ?", SelectionArgs, null);

                        if (_Cursor != null && _Cursor.moveToFirst())
                        {
                            CaptureUri = Uri.parse("content://media/external/images/media/" + _Cursor.getInt(_Cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                            _Cursor.close();
                        }

                        DoCrop(CaptureUri.getPath());
                    }

                    @Override
                    public void OnFailed()
                    {
                        MiscHandler.Toast(context, getString(R.string.PermissionStorage));
                    }
                });
            break;
        }
    }

    private void RetrieveDataFromServer(final Context context, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileGetEdit"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("EditFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
                RelativeLayoutLoading.setVisibility(View.GONE);

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

                        Glide.with(context)
                        .load(Data.getString("Cover"))
                        .dontAnimate()
                        .into(ImageViewCover);

                        Glide.with(context)
                        .load(Data.getString("Avatar"))
                        .dontAnimate()
                        .into(ImageViewCircleProfile);
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("EditFragment-RequestNew: " + e.toString());
                }
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.VISIBLE);
                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }

    public void DoCrop(String URL)
    {
        try
        {
            Context context = getActivity();

            File ImageFile = new File(URL);

            BitmapFactory.Options O = new BitmapFactory.Options();
            O.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(ImageFile.getAbsolutePath(), O);

            int Scale = 1;
            int Height = O.outHeight;
            int Width = O.outWidth;

            if (Height > 500 || Width > 500)
            {
                int HalfHeight = Height / 2;
                int HalfWidth = Width / 2;

                while ((HalfHeight / Scale) >= 500 && (HalfWidth / Scale) >= 500)
                {
                    Scale *= 2;
                }
            }

            O.inSampleSize = Scale;
            O.inJustDecodeBounds = false;

            Bitmap ResizeBitmap = BitmapFactory.decodeFile(ImageFile.getAbsolutePath(), O);

            Matrix matrix = new Matrix();
            int Orientation = new ExifInterface(URL).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            if (Orientation == 6)
                matrix.postRotate(90);
            else if (Orientation == 3)
                matrix.postRotate(180);
            else if (Orientation == 8)
                matrix.postRotate(270);

            ResizeBitmap = Bitmap.createBitmap(ResizeBitmap, 0, 0, ResizeBitmap.getWidth(), ResizeBitmap.getHeight(), matrix, true);
            String ResizeBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), ResizeBitmap, "BioGram Crop Image", null);

            if (IsCover)
                CropImageViewMain.setAspectRatio(2, 1);
            else
                CropImageViewMain.setAspectRatio(1, 1);

            CropImageViewMain.setImageUriAsync(Uri.parse(ResizeBitmapPath));
            RelativeLayoutCrop.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            MiscHandler.Debug("EditFragment-DoCrop: " + e.toString());
        }
    }

    public static class MapFragment extends Fragment
    {
        private GoogleMap _GoogleMap;
        private LocationTask TaskLocation;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final TextView TextViewName = new TextView(context);

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

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
            TextViewHeader.setText(getString(R.string.EditFragmentLocation));
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
            ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, new MapSearchFragment()).addToBackStack("MapSearchFragment").commitAllowingStateLoss(); } });

            RelativeLayoutHeader.addView(ImageViewSearch);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
            RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
            RelativeLayoutBottom.setBackgroundResource(R.color.White5);
            RelativeLayoutBottom.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutBottom);

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
                    if (_GoogleMap != null)
                    {
                        double Latitude = _GoogleMap.getCameraPosition().target.latitude;
                        double Longitude = _GoogleMap.getCameraPosition().target.longitude;

                        String Name = TextViewName.getText().toString();

                        if (Name.length() > 25)
                            Name = Name.substring(0, 25) + " ...";

                        EditFragment Parent = ((EditFragment) getActivity().getSupportFragmentManager().findFragmentByTag("EditFragment"));

                        if (Parent != null)
                        {
                            Parent.Position = (float) Latitude + ":" + (float) Longitude;
                            Parent.EditTextLocation.setText(Name);
                        }
                    }

                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutBottom.addView(ImageViewSend);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewNameParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));

            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewName.setId(MiscHandler.GenerateViewID());
            TextViewName.setText(getString(R.string.EditFragmentUnknownLocation));

            RelativeLayoutBottom.addView(TextViewName);

            RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
            TextViewPositionParam.setMargins(MiscHandler.ToDimension(context, 5),0, 0, 0);

            final TextView TextViewPosition = new TextView(context);
            TextViewPosition.setLayoutParams(TextViewPositionParam);
            TextViewPosition.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            RelativeLayoutBottom.addView(TextViewPosition);

            RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLine2Param.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());

            View ViewLine2 = new View(context);
            ViewLine2.setLayoutParams(ViewLine2Param);
            ViewLine2.setBackgroundResource(R.color.Gray2);
            ViewLine2.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine2);

            RelativeLayout.LayoutParams ViewCenterParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            ViewCenterParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            View ViewCenter = new View(context);
            ViewCenter.setLayoutParams(ViewCenterParam);
            ViewCenter.setBackgroundResource(R.color.Gray2);
            ViewCenter.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewCenter);

            RelativeLayout.LayoutParams ImageViewPinParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 42));
            ImageViewPinParam.addRule(RelativeLayout.ABOVE, ViewCenter.getId());
            ImageViewPinParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            ImageViewPinParam.setMargins(0, MiscHandler.ToDimension(context, 21), 0, 0);

            ImageView ImageViewPin = new ImageView(context);
            ImageViewPin.setLayoutParams(ImageViewPinParam);
            ImageViewPin.setImageResource(R.drawable.ic_location_pin);

            RelativeLayoutMain.addView(ImageViewPin);

            RelativeLayout.LayoutParams MapViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            MapViewParams.addRule(RelativeLayout.BELOW, ViewLine.getId());
            MapViewParams.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

            final MapView MapView = new MapView(context)
            {
                @Override
                public boolean onInterceptTouchEvent(MotionEvent m)
                {
                    if (m.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        if (_GoogleMap != null)
                        {
                            double Latitude = _GoogleMap.getCameraPosition().target.latitude;
                            double Longitude = _GoogleMap.getCameraPosition().target.longitude;

                            TextViewPosition.setText("(" + (float) Latitude + " , " + (float) Longitude + ")");
                            SetLocationName(Latitude, Longitude, TextViewName);
                        }
                    }

                    return super.onInterceptTouchEvent(m);
                }
            };
            MapView.onCreate(savedInstanceState);
            MapView.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    MapView.onResume();
                    _GoogleMap = googleMap;
                    _GoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    _GoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    _GoogleMap.getUiSettings().setCompassEnabled(true);

                    double Latitude = 0, Longitude = 0;
                    String[] Position = new String[] { };
                    EditFragment Parent = ((EditFragment) getActivity().getSupportFragmentManager().findFragmentByTag("EditFragment"));

                    if (Parent != null)
                        Position = Parent.Position.split(":");

                    if (Position.length > 1 && !Position[0].equals("") && !Position[1].equals(""))
                    {
                        Latitude = Double.parseDouble(Position[0]);
                        Longitude = Double.parseDouble(Position[1]);

                        CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), _GoogleMap.getMaxZoomLevel() - 15);
                        _GoogleMap.moveCamera(MyPosition);
                        _GoogleMap.animateCamera(MyPosition);
                    }
                    else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        _GoogleMap.setMyLocationEnabled(true);

                        LocationManager Manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        Location MyLocation = Manager.getLastKnownLocation(Manager.getBestProvider(new Criteria(), true));

                        if (MyLocation != null)
                        {
                            Latitude = MyLocation.getLatitude();
                            Longitude = MyLocation.getLongitude();

                            CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), _GoogleMap.getMaxZoomLevel() - 15);
                            _GoogleMap.moveCamera(MyPosition);
                            _GoogleMap.animateCamera(MyPosition);
                        }
                    }

                    TextViewPosition.setText("(" + (float) Latitude + " , " + (float) Longitude + ")");
                    SetLocationName(Latitude, Longitude, TextViewName);
                }
            });
            MapView.setLayoutParams(MapViewParams);

            RelativeLayoutMain.addView(MapView);

            ImageViewPin.bringToFront();

            return RelativeLayoutMain;
        }

        private void SetLocationName(double Latitude, double Longitude, TextView TextViewName)
        {
            if (TaskLocation != null)
                TaskLocation.cancel(true);

            TaskLocation = new LocationTask(Latitude, Longitude, TextViewName);
            TaskLocation.execute();
        }

        private class LocationTask extends AsyncTask<Void, Void, String>
        {
            final TextView TextViewMain;
            final double Latitude;
            final double Longitude;

            LocationTask(double latitude, double longitude, TextView textView)
            {
                Latitude = latitude;
                Longitude = longitude;
                TextViewMain = textView;
            }

            protected String doInBackground(Void... Voids)
            {
                if (isCancelled())
                    return "";

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
                        LocationName = getString(R.string.EditFragmentUnknownLocation);

                    return LocationName;
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("MapFragment-SetName: " + e.toString());
                }

                return "";
            }

            protected void onPostExecute(String Result)
            {
                if (isCancelled())
                    return;

                TextViewMain.setText(Result);
            }
        }
    }

    public static class MapSearchFragment extends Fragment
    {
        private final List<SearchStruct> SearchList = new ArrayList<>();
        private SearchAdapter AdapterSearch;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            Context context = getActivity();
            final EditText EditTextSearch = new EditText(context);

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

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

                    AndroidNetworking.get(URL)
                    .setTag("MapSearchFragment")
                    .build()
                    .getAsString(new StringRequestListener()
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
                                        MiscHandler.Debug("MapSearchFragment-Search1: " + e.toString());
                                    }
                                }

                                AdapterSearch.notifyDataSetChanged();
                                MiscHandler.HideSoftKey(getActivity());
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("MapSearchFragment-Search2: " + e.toString());
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

            RelativeLayoutMain.addView(ViewBlankLine);

            RelativeLayout.LayoutParams ListViewSearchParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ListViewSearchParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

            ListView ListViewSearch = new ListView(context);
            ListViewSearch.setLayoutParams(ListViewSearchParam);
            ListViewSearch.setAdapter(AdapterSearch = new SearchAdapter(context, SearchList));
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

                    MapFragment mapFragment = ((MapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MapFragment"));

                    if (mapFragment != null)
                        getActivity().getSupportFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();

                    EditFragment editFragment = ((EditFragment) getActivity().getSupportFragmentManager().findFragmentByTag("EditFragment"));

                    if (editFragment != null)
                    {
                        editFragment.Position = (float) Search.Latitude + ":" + (float) Search.Longitude;
                        editFragment.EditTextLocation.setText(Name);
                    }

                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutMain.addView(ListViewSearch);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RelativeLayoutMain;
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

                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));

                if (Search == null)
                    return RelativeLayoutMain;

                ImageView ImageViewIcon = new ImageView(context);
                ImageViewIcon.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
                ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
                ImageViewIcon.setImageResource(R.drawable.ic_location_blue);
                ImageViewIcon.setId(MiscHandler.GenerateViewID());

                RelativeLayoutMain.addView(ImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewNameParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

                TextView TextViewName = new TextView(context);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setText(Search.Name);
                TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewName.setId(MiscHandler.GenerateViewID());

                RelativeLayoutMain.addView(TextViewName);

                RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
                TextViewPositionParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

                TextView TextViewPosition = new TextView(context);
                TextViewPosition.setLayoutParams(TextViewPositionParam);
                TextViewPosition.setText("(" + (float) Search.Latitude + " , " + (float) Search.Longitude + ")");

                RelativeLayoutMain.addView(TextViewPosition);

                return RelativeLayoutMain;
            }
        }
    }
}
