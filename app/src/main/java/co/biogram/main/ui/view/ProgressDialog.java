package co.biogram.main.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.text.NumberFormat;
import java.util.Locale;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class ProgressDialog extends AlertDialog
{
    private ProgressBar ProgressBarMain;
    private TextView TextViewNumber;
    private TextView TextViewPercent;
    private CharSequence Title;
    private int MaxPercentage;
    private final NumberFormat PercentFormat;
    private boolean HasStarted;
    private int ProgressValue;
    private int SecondaryProgressValue;
    private int IncrementBy;
    private int IncrementSecondaryBy;
    private Drawable ProgressDrawable;
    private Drawable IndeterminateDrawable;
    private boolean Indeterminate;
    private Handler ViewUpdateHandler;

    public ProgressDialog(Context context)
    {
        super(context);
        PercentFormat = NumberFormat.getPercentInstance();
        PercentFormat.setMaximumFractionDigits(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Memory Leak
        ViewUpdateHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                int Progress = ProgressBarMain.getProgress();
                int Max = ProgressBarMain.getMax();
                double Percent = (double) Progress / (double) Max;

                SpannableString Span = new SpannableString(PercentFormat.format(Percent));
                Span.setSpan(new StyleSpan(Typeface.NORMAL), 0, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                TextViewPercent.setText(Span);
                TextViewNumber.setText(String.format(Locale.ENGLISH, "%1d / %2d", Progress, Max));
            }
        };

        RelativeLayout RelativeLayoutMain = new RelativeLayout(getContext());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.setMargins(Misc.ToDP(getContext(), 15), Misc.ToDP(getContext(), 10), Misc.ToDP(getContext(), 15), Misc.ToDP(getContext(), 10));
        TextViewTitleParam.addRule(Misc.Align("R"));

        TextView TextViewTitle = new TextView(getContext(), 14, false);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setId(Misc.GenerateViewID());
        TextViewTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.TextWhite));

        RelativeLayoutMain.addView(TextViewTitle);

        RelativeLayout.LayoutParams ProgressBarMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ProgressBarMainParam.setMargins(Misc.ToDP(getContext(), 10), Misc.ToDP(getContext(), 12), Misc.ToDP(getContext(), 10), Misc.ToDP(getContext(), 1));
        ProgressBarMainParam.addRule(RelativeLayout.BELOW, TextViewTitle.getId());
        ProgressBarMainParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        ProgressBarMain = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        ProgressBarMain.setLayoutParams(ProgressBarMainParam);
        ProgressBarMain.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(ProgressBarMain);

        RelativeLayout.LayoutParams TextViewNumberParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNumberParam.setMargins(Misc.ToDP(getContext(), 15), 0, Misc.ToDP(getContext(), 15), 0);
        TextViewNumberParam.addRule(RelativeLayout.BELOW, ProgressBarMain.getId());
        TextViewNumberParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        TextViewNumber = new TextView(getContext(), 14, false);
        TextViewNumber.setLayoutParams(TextViewNumberParam);
        TextViewNumber.setPadding(0, Misc.ToDP(getContext(), 10), 0, Misc.ToDP(getContext(), 10));
        TextViewNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.TextWhite));

        RelativeLayoutMain.addView(TextViewNumber);

        RelativeLayout.LayoutParams TextViewPercentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPercentParam.setMargins(Misc.ToDP(getContext(), 15), 0, Misc.ToDP(getContext(), 15), 0);
        TextViewPercentParam.addRule(RelativeLayout.BELOW, ProgressBarMain.getId());
        TextViewPercentParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        TextViewPercent = new TextView(getContext(), 14, false);
        TextViewPercent.setLayoutParams(TextViewPercentParam);
        TextViewPercent.setPadding(0, Misc.ToDP(getContext(), 10), 0, Misc.ToDP(getContext(), 10));
        TextViewPercent.setTextColor(ContextCompat.getColor(getContext(), R.color.TextWhite));

        RelativeLayoutMain.addView(TextViewPercent);

        setView(RelativeLayoutMain);

        if (MaxPercentage > 0)
            setMax(MaxPercentage);

        if (ProgressValue > 0)
            setProgress(ProgressValue);

        if (SecondaryProgressValue > 0)
            setSecondaryProgress(SecondaryProgressValue);

        if (IncrementBy > 0)
            incrementProgressBy(IncrementBy);

        if (IncrementSecondaryBy > 0)
            incrementSecondaryProgressBy(IncrementSecondaryBy);

        if (ProgressDrawable != null)
            setProgressDrawable(ProgressDrawable);

        if (IndeterminateDrawable != null)
            setIndeterminateDrawable(IndeterminateDrawable);

        if (Title != null)
            TextViewTitle.setText(Title);

        setIndeterminate(Indeterminate);
        onProgressChanged();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        HasStarted = true;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        HasStarted = false;
    }

    public void setProgress(int Value)
    {
        if (HasStarted)
        {
            ProgressBarMain.setProgress(Value);
            onProgressChanged();
            return;
        }

        ProgressValue = Value;
    }

    private void setSecondaryProgress(int Value)
    {
        if (ProgressBarMain != null)
        {
            ProgressBarMain.setSecondaryProgress(Value);
            onProgressChanged();
            return;
        }

        SecondaryProgressValue = Value;
    }

    public void setMax(int max)
    {
        if (ProgressBarMain != null)
        {
            ProgressBarMain.setMax(max);
            onProgressChanged();
            return;
        }

        MaxPercentage = max;
    }

    private void incrementProgressBy(int diff)
    {
        if (ProgressBarMain != null)
        {
            ProgressBarMain.incrementProgressBy(diff);
            onProgressChanged();
            return;
        }

        IncrementBy += diff;
    }

    private void incrementSecondaryProgressBy(int diff)
    {
        if (ProgressBarMain != null)
        {
            ProgressBarMain.incrementSecondaryProgressBy(diff);
            onProgressChanged();
            return;
        }

        IncrementSecondaryBy += diff;
    }

    private void setProgressDrawable(Drawable d)
    {
        if (ProgressBarMain != null)
            ProgressBarMain.setProgressDrawable(d);
        else
            ProgressDrawable = d;
    }

    private void setIndeterminateDrawable(Drawable d)
    {
        if (ProgressBarMain != null)
            ProgressBarMain.setIndeterminateDrawable(d);
        else
            IndeterminateDrawable = d;
    }

    public void setIndeterminate(boolean indeterminate)
    {
        if (ProgressBarMain != null)
            ProgressBarMain.setIndeterminate(indeterminate);
        else
            Indeterminate = indeterminate;
    }

    @Override
    public void setMessage(CharSequence title)
    {
        Title = title;
    }

    private void onProgressChanged()
    {
        if (ViewUpdateHandler != null && !ViewUpdateHandler.hasMessages(0))
            ViewUpdateHandler.sendEmptyMessage(0);
    }
}
