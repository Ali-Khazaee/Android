package co.biogram.main.handler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public class GlideHandler extends AppGlideModule
{
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder)
    {
        super.applyOptions(context, builder);
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.ALL));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, Misc.TAG + "Glide", 1024 * 1024 * 300));
    }

    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }
}
