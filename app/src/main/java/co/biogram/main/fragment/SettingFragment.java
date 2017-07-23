package co.biogram.main.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;

import co.biogram.main.R;
import co.biogram.main.activity.ActivityProfileEdit;
import co.biogram.main.activity.ActivityWelcome;
import co.biogram.main.handler.MiscHandler;

public class SettingFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setClickable(true);
        RelativeLayoutMain.setBackgroundResource(R.color.White);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.SettingFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setId(MiscHandler.GenerateViewID());
        ViewLine.setBackgroundResource(R.color.Gray2);

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(context);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);

        RelativeLayoutMain.addView(ScrollViewMain);

        LinearLayout LinearLayoutMain = new LinearLayout(context);
        LinearLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

        ScrollViewMain.addView(LinearLayoutMain);

        TextView TextViewAccount = new TextView(context);
        TextViewAccount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewAccount.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewAccount.setText(getString(R.string.SettingFragmentAccount));
        TextViewAccount.setTypeface(null, Typeface.BOLD);
        TextViewAccount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewAccount.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20));

        LinearLayoutMain.addView(TextViewAccount);

        View ViewLine2 = new View(context);
        ViewLine2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 2)));
        ViewLine2.setBackgroundResource(R.color.Gray);

        LinearLayoutMain.addView(ViewLine2);

        TextView TextViewPassword = new TextView(context);
        TextViewPassword.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewPassword.setText(getString(R.string.SettingFragmentChangePassword));
        TextViewPassword.setTypeface(null, Typeface.BOLD);
        TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPassword.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        TextViewPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, new ChangePasswordFragment()).addToBackStack("ChangePasswordFragment").commit();
            }
        });

        LinearLayoutMain.addView(TextViewPassword);

        TextView TextViewEditProfile = new TextView(context);
        TextViewEditProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewEditProfile.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewEditProfile.setText(getString(R.string.SettingFragmentEditProfile));
        TextViewEditProfile.setTypeface(null, Typeface.BOLD);
        TextViewEditProfile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewEditProfile.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        TextViewEditProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(context, ActivityProfileEdit.class));
            }
        });

        LinearLayoutMain.addView(TextViewEditProfile);

        TextView TextViewLanguage = new TextView(context);
        TextViewLanguage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLanguage.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewLanguage.setText(getString(R.string.SettingFragmentLanguage));
        TextViewLanguage.setTypeface(null, Typeface.BOLD);
        TextViewLanguage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLanguage.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        TextViewLanguage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MiscHandler.Toast(context, getString(R.string.Soon));
            }
        });

        LinearLayoutMain.addView(TextViewLanguage);

        TextView TextViewContact = new TextView(context);
        TextViewContact.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewContact.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewContact.setText(getString(R.string.SettingFragmentContact));
        TextViewContact.setTypeface(null, Typeface.BOLD);
        TextViewContact.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewContact.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewContact);

        TextView TextViewLogout = new TextView(context);
        TextViewLogout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLogout.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewLogout.setText(getString(R.string.SettingFragmentLogout));
        TextViewLogout.setTypeface(null, Typeface.BOLD);
        TextViewLogout.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLogout.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        TextViewLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LinearLayout LinearLayoutMain = new LinearLayout(context);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitle = new TextView(context);
                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black2));
                TextViewTitle.setText(getString(R.string.SettingFragmentLogoutMessage));
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewTitle.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray2);

                LinearLayoutMain.addView(ViewLine);

                LinearLayout LinearLayoutChoice = new LinearLayout(context);
                LinearLayoutChoice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutChoice.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayoutMain.addView(LinearLayoutChoice);

                TextView TextViewYes = new TextView(context);
                TextViewYes.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextViewYes.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewYes.setText(getString(R.string.SettingFragmentLogoutYes));
                TextViewYes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewYes.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                TextViewYes.setGravity(Gravity.CENTER);

                LinearLayoutChoice.addView(TextViewYes);

                TextView TextViewNo = new TextView(context);
                TextViewNo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextViewNo.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewNo.setText(getString(R.string.SettingFragmentLogoutNo));
                TextViewNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewNo.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                TextViewNo.setGravity(Gravity.CENTER);

                LinearLayoutChoice.addView(TextViewNo);

                final Dialog DialogLogout = new Dialog(getActivity());
                DialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLogout.setContentView(LinearLayoutMain);
                DialogLogout.show();

                TextViewYes.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        context.getSharedPreferences("BioGram", Context.MODE_PRIVATE).edit().clear().apply();

                        File SharedDir = new File(context.getFilesDir().getPath() + context.getPackageName() + "/shared_prefs/");
                        File[] SharedFiles = SharedDir.listFiles();

                        for (File file : SharedFiles)
                            file.delete();

                        getActivity().startActivity(new Intent(context, ActivityWelcome.class));

                        DialogLogout.dismiss();
                    }
                });

                TextViewNo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogLogout.dismiss();
                    }
                });
            }
        });

        LinearLayoutMain.addView(TextViewLogout);

        TextView TextViewAbout = new TextView(context);
        TextViewAbout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewAbout.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewAbout.setText(getString(R.string.SettingFragmentAbout));
        TextViewAbout.setTypeface(null, Typeface.BOLD);
        TextViewAbout.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewAbout.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20));

        LinearLayoutMain.addView(TextViewAbout);

        View ViewLine3 = new View(context);
        ViewLine3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 2)));
        ViewLine3.setBackgroundResource(R.color.Gray);

        LinearLayoutMain.addView(ViewLine3);

        TextView TextViewReport = new TextView(context);
        TextViewReport.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewReport.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewReport.setText(getString(R.string.SettingFragmentReportProblem));
        TextViewReport.setTypeface(null, Typeface.BOLD);
        TextViewReport.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewReport.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewReport);

        TextView TextViewTerms = new TextView(context);
        TextViewTerms.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewTerms.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewTerms.setText(getString(R.string.SettingFragmentTerms));
        TextViewTerms.setTypeface(null, Typeface.BOLD);
        TextViewTerms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTerms.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewTerms);

        TextView TextViewPrivacy = new TextView(context);
        TextViewPrivacy.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewPrivacy.setText(getString(R.string.SettingFragmentPolicy));
        TextViewPrivacy.setTypeface(null, Typeface.BOLD);
        TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewPrivacy);

        TextView TextViewHelpCenter = new TextView(context);
        TextViewHelpCenter.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewHelpCenter.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewHelpCenter.setText(getString(R.string.SettingFragmentHelpCenter));
        TextViewHelpCenter.setTypeface(null, Typeface.BOLD);
        TextViewHelpCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewHelpCenter.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewHelpCenter);

        TextView TextViewBlog = new TextView(context);
        TextViewBlog.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewBlog.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewBlog.setText(getString(R.string.SettingFragmentBlog));
        TextViewBlog.setTypeface(null, Typeface.BOLD);
        TextViewBlog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewBlog.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewBlog);

        TextView TextViewAds = new TextView(context);
        TextViewAds.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewAds.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewAds.setText(getString(R.string.SettingFragmentAds));
        TextViewAds.setTypeface(null, Typeface.BOLD);
        TextViewAds.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewAds.setPadding(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        LinearLayoutMain.addView(TextViewAds);

        return RelativeLayoutMain;
    }
}
