package co.biogram.main.ui;

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
import android.view.Gravity;
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
import co.biogram.main.handler.MiscHandler;

class SignUpDescription extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private int RelativeLayoutMainHeightDifference = 0;
    private CircleImageView CircleImageViewProfile;
    private RelativeLayout RelativeLayoutMain;

    private String Code;
    private String Username;
    private File ProfileFile;

    SignUpDescription(String code, String username)
    {
        Code = code;
        Username = username;
    }

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();
        final Button ButtonFinish = new Button(activity, false);
        final LoadingView LoadingViewFinish = new LoadingView(activity);

        RelativeLayoutMain = new RelativeLayout(activity);
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

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != RelativeLayoutMainHeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    RelativeLayoutMainHeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    RelativeLayoutMainHeightDifference = DifferenceHeight;
                }
                else if (RelativeLayoutMainHeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                    RelativeLayoutMainHeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setImageResource(MiscHandler.IsFa() ? R.drawable.ic_back_white_fa : R.drawable.ic_back_white);
        ImageViewBack.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(activity, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTitle.setText(activity.getString(R.string.SignUpDescription));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 75), MiscHandler.ToDimension(activity, 75));
        CircleImageViewProfileParam.setMargins(MiscHandler.ToDimension(activity, 25), MiscHandler.ToDimension(activity, 25), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 25));
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        CircleImageViewProfile = new CircleImageView(activity);
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.setId(MiscHandler.GenerateViewID());
        CircleImageViewProfile.setCircleBackgroundColor(R.color.BlueLight);
        CircleImageViewProfile.setImageResource(R.drawable.ic_person_blue);
        CircleImageViewProfile.setBorderColor(R.color.Gray);
        CircleImageViewProfile.setBorderWidth(2);
        CircleImageViewProfile.setPadding(MiscHandler.ToDimension(activity, 2), MiscHandler.ToDimension(activity, 2), MiscHandler.ToDimension(activity, 2), MiscHandler.ToDimension(activity, 2));
        CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogProfile = new Dialog(activity);
                DialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogProfile.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(activity, true);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewTitle.setText(activity.getString(R.string.SignUpDescriptionProfile));
                TextViewTitle.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewCamera = new TextView(activity, false);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewCamera.setText(activity.getString(R.string.SignUpDescriptionProfileCamera));
                TextViewCamera.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
                TextViewCamera.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (MiscHandler.HasPermission(activity, Manifest.permission.CAMERA))
                        {
                            DialogProfile.dismiss();
                            GetActivity().GetManager().OpenView(new CameraView(), R.id.WelcomeActivityContainer, "CameraView");
                            return;
                        }

                        PermissionDialog PermissionDialogCamera = new PermissionDialog(activity);
                        PermissionDialogCamera.SetContentView(R.drawable.ic_permission_camera, activity.getString(R.string.SignUpDescriptionPermissionCamera), new PermissionDialog.OnSelectedListener()
                        {
                            @Override
                            public void OnSelected(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    DialogProfile.dismiss();
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpDescriptionPermissionCamera));
                                    return;
                                }

                                GetActivity().RequestPermission(Manifest.permission.CAMERA, new FragmentActivity.OnPermissionListener()
                                {
                                    @Override
                                    public void OnGranted()
                                    {
                                        DialogProfile.dismiss();
                                        GetActivity().GetManager().OpenView(new CameraView(), R.id.WelcomeActivityContainer, "CameraView");
                                    }

                                    @Override
                                    public void OnDenied()
                                    {
                                        DialogProfile.dismiss();
                                        MiscHandler.Toast(activity, activity.getString(R.string.SignUpDescriptionPermissionCamera));
                                    }
                                });
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewCamera);

                View ViewLine2 = new View(activity);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewGallery = new TextView(activity, false);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewGallery.setText(activity.getString(R.string.SignUpDescriptionProfileGallery));
                TextViewGallery.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
                TextViewGallery.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (MiscHandler.HasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
                        {
                            DialogProfile.dismiss();
                            GetActivity().GetManager().OpenView(new GalleryView(1, false), R.id.WelcomeActivityContainer, "GalleryView");
                            return;
                        }

                        PermissionDialog PermissionDialogCamera = new PermissionDialog(activity);
                        PermissionDialogCamera.SetContentView(R.drawable.ic_permission_storage, activity.getString(R.string.SignUpDescriptionPermissionStorage), new PermissionDialog.OnSelectedListener()
                        {
                            @Override
                            public void OnSelected(boolean Allow)
                            {
                                if (!Allow)
                                {
                                    DialogProfile.dismiss();
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpDescriptionPermissionStorage));
                                    return;
                                }

                                GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                                {
                                    @Override
                                    public void OnGranted()
                                    {
                                        DialogProfile.dismiss();
                                        GetActivity().GetManager().OpenView(new GalleryView(1, false), R.id.WelcomeActivityContainer, "GalleryView");
                                    }

                                    @Override
                                    public void OnDenied()
                                    {
                                        DialogProfile.dismiss();
                                        MiscHandler.Toast(activity, activity.getString(R.string.SignUpDescriptionPermissionStorage));
                                    }
                                });
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewGallery);

                View ViewLine3 = new View(activity);
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine3);

                TextView TextViewRemove = new TextView(activity, false);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewRemove.setText(activity.getString(R.string.SignUpDescriptionProfileRemove));
                TextViewRemove.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
                TextViewRemove.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
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
        LinearLayoutNameParam.setMargins(0, MiscHandler.ToDimension(activity, 30), MiscHandler.ToDimension(activity, 15), 0);
        LinearLayoutNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());

        LinearLayout LinearLayoutName = new LinearLayout(activity);
        LinearLayoutName.setLayoutParams(LinearLayoutNameParam);
        LinearLayoutName.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutName.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutName);

        TextView TextViewName = new TextView(activity, true);
        TextViewName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewName.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewName.setText(activity.getString(R.string.SignUpDescriptionName));
        TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewName.setGravity(Gravity.START);

        LinearLayoutName.addView(TextViewName);

        final EditText EditTextName = new EditText(activity);
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
                        int type = Character.getType(s.charAt(I));

                        if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL)
                            return "";
                    }

                    return null;
                }
            }
        });
        EditTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextName.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextName.requestFocus();
        EditTextName.setHintTextColor(ContextCompat.getColor(activity, R.color.Gray3));
        EditTextName.setHint(activity.getString(R.string.SignUpDescriptionEditName));
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
        TextViewDescriptionParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), 0);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, LinearLayoutName.getId());
        TextViewDescriptionParam.addRule(MiscHandler.Align("R"));

        TextView TextViewDescription = new TextView(activity, true);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewDescription.setText(activity.getString(R.string.SignUpDescriptionInfo));
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());

        EditText EditTextDescription = new EditText(activity);
        EditTextDescription.setLayoutParams(EditTextDescriptionParam);
        EditTextDescription.setId(MiscHandler.GenerateViewID());
        EditTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextDescription.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextDescription.setHint(activity.getString(R.string.SignUpDescriptionEditDescription));
        EditTextDescription.setMaxLines(5);
        EditTextDescription.setHintTextColor(ContextCompat.getColor(activity, R.color.Gray3));
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        RelativeLayoutScroll.addView(EditTextDescription);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), 0);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(activity, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
        TextViewMessage.setText(activity.getString(R.string.SignUpDescriptionMessage));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPrivacy = new TextView(activity, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
        TextViewPrivacy.setText(activity.getString(R.string.SignUpDescriptionTerm));
        TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        RelativeLayoutFinishParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        RelativeLayoutFinishParam.addRule(MiscHandler.Align("L"));

        GradientDrawable GradientDrawableFinish = new GradientDrawable();
        GradientDrawableFinish.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
        GradientDrawableFinish.setCornerRadius(MiscHandler.ToDimension(activity, 7));

        GradientDrawable GradientDrawableFinishDisable = new GradientDrawable();
        GradientDrawableFinishDisable.setCornerRadius(MiscHandler.ToDimension(activity, 7));
        GradientDrawableFinishDisable.setColor(ContextCompat.getColor(activity, R.color.Gray2));

        StateListDrawable StateListDrawableNext = new StateListDrawable();
        StateListDrawableNext.addState(new int[] { android.R.attr.state_enabled }, GradientDrawableFinish);
        StateListDrawableNext.addState(new int[] { -android.R.attr.state_enabled }, GradientDrawableFinishDisable);

        RelativeLayout RelativeLayoutFinish = new RelativeLayout(activity);
        RelativeLayoutFinish.setLayoutParams(RelativeLayoutFinishParam);
        RelativeLayoutFinish.setBackground(GradientDrawableFinish);

        RelativeLayoutBottom.addView(RelativeLayoutFinish);

        ButtonFinish.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35)));
        ButtonFinish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ButtonFinish.setTextColor(ContextCompat.getColor(activity, R.color.White));
        ButtonFinish.setText(activity.getString(R.string.SignUpDescriptionFinish));
        ButtonFinish.setBackground(StateListDrawableNext);
        ButtonFinish.setPadding(0, 0, 0, 0);
        ButtonFinish.setEnabled(false);
        ButtonFinish.setAllCaps(false);
        ButtonFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonFinish.setVisibility(View.GONE);
                LoadingViewFinish.Start();

                final ProgressDialog Progress = new ProgressDialog(activity);
                Progress.setMessage(activity.getString(R.string.SignUpDescriptionUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                AndroidNetworking.upload(MiscHandler.GetRandomServer("RegisterPhone"))
                .addMultipartParameter("Code", Code)
                .addMultipartParameter("Username", Username)
                .addMultipartFile("Avatar", ProfileFile)
                .setTag("SignUpDescription")
                .build()
                .setUploadProgressListener(new UploadProgressListener()
                {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes)
                    {
                        Progress.setProgress((int) (100 * bytesUploaded / totalBytes));
                    }
                })
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        LoadingViewFinish.Stop();
                        ButtonFinish.setVisibility(View.VISIBLE);

                        MiscHandler.Debug(Response);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            switch (Result.getInt("Message"))
                            {
                                case -2:
                                    MiscHandler.Toast(activity, activity.getString(R.string.GeneralError2));
                                    break;
                                case -1:
                                    MiscHandler.Toast(activity, activity.getString(R.string.GeneralError1));
                                    break;
                                case 0:
                                    TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                    Anim.setDuration(200);

                                    RelativeLayoutMain.setAnimation(Anim);

                                    MiscHandler.Toast(activity, "Done switch me to ");
                                    //GetActivity().GetManager().OpenView(new SignUpDescription(Code, EditTextUsername.getText().toString()), R.id.WelcomeActivityContainer, "SignUpDescription");
                                    break;
                                case 1:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpUsernameError1));
                                    break;
                                case 2:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpUsernameError3));
                                    break;
                                case 3:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpUsernameError4));
                                    break;
                                case 4:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpUsernameError4));
                                    break;
                                case 5:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpUsernameError5));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignUpDescription-Register: " + e.toString());
                        }

                        Progress.dismiss();
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        Progress.dismiss();
                        LoadingViewFinish.Stop();
                        ButtonFinish.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(activity, activity.getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutFinish.addView(ButtonFinish);

        RelativeLayout.LayoutParams LoadingViewFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
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
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignUpDescription");
        MiscHandler.HideSoftKey(GetActivity());
    }

    void Update(File file)
    {
        CircleImageViewProfile.setImageURI(Uri.parse(file.getAbsolutePath()));
        ProfileFile = file;
    }
}
