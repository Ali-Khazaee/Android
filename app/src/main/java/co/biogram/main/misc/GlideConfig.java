package co.biogram.main.misc;

import android.content.Context;
import android.support.annotation.Keep;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import co.biogram.main.App;
import okhttp3.OkHttpClient;

@Keep
public class GlideConfig implements GlideModule
{
    @Override
    public void applyOptions(Context context, GlideBuilder builder)
    {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide)
    {
        OkHttpClient OKClient = App.GetOKClient();

        Glide.get(context).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OKClient));
    }
}
