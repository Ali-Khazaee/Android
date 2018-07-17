package co.biogram.main.ui.notification.helpers;

import co.biogram.main.ui.notification.Custom;

/**
 * Created by sohrab on 7/17/18.
 */

public interface ImageLoader
{
    void load(int uri, Custom onCompleted);

    void load(String imageResId, Custom onCompleted);
}
