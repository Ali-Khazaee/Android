package co.biogram.main.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.fragment.FragmentActivity;

public class PermissionDialog extends Dialog
{
    public PermissionDialog(Context context)
    {
        super(context);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void SetContentView(int Icon, int Message, final String Permission, final OnChoiceListener Listener)
    {
        if (Misc.checkPermission(Permission))
        {
            Listener.OnChoice(true);
            return;
        }

        View dialogView = View.inflate(getContext(), R.layout.social_dialog_permession, null);

        ImageView ImageViewIcon = dialogView.findViewById(R.id.ImageViewIcon);
        ImageViewIcon.setImageResource(Icon);

        TextView TextViewMessage = dialogView.findViewById(R.id.TextViewMessage);
        TextViewMessage.setText(Misc.String(Message));

        TextView TextViewAccept = dialogView.findViewById(R.id.TextViewAccept);
        TextViewAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();

                ((FragmentActivity) getContext()).RequestPermission(Permission, new FragmentActivity.OnGrantListener()
                {
                    @Override
                    public void OnGrant(boolean Result)
                    {
                        Listener.OnChoice(Result);
                    }
                });
            }
        });

        TextView TextViewDecline = dialogView.findViewById(R.id.TextViewDecline);
        TextViewDecline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
                Listener.OnChoice(false);
            }
        });

        setContentView(dialogView);
        show();
    }

    public interface OnChoiceListener
    {
        void OnChoice(boolean Result);
    }
}
