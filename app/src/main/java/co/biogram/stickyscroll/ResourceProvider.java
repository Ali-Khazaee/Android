package co.biogram.stickyscroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

class ResourceProvider implements IResourceProvider
{
    private final TypedArray mTypeArray;

    ResourceProvider(Context context, AttributeSet attrs, int[] styleRes)
    {
        mTypeArray = context.obtainStyledAttributes(attrs, styleRes);
    }

    @Override
    public int getResourceId(int styleResId)
    {
        return mTypeArray.getResourceId(styleResId, 0);
    }

    @Override
    public void recycle()
    {
        mTypeArray.recycle();
    }
}
