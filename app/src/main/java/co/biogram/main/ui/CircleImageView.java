package co.biogram.main.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import co.biogram.main.handler.MiscHandler;

public class CircleImageView extends ImageView
{
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private final RectF BorderRect = new RectF();
    private final RectF DrawableRect = new RectF();

    private int BorderWidth = 0;
    private int BorderColor = Color.WHITE;
    private int BackgroundColor = Color.TRANSPARENT;

    private boolean IsReady;
    private boolean IsSetupPending;

    private float BorderRadius;
    private float DrawableRadius;

    private final Paint BitmapPaint = new Paint();
    private final Paint BorderPaint = new Paint();
    private final Matrix ShaderMatrix = new Matrix();
    private final Paint CircleBackgroundPaint = new Paint();

    private Bitmap bitmap;

    private ColorFilter colorFilter;

    public CircleImageView(Context context)
    {
        super(context);
        super.setScaleType(SCALE_TYPE);

        IsReady = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setOutlineProvider(new OutlineProvider());

        if (IsSetupPending)
        {
            Setup();
            IsSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType()
    {
        return SCALE_TYPE;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (bitmap == null)
            return;

        if (BackgroundColor != Color.TRANSPARENT)
            canvas.drawCircle(DrawableRect.centerX(), DrawableRect.centerY(), DrawableRadius, CircleBackgroundPaint);

        canvas.drawCircle(DrawableRect.centerX(), DrawableRect.centerY(), DrawableRadius, BitmapPaint);

        if (BorderWidth > 0)
            canvas.drawCircle(BorderRect.centerX(), BorderRect.centerY(), BorderRadius, BorderPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        Setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
        super.setPadding(left, top, right, bottom);
        Setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom)
    {
        super.setPaddingRelative(start, top, end, bottom);
        Setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
        InitializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
        InitializeBitmap();
    }

    @Override
    public void setImageResource(int resId)
    {
        super.setImageResource(resId);
        InitializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri)
    {
        super.setImageURI(uri);
        InitializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        if (cf == colorFilter)
            return;

        colorFilter = cf;
        ApplyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter()
    {
        return colorFilter;
    }

    private void ApplyColorFilter()
    {
        if (BitmapPaint != null)
            BitmapPaint.setColorFilter(colorFilter);
    }

    public void setBorderColor(int ID)
    {
        BorderColor = ContextCompat.getColor(getContext(), ID);
        BorderPaint.setColor(BorderColor);
        invalidate();
    }

    public void setCircleBackgroundColor(int ID)
    {
        BackgroundColor = ContextCompat.getColor(getContext(), ID);
        CircleBackgroundPaint.setColor(BackgroundColor);
        invalidate();
    }

    public void setBorderWidth(int Width)
    {
        BorderWidth = MiscHandler.ToDimension(getContext(), Width);
        Setup();
    }

    private void InitializeBitmap()
    {
        bitmap = null;
        Drawable drawable = getDrawable();

        if (drawable instanceof BitmapDrawable)
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        else if (drawable != null)
        {
            try
            {
                Bitmap bitmap2;

                if (drawable instanceof ColorDrawable)
                    bitmap2 = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
                else
                    bitmap2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);

                Canvas canvas = new Canvas(bitmap2);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                bitmap = bitmap2;
            }
            catch (Exception e)
            {
                MiscHandler.Debug("CircleImageView-getBitmapFromDrawable: " + e.toString());
            }
        }

        Setup();
    }

    private void Setup()
    {
        if (!IsReady)
        {
            IsSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0)
            return;

        if (bitmap == null)
        {
            invalidate();
            return;
        }

        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        BitmapPaint.setAntiAlias(true);
        BitmapPaint.setShader(bitmapShader);

        BorderPaint.setStyle(Paint.Style.STROKE);
        BorderPaint.setAntiAlias(true);
        BorderPaint.setColor(BorderColor);
        BorderPaint.setStrokeWidth(BorderWidth);

        CircleBackgroundPaint.setStyle(Paint.Style.FILL);
        CircleBackgroundPaint.setAntiAlias(true);
        CircleBackgroundPaint.setColor(BackgroundColor);

        int BitmapHeight = bitmap.getHeight();
        int BitmapWidth = bitmap.getWidth();

        int availableWidth  = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        RectF a =  new RectF(left, top, left + sideLength, top + sideLength);

        BorderRect.set(a);
        BorderRadius = Math.min((BorderRect.height() - BorderWidth) / 2.0f, (BorderRect.width() - BorderWidth) / 2.0f);

        DrawableRect.set(BorderRect);

        if (BorderWidth > 0)
            DrawableRect.inset(BorderWidth - 1.0f, BorderWidth - 1.0f);

        DrawableRadius = Math.min(DrawableRect.height() / 2.0f, DrawableRect.width() / 2.0f);

        ApplyColorFilter();

        float Scale;
        float X = 0;
        float Y = 0;

        ShaderMatrix.set(null);

        if (BitmapWidth * DrawableRect.height() > DrawableRect.width() * BitmapHeight)
        {
            Scale = DrawableRect.height() / (float) BitmapHeight;
            X = (DrawableRect.width() - BitmapWidth * Scale) * 0.5f;
        }
        else
        {
            Scale = DrawableRect.width() / (float) BitmapWidth;
            Y = (DrawableRect.height() - BitmapHeight * Scale) * 0.5f;
        }

        ShaderMatrix.setScale(Scale, Scale);
        ShaderMatrix.postTranslate((int) (X + 0.5f) + DrawableRect.left, (int) (Y + 0.5f) + DrawableRect.top);

        bitmapShader.setLocalMatrix(ShaderMatrix);

        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class OutlineProvider extends ViewOutlineProvider
    {
        @Override
        public void getOutline(View view, Outline outline)
        {
            Rect bounds = new Rect();
            BorderRect.roundOut(bounds);
            outline.setRoundRect(bounds, bounds.width() / 2.0f);
        }
    }
}
