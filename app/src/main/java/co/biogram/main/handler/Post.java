package co.biogram.main.handler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.ui.view.TextView;

public class Post
{
    public static void UI(PostAdapter.PostStruct P)
    {
        try
        {

        }
        catch (Exception e)
        {
            Misc.Debug("PostAdapter-ViewType1: " + e.toString());
        }
    }
}
