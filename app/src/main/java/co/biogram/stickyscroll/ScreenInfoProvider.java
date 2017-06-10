package co.biogram.stickyscroll;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

class ScreenInfoProvider implements IScreenInfoProvider{

    private final Context mContext;

    ScreenInfoProvider(Context context)
    {
        mContext = context;
    }

    @Override
    public int getScreenHeight()
    {
        return getDeviceDimension().y;
    }

    private Point getDeviceDimension()
    {
        Point lPoint = new Point();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        lPoint.x = metrics.widthPixels;
        lPoint.y = metrics.heightPixels;
        return lPoint;
    }
}
