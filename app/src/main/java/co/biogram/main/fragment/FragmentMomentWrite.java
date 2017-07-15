package co.biogram.main.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.os.Environment;
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
import android.view.MotionEvent;
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
import android.widget.VideoView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.TextCrawler;
import co.biogram.media.MediaTransCoder;

public class FragmentMomentWrite extends Fragment
{
    private int LastDifferenceHeight = 0;

    private ImageView ImageViewImage;
    private ImageView ImageViewVideo;
    private ImageView ImageViewLink;

    private int SelectType = 0;
    private EditText EditTextMessage;

    private TextView TextViewCategorySelect;
    private String SelectLink = "";
    private int SelectCategory = 0;

    private RelativeLayout RelativeLayoutLink;
    private LoadingView LoadingViewLink;
    private TextView TextViewTryLink;
    private ImageView ImageViewRemoveLink;
    private TextView TextViewTitleLink;
    private TextView TextViewDescriptionLink;
    private ImageView ImageViewFavLink;

    private ViewPager ViewPagerImage;
    private ViewPagerAdapter ViewPagerAdapterImage;
    private final List<Bitmap> SelectImage = new ArrayList<>();

    private File SelectVideo;
    private RelativeLayout RelativeLayoutVideo;
    private ImageView ImageViewThumbVideo;

    private boolean IsPermissionStorageAllowed = false;
    private PermissionHandler _PermissionHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        final RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setClickable(true);

