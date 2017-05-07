package co.biogram.main.misc;

import android.content.Context;
import android.content.res.TypedArray;
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
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import co.biogram.main.App;
import co.biogram.main.R;

public class ImageViewCircle extends ImageView
{
    private int BorderWidth = 0;
    private int BorderColor = Color.BLACK;

    private float DrawableRadius;
    private float BorderRadius;

    private boolean Ready;
    private boolean SetupPending;

    private final Matrix ShaderMatrix = new Matrix();

    private final RectF DrawableRect = new RectF();
    private final RectF BorderRect = new RectF();

    private final Paint BitmapPaint = new Paint();
    private final Paint BorderPaint = new Paint();
    private final Paint FillPaint = new Paint();

    private ColorFilter ColorFilter;

    private Bitmap BaseBitmap;

    public ImageViewCircle(Context context)
    {
        super(context);
        Initializing();
    }

    public ImageViewCircle(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ImageViewCircle(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        if (isInEditMode())
            return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewCircle, defStyle, 0);
        BorderWidth = a.getDimensionPixelSize(R.styleable.ImageViewCircle_IVC_BorderWidth, BorderWidth);
        BorderColor = a.getColor(R.styleable.ImageViewCircle_IVC_BorderColor, BorderColor);
        a.recycle();

        Initializing();
    }

    public void SetBorderWidth(int Width)
    {
        BorderWidth = Width;
    }

    public void SetBorderColor(int Color)
    {
        BorderColor = ContextCompat.getColor(getContext(), Color);
    }

    private void Initializing()
    {
        super.setScaleType(ScaleType.CENTER_CROP);
        Ready = true;

        if (SetupPending)
        {
            SetUp();
            SetupPending = false;
        }
    }

    private void SetUp()
    {
        if (!Ready)
        {
            SetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0)
            return;

        if (BaseBitmap == null)
        {
            invalidate();
            return;
        }

        BitmapShader BaseBitmapShader = new BitmapShader(BaseBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        BitmapPaint.setAntiAlias(true);
        BitmapPaint.setShader(BaseBitmapShader);

        BorderPaint.setStyle(Paint.Style.STROKE);
        BorderPaint.setAntiAlias(true);
        BorderPaint.setColor(BorderColor);
        BorderPaint.setStrokeWidth(BorderWidth);

        FillPaint.setStyle(Paint.Style.FILL);
        FillPaint.setAntiAlias(true);
        FillPaint.setColor(Color.TRANSPARENT);

        int BitmapHeight = BaseBitmap.getHeight();
        int BitmapWidth = BaseBitmap.getWidth();

        int AvailableWidth  = getWidth() - getPaddingLeft() - getPaddingRight();
        int AvailableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int SideLength = Math.min(AvailableWidth, AvailableHeight);

        float Left = getPaddingLeft() + (AvailableWidth - SideLength) / 2f;
        float Top = getPaddingTop() + (AvailableHeight - SideLength) / 2f;

        BorderRect.set(new RectF(Left, Top, Left + SideLength, Top + SideLength));
        BorderRadius = Math.min((BorderRect.height() - BorderWidth) / 2.0f, (BorderRect.width() - BorderWidth) / 2.0f);

        DrawableRect.set(BorderRect);

        if (BorderWidth > 0)
            DrawableRect.inset(BorderWidth - 1.0f, BorderWidth - 1.0f);

        DrawableRadius = Math.min(DrawableRect.height() / 2.0f, DrawableRect.width() / 2.0f);

        ApplyColorFilter();

        float Scale;
        float DX = 0;
        float DY = 0;

        ShaderMatrix.set(null);

        if (BitmapWidth * DrawableRect.height() > DrawableRect.width() * BitmapHeight)
        {
            Scale = DrawableRect.height() / (float) BitmapHeight;
            DX = (DrawableRect.width() - BitmapWidth * Scale) * 0.5f;
        }
        else
        {
            Scale = DrawableRect.width() / (float) BitmapWidth;
            DY = (DrawableRect.height() - BitmapHeight * Scale) * 0.5f;
        }

        ShaderMatrix.setScale(Scale, Scale);
        ShaderMatrix.postTranslate((int) (DX + 0.5f) + DrawableRect.left, (int) (DY + 0.5f) + DrawableRect.top);

        BaseBitmapShader.setLocalMatrix(ShaderMatrix);
        invalidate();
    }

    private void ApplyColorFilter()
    {
        if (BitmapPaint != null)
            BitmapPaint.setColorFilter(ColorFilter);
    }

    private void InitializeBitmap()
    {
        Drawable drawable = getDrawable();

        if (drawable == null)
        {
            BaseBitmap = null;
            SetUp();
            return;
        }

        if (drawable instanceof BitmapDrawable)
        {
            BaseBitmap = ((BitmapDrawable) drawable).getBitmap();
            SetUp();
            return;
        }

        try
        {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable)
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            else
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            BaseBitmap = bitmap;
        }
        catch (Exception e)
        {
            BaseBitmap = null;
        }

        SetUp();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (BaseBitmap == null)
            return;

        canvas.drawCircle(DrawableRect.centerX(), DrawableRect.centerY(), DrawableRadius, BitmapPaint);

        if (BorderWidth > 0)
            canvas.drawCircle(BorderRect.centerX(), BorderRect.centerY(), BorderRadius, BorderPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int OldWidth, int OldHeight)
    {
        super.onSizeChanged(w, h, OldWidth, OldHeight);
        SetUp();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom)
    {
        super.setPadding(left, top, right, bottom);
        SetUp();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom)
    {
        super.setPaddingRelative(start, top, end, bottom);
        SetUp();
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
        if (cf == ColorFilter)
            return;

        ColorFilter = cf;
        ApplyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter()
    {
        return ColorFilter;
    }
}
