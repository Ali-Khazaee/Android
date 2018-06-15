package co.biogram.main.ui.welcome;

import android.Manifest;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
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

import co.biogram.main.ui.component.CircleImageView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.ProgressDialog;
import co.biogram.main.ui.view.TextView;

public class DescriptionUI extends FragmentView {
    private final String Code;
    private final int Type;
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private CircleImageView CircleImageViewProfile;
    private RelativeLayout RelativeLayoutMain;
    private CropImageView CropImageViewMain;
    private File ProfileFile;
    private String Username;

    DescriptionUI(String code) {
        Code = code;
        Type = 2;
    }

    DescriptionUI(String code, String username, int type) {
        Code = code;
        Type = type;
        Username = username;
    }

    @Override
    public void OnCreate() {
        final Button ButtonFinish = new Button(Activity, 16, false);
        final LoadingView LoadingViewFinish = new LoadingView(Activity);

        RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.Primary);
        RelativeLayoutMain.setClickable(true);

        LayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int HeightDifference = 0;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != HeightDifference) {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    HeightDifference = DifferenceHeight;
                } else if (DifferenceHeight != HeightDifference) {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    HeightDifference = DifferenceHeight;
                } else if (HeightDifference != 0) {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(HeightDifference)));
                    HeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.Primary);
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(Misc.generateViewId());
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.z_general_back_white : R.drawable.z_general_back_white);
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.DescriptionUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(75), Misc.ToDP(75));
        CircleImageViewProfileParam.setMargins(Misc.ToDP(25), Misc.ToDP(25), Misc.ToDP(15), Misc.ToDP(25));
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        CircleImageViewProfile = new CircleImageView(Activity);
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.setId(Misc.generateViewId());
        CircleImageViewProfile.setImageResource(R.drawable.person_blue);
        //CircleImageViewProfile.SetBorderColor(R.color.Gray);
        //CircleImageViewProfile.SetBorderWidth(audio_start);
        CircleImageViewProfile.setPadding(Misc.ToDP(2), Misc.ToDP(2), Misc.ToDP(2), Misc.ToDP(2));
        CircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog DialogProfile = new Dialog(Activity);
                DialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogProfile.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.Primary);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(Activity, 16, true);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.SetColor(R.color.Primary);
                TextViewTitle.setText(Misc.String(R.string.DescriptionUIProfile));
                TextViewTitle.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewCamera = new TextView(Activity, 16, false);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.SetColor(R.color.Primary);
                TextViewCamera.setText(Misc.String(R.string.DescriptionUIProfileCamera));
                TextViewCamera.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogProfile.dismiss();

                        PermissionDialog PermissionDialogCamera = new PermissionDialog(Activity);
                        PermissionDialogCamera.SetContentView(R.drawable.z_general_permission_camera, R.string.DescriptionUIPermissionCamera, Manifest.permission.CAMERA, new PermissionDialog.OnChoiceListener() {
                            @Override
                            public void OnChoice(boolean Allow) {
                                if (!Allow) {
                                    Misc.ToastOld(Misc.String(R.string.DescriptionUIPermissionCamera));
                                    return;
                                }

                                Activity.GetManager().OpenView(new CameraViewUI(0, 0, false, null), "CameraViewUI", true);
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewCamera);

                View ViewLine2 = new View(Activity);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewGallery = new TextView(Activity, 16, false);
                TextViewGallery.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewGallery.SetColor(R.color.Primary);
                TextViewGallery.setText(Misc.String(R.string.DescriptionUIProfileGallery));
                TextViewGallery.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            DialogProfile.dismiss();

                            Activity.GetManager().OpenView(new GalleryViewUI(1, 1, new GalleryViewUI.GalleryListener() {
                                String ImageURL;

                                @Override
                                public void OnSelection(String URL) {
                                    ImageURL = URL;
                                }

                                @Override
                                public void OnRemove(String URL) {
                                    ImageURL = "";
                                }

                                @Override
                                public void OnSave() {

                                    Update(new File(ImageURL), true);
                                }
                            }), "GalleryViewUI", true);
                            return;
                        }

                        DialogProfile.dismiss();

                        PermissionDialog PermissionDialogGallery = new PermissionDialog(Activity);
                        PermissionDialogGallery.SetContentView(R.drawable.z_general_permission_storage, R.string.DescriptionUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener() {
                            @Override
                            public void OnChoice(boolean Allow) {
                                if (!Allow) {

                                    Misc.ToastOld(Misc.String(R.string.DescriptionUIPermissionStorage));
                                    return;
                                }

                                Activity.GetManager().OpenView(new GalleryViewUI(1, 1, new GalleryViewUI.GalleryListener() {
                                    String ImageURL;

                                    @Override
                                    public void OnSelection(String URL) {
                                        ImageURL = URL;
                                    }

                                    @Override
                                    public void OnRemove(String URL) {
                                        ImageURL = "";
                                    }

                                    @Override
                                    public void OnSave() {

                                        Update(new File(ImageURL), true);
                                    }
                                }), "GalleryViewUI", true);
                            }
                        });
                    }
                });

                LinearLayoutMain.addView(TextViewGallery);

                View ViewLine3 = new View(Activity);
                ViewLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine3.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine3);

                TextView TextViewRemove = new TextView(Activity, 16, false);
                TextViewRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewRemove.SetColor(R.color.Primary);
                TextViewRemove.setText(Misc.String(R.string.DescriptionUIProfileRemove));
                TextViewRemove.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageViewProfile.setImageResource(R.drawable.person_blue);
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
        LinearLayoutNameParam.setMargins(0, Misc.ToDP(30), Misc.ToDP(15), 0);
        LinearLayoutNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());

        LinearLayout LinearLayoutName = new LinearLayout(Activity);
        LinearLayoutName.setLayoutParams(LinearLayoutNameParam);
        LinearLayoutName.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutName.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(LinearLayoutName);

        RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNameParam.addRule(Misc.Align("L"));

        TextView TextViewName = new TextView(Activity, 16, false);
        TextViewName.setLayoutParams(TextViewNameParam);
        TextViewName.SetColor(R.color.Gray);
        TextViewName.setText(Misc.String(R.string.DescriptionUIName));

        LinearLayoutName.addView(TextViewName);

        final EditText EditTextName = new EditText(Activity);
        EditTextName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        EditTextName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextName.setFilters(new InputFilter[]
                {
                        new InputFilter.LengthFilter(32), new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de) {
                        for (int I = Start; I < End; I++) {
                            int Type = Character.getType(s.charAt(I));

                            if (Type == Character.SURROGATE || Type == Character.OTHER_SYMBOL)
                                return "";
                        }

                        return null;
                    }
                }
                });
        EditTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextName.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
        EditTextName.requestFocus();
        EditTextName.setPadding(0, -Misc.ToDP(2), 0, Misc.ToDP(5));
        EditTextName.setTypeface(Misc.GetTypeface());
        EditTextName.setHintTextColor(ContextCompat.getColor(Activity, R.color.Gray));
        EditTextName.setHint(Misc.String(R.string.DescriptionUIEditName));
        EditTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ButtonFinish.setEnabled(s.length() > 1);
            }
        });

        LinearLayoutName.addView(EditTextName);

        RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), 0);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, LinearLayoutName.getId());
        TextViewDescriptionParam.addRule(Misc.Align("R"));

        TextView TextViewDescription = new TextView(Activity, 16, false);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.SetColor(R.color.Gray);
        TextViewDescription.setText(Misc.String(R.string.GeneralDescription));
        TextViewDescription.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());

        final EditText EditTextDescription = new EditText(Activity);
        EditTextDescription.setLayoutParams(EditTextDescriptionParam);
        EditTextDescription.setId(Misc.generateViewId());
        EditTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(150)});
        EditTextDescription.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
        EditTextDescription.setHint(Misc.String(R.string.DescriptionUIEditDescription));
        EditTextDescription.setMaxLines(5);
        EditTextDescription.setMaxHeight(Misc.ToDP(100));
        EditTextDescription.setPadding(Misc.ToDP(3), -Misc.ToDP(2), Misc.ToDP(3), Misc.ToDP(5));
        EditTextDescription.setTypeface(Misc.GetTypeface());
        EditTextDescription.setHintTextColor(ContextCompat.getColor(Activity, R.color.Gray));
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        RelativeLayoutScroll.addView(EditTextDescription);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), 0);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());
        TextViewMessageParam.addRule(Misc.Align("R"));

        TextView TextViewMessage = new TextView(Activity, 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.SetColor(R.color.Primary);
        TextViewMessage.setText(Misc.String(R.string.DescriptionUIMessage));
        TextViewMessage.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(Activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(Misc.Align("R"));

        TextView TextViewPrivacy = new TextView(Activity, 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.SetColor(R.color.Primary);
        TextViewPrivacy.setText(Misc.String(R.string.GeneralTerm));
        TextViewPrivacy.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutFinishParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        RelativeLayoutFinishParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        RelativeLayoutFinishParam.addRule(Misc.Align("L"));

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(Activity, R.color.Primary));
        DrawableEnable.setCornerRadius(Misc.ToDP(7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(7));
        DrawableDisable.setColor(ContextCompat.getColor(Activity, R.color.Gray));

        StateListDrawable ListDrawableFinish = new StateListDrawable();
        ListDrawableFinish.addState(new int[]{android.R.attr.state_enabled}, DrawableEnable);
        ListDrawableFinish.addState(new int[]{-android.R.attr.state_enabled}, DrawableDisable);

        RelativeLayout RelativeLayoutFinish = new RelativeLayout(Activity);
        RelativeLayoutFinish.setLayoutParams(RelativeLayoutFinishParam);
        RelativeLayoutFinish.setBackground(DrawableEnable);

        RelativeLayoutBottom.addView(RelativeLayoutFinish);

        ButtonFinish.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35)));
        ButtonFinish.setText(Misc.String(R.string.DescriptionUIFinish));
        ButtonFinish.setBackground(ListDrawableFinish);
        ButtonFinish.setEnabled(false);
        ButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonFinish.setVisibility(View.GONE);
                LoadingViewFinish.Start();

                final ProgressDialog Progress = new ProgressDialog(Activity);
                Progress.setMessage(Misc.String(R.string.DescriptionUIUpload));
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                if (Type == 0) {
                    AndroidNetworking.upload(Misc.GetRandomServer("SignInGoogleVerify"))
                            .addMultipartParameter("Token", Code)
                            .addMultipartParameter("Name", EditTextName.getText().toString())
                            .addMultipartParameter("Username", Username)
                            .addMultipartParameter("Description", EditTextDescription.getText().toString())
                            .addMultipartParameter("Session", Misc.GenerateSession())
                            .addMultipartFile("Avatar", ProfileFile)
                            .setTag("DescriptionUI")
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long u, long t) {
                                    Progress.setProgress((int) (100 * u / t));
                                }
                            })
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String Response) {
                                    Progress.dismiss();
                                    LoadingViewFinish.Stop();
                                    ButtonFinish.setVisibility(View.VISIBLE);

                                    try {
                                        JSONObject Result = new JSONObject(Response);

                                        switch (Result.getInt("Message")) {
                                            case 0:
                                                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                                Anim.setDuration(200);

                                                RelativeLayoutMain.setAnimation(Anim);

                                                Misc.SetBoolean("IsLogin", true);
                                                Misc.SetBoolean("IsGoogle", true);
                                                Misc.SetString("Token", Result.getString("Token"));
                                                Misc.SetString("ID", Result.getString("ID"));
                                                Misc.SetString("Username", Result.getString("Username"));
                                                Misc.SetString("Avatar", Result.getString("Avatar"));

                                                Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                                Activity.finish();
                                                break;
                                            case 13:
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUIUsernameUnavailable));
                                                break;
                                            case 6:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameEmpty));
                                                break;
                                            case 7:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameLess));
                                                break;
                                            case 8:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameGreater));
                                                break;
                                            case 1:
                                            case 9:
                                            case 10:
                                            case 11:
                                            case 12:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUICode));
                                                break;
                                            default:
                                                Misc.GeneralError(Result.getInt("Message"));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        Misc.Debug("DescriptionUI-SignInGoogleVerify: " + e.toString());
                                    }
                                }

                                @Override
                                public void onError(ANError e) {
                                    Progress.dismiss();
                                    LoadingViewFinish.Stop();
                                    ButtonFinish.setVisibility(View.VISIBLE);
                                    Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                                }
                            });
                } else if (Type == 1) {
                    AndroidNetworking.upload(Misc.GetRandomServer("SignUpPhoneFinish"))
                            .addMultipartParameter("Issue", Code)
                            .addMultipartParameter("Name", EditTextName.getText().toString())
                            .addMultipartParameter("Username", Username)
                            .addMultipartParameter("Description", EditTextDescription.getText().toString())
                            .addMultipartParameter("Session", Misc.GenerateSession())
                            .addMultipartFile("Avatar", ProfileFile)
                            .setTag("DescriptionUI")
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long u, long t) {
                                    Progress.setProgress((int) (100 * u / t));
                                }
                            })
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String Response) {
                                    Progress.dismiss();
                                    LoadingViewFinish.Stop();
                                    ButtonFinish.setVisibility(View.VISIBLE);

                                    try {
                                        JSONObject Result = new JSONObject(Response);

                                        switch (Result.getInt("Message")) {
                                            case 0:
                                                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                                Anim.setDuration(200);

                                                RelativeLayoutMain.setAnimation(Anim);

                                                Misc.SetBoolean("IsLogin", true);
                                                Misc.SetBoolean("IsGoogle", true);
                                                Misc.SetString("Token", Result.getString("Token"));
                                                Misc.SetString("ID", Result.getString("ID"));
                                                Misc.SetString("Username", Result.getString("Username"));
                                                Misc.SetString("Avatar", Result.getString("Avatar"));

                                                Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                                Activity.finish();
                                                break;
                                            case 1:
                                            case 9:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUICode));
                                                break;
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                            case 10:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUIUsernameUnavailable));
                                                break;
                                            case 6:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameEmpty));
                                                break;
                                            case 7:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameLess));
                                                break;
                                            case 8:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameGreater));
                                                break;
                                            default:
                                                Misc.GeneralError(Result.getInt("Message"));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        Misc.Debug("DescriptionUI-SignUpPhoneFinish: " + e.toString());
                                    }
                                }

                                @Override
                                public void onError(ANError e) {
                                    Progress.dismiss();
                                    LoadingViewFinish.Stop();
                                    ButtonFinish.setVisibility(View.VISIBLE);
                                    Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                                }
                            });
                } else if (Type == 2) {
                    AndroidNetworking.upload(Misc.GetRandomServer("SignUpEmailFinish"))
                            .addMultipartParameter("Issue", Code)
                            .addMultipartParameter("Name", EditTextName.getText().toString())
                            .addMultipartParameter("Description", EditTextDescription.getText().toString())
                            .addMultipartParameter("Session", Misc.GenerateSession())
                            .addMultipartFile("Avatar", ProfileFile)
                            .setTag("DescriptionUI")
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long u, long t) {
                                    Progress.setProgress((int) (100 * u / t));
                                }
                            })
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String Response) {
                                    Progress.dismiss();
                                    LoadingViewFinish.Stop();
                                    ButtonFinish.setVisibility(View.VISIBLE);

                                    try {
                                        JSONObject Result = new JSONObject(Response);

                                        switch (Result.getInt("Message")) {
                                            case 0:
                                                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                                Anim.setDuration(200);

                                                RelativeLayoutMain.setAnimation(Anim);

                                                Misc.SetBoolean("IsLogin", true);
                                                Misc.SetBoolean("IsGoogle", true);
                                                Misc.SetString("Token", Result.getString("Token"));
                                                Misc.SetString("ID", Result.getString("ID"));
                                                Misc.SetString("Username", Result.getString("Username"));
                                                Misc.SetString("Avatar", Result.getString("Avatar"));

                                                Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                                Activity.finish();
                                                break;
                                            case 1:
                                            case 9:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUICode));
                                                break;
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                            case 10:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUIUsernameUnavailable));
                                                break;
                                            case 6:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameEmpty));
                                                break;
                                            case 7:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameLess));
                                                break;
                                            case 8:
                                                Misc.ToastOld(Misc.String(R.string.DescriptionUINameGreater));
                                                break;
                                            default:
                                                Misc.GeneralError(Result.getInt("Message"));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        Misc.Debug("DescriptionUI-SignUpEmailFinish: " + e.toString());
                                    }
                                }

                                @Override
                                public void onError(ANError e) {
                                    Progress.dismiss();
                                    LoadingViewFinish.Stop();
                                    ButtonFinish.setVisibility(View.VISIBLE);
                                    Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                                }
                            });
                }
            }
        });

        RelativeLayoutFinish.addView(ButtonFinish);

        RelativeLayout.LayoutParams LoadingViewFinishParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        LoadingViewFinishParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewFinish.setLayoutParams(LoadingViewFinishParam);
        LoadingViewFinish.SetColor(R.color.Primary);

        RelativeLayoutFinish.addView(LoadingViewFinish);

        CropImageViewMain = new CropImageView(Activity);
        CropImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        CropImageViewMain.setFixedAspectRatio(true);
        CropImageViewMain.setBackgroundResource(R.color.Primary);
        CropImageViewMain.setAutoZoomEnabled(true);
        CropImageViewMain.setAspectRatio(1, 1);
        CropImageViewMain.setVisibility(View.GONE);

        RelativeLayoutMain.addView(CropImageViewMain);

        RelativeLayout RelativeLayoutCrop = new RelativeLayout(Activity);
        RelativeLayoutCrop.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));

        CropImageViewMain.addView(RelativeLayoutCrop);

        RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewDoneParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewDone = new ImageView(Activity);
        ImageViewDone.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewDone.setLayoutParams(ImageViewDoneParam);
        ImageViewDone.setImageResource(R.drawable.___general_done_white);
        ImageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 20)
                    Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.Primary));

                try {
                    CropImageViewMain.setVisibility(View.GONE);

                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                    CropImageViewMain.getCroppedImage(250, 250).compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                    File ProfileFile = new File(Misc.Temp(), (String.valueOf(System.currentTimeMillis()) + "_imagepreview_crop.jpg"));

                    FileOutputStream FOS = new FileOutputStream(ProfileFile);
                    FOS.write(BAOS.toByteArray());
                    FOS.flush();
                    FOS.close();

                    Update(ProfileFile, false);
                } catch (Exception e) {
                    Misc.Debug("DescriptionUI-Crop: " + e.toString());
                }
            }
        });

        RelativeLayoutCrop.addView(ImageViewDone);

        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Misc.ShowSoftKey(EditTextName);
            }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(LayoutListener);
    }

    @Override
    public void OnPause() {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("DescriptionUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
    }

    public void Update(File file, boolean Crop) {
        if (file == null)
            return;

        if (Crop) {
            if (Build.VERSION.SDK_INT > 20)
                Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.Primary));

            CropImageViewMain.setVisibility(View.VISIBLE);
            CropImageViewMain.setImageUriAsync(Uri.fromFile(file));
            return;
        }

        CircleImageViewProfile.setImageURI(Uri.parse(file.getAbsolutePath()));
        ProfileFile = file;
    }
}
