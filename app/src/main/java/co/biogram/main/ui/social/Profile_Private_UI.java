package co.biogram.main.ui.social;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import co.biogram.main.R;
import co.biogram.main.databinding.SocialProfilePrivateBinding;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;

public class Profile_Private_UI extends FragmentView
{
    private SocialProfilePrivateBinding Binding;

    @Override
    public void OnCreate()
    {
        Typeface IranSans = Misc.GetTypeface();
        Binding = DataBindingUtil.inflate(LayoutInflater.from(Activity), R.layout.social_profile_private, null, false);

        Binding.TextViewProfile.setTypeface(IranSans);

        Binding.ImageViewSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Switch
            }
        });

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
                // TODO Profile
            }
        });

        Binding.TextViewName.setTypeface(IranSans);
        Binding.TextViewName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Name
            }
        });

        Binding.TextViewUsername.setTypeface(IranSans);
        Binding.TextViewUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Username
            }
        });

        Binding.TextViewFollower.setTypeface(IranSans);
        Binding.TextViewFollower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Follower
            }
        });

        Binding.TextViewFollower2.setTypeface(IranSans);

        Binding.TextViewFollowing.setTypeface(IranSans);
        Binding.TextViewFollowing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Following
            }
        });

        Binding.TextViewFollowing2.setTypeface(IranSans);

        Binding.TextViewVisitor.setTypeface(IranSans);
        Binding.TextViewVisitor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Visitor
            }
        });

        Binding.TextViewVisitor2.setTypeface(IranSans);

        Binding.TextViewRating.setTypeface(IranSans);
        Binding.TextViewRating.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Rating
            }
        });

        Binding.TextViewRating2.setTypeface(IranSans);
        Binding.TextViewHolding.setTypeface(IranSans);

        Binding.TextViewIncrease.setTypeface(IranSans);
        Binding.TextViewIncrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Increase
            }
        });

        Binding.TextViewMoney.setTypeface(IranSans);
        Binding.TextViewRial.setTypeface(IranSans);

        GradientDrawable DrawableCheckOut = new GradientDrawable();
        DrawableCheckOut.setColor(Misc.Color(R.color.Primary));
        DrawableCheckOut.setCornerRadius(Misc.ToDP(10));

        Binding.TextViewCheckOut.setBackground(DrawableCheckOut);
        Binding.TextViewCheckOut.setTypeface(IranSans);
        Binding.TextViewCheckOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO CheckOut
            }
        });

        Binding.TextViewTransAction.setTypeface(IranSans);
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

        Binding.TextViewPhone2.setTypeface(IranSans);
        Binding.TextViewPhone.setTypeface(IranSans);

        Binding.LinearLayoutEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Email
            }
        });

        Binding.TextViewEmail2.setTypeface(IranSans);
        Binding.TextViewEmail.setTypeface(IranSans);

        Binding.LinearLayoutLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Link
            }
        });

        Binding.TextViewLink2.setTypeface(IranSans);
        Binding.TextViewLink.setTypeface(IranSans);

        Binding.LinearLayoutLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Location
            }
        });

        Binding.TextViewLocation2.setTypeface(IranSans);

        Binding.FlexboxLayoutTag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Tag
            }
        });

        Binding.TextViewTag.setTypeface(IranSans);
        Binding.TextViewTag1.setTypeface(IranSans);
        Binding.TextViewTag2.setTypeface(IranSans);
        Binding.TextViewTag3.setTypeface(IranSans);
        Binding.TextViewTag4.setTypeface(IranSans);
        Binding.TextViewTag5.setTypeface(IranSans);
        Binding.TextViewTag6.setTypeface(IranSans);
        Binding.TextViewTag7.setTypeface(IranSans);





        Binding.TextViewName.setText("علی خزایی");
        Binding.TextViewUsername.setText("ali.khazaee");
        Binding.TextViewFollower.setText("52.9K");
        Binding.TextViewFollowing.setText("1920");
        Binding.TextViewVisitor.setText("850K");
        Binding.TextViewRating.setText("4.5");
        Binding.TextViewMoney.setText("20,150,000");
        Binding.TextViewPhone.setText("+989385454764");
        Binding.TextViewPhone.setTextColor(Misc.Color(R.color.Primary));
        Binding.TextViewEmail.setText(Misc.String(R.string.SocialProfilePrivateNone));
        Binding.TextViewEmail.setTextColor(Misc.Color(R.color.Gray));
        Binding.TextViewLink.setText("https://google.com/");
        Binding.TextViewLink.setTextColor(Misc.Color(R.color.Primary));
        Binding.TextViewLocation.setTypeface(IranSans);
        Binding.TextViewLocation.setText(Misc.String(R.string.SocialProfilePrivateNone));
        Binding.TextViewLocation.setTextColor(Misc.Color(R.color.Gray));
        Binding.TextViewTag1.setText("Programmer");
        Binding.TextViewTag2.setText("English");
        Binding.TextViewTag3.setText("HTTP");
        Binding.TextViewTag4.setText("Google");
        Binding.TextViewTag5.setText("معلم");
        Binding.TextViewTag6.setText("استاد دانشگاه");
        Binding.TextViewTag7.setText("سسیس بندری");

        ViewMain = Binding.ScrollViewMain;
    }
}
