package co.biogram.main.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;

public class ActivityProfileEdit extends AppCompatActivity
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private ImageView ImageViewCover;
    private ImageViewCircle ImageViewCircleProfile;

    private EditText EditTextUsername;
    private EditText EditTextDescription;
    private EditText EditTextLink;
    private EditText EditTextLocation;
    private EditText EditTextEmail;
    private String Position = "";

    private static int FrameLayoutID = MiscHandler.GenerateViewID();

    private Uri CaptureUri;
    private final int FromFile = 1;
    private final int FromCamera = 2;

    private boolean IsCover = false;

    private AlertDialog DialogProfile, DialogCover;

    private File FileCover;
    private File FileProfile;

    private PermissionHandler _PermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = this;

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setFocusableInTouchMode(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        Root.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ActivityProfileEdit.this, ActivityMain.class));
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
        TextViewHeader.setText(getString(R.string.ActivityProfileEditTitle));
        TextViewHeader.setTypeface(null, Typeface.BOLD);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewHeader);

        RelativeLayout.LayoutParams LoadingViewSaveParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LoadingViewSaveParam.addRule(RelativeLayout.CENTER_VERTICAL);
        LoadingViewSaveParam.setMargins(0, 0, MiscHandler.DpToPx(15), 0);

        final LoadingView LoadingViewSave = new LoadingView(context);
        LoadingViewSave.setLayoutParams(LoadingViewSaveParam);
        LoadingViewSave.SetColor(R.color.BlueLight);

        RelativeLayoutHeader.addView(LoadingViewSave);

        RelativeLayout.LayoutParams TextViewSaveParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextViewSaveParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewSaveParam.setMargins(0, 0, MiscHandler.DpToPx(15), 0);

        final TextView TextViewSave = new TextView(context);
        TextViewSave.setLayoutParams(TextViewSaveParam);
        TextViewSave.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewSave.setText(getString(R.string.ActivityProfileEditSave));
        TextViewSave.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextViewSave.setVisibility(View.GONE);
                LoadingViewSave.Start();

                HashMap<String, File> UploadFile = new HashMap<>();

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
                Progress.setMessage(getString(R.string.ActivityProfileEditUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setMax(100);
                Progress.setProgress(0);

                if (UploadFile != null)
                    Progress.show();

                RequestHandler.Core().Method("UPLOAD")
                .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_SET))
                .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                .Param("Username", EditTextUsername.getText().toString())
                .Param("Description", EditTextDescription.getText().toString())
                .Param("Link", EditTextLink.getText().toString())
                .Param("Position", Position)
                .Param("Location", EditTextLocation.getText().toString())
                .Param("Email", EditTextEmail.getText().toString())
                .File(UploadFile)
                .Tag("ActivityProfileEdit")
                .Listen(new RequestHandler.OnProgressCallBack()
                {
                    @Override
                    public void OnProgress(long Sent, long Total)
                    {
                        Progress.setProgress((int) (100 * Sent / Total));
                    }
                })
                .Build(new RequestHandler.OnCompleteCallBack()
                {
                    @Override
                    public void OnFinish(String Response, int Status)
                    {
                        TextViewSave.setVisibility(View.VISIBLE);
                        LoadingViewSave.Stop();
                        Progress.cancel();

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                                finish();
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Log(e.toString());
                            // Leave Me Alone
                        }
                    }
                });
            }
        });

        RelativeLayoutHeader.addView(TextViewSave);

        RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
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
        RelativeLayoutCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(160)));
        RelativeLayoutCover.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutCover.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogCover.show(); } });

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

        RelativeLayout.LayoutParams ImageViewCoverAddParam =  new RelativeLayout.LayoutParams(MiscHandler.DpToPx(50), MiscHandler.DpToPx(50));
        ImageViewCoverAddParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewCoverAdd = new ImageView(context);
        ImageViewCoverAdd.setLayoutParams(ImageViewCoverAddParam);
        ImageViewCoverAdd.setAlpha(0.50f);
        ImageViewCoverAdd.setImageResource(R.drawable.ic_camera_white);

        RelativeLayoutCover.addView(ImageViewCoverAdd);

        RelativeLayout.LayoutParams RelativeLayoutProfileParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(90), MiscHandler.DpToPx(90));
        RelativeLayoutProfileParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(110), 0, 0);

        RelativeLayout RelativeLayoutProfile = new RelativeLayout(context);
        RelativeLayoutProfile.setLayoutParams(RelativeLayoutProfileParam);
        RelativeLayoutProfile.setId(MiscHandler.GenerateViewID());
        RelativeLayoutProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogProfile.show(); } });

        RelativeLayoutMain.addView(RelativeLayoutProfile);

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCircleProfile.SetBorderColor(R.color.White);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.DpToPx(3));
        ImageViewCircleProfile.setImageResource(R.color.BlueLight);

        RelativeLayoutProfile.addView(ImageViewCircleProfile);

        GradientDrawable ShapeViewProfile = new GradientDrawable();
        ShapeViewProfile.setShape(GradientDrawable.OVAL);
        ShapeViewProfile.setCornerRadius(MiscHandler.DpToPx(15));
        ShapeViewProfile.setColor(Color.BLACK);
        ShapeViewProfile.setStroke(MiscHandler.DpToPx(3), Color.WHITE);

        View ViewBlackProfile = new View(context);
        ViewBlackProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewBlackProfile.setBackground(ShapeViewProfile);
        ViewBlackProfile.setAlpha(0.25f);

        RelativeLayoutProfile.addView(ViewBlackProfile);

        RelativeLayout.LayoutParams ImageViewProfileAddParam =  new RelativeLayout.LayoutParams(MiscHandler.DpToPx(35), MiscHandler.DpToPx(35));
        ImageViewProfileAddParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewProfileAdd = new ImageView(context);
        ImageViewProfileAdd.setLayoutParams(ImageViewProfileAddParam);
        ImageViewProfileAdd.setAlpha(0.50f);
        ImageViewProfileAdd.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewProfileAdd.setImageResource(R.drawable.ic_camera_white);

        RelativeLayoutProfile.addView(ImageViewProfileAdd);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.BELOW, RelativeLayoutProfile.getId());
        TextViewUsernameParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewUsername = new TextView(context);
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewUsername.setText(getString(R.string.ActivityProfileEditUsername));
        TextViewUsername.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewUsername);

        RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
        EditTextUsernameParam.setMargins(MiscHandler.DpToPx(10), 0, MiscHandler.DpToPx(10), 0);

        EditTextUsername = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextUsername.setLayoutParams(EditTextUsernameParam);
        EditTextUsername.setMaxLines(1);
        EditTextUsername.setId(MiscHandler.GenerateViewID());
        EditTextUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextUsername.setHint(R.string.ActivityProfileEditUsernameHint);
        EditTextUsername.setFilters(new InputFilter[]
        {
            new InputFilter.LengthFilter(32), new InputFilter()
            {
                @Override
                public CharSequence filter(CharSequence Source, int Start, int End, Spanned Dest, int DestStart, int DestEnd)
                {
                    if (End > Start)
                    {
                        char[] AcceptedChars = new char[] {'a', 'b', 'c', 'd', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

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
        TextViewDescriptionParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewDescription = new TextView(context);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setText(getString(R.string.ActivityProfileEditDescription));
        TextViewDescription.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());
        EditTextDescriptionParam.setMargins(MiscHandler.DpToPx(10), 0, MiscHandler.DpToPx(10), 0);

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
        TextViewLinkParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewLink = new TextView(context);
        TextViewLink.setLayoutParams(TextViewLinkParam);
        TextViewLink.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLink.setText(getString(R.string.ActivityProfileEditWebsite));
        TextViewLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewLink);

        RelativeLayout.LayoutParams EditTextLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextLinkParam.addRule(RelativeLayout.BELOW, TextViewLink.getId());
        EditTextLinkParam.setMargins(MiscHandler.DpToPx(10), 0, MiscHandler.DpToPx(10), 0);

        EditTextLink = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextLink.setLayoutParams(EditTextLinkParam);
        EditTextLink.setMaxLines(1);
        EditTextLink.setId(MiscHandler.GenerateViewID());
        EditTextLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextLink.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextLink.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        RelativeLayoutMain.addView(EditTextLink);

        RelativeLayout.LayoutParams TextViewLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLocationParam.addRule(RelativeLayout.BELOW, EditTextLink.getId());
        TextViewLocationParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewLocation = new TextView(context);
        TextViewLocation.setLayoutParams(TextViewLocationParam);
        TextViewLocation.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLocation.setText(getString(R.string.ActivityProfileEditLocation));
        TextViewLocation.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewLocation);

        RelativeLayout.LayoutParams EditTextLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextLocationParam.addRule(RelativeLayout.BELOW, TextViewLocation.getId());
        EditTextLocationParam.setMargins(MiscHandler.DpToPx(10), 0, MiscHandler.DpToPx(10), 0);

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

                getFragmentManager().beginTransaction().replace(FrameLayoutID, new FragmentMap()).addToBackStack("FragmentMap").commit();
            }
        });

        RelativeLayoutMain.addView(EditTextLocation);

        RelativeLayout.LayoutParams TextViewEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailParam.addRule(RelativeLayout.BELOW, EditTextLocation.getId());
        TextViewEmailParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewEmail = new TextView(context);
        TextViewEmail.setLayoutParams(TextViewEmailParam);
        TextViewEmail.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewEmail.setText(getString(R.string.ActivityProfileEditEmail));
        TextViewEmail.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(TextViewEmail);

        RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewEmail.getId());
        EditTextEmailParam.setMargins(MiscHandler.DpToPx(10), 0, MiscHandler.DpToPx(10), 0);

        EditTextEmail = new EditText(new ContextThemeWrapper(this, R.style.GeneralEditTextTheme));
        EditTextEmail.setLayoutParams(EditTextEmailParam);
        EditTextEmail.setMaxLines(1);
        EditTextEmail.setHint(R.string.ActivityProfileEditEmailHint);
        EditTextEmail.setId(MiscHandler.GenerateViewID());
        EditTextEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        RelativeLayoutMain.addView(EditTextEmail);

        FrameLayout FrameLayoutTab = new FrameLayout(context);
        FrameLayoutTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutTab.setId(FrameLayoutID);

        Root.addView(FrameLayoutTab);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewData = new LoadingView(context);
        LoadingViewData.setLayoutParams(LoadingViewDataParam);
        LoadingViewData.SetColor(R.color.BlueGray2);

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

        InitializationCover();
        InitializationProfile();
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
        RequestHandler.Core().Cancel("ActivityProfileEdit");
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
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
            ImageViewBack.setImageResource(R.drawable.ic_back_blue);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().getFragmentManager().beginTransaction().remove(FragmentMap.this).commit(); } });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewHeader.setText(getString(R.string.ActivityProfileEditLocation));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewSearch = new ImageView(context);
            ImageViewSearch.setLayoutParams(ImageViewSearchParam);
            ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewSearch.setPadding(MiscHandler.DpToPx(16), MiscHandler.DpToPx(16), MiscHandler.DpToPx(16), MiscHandler.DpToPx(16));
            ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
            ImageViewSearch.setId(MiscHandler.GenerateViewID());
            ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getFragmentManager().beginTransaction().replace(FrameLayoutID, new FragmentSearch()).addToBackStack("FragmentSearch").commit(); } });

            RelativeLayoutHeader.addView(ImageViewSearch);

            RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
            ViewBlankLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewBlankLine = new View(context);
            ViewBlankLine.setLayoutParams(ViewBlankLineParam);
            ViewBlankLine.setBackgroundResource(R.color.Gray2);
            ViewBlankLine.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewBlankLine);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56));
            RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
            RelativeLayoutBottom.setBackgroundResource(R.color.White5);
            RelativeLayoutBottom.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutBottom);

            ImageView ImageViewSend = new ImageView(context);
            ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewSend.setPadding(MiscHandler.DpToPx(13), MiscHandler.DpToPx(13), MiscHandler.DpToPx(13), MiscHandler.DpToPx(13));
            ImageViewSend.setImageResource(R.drawable.ic_location_blue);
            ImageViewSend.setId(MiscHandler.GenerateViewID());
            ImageViewSend.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (_GoogleMap != null)
                    {
                        double Lat = _GoogleMap.getCameraPosition().target.latitude;
                        double Lon = _GoogleMap.getCameraPosition().target.longitude;

                        String Name = TextViewName.getText().toString();

                        if (Name.length() > 25)
                            Name = Name.substring(0, 25) + " ...";

                        ((ActivityProfileEdit) getActivity()).Position = (float) Lat + ":" + (float) Lon;
                        ((ActivityProfileEdit) getActivity()).EditTextLocation.setText(Name);
                    }

                    getActivity().getFragmentManager().beginTransaction().remove(FragmentMap.this).commit();
                }
            });

            RelativeLayoutBottom.addView(ImageViewSend);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewNameParam.setMargins(MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5));

            TextViewName = new TextView(context);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewName.setId(MiscHandler.GenerateViewID());

            RelativeLayoutBottom.addView(TextViewName);

            RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewSend.getId());
            TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
            TextViewPositionParam.setMargins(MiscHandler.DpToPx(5),0, 0, 0);

            TextViewPosition = new TextView(context);
            TextViewPosition.setLayoutParams(TextViewPositionParam);
            TextViewPosition.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            RelativeLayoutBottom.addView(TextViewPosition);

            RelativeLayout.LayoutParams ViewBlankLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
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

            RelativeLayout.LayoutParams ImageViewPinParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(24), MiscHandler.DpToPx(42));
            ImageViewPinParam.addRule(RelativeLayout.ABOVE, ViewCenter.getId());
            ImageViewPinParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            ImageViewPinParam.setMargins(0, MiscHandler.DpToPx(21), 0, 0);

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

        public class MapThreadClass extends Thread
        {
            private double Latitude, Longitude;
            private Handler MapHandler;
            private Runnable MapRunnable = new Runnable()
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

                if (MapHandler != null && MapRunnable != null)
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
        private List<SearchStruct> SearchList = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
            ImageViewBack.setImageResource(R.drawable.ic_back_blue);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { MiscHandler.HideKeyBoard(getActivity()); getActivity().onBackPressed(); } });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewSearch = new ImageView(context);
            ImageViewSearch.setLayoutParams(ImageViewSearchParam);
            ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewSearch.setPadding(MiscHandler.DpToPx(16), MiscHandler.DpToPx(16), MiscHandler.DpToPx(16), MiscHandler.DpToPx(16));
            ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
            ImageViewSearch.setId(MiscHandler.GenerateViewID());
            ImageViewSearch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String URL = "https://api.foursquare.com/v2/venues/search/?v=20170101&locale=en&limit=25&client_id=YXWKVYF1XFBW3XRC4I0EIVALENCVNVEX5ARMQA4TP5ZUAB41&client_secret=QX2MNDH4GGFYSQCLFE1NVXDFJBNTNWKT3IDX0OUJD54TNKJ2&near=" + EditTextSearch.getText().toString();

                    RequestHandler.Core().Method("GET").Address(URL).Tag("FragmentSearch").Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
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
                                MiscHandler.HideKeyBoard(getActivity());
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }
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

            RelativeLayoutHeader.addView(EditTextSearch);

            RelativeLayout.LayoutParams ViewBlankLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
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

                    ((ActivityProfileEdit) getActivity()).Position = (float) Search.Latitude + ":" + (float) Search.Longitude;
                    ((ActivityProfileEdit) getActivity()).EditTextLocation.setText(Name);

                    getActivity().getFragmentManager().beginTransaction().remove(FragmentSearch.this).commit();
                }
            });

            Root.addView(ListViewSearch);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextSearch, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            EditTextSearch.requestFocus();

            return Root;
        }

        public class SearchStruct
        {
            String Name;
            double Latitude;
            double Longitude;

            SearchStruct(String name, double latitude, double longitude)
            {
                Name = name;
                Latitude = latitude;
                Longitude = longitude;
            }
        }

        public class SearchAdapter extends ArrayAdapter<SearchStruct>
        {
            Context context;

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
                MainView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));

                if (Search == null)
                    return MainView;

                ImageView ImageViewIcon = new ImageView(context);
                ImageViewIcon.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
                ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT));
                ImageViewIcon.setImageResource(R.drawable.ic_location_blue);
                ImageViewIcon.setId(MiscHandler.GenerateViewID());

                MainView.addView(ImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewNameParam.setMargins(0, MiscHandler.DpToPx(5), 0, 0);

                TextView TextViewName = new TextView(context);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setText(Search.Name);
                TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewName.setId(MiscHandler.GenerateViewID());

                MainView.addView(TextViewName);

                RelativeLayout.LayoutParams TextViewPositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewPositionParam.addRule(RelativeLayout.RIGHT_OF, ImageViewIcon.getId());
                TextViewPositionParam.addRule(RelativeLayout.BELOW, TextViewName.getId());
                TextViewPositionParam.setMargins(0, MiscHandler.DpToPx(5), 0, 0);

                TextView TextViewPosition = new TextView(context);
                TextViewPosition.setLayoutParams(TextViewPositionParam);
                TextViewPosition.setText("(" + (float) Search.Latitude + " , " + (float) Search.Longitude + ")");

                MainView.addView(TextViewPosition);

                return MainView;
            }
        }
    }

    private void InitializationProfile()
    {
        final String[] DialogItems = new String[] { getString(R.string.ActivityProfileEditDialogMessage1), getString(R.string.ActivityProfileEditDialogMessage2), getString(R.string.ActivityProfileEditDialogMessage3) };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, DialogItems);
        AlertDialog.Builder ProfileBuilder = new AlertDialog.Builder(this);
        ProfileBuilder.setTitle(getString(R.string.ActivityProfileEditDialogMessage4));
        ProfileBuilder.setAdapter(adapter, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int Position)
            {
                if (Position == 0)
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
                            MiscHandler.Toast(ActivityProfileEdit.this, getString(R.string.GeneralPermissionCamera));
                        }
                    });
                }
                else if (Position == 1)
                {
                    IsCover = false;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileEditCompleteAction)), FromFile);
                }
                else if (Position == 2)
                {
                    RequestHandler.Core().Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_AVATAR_DELETE))
                    .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                    .Tag("ActivityProfileEdit")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            ImageViewCircleProfile.setImageResource(R.color.BlueLight);
                            MiscHandler.Toast(ActivityProfileEdit.this, getString(R.string.ActivityProfileEditImageBackGroundRemoved));
                        }
                    });
                }
            }
        });

        DialogProfile = ProfileBuilder.create();
    }

    private void InitializationCover()
    {
        String[] DialogItem = new String[] { getString(R.string.ActivityProfileEditDialogMessage1), getString(R.string.ActivityProfileEditDialogMessage2), getString(R.string.ActivityProfileEditDialogMessage3) };

        AlertDialog.Builder BackGroundBuilder = new AlertDialog.Builder(this);
        BackGroundBuilder.setTitle(getString(R.string.ActivityProfileEditDialogMessage4));
        BackGroundBuilder.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item, DialogItem), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface d, int Position)
            {
                if (Position == 0)
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
                            MiscHandler.Toast(ActivityProfileEdit.this, getString(R.string.GeneralPermissionCamera));
                        }
                    });

                }
                else if (Position == 1)
                {
                    IsCover = true;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.ActivityProfileEditCompleteAction)), FromFile);
                }
                else if (Position == 2)
                {
                    RequestHandler.Core().Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_COVER_DELETE))
                    .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                    .Tag("ActivityProfileEdit")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            ImageViewCover.setImageResource(R.color.BlueLight);
                            MiscHandler.Toast(ActivityProfileEdit.this, getString(R.string.ActivityProfileEditImageBackGroundRemoved));
                        }
                    });
                }
            }
        });

        DialogCover = BackGroundBuilder.create();
    }

    private void RetrieveDataFromServer()
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        RequestHandler.Core().Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_GET))
        .Header("TOKEN", SharedHandler.GetString("TOKEN"))
        .Tag("ActivityProfileEdit")
        .Build(new RequestHandler.OnCompleteCallBack()
        {
            @Override
            public void OnFinish(String Response, int Status)
            {
                if (Status < 0)
                {
                    TextViewTry.setVisibility(View.VISIBLE);
                    LoadingViewData.Stop();
                    return;
                }

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

                        RequestHandler.Core().LoadImage(ImageViewCover, Data.getString("Cover"), "ActivityProfileEdit", false);
                        RequestHandler.Core().LoadImage(ImageViewCircleProfile, Data.getString("Avatar"), "ActivityProfileEdit", MiscHandler.DpToPx(90), MiscHandler.DpToPx(90), true);
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }
            }
        });
    }

    private void DoCrop()
    {
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

                while ((o.outWidth / Scale / 2) >= (int)(MiscHandler.DpToPx(160) * 2.45f) && (o.outHeight / Scale / 2) >= MiscHandler.DpToPx(160))
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
                .setActivityTitle(getString(R.string.ActivityProfileEditCrop))
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(2, 1)
                .setFixAspectRatio(true)
                .setRequestedSize((int)(MiscHandler.DpToPx(160) * 2.45f), MiscHandler.DpToPx(160), CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .start(this);
            }
            catch (Exception e)
            {
                // Leave Me Alone
            }
        }
        else
            CropImage.activity(CaptureUri).setAllowRotation(true).setActivityTitle(getString(R.string.ActivityProfileEditCrop)).setGuidelines(CropImageView.Guidelines.ON_TOUCH).setFixAspectRatio(true).start(this);
    }
}
