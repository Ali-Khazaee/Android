package co.biogram.main.ui.general;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.fragment.FragmentDialog;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.TextView;

public class Permission_UI extends FragmentDialog
{
    private int Icon;
    private int Message;
    private String Permission;
    private OnPermissionListener Listener;

    public Permission_UI(int icon, int message, String permission, OnPermissionListener listener)
    {
        Icon = icon;
        Message = message;
        Listener = listener;
        Permission = permission;
    }

    @Override
    public void OnCreate()
    {
        if (Misc.CheckPermission(Permission))
        {
            OnDestroy();
            Listener.OnPermission(true);
            return;
        }

        ViewMain = View.inflate(Activity, R.layout.general_permission, null);

        GradientDrawable DrawableMain = new GradientDrawable();
        DrawableMain.setColor(Misc.Color(R.color.White));
        DrawableMain.setCornerRadius(Misc.ToDP(10));

        ViewMain.findViewById(R.id.ConstraintLayoutMain).setBackground(DrawableMain);

        ViewMain.findViewById(R.id.ImageViewIcon).setBackgroundResource(Icon);

        ((TextView) ViewMain.findViewById(R.id.TextViewMessage)).setText(Misc.String(Message));

        ViewMain.findViewById(R.id.TextViewDecline).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

        ViewMain.findViewById(R.id.TextViewAccept).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.RequestPermission(Permission, new FragmentActivity.OnPermissionListener() { @Override public void OnPermission(boolean Result) { Listener.OnPermission(Result); }});
                Activity.onBackPressed();
            }
        });
    }

    public interface OnPermissionListener
    {
        void OnPermission(boolean Result);
    }
}
