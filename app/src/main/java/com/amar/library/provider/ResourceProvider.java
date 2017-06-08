package com.amar.library.provider;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;

import com.amar.library.provider.interfaces.IResourceProvider;

public class ResourceProvider implements IResourceProvider {

    private final TypedArray mTypeArray;

    public ResourceProvider(Context context, AttributeSet attrs, @StyleableRes int[] styleRes) {
        mTypeArray = context.obtainStyledAttributes(attrs, styleRes);
    }

    @Override
    public int getResourceId(@StyleableRes int styleResId) {
        return mTypeArray.getResourceId(styleResId, 0);
    }

    @Override
    public void recycle() {
        mTypeArray.recycle();
    }
}
