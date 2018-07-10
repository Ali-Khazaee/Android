package co.biogram.main.ui.general;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.component.ImageViewZoom;

public class ImagePreview_UI extends FragmentView
{
    private ArrayList<Gallery_UI.Struct> ItemList = new ArrayList<>();
    private ConstraintLayout ConstraintLayoutMain;
    private ViewPager ViewPagerMain;
    private int CurrentPosition;

    @Override
    public void OnCreate()
    {
        ViewMain = View.inflate(Activity, R.layout.general_image_preview, null);
        ConstraintLayoutMain = ViewMain.findViewById(R.id.ConstraintLayoutMain);

        ImageView ImageViewClose = ViewMain.findViewById(R.id.ImageViewClose);
        ImageViewClose.setColorFilter(Misc.Color(R.color.White));
        ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

        ViewPagerMain = new ViewPager(Activity)
        {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev)
            {
                try
                {
                    return super.onInterceptTouchEvent(ev);
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        };
        ViewPagerMain.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ViewPagerMain.setAdapter(new ImagePreviewAdapter());

        ((ViewGroup) ViewMain).addView(ViewPagerMain);
        ConstraintLayoutMain.bringToFront();
    }

    @Override
    public void OnResume()
    {
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void SetForGallery(final Gallery_UI.AdapterGallery Adapter, int Position, final OnChoiceListener Listener)
    {
        ItemList = Adapter.ItemList;
        CurrentPosition = Position;

        View ViewSelect = ViewMain.findViewById(R.id.ViewSelect);
        ViewSelect.setBackgroundResource(ItemList.get(Position).Selection ? R.drawable.general_gallery_bg_fill : R.drawable.general_gallery_bg);
        ViewSelect.setVisibility(View.VISIBLE);
        ViewSelect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Listener.OnChoice(CurrentPosition);
                v.setBackgroundResource(ItemList.get(CurrentPosition).Selection ? R.drawable.general_gallery_bg_fill : R.drawable.general_gallery_bg);
                Adapter.notifyItemChanged(CurrentPosition);
            }
        });

        // noinspection all
        ViewPagerMain.getAdapter().notifyDataSetChanged();
        ViewPagerMain.setCurrentItem(Position);
        ViewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override public void onPageScrollStateChanged(int state) { }
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position)
            {
                CurrentPosition = position;
                ViewMain.findViewById(R.id.ViewSelect).setBackgroundResource(ItemList.get(position).Selection ? R.drawable.general_gallery_bg_fill : R.drawable.general_gallery_bg);
            }
        });
    }

    public interface OnChoiceListener
    {
        void OnChoice(int Position);
    }

    private class ImagePreviewAdapter extends PagerAdapter
    {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup Container, int Position)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ImageViewZoom PhotoViewMain = new ImageViewZoom(Activity);
            PhotoViewMain.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            PhotoViewMain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ConstraintLayoutMain.setVisibility(ConstraintLayoutMain.getVisibility() == View.GONE ? View.VISIBLE : View.GONE); } });

            RelativeLayoutMain.addView(PhotoViewMain);

            RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
            LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            final LoadingView LoadingViewMain = new LoadingView(Activity);
            LoadingViewMain.setLayoutParams(LoadingViewMainParam);
            LoadingViewMain.SetColor(R.color.White);
            LoadingViewMain.Start();

            RelativeLayoutMain.addView(LoadingViewMain);

            GlideApp.with(Activity).load(ItemList.get(Position).Path).listener(new RequestListener<Drawable>()
            {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                {
                    LoadingViewMain.Stop();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                {
                    LoadingViewMain.Stop();
                    return false;
                }
            }).into(PhotoViewMain);

            Container.addView(RelativeLayoutMain);

            return RelativeLayoutMain;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup Container, int position, @NonNull Object object)
        {
            Container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {
            return view == object;
        }

        @Override
        public int getCount()
        {
            return ItemList.size();
        }
    }
}
