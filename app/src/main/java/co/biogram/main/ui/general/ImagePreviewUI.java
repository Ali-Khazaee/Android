package co.biogram.main.ui.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
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

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.PhotoView;
import co.biogram.main.ui.view.TextView;
import co.biogram.main.ui.welcome.DescriptionUI;

public class ImagePreviewUI extends FragmentView {
    private List<String> UrlList = new ArrayList<>();
    private RelativeLayout RelativeLayoutHeader;
    private OnSelectListener SelectListener;
    private ViewPager ViewPagerMain;
    private boolean Selected = false;
    private boolean Anim = false;
    private Bitmap bitmap = null;
    private boolean IsMax = false;
    private int Type = 0;

    ImagePreviewUI(byte[] Data, int O) {
        try {
            Type = 1;

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(Data, 0, Data.length, o);

            int Size = Misc.ToDP(150);

            o.inSampleSize = Misc.SampleSize(o, Size, Size);
            o.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeByteArray(Data, 0, Data.length, o);

            Matrix matrix = new Matrix();
            matrix.postRotate(O);

            if (O == -90)
                matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            Misc.Debug("ImagePreviewUI: " + e.toString());
        }
    }

    public ImagePreviewUI(String URL, boolean anim) {
        Anim = anim;
        UrlList.add(URL);
    }

    public ImagePreviewUI(String URL, String URL2, boolean anim) {
        Anim = anim;
        UrlList.add(URL);
        UrlList.add(URL2);
    }

    public ImagePreviewUI(String URL, String URL2, String URL3, boolean anim) {
        Anim = anim;
        UrlList.add(URL);
        UrlList.add(URL2);
        UrlList.add(URL3);
    }

    @Override
    public void OnCreate() {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextWhite);
        RelativeLayoutMain.setClickable(true);

        if (bitmap != null) {
            PhotoView PhotoViewMain = new PhotoView(Activity);
            PhotoViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            PhotoViewMain.setImageBitmap(bitmap);
            PhotoViewMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RelativeLayoutHeader.getVisibility() == View.GONE)
                        RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    else
                        RelativeLayoutHeader.setVisibility(View.GONE);
                }
            });

            RelativeLayoutMain.addView(PhotoViewMain);
        } else {
            ViewPagerMain = new ViewPager(Activity) {
                @Override
                public boolean onTouchEvent(MotionEvent ev) {
                    try {
                        return super.onTouchEvent(ev);
                    } catch (Exception e) {
                        return false;
                    }
                }

                @Override
                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    try {
                        return super.onInterceptTouchEvent(ev);
                    } catch (Exception e) {
                        return false;
                    }
                }
            };
            ViewPagerMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ViewPagerMain.setAdapter(new PreviewAdapter());

            RelativeLayoutMain.addView(ViewPagerMain);
        }

        RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#3f000000"));

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.z_general_back_white : R.drawable.z_general_back_white);
        ImageViewBack.setId(Misc.generateViewId());
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.ImagePreviewUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        if (Type == 1) {
            final CropImageView CropImageViewMain = new CropImageView(Activity);

            RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewDoneParam.addRule(Misc.Align("L"));

            ImageView ImageViewDone = new ImageView(Activity);
            ImageViewDone.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
            ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone.setLayoutParams(ImageViewDoneParam);
            ImageViewDone.setImageResource(R.drawable.___general_done_white);
            ImageViewDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            ImageViewDone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CropImageViewMain.setVisibility(View.GONE);

                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                        CropImageViewMain.getCroppedImage(250, 250).compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                        File ProfileFile = new File(Misc.Temp(), (String.valueOf(System.currentTimeMillis()) + "_imagepreview_crop.jpg"));

                        FileOutputStream FOS = new FileOutputStream(ProfileFile);
                        FOS.write(BAOS.toByteArray());
                        FOS.flush();
                        FOS.close();

                        DescriptionUI SignUpDescription = (DescriptionUI) Activity.GetManager().FindByTag("DescriptionUI");

                        if (SignUpDescription != null)
                            SignUpDescription.Update(ProfileFile, false);

                        Activity.onBackPressed();
                        Activity.onBackPressed();
                    } catch (Exception e) {
                        Misc.Debug("ImagePreviewUI-Type1: " + e.toString());
                    }
                }
            });

            RelativeLayoutCrop.addView(ImageViewDone2);
        } else if (Type == 2) {
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
            ViewCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity.onBackPressed();

                    if (IsMax) {
                        Misc.ToastOld(Misc.String(R.string.GalleryViewUIReach));
                        return;
                    }

                    if (Selected) {
                        v.setBackground(DrawableSelect);
                        Selected = false;
                    } else {
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
        } else {
            RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
            ImageViewOptionParam.addRule(Misc.Align("L"));

            ImageView ImageViewDownload = new ImageView(Activity);
            ImageViewDownload.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
            ImageViewDownload.setLayoutParams(ImageViewOptionParam);
            ImageViewDownload.setImageResource(R.drawable._general_download);
            ImageViewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlideApp.with(Activity)
                            .asBitmap()
                            .load(UrlList.get(ViewPagerMain.getCurrentItem()))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    try {
                                        OutputStream OS = new FileOutputStream(new File(Misc.Dir(Misc.DIR_DOWNLOAD), DateFormat.format("yyyy_mm_dd_hh_mm_ss", new Date().getTime()) + ".jpg"));
                                        resource.compress(Bitmap.CompressFormat.PNG, 100, OS);
                                        OS.close();

                                        Misc.ToastOld(Misc.String(R.string.ImagePreviewUIDownloaded));
                                    } catch (Exception e) {
                                        Misc.Debug("ImagePreviewUI-Download: " + e.toString());
                                    }
                                }
                            });
                }
            });

            RelativeLayoutHeader.addView(ImageViewDownload);
        }

        if (Anim) {
            TranslateAnimation Trans = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
            Trans.setDuration(200);

            RelativeLayoutMain.startAnimation(Trans);
        }

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Color.BLACK);
    }

    @Override
    public void OnPause() {
        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Misc.Color(Misc.IsDark() ? R.color.StatusBarDark : R.color.StatusBarWhite));
    }

    void SetType(boolean select, boolean isMax, OnSelectListener l) {
        Type = 2;
        IsMax = isMax;
        Selected = select;
        SelectListener = l;
    }

    interface OnSelectListener {
        void OnSelect();
    }

    private class PreviewAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup Container, int Position) {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            PhotoView PhotoViewMain = new PhotoView(Activity);
            PhotoViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            PhotoViewMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RelativeLayoutHeader.getVisibility() == View.GONE)
                        RelativeLayoutHeader.setVisibility(View.VISIBLE);
                    else
                        RelativeLayoutHeader.setVisibility(View.GONE);
                }
            });

            RelativeLayoutMain.addView(PhotoViewMain);

            RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
            LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            final LoadingView LoadingViewMain = new LoadingView(Activity);
            LoadingViewMain.setLayoutParams(LoadingViewMainParam);
            LoadingViewMain.SetColor(R.color.TextDark);
            LoadingViewMain.Start();

            RelativeLayoutMain.addView(LoadingViewMain);

            GlideApp.with(Activity)
                    .load(UrlList.get(Position))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            LoadingViewMain.Stop();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            LoadingViewMain.Stop();
                            return false;
                        }
                    })
                    .into(PhotoViewMain);

            Container.addView(RelativeLayoutMain);

            return RelativeLayoutMain;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup Container, int position, @NonNull Object object) {
            Container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return UrlList.size();
        }
    }
}
