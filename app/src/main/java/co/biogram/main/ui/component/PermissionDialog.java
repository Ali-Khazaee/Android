package co.biogram.main.ui.component;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentDialog;
import co.biogram.main.handler.Misc;

public class PermissionDialog extends FragmentDialog
{
    private int Icon;
    private int Message;
    private String Permission;

    public PermissionDialog(int icon, int message, String permission)
    {
        Icon = icon;
        Message = message;
        Permission = permission;
    }

    @Override
    public void OnCreate()
    {
        ViewMain = View.inflate(Activity, R.layout.general_permission_dialog, null);

        GradientDrawable DrawableMain = new GradientDrawable();
        DrawableMain.setColor(Misc.Color(R.color.White));
        DrawableMain.setCornerRadius(Misc.ToDP(10));

        ViewMain.findViewById(R.id.ConstraintLayoutMain).setBackground(DrawableMain);

        ViewMain.findViewById(R.id.ImageViewIcon).setBackgroundResource(Icon);

        ((TextView) ViewMain.findViewById(R.id.TextViewMessage)).setText(Misc.String(Message));

        ViewMain.findViewById(R.id.TextViewDecline).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { PermissionDialog.super.OnDestroy(); } });

        ViewMain.findViewById(R.id.TextViewAccept).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
}
