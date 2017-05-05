package co.biogram.main.fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class FragmentProfileEdit extends AppCompatActivity
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

    private int FrameLayoutID = MiscHandler.GenerateViewID();

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

        RelativeLayout Root = new RelativeLayout(App.GetContext());
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setFocusableInTouchMode(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(App.GetContext());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        Root.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(App.GetContext());
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { finish(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewHeader = new TextView(App.GetContext());
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TextViewHeader.setText(getString(R.string.ActivityProfileEditTitle));
        TextViewHeader.setTypeface(null, Typeface.BOLD);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewHeader);

        RelativeLayout.LayoutParams LoadingViewSaveParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LoadingViewSaveParam.addRule(RelativeLayout.CENTER_VERTICAL);
        LoadingViewSaveParam.setMargins(0, 0, MiscHandler.DpToPx(15), 0);

        final LoadingView LoadingViewSave = new LoadingView(App.GetContext());
        LoadingViewSave.setLayoutParams(LoadingViewSaveParam);
        LoadingViewSave.SetColor(R.color.BlueLight);

        RelativeLayoutHeader.addView(LoadingViewSave);

        RelativeLayout.LayoutParams TextViewSaveParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextViewSaveParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewSaveParam.setMargins(0, 0, MiscHandler.DpToPx(15), 0);

        final TextView TextViewSave = new TextView(App.GetContext());
        TextViewSave.setLayoutParams(TextViewSaveParam);
        TextViewSave.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
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

                final ProgressDialog Progress = new ProgressDialog(FragmentProfileEdit.this);
                Progress.setMessage(getString(R.string.ActivityProfileEditUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setMax(100);
                Progress.setProgress(0);

                if (UploadFile != null)
                    Progress.show();

                RequestHandler.Method("UPLOAD")
                .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_SET))
                .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                .Param("Username", EditTextUsername.getText().toString())
                .Param("Description", EditTextDescription.getText().toString())
                .Param("Link", EditTextLink.getText().toString())
                .Param("Position", Position)
                .Param("Location", EditTextLocation.getText().toString())
                .Param("Email", EditTextEmail.getText().toString())
                .File(UploadFile)
                .Tag("FragmentProfileEdit")
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

        View ViewBlankLine = new View(App.GetContext());
        ViewBlankLine.setLayoutParams(ViewBlankLineParam);
        ViewBlankLine.setBackgroundResource(R.color.Gray2);
        ViewBlankLine.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewBlankLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

        ScrollView ScrollViewMain = new ScrollView(App.GetContext());
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setVerticalScrollBarEnabled(false);
        ScrollViewMain.setHorizontalScrollBarEnabled(false);
        ScrollViewMain.setFillViewport(true);

        Root.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(App.GetContext());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain);

        RelativeLayout RelativeLayoutCover = new RelativeLayout(App.GetContext());
        RelativeLayoutCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(160)));
        RelativeLayoutCover.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutCover.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogCover.show(); } });

        RelativeLayoutMain.addView(RelativeLayoutCover);

        ImageViewCover = new ImageView(App.GetContext());
        ImageViewCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCover.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayoutCover.addView(ImageViewCover);

        View ViewBlackCover = new View(App.GetContext());
        ViewBlackCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewBlackCover.setBackgroundResource(R.color.Black);
        ViewBlackCover.setAlpha(0.25f);

        RelativeLayoutCover.addView(ViewBlackCover);

        RelativeLayout.LayoutParams ImageViewCoverAddParam =  new RelativeLayout.LayoutParams(MiscHandler.DpToPx(50), MiscHandler.DpToPx(50));
        ImageViewCoverAddParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewCoverAdd = new ImageView(App.GetContext());
        ImageViewCoverAdd.setLayoutParams(ImageViewCoverAddParam);
        ImageViewCoverAdd.setAlpha(0.50f);
        ImageViewCoverAdd.setImageResource(R.drawable.ic_camera_white);

        RelativeLayoutCover.addView(ImageViewCoverAdd);

        RelativeLayout.LayoutParams RelativeLayoutProfileParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(90), MiscHandler.DpToPx(90));
        RelativeLayoutProfileParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(110), 0, 0);

        RelativeLayout RelativeLayoutProfile = new RelativeLayout(App.GetContext());
        RelativeLayoutProfile.setLayoutParams(RelativeLayoutProfileParam);
        RelativeLayoutProfile.setId(MiscHandler.GenerateViewID());
        RelativeLayoutProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogProfile.show(); } });

        RelativeLayoutMain.addView(RelativeLayoutProfile);

        ImageViewCircleProfile = new ImageViewCircle(App.GetContext());
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

        View ViewBlackProfile = new View(App.GetContext());
        ViewBlackProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewBlackProfile.setBackground(ShapeViewProfile);
        ViewBlackProfile.setAlpha(0.25f);

        RelativeLayoutProfile.addView(ViewBlackProfile);

        RelativeLayout.LayoutParams ImageViewProfileAddParam =  new RelativeLayout.LayoutParams(MiscHandler.DpToPx(35), MiscHandler.DpToPx(35));
        ImageViewProfileAddParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewProfileAdd = new ImageView(App.GetContext());
        ImageViewProfileAdd.setLayoutParams(ImageViewProfileAddParam);
        ImageViewProfileAdd.setAlpha(0.50f);
        ImageViewProfileAdd.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewProfileAdd.setImageResource(R.drawable.ic_camera_white);

        RelativeLayoutProfile.addView(ImageViewProfileAdd);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.BELOW, RelativeLayoutProfile.getId());
        TextViewUsernameParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewUsername = new TextView(App.GetContext());
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray3));
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
        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(App.GetContext(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        RelativeLayoutMain.addView(EditTextUsername);

        RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());
        TextViewDescriptionParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewDescription = new TextView(App.GetContext());
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray3));
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
        EditTextDescription.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);

        RelativeLayoutMain.addView(EditTextDescription);

        RelativeLayout.LayoutParams TextViewLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLinkParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());
        TextViewLinkParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewLink = new TextView(App.GetContext());
        TextViewLink.setLayoutParams(TextViewLinkParam);
        TextViewLink.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray3));
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

        TextView TextViewLocation = new TextView(App.GetContext());
        TextViewLocation.setLayoutParams(TextViewLocationParam);
        TextViewLocation.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray3));
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

        RelativeLayoutMain.addView(EditTextLocation);

        RelativeLayout.LayoutParams TextViewEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailParam.addRule(RelativeLayout.BELOW, EditTextLocation.getId());
        TextViewEmailParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), 0);

        TextView TextViewEmail = new TextView(App.GetContext());
        TextViewEmail.setLayoutParams(TextViewEmailParam);
        TextViewEmail.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray3));
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

        FrameLayout FrameLayoutTab = new FrameLayout(App.GetContext());
        FrameLayoutTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutTab.setId(FrameLayoutID);

        Root.addView(FrameLayoutTab);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewBlankLine.getId());

        RelativeLayoutLoading = new RelativeLayout(App.GetContext());
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewData = new LoadingView(App.GetContext());
        LoadingViewData.setLayoutParams(LoadingViewDataParam);
        LoadingViewData.SetColor(R.color.BlueGray2);

        RelativeLayoutLoading.addView(LoadingViewData);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTry = new TextView(App.GetContext());
        TextViewTry.setLayoutParams(TextViewTryParam);
        TextViewTry.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray2));
        TextViewTry.setText(getString(R.string.GeneralTryAgain));
        TextViewTry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(); } });

        RelativeLayoutLoading.addView(TextViewTry);

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
        RequestHandler.Cancel("FragmentProfileEdit");
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
                _PermissionHandler = new PermissionHandler(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101, FragmentProfileEdit.this, new PermissionHandler.PermissionEvent()
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
                        MiscHandler.Toast(getString(R.string.GeneralPermissionStorage));
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
                    _PermissionHandler = new PermissionHandler(Manifest.permission.CAMERA, 100, FragmentProfileEdit.this, new PermissionHandler.PermissionEvent()
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
                            MiscHandler.Toast(getString(R.string.GeneralPermissionCamera));
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
                    RequestHandler.Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_AVATAR_DELETE))
                    .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                    .Tag("FragmentProfileEdit")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            ImageViewCircleProfile.setImageResource(R.color.BlueLight);
                            MiscHandler.Toast(getString(R.string.ActivityProfileEditImageBackGroundRemoved));
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
                    _PermissionHandler = new PermissionHandler(Manifest.permission.CAMERA, 100, FragmentProfileEdit.this, new PermissionHandler.PermissionEvent()
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
                            MiscHandler.Toast(getString(R.string.GeneralPermissionCamera));
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
                    RequestHandler.Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_COVER_DELETE))
                    .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                    .Tag("FragmentProfileEdit")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            ImageViewCover.setImageResource(R.color.BlueLight);
                            MiscHandler.Toast(getString(R.string.ActivityProfileEditImageBackGroundRemoved));
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

        RequestHandler.Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_EDIT_GET))
        .Header("TOKEN", SharedHandler.GetString("TOKEN"))
        .Tag("FragmentProfileEdit")
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

                        RequestHandler.GetImage(ImageViewCover, Data.getString("Cover"), "FragmentProfileEdit", false);
                        RequestHandler.GetImage(ImageViewCircleProfile, Data.getString("Avatar"), "FragmentProfileEdit", MiscHandler.DpToPx(90), MiscHandler.DpToPx(90), false);
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Log(e.toString());
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





























        /*



        EditTextLocation.setFocusable(false);
        EditTextLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment _Map = new FragmentMap();
                FragmentTransaction Trans = getFragmentManager().beginTransaction();
                Trans.add(R.id.FrameLayoutMap, _Map);
                Trans.addToBackStack("FragmentMap");
                Trans.commit();
            }
        });

        EditTextUsername = (EditText) findViewById(R.id.EditTextUsername);
        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(App.GetContext(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);


        RetrieveDataFromServer();
    }

    public static class FragmentMap extends Fragment
    {
        private MapThreadClass MapThread;
        private MapView _MapView;
        private GoogleMap _GoogleMap;
        private TextView TextViewName;
        private TextView TextViewPosition;

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_main_fragment_profile_edit_fragment_map, Parent, false);

            TextViewName = (TextView) RootView.findViewById(R.id.TextViewName);
            TextViewPosition = (TextView) RootView.findViewById(R.id.TextViewPosition);

            _MapView = new MapView(App.GetContext())
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

            _MapView.onCreate(null);

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

                    double Lat = 0;
                    double Lon = 0;
                    String[] Position = ((FragmentProfileEdit) getActivity()).Position.split(":");

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

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getActivity().onBackPressed();
                }
            });

            RootView.findViewById(R.id.ImageViewSearch).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Fragment Search = new FragmentSearch();
                    FragmentTransaction Trans = getFragmentManager().beginTransaction();
                    Trans.add(R.id.FrameLayoutMap, Search);
                    Trans.addToBackStack("FragmentSearch");
                    Trans.commit();
                }
            });

            RootView.findViewById(R.id.ImageViewSend).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (_GoogleMap != null)
                    {
                        double Lat = _GoogleMap.getCameraPosition().target.latitude;
                        double Lon = _GoogleMap.getCameraPosition().target.longitude;

                        ((FragmentProfileEdit) getActivity()).Position = (float) Lat + ":" + (float) Lon;
                        ((FragmentProfileEdit) getActivity()).EditTextLocation.setText(TextViewName.getText().toString());
                    }

                    getActivity().onBackPressed();
                }
            });

            RelativeLayout.LayoutParams MapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            MapParams.addRule(RelativeLayout.BELOW, R.id.ViewBlankLine);
            MapParams.addRule(RelativeLayout.ABOVE, R.id.ViewBlankLine2);
            _MapView.setLayoutParams(MapParams);

            ((RelativeLayout) RootView.findViewById(R.id.RelativeLayoutRoot)).addView(_MapView);

            RootView.findViewById(R.id.ImageViewPin).bringToFront();

            return RootView;
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
            private double Lat;
            private double Lon;
            private Handler MapHandler;
            private Runnable MapRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String LocationName = "";
                        Geocoder geocoder = new Geocoder(App.GetContext());
                        List<Address> Address = geocoder.getFromLocation(Lat, Lon, 1);

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

            void FindLocationName(double _Lat, double _Lon)
            {
                Lat = _Lat;
                Lon = _Lon;

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
        private SearchAdapter _SearchAdapter;
        private List<SearchStruct> SearchList = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_main_fragment_profile_edit_fragment_search, Parent, false);

            final EditText EditTextHeader = (EditText) RootView.findViewById(R.id.EditTextHeader);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getActivity().onBackPressed();
                }
            });

            RootView.findViewById(R.id.ImageViewSearch).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String URL = "https://api.foursquare.com/v2/venues/search/?v=20170101&locale=en&limit=25&client_id=YXWKVYF1XFBW3XRC4I0EIVALENCVNVEX5ARMQA4TP5ZUAB41&client_secret=QX2MNDH4GGFYSQCLFE1NVXDFJBNTNWKT3IDX0OUJD54TNKJ2&near=" + EditTextHeader.getText().toString();

                    AndroidNetworking.get(URL).setTag("FragmentProfileEdit").build().getAsString(new StringRequestListener()
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
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            Toast.makeText(App.GetContext(), getString(R.string.GeneralCheckInternet), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            InputMethodManager IMG = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMG.showSoftInput(EditTextHeader, 0);
            IMG.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            EditTextHeader.requestFocus();

            _SearchAdapter = new SearchAdapter(getActivity().getApplicationContext(), SearchList);
            ListView ListViewSearch = (ListView) RootView.findViewById(R.id.ListViewSearch);
            ListViewSearch.setAdapter(_SearchAdapter);

            return RootView;
        }

        public class SearchStruct
        {
            String Name;
            double Lat;
            double Lon;

            SearchStruct(String Name, double Lat, double Lon)
            {
                this.Name = Name;
                this.Lat = Lat;
                this.Lon = Lon;
            }
        }

        public class SearchAdapter extends ArrayAdapter<SearchStruct>
        {
            SearchAdapter(Context context, List<SearchStruct> SearchList)
            {
                super(context, -1, SearchList);
            }

            @Override
            public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
            {
                SearchStruct Search = SearchList.get(position);

                RelativeLayout MainView = new RelativeLayout(App.GetContext());
                RelativeLayout.LayoutParams MainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56));
                MainView.setLayoutParams(MainParam);

                if (Search == null)
                    return MainView;

                ImageView Icon = new ImageView(App.GetContext());
                RelativeLayout.LayoutParams IconParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), RelativeLayout.LayoutParams.MATCH_PARENT);
                Icon.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
                Icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Icon.setLayoutParams(IconParam);
                Icon.setImageResource(R.drawable.ic_location_blue);
                Icon.setId(MiscHandler.GenerateViewID());

                MainView.addView(Icon);

                TextView Name = new TextView(App.GetContext());
                RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                NameParam.addRule(RelativeLayout.RIGHT_OF, Icon.getId());
                NameParam.setMargins(0, MiscHandler.DpToPx(2), 0, 0);
                Name.setLayoutParams(NameParam);
                Name.setText(Search.Name);
                Name.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Name.setId(MiscHandler.GenerateViewID());

                MainView.addView(Name);

                TextView Position = new TextView(App.GetContext());
                RelativeLayout.LayoutParams PositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                PositionParam.addRule(RelativeLayout.RIGHT_OF, Icon.getId());
                PositionParam.addRule(RelativeLayout.BELOW, Name.getId());
                PositionParam.setMargins(0, MiscHandler.DpToPx(5), 0, 0);
                Position.setLayoutParams(PositionParam);
                Position.setText("(" + (float) Search.Lat + " , " + (float) Search.Lon + ")");

                MainView.addView(Position);

                return MainView;
            }
        }
    }




}*/
