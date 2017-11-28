package co.biogram.main.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;

import co.biogram.main.handler.Misc;

public class CircleView extends View
{
    private RectF InnerRectF;
    private Bitmap bitmap;
    private int ViewSize;

    private Paint ProgressPaint;
    private float ProgressSweep;
    private float ProgressWidth;
    private int ProgressColor;

    private Paint StrokePaint;
    private int StrokeColor;
    private float StrokeWidth;

    private String Message;
    private int MessageColor;
    private float MessageSize;
    private TextPaint MessagePaint;
    private boolean MessageBold;

    private String SubMessage;
    private int SubMessageColor;
    private float SubMessageSize;
    private float SubMessageSpace;
    private TextPaint SubMessagePaint;

    public CircleView(Context context)
    {
        super(context);

        InnerRectF = new RectF();

        MessagePaint = new TextPaint();
        MessagePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        MessagePaint.setTextAlign(Paint.Align.CENTER);
        MessagePaint.setLinearText(true);

        SubMessagePaint = new TextPaint();
        SubMessagePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        SubMessagePaint.setTextAlign(Paint.Align.CENTER);
        SubMessagePaint.setLinearText(true);

        StrokePaint = new Paint();
        StrokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        StrokePaint.setStyle(Paint.Style.STROKE);

        ProgressPaint = new Paint();
        ProgressPaint.setAntiAlias(true);
        ProgressPaint.setStyle(Paint.Style.STROKE);
        ProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure)
    {
        super.onMeasure(widthMeasure, heightMeasure);

        int Width = resolveSize(96, widthMeasure);
        int Height = resolveSize(96, heightMeasure);

        ViewSize = Math.min(Width, Height);

        setMeasuredDimension(Width, Height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        InnerRectF.set(0, 0, ViewSize, ViewSize);
        InnerRectF.offset((getWidth() - ViewSize) / 2, (getHeight() - ViewSize) / 2);

        int HalfBorder = (int) ((StrokePaint.getStrokeWidth() / 2f) + 5.0f);

        InnerRectF.inset(HalfBorder, HalfBorder);

        int XPos = (int) InnerRectF.centerX();
        int YPos = (int) (InnerRectF.centerY() - (MessagePaint.descent() + MessagePaint.ascent()) / 2);

        canvas.drawOval(InnerRectF, StrokePaint);

        if (Message != null && !Message.equals(""))
            canvas.drawText(Message, XPos, YPos - (SubMessageSpace / 2), MessagePaint);

        canvas.drawArc(InnerRectF, -90, ProgressSweep, false, ProgressPaint);

        if (SubMessage != null && !SubMessage.equals(""))
            canvas.drawText(SubMessage, XPos, YPos + SubMessageSpace, SubMessagePaint);

        if (bitmap != null)
            canvas.drawBitmap(bitmap, XPos - Misc.ToDP(getContext(), 10), YPos, null);
    }

    public void SetBitmap(int resource, int size)
    {
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), resource);
        bitmap = Bitmap.createScaledBitmap(bitmap, Misc.ToDP(getContext(), size), Misc.ToDP(getContext(), size), true);
    }

    public void SetMessage(String message)
    {
        Message = message;
    }

    public void SetMessageBold()
    {
        MessageBold = true;
    }

    public void SetMessageSize(int size)
    {
        MessageSize = Misc.ToDP(getContext(), size);
    }

    public void SetMessageColor(int color)
    {
        MessageColor = ContextCompat.getColor(getContext(), color);
    }

    public void SetSubMessage(String message)
    {
        SubMessage = message;
    }

    public void SetSubMessageSize(int size)
    {
        SubMessageSize = Misc.ToDP(getContext(), size);
    }

    public void SetSubMessageColor(int color)
    {
        SubMessageColor = ContextCompat.getColor(getContext(), color);
    }

    public void SetSubMessageSpace(int size)
    {
        SubMessageSpace = Misc.ToDP(getContext(), size);
    }

    public void SetStrokeColor(int color)
    {
        StrokeColor = ContextCompat.getColor(getContext(), color);
    }

    public void SetStrokeWidth(int width)
    {
        StrokeWidth = Misc.ToDP(getContext(), width);
    }

    public void SetProgressColor(int color)
    {
        ProgressColor = ContextCompat.getColor(getContext(), color);
    }

    public void SetProgressWidth(int width)
    {
        ProgressWidth = Misc.ToDP(getContext(), width);
    }

    public void SetProgressPercentage(int percent)
    {
        ProgressSweep = 360 * percent / 100;
        invalidate();
    }

    public void InvalidateTextPaints()
    {
        if (MessageBold)
            MessagePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        else
            MessagePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        MessagePaint.setColor(MessageColor);
        MessagePaint.setTextSize(MessageSize);
        SubMessagePaint.setColor(SubMessageColor);
        SubMessagePaint.setTextSize(SubMessageSize);
        StrokePaint.setColor(StrokeColor);
        StrokePaint.setStrokeWidth(StrokeWidth);
        ProgressPaint.setStrokeWidth(ProgressWidth);
        ProgressPaint.setColor(ProgressColor);
        invalidate();
    }
}
