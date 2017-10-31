package co.biogram.main.ui.welcome;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import java.io.File;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.TextView;

public class SignUpDescriptionUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private CircleImageView CircleImageViewProfile;
    private RelativeLayout RelativeLayoutMain;

    private int HeightDifference = 0;
    private File ProfileFile;
    private String Username;
    private String Password;
    private String Email;
    private String Token;
    private int Type;
    private String Code;

    SignUpDescriptionUI(String token, String username, int type)
    {
        Type = type;
        Token = token;
        Username = username;
    }

    @Override
    public void OnCreate()
    {
        final Button ButtonFinish = new Button(GetActivity(), 16, false);
        final LoadingView LoadingViewFinish = new LoadingView(GetActivity());

        RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (HeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(HeightDifference)));
                    HeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.SignUpDescription));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(GetActivity());
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(GetActivity());
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 75), MiscHandler.ToDimension(GetActivity(), 75));
        CircleImageViewProfileParam.setMargins(MiscHandler.ToDimension(GetActivity(), 25), MiscHandler.ToDimension(GetActivity(), 25), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 25));
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        CircleImageViewProfile = new CircleImageView(GetActivity());
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.setId(MiscHandler.GenerateViewID());
        CircleImageViewProfile.setImageResource(R.drawable.ic_person_blue);
        CircleImageViewProfile.setBorderColor(R.color.Gray);
        CircleImageViewProfile.setBorderWidth(2);
        CircleImageViewProfile.setPadding(MiscHandler.ToDimension(GetActivity(), 2), MiscHandler.ToDimension(GetActivity(), 2), MiscHandler.ToDimension(GetActivity(), 2), MiscHandler.ToDimension(GetActivity(), 2));
        CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogProfile = new Dialog(GetActivity());
                DialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogProfile.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(GetActivity());
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(GetActivity(), 16, true);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewTitle.setText(GetActivity().getString(R.string.SignUpDescriptionProfile));
                TextViewTitle.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewCamera = new TextView(GetActivity(), 16, false);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewCamera.setText(GetActivity().getString(R.string.SignUpDescriptionProfileCamera));
                TextViewCamera.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (MiscHandler.HasPermission(GetActivity(), Manifest.permission.CAMERA))
                        {
                            DialogProfile.dismiss();
                            GetActivity().GetManager().OpenView(new CameraViewUI(), R.id.WelcomeActivityContainer, "CameraViewUI");
                            return;
                        }

                        PermissionDialog PermissionDialogCamera = new PermissionDialog(GetActivity());
                        PermissionDialogCamera.SetContentView(R.drawable.ic_permission_camera, GetActivity().getString(R.string.SignUpDescriptionPermissionCamera), new PermissionDialog.OnSelectedListener()
                        {
                            @Override
                            public void OnSelected(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    DialogProfile.dismiss();
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpDescriptionPermissionCamera));
                                    return;
                                }

                                GetActivity().RequestPermission(Manifest.permission.CAMERA, new FragmentActivity.OnPermissionListener()
                                {
                                    @Override
                                    public void OnGranted()
                                    {
                                        DialogProfile.dismiss();
                                        GetActivity().GetManager().OpenView(new CameraViewUI(), R.id.WelcomeActivityContainer, "CameraViewUI");
                                    }

                                    @Override
                                    public void OnDenied()
                                    {
                                        DialogProfile.dismiss();
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpDescriptionPermissionCamera));
                                    }
                                });
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewCamera);

                View ViewLine2 = new View(GetActivity());
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewGallery = new TextView(GetActivity(), 16, false);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewGallery.setText(GetActivity().getString(R.string.SignUpDescriptionProfileGallery));
                TextViewGallery.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (MiscHandler.HasPermission(GetActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
                        {
                            DialogProfile.dismiss();
                            GetActivity().GetManager().OpenView(new GalleryViewUI(1, false), R.id.WelcomeActivityContainer, "GalleryViewUI");
                            return;
                        }

                        PermissionDialog PermissionDialogCamera = new PermissionDialog(GetActivity());
                        PermissionDialogCamera.SetContentView(R.drawable.ic_permission_storage, GetActivity().getString(R.string.SignUpDescriptionPermissionStorage), new PermissionDialog.OnSelectedListener()
                        {
                            @Override
                            public void OnSelected(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    DialogProfile.dismiss();
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpDescriptionPermissionStorage));
                                    return;
                                }

                                GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                                {
                                    @Override
                                    public void OnGranted()
                                    {
                                        DialogProfile.dismiss();
                                        GetActivity().GetManager().OpenView(new GalleryViewUI(1, false), R.id.WelcomeActivityContainer, "GalleryViewUI");
                                    }

                                    @Override
                                    public void OnDenied()
                                    {
                                        DialogProfile.dismiss();
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpDescriptionPermissionStorage));
                                    }
                                });
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewGallery);

                View ViewLine3 = new View(GetActivity());
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine3);

                TextView TextViewRemove = new TextView(GetActivity(), 16, false);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewRemove.setText(GetActivity().getString(R.string.SignUpDescriptionProfileRemove));
                TextViewRemove.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
                TextViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CircleImageViewProfile.setImageResource(R.drawable.ic_person_blue);
                        DialogProfile.dismiss();
                        ProfileFile = null;
                    }
                });

                LinearLayoutMain.addView(TextViewRemove);

                DialogProfile.setContentView(LinearLayoutMain);
                DialogProfile.show();
            }
        });

        RelativeLayoutScroll.addView(CircleImageViewProfile);

        RelativeLayout.LayoutParams LinearLayoutNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutNameParam.setMargins(0, MiscHandler.ToDimension(GetActivity(), 30), MiscHandler.ToDimension(GetActivity(), 15), 0);
        LinearLayoutNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());

        LinearLayout LinearLayoutName = new LinearLayout(GetActivity());
        LinearLayoutName.setLayoutParams(LinearLayoutNameParam);
        LinearLayoutName.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutName.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutName);

        TextView TextViewName = new TextView(GetActivity(), 16, true);
        TextViewName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewName.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewName.setText(GetActivity().getString(R.string.SignUpDescriptionName));
        TextViewName.setGravity(MiscHandler.Gravity("R"));

        LinearLayoutName.addView(TextViewName);

        final EditText EditTextName = new EditText(GetActivity());
        EditTextName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        EditTextName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextName.setFilters(new InputFilter[]
        {
            new InputFilter.LengthFilter(32), new InputFilter()
            {
                @Override
                public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de)
                {
                    for (int I = Start; I < End; I++)
                    {
                        int Type = Character.getType(s.charAt(I));

                        if (Type == Character.SURROGATE || Type == Character.OTHER_SYMBOL)
                            return "";
                    }

                    return null;
                }
            }
        });
        EditTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextName.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextName.requestFocus();
        EditTextName.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray3));
        EditTextName.setHint(GetActivity().getString(R.string.SignUpDescriptionEditName));
        EditTextName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 1)
                    ButtonFinish.setEnabled(true);
                else
                    ButtonFinish.setEnabled(false);
            }
        });

        LinearLayoutName.addView(EditTextName);

        RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), 0);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, LinearLayoutName.getId());
        TextViewDescriptionParam.addRule(MiscHandler.Align("R"));

        TextView TextViewDescription = new TextView(GetActivity(), 16, true);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewDescription.setText(GetActivity().getString(R.string.SignUpDescriptionInfo));
        TextViewDescription.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());

        final EditText EditTextDescription = new EditText(GetActivity());
        EditTextDescription.setLayoutParams(EditTextDescriptionParam);
        EditTextDescription.setId(MiscHandler.GenerateViewID());
        EditTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextDescription.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextDescription.setHint(GetActivity().getString(R.string.SignUpDescriptionEditDescription));
        EditTextDescription.setMaxLines(5);
        EditTextDescription.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray3));
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        RelativeLayoutScroll.addView(EditTextDescription);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), 0);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setText(GetActivity().getString(R.string.SignUpDescriptionMessage));
        TextViewMessage.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPrivacy = new TextView(GetActivity(), 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        TextViewPrivacy.setText(GetActivity().getString(R.string.SignUpDescriptionTerm));
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        RelativeLayoutFinishParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        RelativeLayoutFinishParam.addRule(MiscHandler.Align("L"));

        GradientDrawable DrawableFinish = new GradientDrawable();
        DrawableFinish.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        DrawableFinish.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));

        GradientDrawable DrawableFinish2 = new GradientDrawable();
        DrawableFinish2.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));
        DrawableFinish2.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable ListDrawableFinish = new StateListDrawable();
        ListDrawableFinish.addState(new int[] { android.R.attr.state_enabled }, DrawableFinish);
        ListDrawableFinish.addState(new int[] { -android.R.attr.state_enabled }, DrawableFinish2);

        RelativeLayout RelativeLayoutFinish = new RelativeLayout(GetActivity());
        RelativeLayoutFinish.setLayoutParams(RelativeLayoutFinishParam);
        RelativeLayoutFinish.setBackground(DrawableFinish);

        RelativeLayoutBottom.addView(RelativeLayoutFinish);

        ButtonFinish.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35)));
        ButtonFinish.setTextColor(ContextCompat.getColor(GetActivity(), R.color.White));
        ButtonFinish.setText(GetActivity().getString(R.string.SignUpDescriptionFinish));
        ButtonFinish.setBackground(ListDrawableFinish);
        ButtonFinish.setPadding(0, 0, 0, 0);
        ButtonFinish.setEnabled(false);
        ButtonFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonFinish.setVisibility(View.GONE);
                LoadingViewFinish.Start();

                final ProgressDialog Progress = new ProgressDialog(GetActivity());
                Progress.setMessage(GetActivity().getString(R.string.SignUpDescriptionUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                 if (Type == 0)
                 {
                     AndroidNetworking.upload(MiscHandler.GetRandomServer("SignInGoogleVerify"))
                     .addMultipartParameter("Token", Token)
                     .addMultipartParameter("Name", EditTextName.getText().toString())
                     .addMultipartParameter("Username", Username)
                     .addMultipartParameter("Description", EditTextDescription.getText().toString())
                     .addMultipartParameter("Session", MiscHandler.GenerateSession())
                     .addMultipartFile("Avatar", ProfileFile)
                     .setTag("SignUpDescriptionUI")
                     .build()
                     .setUploadProgressListener(new UploadProgressListener()
                     {
                         @Override
                         public void onProgress(long u, long t)
                         {
                             Progress.setProgress((int) (100 * u / t));
                         }
                     })
                     .getAsString(new StringRequestListener()
                     {
                         @Override
                         public void onResponse(String Response)
                         {
                             Progress.dismiss();
                             LoadingViewFinish.Stop();
                             ButtonFinish.setVisibility(View.VISIBLE);

                             MiscHandler.Debug(Response);

                             try
                             {
                                 JSONObject Result = new JSONObject(Response);

                                 switch (Result.getInt("Message"))
                                 {
                                     case -7:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError7));
                                         break;
                                     case -6:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError6));
                                         break;
                                     case -2:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError2));
                                         break;
                                     case -1:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError1));
                                         break;
                                     case 0:
                                         TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                         Anim.setDuration(200);

                                         RelativeLayoutMain.setAnimation(Anim);

                                         SharedHandler.SetBoolean(GetActivity(), "IsLogin", true);
                                         SharedHandler.SetString(GetActivity(), "Token", Result.getString("Token"));
                                         SharedHandler.SetString(GetActivity(), "ID", Result.getString("ID"));
                                         SharedHandler.SetString(GetActivity(), "Username", Result.getString("Username"));
                                         SharedHandler.SetString(GetActivity(), "Avatar", Result.getString("Avatar"));

                                         GetActivity().startActivity(new Intent(GetActivity(), SocialActivity.class));
                                         GetActivity().finish();
                                         break;
                                     case 1:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError1));
                                         break;
                                     case 2:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError3));
                                         break;
                                     case 3:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError4));
                                         break;
                                     case 4:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError4));
                                         break;
                                     case 5:
                                         MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError5));
                                         break;
                                 }
                             }
                             catch (Exception e)
                             {
                                 MiscHandler.Debug("SignUpDescriptionUI-SignInGoogleVerify: " + e.toString());
                             }
                         }

                         @Override
                         public void onError(ANError e)
                         {
                             Progress.dismiss();
                             LoadingViewFinish.Stop();
                             ButtonFinish.setVisibility(View.VISIBLE);
                             MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                         }
                     });
                 }
                else if (Type == 1)
                {
                    AndroidNetworking.upload(MiscHandler.GetRandomServer("SignInGoogleVerify"))
                    .addMultipartParameter("Token", Token)
                    .addMultipartParameter("Name", EditTextName.getText().toString())
                    .addMultipartParameter("Username", Username)
                    .addMultipartParameter("Description", EditTextDescription.getText().toString())
                    .addMultipartParameter("Session", MiscHandler.GenerateSession())
                    .addMultipartFile("Avatar", ProfileFile)
                    .setTag("SignUpDescriptionUI")
                    .build()
                    .setUploadProgressListener(new UploadProgressListener()
                    {
                        @Override
                        public void onProgress(long u, long t)
                        {
                            Progress.setProgress((int) (100 * u / t));
                        }
                    })
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setVisibility(View.VISIBLE);

                            MiscHandler.Debug(Response);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                switch (Result.getInt("Message"))
                                {
                                    case -7:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError7));
                                        break;
                                    case -6:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError6));
                                        break;
                                    case -2:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError2));
                                        break;
                                    case -1:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError1));
                                        break;
                                    case 0:
                                        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                        Anim.setDuration(200);

                                        RelativeLayoutMain.setAnimation(Anim);

                                        SharedHandler.SetBoolean(GetActivity(), "IsLogin", true);
                                        SharedHandler.SetString(GetActivity(), "Token", Result.getString("Token"));
                                        SharedHandler.SetString(GetActivity(), "ID", Result.getString("ID"));
                                        SharedHandler.SetString(GetActivity(), "Username", Result.getString("Username"));
                                        SharedHandler.SetString(GetActivity(), "Avatar", Result.getString("Avatar"));

                                        GetActivity().startActivity(new Intent(GetActivity(), SocialActivity.class));
                                        GetActivity().finish();
                                        break;
                                    case 1:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError1));
                                        break;
                                    case 2:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError3));
                                        break;
                                    case 3:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError4));
                                        break;
                                    case 4:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError4));
                                        break;
                                    case 5:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpUsernameError5));
                                        break;
                                }
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("SignUpDescriptionUI-SignInGoogleVerify: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                        }
                    });
                }
            }
        });

        RelativeLayoutFinish.addView(ButtonFinish);

        RelativeLayout.LayoutParams LoadingViewFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        LoadingViewFinishParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewFinish.setLayoutParams(LoadingViewFinishParam);
        LoadingViewFinish.SetColor(R.color.White);

        RelativeLayoutFinish.addView(LoadingViewFinish);

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { MiscHandler.ShowSoftKey(EditTextName); }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);
    }

    @Override
    public void OnPause()
    {
        MiscHandler.HideSoftKey(GetActivity());
        AndroidNetworking.forceCancel("SignUpDescriptionUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
    }

    public void Update(File file)
    {
        CircleImageViewProfile.setImageURI(Uri.parse(file.getAbsolutePath()));
        ProfileFile = file;
    }
}
