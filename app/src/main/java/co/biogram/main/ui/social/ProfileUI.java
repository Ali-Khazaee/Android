package co.biogram.main.ui.social;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;

import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.CropViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.PermissionDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUI extends FragmentView
{
    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.profile, null);

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

                View dialogView = View.inflate(Activity, R.layout.profile_dialog_avatar, null);

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
                        permissionDialog.SetContentView(R.drawable.__general_permission_camera_white, R.string.ProfileUIAvatarCameraMessage, Manifest.permission.CAMERA, Activity, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                    return;

                                Activity.GetManager().OpenView(new CameraViewUI(Misc.ToDP(300), Misc.ToDP(300), true, new CameraViewUI.OnCaptureListener()
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
                        permissionDialog.SetContentView(R.drawable.__general_permission_storage_white, R.string.ProfileUIAvatarGalleryMessage, Manifest.permission.READ_EXTERNAL_STORAGE, Activity, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.Toast(R.string.ProfileUIAvatarGalleryMessage);
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
                        CircleImageViewProfile.setImageResource(R.drawable.__profile_avatar);

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

                View dialogView = View.inflate(Activity, R.layout.profile_dialog_avatar, null);

                ImageView ImageViewClose = dialogView.findViewById(R.id.ImageViewClose);
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dialog.dismiss(); } });

                TextView TextViewDelete = dialogView.findViewById(R.id.TextViewDelete);
                TextViewDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        CircleImageViewProfile.setImageResource(R.drawable.__profile_avatar);

                        // TODO Request Delete Profile Image
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
                // TODO Open Saved Page
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


        TextView TextViewFollower = view.findViewById(R.id.TextViewFollower);


        TextView TextViewProfileView = view.findViewById(R.id.TextViewProfileView);


        TextView TextViewRating = view.findViewById(R.id.TextViewRating);


        TextView TextViewPopular = view.findViewById(R.id.TextViewPopular);


        TextView TextViewRate = view.findViewById(R.id.TextViewRate);

        TextView TextViewTag1 = view.findViewById(R.id.TextViewTag1);


        TextView TextViewTag2 = view.findViewById(R.id.TextViewTag2);


        TextView TextViewTag3 = view.findViewById(R.id.TextViewTag3);


        TextView TextViewTag4 = view.findViewById(R.id.TextViewTag4);


        TextView TextViewTag5 = view.findViewById(R.id.TextViewTag5);


        TextView TextViewTag6 = view.findViewById(R.id.TextViewTag6);


        TextView TextViewTag7 = view.findViewById(R.id.TextViewTag7);


        TextViewUsername.setText("@alikhazaee");
        TextViewName.setText("Ali Khazaee");
        TextViewFollowing.setText("12.6K");
        TextViewFollower.setText("1892");
        TextViewProfileView.setText("102K");
        TextViewRating.setText("4.6");
        TextViewPopular.setText("Lv . 2");
        TextViewRate.setText("1023");
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






























/*final DBHandler DB = new DBHandler(Activity);

        if (!IsUsername)
            ID = SharedHandler.GetString("ID");

        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(Misc.Align("R"));

        final TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.ProfileUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSettingParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSettingParam.addRule(Misc.Align("L"));

        ImageView ImageViewSetting = new ImageView(Activity);
        ImageViewSetting.setLayoutParams(ImageViewSettingParam);
        ImageViewSetting.setId(Misc.generateViewId());
        ImageViewSetting.setImageResource(R.drawable._inbox_search);
        ImageViewSetting.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewSetting.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Open Setting * /  } });

        RelativeLayoutHeader.addView(ImageViewSetting);

        RelativeLayout.LayoutParams ImageViewWriteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWriteParam.addRule(Misc.AlignTo("L"), ImageViewSetting.getId());

        ImageView ImageViewProfile = new ImageView(Activity);
        ImageViewProfile.setLayoutParams(ImageViewWriteParam);
        ImageViewProfile.setImageResource(R.drawable._inbox_write);
        ImageViewProfile.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Open Profile * /  } });

        RelativeLayoutHeader.addView(ImageViewProfile);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        StickyScrollView ScrollViewMain = new StickyScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(65), Misc.ToDP(65));
        CircleImageViewProfileParam.setMargins(Misc.ToDP(15), Misc.ToDP(20), Misc.ToDP(15), 0);
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        final CircleImageView CircleImageViewProfile = new CircleImageView(Activity);
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
        CircleImageViewProfile.setImageResource(R.drawable._general_avatar);
        CircleImageViewProfile.setId(Misc.generateViewId());
        CircleImageViewProfile.SetBorderWidth(1);

        RelativeLayoutScroll.addView(CircleImageViewProfile);

        RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());
        TextViewNameParam.setMargins(0, Misc.ToDP(20), 0, 0);

        final TextView TextViewName = new TextView(Activity, 14, true);
        TextViewName.setLayoutParams(TextViewNameParam);
        TextViewName.SetColor(R.color.TextWhite);

        RelativeLayoutScroll.addView(TextViewName);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF,  CircleImageViewProfile.getId());
        TextViewUsernameParam.setMargins(0, Misc.ToDP(39), 0, 0);

        final TextView TextViewUsername = new TextView(Activity, 14, false);
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.SetColor(R.color.Gray);

        RelativeLayoutScroll.addView(TextViewUsername);

        RelativeLayout.LayoutParams LinearLayoutTypeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutTypeParam.addRule(RelativeLayout.RIGHT_OF,  CircleImageViewProfile.getId());
        LinearLayoutTypeParam.setMargins(0, Misc.ToDP(61), 0, 0);

        LinearLayout LinearLayoutType = new LinearLayout(Activity);
        LinearLayoutType.setLayoutParams(LinearLayoutTypeParam);
        LinearLayoutType.setOrientation(LinearLayout.HORIZONTAL);

        RelativeLayoutScroll.addView(LinearLayoutType);

        ImageView ImageViewType = new ImageView(Activity);
        ImageViewType.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24)));
        ImageViewType.setImageResource(R.drawable._profile_avatar);

        LinearLayoutType.addView(ImageViewType);

        final TextView TextViewType = new TextView(Activity, 14, false);
        TextViewType.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewType.SetColor(R.color.Primary);

        LinearLayoutType.addView(TextViewType);

        RelativeLayout.LayoutParams LinearLayoutEditParam = new RelativeLayout.LayoutParams(Misc.ToDP(85), Misc.ToDP(45));
        LinearLayoutEditParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LinearLayoutEditParam.setMargins(0, Misc.ToDP(30), Misc.ToDP(15), 0);

        GradientDrawable DrawableEdit = new GradientDrawable();
        DrawableEdit.setColor(Misc.Color(R.color.Primary));
        DrawableEdit.setCornerRadius(Misc.ToDP(25));

        LinearLayout LinearLayoutEdit = new LinearLayout(Activity);
        LinearLayoutEdit.setLayoutParams(LinearLayoutEditParam);
        LinearLayoutEdit.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutEdit.setGravity(Gravity.CENTER);
        LinearLayoutEdit.setBackground(DrawableEdit);

        RelativeLayoutScroll.addView(LinearLayoutEdit);

        ImageView ImageViewEdit = new ImageView(Activity);
        ImageViewEdit.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewEdit.setImageResource(R.drawable._profile_edit);

        LinearLayoutEdit.addView(ImageViewEdit);

        TextView TextViewEdit = new TextView(Activity, 14, false);
        TextViewEdit.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewEdit.SetColor(R.color.TextDark);
        TextViewEdit.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewEdit.setText(Activity.getString(R.string.ProfileUIEdit));

        LinearLayoutEdit.addView(TextViewEdit);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine2Param.addRule(RelativeLayout.BELOW, CircleImageViewProfile.getId());
        ViewLine2Param.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(20));

        View ViewLine2 = new View(Activity);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(R.color.LineWhite);
        ViewLine2.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        LinearLayoutCountParam.addRule(RelativeLayout.BELOW, ViewLine2.getId());

        LinearLayout LinearLayoutCount = new LinearLayout(Activity);
        LinearLayoutCount.setLayoutParams(LinearLayoutCountParam);
        LinearLayoutCount.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCount.setGravity(Gravity.CENTER);
        LinearLayoutCount.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(LinearLayoutCount);

        LinearLayout LinearLayoutPost = new LinearLayout(Activity);
        LinearLayoutPost.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));
        LinearLayoutPost.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutPost.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutPost);

        final TextView TextViewPostCount = new TextView(Activity, 14, true);
        TextViewPostCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewPostCount.SetColor(R.color.TextWhite);
        TextViewPostCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutPost.addView(TextViewPostCount);

        TextView TextViewPost = new TextView(Activity, 14, true);
        TextViewPost.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewPost.SetColor(R.color.Gray);
        TextViewPost.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewPost.setText(Activity.getString(R.string.ProfileUIPost));

        LinearLayoutPost.addView(TextViewPost);

        LinearLayout LinearLayoutFollower = new LinearLayout(Activity);
        LinearLayoutFollower.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));
        LinearLayoutFollower.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutFollower.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutFollower);

        final TextView TextViewFollowerCount = new TextView(Activity, 14, true);
        TextViewFollowerCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollowerCount.SetColor(R.color.TextWhite);
        TextViewFollowerCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutFollower.addView(TextViewFollowerCount);

        TextView TextViewFollower = new TextView(Activity, 14, true);
        TextViewFollower.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollower.SetColor(R.color.Gray);
        TextViewFollower.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewFollower.setText(Activity.getString(R.string.ProfileUIFollower));

        LinearLayoutFollower.addView(TextViewFollower);

        LinearLayout LinearLayoutFollowing = new LinearLayout(Activity);
        LinearLayoutFollowing.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));
        LinearLayoutFollowing.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutFollowing.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutFollowing);

        final TextView TextViewFollowingCount = new TextView(Activity, 14, true);
        TextViewFollowingCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollowingCount.SetColor(R.color.TextWhite);
        TextViewFollowingCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutFollowing.addView(TextViewFollowingCount);

        TextView TextViewFollowing = new TextView(Activity, 14, true);
        TextViewFollowing.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollowing.SetColor(R.color.Gray);
        TextViewFollowing.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewFollowing.setText(Activity.getString(R.string.ProfileUIFollowing));

        LinearLayoutFollowing.addView(TextViewFollowing);

        LinearLayout LinearLayoutProfile = new LinearLayout(Activity);
        LinearLayoutProfile.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        LinearLayoutProfile.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutProfile.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutProfile);

        final TextView TextViewProfileCount = new TextView(Activity, 14, true);
        TextViewProfileCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewProfileCount.SetColor(R.color.TextWhite);
        TextViewProfileCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutProfile.addView(TextViewProfileCount);

        TextView TextViewProfile = new TextView(Activity, 14, true);
        TextViewProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewProfile.SetColor(R.color.Gray);
        TextViewProfile.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewProfile.setText(Activity.getString(R.string.ProfileUIProfileView));

        LinearLayoutProfile.addView(TextViewProfile);

        RelativeLayout.LayoutParams ViewLine3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine3Param.addRule(RelativeLayout.BELOW, LinearLayoutCount.getId());
        ViewLine3Param.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(20));

        View ViewLine3 = new View(Activity);
        ViewLine3.setLayoutParams(ViewLine3Param);
        ViewLine3.setBackgroundResource(R.color.LineWhite);
        ViewLine3.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(ViewLine3);

        RelativeLayout.LayoutParams TextViewPropertyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPropertyParam.addRule(RelativeLayout.BELOW, ViewLine3.getId());
        TextViewPropertyParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));

        TextView TextViewProperty = new TextView(Activity, 16, true);
        TextViewProperty.setLayoutParams(TextViewPropertyParam);
        TextViewProperty.SetColor(R.color.TextWhite);
        TextViewProperty.setId(Misc.generateViewId());
        TextViewProperty.setText(Activity.getString(R.string.ProfileUIProperty));

        RelativeLayoutScroll.addView(TextViewProperty);

        RelativeLayout.LayoutParams TextViewLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(150), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLevelParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));
        TextViewLevelParam.addRule(RelativeLayout.BELOW, TextViewProperty.getId());

        TextView TextViewLevel = new TextView(Activity, 14, true);
        TextViewLevel.setLayoutParams(TextViewLevelParam);
        TextViewLevel.setId(Misc.generateViewId());
        TextViewLevel.SetColor(R.color.Gray);
        TextViewLevel.setText(Activity.getString(R.string.ProfileUILevel));

        RelativeLayoutScroll.addView(TextViewLevel);

        RelativeLayout.LayoutParams RelativeLayoutLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutLevelParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutLevelParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());

        GradientDrawable DrawableLevel = new GradientDrawable();
        DrawableLevel.setColor(Color.parseColor("#01c09e"));
        DrawableLevel.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutLevel = new RelativeLayout(Activity);
        RelativeLayoutLevel.setLayoutParams(RelativeLayoutLevelParam);
        RelativeLayoutLevel.setBackground(DrawableLevel);
        RelativeLayoutLevel.setId(Misc.generateViewId());
        RelativeLayoutLevel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new MyLevelUI(), R.id.ContainerFull, "MyLevelUI");
            }
        });

        RelativeLayoutScroll.addView(RelativeLayoutLevel);

        RelativeLayout.LayoutParams ImageViewLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewLevelParam.setMargins(Misc.ToDP(5), 0, 0, 0);
        ImageViewLevelParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewLevel = new ImageView(Activity);
        ImageViewLevel.setLayoutParams(ImageViewLevelParam);
        ImageViewLevel.setImageResource(R.drawable._profile_level);
        ImageViewLevel.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
        ImageViewLevel.setId(Misc.generateViewId());

        RelativeLayoutLevel.addView(ImageViewLevel);

        RelativeLayout.LayoutParams TextViewLevel2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLevel2Param.addRule(RelativeLayout.RIGHT_OF, ImageViewLevel.getId());
        TextViewLevel2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewLevel2 = new TextView(Activity, 14, true);
        TextViewLevel2.setLayoutParams(TextViewLevel2Param);
        TextViewLevel2.SetColor(R.color.TextDark);
        TextViewLevel2.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);

        RelativeLayoutLevel.addView(TextViewLevel2);

        RelativeLayout.LayoutParams ImageViewLevel2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewLevel2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewLevel2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewLevel2 = new ImageView(Activity);
        ImageViewLevel2.setLayoutParams(ImageViewLevel2Param);
        ImageViewLevel2.setImageResource(R.drawable.back_white_rtl);

        RelativeLayoutLevel.addView(ImageViewLevel2);

        RelativeLayout.LayoutParams TextViewCashParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCashParam.addRule(RelativeLayout.BELOW, TextViewProperty.getId());
        TextViewCashParam.addRule(RelativeLayout.RIGHT_OF, TextViewLevel.getId());

        TextView TextViewCash = new TextView(Activity, 14, true);
        TextViewCash.setLayoutParams(TextViewCashParam);
        TextViewCash.SetColor(R.color.Gray);
        TextViewCash.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewCash.setText(Activity.getString(R.string.ProfileUICash));

        RelativeLayoutScroll.addView(TextViewCash);

        RelativeLayout.LayoutParams RelativeLayoutCashParam =  new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutCashParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutCashParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());
        RelativeLayoutCashParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        GradientDrawable DrawableCash = new GradientDrawable();
        DrawableCash.setColor(Color.parseColor("#ef9c00"));
        DrawableCash.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutCash = new RelativeLayout(Activity);
        RelativeLayoutCash.setLayoutParams(RelativeLayoutCashParam);
        RelativeLayoutCash.setBackground(DrawableCash);

        RelativeLayoutScroll.addView(RelativeLayoutCash);

        RelativeLayout.LayoutParams ImageViewCashParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewCashParam.setMargins(Misc.ToDP(5), 0, 0, 0);
        ImageViewCashParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewCash = new ImageView(Activity);
        ImageViewCash.setLayoutParams(ImageViewCashParam);
        ImageViewCash.setImageResource(R.drawable._profile_cash);
        ImageViewCash.setId(Misc.generateViewId());

        RelativeLayoutCash.addView(ImageViewCash);

        RelativeLayout.LayoutParams TextViewCash2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCash2Param.addRule(RelativeLayout.RIGHT_OF, ImageViewCash.getId());
        TextViewCash2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewCash2 = new TextView(Activity, 14, false);
        TextViewCash2.setLayoutParams(TextViewCash2Param);
        TextViewCash2.SetColor(R.color.TextDark);
        TextViewCash2.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);

        RelativeLayoutCash.addView(TextViewCash2);

        RelativeLayout.LayoutParams ImageViewCash2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewCash2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewCash2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewCash2 = new ImageView(Activity);
        ImageViewCash2.setLayoutParams(ImageViewCash2Param);
        ImageViewCash2.setImageResource(R.drawable.back_white_rtl);

        RelativeLayoutCash.addView(ImageViewCash2);

        RelativeLayout.LayoutParams TextViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewRatingParam.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewRatingParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(8), Misc.ToDP(5));

        TextView TextViewRating = new TextView(Activity, 14, true);
        TextViewRating.setLayoutParams(TextViewRatingParam);
        TextViewRating.setId(Misc.generateViewId());
        TextViewRating.SetColor(R.color.Gray);
        TextViewRating.setText(Activity.getString(R.string.ProfileUIRating));

        RelativeLayoutScroll.addView(TextViewRating);

        RelativeLayout.LayoutParams TextViewRating3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewRating3Param.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewRating3Param.addRule(RelativeLayout.RIGHT_OF, TextViewRating.getId());
        TextViewRating3Param.setMargins(0, Misc.ToDP(3), 0, 0);

        final TextView TextViewRating3 = new TextView(Activity, 12, false);
        TextViewRating3.setLayoutParams(TextViewRating3Param);
        TextViewRating3.SetColor(R.color.Gray);

        RelativeLayoutScroll.addView(TextViewRating3);

        RelativeLayout.LayoutParams RelativeLayoutRatingParam =  new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutRatingParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutRatingParam.addRule(RelativeLayout.BELOW, TextViewRating.getId());

        GradientDrawable DrawableRating = new GradientDrawable();
        DrawableRating.setStroke(Misc.ToDP(2), Misc.Color(R.color.Primary));
        DrawableRating.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutRating = new RelativeLayout(Activity);
        RelativeLayoutRating.setLayoutParams(RelativeLayoutRatingParam);
        RelativeLayoutRating.setBackground(DrawableRating);

        RelativeLayoutScroll.addView(RelativeLayoutRating);

        RelativeLayout.LayoutParams TextViewRating2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewRating2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewRating2 = new TextView(Activity, 16, true);
        TextViewRating2.setLayoutParams(TextViewRating2Param);
        TextViewRating2.SetColor(R.color.TextWhite);
        TextViewRating2.setPadding(Misc.ToDP(15), Misc.ToDP(5), Misc.ToDP(30), 0);
        TextViewRating2.setId(Misc.generateViewId());

        RelativeLayoutRating.addView(TextViewRating2);

        RelativeLayout.LayoutParams ImageViewRatingParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewRatingParam.addRule(RelativeLayout.RIGHT_OF, TextViewRating2.getId());
        ImageViewRatingParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewRating = new ImageView(Activity);
        ImageViewRating.setLayoutParams(ImageViewRatingParam);
        ImageViewRating.setImageResource(R.drawable._profile_rating);

        RelativeLayoutRating.addView(ImageViewRating);

        RelativeLayout.LayoutParams ImageViewRating2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewRating2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewRating2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewRating2 = new ImageView(Activity);
        ImageViewRating2.setLayoutParams(ImageViewRating2Param);
        ImageViewRating2.setImageResource(R.drawable.back_blue_rtl);

        RelativeLayoutRating.addView(ImageViewRating2);

        RelativeLayout.LayoutParams TextViewBadgeParam = new RelativeLayout.LayoutParams(Misc.ToDP(150), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewBadgeParam.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewBadgeParam.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewBadgeParam.addRule(RelativeLayout.RIGHT_OF, TextViewLevel.getId());

        TextView TextViewBadge = new TextView(Activity, 14, true);
        TextViewBadge.setLayoutParams(TextViewBadgeParam);
        TextViewBadge.setId(Misc.generateViewId());
        TextViewBadge.SetColor(R.color.Gray);
        TextViewBadge.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewBadge.setText(Activity.getString(R.string.ProfileUIBadge));

        RelativeLayoutScroll.addView(TextViewBadge);

        RelativeLayout.LayoutParams RelativeLayoutBadgeParam =  new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutBadgeParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutBadgeParam.addRule(RelativeLayout.BELOW, TextViewBadge.getId());
        RelativeLayoutBadgeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        GradientDrawable DrawableBadge = new GradientDrawable();
        DrawableBadge.setStroke(Misc.ToDP(2), Misc.Color(R.color.Primary));
        DrawableBadge.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutBadge = new RelativeLayout(Activity);
        RelativeLayoutBadge.setLayoutParams(RelativeLayoutBadgeParam);
        RelativeLayoutBadge.setBackground(DrawableBadge);
        RelativeLayoutBadge.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(RelativeLayoutBadge);

        RelativeLayout.LayoutParams TextViewBadge2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewBadge2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewBadge2 = new TextView(Activity, 14, false);
        TextViewBadge2.setLayoutParams(TextViewBadge2Param);
        TextViewBadge2.SetColor(R.color.Primary);
        TextViewBadge2.setPadding(Misc.ToDP(15), Misc.ToDP(5), 0, 0);
        TextViewBadge2.setId(Misc.generateViewId());
        TextViewBadge2.setText(Activity.getString(R.string.ProfileUIBadge));

        RelativeLayoutBadge.addView(TextViewBadge2);

        RelativeLayout.LayoutParams TextViewBadge3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewBadge3Param.addRule(RelativeLayout.RIGHT_OF, TextViewBadge2.getId());
        TextViewBadge3Param.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewBadge3 = new TextView(Activity, 14, false);
        TextViewBadge3.setLayoutParams(TextViewBadge3Param);
        TextViewBadge3.SetColor(R.color.TextWhite);
        TextViewBadge3.setPadding(Misc.ToDP(15), Misc.ToDP(5), Misc.ToDP(15), 0);

        RelativeLayoutBadge.addView(TextViewBadge3);

        RelativeLayout.LayoutParams ImageViewBadge2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewBadge2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewBadge2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewBadge2 = new ImageView(Activity);
        ImageViewBadge2.setLayoutParams(ImageViewBadge2Param);
        ImageViewBadge2.setImageResource(R.drawable.back_blue_rtl);

        RelativeLayoutBadge.addView(ImageViewBadge2);

        RelativeLayout.LayoutParams ViewLine4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine4Param.addRule(RelativeLayout.BELOW, RelativeLayoutBadge.getId());
        ViewLine4Param.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(20));

        View ViewLine4 = new View(Activity);
        ViewLine4.setLayoutParams(ViewLine4Param);
        ViewLine4.setBackgroundResource(R.color.LineWhite);
        ViewLine4.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(ViewLine4);

        RelativeLayout.LayoutParams TextViewAboutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewAboutParam.addRule(RelativeLayout.BELOW, ViewLine4.getId());
        TextViewAboutParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), 0);

        TextView TextViewAbout = new TextView(Activity, 16, true);
        TextViewAbout.setLayoutParams(TextViewAboutParam);
        TextViewAbout.SetColor(R.color.TextWhite);
        TextViewAbout.setId(Misc.generateViewId());
        TextViewAbout.setText(Activity.getString(R.string.ProfileUIAbout));

        RelativeLayoutScroll.addView(TextViewAbout);

        RelativeLayout.LayoutParams TextViewAboutMeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewAboutMeParam.addRule(RelativeLayout.BELOW, TextViewAbout.getId());
        TextViewAboutMeParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));

        final TextView TextViewAboutMe = new TextView(Activity, 14, false);
        TextViewAboutMe.setLayoutParams(TextViewAboutMeParam);
        TextViewAboutMe.SetColor(R.color.Gray);
        TextViewAboutMe.setId(Misc.generateViewId());
        TextViewAboutMe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogLink = new Dialog(Activity);
                DialogLink.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLink.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(Activity, 14, true);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                TextViewTitle.setText(Misc.String(R.string.ProfileUIAboutTitle));
                TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine);

                final EditText EditTextMessage = new EditText(Activity);
                EditTextMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                EditTextMessage.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                EditTextMessage.setMinHeight(Misc.ToDP(56));
                EditTextMessage.setBackground(null);
                EditTextMessage.setText(TextViewAboutMe.getText().toString());
                EditTextMessage.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
                EditTextMessage.setSelection(EditTextMessage.getText().length());
                EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(512) });

                LinearLayoutMain.addView(EditTextMessage);

                View ViewLine2 = new View(Activity);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine2.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewSubmit = new TextView(Activity, 14, true);
                TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                TextViewSubmit.SetColor(R.color.Primary);
                TextViewSubmit.setText(Misc.String(R.string.ProfileUISubmit));
                TextViewSubmit.setGravity(Gravity.CENTER);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AndroidNetworking.post(Misc.GetRandomServer("ProfileAbout"))
                        .addBodyParameter("Message", EditTextMessage.getText().toString())
                        .addHeaders("Token", SharedHandler.GetString("Token"))
                        .setTag("ProfileUI")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    JSONObject Result = new JSONObject(Response);

                                    if (Result.getInt("Message") == 0)
                                    {
                                        String Message = Result.getString("Result");

                                        TextViewAboutMe.setText(Message);
                                        DB.ProfilePrivateAbout(SharedHandler.GetString("ID"), Message);
                                    }
                                }
                                catch (Exception e)
                                {
                                    Misc.Debug("ProfileUI-About: " + e.toString());
                                }
                            }

                            @Override public void onError(ANError e) { }
                        });

                        DialogLink.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewSubmit);

                DialogLink.setContentView(LinearLayoutMain);
                DialogLink.show();
            }
        });

        RelativeLayoutScroll.addView(TextViewAboutMe);

        RelativeLayout.LayoutParams LinearLayoutLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutLinkParam.addRule(RelativeLayout.BELOW, TextViewAboutMe.getId());
        LinearLayoutLinkParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));

        LinearLayout LinearLayoutLink = new LinearLayout(Activity);
        LinearLayoutLink.setLayoutParams(LinearLayoutLinkParam);
        LinearLayoutLink.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutLink.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(LinearLayoutLink);

        ImageView ImageViewLink = new ImageView(Activity);
        ImageViewLink.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewLink.setImageResource(R.drawable._category_artist_black);
        ImageViewLink.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));

        LinearLayoutLink.addView(ImageViewLink);

        final TextView TextViewLink = new TextView(Activity, 14, false);
        TextViewLink.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLink.SetColor(R.color.Primary);
        TextViewLink.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogLink = new Dialog(Activity);
                DialogLink.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLink.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(Activity, 14, true);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                TextViewTitle.setText(Misc.String(R.string.ProfileUILink));
                TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine);

                final EditText EditTextMessage = new EditText(Activity);
                EditTextMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                EditTextMessage.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                EditTextMessage.setMinHeight(Misc.ToDP(56));
                EditTextMessage.setBackground(null);
                EditTextMessage.setText(TextViewLink.getText().toString());
                EditTextMessage.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
                EditTextMessage.setSelection(EditTextMessage.getText().length());
                EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(512) });

                LinearLayoutMain.addView(EditTextMessage);

                View ViewLine2 = new View(Activity);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine2.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine2);

                TextView TextViewSubmit = new TextView(Activity, 14, true);
                TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                TextViewSubmit.SetColor(R.color.Primary);
                TextViewSubmit.setText(Misc.String(R.string.ProfileUISubmit));
                TextViewSubmit.setGravity(Gravity.CENTER);
                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(Misc.GetRandomServer("ProfileURL"))
                        .addBodyParameter("URL", EditTextMessage.getText().toString())
                        .addHeaders("Token", SharedHandler.GetString("Token"))
                        .setTag("ProfileUI")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    JSONObject Result = new JSONObject(Response);

                                    if (Result.getInt("Message") == 0)
                                    {
                                        String Message = EditTextMessage.getText().toString();

                                        if (Message.isEmpty())
                                        {
                                            TextViewLink.setTextColor(Misc.Color(R.color.Gray));
                                            TextViewLink.setText(Activity.getString(R.string.ProfileUINone));
                                        }
                                        else
                                        {
                                            TextViewLink.setTextColor(Misc.Color(R.color.Primary));
                                            TextViewLink.setText(Message);
                                        }

                                        DB.ProfilePrivateLink(SharedHandler.GetString("ID"), Message);
                                    }
                                }
                                catch (Exception e)
                                {
                                    Misc.Debug("ProfileUI-Link: " + e.toString());
                                }
                            }

                            @Override public void onError(ANError e) { }
                        });

                        DialogLink.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewSubmit);

                DialogLink.setContentView(LinearLayoutMain);
                DialogLink.show();
            }
        });

        LinearLayoutLink.addView(TextViewLink);

        RelativeLayout.LayoutParams LinearLayoutLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutLocationParam.addRule(RelativeLayout.BELOW, LinearLayoutLink.getId());
        LinearLayoutLocationParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));

        LinearLayout LinearLayoutLocation = new LinearLayout(Activity);
        LinearLayoutLocation.setLayoutParams(LinearLayoutLocationParam);
        LinearLayoutLocation.setOrientation(LinearLayout.HORIZONTAL);

        RelativeLayoutScroll.addView(LinearLayoutLocation);

        ImageView ImageViewLocation = new ImageView(Activity);
        ImageViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewLocation.setImageResource(R.drawable._category_artist_black);
        ImageViewLocation.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));

        LinearLayoutLocation.addView(ImageViewLocation);

        final TextView TextViewLocation = new TextView(Activity, 14, false);
        TextViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLocation.SetColor(R.color.Gray);
        TextViewLocation.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText EditTextLocation = new EditText(Activity);

                final Dialog DialogLocation = new Dialog(Activity);
                DialogLocation.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLocation.setCancelable(true);

                RelativeLayout RelativeLayoutLocation = new RelativeLayout(Activity);
                RelativeLayoutLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                RelativeLayoutLocation.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);

                RelativeLayout.LayoutParams ImageViewSaveParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewSave = new ImageView(Activity);
                ImageViewSave.setLayoutParams(ImageViewSaveParam);
                ImageViewSave.setImageResource(R.drawable.done_blue);
                ImageViewSave.setId(Misc.generateViewId());
                ImageViewSave.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
                ImageViewSave.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AndroidNetworking.post(Misc.GetRandomServer("ProfileLocation"))
                        .addBodyParameter("Name", EditTextLocation.getText().toString())
                        .addBodyParameter("Latitude", String.valueOf(Latitude))
                        .addBodyParameter("Longitude", String.valueOf(Longitude))
                        .addHeaders("Token", SharedHandler.GetString("Token"))
                        .setTag("ProfileUI")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    JSONObject Result = new JSONObject(Response);

                                    if (Result.getInt("Message") == 0)
                                    {
                                        TextViewLocation.setText(Result.getString("Result"));

                                        DB.ProfilePrivateLocation(SharedHandler.GetString("ID"), Result.getString("Result"), String.valueOf(Latitude), String.valueOf(Longitude));
                                    }
                                }
                                catch (Exception e)
                                {
                                    Misc.Debug("ProfileUI-Location: " + e.toString());
                                }
                            }

                            @Override public void onError(ANError e) { }
                        });

                        DialogLocation.dismiss();
                    }
                });

                RelativeLayoutLocation.addView(ImageViewSave);

                RelativeLayout.LayoutParams EditTextLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
                EditTextLocationParam.addRule(RelativeLayout.LEFT_OF, ImageViewSave.getId());

                EditTextLocation.setLayoutParams(EditTextLocationParam);
                EditTextLocation.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                EditTextLocation.setId(Misc.generateViewId());
                EditTextLocation.setBackground(null);
                EditTextLocation.setHint(Activity.getString(R.string.ProfileUILocation));
                EditTextLocation.setHintTextColor(Misc.Color(R.color.Gray));
                EditTextLocation.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
                EditTextLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                EditTextLocation.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });

                RelativeLayoutLocation.addView(EditTextLocation);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
                ViewLineParam.addRule(RelativeLayout.BELOW, EditTextLocation.getId());

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.LineWhite);
                ViewLine.setId(Misc.generateViewId());

                RelativeLayoutLocation.addView(ViewLine);

                RelativeLayout.LayoutParams MapViewParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                MapViewParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                final MapView MapView = new MapView(Activity);
                MapView.onCreate(null);
                MapView.setLayoutParams(MapViewParam);
                MapView.getMapAsync(new OnMapReadyCallback()
                {
                    @Override
                    public void onMapReady(GoogleMap map)
                    {
                        MapView.onResume();

                        FrameLayout.LayoutParams ImageViewPinParam = new FrameLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(42));
                        ImageViewPinParam.topMargin = -Misc.ToDP(20);
                        ImageViewPinParam.gravity = Gravity.CENTER;

                        ImageView ImageViewPin = new ImageView(Activity);
                        ImageViewPin.setLayoutParams(ImageViewPinParam);
                        ImageViewPin.setImageResource(R.drawable._profile_pin);

                        MapView.addView(ImageViewPin);

                        map.getUiSettings().setMyLocationButtonEnabled(true);
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.getUiSettings().setCompassEnabled(true);

                        if (Misc.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION))
                             map.setMyLocationEnabled(true);

                        if (Latitude != 0.0f && Longitude != 0.0f)
                        {
                            CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), map.getMaxZoomLevel() - 15);
                            map.moveCamera(MyPosition);
                            map.animateCamera(MyPosition);
                        }
                        else if (Misc.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                        {
                            map.setMyLocationEnabled(true);

                            Location MyLocation = null;
                            LocationManager Manager = (LocationManager) Activity.getSystemService(LOCATION_SERVICE);

                            if (Manager != null)
                                MyLocation = Manager.getLastKnownLocation(Manager.getBestProvider(new Criteria(), true));

                            if (MyLocation != null)
                            {
                                double Lat = MyLocation.getLatitude();
                                double Long = MyLocation.getLongitude();

                                CameraUpdate MyPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Long), map.getMaxZoomLevel() - 15);
                                map.moveCamera(MyPosition);
                                map.animateCamera(MyPosition);
                            }
                        }
                    }
                });

                RelativeLayoutLocation.addView(MapView);

                DialogLocation.setContentView(RelativeLayoutLocation);
                DialogLocation.show();
            }
        });

        LinearLayoutLocation.addView(TextViewLocation);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadingViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(Activity);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);
        LoadingViewMain.setBackgroundResource(R.color.GroundWhite);
        LoadingViewMain.Start();

        RelativeLayoutMain.addView(LoadingViewMain);

        Cursor cursor = DB.ProfilePrivate(ID, IsUsername);

        while (cursor.moveToNext())
        {
            TextViewName.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_NAME)));
            TextViewUsername.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_USERNAME)));
            TextViewType.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_TYPE)));
            TextViewProfileCount.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_PROFILE_COUNT)));
            TextViewFollowingCount.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_FOLLOWING_COUNT)));
            TextViewFollowerCount.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_FOLLOWER_COUNT)));
            TextViewPostCount.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_POST_COUNT)));
            TextViewLevel2.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_LEVEL)));
            TextViewCash2.setText((cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_CASH)) + " T"));
            TextViewRating2.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_RATING)));
            TextViewBadge3.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_BADGE)));
            TextViewAboutMe.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_ABOUTME)));
            TextViewLink.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_LINK)));
            TextViewLocation.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_LOCATION)));
            TextViewRating3.setText(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_RATING_COUNT)));
            Latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_LATITUDE)));
            Longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_LONGITUDE)));

            if (cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_LINK)).equals(Activity.getString(R.string.ProfileUINone)))
                TextViewLink.setTextColor(Misc.Color(R.color.Gray));

            LoadingViewMain.Stop();
            LoadingViewMain.setVisibility(View.GONE);

            GlideApp.with(Activity).load(cursor.getString(cursor.getColumnIndex(DBHandler.PROFILE_PRIVATE_PROFILE))).placeholder(R.drawable._general_avatar).into(CircleImageViewProfile);
        }

        cursor.close();

        AndroidNetworking.post(Misc.GetRandomServer("ProfilePrivate"))
        .addHeaders("Token", SharedHandler.GetString( "Token"))
        .setTag("ProfileUI")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 0)
                    {
                        String Name = Result.getString("Name");
                        String Username = "@" + Result.getString("Username");
                        String Profile = Result.getString("Profile");
                        String ProfileCount = Result.getString("ProfileCount");
                        String FollowingCount = Result.getString("Following");
                        String FollowerCount = Result.getString("Follower");
                        String PostCount = Result.getString("Post");
                        String Rating = Result.getString("Rating");
                        String Rating2 = Result.getString("Star");

                        if (Rating2 == null || Rating2.equals("null"))
                            Rating2 = "0 " + Activity.getString(R.string.ProfileUIRate);
                        else
                            Rating2 = Rating2 + " " + Activity.getString(R.string.ProfileUIRate);

                        String Type = Activity.getString(R.string.ProfileUINormal);

                        if (!Result.isNull("Type"))
                            Type = Result.getString("Type");

                        String Link = Activity.getString(R.string.ProfileUINone);

                        if (!Result.isNull("Link"))
                            Link = Result.getString("Link");

                        if (Link.equals(Activity.getString(R.string.ProfileUINone)))
                            TextViewLink.setTextColor(Misc.Color(R.color.Gray));

                        String Level = "0";

                        if (!Result.isNull("Level"))
                            Level = Result.getString("Level");

                        String Cash = "0";

                        if (!Result.isNull("Cash"))
                            Cash = Result.getString("Cash");

                        String AboutMe = Activity.getString(R.string.ProfileUINone);

                        if (!Result.isNull("About"))
                            AboutMe = Result.getString("About");

                        String Location = Activity.getString(R.string.ProfileUINone);

                        if (!Result.isNull("Location"))
                            Location = Result.getString("Location");

                        if (!Result.isNull("Latitude"))
                            Latitude = Result.getDouble("Latitude");

                        if (!Result.isNull("Longitude"))
                            Longitude = Result.getDouble("Longitude");

                        String Badge = Activity.getString(R.string.ProfileUINone);

                        if (!Result.isNull("Badge"))
                            Badge = Result.getString("Badge");

                        // TODO Hide Text Add Image

                        GlideApp.with(Activity).load(Profile).placeholder(R.drawable._general_avatar).into(CircleImageViewProfile);

                        ContentValues Value = new ContentValues();
                        Value.put(DBHandler.PROFILE_PRIVATE_ID, SharedHandler.GetString("ID"));
                        Value.put(DBHandler.PROFILE_PRIVATE_NAME, Name);
                        Value.put(DBHandler.PROFILE_PRIVATE_PROFILE, Profile);
                        Value.put(DBHandler.PROFILE_PRIVATE_USERNAME, Username);
                        Value.put(DBHandler.PROFILE_PRIVATE_TYPE, Type);
                        Value.put(DBHandler.PROFILE_PRIVATE_PROFILE_COUNT, ProfileCount);
                        Value.put(DBHandler.PROFILE_PRIVATE_FOLLOWING_COUNT, FollowingCount);
                        Value.put(DBHandler.PROFILE_PRIVATE_FOLLOWER_COUNT, FollowerCount);
                        Value.put(DBHandler.PROFILE_PRIVATE_POST_COUNT, PostCount);
                        Value.put(DBHandler.PROFILE_PRIVATE_LEVEL, Level);
                        Value.put(DBHandler.PROFILE_PRIVATE_CASH, Cash);
                        Value.put(DBHandler.PROFILE_PRIVATE_RATING, Rating);
                        Value.put(DBHandler.PROFILE_PRIVATE_BADGE, Badge);
                        Value.put(DBHandler.PROFILE_PRIVATE_ABOUTME, AboutMe);
                        Value.put(DBHandler.PROFILE_PRIVATE_LINK, Link);
                        Value.put(DBHandler.PROFILE_PRIVATE_LOCATION, Location);
                        Value.put(DBHandler.PROFILE_PRIVATE_RATING_COUNT, Rating2);
                        Value.put(DBHandler.PROFILE_PRIVATE_LATITUDE, Latitude);
                        Value.put(DBHandler.PROFILE_PRIVATE_LONGITUDE, Longitude);

                        DB.ProfilePrivate(Value);

                        TextViewName.setText(Name);
                        TextViewUsername.setText(Username);
                        TextViewType.setText(Type);
                        TextViewProfileCount.setText(ProfileCount);
                        TextViewFollowingCount.setText(FollowingCount);
                        TextViewFollowerCount.setText(FollowerCount);
                        TextViewPostCount.setText(PostCount);
                        TextViewLevel2.setText(Level);
                        TextViewCash2.setText(Cash);
                        TextViewRating2.setText(Rating);
                        TextViewBadge3.setText(Badge);
                        TextViewAboutMe.setText(AboutMe);
                        TextViewLink.setText(Link);
                        TextViewLocation.setText(Location);
                        TextViewRating3.setText(Rating2);
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("ProfileUI: " + e.toString());
                }

                LoadingViewMain.Stop();
                LoadingViewMain.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError e)
            {
                LoadingViewMain.Stop();
                LoadingViewMain.setVisibility(View.GONE);
            }
        });*/