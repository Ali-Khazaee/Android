package co.biogram.main.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;

import co.biogram.main.handler.Misc;

public class LineView extends View {
    private int Width;

    private Paint ProgressPaint;
    private int ProgressPercent;
    private int ProgressColor;

    private Paint StrokePaint;
    private int StrokeColor;
    private float StrokeWidth;

    public LineView(Context context) {
        super(context);

        StrokePaint = new Paint();
        StrokePaint.setAntiAlias(true);
        StrokePaint.setStyle(Paint.Style.STROKE);

        ProgressPaint = new Paint();
        ProgressPaint.setAntiAlias(true);
        ProgressPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure) {
        super.onMeasure(widthMeasure, heightMeasure);
        Width = resolveSize(96, widthMeasure);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, Width, 0, StrokePaint);
        canvas.drawLine(0, 0, (Width * ProgressPercent / 100), 0, ProgressPaint);
    }

    public void SetStrokeColor(int color) {
        StrokeColor = ContextCompat.getColor(getContext(), color);
    }

    public void SetStrokeWidth(int width) {
        StrokeWidth = Misc.ToDP(width);
    }

    public void SetProgressColor(int color) {
        ProgressColor = ContextCompat.getColor(getContext(), color);
    }

    public void SetProgressPercent(int percent) {
        ProgressPercent = percent;
        invalidate();
    }

    public void InvalidateTextPaints() {
        StrokePaint.setColor(StrokeColor);
        StrokePaint.setStrokeWidth(StrokeWidth);
        ProgressPaint.setStrokeWidth(StrokeWidth);
        ProgressPaint.setColor(ProgressColor);
        invalidate();
    }
}
