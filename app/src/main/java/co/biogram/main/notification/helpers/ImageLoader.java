package co.biogram.main.notification.helpers;

import co.biogram.main.notification.Custom;

/**
 * Created by sohrab on 7/17/18.
 */

public interface ImageLoader
{
    void load(int uri, Custom onCompleted);

    void load(String imageResId, Custom onCompleted);
}