        Root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                Root.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = Root.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                {
                    Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    LastDifferenceHeight = DifferenceHeight;
                }
                else if (DifferenceHeight != LastDifferenceHeight)
                {
                    Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    LastDifferenceHeight = DifferenceHeight;
                }
                else if (LastDifferenceHeight != 0)
                {
                    Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDifferenceHeight)));
                    LastDifferenceHeight = 0;
                }
            }
        });

        final RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        Root.addView(RelativeLayoutHeader);

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
        TextViewTitle.setText(getString(R.string.FragmentMomentWrite));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ButtonSendParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        ButtonSendParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewSend = new ImageView(context);
        ImageViewSend.setLayoutParams(ButtonSendParam);
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
                    MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteChoiceCategory));
                    return;
                }

                if (EditTextMessage.getText().length() <= 19 && SelectType == 0)
                {
                    MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteMoreContent));
                    return;
                }

                Map<String, File> UploadFile = new HashMap<>();

                if (SelectType == 1)
                {
                    try
                    {
                        boolean IsCreated = true;
                        File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                        if (!Root.exists())
                            IsCreated = Root.mkdirs();

                        if (!IsCreated)
                        {
                            MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteCantCreateFolder));
                            return;
                        }

                        for (int A = 0; A < SelectImage.size(); A++)
                        {
                            File IMG = new File(Root, "image." + String.valueOf(System.currentTimeMillis()) + ".jpg");

                            ByteArrayOutputStream BOS = new ByteArrayOutputStream();
                            SelectImage.get(A).compress(Bitmap.CompressFormat.JPEG, 90, BOS);

                            FileOutputStream FOS = new FileOutputStream(IMG);
                            FOS.write(BOS.toByteArray());
                            FOS.flush();
                            FOS.close();

                            UploadFile.put(("Image" + A), IMG);
                        }
                    }
                    catch (Exception e)
                    {
                        // Leave Me Alone
                    }
                }
                else if (SelectType == 2)
                    UploadFile.put("Video", SelectVideo);
                else
                    UploadFile = null;

                final ProgressDialog Progress = new ProgressDialog(getActivity());
                Progress.setMessage(getString(R.string.FragmentMomentWriteUpload));
                Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                Progress.setIndeterminate(false);
                Progress.setCancelable(true);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                AndroidNetworking.upload(URLHandler.GetURL(URLHandler.URL.POST_WRITE))
                .addMultipartParameter("Message", EditTextMessage.getText().toString())
                .addMultipartParameter("Category", String.valueOf(SelectCategory))
                .addMultipartParameter("Type", String.valueOf(SelectType))
                .addMultipartParameter("Link", SelectLink)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addMultipartFile(UploadFile)
                .setTag("FragmentMomentWrite")
                .build().setUploadProgressListener(new UploadProgressListener()
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
                    public void onResponse(String response)
                    {MiscHandler.Debug("MomentWrite: " + response);
                        Progress.cancel();
                        MiscHandler.HideSoftKey(getActivity());
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
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
        TextViewMessageCount.setText(getString(R.string.FragmentMomentWriteMessageCount));
        TextViewMessageCount.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewMessageCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutHeader.addView(TextViewMessageCount);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewLine);

        RelativeLayout.LayoutParams EditTextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextMessageParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        EditTextMessage = new EditText(context);
        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
        EditTextMessage.setId(MiscHandler.GenerateViewID());
        EditTextMessage.setMaxLines(5);
        EditTextMessage.setHint(R.string.FragmentMomentWriteMessage);
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
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                TextViewMessageCount.setText(String.valueOf(150 - s.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        EditTextMessage.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                final Dialog DialogOption = new Dialog(getActivity());
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout Root = new LinearLayout(context);
                Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Root.setBackgroundResource(R.color.White);
                Root.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewPaste = new TextView(context);
                TextViewPaste.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewPaste.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewPaste.setText(getActivity().getString(R.string.FragmentMomentWriteDialogPaste));
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

                Root.addView(TextViewPaste);

                View PasteLine = new View(context);
                PasteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                PasteLine.setBackgroundResource(R.color.Gray1);

                Root.addView(PasteLine);

                TextView TextViewCopy = new TextView(context);
                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCopy.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCopy.setText(getActivity().getString(R.string.FragmentMomentWriteDialogCopy));
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

                        MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteDialogClipboard));
                        DialogOption.dismiss();
                    }
                });

                Root.addView(TextViewCopy);

                DialogOption.setContentView(Root);
                DialogOption.show();
                return false;
            }
        });

        Root.addView(EditTextMessage);

        RelativeLayout.LayoutParams LinearLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LinearLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutBottom = new LinearLayout(context);
        LinearLayoutBottom.setLayoutParams(LinearLayoutBottomParam);
        LinearLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutBottom.setBackgroundResource(R.color.White5);
        LinearLayoutBottom.setId(MiscHandler.GenerateViewID());

        Root.addView(LinearLayoutBottom);

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
                    MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteMaxImage));
                    return;
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.FragmentMomentWriteSelectImage)), 0);
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
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.FragmentMomentWriteSelectVideo)), 1);
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

                LinearLayout RootLink = new LinearLayout(context);
                RootLink.setBackgroundResource(R.color.White);
                RootLink.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewTitleLink2 = new TextView(context);
                TextViewTitleLink2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTitleLink2.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitleLink2.setText(getString(R.string.FragmentMomentWriteShareLink));
                TextViewTitleLink2.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTitleLink2.setTypeface(null, Typeface.BOLD);
                TextViewTitleLink2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                RootLink.addView(TextViewTitleLink2);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewLine.setBackgroundResource(R.color.Gray2);

                RootLink.addView(ViewLine);

                LinearLayout.LayoutParams EditTextURLParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditTextURLParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

                final EditText EditTextURL = new EditText(context);
                EditTextURL.setLayoutParams(EditTextURLParam);
                EditTextURL.setTextColor(ContextCompat.getColor(context, R.color.Black));
                EditTextURL.setMaxLines(1);
                EditTextURL.setBackgroundColor(Color.TRANSPARENT);
                EditTextURL.setInputType(0x00000010);

                RootLink.addView(EditTextURL);

                LinearLayout LinearLayoutButton = new LinearLayout(context);
                LinearLayoutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutButton.setBackgroundResource(R.color.White);
                LinearLayoutButton.setOrientation(LinearLayout.HORIZONTAL);

                RootLink.addView(LinearLayoutButton);

                final TextView TextViewSubmit = new TextView(context);
                TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                TextViewSubmit.setText(getString(R.string.FragmentMomentWriteShareLinkSub));
                TextViewSubmit.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                TextViewSubmit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewSubmit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewSubmit.setEnabled(false);
                TextViewSubmit.setGravity(Gravity.CENTER);

                LinearLayoutButton.addView(TextViewSubmit);

                TextView TextViewCancel = new TextView(context);
                TextViewCancel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                TextViewCancel.setText(getString(R.string.FragmentMomentWriteShareLinkCan));
                TextViewCancel.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCancel.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewCancel.setGravity(Gravity.CENTER);

                LinearLayoutButton.addView(TextViewCancel);

                DialogLink.setContentView(RootLink);
                DialogLink.show();

                if (SelectLink == null || SelectLink.equals(""))
                {
                    EditTextURL.setText(String.valueOf("http://"));
                    EditTextURL.setSelection(EditTextURL.getText().length());
                }
                else
                {
                    TextViewSubmit.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                    TextViewSubmit.setEnabled(true);
                    EditTextURL.setText(SelectLink);
                    EditTextURL.setSelection(EditTextURL.getText().length());
                }

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

                    @Override
                    public void beforeTextChanged(CharSequence s, int y, int u, int i) { }

                    @Override
                    public void afterTextChanged(Editable e) { }
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

                        if (!SelectLink.startsWith("http://"))
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

                        final TextCrawler Request = new TextCrawler(SelectLink, "FragmentMomentWrite", new TextCrawler.TextCrawlerCallBack()
                        {
                            @Override
                            public void OnCompleted(TextCrawler.URLContent Content)
                            {
                                LoadingViewLink.Stop();

                                if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewTitleLink.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    TextViewTitleLink.setLayoutParams(params);
                                }
                                else
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewTitleLink.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    TextViewTitleLink.setLayoutParams(params);
                                }

                                if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewDescriptionLink.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    TextViewDescriptionLink.setLayoutParams(params);
                                }
                                else
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewDescriptionLink.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    TextViewDescriptionLink.setLayoutParams(params);
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

        Root.addView(ViewLine2);

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
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, new FragmentCategory()).addToBackStack("FragmentCategory").commit();
            }
        });


        Root.addView(LinearLayoutCategory);

        RelativeLayout.LayoutParams TextViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCategoryParam.setMargins(MiscHandler.ToDimension(context, 5), 0, 0, 0);

        TextView TextViewCategory = new TextView(context);
        TextViewCategory.setLayoutParams(TextViewCategoryParam);
        TextViewCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewCategory.setText(getString(R.string.FragmentMomentWriteCategory));
        TextViewCategory.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));

        LinearLayoutCategory.addView(TextViewCategory);

        TextViewCategorySelect = new TextView(context);
        TextViewCategorySelect.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewCategorySelect.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewCategorySelect.setText(getString(R.string.FragmentMomentWriteCategorySelect));
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

        Root.addView(RelativeLayoutContent);

        RelativeLayout.LayoutParams RelativeLayoutLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutLinkParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RelativeLayoutLinkParam.setMargins(MiscHandler.ToDimension(context, 10), 0 , MiscHandler.ToDimension(context, 10), 0);

        GradientDrawable Shape = new GradientDrawable();
        Shape.setStroke(MiscHandler.ToDimension(context, 1), ContextCompat.getColor(context, R.color.BlueGray));

        RelativeLayoutLink = new RelativeLayout(context);
        RelativeLayoutLink.setLayoutParams(RelativeLayoutLinkParam);
        RelativeLayoutLink.setVisibility(View.GONE);
        RelativeLayoutLink.setBackground(Shape);

        RelativeLayoutContent.addView(RelativeLayoutLink);

        RelativeLayout.LayoutParams LoadingViewLinkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewLinkParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewLink = new LoadingView(context);
        LoadingViewLink.setLayoutParams(LoadingViewLinkParam);

        RelativeLayoutLink.addView(LoadingViewLink);

        RelativeLayout.LayoutParams TextViewTryLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryLinkParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTryLink = new TextView(context);
        TextViewTryLink.setLayoutParams(TextViewTryLinkParam);
        TextViewTryLink.setVisibility(View.GONE);
        TextViewTryLink.setText(getString(R.string.TryAgain));
        TextViewTryLink.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTryLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutLink.addView(TextViewTryLink);

        RelativeLayout.LayoutParams ImageViewRemoveLinkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25));
        ImageViewRemoveLinkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewRemoveLinkParam.setMargins(0, MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), 0);

        ImageViewRemoveLink = new ImageView(context);
        ImageViewRemoveLink.setLayoutParams(ImageViewRemoveLinkParam);
        ImageViewRemoveLink.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveLink.setImageResource(R.drawable.ic_remove);
        ImageViewRemoveLink.setAlpha(0.75f);
        ImageViewRemoveLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutLink.addView(ImageViewRemoveLink);

        RelativeLayout.LayoutParams TextViewTitleLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleLinkParam.addRule(RelativeLayout.LEFT_OF, ImageViewRemoveLink.getId());
        TextViewTitleLinkParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5), 0, 0);

        TextViewTitleLink = new TextView(context);
        TextViewTitleLink.setLayoutParams(TextViewTitleLinkParam);
        TextViewTitleLink.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTitleLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitleLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutLink.addView(TextViewTitleLink);

        RelativeLayout.LayoutParams TextViewDescriptionLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionLinkParam.addRule(RelativeLayout.LEFT_OF, ImageViewRemoveLink.getId());
        TextViewDescriptionLinkParam.addRule(RelativeLayout.BELOW, TextViewTitleLink.getId());
        TextViewDescriptionLinkParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 3), 0, MiscHandler.ToDimension(context, 5));

        TextViewDescriptionLink = new TextView(context);
        TextViewDescriptionLink.setLayoutParams(TextViewDescriptionLinkParam);
        TextViewDescriptionLink.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewDescriptionLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDescriptionLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutLink.addView(TextViewDescriptionLink);

        RelativeLayout.LayoutParams ImageViewFavLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageViewFavLinkParam.addRule(RelativeLayout.BELOW, TextViewDescriptionLink.getId());
        ImageViewFavLinkParam.setMargins(MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1));

        ImageViewFavLink = new ImageView(context);
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
                bundle.putString("Video", SelectVideo.getAbsolutePath());

                Fragment fragment = new FragmentPreviewVideo();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentPreviewVideo").commit();
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

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentMomentWrite");
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        if (_PermissionHandler != null)
            _PermissionHandler.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (ResultCode != -1)
            return;

        Context context = getActivity();

        try
        {
            Uri ResultUri = Data.getData();

            String URL = null;
            Cursor _Cursor = context.getContentResolver().query(ResultUri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);

            if (_Cursor != null && _Cursor.moveToFirst())
            {
                URL = _Cursor.getString(_Cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                _Cursor.close();
            }

            if (URL == null)
            {
                MiscHandler.Toast(context, getString(R.string.GeneralFileNotFound));
                return;
            }

            _PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, this, new PermissionHandler.PermissionEvent()
            {
                @Override
                public void OnGranted()
                {
                    IsPermissionStorageAllowed = true;
                }

                @Override
                public void OnFailed()
                {
                    IsPermissionStorageAllowed = false;
                }
            });

            if (!IsPermissionStorageAllowed)
            {
                MiscHandler.Toast(context, getString(R.string.GeneralPermissionStorage));
                return;
            }

            if (RequestCode == 0)
            {
                File IMG = new File(URL);
                Bitmap ResizeBitmap;

                if (IMG.length() > 66560)
                {
                    BitmapFactory.Options O = new BitmapFactory.Options();
                    O.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(new FileInputStream(IMG), null, O);

                    int Scale = 1;

                    while ((O.outWidth / Scale / 2) >= 250 && (O.outHeight / Scale / 2) >= 250)
                    {
                        Scale *= 2;
                    }

                    BitmapFactory.Options O2 = new BitmapFactory.Options();
                    O2.inSampleSize = Scale;
                    ResizeBitmap = BitmapFactory.decodeStream(new FileInputStream(IMG), null, O2);

                    Matrix matrix = new Matrix();
                    int Orientation = new ExifInterface(URL).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                    if (Orientation == 6)
                        matrix.postRotate(90);
                    else if (Orientation == 3)
                        matrix.postRotate(180);
                    else if (Orientation == 8)
                        matrix.postRotate(270);

                    ResizeBitmap = Bitmap.createBitmap(ResizeBitmap, 0, 0, ResizeBitmap.getWidth(), ResizeBitmap.getHeight(), matrix, true);
                }
                else
                {
                    ResizeBitmap = BitmapFactory.decodeStream(new FileInputStream(IMG));
                }

                ChangeType(1);
                SelectImage.add(ResizeBitmap);
                ViewPagerAdapterImage.notifyDataSetChanged();
                ViewPagerImage.setCurrentItem(SelectImage.size());
                ViewPagerImage.setVisibility(View.VISIBLE);
            }

            if (RequestCode == 1)
            {
                final MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                Retriever.setDataSource(URL);
                int Time = Math.round(Integer.parseInt(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000);

                if (Time > 180)
                {
                    MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteVideoLength));
                    return;
                }

                SelectVideo = new File(URL);

                if (SelectVideo.length() > 31452800)
                {
                    MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteVideoSize));
                    return;
                }

                if (Build.VERSION.SDK_INT <= 17)
                {
                    if (SelectVideo.length() > 15728640)
                    {
                        MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteVideoSupport));
                        return;
                    }

                    ChangeType(2);
                    ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                    RelativeLayoutVideo.setVisibility(View.VISIBLE);
                    return;
                }

                if (SelectVideo.length() < 3145728)
                {
                    ChangeType(2);
                    ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                    RelativeLayoutVideo.setVisibility(View.VISIBLE);
                    return;
                }

                boolean IsCreated = true;
                File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                if (!Root.exists())
                    IsCreated = Root.mkdirs();

                if (!IsCreated)
                {
                    MiscHandler.Toast(context, getString(R.string.FragmentMomentWriteCantCreateFolder));
                    return;
                }

                SelectVideo = new File(Root, "video." + String.valueOf(System.currentTimeMillis()) + ".mp4");

                final ProgressDialog Progress = new ProgressDialog(getActivity());
                Progress.setMessage(getString(R.string.FragmentMomentWriteVideoCompress));
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

                        try { Frame = Format.getInteger(MediaFormat.KEY_FRAME_RATE); } catch (Exception e) { /* Leave Me Alone  */ }

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

                        try { Sample = Format.getInteger(MediaFormat.KEY_SAMPLE_RATE); } catch (Exception e) { /* Leave Me Alone  */ }
                        try { Channel = Format.getInteger(MediaFormat.KEY_CHANNEL_COUNT); } catch (Exception e) { /* Leave Me Alone  */ }

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
                    }
                });
            }
        }
        catch (Exception e)
        {
            // Leave Me Alone
        }
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

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

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

                        FragmentImagePreview fragment = new FragmentImagePreview();
                        fragment.SetBitmap(SelectImage.get(Position));

                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentImagePreview").commit();
                    }
                }
            });

            Root.addView(ImageViewImage);

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

            Root.addView(ImageViewRemove);

            Container.addView(Root);

            return Root;
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

    public static class FragmentCategory extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setClickable(true);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.White5);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
            ImageViewBack.setImageResource(R.drawable.ic_back_blue);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewNameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            TextView TextViewName = new TextView(context);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewName.setText(getString(R.string.FragmentMomentWriteCategory));
            TextViewName.setTypeface(null, Typeface.BOLD);
            TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewName);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            Root.addView(ViewLine);

            RelativeLayout.LayoutParams RecyclerViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            RecyclerViewCategoryParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            RecyclerView RecyclerViewCategory = new RecyclerView(context);
            RecyclerViewCategory.setLayoutManager(new LinearLayoutManager(context));
            RecyclerViewCategory.setAdapter(new AdapterCategory());
            RecyclerViewCategory.setLayoutParams(RecyclerViewCategoryParam);

            Root.addView(RecyclerViewCategory);

            return Root;
        }

        private class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolderCategory>
        {
            private final List<Struct> CategoryList = new ArrayList<>();

            private final int ID_ROOT = MiscHandler.GenerateViewID();
            private final int ID_ICON = MiscHandler.GenerateViewID();
            private final int ID_NAME = MiscHandler.GenerateViewID();
            private final int ID_LINE = MiscHandler.GenerateViewID();

            AdapterCategory()
            {
                CategoryList.clear();
                CategoryList.add(new Struct(1, "News", R.drawable.ic_category_news));
                CategoryList.add(new Struct(2, "Fun", R.drawable.ic_category_fun));
                CategoryList.add(new Struct(3, "Music", R.drawable.ic_category_music));
                CategoryList.add(new Struct(4, "Sport", R.drawable.ic_category_sport));
                CategoryList.add(new Struct(5, "Fashion", R.drawable.ic_category_fashion));
                CategoryList.add(new Struct(6, "Food", R.drawable.ic_category_food));
                CategoryList.add(new Struct(7, "Technology", R.drawable.ic_category_technology));
                CategoryList.add(new Struct(8, "Art", R.drawable.ic_category_art));
                CategoryList.add(new Struct(9, "Artist", R.drawable.ic_category_artist));
                CategoryList.add(new Struct(10, "Media", R.drawable.ic_category_media));
                CategoryList.add(new Struct(11, "Business", R.drawable.ic_category_business));
                CategoryList.add(new Struct(12, "Economy", R.drawable.ic_category_echonomy));
                CategoryList.add(new Struct(13, "Literature", R.drawable.ic_category_lilterature));
                CategoryList.add(new Struct(14, "Travel", R.drawable.ic_category_travel));
                CategoryList.add(new Struct(15, "Politics", R.drawable.ic_category_politics));
                CategoryList.add(new Struct(16, "Health", R.drawable.ic_category_health));
                CategoryList.add(new Struct(17, "Other", R.drawable.ic_category_other));
            }

            class ViewHolderCategory extends RecyclerView.ViewHolder
            {
                final RelativeLayout RelativeLayoutRoot;
                final ImageView ImageViewIcon;
                final TextView TextViewName;
                final View ViewLine;

                ViewHolderCategory(View view)
                {
                    super(view);
                    RelativeLayoutRoot = (RelativeLayout) view.findViewById(ID_ROOT);
                    ImageViewIcon = (ImageView) view.findViewById(ID_ICON);
                    TextViewName = (TextView) view.findViewById(ID_NAME);
                    ViewLine = view.findViewById(ID_LINE);
                }
            }

            @Override
            public void onBindViewHolder(ViewHolderCategory Holder, int p)
            {
                final int Position = Holder.getAdapterPosition();

                Holder.RelativeLayoutRoot.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        FragmentMomentWrite Parent = (FragmentMomentWrite) getActivity().getSupportFragmentManager().findFragmentByTag("FragmentMomentWrite");
                        Parent.TextViewCategorySelect.setText(CategoryList.get(Position).Name);
                        Parent.SelectCategory = CategoryList.get(Position).ID;

                        getActivity().onBackPressed();
                    }
                });

                Holder.ImageViewIcon.setImageResource(CategoryList.get(Position).Image);
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
                Context context = getActivity();

                RelativeLayout root = new RelativeLayout(context);
                root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                root.setId(ID_ROOT);
                root.setClickable(true);

                ImageView ImageViewIcon = new ImageView(context);
                ImageViewIcon.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 45)));
                ImageViewIcon.setId(ID_ICON);

                root.addView(ImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID_ICON);
                TextViewNameParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);
                TextViewNameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                TextView TextViewName = new TextView(context);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewName.setId(ID_NAME);
                TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                root.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ID_ICON);

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(ID_LINE);

                root.addView(ViewLine);

                return new ViewHolderCategory(root);
            }

            class Struct
            {
                final int ID;
                final String Name;
                final int Image;

                Struct(int id, String name, int image)
                {
                    ID = id;
                    Name = name;
                    Image = image;
                }
            }
        }
    }

    public static class FragmentPreviewVideo extends Fragment
    {
        private RelativeLayout RelativeLayoutHeader;
        private ImageView ImageViewPlay;
        private boolean IsPlaying = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Root = new RelativeLayout(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.Black);
            Root.setClickable(true);

            RelativeLayout.LayoutParams VideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            VideoParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            final VideoView Video = new VideoView(context);
            Video.setLayoutParams(VideoParam);
            Video.setVideoPath(getArguments().getString("Video"));
            Video.seekTo(100);
            Video.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (RelativeLayoutHeader.getVisibility() == View.VISIBLE)
                        RelativeLayoutHeader.setVisibility(View.GONE);
                    else
                        RelativeLayoutHeader.setVisibility(View.VISIBLE);

                    if (ImageViewPlay.getVisibility() == View.VISIBLE)
                        ImageViewPlay.setVisibility(View.GONE);
                    else
                        ImageViewPlay.setVisibility(View.VISIBLE);

                    if (IsPlaying)
                    {
                        IsPlaying = false;
                        Video.pause();
                    }
                    else
                    {
                        IsPlaying = true;
                        Video.start();
                    }

                    return false;
                }
            });

            Root.addView(Video);

            RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundColor(Color.parseColor("#3f000000"));

            Root.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            TextView TextViewTitle = new TextView(context);
            TextViewTitle.setLayoutParams(TextViewTitleParam);
            TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewTitle.setText(getString(R.string.FragmentMomentWriteVideoPreview));
            TextViewTitle.setTypeface(null, Typeface.BOLD);
            TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewTitle);

            RelativeLayout.LayoutParams ImageViewPlayParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 65), MiscHandler.ToDimension(context, 65));
            ImageViewPlayParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            ImageViewPlay = new ImageView(context);
            ImageViewPlay.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewPlay.setLayoutParams(ImageViewPlayParam);
            ImageViewPlay.setImageResource(R.drawable.ic_play);
            ImageViewPlay.setId(MiscHandler.GenerateViewID());

            Root.addView(ImageViewPlay);

            return Root;
        }
    }
}
