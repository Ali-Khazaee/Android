package co.biogram.main.ui.social;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import co.biogram.main.R;
import co.biogram.main.databinding.SocialProfilePrivateBinding;
import co.biogram.main.fragment.FragmentDialog;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.Permission_UI;
import co.biogram.main.ui.general.Gallery_UI;

public class Profile_Private_UI extends FragmentView
{
    private SocialProfilePrivateBinding Binding;

    @Override
    public void OnCreate()
    {
        Binding = DataBindingUtil.inflate(LayoutInflater.from(Activity), R.layout.social_profile_private, null, false);

        Binding.ImageViewSwitch.setColorFilter(Misc.Color(R.color.Black));
        Binding.ImageViewSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Switch
            }
        });

        Binding.ImageViewSetting.setColorFilter(Misc.Color(R.color.Black));
        Binding.ImageViewSetting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Setting
            }
        });

        Binding.CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenDialog(new DialogProfile());
            }
        });

        Binding.TextViewName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Name
            }
        });

        Binding.TextViewUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Username
            }
        });

        Binding.TextViewFollower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Follower
            }
        });

        Binding.TextViewFollowing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Following
            }
        });

        Binding.TextViewVisitor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Visitor
            }
        });

        Binding.TextViewRating.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Rating
            }
        });

        Binding.TextViewIncrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Increase
            }
        });

        GradientDrawable DrawableCheckOut = new GradientDrawable();
        DrawableCheckOut.setColor(Misc.Color(R.color.Primary));
        DrawableCheckOut.setCornerRadius(Misc.ToDP(10));

        Binding.TextViewCheckOut.setBackground(DrawableCheckOut);
        Binding.TextViewCheckOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO CheckOut
            }
        });

        Binding.TextViewTransAction.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO CheckOut
            }
        });

        Binding.LinearLayoutPhone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Phone
            }
        });

        Binding.LinearLayoutEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Email
            }
        });

        Binding.LinearLayoutLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Link
            }
        });

        Binding.LinearLayoutLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Location
            }
        });

        Binding.FlexboxLayoutTag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Tag
            }
        });

        ViewMain = Binding.ScrollViewMain;

        FetchUpdate();
    }

    @Override
    public void OnOpen()
    {
        FetchUpdate();
    }

    private void FetchUpdate()
    {
        GlideApp.with(Activity).load("http://icons.iconarchive.com/icons/iconarchive/blue-election/128/Election-Badge-Outline-icon.png1").placeholder(R.drawable.social_profile_private).into(Binding.CircleImageViewProfile);
        Binding.TextViewName.setText("علی خزایی");
        Binding.TextViewUsername.setText("ali.khazaee");
        GlideApp.with(Activity).load("http://icons.iconarchive.com/icons/iconarchive/blue-election/128/Election-Badge-Outline-icon.png").into(Binding.ImageViewBadge);
        Binding.TextViewFollower.setText("52.9K");
        Binding.TextViewFollowing.setText("1920");
        Binding.TextViewVisitor.setText("850K");
        Binding.TextViewRating.setText("4.5");
        Binding.TextViewMoney.setText("20,150,000");
        Binding.TextViewDescription.setText("بودن یا نبودن مسئله این است");
        Binding.TextViewPhone.setText("+989385454764");
        Binding.TextViewPhone.setTextColor(Misc.Color(R.color.Primary));
        Binding.TextViewEmail.setText(Misc.String(R.string.SocialProfilePrivateNone));
        Binding.TextViewEmail.setTextColor(Misc.Color(R.color.Gray));
        Binding.TextViewLink.setText("https://google.com/");
        Binding.TextViewLink.setTextColor(Misc.Color(R.color.Primary));
        Binding.TextViewLocation.setText(Misc.String(R.string.SocialProfilePrivateNone));
        Binding.TextViewLocation.setTextColor(Misc.Color(R.color.Gray));
        Binding.TextViewTag1.setText("Programmer");
        Binding.TextViewTag2.setText("English");
        Binding.TextViewTag3.setText("HTTP");
        Binding.TextViewTag4.setText("Google");
        Binding.TextViewTag5.setText("معلم");
        Binding.TextViewTag6.setText("استاد دانشگاه");
        Binding.TextViewTag7.setText("سسیس بندری");
    }

    private class DialogProfile extends FragmentDialog
    {
        private ConstraintLayout ConstraintLayoutMain;

        @Override
        public void OnCreate()
        {
            ViewMain = View.inflate(Activity, R.layout.social_profile_private_profile, null);

            ViewMain.findViewById(R.id.ImageViewClose).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

            ViewMain.findViewById(R.id.TextViewGallery).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Activity.GetManager().OpenDialog(new Permission_UI(R.drawable.general_permission_storage, R.string.GeneralPermissionMessageStorageRead, Manifest.permission.READ_EXTERNAL_STORAGE, new Permission_UI.OnPermissionListener()
                    {
                        @Override
                        public void OnPermission(boolean Result)
                        {
                            if (!Result)
                                return;

                            Activity.GetManager().OpenView(new Gallery_UI(1, Gallery_UI.TYPE_IMAGE, new Gallery_UI.OnGalleryListener()
                            {
                                @Override
                                public void OnAdd(String path)
                                {

                                }

                                @Override
                                public void OnRemove(String path)
                                {

                                }

                                @Override
                                public void OnDone()
                                {

                                }
                            }), "Gallery_UI");

                        }
                    }));
                }
            });

            ViewMain.findViewById(R.id.TextViewCamera).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Activity.onBackPressed();
                }
            });

            ViewMain.findViewById(R.id.TextViewRemove).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Activity.onBackPressed();
                }
            });

            Animation Anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            Anim.setDuration(300);

            ConstraintLayoutMain = ViewMain.findViewById(R.id.ConstraintLayoutMain);
            ConstraintLayoutMain.startAnimation(Anim);
        }

        @Override
        public void OnDestroy()
        {
            Animation Anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            Anim.setAnimationListener(new Animation.AnimationListener()
            {
                @Override public void onAnimationStart(Animation animation) { }
                @Override public void onAnimationRepeat(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    DialogProfile.super.OnDestroy();
                }
            });
            Anim.setDuration(300);

            ConstraintLayoutMain.startAnimation(Anim);
        }
    }
}
