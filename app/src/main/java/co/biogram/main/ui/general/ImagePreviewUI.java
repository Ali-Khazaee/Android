package co.biogram.main.ui.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.ui.view.PhotoView;
import co.biogram.main.ui.welcome.DescriptionUI;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class ImagePreviewUI extends FragmentBase
{
    private final List<String> UrlList = new ArrayList<>();
    private RelativeLayout RelativeLayoutHeader;
    private OnSelectListener SelectListener;
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

            int Size = MiscHandler.ToDimension(context, 150);

            o.inSampleSize = MiscHandler.SampleSize(o, Size, Size);
            o.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, o);

            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);
            byte[] ByteData = BAOS.toByteArray();

            String PicturePath = CacheHandler.CacheDir(context).getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + "_imagepreview.jpg";
            File PictureFile = new File(PicturePath);

            FileOutputStream FOS = new FileOutputStream(PictureFile);
            FOS.write(ByteData);
            FOS.flush();
            FOS.close();

            Matrix matrix = new Matrix();
            matrix.postRotate(O);

            if (O == -90)
                matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Exception e)
        {
            MiscHandler.Debug("ImagePreviewUI-Data: " + e.toString());
        }
    }

    ImagePreviewUI(Bitmap b)
    {
        bitmap = b;
    }

    ImagePreviewUI(String URL)
    {
        UrlList.add(URL);
    }

    ImagePreviewUI(String URL, String URL2)
    {
        UrlList.add(URL);
        UrlList.add(URL2);
    }

    ImagePreviewUI(String URL, String URL2, String URL3)
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
        RelativeLayoutMain.setBackgroundResource(R.color.Black);
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
            ViewPager ViewPagerMain = new ViewPager(GetActivity())
            {
                @Override
                public boolean onTouchEvent(MotionEvent ev)
                {
                    try
                    {
                        return super.onTouchEvent(ev);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        //
                    }

                    return false;
                }

                @Override
                public boolean onInterceptTouchEvent(MotionEvent ev)
                {
                    try
                    {
                        return super.onInterceptTouchEvent(ev);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        //
                    }

                    return false;
                }
            };
            ViewPagerMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ViewPagerMain.setAdapter(new PreviewAdapter());

            RelativeLayoutMain.addView(ViewPagerMain);
        }

        RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#3f000000"));

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.ImagePreview));

        RelativeLayoutHeader.addView(TextViewTitle);

        if (Type == 1)
        {
            final CropImageView CropImageViewMain = new CropImageView(GetActivity());

            RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
            ImageViewDoneParam.addRule(MiscHandler.Align("L"));

            ImageView ImageViewDone = new ImageView(GetActivity());
            ImageViewDone.setPadding(MiscHandler.ToDimension(GetActivity(), 6), MiscHandler.ToDimension(GetActivity(), 6), MiscHandler.ToDimension(GetActivity(), 6), MiscHandler.ToDimension(GetActivity(), 6));
            ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone.setLayoutParams(ImageViewDoneParam);
            ImageViewDone.setImageResource(R.drawable.ic_done_white);
            ImageViewDone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CropImageViewMain.setVisibility(View.VISIBLE);
                    String ResizeBitmapPath = MediaStore.Images.Media.insertImage(GetActivity().getContentResolver(), bitmap, "Biogram Crop Image", null);
                    CropImageViewMain.setImageUriAsync(Uri.parse(ResizeBitmapPath));
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

            RelativeLayout.LayoutParams ImageViewDone2Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
            ImageViewDone2Param.addRule(MiscHandler.Align("L"));

            ImageView ImageViewDone2 = new ImageView(GetActivity());
            ImageViewDone2.setPadding(MiscHandler.ToDimension(GetActivity(), 6), MiscHandler.ToDimension(GetActivity(), 6), MiscHandler.ToDimension(GetActivity(), 6), MiscHandler.ToDimension(GetActivity(), 6));
            ImageViewDone2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone2.setLayoutParams(ImageViewDone2Param);
            ImageViewDone2.setImageResource(R.drawable.ic_done_white);
            ImageViewDone2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        CropImageViewMain.setVisibility(View.GONE);

                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                        CropImageViewMain.getCroppedImage().compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

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
                        MiscHandler.Debug("ImagePreviewUI-Type1: " + e.toString());
                    }
                }
            });

            CropImageViewMain.addView(ImageViewDone2);
        }
        else if (Type == 2)
        {
            final GradientDrawable DrawableSelect = new GradientDrawable();
            DrawableSelect.setShape(GradientDrawable.OVAL);
            DrawableSelect.setStroke(MiscHandler.ToDimension(GetActivity(), 2), Color.WHITE);

            final GradientDrawable DrawableSelected = new GradientDrawable();
            DrawableSelected.setShape(GradientDrawable.OVAL);
            DrawableSelected.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
            DrawableSelected.setStroke(MiscHandler.ToDimension(GetActivity(), 2), Color.WHITE);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 24), MiscHandler.ToDimension(GetActivity(), 24));
            ViewCircleParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), 0, MiscHandler.ToDimension(GetActivity(), 15), 0);
            ViewCircleParam.addRule(RelativeLayout.CENTER_VERTICAL);
            ViewCircleParam.addRule(MiscHandler.Align("L"));

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
                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GalleryViewReach));
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
            RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
            ImageViewOptionParam.addRule(MiscHandler.Align("L"));

            ImageView ImageViewOption = new ImageView(GetActivity());
            ImageViewOption.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
            ImageViewOption.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewOption.setLayoutParams(ImageViewOptionParam);
            ImageViewOption.setImageResource(R.drawable.ic_more_white);
            ImageViewOption.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Download
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
        @Override
        public Object instantiateItem(ViewGroup Container, int Position)
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

            RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56));
            LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            final LoadingView LoadingViewMain = new LoadingView(GetActivity());
            LoadingViewMain.setLayoutParams(LoadingViewMainParam);
            LoadingViewMain.SetColor(R.color.White);
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
