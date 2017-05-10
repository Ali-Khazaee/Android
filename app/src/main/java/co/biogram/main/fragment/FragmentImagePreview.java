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

import com.androidnetworking.AndroidNetworking;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.misc.TouchImageView;

public class FragmentImagePreview extends Fragment
{
    private Bitmap ImageCache = null;
    private RelativeLayout Header;
    private List<String> ImageList = new ArrayList<>();

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

        RelativeLayout Main = new RelativeLayout(context);
        Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Main.setBackgroundColor(ContextCompat.getColor(context, R.color.Black));

        ViewPager Pager = new ViewPager(context);
        Pager.setAdapter(new ViewPagerAdapter());

        Main.addView(Pager);

        Header = new RelativeLayout(context);
        Header.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(56)));
        Header.setBackgroundColor(Color.parseColor("#3f000000"));

        Main.addView(Header);

        ImageView Back = new ImageView(context);
        Back.setPadding(MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12));
        Back.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Back.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(56), MiscHandler.ToDimension(56)));
        Back.setImageResource(R.drawable.ic_back_white);
        Back.setId(MiscHandler.GenerateViewID());
        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentImagePreview.this).commit();
            }
        });

        Header.addView(Back);

        RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        NameParam.addRule(RelativeLayout.RIGHT_OF, Back.getId());
        NameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        TextView Title = new TextView(context);
        Title.setLayoutParams(NameParam);
        Title.setTextColor(ContextCompat.getColor(context, R.color.White));
        Title.setText(getString(R.string.FragmentMomentWriteImagePreview));
        Title.setTypeface(null, Typeface.BOLD);
        Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        Header.addView(Title);

        return Main;
    }

    public void SetBitmap(Bitmap bitmap)
    {
        ImageCache = bitmap;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        AndroidNetworking.cancel("FragmentImagePreview");
    }

    private class ViewPagerAdapter extends PagerAdapter
    {
        @Override
        public Object instantiateItem(ViewGroup Container, int Position)
        {
            TouchImageView Image = new TouchImageView(getActivity());
            Image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            if (ImageCache != null)
                Image.setImageBitmap(ImageCache);
            else
                RequestHandler.Core().LoadImage(Image, ImageList.get(Position), "FragmentImagePreview", true);

            Container.addView(Image);

            Image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (Header.getVisibility() == View.GONE)
                        Header.setVisibility(View.VISIBLE);
                    else
                        Header.setVisibility(View.GONE);
                }
            });

            return Image;
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
