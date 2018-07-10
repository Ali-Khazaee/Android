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

import co.biogram.main.ui.component.ImageViewZoom;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;

public class ImagePreviewUI extends FragmentView
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




















    /*
        if (Type == 1)
        {
            final CropImageView CropImageViewMain = new CropImageView(Activity);

            RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewDoneParam.addRule(Misc.Align("L"));

            ImageView ImageViewDone = new ImageView(Activity);
            ImageViewDone.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
            ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone.setLayoutParams(ImageViewDoneParam);
            ImageViewDone.setImageResource(R.drawable.___general_done_white);
            ImageViewDone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CropImageViewMain.setVisibility(View.VISIBLE);
                    CropImageViewMain.setImageBitmap(bitmap);
                }
            });

            RelativeLayoutHeader.addView(ImageViewDone);

            CropImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            CropImageViewMain.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
            CropImageViewMain.setFixedAspectRatio(true);
            CropImageViewMain.setAutoZoomEnabled(true);
            CropImageViewMain.setAspectRatio(1, 1);
            CropImageViewMain.setVisibility(View.GONE);

            RelativeLayoutMain.addView(CropImageViewMain);

            RelativeLayout RelativeLayoutCrop = new RelativeLayout(Activity);
            RelativeLayoutCrop.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));

            CropImageViewMain.addView(RelativeLayoutCrop);

            RelativeLayout.LayoutParams ImageViewDone2Param = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewDone2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewDone2 = new ImageView(Activity);
            ImageViewDone2.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
            ImageViewDone2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone2.setLayoutParams(ImageViewDone2Param);
            ImageViewDone2.setImageResource(R.drawable.___general_done_white);
            ImageViewDone2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        CropImageViewMain.setVisibility(View.GONE);

                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                        CropImageViewMain.getCroppedImage(250, 250).compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                        File ProfileFile = new File(Misc.Temp(), (String.valueOf(System.currentTimeMillis()) + "_imagepreview_crop.jpg"));

                        FileOutputStream FOS = new FileOutputStream(ProfileFile);
                        FOS.write(BAOS.toByteArray());
                        FOS.flush();
                        FOS.close();

                        /*DescriptionUI SignUpDescription = (DescriptionUI) Activity.GetManager().FindByTag("DescriptionUI");

                        if (SignUpDescription != null)
                            SignUpDescription.Update(ProfileFile, false);/

                        Activity.onBackPressed();
                        Activity.onBackPressed();
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("ImagePreviewUI-Type1: " + e.toString());
                    }
                }
            });

            RelativeLayoutCrop.addView(ImageViewDone2);
        }
        else if (Type == 2)
        {
            final GradientDrawable DrawableSelect = new GradientDrawable();
            DrawableSelect.setShape(GradientDrawable.OVAL);
            DrawableSelect.setStroke(Misc.ToDP(2), Color.WHITE);

            final GradientDrawable DrawableSelected = new GradientDrawable();
            DrawableSelected.setShape(GradientDrawable.OVAL);
            DrawableSelected.setColor(ContextCompat.getColor(Activity, R.color.Primary));
            DrawableSelected.setStroke(Misc.ToDP(2), Color.WHITE);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            ViewCircleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
            ViewCircleParam.addRule(RelativeLayout.CENTER_VERTICAL);
            ViewCircleParam.addRule(Misc.Align("L"));

            View ViewCircle = new View(Activity);
            ViewCircle.setLayoutParams(ViewCircleParam);
            ViewCircle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Activity.onBackPressed();

                    if (IsMax)
                    {
                        //Misc.ToastOld(Misc.String(R.string.GalleryViewUIReach));
                        return;
                    }

                    if (Selected)
                    {
                        v.setBackground(DrawableSelect);
                        Selected = false;
                    }
                    else
                    {
                        Selected = true;
                        v.setBackground(DrawableSelected);
                    }

                    SelectListener.OnSelect();
                }
            });

            if (Selected)
                ViewCircle.setBackground(DrawableSelected);
            else
                ViewCircle.setBackground(DrawableSelect);

            RelativeLayoutHeader.addView(ViewCircle);
        }
        else
        {
            RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewOptionParam.addRule(Misc.Align("L"));

            ImageView ImageViewDownload = new ImageView(Activity);
            ImageViewDownload.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
            ImageViewDownload.setLayoutParams(ImageViewOptionParam);
            ImageViewDownload.setImageResource(R.drawable._general_download);
            ImageViewDownload.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    GlideApp.with(Activity).asBitmap().load(ImageList.get(ViewPagerMain.getCurrentItem())).into(new SimpleTarget<Bitmap>()
                    {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition)
                        {
                            try
                            {
                                OutputStream OS = new FileOutputStream(new File(Misc.Dir(Misc.DIR_DOWNLOAD), DateFormat.format("yyyy_mm_dd_hh_mm_ss", new Date().getTime()) + ".jpg"));
                                resource.compress(Bitmap.CompressFormat.PNG, 100, OS);
                                OS.close();

                                //Misc.ToastOld(Misc.String(R.string.ImagePreviewUIDownloaded));
                            }
                            catch (Exception e)
                            {
                                Misc.Debug("ImagePreviewUI-Download: " + e.toString());
                            }
                        }
                    });
                }
            });

            RelativeLayoutHeader.addView(ImageViewDownload);
        }

        if (Anim)
        {
            TranslateAnimation Trans = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
            Trans.setDuration(200);

            RelativeLayoutMain.startAnimation(Trans);
        }

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Color.BLACK);
    }

    @Override
    public void OnPause()
    {
        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Misc.Color(Misc.IsDark() ? R.color.White : R.color.White));
    }

    void SetType(boolean select, boolean isMax, OnSelectListener l)
    {
        Type = 2;
        IsMax = isMax;
        Selected = select;
        SelectListener = l;
    }

    interface OnSelectListener
    {
        void OnSelect();
    }

    */
}
