package co.biogram.main.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

class DialogPermission extends Dialog
{
    DialogPermission(Context context)
    {
        super(context);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    void SetContentView(int Icon, String Message)
    {
        Context context = getContext();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 125)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewMainParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 32), MiscHandler.ToDimension(context, 32));
        ImageViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewMain = new ImageView(context);
        ImageViewMain.setLayoutParams(ImageViewMainParam);
        ImageViewMain.setImageResource(Icon);

        RelativeLayoutHeader.addView(ImageViewMain);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 25));
        TextViewMessage.setText(Message);

        RelativeLayoutMain.addView(TextViewMessage);

        RelativeLayout.LayoutParams LinearLayoutChoiceParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutChoiceParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        LinearLayout LinearLayoutChoice = new LinearLayout(context);
        LinearLayoutChoice.setLayoutParams(LinearLayoutChoiceParam);
        LinearLayoutChoice.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutChoice.setGravity(MiscHandler.Gravity("L"));

        RelativeLayoutMain.addView(LinearLayoutChoice);

        TextView TextViewContinue = new TextView(context);
        TextViewContinue.setLayoutParams(TextViewMessageParam);
        TextViewContinue.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewContinue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewContinue.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        TextViewContinue.setText("اجازه دادن");

        TextView TextViewDecline = new TextView(context);
        TextViewDecline.setLayoutParams(TextViewMessageParam);
        TextViewDecline.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewDecline.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDecline.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        TextViewDecline.setText("نه فعلا");

        if (MiscHandler.IsRTL())
        {
            TextViewMessage.setTypeface(Typeface.createFromAsset(context.getAssets(), "iran-sans.ttf"));
            TextViewContinue.setTypeface(Typeface.createFromAsset(context.getAssets(), "iran-sans.ttf"));
            TextViewDecline.setTypeface(Typeface.createFromAsset(context.getAssets(), "iran-sans.ttf"));

            LinearLayoutChoice.addView(TextViewDecline);
            LinearLayoutChoice.addView(TextViewContinue);
        }
        else
        {
            LinearLayoutChoice.addView(TextViewContinue);
            LinearLayoutChoice.addView(TextViewDecline);
        }

        setContentView(RelativeLayoutMain);
        show();
    }
}
