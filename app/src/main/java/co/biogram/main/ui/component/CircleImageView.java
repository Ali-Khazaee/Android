package co.biogram.main.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView
{
    private boolean IsReady;
    private boolean IsSetUp;
    private int BorderColor;
    private int BorderWidth;
    private Bitmap BitmapMain;
    private float BorderRadius;
    private int BackgroundColor;
    private float DrawableRadius;
    private final RectF BorderRect = new RectF();
    private final Paint BitmapPaint = new Paint();
    private final Paint BorderPaint = new Paint();
    private final RectF DrawableRect = new RectF();
    private final Matrix ShaderMatrix = new Matrix();
    private final Paint BackgroundPaint = new Paint();

    public CircleImageView(Context context)
    {
        this(context, null, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        super.setScaleType(ScaleType.CENTER_CROP);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        BorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, Misc.ToDP(1));
        BorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, Misc.Color(R.color.Gray));
        BackgroundColor = a.getColor(R.styleable.CircleImageView_civ_background_color, Color.TRANSPARENT);
        a.recycle();

        IsReady = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setOutlineProvider(new OutlineProvider());

        if (IsSetUp)
        {
            SetUp();
            IsSetUp = false;
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event)
    {
        return Math.pow(event.getX() - BorderRect.centerX(), 2) + Math.pow(event.getY() - BorderRect.centerY(), 2) <= Math.pow(BorderRadius, 2) && super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
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
        SetUpBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
        SetUpBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId)
    {
        super.setImageResource(resId);
        SetUpBitmap();
    }

    @Override
    public void setImageURI(Uri uri)
    {
        super.setImageURI(uri);
        SetUpBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (BitmapMain == null)
            return;

        if (BackgroundColor != Color.TRANSPARENT)
            canvas.drawCircle(DrawableRect.centerX(), DrawableRect.centerY(), DrawableRadius, BackgroundPaint);

        canvas.drawCircle(DrawableRect.centerX(), DrawableRect.centerY(), DrawableRadius, BitmapPaint);

        if (BorderWidth > 0)
            canvas.drawCircle(BorderRect.centerX(), BorderRect.centerY(), BorderRadius, BorderPaint);
    }

    private void SetUpBitmap()
    {
        Drawable drawable = getDrawable();

        if (drawable == null)
            BitmapMain = null;
        else if (drawable instanceof BitmapDrawable)
            BitmapMain = ((BitmapDrawable) drawable).getBitmap();
        else
        {
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

                BitmapMain = bitmap;
            }
            catch (Exception e)
            {
                BitmapMain = null;
            }
        }

        SetUp();
    }

    private void SetUp()
    {
        if (!IsReady)
        {
            IsSetUp = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0)
            return;

        if (BitmapMain == null)
        {
            invalidate();
            return;
        }

        BitmapShader BitmapShaderMain = new BitmapShader(BitmapMain, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        BitmapPaint.setAntiAlias(true);
        BitmapPaint.setShader(BitmapShaderMain);

        BorderPaint.setStyle(Paint.Style.STROKE);
        BorderPaint.setAntiAlias(true);
        BorderPaint.setColor(BorderColor);
        BorderPaint.setStrokeWidth(BorderWidth);

        BackgroundPaint.setStyle(Paint.Style.FILL);
        BackgroundPaint.setAntiAlias(true);
        BackgroundPaint.setColor(BackgroundColor);

        int BitmapHeight = BitmapMain.getHeight();
        int BitmapWidth = BitmapMain.getWidth();

        int AvailableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
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

        BitmapShaderMain.setLocalMatrix(ShaderMatrix);

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
