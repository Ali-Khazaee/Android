package co.biogram.main.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.chat.Chat_List_UI;
import co.biogram.main.fragment.FragmentActivity;

public class MessengerActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        setTheme(Misc.GetTheme());

        if (getActionBar() != null)
            getActionBar().hide();

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(Misc.Attr(this, R.attr.StatusColor));

        FrameLayout FrameLayoutMain = new FrameLayout(this);
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutMain.setId(R.id.ContainerFull);

        setContentView(FrameLayoutMain);

        GetManager().OpenView(new Chat_List_UI(), "Chat_List_UI", true);
    }
}
