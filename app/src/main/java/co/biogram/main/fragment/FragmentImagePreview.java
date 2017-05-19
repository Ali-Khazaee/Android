package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.misc.TouchImageView;

public class FragmentImagePreview extends Fragment
{
    private Bitmap ImageCache = null;
    private RelativeLayout RelativeLayoutHeader;
    private final List<String> ImageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (getArguments() != null)
        {
            if (!getArguments().getString("URL", "").equals(""))
                ImageList.add(getArguments().getString("URL"));

            if (!getArguments().getString("URL2", "").equals(""))
                ImageList.add(getArguments().getString("URL2"));

            if (!getArguments().getString("URL3", "").equals(""))
                ImageList.add(getArguments().getString("URL3"));
        }

        Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.Black);

        ViewPager ViewPagerPreview = new ViewPager(context);
        ViewPagerPreview.setAdapter(new ViewPagerAdapter());

        RelativeLayoutMain.addView(ViewPagerPreview);

        RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#3f000000"));

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_white);
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
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.White));
        TextViewTitle.setText(getString(R.string.FragmentImagePreview));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        RequestHandler.Core().Cancel("FragmentImagePreview");
    }

    public void SetBitmap(Bitmap bitmap)
    {
        ImageCache = bitmap;
    }

    private class ViewPagerAdapter extends PagerAdapter
    {
        @Override
        public Object instantiateItem(ViewGroup Container, int Position)
        {
            TouchImageView ImagePreview = new TouchImageView(getActivity());
            ImagePreview.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            if (ImageCache != null)
                ImagePreview.setImageBitmap(ImageCache);
            else
                RequestHandler.Core().LoadImage(ImagePreview, ImageList.get(Position), "FragmentImagePreview", true);

            Container.addView(ImagePreview);

            ImagePreview.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (RelativeLayoutHeader.getVisibility() == View.GONE)
                        RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    else
                        RelativeLayoutHeader.setVisibility(View.GONE);
                }
            });

            return ImagePreview;
        }

        @Override
        public void destroyItem(ViewGroup Container, int position, Object object)
        {
            Container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public int getCount()
        {
            if (ImageCache != null)
                return 1;

            return ImageList.size();
        }
    }
}
