package co.biogram.main.ui.social;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;

import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.CropViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.PermissionDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUI extends FragmentView
{
    private String Name = "";
    private String Username = "";
    private String Phone = "";
    private String Email = "";
    private String About = "";
    private String Website = "";

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.social_profile, null);

        ImageView ImageViewSetting = view.findViewById(R.id.ImageViewSetting);
        ImageViewSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Setting Page
            }
        });

        ImageView ImageViewProfile = view.findViewById(R.id.ImageViewProfile);
        ImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Profile Page
            }
        });

        final CircleImageView CircleImageViewProfile = view.findViewById(R.id.CircleImageViewProfile);
        CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_avatar, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                TextView TextViewCamera = dialogView.findViewById(R.id.TextViewCamera);
                TextViewCamera.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        PermissionDialog permissionDialog = new PermissionDialog(Activity);
                        permissionDialog.SetContentView(R.drawable.___general_permission_camera, R.string.SocialProfileUIAvatarCameraMessage, Manifest.permission.CAMERA, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.Toast(R.string.SocialProfileUIAvatarCameraMessage);
                                    return;
                                }

                                Activity.GetManager().OpenView(new CameraViewUI(Misc.ToDP(250), Misc.ToDP(250), true, new CameraViewUI.OnCaptureListener()
                                {
                                    @Override
                                    public void OnCapture(Bitmap bitmap)
                                    {
                                        // TODO Update Profile
                                    }
                                }), R.id.ContainerFull, "CameraViewUI");
                            }
                        });
                    }
                });

                TextView TextViewGallery = dialogView.findViewById(R.id.TextViewGallery);
                TextViewGallery.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        PermissionDialog permissionDialog = new PermissionDialog(Activity);
                        permissionDialog.SetContentView(R.drawable.___general_permission_storage, R.string.SocialProfileUIAvatarGalleryMessage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.Toast(R.string.SocialProfileUIAvatarGalleryMessage);
                                    return;
                                }

                                Activity.GetManager().OpenView(new GalleryViewUI(1, GalleryViewUI.TYPE_IMAGE, new GalleryViewUI.GalleryListener()
                                {
                                    private String Path = "";

                                    @Override
                                    public void OnSelection(String path)
                                    {
                                        Path = path;
                                    }

                                    @Override
                                    public void OnRemove(String path)
                                    {
                                        Path = path;
                                    }

                                    @Override
                                    public void OnSave()
                                    {
                                        Activity.GetManager().OpenView(new CropViewUI(Path, true, new CropViewUI.OnCropListener()
                                        {
                                            @Override
                                            public void OnCrop(Bitmap bitmap)
                                            {
                                                Activity.onBackPressed();
                                                // TODO Send avaar
                                            }
                                        }), R.id.ContainerFull, "CropViewUI");
                                    }
                                }), R.id.ContainerFull, "CameraViewUI");
                            }
                        });
                    }
                });

                TextView TextViewDelete = dialogView.findViewById(R.id.TextViewDelete);
                TextViewDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        CircleImageViewProfile.setImageResource(R.drawable.___social_profile_avatar);

                        // TODO Request Delete Profile Image
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewName = view.findViewById(R.id.TextViewName);
        TextViewName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_name, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final EditText EditTextName = dialogView.findViewById(R.id.EditTextName);
                EditTextName.setText(Name);
                EditTextName.setSelection(Name.length());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update Name
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewUsername = view.findViewById(R.id.TextViewUsername);
        TextViewUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_username, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final EditText EditTextUsername = dialogView.findViewById(R.id.EditTextUsername);
                EditTextUsername.setText(Username);
                EditTextUsername.setSelection(Username.length());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update Username
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        LinearLayout LinearLayoutSaved = view.findViewById(R.id.LinearLayoutSaved);
        LinearLayoutSaved.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Saved Page
            }
        });

        TextView TextViewFollowing = view.findViewById(R.id.TextViewFollowing);
        TextViewFollowing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Saved Page
            }
        });

        TextView TextViewFollower = view.findViewById(R.id.TextViewFollower);
        TextViewFollower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Saved Page
            }
        });

        TextView TextViewProfileView = view.findViewById(R.id.TextViewProfileView);
        TextViewProfileView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Saved Page
            }
        });

        TextView TextViewRating = view.findViewById(R.id.TextViewRating);
        TextView TextViewRate = view.findViewById(R.id.TextViewRate);

        LinearLayout LinearLayoutRating = view.findViewById(R.id.LinearLayoutRating);
        LinearLayoutRating.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Rating Page
            }
        });

        LinearLayout LinearLayoutSpecial = view.findViewById(R.id.LinearLayoutSpecial);
        LinearLayoutSpecial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new Profile_SpecialCenterUI(), R.id.ContainerFull, "Profile_SpecialCenterUI");
            }
        });

        TextView TextViewPhoneNumber = view.findViewById(R.id.TextViewPhoneNumber);

        LinearLayout LinearLayoutPhoneNumber = view.findViewById(R.id.LinearLayoutPhoneNumber);
        LinearLayoutPhoneNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_phone, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final EditText EditTextPhone = dialogView.findViewById(R.id.EditTextPhone);
                EditTextPhone.setText(Phone);
                EditTextPhone.setSelection(Phone.length());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update Phone
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewEmailAddress = view.findViewById(R.id.TextViewEmailAddress);

        LinearLayout LinearLayoutEmail = view.findViewById(R.id.LinearLayoutEmail);
        LinearLayoutEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_email, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final EditText EditTextEmail = dialogView.findViewById(R.id.EditTextEmail);
                EditTextEmail.setText(Email);
                EditTextEmail.setSelection(Email.length());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update Email
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewAboutMe = view.findViewById(R.id.TextViewAboutMe);

        LinearLayout LinearLayoutAboutMe = view.findViewById(R.id.LinearLayoutAboutMe);
        LinearLayoutAboutMe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_about, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final EditText EditTextAbout = dialogView.findViewById(R.id.EditTextAbout);
                EditTextAbout.setText(About);
                EditTextAbout.setSelection(About.length());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update AboutMe
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewWebsite = view.findViewById(R.id.TextViewWebsite);

        LinearLayout LinearLayoutWebsite = view.findViewById(R.id.LinearLayoutWebsite);
        LinearLayoutWebsite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_website, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final EditText EditTextWebsite = dialogView.findViewById(R.id.EditTextWebsite);
                EditTextWebsite.setText(Website);
                EditTextWebsite.setSelection(Website.length());

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update Website
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewLocation = view.findViewById(R.id.TextViewLocation);

        LinearLayout LinearLayoutLocation = view.findViewById(R.id.LinearLayoutLocation);
        LinearLayoutLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_location, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                final ListView ListViewSearch = dialogView.findViewById(R.id.ListViewSearch);
                final EditText EditTextLocation = dialogView.findViewById(R.id.EditTextLocation);

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                ImageView ImageViewSearch = dialogView.findViewById(R.id.ImageViewSearch);
                ImageViewSearch.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String[] values = new String[] { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Android", "iPhone", "WindowsMobile" };


                        ListViewSearch.setAdapter(new ArrayAdapter<>(Activity, android.R.layout.simple_list_item_1, values));
                    }
                });

                ListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                    {

                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        TextView TextViewTag1 = view.findViewById(R.id.TextViewTag1);
        TextView TextViewTag2 = view.findViewById(R.id.TextViewTag2);
        TextView TextViewTag3 = view.findViewById(R.id.TextViewTag3);
        TextView TextViewTag4 = view.findViewById(R.id.TextViewTag4);
        TextView TextViewTag5 = view.findViewById(R.id.TextViewTag5);
        TextView TextViewTag6 = view.findViewById(R.id.TextViewTag6);
        TextView TextViewTag7 = view.findViewById(R.id.TextViewTag7);

        LinearLayout LinearLayoutFeature = view.findViewById(R.id.LinearLayoutFeature);
        LinearLayoutFeature.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_feature, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                if (dialog.getWindow() != null)
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                TextView TextViewSubmit = dialogView.findViewById(R.id.TextViewSubmit);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        // TODO Update AboutMe
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        LoadingView LoadingViewMain = view.findViewById(R.id.LoadingViewMain);

        // TODO Cache
        // TODO Server

        TextViewUsername.setText("@alikhazaee");
        TextViewName.setText("Ali Khazaee");
        TextViewFollowing.setText("12.6K");
        TextViewFollower.setText("1892");
        TextViewProfileView.setText("102K");
        TextViewRating.setText("4.6");
        TextViewRate.setText("1023");
        TextViewPhoneNumber.setText("09385454764");
        TextViewEmailAddress.setText("dev.khazaee@gmail.com");
        TextViewAboutMe.setText("I am e Sosis");
        TextViewWebsite.setText("https://google.com");
        TextViewLocation.setText("Karaj - MehrShahr");
        TextViewTag1.setText("Developer");
        TextViewTag2.setText("PHP");
        TextViewTag3.setText("Node JS");
        TextViewTag4.setText("Sexy Lady");
        TextViewTag5.setText("Hava Garm e Haa");
        TextViewTag6.setText("Tanbe 1000");
        TextViewTag7.setText("9103");

        ViewMain = view;
    }
}
