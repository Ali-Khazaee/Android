package co.biogram.main.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewOnClick;
import co.biogram.main.misc.TextCrawler;
import co.biogram.media.MediaTransCoder;

public class WriteFragment extends Fragment
{
    private PermissionHandler PermissionHandler;

    private ImageView ImageViewImage;
    private ImageView ImageViewVideo;
    private ImageView ImageViewLink;

    private ImageView ImageViewThumbVideo;
    private RelativeLayout RelativeLayoutVideo;

    private ViewPager ViewPagerImage;
    private ViewPagerAdapter ViewPagerAdapterImage;

    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;
    private int RelativeLayoutMainHeightDifference = 0;

    private final List<Bitmap> SelectImage = new ArrayList<>();
    private int SelectCategory = 0;
    private File SelectVideo;
    private int SelectType = 0;
    private String SelectLink = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final EditText EditTextMessage = new EditText(context);
        final RelativeLayout RelativeLayoutLink = new RelativeLayout(context);
        final LoadingView LoadingViewLink = new LoadingView(context);
        final TextView TextViewTryLink = new TextView(context);
        final ImageView ImageViewRemoveLink = new ImageView(context);
        final TextView TextViewTitleLink = new TextView(context);
        final TextView TextViewDescriptionLink = new TextView(context);
        final ImageView ImageViewFavLink = new ImageView(context);
        final TextView TextViewCategorySelect = new TextView(context);

        RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    RelativeLayoutMainHeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    RelativeLayoutMainHeightDifference = DifferenceHeight;
                }
                else if (RelativeLayoutMainHeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                    RelativeLayoutMainHeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { MiscHandler.HideSoftKey(getActivity()); getActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(getString(R.string.WriteFragment));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSendParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        ImageViewSendParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewSend = new ImageView(context);
        ImageViewSend.setLayoutParams(ImageViewSendParam);
        ImageViewSend.setImageResource(R.drawable.ic_send_blue2);
        ImageViewSend.setId(MiscHandler.GenerateViewID());
        ImageViewSend.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));
        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SelectCategory == 0)
                {
                    MiscHandler.Toast(context, getString(R.string.WriteFragmentChoiceCategory));
                    return;
                }

                if (EditTextMessage.getText().length() <= 30 && SelectType == 0)
                {
                    MiscHandler.Toast(context, getString(R.string.WriteFragmentMoreContent));
                    return;
                }

                Map<String, File> UploadFile = new HashMap<>();

                if (SelectType == 1)
                {
                    try
                    {
                        for (int I = 0; I < SelectImage.size(); I++)
                        {
                            File CacheFolder = new File(context.getCacheDir(), "BioGram");

                            if (CacheFolder.exists() || CacheFolder.mkdir())
                            {
                                ByteArrayOutputStream ByteArrayStream = new ByteArrayOutputStream();
                                File ImageFile = new File(CacheFolder, "image." + String.valueOf(System.currentTimeMillis()) + ".jpg");

                                SelectImage.get(I).compress(Bitmap.CompressFormat.JPEG, 95, ByteArrayStream);

                                FileOutputStream FileStream = new FileOutputStream(ImageFile);
                                FileStream.write(ByteArrayStream.toByteArray());
                                FileStream.flush();
                                FileStream.close();

                                UploadFile.put(("Image" + I), ImageFile);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        MiscHandler.Debug("WriteFragment-ImageCompress: " + e.toString());
                    }
                }
                else if (SelectType == 2)
                    UploadFile.put("Video", SelectVideo);
                else
                    UploadFile = null;

                final ProgressDialog Progress = new ProgressDialog(getActivity());
                Progress.setMessage(getString(R.string.WriteFragmentUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                AndroidNetworking.upload(MiscHandler.GetRandomServer("PostWrite"))
                .addMultipartParameter("Message", EditTextMessage.getText().toString())
                .addMultipartParameter("Category", String.valueOf(SelectCategory))
                .addMultipartParameter("Type", String.valueOf(SelectType))
                .addMultipartParameter("Link", SelectLink)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addMultipartFile(UploadFile)
                .setTag("WriteFragment")
                .build()
                .setUploadProgressListener(new UploadProgressListener()
                {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes)
                    {
                        Progress.setProgress((int) (100 * bytesUploaded / totalBytes));
                    }
                })
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        Progress.cancel();

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                MiscHandler.HideSoftKey(getActivity());
                                getActivity().onBackPressed();
                                return;
                            }

                            MiscHandler.Toast(context, getString(R.string.WriteFragmentUploadFailed));
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("WriteFragment-RequestPost: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        Progress.cancel();
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        RelativeLayoutHeader.addView(ImageViewSend);

        RelativeLayout.LayoutParams TextViewMessageCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageCountParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewMessageCountParam.addRule(RelativeLayout.LEFT_OF, ImageViewSend.getId());
        TextViewMessageCountParam.setMargins(0, 0, MiscHandler.ToDimension(context, 5), 0);

        final TextView TextViewMessageCount = new TextView(context);
        TextViewMessageCount.setLayoutParams(TextViewMessageCountParam);
        TextViewMessageCount.setText(getString(R.string.WriteFragmentMessageCount));
        TextViewMessageCount.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewMessageCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutHeader.addView(TextViewMessageCount);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams EditTextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextMessageParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
        EditTextMessage.setId(MiscHandler.GenerateViewID());
        EditTextMessage.setMaxLines(5);
        EditTextMessage.setHint(R.string.WriteFragmentMessage);
        EditTextMessage.setBackground(null);
        EditTextMessage.requestFocus();
        EditTextMessage.setHintTextColor(ContextCompat.getColor(context, R.color.Gray2));
        EditTextMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
        EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        EditTextMessage.setCustomSelectionActionModeCallback(new android.view.ActionMode.Callback()
        {
            @Override public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) { return false; }
            @Override public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) { return false; }
            @Override public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) { return false; }
            @Override public void onDestroyActionMode(android.view.ActionMode mode) { }
        });
        EditTextMessage.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                TextViewMessageCount.setText(String.valueOf(150 - s.length()));
            }
        });
        EditTextMessage.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                final Dialog DialogOption = new Dialog(getActivity());
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(context);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewPaste = new TextView(context);
                TextViewPaste.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewPaste.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewPaste.setText(getActivity().getString(R.string.WriteFragmentDialogPaste));
                TextViewPaste.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewPaste.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewPaste.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                        if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType("text/plain"))
                        {
                            ClipData.Item ClipItem = clipboard.getPrimaryClip().getItemAt(0);
                            String Message = EditTextMessage.getText().toString() + ClipItem.getText().toString();
                            EditTextMessage.setText(Message);
                        }

                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewPaste);

                View PasteLine = new View(context);
                PasteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                PasteLine.setBackgroundResource(R.color.Gray1);

                LinearLayoutMain.addView(PasteLine);

                TextView TextViewCopy = new TextView(context);
                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCopy.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCopy.setText(getActivity().getString(R.string.WriteFragmentDialogCopy));
                TextViewCopy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCopy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewCopy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("WriteMessage", EditTextMessage.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        MiscHandler.Toast(context, getString(R.string.WriteFragmentDialogClipboard));
                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewCopy);

                DialogOption.setContentView(LinearLayoutMain);
                DialogOption.show();
                return false;
            }
        });

        RelativeLayoutMain.addView(EditTextMessage);

        RelativeLayout.LayoutParams LinearLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LinearLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutBottom = new LinearLayout(context);
        LinearLayoutBottom.setLayoutParams(LinearLayoutBottomParam);
        LinearLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutBottom.setBackgroundResource(R.color.White5);
        LinearLayoutBottom.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(LinearLayoutBottom);

        ImageViewImage = new ImageView(context);
        ImageViewImage.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 56), 1.0f));
        ImageViewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewImage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewImage.setImageResource(R.drawable.ic_camera);
        ImageViewImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (SelectImage.size() >= 3)
                {
                    MiscHandler.Toast(context, getString(R.string.WriteFragmentMaxImage));
                    return;
                }

                PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, WriteFragment.this, new PermissionHandler.PermissionEvent()
                {
                    @Override
                    public void OnGranted()
                    {
                        Matisse.from(WriteFragment.this)
                        .choose(MimeType.of(MimeType.PNG, MimeType.JPEG))
                        .countable(true)
                        .maxSelectable(3)
                        .gridExpectedSize(MiscHandler.ToDimension(context, 90))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(0);
                    }

                    @Override
                    public void OnFailed()
                    {
                        MiscHandler.Toast(context, getString(R.string.PermissionStorage));
                    }
                });
            }
        });

        LinearLayoutBottom.addView(ImageViewImage);

        ImageViewVideo = new ImageView(context);
        ImageViewVideo.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 56), 1.0f));
        ImageViewVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewVideo.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewVideo.setImageResource(R.drawable.ic_video);
        ImageViewVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, WriteFragment.this, new PermissionHandler.PermissionEvent()
                {
                    @Override
                    public void OnGranted()
                    {
                        Matisse.from(WriteFragment.this)
                        .choose(MimeType.ofVideo())
                        .countable(false)
                        .gridExpectedSize(MiscHandler.ToDimension(context, 90))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(1);
                    }

                    @Override
                    public void OnFailed()
                    {
                        MiscHandler.Toast(context, getString(R.string.PermissionStorage));
                    }
                });
            }
        });

        LinearLayoutBottom.addView(ImageViewVideo);

        ImageViewLink = new ImageView(context);
        ImageViewLink.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 56), 1.0f));
        ImageViewLink.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewLink.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewLink.setImageResource(R.drawable.ic_link);
        ImageViewLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog DialogLink = new Dialog(getActivity());
                DialogLink.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLink.setCancelable(false);

                LinearLayout LinearLayoutMain = new LinearLayout(context);
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitleLink2 = new TextView(context);
                TextViewTitleLink2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitleLink2.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitleLink2.setText(getString(R.string.WriteFragmentShareLink));
                TextViewTitleLink2.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTitleLink2.setTypeface(null, Typeface.BOLD);
                TextViewTitleLink2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                LinearLayoutMain.addView(TextViewTitleLink2);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray2);

                LinearLayoutMain.addView(ViewLine);

                LinearLayout.LayoutParams EditTextURLParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditTextURLParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

                final EditText EditTextURL = new EditText(context);
                EditTextURL.setLayoutParams(EditTextURLParam);
                EditTextURL.setTextColor(ContextCompat.getColor(context, R.color.Black));
                EditTextURL.setMaxLines(1);
                EditTextURL.setBackgroundColor(Color.TRANSPARENT);
                EditTextURL.setInputType(0x00000010);

                LinearLayoutMain.addView(EditTextURL);

                LinearLayout LinearLayoutButton = new LinearLayout(context);
                LinearLayoutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutButton.setBackgroundResource(R.color.White);
                LinearLayoutButton.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayoutMain.addView(LinearLayoutButton);

                final TextView TextViewSubmit = new TextView(context);
                TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                TextViewSubmit.setText(getString(R.string.WriteFragmentShareLinkSub));
                TextViewSubmit.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                TextViewSubmit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewSubmit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewSubmit.setEnabled(false);
                TextViewSubmit.setGravity(Gravity.CENTER);

                LinearLayoutButton.addView(TextViewSubmit);

                TextView TextViewCancel = new TextView(context);
                TextViewCancel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                TextViewCancel.setText(getString(R.string.WriteFragmentShareLinkCan));
                TextViewCancel.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCancel.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCancel.setGravity(Gravity.CENTER);

                LinearLayoutButton.addView(TextViewCancel);

                DialogLink.setContentView(LinearLayoutMain);
                DialogLink.show();

                if (SelectLink == null || SelectLink.equals(""))
                    EditTextURL.setText(String.valueOf("http://"));
                else
                {
                    TextViewSubmit.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                    TextViewSubmit.setEnabled(true);
                    EditTextURL.setText(SelectLink);
                }

                EditTextURL.setSelection(EditTextURL.getText().length());

                EditTextURL.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void onTextChanged(CharSequence s, int y, int u, int i)
                    {
                        if (s.length() > 5 && Patterns.WEB_URL.matcher(s.toString()).matches())
                        {
                            TextViewSubmit.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                            TextViewSubmit.setEnabled(true);
                        }
                        else
                        {
                            TextViewSubmit.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                            TextViewSubmit.setEnabled(false);
                        }
                    }

                    @Override public void beforeTextChanged(CharSequence s, int y, int u, int i) { }
                    @Override public void afterTextChanged(Editable e) { }
                });

                if (DialogLink.getWindow() != null)
                    DialogLink.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                TextViewCancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ChangeType(0);
                        SelectLink = "";
                        DialogLink.dismiss();
                    }
                });

                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ChangeType(3);
                        SelectLink = EditTextURL.getText().toString();

                        if (!SelectLink.startsWith("http://") && !SelectLink.startsWith("https://"))
                            SelectLink = "http://" + SelectLink;

                        DialogLink.dismiss();

                        RelativeLayoutLink.setVisibility(View.VISIBLE);
                        LoadingViewLink.Start();

                        TextViewTitleLink.setText("");
                        TextViewDescriptionLink.setText("");
                        ImageViewFavLink.setImageResource(android.R.color.transparent);
                        ImageViewRemoveLink.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                ChangeType(0);
                                SelectLink = "";
                                TextViewTitleLink.setText("");
                                LoadingViewLink.Stop();
                                TextViewDescriptionLink.setText("");
                                ImageViewFavLink.setImageResource(android.R.color.transparent);
                                RelativeLayoutLink.setVisibility(View.GONE);
                            }
                        });

                        final TextCrawler Request = new TextCrawler(context, SelectLink, "WriteFragment", new TextCrawler.TextCrawlerCallBack()
                        {
                            @Override
                            public void OnCompleted(TextCrawler.URLContent Content)
                            {
                                LoadingViewLink.Stop();

                                if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                {
                                    RelativeLayout.LayoutParams TextViewTitleLinkParam = (RelativeLayout.LayoutParams) TextViewTitleLink.getLayoutParams();
                                    TextViewTitleLinkParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    TextViewTitleLink.setLayoutParams(TextViewTitleLinkParam);
                                }
                                else
                                {
                                    RelativeLayout.LayoutParams TextViewTitleLinkParam = (RelativeLayout.LayoutParams) TextViewTitleLink.getLayoutParams();
                                    TextViewTitleLinkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    TextViewTitleLink.setLayoutParams(TextViewTitleLinkParam);
                                }

                                if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                {
                                    RelativeLayout.LayoutParams TextViewDescriptionLinkParam = (RelativeLayout.LayoutParams) TextViewDescriptionLink.getLayoutParams();
                                    TextViewDescriptionLinkParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    TextViewDescriptionLink.setLayoutParams(TextViewDescriptionLinkParam);
                                }
                                else
                                {
                                    RelativeLayout.LayoutParams TextViewDescriptionLinkParam = (RelativeLayout.LayoutParams) TextViewDescriptionLink.getLayoutParams();
                                    TextViewDescriptionLinkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    TextViewDescriptionLink.setLayoutParams(TextViewDescriptionLinkParam);
                                }

                                TextViewTitleLink.setText(Content.Title);
                                TextViewDescriptionLink.setText(Content.Description);

                                Glide.with(context).load(Content.Image).into(ImageViewFavLink);
                            }

                            @Override
                            public void OnFailed()
                            {
                                TextViewTitleLink.setText("");
                                LoadingViewLink.Stop();
                                TextViewDescriptionLink.setText("");
                                ImageViewFavLink.setImageResource(android.R.color.transparent);
                                TextViewTryLink.setVisibility(View.VISIBLE);
                            }
                        });

                        TextViewTryLink.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Request.Start();
                                LoadingViewLink.Start();
                                TextViewTryLink.setVisibility(View.GONE);
                            }
                        });

                        Request.Start();
                    }
                });
            }
        });

        LinearLayoutBottom.addView(ImageViewLink);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLine2Param.addRule(RelativeLayout.ABOVE, LinearLayoutBottom.getId());

        View ViewLine2 = new View(context);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(R.color.Gray2);
        ViewLine2.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 40));
        LinearLayoutCategoryParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());
        LinearLayoutCategoryParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 3));

        LinearLayout LinearLayoutCategory = new LinearLayout(context);
        LinearLayoutCategory.setLayoutParams(LinearLayoutCategoryParam);
        LinearLayoutCategory.setBackgroundResource(R.color.White);
        LinearLayoutCategory.setId(MiscHandler.GenerateViewID());
        LinearLayoutCategory.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCategory.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayoutCategory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditTextMessage.clearFocus();
                MiscHandler.HideSoftKey(getActivity());

                final Dialog DialogCategory = new Dialog(getActivity());
                DialogCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCategory.setCancelable(false);

                RelativeLayout LinearLayoutMain = new RelativeLayout(context);
                LinearLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
                RelativeLayoutHeader.setBackgroundResource(R.color.White5);
                RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
                ImageViewBackParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewBack = new ImageView(context);
                ImageViewBack.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));
                ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.ic_close_blue);
                ImageViewBack.setId(MiscHandler.GenerateViewID());
                ImageViewBack.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogCategory.dismiss();
                    }
                });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewNameParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);

                TextView TextViewName = new TextView(context);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewName.setText(getString(R.string.WriteFragmentCategory2));
                TextViewName.setTypeface(null, Typeface.BOLD);
                TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                RelativeLayoutHeader.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray2);
                ViewLine.setId(MiscHandler.GenerateViewID());

                LinearLayoutMain.addView(ViewLine);

                RelativeLayout.LayoutParams RecyclerViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RecyclerViewCategoryParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                RecyclerView RecyclerViewCategory = new RecyclerView(context);
                RecyclerViewCategory.setLayoutManager(new LinearLayoutManager(context));
                RecyclerViewCategory.setAdapter(new AdapterCategory(context));
                RecyclerViewCategory.setLayoutParams(RecyclerViewCategoryParam);
                RecyclerViewCategory.addOnItemTouchListener(new RecyclerViewOnClick(context, RecyclerViewCategory, new RecyclerViewOnClick.OnItemClickListener()
                {
                    @Override
                    public void OnClick(View view, int Position)
                    {
                        switch (Position)
                        {
                            case 0:  TextViewCategorySelect.setText(getString(R.string.CategoryNews));       SelectCategory = 1;  break;
                            case 1:  TextViewCategorySelect.setText(getString(R.string.CategoryFun));        SelectCategory = 2;  break;
                            case 2:  TextViewCategorySelect.setText(getString(R.string.CategoryMusic));      SelectCategory = 3;  break;
                            case 3:  TextViewCategorySelect.setText(getString(R.string.CategorySport));      SelectCategory = 4;  break;
                            case 4:  TextViewCategorySelect.setText(getString(R.string.CategoryFashion));    SelectCategory = 5;  break;
                            case 5:  TextViewCategorySelect.setText(getString(R.string.CategoryFood));       SelectCategory = 6;  break;
                            case 6:  TextViewCategorySelect.setText(getString(R.string.CategoryTechnology)); SelectCategory = 7;  break;
                            case 7:  TextViewCategorySelect.setText(getString(R.string.CategoryArt));        SelectCategory = 8;  break;
                            case 8:  TextViewCategorySelect.setText(getString(R.string.CategoryArtist));     SelectCategory = 9;  break;
                            case 9:  TextViewCategorySelect.setText(getString(R.string.CategoryMedia));      SelectCategory = 10; break;
                            case 10: TextViewCategorySelect.setText(getString(R.string.CategoryBusiness));   SelectCategory = 11; break;
                            case 11: TextViewCategorySelect.setText(getString(R.string.CategoryEconomy));    SelectCategory = 12; break;
                            case 12: TextViewCategorySelect.setText(getString(R.string.CategoryLiterature)); SelectCategory = 13; break;
                            case 13: TextViewCategorySelect.setText(getString(R.string.CategoryTravel));     SelectCategory = 14; break;
                            case 14: TextViewCategorySelect.setText(getString(R.string.CategoryPolitics));   SelectCategory = 15; break;
                            case 15: TextViewCategorySelect.setText(getString(R.string.CategoryHealth));     SelectCategory = 16; break;
                            case 16: TextViewCategorySelect.setText(getString(R.string.CategoryReligious));  SelectCategory = 18; break;
                            case 17: TextViewCategorySelect.setText(getString(R.string.CategoryOther));      SelectCategory = 17; break;
                        }

                        DialogCategory.dismiss();
                    }

                    @Override public void OnLongClick(View view, int position) { }
                }));

                LinearLayoutMain.addView(RecyclerViewCategory);

                DialogCategory.setContentView(LinearLayoutMain);
                DialogCategory.show();
            }
        });

        RelativeLayoutMain.addView(LinearLayoutCategory);

        RelativeLayout.LayoutParams TextViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCategoryParam.setMargins(MiscHandler.ToDimension(context, 5), 0, 0, 0);

        TextView TextViewCategory = new TextView(context);
        TextViewCategory.setLayoutParams(TextViewCategoryParam);
        TextViewCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewCategory.setText(getString(R.string.WriteFragmentCategory));
        TextViewCategory.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));

        LinearLayoutCategory.addView(TextViewCategory);

        TextViewCategorySelect.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewCategorySelect.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewCategorySelect.setText(getString(R.string.WriteFragmentCategorySelect));
        TextViewCategorySelect.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewCategorySelect.setPadding(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        LinearLayoutCategory.addView(TextViewCategorySelect);

        ImageView ImageViewArrow = new ImageView(context);
        ImageViewArrow.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 15), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewArrow.setImageResource(R.drawable.ic_arrow_down_blue);

        LinearLayoutCategory.addView(ImageViewArrow);

        RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextMessage.getId());
        RelativeLayoutContentParam.addRule(RelativeLayout.ABOVE, LinearLayoutCategory.getId());

        RelativeLayout RelativeLayoutContent = new RelativeLayout(context);
        RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

        RelativeLayoutMain.addView(RelativeLayoutContent);

        RelativeLayout.LayoutParams RelativeLayoutLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutLinkParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RelativeLayoutLinkParam.setMargins(MiscHandler.ToDimension(context, 10), 0 , MiscHandler.ToDimension(context, 10), 0);

        GradientDrawable Shape = new GradientDrawable();
        Shape.setStroke(MiscHandler.ToDimension(context, 1), ContextCompat.getColor(context, R.color.BlueGray));

        RelativeLayoutLink.setLayoutParams(RelativeLayoutLinkParam);
        RelativeLayoutLink.setVisibility(View.GONE);
        RelativeLayoutLink.setBackground(Shape);

        RelativeLayoutContent.addView(RelativeLayoutLink);

        RelativeLayout.LayoutParams LoadingViewLinkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewLinkParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewLink.setLayoutParams(LoadingViewLinkParam);

        RelativeLayoutLink.addView(LoadingViewLink);

        RelativeLayout.LayoutParams TextViewTryLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryLinkParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTryLink.setLayoutParams(TextViewTryLinkParam);
        TextViewTryLink.setVisibility(View.GONE);
        TextViewTryLink.setText(getString(R.string.TryAgain));
        TextViewTryLink.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTryLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutLink.addView(TextViewTryLink);

        RelativeLayout.LayoutParams ImageViewRemoveLinkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25));
        ImageViewRemoveLinkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewRemoveLinkParam.setMargins(0, MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), 0);

        ImageViewRemoveLink.setLayoutParams(ImageViewRemoveLinkParam);
        ImageViewRemoveLink.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveLink.setImageResource(R.drawable.ic_remove);
        ImageViewRemoveLink.setAlpha(0.75f);
        ImageViewRemoveLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutLink.addView(ImageViewRemoveLink);

        RelativeLayout.LayoutParams TextViewTitleLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleLinkParam.addRule(RelativeLayout.LEFT_OF, ImageViewRemoveLink.getId());
        TextViewTitleLinkParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5), 0, 0);

        TextViewTitleLink.setLayoutParams(TextViewTitleLinkParam);
        TextViewTitleLink.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTitleLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitleLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutLink.addView(TextViewTitleLink);

        RelativeLayout.LayoutParams TextViewDescriptionLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionLinkParam.addRule(RelativeLayout.LEFT_OF, ImageViewRemoveLink.getId());
        TextViewDescriptionLinkParam.addRule(RelativeLayout.BELOW, TextViewTitleLink.getId());
        TextViewDescriptionLinkParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 3), 0, MiscHandler.ToDimension(context, 5));

        TextViewDescriptionLink.setLayoutParams(TextViewDescriptionLinkParam);
        TextViewDescriptionLink.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewDescriptionLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDescriptionLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutLink.addView(TextViewDescriptionLink);

        RelativeLayout.LayoutParams ImageViewFavLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageViewFavLinkParam.addRule(RelativeLayout.BELOW, TextViewDescriptionLink.getId());
        ImageViewFavLinkParam.setMargins(MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1));

        ImageViewFavLink.setLayoutParams(ImageViewFavLinkParam);
        ImageViewFavLink.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayoutLink.addView(ImageViewFavLink);

        ViewPagerImage = new ViewPager(context);
        ViewPagerImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewPagerImage.setVisibility(View.GONE);
        ViewPagerImage.setAdapter(ViewPagerAdapterImage = new ViewPagerAdapter());

        RelativeLayoutContent.addView(ViewPagerImage);

        RelativeLayout.LayoutParams RelativeLayoutVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RelativeLayoutVideoParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        RelativeLayoutVideo = new RelativeLayout(context);
        RelativeLayoutVideo.setLayoutParams(RelativeLayoutVideoParam);
        RelativeLayoutVideo.setBackgroundResource(R.color.Black);
        RelativeLayoutVideo.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutVideo);

        ImageViewThumbVideo = new ImageView(context);
        ImageViewThumbVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewThumbVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewThumbVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MiscHandler.HideSoftKey(getActivity());

                Bundle bundle = new Bundle();
                bundle.putString("VideoURL", SelectVideo.getAbsolutePath());

                Fragment fragment = new VideoPreviewFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("VideoPreviewFragment").commit();
            }
        });

        RelativeLayoutVideo.addView(ImageViewThumbVideo);

        RelativeLayout.LayoutParams ImageViewRemoveVideoParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25));
        ImageViewRemoveVideoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewRemoveVideoParam.setMargins(0, MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), 0);

        ImageView ImageViewRemoveVideo = new ImageView(context);
        ImageViewRemoveVideo.setLayoutParams(ImageViewRemoveVideoParam);
        ImageViewRemoveVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveVideo.setImageResource(R.drawable.ic_remove);
        ImageViewRemoveVideo.setAlpha(0.75f);
        ImageViewRemoveVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(0);
                SelectVideo = null;
                ImageViewThumbVideo.setImageResource(android.R.color.transparent);
                RelativeLayoutVideo.setVisibility(View.GONE);
            }
        });

        RelativeLayoutVideo.addView(ImageViewRemoveVideo);

        RelativeLayout.LayoutParams ImageViewPlayVideoParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 65), MiscHandler.ToDimension(context, 65));
        ImageViewPlayVideoParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPlayVideo = new ImageView(context);
        ImageViewPlayVideo.setLayoutParams(ImageViewPlayVideoParam);
        ImageViewPlayVideo.setImageResource(R.drawable.ic_play);

        RelativeLayoutVideo.addView(ImageViewPlayVideo);

        return RelativeLayoutMain;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("WriteFragment");
        MiscHandler.HideSoftKey(getActivity());
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        if (PermissionHandler != null)
            PermissionHandler.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);
    }

    @Override
    public void onActivityResult(final int RequestCode, int ResultCode, final Intent Data)
    {
        if (Data == null || ResultCode == 0)
            return;

        final String URL;
        final Context context = getActivity();

        Cursor cursor = context.getContentResolver().query(Matisse.obtainResult(Data).get(0), new String[] { MediaStore.Images.Media.DATA }, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            URL = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        else
        {
            MiscHandler.Toast(context, getString(R.string.FileNotFound));
            return;
        }

        PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, this, new PermissionHandler.PermissionEvent()
        {
            @Override
            public void OnGranted()
            {
                if (RequestCode == 0)
                {
                    try
                    {
                        for (int I = 0; I < Matisse.obtainResult(Data).size(); I++)
                        {
                            String ImagePath = "";
                            Cursor cursor = context.getContentResolver().query(Matisse.obtainResult(Data).get(I), new String[] { MediaStore.Images.Media.DATA }, null, null, null);

                            if (cursor != null && cursor.moveToFirst())
                            {
                                ImagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                                cursor.close();
                            }
                            else
                                continue;

                            File ImageFile = new File(ImagePath);
                            Bitmap ResizeBitmap;

                            if (ImageFile.length() > 66560)
                            {
                                BitmapFactory.Options O = new BitmapFactory.Options();
                                O.inJustDecodeBounds = true;

                                BitmapFactory.decodeFile(ImageFile.getAbsolutePath(), O);

                                int Scale = 1;
                                int Height = O.outHeight;
                                int Width = O.outWidth;

                                if (Height > 500 || Width > 500)
                                {
                                    int HalfHeight = Height / 2;
                                    int HalfWidth = Width / 2;

                                    while ((HalfHeight / Scale) >= 500 && (HalfWidth / Scale) >= 500)
                                    {
                                        Scale *= 2;
                                    }
                                }

                                O.inJustDecodeBounds = false;
                                O.inSampleSize = Scale;

                                ResizeBitmap = BitmapFactory.decodeFile(ImageFile.getAbsolutePath(), O);

                                Matrix matrix = new Matrix();
                                int Orientation = new ExifInterface(ImagePath).getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

                                if (Orientation == 6)
                                    matrix.postRotate(90);
                                else if (Orientation == 3)
                                    matrix.postRotate(180);
                                else if (Orientation == 8)
                                    matrix.postRotate(270);

                                ResizeBitmap = Bitmap.createBitmap(ResizeBitmap, 0, 0, ResizeBitmap.getWidth(), ResizeBitmap.getHeight(), matrix, true);
                            }
                            else
                                ResizeBitmap = BitmapFactory.decodeFile(ImageFile.getAbsolutePath());

                            ChangeType(1);
                            SelectImage.add(ResizeBitmap);
                            ViewPagerAdapterImage.notifyDataSetChanged();
                            ViewPagerImage.setCurrentItem(SelectImage.size());
                            ViewPagerImage.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception e)
                    {
                        MiscHandler.Debug("WriteFragment-ImageCompress: " + e.toString());
                    }
                }

                if (RequestCode == 1)
                {
                    final MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                    Retriever.setDataSource(URL);
                    int Time = Math.round(Integer.parseInt(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000);

                    if (Time > 300)
                    {
                        MiscHandler.Toast(context, getString(R.string.WriteFragmentVideoLength));
                        return;
                    }

                    SelectVideo = new File(URL);

                    if (SelectVideo.length() < 3145728)
                    {
                        ChangeType(2);
                        ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                        RelativeLayoutVideo.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (Build.VERSION.SDK_INT <= 17)
                    {
                        if (SelectVideo.length() > 3145728)
                        {
                            MiscHandler.Toast(context, getString(R.string.WriteFragmentVideoSupport));
                            return;
                        }

                        ChangeType(2);
                        ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                        RelativeLayoutVideo.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (SelectVideo.length() > 31452800)
                    {
                        MiscHandler.Toast(context, getString(R.string.WriteFragmentVideoSize));
                        return;
                    }

                    File CacheFolder = new File(context.getCacheDir(), "BioGram");

                    if (!CacheFolder.exists() && !CacheFolder.mkdir())
                    {
                        MiscHandler.Toast(context, getString(R.string.WriteFragmentWrong));
                        return;
                    }

                    SelectVideo = new File(CacheFolder, "video." + String.valueOf(System.currentTimeMillis()) + ".mp4");

                    final ProgressDialog Progress = new ProgressDialog(getActivity());
                    Progress.setMessage(getString(R.string.WriteFragmentVideoCompress));
                    Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    Progress.setIndeterminate(false);
                    Progress.setMax(100);
                    Progress.setCancelable(false);
                    Progress.setProgress(0);
                    Progress.show();

                    MediaTransCoder.Start(URL, SelectVideo.getAbsolutePath(), new MediaTransCoder.MediaStrategy()
                    {
                        @Override
                        public MediaFormat CreateVideo(MediaFormat Format)
                        {
                            int Frame = 30;
                            int BitRate = 450000;
                            int Width = Format.getInteger(MediaFormat.KEY_WIDTH);
                            int Height = Format.getInteger(MediaFormat.KEY_HEIGHT);

                            if (Width > 640 || Height > 640)
                            {
                                Width = Width / 2;
                                Height = Height / 2;
                            }

                            try { Frame = Format.getInteger(MediaFormat.KEY_FRAME_RATE); } catch (Exception e) { /* */ }

                            MediaFormat format = MediaFormat.createVideoFormat("video/avc", Width, Height);
                            format.setInteger(MediaFormat.KEY_BIT_RATE, BitRate);
                            format.setInteger(MediaFormat.KEY_FRAME_RATE, Frame);
                            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
                            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, 0x7F000789);
                            return format;
                        }

                        @Override
                        public MediaFormat CreateAudio(MediaFormat Format)
                        {
                            int Sample = 44100;
                            int Channel = 1;

                            try { Sample = Format.getInteger(MediaFormat.KEY_SAMPLE_RATE); } catch (Exception e) { /* */ }
                            try { Channel = Format.getInteger(MediaFormat.KEY_CHANNEL_COUNT); } catch (Exception e) { /* */ }

                            int Bitrate = Sample * Channel;

                            MediaFormat format = MediaFormat.createAudioFormat("audio/mp4a-latm", Sample, Channel);
                            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                            format.setInteger(MediaFormat.KEY_BIT_RATE, Bitrate);
                            return format;
                        }
                    },
                    new MediaTransCoder.CallBack()
                    {
                        @Override
                        public void OnProgress(double progress)
                        {
                            Progress.setProgress((int) (((progress + 0.001) * 100) % 100));
                        }

                        @Override
                        public void OnCompleted()
                        {
                            getActivity().runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Progress.cancel();
                                    ChangeType(2);
                                    ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                                    RelativeLayoutVideo.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                        @Override
                        public void OnFailed(Exception exception)
                        {
                            Progress.cancel();
                            MiscHandler.Debug("WriteFragment-VideoCompress: " + exception.toString());
                        }
                    });
                }
            }

            @Override
            public void OnFailed()
            {
                MiscHandler.Toast(context, getString(R.string.PermissionStorage));
            }
        });
    }

    private void ChangeType(int type)
    {
        if (type == 0)
        {
            SelectType = 0;
            ImageViewImage.setEnabled(true);
            ImageViewImage.setImageResource(R.drawable.ic_camera);
            ImageViewVideo.setEnabled(true);
            ImageViewVideo.setImageResource(R.drawable.ic_video);
            ImageViewLink.setEnabled(true);
            ImageViewLink.setImageResource(R.drawable.ic_link);
            return;
        }

        ImageViewImage.setEnabled(false);
        ImageViewImage.setImageResource(R.drawable.ic_camera_gray);
        ImageViewVideo.setEnabled(false);
        ImageViewVideo.setImageResource(R.drawable.ic_video_gray);
        ImageViewLink.setEnabled(false);
        ImageViewLink.setImageResource(R.drawable.ic_link_gray);

        switch (type)
        {
            case 1:
                SelectType = 1;
                ImageViewImage.setEnabled(true);
                ImageViewImage.setImageResource(R.drawable.ic_camera);
                break;
            case 2:
                SelectType = 2;
                ImageViewVideo.setEnabled(true);
                ImageViewVideo.setImageResource(R.drawable.ic_video);
                break;
            case 3:
                SelectType = 3;
                ImageViewLink.setEnabled(true);
                ImageViewLink.setImageResource(R.drawable.ic_link);
                break;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter
    {
        @Override
        public Object instantiateItem(ViewGroup Container, final int Position)
        {
            final Context context = getActivity();

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            RelativeLayout.LayoutParams ImageViewImageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewImageParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            ImageView ImageViewImage = new ImageView(context);
            ImageViewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewImage.setLayoutParams(ImageViewImageParam);
            ImageViewImage.setImageBitmap(SelectImage.get(Position));
            ImageViewImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SelectImage.size() > 0)
                    {
                        MiscHandler.HideSoftKey(getActivity());

                        ImagePreviewFragment fragment = new ImagePreviewFragment();
                        fragment.SetBitmap(SelectImage.get(Position));

                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ImagePreviewFragment").commit();
                    }
                }
            });

            RelativeLayoutMain.addView(ImageViewImage);

            RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
            ImageViewRemoveParam.setMargins(0, MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 20), 0);
            ImageViewRemoveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewRemove = new ImageView(context);
            ImageViewRemove.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewRemove.setLayoutParams(ImageViewRemoveParam);
            ImageViewRemove.setAlpha(0.75f);
            ImageViewRemove.setImageResource(R.drawable.ic_remove);
            ImageViewRemove.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    SelectImage.remove(Position);
                    ViewPagerAdapterImage.notifyDataSetChanged();

                    if (SelectImage.size() <= 0)
                    {
                        ChangeType(0);
                        ViewPagerImage.setVisibility(View.GONE);
                    }
                }
            });

            RelativeLayoutMain.addView(ImageViewRemove);

            Container.addView(RelativeLayoutMain);

            return RelativeLayoutMain;
        }

        @Override
        public void destroyItem(ViewGroup Container, int position, Object object)
        {
            Container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public int getCount()
        {
            return SelectImage.size();
        }
    }

    private class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolderCategory>
    {
        private final List<Struct> CategoryList = new ArrayList<>();

        private final int ID_ICON = MiscHandler.GenerateViewID();
        private final int ID_NAME = MiscHandler.GenerateViewID();
        private final int ID_LINE = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterCategory(Context c)
        {
            context = c;

            CategoryList.clear();
            CategoryList.add(new Struct(getString(R.string.CategoryNews), R.drawable.ic_category_news));
            CategoryList.add(new Struct(getString(R.string.CategoryFun), R.drawable.ic_category_fun));
            CategoryList.add(new Struct(getString(R.string.CategoryMusic), R.drawable.ic_category_music));
            CategoryList.add(new Struct(getString(R.string.CategorySport), R.drawable.ic_category_sport));
            CategoryList.add(new Struct(getString(R.string.CategoryFashion), R.drawable.ic_category_fashion));
            CategoryList.add(new Struct(getString(R.string.CategoryFood), R.drawable.ic_category_food));
            CategoryList.add(new Struct(getString(R.string.CategoryTechnology), R.drawable.ic_category_technology));
            CategoryList.add(new Struct(getString(R.string.CategoryArt), R.drawable.ic_category_art));
            CategoryList.add(new Struct(getString(R.string.CategoryArtist), R.drawable.ic_category_artist));
            CategoryList.add(new Struct(getString(R.string.CategoryMedia), R.drawable.ic_category_media));
            CategoryList.add(new Struct(getString(R.string.CategoryBusiness), R.drawable.ic_category_business));
            CategoryList.add(new Struct(getString(R.string.CategoryEconomy), R.drawable.ic_category_echonomy));
            CategoryList.add(new Struct(getString(R.string.CategoryLiterature), R.drawable.ic_category_lilterature));
            CategoryList.add(new Struct(getString(R.string.CategoryTravel), R.drawable.ic_category_travel));
            CategoryList.add(new Struct(getString(R.string.CategoryPolitics), R.drawable.ic_category_politics));
            CategoryList.add(new Struct(getString(R.string.CategoryHealth), R.drawable.ic_category_health));
            CategoryList.add(new Struct(getString(R.string.CategoryReligious), R.drawable.ic_category_religious));
            CategoryList.add(new Struct(getString(R.string.CategoryOther), R.drawable.ic_category_other));
        }

        class ViewHolderCategory extends RecyclerView.ViewHolder
        {
            final ImageView ImageViewIcon;
            final TextView TextViewName;
            final View ViewLine;

            ViewHolderCategory(View view)
            {
                super(view);
                ImageViewIcon = (ImageView) view.findViewById(ID_ICON);
                TextViewName = (TextView) view.findViewById(ID_NAME);
                ViewLine = view.findViewById(ID_LINE);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolderCategory Holder, int p)
        {
            final int Position = Holder.getAdapterPosition();

            Glide.with(context)
            .load(CategoryList.get(Position).Image)
            .override(MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 45))
            .dontAnimate()
            .into(Holder.ImageViewIcon);

            Holder.TextViewName.setText(CategoryList.get(Position).Name);

            if (Position == CategoryList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount()
        {
            return CategoryList.size();
        }

        @Override
        public ViewHolderCategory onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutMain.setId(MiscHandler.GenerateViewID());
            RelativeLayoutMain.setClickable(true);

            ImageView ImageViewIcon = new ImageView(context);
            ImageViewIcon.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
            ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 45)));
            ImageViewIcon.setId(ID_ICON);

            RelativeLayoutMain.addView(ImageViewIcon);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID_ICON);
            TextViewNameParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);
            TextViewNameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            TextView TextViewName = new TextView(context);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewName.setId(ID_NAME);
            TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, ID_ICON);

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray);
            ViewLine.setId(ID_LINE);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderCategory(RelativeLayoutMain);
        }

        class Struct
        {
            final String Name;
            final int Image;

            Struct(String name, int image)
            {
                Name = name;
                Image = image;
            }
        }
    }
}
