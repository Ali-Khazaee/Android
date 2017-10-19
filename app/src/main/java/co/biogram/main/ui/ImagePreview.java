package co.biogram.main.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
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

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.misc.TouchImageView;
import co.biogram.main.view.LoadingView;
import co.biogram.main.view.TextView;

class ImagePreview extends FragmentBase
{
    private final List<String> UrlList = new ArrayList<>();

    private boolean Selected = false;
    private OnSelectListener SelectListener;

    private RelativeLayout RelativeLayoutHeader;
    private Bitmap bitmap = null;
    private int Type = 0;

    ImagePreview(Context context, byte[] data)
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
            matrix.postRotate(90);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Exception e)
        {
            MiscHandler.Debug("ImagePreview-Data: " + e.toString());
        }
    }

    ImagePreview(Bitmap b)
    {
        bitmap = b;
    }

    ImagePreview(String URL)
    {
        UrlList.add(URL);
    }

    ImagePreview(String URL, String URL2)
    {
        UrlList.add(URL);
        UrlList.add(URL2);
    }

    ImagePreview(String URL, String URL2, String URL3)
    {
        UrlList.add(URL);
        UrlList.add(URL2);
        UrlList.add(URL3);
    }

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.Black);
        RelativeLayoutMain.setClickable(true);

        if (bitmap != null)
        {
            TouchImageView TouchImageViewMain = new TouchImageView(activity);
            TouchImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            TouchImageViewMain.setImageBitmap(bitmap);
            TouchImageViewMain.setOnClickListener(new View.OnClickListener()
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

            RelativeLayoutMain.addView(TouchImageViewMain);
        }
        else
        {
            ViewPager ViewPagerMain = new ViewPager(activity);
            ViewPagerMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ViewPagerMain.setAdapter(new PreviewAdapter());

            RelativeLayoutMain.addView(ViewPagerMain);
        }

        RelativeLayoutHeader = new RelativeLayout(activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56)));
        RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#3f000000"));

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), MiscHandler.ToDimension(activity, 56));
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(activity);
        ImageViewBack.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_fa : R.drawable.ic_back_white);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(activity, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTitle.setText(activity.getString(R.string.ImagePreview));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutHeader.addView(TextViewTitle);

        if (Type == 1)
        {
            final CropImageView CropImageViewMain = new CropImageView(activity);

            RelativeLayout.LayoutParams ImageViewDoneParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), MiscHandler.ToDimension(activity, 56));
            ImageViewDoneParam.addRule(MiscHandler.Align("L"));

            ImageView ImageViewDone = new ImageView(activity);
            ImageViewDone.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
            ImageViewDone.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewDone.setLayoutParams(ImageViewDoneParam);
            ImageViewDone.setImageResource(R.drawable.ic_done_white);
            ImageViewDone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CropImageViewMain.setVisibility(View.VISIBLE);
                    String ResizeBitmapPath = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "BioGram Crop Image", null);
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

            RelativeLayout.LayoutParams ImageViewDone2Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), MiscHandler.ToDimension(activity, 56));
            ImageViewDone2Param.addRule(MiscHandler.Align("L"));

            ImageView ImageViewDone2 = new ImageView(activity);
            ImageViewDone2.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
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

                        File ProfileFile = new File(CacheHandler.CacheDir(activity), (String.valueOf(System.currentTimeMillis()) + "_imagepreview_crop.jpg"));

                        FileOutputStream FOS = new FileOutputStream(ProfileFile);
                        FOS.write(BAOS.toByteArray());
                        FOS.flush();
                        FOS.close();

                        SignUpDescription SignUpDescription = (SignUpDescription) activity.GetManager().FindByTag("SignUpDescription");
                        SignUpDescription.Update(ProfileFile);

                        GetActivity().onBackPressed();
                        GetActivity().onBackPressed();
                    }
                    catch (Exception e)
                    {
                        MiscHandler.Debug("ImagePreview-Crop: " + e.toString());
                    }
                }
            });

            CropImageViewMain.addView(ImageViewDone2);
        }
        else if (Type == 2)
        {
            final GradientDrawable GradientDrawableSelect = new GradientDrawable();
            GradientDrawableSelect.setShape(GradientDrawable.OVAL);
            GradientDrawableSelect.setStroke(MiscHandler.ToDimension(activity, 2), Color.WHITE);

            final GradientDrawable GradientDrawableSelected = new GradientDrawable();
            GradientDrawableSelected.setShape(GradientDrawable.OVAL);
            GradientDrawableSelected.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
            GradientDrawableSelected.setStroke(MiscHandler.ToDimension(activity, 2), Color.WHITE);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 24), MiscHandler.ToDimension(activity, 24));
            ViewCircleParam.setMargins(MiscHandler.ToDimension(activity, 15), 0, MiscHandler.ToDimension(activity, 15), 0);
            ViewCircleParam.addRule(RelativeLayout.CENTER_VERTICAL);
            ViewCircleParam.addRule(MiscHandler.Align("L"));

            View ViewCircle = new View(activity);
            ViewCircle.setLayoutParams(ViewCircleParam);
            ViewCircle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (Selected)
                    {
                        v.setBackground(GradientDrawableSelect);
                        Selected = false;
                    }
                    else
                    {
                        Selected = true;
                        v.setBackground(GradientDrawableSelected);
                    }

                    SelectListener.OnSelect();
                }
            });

            if (Selected)
                ViewCircle.setBackground(GradientDrawableSelected);
            else
                ViewCircle.setBackground(GradientDrawableSelect);

            RelativeLayoutHeader.addView(ViewCircle);
        }
        else
        {
            RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), MiscHandler.ToDimension(activity, 56));
            ImageViewOptionParam.addRule(MiscHandler.Align("L"));

            ImageView ImageViewOption = new ImageView(activity);
            ImageViewOption.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
            ImageViewOption.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewOption.setLayoutParams(ImageViewOptionParam);
            ImageViewOption.setImageResource(R.drawable.ic_more_white);
            ImageViewOption.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });

            RelativeLayoutHeader.addView(ImageViewOption);
        }

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        if (Build.VERSION.SDK_INT > 20)
            GetActivity().getWindow().setStatusBarColor(ContextCompat.getColor(GetActivity(), R.color.Black));
    }

    @Override
    public void OnPause()
    {
        if (Build.VERSION.SDK_INT > 20)
            GetActivity().getWindow().setStatusBarColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
    }

    void SetType(boolean select, OnSelectListener l)
    {
        Type = 2;
        Selected = select;
        SelectListener = l;
    }

    private class PreviewAdapter extends PagerAdapter
    {
        @Override
        public Object instantiateItem(ViewGroup Container, int Position)
        {
            FragmentActivity activity = GetActivity();

            RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            TouchImageView TouchImageViewMain = new TouchImageView(activity);
            TouchImageViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            TouchImageViewMain.setOnClickListener(new View.OnClickListener()
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

            RelativeLayoutMain.addView(TouchImageViewMain);

            RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56));
            LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            final LoadingView LoadingViewMain = new LoadingView(activity);
            LoadingViewMain.setLayoutParams(LoadingViewMainParam);
            LoadingViewMain.SetColor(R.color.White);
            LoadingViewMain.Start();

            RelativeLayoutMain.addView(LoadingViewMain);

            Glide.with(activity)
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
            .into(TouchImageViewMain);

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

    interface OnSelectListener
    {
        void OnSelect();
    }
}
