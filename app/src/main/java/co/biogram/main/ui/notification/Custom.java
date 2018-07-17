package co.biogram.main.ui.notification;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.text.Spanned;
import android.widget.RemoteViews;
import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.ui.notification.helpers.ImageLoader;
import co.biogram.main.ui.notification.helpers.imageLoadingCompleted;

/**
 * Created by sohrab on 7/17/18.
 */

@TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
public class Custom extends Builder implements imageLoadingCompleted
{
    private static final String TAG = Custom.class.getSimpleName();
    private RemoteViews mRemoteView;
    private String mTitle;
    private String mMessage;
    private Spanned mMessageSpanned;
    private String mUri;
    private Bitmap mLargeIcon;
    private int mSmallIcon;
    private int mBackgroundResId;
    private int mPlaceHolderResourceId;
    private ImageLoader mImageLoader;

    public Custom(NotificationCompat.Builder builder, int identifier, String title, String message, Spanned messageSpanned, int smallIcon, Bitmap largeIcon, String tag)
    {
        super(builder, identifier, tag);
        this.mRemoteView = new RemoteViews(App.getContext().getPackageName(), R.layout.notification_custom);
        this.mTitle = title;
        this.mMessage = message;
        this.mMessageSpanned = messageSpanned;
        this.mSmallIcon = smallIcon;
        this.mLargeIcon = largeIcon;
        this.mPlaceHolderResourceId = R.drawable.ic_logo;
        this.init();
    }

    private void init()
    {
        this.setTitle();
        this.setMessage();
        this.setIcon();
    }

    private void setTitle()
    {
        mRemoteView.setTextViewText(R.id.notification_text_title, mTitle);
    }

    private void setMessage()
    {
        if (mMessageSpanned != null)
        {
            mRemoteView.setTextViewText(R.id.notification_text_message, mMessageSpanned);
        }
        else
        {
            mRemoteView.setTextViewText(R.id.notification_text_message, mMessage);
        }
    }

    private void setIcon()
    {
        mRemoteView.setImageViewBitmap(R.id.notification_img_icon, mLargeIcon);
    }

    public Custom background(@DrawableRes int resource)
    {
        if (resource <= 0)
        {
            throw new IllegalArgumentException("Resource ID Should Not Be Less Than Or Equal To Zero!");
        }

        if (mUri != null)
        {
            throw new IllegalStateException("Background Already Set!");
        }

        this.mBackgroundResId = resource;
        return this;
    }

    public Custom setPlaceholder(@DrawableRes int resource)
    {
        if (resource <= 0)
        {
            throw new IllegalArgumentException("Resource ID Should Not Be Less Than Or Equal To Zero!");
        }

        this.mPlaceHolderResourceId = resource;
        return this;
    }

    public Custom setImageLoader(ImageLoader imageLoader)
    {
        this.mImageLoader = imageLoader;
        return this;
    }

    public Custom background(String uri)
    {
        if (mBackgroundResId > 0)
        {
            throw new IllegalStateException("Background Already Set!");
        }

        if (mUri != null)
        {
            throw new IllegalStateException("Background Already Set!");
        }

        if (uri == null)
        {
            throw new IllegalArgumentException("Path Must Not Be Null!");
        }
        if (uri.trim().length() == 0)
        {
            throw new IllegalArgumentException("Path Must Not Be Empty!");
        }

        if (mImageLoader == null)
        {
            throw new IllegalStateException("You have to set an ImageLoader!");
        }

        this.mUri = uri;
        return this;
    }

    @Override
    public void build()
    {
        if (!(Looper.getMainLooper().getThread() == Thread.currentThread()))
        {
            throw new IllegalStateException("Method call should happen from the main thread.");
        }

        super.build();
        setBigContentView(mRemoteView);
        loadImageBackground();
        super.notificationNotify();
    }

    private void loadImageBackground()
    {
        mRemoteView.setImageViewResource(R.id.notification_img_background, mPlaceHolderResourceId);
        if (mUri != null)
        {
            mImageLoader.load(mUri, this);
        }
        else
        {
            mImageLoader.load(mBackgroundResId, this);
        }
    }

    @Override
    public void imageLoadingCompleted(Bitmap bitmap)
    {
        if (bitmap == null)
        {
            throw new IllegalArgumentException("bitmap cannot be null");
        }
        mRemoteView.setImageViewBitmap(R.id.notification_img_background, bitmap);
        super.notificationNotify();
    }
}