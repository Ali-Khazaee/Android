package co.biogram.main.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.fragment.FragmentActivity;

public class PermissionDialog extends Dialog {
    Context Activity;


    public PermissionDialog(Context context) {
        super(context);
        this.Activity = context;
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void SetContentView(int Icon, int Message, final String Permission, final OnChoiceListener Listener) {
        if (Misc.CheckPermission(Permission)) {
            Listener.OnChoice(true);
            return;
        }

        View dialogView = View.inflate(getContext(), R.layout.social_dialog_permession, null);

        ImageView ImageViewIcon = dialogView.findViewById(R.id.ImageViewIcon);
        ImageViewIcon.setImageResource(Icon);

        TextView TextViewMessage = dialogView.findViewById(R.id.TextViewMessage);
        TextViewMessage.setText(Misc.String(Message));

        dialogView.findViewById(R.id.TextViewAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                ((FragmentActivity) Activity).RequestPermission(Permission, new FragmentActivity.OnPermissionListener() {
                    @Override
                    public void OnPermission(boolean Result) {
                        Listener.OnChoice(Result);
                    }
                });
            }
        });

        dialogView.findViewById(R.id.TextViewDecline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Listener.OnChoice(false);
            }
        });

        setContentView(dialogView);
        show();
    }


    public interface OnChoiceListener {
        void OnChoice(boolean Result);
    }
}
