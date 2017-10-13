package co.biogram.main.handler;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;

import static co.biogram.main.App.GetOKClient;

@GlideModule
public class GlideHandler extends AppGlideModule
{
    @Override
    public void applyOptions(Context context, GlideBuilder builder)
    {
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).disallowHardwareConfig());
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry)
    {
        glide.getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(GetOKClient()));
    }

    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }
}
