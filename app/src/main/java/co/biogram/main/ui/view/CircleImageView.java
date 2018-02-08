package co.biogram.main.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import co.biogram.main.handler.Misc;

public class CircleImageView extends ImageView
{
    private final RectF BorderRect = new RectF();
    private final RectF DrawableRect = new RectF();

    private int BorderWidth = 0;
    private int BorderColor = Color.WHITE;
    private int BackgroundColor = Color.TRANSPARENT;

    private final boolean IsReady;
    private boolean WithPladding = false;
    private boolean IsSetupPending;

    private float BorderRadius;
    private float DrawableRadius;

    private final Paint BitmapPaint = new Paint();
    private final Paint BorderPaint = new Paint();
    private final Paint BackgroundPaint = new Paint();

    private Bitmap bitmap;
    private ColorFilter colorFilter;

    public CircleImageView(Context context)
    {
        super(context);
        super.setScaleType(ScaleType.CENTER_CROP);

        IsReady = true;

        if (IsSetupPending)
        {
            Setup();
            IsSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType()
    {
        return ScaleType.CENTER_CROP;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (bitmap == null)
            return;

        if (BackgroundColor != Color.TRANSPARENT)
            canvas.drawCircle(DrawableRect.centerX(), DrawableRect.centerY(), DrawableRadius, BackgroundPaint);

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

    public void SetBorderColor(int ID)
    {
        BorderColor = Misc.Color(ID);
        BorderPaint.setColor(BorderColor);
        invalidate();
    }

    public void SetCircleBackgroundColor(int ID)
    {
        BackgroundColor = Misc.Color(ID);
        BackgroundPaint.setColor(BackgroundColor);
        invalidate();
    }

    public void SetWidthPadding()
    {
        WithPladding = true;
    }

    public void SetBorderWidth(int Width)
    {
        BorderWidth = Misc.ToDP(Width);
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
            Bitmap bitmap2;

            if (drawable instanceof ColorDrawable)
                bitmap2 = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            else
                bitmap2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap2);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            bitmap = bitmap2;
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

        BackgroundPaint.setStyle(Paint.Style.FILL);
        BackgroundPaint.setAntiAlias(true);
        BackgroundPaint.setColor(BackgroundColor);

        int BitmapHeight = bitmap.getHeight();
        int BitmapWidth = bitmap.getWidth();

        int AvailableWidth  = getWidth() - getPaddingLeft() - getPaddingRight();
        int AvailableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int SideLength = Math.min(AvailableWidth, AvailableHeight);

        float Left = getPaddingLeft() + (AvailableWidth - SideLength) / 2f;
        float Top = getPaddingTop() + (AvailableHeight - SideLength) / 2f;

        BorderRect.set(new RectF(Left, Top, Left + SideLength, Top + SideLength));
        BorderRadius = Math.min((BorderRect.height() - BorderWidth) / 2.0f, (BorderRect.width() - BorderWidth) / 2.0f);

        if (WithPladding)
            BorderRadius += (getPaddingTop() + getPaddingLeft() + getPaddingRight() + getPaddingBottom()) / 4;

        DrawableRect.set(BorderRect);

        if (BorderWidth > 0)
            DrawableRect.inset(BorderWidth - 1.0f, BorderWidth - 1.0f);

        DrawableRadius = Math.min(DrawableRect.height() / 2.0f, DrawableRect.width() / 2.0f);

        ApplyColorFilter();

        float Scale;
        float X = 0;
        float Y = 0;

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

        Matrix ShaderMatrix = new Matrix();
        ShaderMatrix.setScale(Scale, Scale);
        ShaderMatrix.postTranslate((int) (X + 0.5f) + DrawableRect.left, (int) (Y + 0.5f) + DrawableRect.top);

        bitmapShader.setLocalMatrix(ShaderMatrix);

        invalidate();
    }
}
