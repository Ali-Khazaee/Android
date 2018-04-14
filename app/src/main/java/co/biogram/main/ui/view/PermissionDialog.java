package co.biogram.main.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.handler.Misc;

public class PermissionDialog extends Dialog
{
    public PermissionDialog(Context context)
    {
        super(context);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void SetContentView(int Icon, int Message, final String Permession, final FragmentActivity Activity, final OnChoiceListener Listener)
    {
        if (Misc.checkPermission(Permession))
        {
            dismiss();
            Listener.OnChoice(true);
            return;
        }

        View dialogView = View.inflate(getContext(), R.layout.general_dialog_permession, null);

        ImageView ImageViewIcon = dialogView.findViewById(R.id.ImageViewIcon);
        ImageViewIcon.setImageResource(Icon);

        TextView TextViewMessage = dialogView.findViewById(R.id.TextViewMessage);
        TextViewMessage.setText(getContext().getString(Message));

        TextView TextViewAccept = dialogView.findViewById(R.id.TextViewAccept);
        TextViewAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();

                Activity.RequestPermission(Permession, new FragmentActivity.OnGrantListener()
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
