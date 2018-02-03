package co.biogram.main.ui.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import com.bumptech.glide.request.transition.Transition;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.PhotoView;
import co.biogram.main.ui.welcome.DescriptionUI;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

public class ImagePreviewUI extends FragmentView
{
    private final List<String> UrlList = new ArrayList<>();
    private RelativeLayout RelativeLayoutHeader;
    private OnSelectListener SelectListener;
    private ViewPager ViewPagerMain;
    private boolean Selected = false;
    private Bitmap bitmap = null;
    private boolean IsMax = false;
    private int Type = 0;

    ImagePreviewUI(Context context, byte[] data, int O)
    {
        try
        {
            Type = 1;

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(data, 0, data.length, o);

            int Size = Misc.ToDP(150);

            o.inSampleSize = Misc.SampleSize(o, Size, Size);
            o.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, o);

            Matrix matrix = new Matrix();
            matrix.postRotate(O);

            if (O == -90)
                matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Exception e)
        {
            Misc.Debug("ImagePreviewUI-Data: " + e.toString());
        }
    }

    public ImagePreviewUI(String URL)
    {
        UrlList.add(URL);
    }

    public ImagePreviewUI(String URL, String URL2)
    {
        UrlList.add(URL);
        UrlList.add(URL2);
    }

    public ImagePreviewUI(String URL, String URL2, String URL3)
    {
        UrlList.add(URL);
        UrlList.add(URL2);
        UrlList.add(URL3);
    }

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextWhite);
        RelativeLayoutMain.setClickable(true);

        if (bitmap != null)
        {
            PhotoView PhotoViewMain = new PhotoView(GetActivity());
            PhotoViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            PhotoViewMain.setImageBitmap(bitmap);
            PhotoViewMain.setOnClickListener(new View.OnClickListener()
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

            RelativeLayoutMain.addView(PhotoViewMain);
        }
        else
        {
            ViewPagerMain = new ViewPager(GetActivity())
            {
                @Override
                public boolean onTouchEvent(MotionEvent ev)
                {
                    try
                    {
                        return super.onTouchEvent(ev);
                    }
                    catch (Exception e)
                    {
                        return false;
                    }
                }

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
            ViewPagerMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ViewPagerMain.setAdapter(new PreviewAdapter());

            RelativeLayoutMain.addView(ViewPagerMain);
        }

        RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#3f000000"));

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_white_rtl : R.drawable.back_white);
        ImageViewBack.setId(Misc.ViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.ImagePreviewUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        if (Type == 1)
        {
            final CropImageView CropImageViewMain = new CropImageView(GetActivity());

            RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewDoneParam.addRule(Misc.Align("L"));

            ImageView ImageViewDone = new ImageView(GetActivity());
            ImageViewDone.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
            ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone.setLayoutParams(ImageViewDoneParam);
            ImageViewDone.setImageResource(R.drawable.done_white);
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

            RelativeLayout RelativeLayoutCrop = new RelativeLayout(GetActivity());
            RelativeLayoutCrop.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));

            CropImageViewMain.addView(RelativeLayoutCrop);

            RelativeLayout.LayoutParams ImageViewDone2Param = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewDone2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewDone2 = new ImageView(GetActivity());
            ImageViewDone2.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
            ImageViewDone2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone2.setLayoutParams(ImageViewDone2Param);
            ImageViewDone2.setImageResource(R.drawable.done_white);
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

                        File ProfileFile = new File(CacheHandler.CacheDir(GetActivity()), (String.valueOf(System.currentTimeMillis()) + "_imagepreview_crop.jpg"));

                        FileOutputStream FOS = new FileOutputStream(ProfileFile);
                        FOS.write(BAOS.toByteArray());
                        FOS.flush();
                        FOS.close();

                        DescriptionUI SignUpDescription = (DescriptionUI) GetActivity().GetManager().FindByTag("DescriptionUI");
                        SignUpDescription.Update(ProfileFile, false);

                        GetActivity().onBackPressed();
                        GetActivity().onBackPressed();
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
            DrawableSelected.setColor(ContextCompat.getColor(GetActivity(), R.color.Primary));
            DrawableSelected.setStroke(Misc.ToDP(2), Color.WHITE);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            ViewCircleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
            ViewCircleParam.addRule(RelativeLayout.CENTER_VERTICAL);
            ViewCircleParam.addRule(Misc.Align("L"));

            View ViewCircle = new View(GetActivity());
            ViewCircle.setLayoutParams(ViewCircleParam);
            ViewCircle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    GetActivity().onBackPressed();

                    if (IsMax)
                    {
                        Misc.Toast( GetActivity().getString(R.string.GalleryViewUIReach));
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

            ImageView ImageViewOption = new ImageView(GetActivity());
            ImageViewOption.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
            ImageViewOption.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewOption.setLayoutParams(ImageViewOptionParam);
            ImageViewOption.setImageResource(R.drawable.more_white);
            ImageViewOption.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PopupMenu PopMenu = new PopupMenu(GetActivity(), v);
                    PopMenu.getMenu().add(0, 0, 0, GetActivity().getString(R.string.ImagePreviewUIDownload));
                    PopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            if (item.getItemId() == 0)
                            {
                                GlideApp.with(GetActivity())
                                .asBitmap()
                                .load(UrlList.get(ViewPagerMain.getCurrentItem()))
                                .into(new SimpleTarget<Bitmap>()
                                {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition)
                                    {
                                        try
                                        {
                                            File Download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                            Download.mkdir();

                                            File Biogram = new File(Download, "Biogram");
                                            Biogram.mkdir();

                                            CharSequence Name = DateFormat.format("yyyy_mm_dd_hh_mm_ss", new Date().getTime());

                                            File ImageFile = new File(Biogram, Name + ".jpg");
                                            OutputStream OS = new FileOutputStream(ImageFile);
                                            resource.compress(Bitmap.CompressFormat.JPEG, 100, OS);

                                            Misc.Toast( GetActivity().getString(R.string.ImagePreviewUIDownloaded));
                                        }
                                        catch (Exception e)
                                        {
                                            Misc.Debug("ImagePreviewUI-Download: " + e.toString());
                                        }
                                    }
                                });
                            }

                            return false;
                        }
                    });
                    PopMenu.show();
                }
            });

            RelativeLayoutHeader.addView(ImageViewOption);
        }

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        GetActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        GetActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private class PreviewAdapter extends PagerAdapter
    {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup Container, int Position)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            PhotoView PhotoViewMain = new PhotoView(GetActivity());
            PhotoViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            PhotoViewMain.setOnClickListener(new View.OnClickListener()
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

            RelativeLayoutMain.addView(PhotoViewMain);

            RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
            LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            final LoadingView LoadingViewMain = new LoadingView(GetActivity());
            LoadingViewMain.setLayoutParams(LoadingViewMainParam);
            LoadingViewMain.SetColor(R.color.TextDark);
            LoadingViewMain.Start();

            RelativeLayoutMain.addView(LoadingViewMain);

            GlideApp.with(GetActivity())
            .load(UrlList.get(Position))
            .listener(new RequestListener<Drawable>()
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
            })
            .into(PhotoViewMain);

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
            return UrlList.size();
        }
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
}
