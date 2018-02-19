package co.biogram.main.ui.social;

import android.view.View;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.TextView;

public class ProfileUI extends FragmentView
{
    public ProfileUI()
    {

    }

    public ProfileUI(String username)
    {

    }

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextDark);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout.LayoutParams TextViewThemeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(50));
        TextViewThemeParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTheme = new TextView(GetActivity() , 16, false);
        TextViewTheme.setLayoutParams(TextViewThemeParam);
        TextViewTheme.setText(("Theme"));
        TextViewTheme.setId(Misc.ViewID());
        TextViewTheme.setBackgroundResource(R.color.Gray);
        TextViewTheme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Misc.ChangeTheme();
            }
        });

        RelativeLayoutMain.addView(TextViewTheme);

        RelativeLayout.LayoutParams TextViewLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(50));
        TextViewLanguageParam.addRule(RelativeLayout.BELOW, TextViewTheme.getId());

        TextView TextViewLanguage = new TextView(GetActivity() , 16, false);
        TextViewLanguage.setLayoutParams(TextViewLanguageParam);
        TextViewLanguage.setText(("Language"));
        TextViewLanguage.setBackgroundResource(R.color.Gray);
        TextViewLanguage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SharedHandler.GetString("Language").equals("fa"))
                    Misc.ChangeLanguage("en");
                else
                    Misc.ChangeLanguage("fa");
            }
        });

        RelativeLayoutMain.addView(TextViewLanguage);

        ViewMain = RelativeLayoutMain;
    }
}
