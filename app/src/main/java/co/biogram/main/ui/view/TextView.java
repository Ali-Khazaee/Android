package co.biogram.main.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class TextView extends android.widget.TextView
{
    private int Width;
    private Paint PaintFill;

    private TextView(Context context)
    {
        super(context);
    }

    public TextView(Context context, float size, boolean isBold)
    {
        this(context);

        if (isBold)
            setTypeface(Misc.GetTypeface(), Typeface.BOLD);
        else
            setTypeface(Misc.GetTypeface());

        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        setTextColor(ContextCompat.getColor(context, R.color.TextDark));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (Width > 0)
        {
            // noinspection all
            RectF RectRound = new RectF(0, 0, (getWidth() * Width / 100), getHeight());
            canvas.drawRoundRect(RectRound, Misc.ToDP(5), Misc.ToDP(5), PaintFill);
        }

        super.onDraw(canvas);
    }

    public void SetColor(int C)
    {
        setTextColor(Misc.Color(C));
    }

    public void FillBackground(int width)
    {
        Width = width;
        String FillColor = "#1da1f2";

        if (width >= 100)
            FillColor = "#b01da1f2";
        else if (width >= 90)
            FillColor = "#a01da1f2";
        else if (width >= 80)
            FillColor = "#901da1f2";
        else if (width >= 70)
            FillColor = "#901da1f2";
        else if (width >= 60)
            FillColor = "#701da1f2";
        else if (width >= 50)
            FillColor = "#601da1f2";
        else if (width >= 40)
            FillColor = "#501da1f2";
        else if (width >= 30)
            FillColor = "#401da1f2";
        else if (width >= 20)
            FillColor = "#301da1f2";
        else if (width >= 10)
            FillColor = "#201da1f2";

        PaintFill = new Paint();
        PaintFill.setAntiAlias(true);
        PaintFill.setColor(Color.parseColor(FillColor));

        invalidate();
    }
}
