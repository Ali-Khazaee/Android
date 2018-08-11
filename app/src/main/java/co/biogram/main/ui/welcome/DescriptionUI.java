package co.biogram.main.ui.welcome;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.*;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class DescriptionUI extends FragmentView
{
    private final String Code;
    private final int Type;
    private CircleImageView CircleImageViewProfile;
    private CropImageView CropImageViewMain;
    private File ProfileFile;
    private String Username;

    DescriptionUI(String code)
    {
        Code = code;
        Type = 2;
    }

    DescriptionUI(String code, String username, int type)
    {
        Code = code;
        Type = type;
        Username = username;
    }

    @Override
    public void OnCreate()
    {

        View view = View.inflate(Activity, R.layout.welcome_description, null);

        final Button ButtonFinish = view.findViewById(R.id.buttonFinish);
        final LoadingView LoadingViewFinish = new LoadingView(Activity);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        CircleImageViewProfile = view.findViewById(R.id.ImageViewProfile);
        CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogProfile = new Dialog(Activity);
                DialogProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogProfile.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.TextDark);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(Activity, 16, true);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.SetColor(R.color.TextWhite);
                TextViewTitle.setText(Misc.String(R.string.DescriptionUIProfile));
                TextViewTitle.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewCamera = new TextView(Activity, 16, false);
                TextViewCamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCamera.SetColor(R.color.TextWhite);
                TextViewCamera.setText(Misc.String(R.string.DescriptionUIProfileCamera));
                TextViewCamera.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        DialogProfile.dismiss();

                        PermissionDialog PermissionDialogCamera = new PermissionDialog(Activity);
                        PermissionDialogCamera.SetContentView(R.drawable.z_general_permission_camera, R.string.DescriptionUIPermissionCamera, Manifest.permission.CAMERA, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Allow)
                            {
                                if (!Allow)
                                {
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
                TextViewGallery.SetColor(R.color.TextWhite);
                TextViewGallery.setText(Misc.String(R.string.DescriptionUIProfileGallery));
                TextViewGallery.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                        {
                            DialogProfile.dismiss();

                            Activity.GetManager().OpenView(new GalleryViewUI(1, 1, new GalleryViewUI.GalleryListener()
                            {
                                String ImageURL;

                                @Override
                                public void OnSelection(String URL)
                                {
                                    ImageURL = URL;
                                }

                                @Override
                                public void OnRemove(String URL)
                                {
                                    ImageURL = "";
                                }

                                @Override
                                public void OnSave()
                                {

                                    Update(new File(ImageURL), true);
                                }
                            }), "GalleryViewUI", true);
                            return;
                        }

                        DialogProfile.dismiss();

                        PermissionDialog PermissionDialogGallery = new PermissionDialog(Activity);
                        PermissionDialogGallery.SetContentView(R.drawable.z_general_permission_storage, R.string.DescriptionUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Allow)
                            {
                                if (!Allow)
                                {

                                    Misc.ToastOld(Misc.String(R.string.DescriptionUIPermissionStorage));
                                    return;
                                }

                                Activity.GetManager().OpenView(new GalleryViewUI(1, 1, new GalleryViewUI.GalleryListener()
                                {
                                    String ImageURL;

                                    @Override
                                    public void OnSelection(String URL)
                                    {
                                        ImageURL = URL;
                                    }

                                    @Override
                                    public void OnRemove(String URL)
                                    {
                                        ImageURL = "";
                                    }

                                    @Override
                                    public void OnSave()
                                    {

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
                TextViewRemove.SetColor(R.color.TextWhite);
                TextViewRemove.setText(Misc.String(R.string.DescriptionUIProfileRemove));
                TextViewRemove.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
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

        final EditText EditTextName = view.findViewById(R.id.editTextName);
        EditTextName.requestFocus();
        Misc.SetCursorColor(EditTextName, R.color.Primary);
        EditTextName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32), new InputFilter()
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
        } });
        EditTextName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ButtonFinish.setEnabled(s.length() > 1);
            }
        });

        final EditText EditTextDescription = view.findViewById(R.id.editTextDescription);
        Misc.SetCursorColor(EditTextName, R.color.Primary);

        ButtonFinish.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35)));
        ButtonFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonFinish.setEnabled(false);
                LoadingViewFinish.Start();

                final ProgressDialog Progress = new ProgressDialog(Activity);
                Progress.setMessage(Misc.String(R.string.DescriptionUIUpload));
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                if (Type == 0)
                {
                    AndroidNetworking.upload(Misc.GetRandomServer("SignInGoogleVerify")).addMultipartParameter("Token", Code).addMultipartParameter("Name", EditTextName.getText().toString()).addMultipartParameter("Username", Username).addMultipartParameter("Description", EditTextDescription.getText().toString()).addMultipartParameter("Session", Misc.GenerateSession()).addMultipartFile("Avatar", ProfileFile).setTag("DescriptionUI").build().setUploadProgressListener(new UploadProgressListener()
                    {
                        @Override
                        public void onProgress(long u, long t)
                        {
                            Progress.setProgress((int) (100 * u / t));
                        }
                    }).getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setEnabled(true);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                switch (Result.getInt("Message"))
                                {
                                    case 0:
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
                            }
                            catch (Exception e)
                            {
                                Misc.Debug("DescriptionUI-SignInGoogleVerify: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setEnabled(true);
                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                        }
                    });
                }
                else if (Type == 1)
                {
                    AndroidNetworking.upload(Misc.GetRandomServer("SignUpPhoneFinish")).addMultipartParameter("Issue", Code).addMultipartParameter("Name", EditTextName.getText().toString()).addMultipartParameter("Username", Username).addMultipartParameter("Description", EditTextDescription.getText().toString()).addMultipartParameter("Session", Misc.GenerateSession()).addMultipartFile("Avatar", ProfileFile).setTag("DescriptionUI").build().setUploadProgressListener(new UploadProgressListener()
                    {
                        @Override
                        public void onProgress(long u, long t)
                        {
                            Progress.setProgress((int) (100 * u / t));
                        }
                    }).getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setEnabled(true);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                switch (Result.getInt("Message"))
                                {
                                    case 0:
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
                            }
                            catch (Exception e)
                            {
                                Misc.Debug("DescriptionUI-SignUpPhoneFinish: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setEnabled(true);
                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                        }
                    });
                }
                else if (Type == 2)
                {
                    AndroidNetworking.upload(Misc.GetRandomServer("SignUpEmailFinish")).addMultipartParameter("Issue", Code).addMultipartParameter("Name", EditTextName.getText().toString()).addMultipartParameter("Description", EditTextDescription.getText().toString()).addMultipartParameter("Session", Misc.GenerateSession()).addMultipartFile("Avatar", ProfileFile).setTag("DescriptionUI").build().setUploadProgressListener(new UploadProgressListener()
                    {
                        @Override
                        public void onProgress(long u, long t)
                        {
                            Progress.setProgress((int) (100 * u / t));
                        }
                    }).getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setEnabled(true);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                switch (Result.getInt("Message"))
                                {
                                    case 0:

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
                            }
                            catch (Exception e)
                            {
                                Misc.Debug("DescriptionUI-SignUpEmailFinish: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            Progress.dismiss();
                            LoadingViewFinish.Stop();
                            ButtonFinish.setEnabled(true);
                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                        }
                    });
                }
            }
        });

        CropImageViewMain = new CropImageView(Activity);
        CropImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        CropImageViewMain.setFixedAspectRatio(true);
        CropImageViewMain.setBackgroundResource(R.color.TextWhite);
        CropImageViewMain.setAutoZoomEnabled(true);
        CropImageViewMain.setAspectRatio(1, 1);
        CropImageViewMain.setVisibility(View.GONE);

        ((RelativeLayout) view.getRootView()).addView(CropImageViewMain);

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
        ImageViewDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT > 20)
                    Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.Primary));

                try
                {
                    CropImageViewMain.setVisibility(View.GONE);

                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                    CropImageViewMain.getCroppedImage(250, 250).compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                    File ProfileFile = new File(Misc.Temp(), (String.valueOf(System.currentTimeMillis()) + "_imagepreview_crop.jpg"));

                    FileOutputStream FOS = new FileOutputStream(ProfileFile);
                    FOS.write(BAOS.toByteArray());
                    FOS.flush();
                    FOS.close();

                    Update(ProfileFile, false);
                }
                catch (Exception e)
                {
                    Misc.Debug("DescriptionUI-Crop: " + e.toString());
                }
            }
        });

        RelativeLayoutCrop.addView(ImageViewDone);

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnResume()
    {
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("DescriptionUI");
    }

    public void Update(File file, boolean Crop)
    {
        if (file == null)
            return;

        if (Crop)
        {
            if (Build.VERSION.SDK_INT > 20)
                Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.TextWhite));

            CropImageViewMain.setVisibility(View.VISIBLE);
            CropImageViewMain.setImageUriAsync(Uri.fromFile(file));
            return;
        }

        CircleImageViewProfile.setImageURI(Uri.parse(file.getAbsolutePath()));
        ProfileFile = file;
    }
}
