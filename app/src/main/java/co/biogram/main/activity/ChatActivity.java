package co.biogram.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.chat.Chat_UI;

public class ChatActivity extends FragmentActivity {

    public static void start(Context context) {

        Intent intentStart = new Intent(context, ChatActivity.class);
        context.startActivity(intentStart);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EmojiManager.install(new IosEmojiProvider());

        setTheme(Misc.GetBoolean("ThemeDark") ? R.style.AppThemeDark : R.style.AppThemeLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        GetManager().OpenView(new Chat_UI(Chat_UI.MODE_SINGLE), "Chat_ListUI", false);

        Log.d("KIRIKHAN", " " + getApplicationContext().getResources().getDimension(R.dimen.padding_icon_chat));
    }

}
