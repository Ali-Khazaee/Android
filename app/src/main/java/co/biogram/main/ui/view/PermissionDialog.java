package co.biogram.main.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class PermissionDialog extends Dialog
{
    public PermissionDialog(Context context)
    {
        super(context);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void SetContentView(int IconID, String Message, final OnSelectedListener Listener)
    {
        Context context = getContext();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(125)));
        RelativeLayoutHeader.setBackgroundResource(R.color.PrimaryColor);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewMainParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewMain = new ImageView(context);
        ImageViewMain.setLayoutParams(ImageViewMainParam);
        ImageViewMain.setImageResource(IconID);

        RelativeLayoutHeader.addView(ImageViewMain);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());
        TextViewMessageParam.addRule(Misc.Align("R"));

        TextView TextViewMessage = new TextView(context, 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.TextWhite));
        TextViewMessage.setId(Misc.ViewID());
        TextViewMessage.setPadding(Misc.ToDP(15), Misc.ToDP(25), Misc.ToDP(15), Misc.ToDP(25));
        TextViewMessage.setText(Message);

        RelativeLayoutMain.addView(TextViewMessage);

        RelativeLayout.LayoutParams LinearLayoutChoiceParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutChoiceParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        LinearLayout LinearLayoutChoice = new LinearLayout(context);
        LinearLayoutChoice.setLayoutParams(LinearLayoutChoiceParam);
        LinearLayoutChoice.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutChoice.setGravity(Gravity.END);

        RelativeLayoutMain.addView(LinearLayoutChoice);

        TextView TextViewDecline = new TextView(context, 14, true);
        TextViewDecline.setLayoutParams(TextViewMessageParam);
        TextViewDecline.setTextColor(ContextCompat.getColor(context, R.color.Gray));
        TextViewDecline.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewDecline.setText(context.getString(R.string.DialogPermissionDecline));
        TextViewDecline.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dismiss(); Listener.OnSelected(false); } });

        LinearLayoutChoice.addView(TextViewDecline);

        TextView TextViewContinue = new TextView(context, 14, true);
        TextViewContinue.setLayoutParams(TextViewMessageParam);
        TextViewContinue.setTextColor(ContextCompat.getColor(context, R.color.PrimaryColor));
        TextViewContinue.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewContinue.setText(context.getString(R.string.DialogPermissionAccept));
        TextViewContinue.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { dismiss(); Listener.OnSelected(true); } });

        LinearLayoutChoice.addView(TextViewContinue);

        setContentView(RelativeLayoutMain);
        show();
    }

    public interface OnSelectedListener
    {
        void OnSelected(boolean Allow);
    }
}
