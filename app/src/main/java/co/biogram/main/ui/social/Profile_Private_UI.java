package co.biogram.main.ui.social;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;

public class Profile_Private_UI extends FragmentView
{
    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.social_profile_private, null);
        Typeface IranSans = Misc.GetTypeface();

        TextView TextViewProfile = view.findViewById(R.id.TextViewProfile);
        TextViewProfile.setTypeface(IranSans);

        TextView TextViewName = view.findViewById(R.id.TextViewName);
        TextViewName.setTypeface(IranSans);

        TextView TextViewUsername = view.findViewById(R.id.TextViewUsername);
        TextViewUsername.setTypeface(IranSans);

        TextView TextViewFollower = view.findViewById(R.id.TextViewFollower);
        TextViewFollower.setTypeface(IranSans);

        TextView TextViewFollower2 = view.findViewById(R.id.TextViewFollower2);
        TextViewFollower2.setTypeface(IranSans);

        TextView TextViewFollowing = view.findViewById(R.id.TextViewFollowing);
        TextViewFollowing.setTypeface(IranSans);

        TextView TextViewFollowing2 = view.findViewById(R.id.TextViewFollowing2);
        TextViewFollowing2.setTypeface(IranSans);

        TextView TextViewVisitor = view.findViewById(R.id.TextViewVisitor);
        TextViewVisitor.setTypeface(IranSans);

        TextView TextViewVisitor2 = view.findViewById(R.id.TextViewVisitor2);
        TextViewVisitor2.setTypeface(IranSans);

        TextView TextViewHolding = view.findViewById(R.id.TextViewHolding);
        TextViewHolding.setTypeface(IranSans);

        TextView TextViewIncrease = view.findViewById(R.id.TextViewIncrease);
        TextViewIncrease.setTypeface(IranSans);

        TextView TextViewMoney = view.findViewById(R.id.TextViewMoney);
        TextViewMoney.setTypeface(IranSans);

        TextView TextViewRial = view.findViewById(R.id.TextViewRial);
        TextViewRial.setTypeface(IranSans);

        GradientDrawable DrawableCheckOut = new GradientDrawable();
        DrawableCheckOut.setColor(Misc.Color(R.color.Primary));
        DrawableCheckOut.setCornerRadius(Misc.ToDP(10));

        TextView TextViewCheckOut = view.findViewById(R.id.TextViewCheckOut);
        TextViewCheckOut.setBackground(DrawableCheckOut);
        TextViewCheckOut.setTypeface(IranSans);

        TextView TextViewTransAction = view.findViewById(R.id.TextViewTransAction);
        TextViewTransAction.setTypeface(IranSans);


        ViewMain = view;
    }
}
