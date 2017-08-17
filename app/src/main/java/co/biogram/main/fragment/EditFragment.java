package co.biogram.main.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class EditFragment extends Fragment
{
    private PermissionHandler PermissionHandler;

    private Uri CaptureUri;
    private final int FromFile = 1;
    private final int FromCamera = 2;

    private File CoverFile;
    private File ProfileFile;
    private boolean IsCover = false;

    private EditText EditTextUsername;
    private EditText EditTextDescription;
    private EditText EditTextLink;
    private EditText EditTextPhone;
    private EditText EditTextLocation;
    private EditText EditTextEmail;
    private String Position = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final ImageView ImageViewCover = new ImageView(context);
        final ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setFocusableInTouchMode(true);

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
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MiscHandler.HideSoftKey(getActivity());
                getActivity().onBackPressed();
            }
        });

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
                TextViewCamera.setText(getString(R.string.ActivityProfileDialogCamera));
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
                        IsCover = true;

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.EditFragmentCompleteAction)), FromFile);

                        DialogCover.dismiss();
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

        RelativeLayoutMain.addView(RelativeLayoutCover);

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
                TextViewCamera.setText(getString(R.string.ActivityProfileDialogCamera));
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
                        IsCover = false;

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileCompleteAction)), FromFile);

                        DialogProfile.dismiss();
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
                        .setTag("EditFragment")
                        .build()
                        .getAsString(new StringRequestListener()
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

                LinearLayoutMain.addView(TextViewRemove);

                DialogProfile.setContentView(LinearLayoutMain);
                DialogProfile.show();
            }
        });

        RelativeLayoutMain.addView(RelativeLayoutProfile);

        ImageViewCircleProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCircleProfile.SetBorderColor(R.color.White);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setImageResource(R.color.BlueLight);

        RelativeLayoutProfile.addView(ImageViewCircleProfile);

        GradientDrawable ShapeViewProfile = new GradientDrawable();
        ShapeViewProfile.setShape(GradientDrawable.OVAL);
        ShapeViewProfile.setCornerRadius(MiscHandler.ToDimension(context, 15));
        ShapeViewProfile.setColor(0xFF000000);
        ShapeViewProfile.setStroke(MiscHandler.ToDimension(context, 3), 0xFFFFFFFF);

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

        EditTextUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
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

        EditTextDescription = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
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

        EditTextLink = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
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

        EditTextPhone = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
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
                if (getCurrentFocus() != null)
                    getCurrentFocus().clearFocus();

                //getFragmentManager().beginTransaction().add(R.id.ActivityProfileContainer, new FragmentMap()).addToBackStack("FragmentMap").commit();
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
        TextViewTry.setText(getString(R.string.TryAgain));
        TextViewTry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(); } });

        RelativeLayoutLoading.addView(TextViewTry);
        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);

        RetrieveDataFromServer();

        return RelativeLayoutMain;
    }
}
