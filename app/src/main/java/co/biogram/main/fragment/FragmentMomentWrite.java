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
import android.support.annotation.IdRes;
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
import android.widget.Button;
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
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.TextCrawler;
import co.biogram.media.MediaTransCoder;

public class FragmentMomentWrite extends Fragment
{
    private int LastDifferenceHeight = 0;

    private EditText EditTextMessage;

    private TextView TextViewCategory;
    private int SelectCategory = 0;

    private int SelectType = 0;

    private ImageView ImageViewImage;
    private ImageView ImageViewVideo;
    private ImageView ImageViewLink;

    private ViewPager ViewPagerImage;
    private ViewPagerAdapter ViewPagerAdapterImage;

    private File SelectVideo;
    private RelativeLayout RelativeLayoutVideo;
    private ImageView ImageViewThumb;

    private String SelectLink = "";

    private boolean IsPermissionStorageAllowed = false;
    private PermissionHandler _PermissionHandler;

    private List<Bitmap> SelectImage = new ArrayList<>();

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

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
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
        ButtonSendParam.setMargins(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        ImageView ImageViewSend = new ImageView(context);
        ImageViewSend.setLayoutParams(ButtonSendParam);
        ImageViewSend.setImageResource(R.drawable.ic_send_blue);
        ImageViewSend.setId(MiscHandler.GenerateViewID());
        ImageViewSend.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));

        RelativeLayoutHeader.addView(ImageViewSend);

        RelativeLayout.LayoutParams TextViewMessageCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageCountParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewMessageCountParam.addRule(RelativeLayout.LEFT_OF, ImageViewSend.getId());
        TextViewMessageCountParam.setMargins(0, 0, MiscHandler.ToDimension(context, 15), 0);

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

        final EditText EditTextMessage = new EditText(context);
        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
        EditTextMessage.setId(MiscHandler.GenerateViewID());
        EditTextMessage.setMaxLines(5);
        EditTextMessage.setHint(R.string.FragmentMomentWriteMessage);
        EditTextMessage.setBackground(null);
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

        ImageView ImageViewImage = new ImageView(context);
        ImageViewImage.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 56), 1.0f));
        ImageViewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewImage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewImage.setImageResource(R.drawable.ic_camera);

        LinearLayoutBottom.addView(ImageViewImage);

        ImageView ImageViewVideo = new ImageView(context);
        ImageViewVideo.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 56), 1.0f));
        ImageViewVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewVideo.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewVideo.setImageResource(R.drawable.ic_video);

        LinearLayoutBottom.addView(ImageViewVideo);

        ImageView ImageViewLink = new ImageView(context);
        ImageViewLink.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 56), 1.0f));
        ImageViewLink.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewLink.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewLink.setImageResource(R.drawable.ic_link);

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
        LinearLayoutCategoryParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        LinearLayout LinearLayoutCategory = new LinearLayout(context);
        LinearLayoutCategory.setLayoutParams(LinearLayoutCategoryParam);
        LinearLayoutCategory.setBackgroundResource(R.color.White);
        LinearLayoutCategory.setId(MiscHandler.GenerateViewID());
        LinearLayoutCategory.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCategory.setGravity(Gravity.CENTER_VERTICAL);

        Root.addView(LinearLayoutCategory);

        RelativeLayout.LayoutParams TextViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCategoryParam.setMargins(MiscHandler.ToDimension(context, 5), 0, 0, 0);

        TextView TextViewCategory = new TextView(context);
        TextViewCategory.setLayoutParams(TextViewCategoryParam);
        TextViewCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewCategory.setText(getString(R.string.FragmentMomentWriteCategory));
        TextViewCategory.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));

        LinearLayoutCategory.addView(TextViewCategory);

        TextView TextViewCategorySelect = new TextView(context);
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











        return Root;

        /*


        TextViewCategory = (TextView) RootView.findViewById(R.id.TextViewCategory);
        RootView.findViewById(R.id.LinearLayoutCategory).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditTextMessage.clearFocus();
                MiscHandler.HideSoftKey(getActivity());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, new FragmentCategory()).addToBackStack("FragmentCategory").commit();
            }
        });

        ImageViewImage = (ImageView) RootView.findViewById(R.id.ImageViewImage);
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

        ViewPagerImage = (ViewPager) RootView.findViewById(R.id.ViewPagerImage);
        ViewPagerAdapterImage = new ViewPagerAdapter();
        ViewPagerImage.setAdapter(ViewPagerAdapterImage);

        ImageViewVideo = (ImageView) RootView.findViewById(R.id.ImageViewVideo);
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

        RelativeLayoutVideo = (RelativeLayout) RootView.findViewById(R.id.RelativeLayoutVideo);
        ImageViewThumb = (ImageView) RootView.findViewById(R.id.ImageViewThumb);
        ImageViewThumb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MiscHandler.HideSoftKey(getActivity());

                Bundle bundle = new Bundle();
                bundle.putString("Video", SelectVideo.getAbsolutePath());

                Fragment fragment = new FragmentPreviewVideo();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentPreviewVideo").commit();
            }
        });

        RootView.findViewById(R.id.ImageViewRemove).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(0);
                SelectVideo = null;
                ImageViewThumb.setImageResource(android.R.color.transparent);
                RelativeLayoutVideo.setVisibility(View.GONE);
            }
        });

        ImageViewLink = (ImageView) RootView.findViewById(R.id.ImageViewLink);
        ImageViewLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog DialogLink = new Dialog(getActivity());
                DialogLink.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLink.setCancelable(false);

                LinearLayout Main = new LinearLayout(context);
                Main.setBackgroundColor(ContextCompat.getColor(context, R.color.White));
                Main.setOrientation(LinearLayout.VERTICAL);

                TextView Title = new TextView(context);
                Title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Title.setTextColor(ContextCompat.getColor(context, R.color.Black));
                Title.setText(getString(R.string.FragmentMomentWriteShareLink));
                Title.setPadding(MiscHandler.ToDimension(15), MiscHandler.ToDimension(15), MiscHandler.ToDimension(15), MiscHandler.ToDimension(15));
                Title.setTypeface(null, Typeface.BOLD);
                Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                View Line = new View(context);
                Line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(1)));
                Line.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray2));

                LinearLayout.LayoutParams URLParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                URLParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

                final EditText URL = new EditText(context);
                URL.setLayoutParams(URLParam);
                URL.setTextColor(ContextCompat.getColor(context, R.color.Black));
                URL.setMaxLines(1);
                URL.setBackgroundColor(Color.TRANSPARENT);
                URL.setInputType(0x00000010);

                LinearLayout ButtonLinear = new LinearLayout(context);
                ButtonLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ButtonLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.White));
                ButtonLinear.setOrientation(LinearLayout.HORIZONTAL);

                final TextView Submit = new TextView(context);
                Submit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                Submit.setText(getString(R.string.FragmentMomentWriteShareLinkSub));
                Submit.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                Submit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                Submit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                Submit.setEnabled(false);
                Submit.setGravity(Gravity.CENTER);

                TextView Cancel = new TextView(context);
                Cancel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                Cancel.setText(getString(R.string.FragmentMomentWriteShareLinkCan));
                Cancel.setTextColor(ContextCompat.getColor(context, R.color.Black));
                Cancel.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                Cancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                Cancel.setGravity(Gravity.CENTER);

                Main.addView(Title);
                Main.addView(Line);
                Main.addView(URL);
                ButtonLinear.addView(Cancel);
                ButtonLinear.addView(Submit);
                Main.addView(ButtonLinear);

                DialogLink.setContentView(Main);
                DialogLink.show();

                if (SelectLink == null || SelectLink.equals(""))
                {
                    URL.setText(String.valueOf("http://"));
                    URL.setSelection(URL.getText().length());
                }
                else
                {
                    Submit.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                    Submit.setEnabled(true);
                    URL.setText(SelectLink);
                    URL.setSelection(URL.getText().length());
                }

                URL.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void onTextChanged(CharSequence s, int y, int u, int i)
                    {
                        if (s.length() > 5 && Patterns.WEB_URL.matcher(s.toString()).matches())
                        {
                            Submit.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                            Submit.setEnabled(true);
                        }
                        else
                        {
                            Submit.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                            Submit.setEnabled(false);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int y, int u, int i) { }

                    @Override
                    public void afterTextChanged(Editable e) { }
                });

                if (DialogLink.getWindow() != null)
                    DialogLink.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                Cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ChangeType(0);
                        SelectLink = "";
                        DialogLink.dismiss();
                    }
                });

                Submit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ChangeType(3);
                        SelectLink = URL.getText().toString();

                        if (!SelectLink.startsWith("http://"))
                            SelectLink = "http://" + SelectLink;

                        DialogLink.dismiss();

                        GradientDrawable Shape = new GradientDrawable();
                        Shape.setStroke(MiscHandler.ToDimension(1), ContextCompat.getColor(context, R.color.BlueGray));

                        final RelativeLayout RelativeLayoutLink = (RelativeLayout) RootView.findViewById(R.id.RelativeLayoutLink);
                        RelativeLayoutLink.setBackground(Shape);

                        final LoadingView LoadingViewLink = (LoadingView) RootView.findViewById(R.id.LoadingViewLink);
                        final ImageView ImageViewFav = (ImageView) RootView.findViewById(R.id.ImageViewFav);
                        final TextView TextViewTitle = (TextView) RootView.findViewById(R.id.TextViewTitle);
                        final TextView TextViewDescription = (TextView) RootView.findViewById(R.id.TextViewDescription);
                        final TextView TextViewTry = (TextView) RootView.findViewById(R.id.TextViewTry);

                        RelativeLayoutLink.setVisibility(View.VISIBLE);
                        LoadingViewLink.Start();

                        TextViewTitle.setText("");
                        TextViewDescription.setText("");
                        ImageViewFav.setImageResource(android.R.color.transparent);

                        RootView.findViewById(R.id.ImageViewRemoveLink).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                ChangeType(0);
                                SelectLink = "";
                                TextViewTitle.setText("");
                                LoadingViewLink.Stop();
                                TextViewDescription.setText("");
                                ImageViewFav.setImageResource(android.R.color.transparent);
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
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewTitle.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    TextViewTitle.setLayoutParams(params);
                                }
                                else
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewTitle.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    TextViewTitle.setLayoutParams(params);
                                }

                                if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewDescription.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    TextViewDescription.setLayoutParams(params);
                                }
                                else
                                {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewDescription.getLayoutParams();
                                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                    TextViewDescription.setLayoutParams(params);
                                }

                                TextViewTitle.setText(Content.Title);
                                TextViewDescription.setText(Content.Description);

                                RequestHandler.Core().LoadImage(ImageViewFav, Content.Image, "FragmentMomentWrite", true);
                            }

                            @Override
                            public void OnFailed()
                            {
                                TextViewTitle.setText("");
                                LoadingViewLink.Stop();
                                TextViewDescription.setText("");
                                ImageViewFav.setImageResource(android.R.color.transparent);
                                TextViewTry.setVisibility(View.VISIBLE);
                            }
                        });

                        TextViewTry.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Request.Start();
                                LoadingViewLink.Start();
                                TextViewTry.setVisibility(View.GONE);
                            }
                        });

                        Request.Start();
                    }
                });
            }
        });

        RootView.findViewById(R.id.ButtonSend).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SelectCategory == 0)
                {
                    MiscHandler.Toast(context, "Please Choice a Category"); // TODO String Kon
                    return;
                }

                if (EditTextMessage.getText().length() <= 19 && SelectType == 0)
                {
                    MiscHandler.Toast(context, "Please, Add More Content"); // TODO String Kon
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
                .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                .addMultipartFile(UploadFile)
                .setTag("FragmentMomentWrite")
                .build().setUploadProgressListener(new UploadProgressListener()
                {
                    @Override
                    public void onProgress(long Uploaded, long Total)
                    {
                        Progress.setProgress((int) (100 * Uploaded / Total));
                    }
                })
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Progress.cancel();
                        MiscHandler.HideSoftKey(getActivity());
                        getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentMomentWrite.this).commit();
                    }
                    @Override
                    public void onError(ANError error)
                    {
                        Progress.cancel();
                        MiscHandler.Toast(getActivity(), getString(R.string.GeneralCheckInternet));
                    }
                });
            }
        });

        return RootView;*/
    }

    @Override
    public void onPause()
    {
        super.onPause();
        RequestHandler.Core().Cancel("FragmentMomentWrite");
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

        try
        {
            Uri ResultUri = Data.getData();

            String URL = null;
            Cursor _Cursor = getActivity().getContentResolver().query(ResultUri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);

            if (_Cursor != null && _Cursor.moveToFirst())
            {
                URL = _Cursor.getString(_Cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                _Cursor.close();
            }

            if (URL == null)
            {
                MiscHandler.Toast(getActivity(), getString(R.string.GeneralFileNotFound));
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
                MiscHandler.Toast(getActivity(), getString(R.string.GeneralPermissionStorage));
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
                    MiscHandler.Toast(getActivity(), getString(R.string.FragmentMomentWriteVideoLength));
                    return;
                }

                SelectVideo = new File(URL);

                if (SelectVideo.length() > 31452800)
                {
                    MiscHandler.Toast(getActivity(), getString(R.string.FragmentMomentWriteVideoSize));
                    return;
                }

                if (Build.VERSION.SDK_INT <= 17)
                {
                    if (SelectVideo.length() > 15728640)
                    {
                        MiscHandler.Toast(getActivity(), getString(R.string.FragmentMomentWriteVideoSupport));
                        return;
                    }

                    ChangeType(2);
                    ImageViewThumb.setImageBitmap(Retriever.getFrameAtTime(100));
                    RelativeLayoutVideo.setVisibility(View.VISIBLE);
                    return;
                }

                if (SelectVideo.length() < 3145728)
                {
                    ChangeType(2);
                    ImageViewThumb.setImageBitmap(Retriever.getFrameAtTime(100));
                    RelativeLayoutVideo.setVisibility(View.VISIBLE);
                    return;
                }

                boolean IsCreated = true;
                File Root = new File(Environment.getExternalStorageDirectory(), "BioGram");

                if (!Root.exists())
                    IsCreated = Root.mkdirs();

                if (!IsCreated)
                {
                    MiscHandler.Toast(getActivity(), getString(R.string.FragmentMomentWriteCantCreateFolder));
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
                                ImageViewThumb.setImageBitmap(Retriever.getFrameAtTime(100));
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
            Context context = getActivity();

            RelativeLayout Main = new RelativeLayout(context);
            Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            ImageView Image = new ImageView(context);
            RelativeLayout.LayoutParams ImageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageParam.setMargins(MiscHandler.ToDimension(10), 0, MiscHandler.ToDimension(10), 0);
            Image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Image.setLayoutParams(ImageParam);
            Image.setImageBitmap(SelectImage.get(Position));
            Image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SelectImage.size() > 0)
                    {
                        MiscHandler.HideSoftKey(getActivity());

                        FragmentImagePreview fragment = new FragmentImagePreview();
                        fragment.SetBitmap(SelectImage.get(Position));

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentImagePreview").commit();
                    }
                }
            });

            Main.addView(Image);

            ImageView Remove = new ImageView(context);
            RelativeLayout.LayoutParams RemoveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(30), MiscHandler.ToDimension(30));
            RemoveParam.setMargins(0, MiscHandler.ToDimension(5), MiscHandler.ToDimension(20), 0);
            RemoveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            Remove.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Remove.setLayoutParams(RemoveParam);
            Remove.setAlpha(0.75f);
            Remove.setImageResource(R.drawable.ic_remove);
            Remove.setOnClickListener(new View.OnClickListener()
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

            Main.addView(Remove);
            Container.addView(Main);

            return Main;
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

    public static class FragmentPreviewVideo extends Fragment
    {
        private ImageView Play;
        private RelativeLayout Header;
        private boolean IsPlaying = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Main = new RelativeLayout(context);
            Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Main.setBackgroundColor(ContextCompat.getColor(context, R.color.Black));
            Main.setClickable(true);

            RelativeLayout.LayoutParams VideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            VideoParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            final VideoView Video = new VideoView(context);
            Video.setLayoutParams(VideoParam);
            Video.setVideoPath(getArguments().getString("Video"));
            Video.seekTo(100);
            Video.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (Header.getVisibility() == View.VISIBLE)
                        Header.setVisibility(View.GONE);
                    else
                        Header.setVisibility(View.VISIBLE);

                    if (Play.getVisibility() == View.VISIBLE)
                        Play.setVisibility(View.GONE);
                    else
                        Play.setVisibility(View.VISIBLE);

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

            Main.addView(Video);

            Header = new RelativeLayout(context);
            Header.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(56)));
            Header.setBackgroundColor(Color.parseColor("#3f000000"));

            Main.addView(Header);

            ImageView Back = new ImageView(context);
            Back.setPadding(MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12));
            Back.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Back.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(56), MiscHandler.ToDimension(56)));
            Back.setImageResource(R.drawable.ic_back_white);
            Back.setId(MiscHandler.GenerateViewID());
            Back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentPreviewVideo.this).commit();
                }
            });

            Header.addView(Back);

            RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            NameParam.addRule(RelativeLayout.RIGHT_OF, Back.getId());
            NameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            TextView Title = new TextView(context);
            Title.setLayoutParams(NameParam);
            Title.setTextColor(ContextCompat.getColor(context, R.color.White));
            Title.setText(getString(R.string.FragmentMomentWriteVideoPreview));
            Title.setTypeface(null, Typeface.BOLD);
            Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            Header.addView(Title);

            RelativeLayout.LayoutParams PlayParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(65), MiscHandler.ToDimension(65));
            PlayParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            Play = new ImageView(context);
            Play.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Play.setLayoutParams(PlayParam);
            Play.setImageResource(R.drawable.ic_play);
            Play.setId(MiscHandler.GenerateViewID());

            Main.addView(Play);

            return Main;
        }
    }

    public static  class FragmentCategory extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Context context = getActivity();

            RelativeLayout Main = new RelativeLayout(context);
            Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Main.setBackgroundColor(ContextCompat.getColor(context, R.color.White));
            Main.setClickable(true);

            RelativeLayout Header = new RelativeLayout(context);
            Header.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(56)));
            Header.setId(MiscHandler.GenerateViewID());

            Main.addView(Header);

            ImageView Back = new ImageView(context);
            Back.setPadding(MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12));
            Back.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Back.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(56), MiscHandler.ToDimension(56)));
            Back.setImageResource(R.drawable.ic_back_blue);
            Back.setId(MiscHandler.GenerateViewID());
            Back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentCategory.this).commit();
                }
            });

            Header.addView(Back);

            RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            NameParam.addRule(RelativeLayout.RIGHT_OF, Back.getId());
            NameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            TextView Name = new TextView(context);
            Name.setLayoutParams(NameParam);
            Name.setTextColor(ContextCompat.getColor(context, R.color.Black));
            Name.setText(getString(R.string.FragmentMomentWriteCategory));
            Name.setTypeface(null, Typeface.BOLD);
            Name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            Header.addView(Name);

            RelativeLayout.LayoutParams LineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(1));
            LineParam.addRule(RelativeLayout.BELOW, Header.getId());

            View Line = new View(context);
            Line.setLayoutParams(LineParam);
            Line.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray2));
            Line.setId(MiscHandler.GenerateViewID());

            Main.addView(Line);

            RelativeLayout.LayoutParams RVCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            RVCategoryParam.addRule(RelativeLayout.BELOW, Line.getId());

            RecyclerView RVCategory = new RecyclerView(context);
            RVCategory.setLayoutManager(new LinearLayoutManager(context));
            RVCategory.setAdapter(new AdapterCategory());
            RVCategory.setLayoutParams(RVCategoryParam);

            Main.addView(RVCategory);

            return Main;
        }

        private class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolderCategory>
        {
            private List<CategoryStruct> CategoryList = new ArrayList<>();

            private @IdRes int ID_MAIN;
            private @IdRes int ID_ICON;
            private @IdRes int ID_NAME;
            private @IdRes int ID_LINE;

            AdapterCategory()
            {
                ID_MAIN = MiscHandler.GenerateViewID();
                ID_ICON = MiscHandler.GenerateViewID();
                ID_NAME = MiscHandler.GenerateViewID();
                ID_LINE = MiscHandler.GenerateViewID();

                CategoryList.clear();
                CategoryList.add(new CategoryStruct(1, "News", R.drawable.ic_category_news));
                CategoryList.add(new CategoryStruct(2, "Fun", R.drawable.ic_category_fun));
                CategoryList.add(new CategoryStruct(3, "Music", R.drawable.ic_category_music));
                CategoryList.add(new CategoryStruct(4, "Sport", R.drawable.ic_category_sport));
                CategoryList.add(new CategoryStruct(5, "Fashion", R.drawable.ic_category_fashion));
                CategoryList.add(new CategoryStruct(6, "Food", R.drawable.ic_category_food));
                CategoryList.add(new CategoryStruct(7, "Technology", R.drawable.ic_category_technology));
                CategoryList.add(new CategoryStruct(8, "Art", R.drawable.ic_category_art));
                CategoryList.add(new CategoryStruct(9, "Artist", R.drawable.ic_category_artist));
                CategoryList.add(new CategoryStruct(10, "Media", R.drawable.ic_category_media));
                CategoryList.add(new CategoryStruct(11, "Business", R.drawable.ic_category_business));
                CategoryList.add(new CategoryStruct(12, "Economy", R.drawable.ic_category_echonomy));
                CategoryList.add(new CategoryStruct(13, "Literature", R.drawable.ic_category_lilterature));
                CategoryList.add(new CategoryStruct(14, "Travel", R.drawable.ic_category_travel));
                CategoryList.add(new CategoryStruct(15, "Politics", R.drawable.ic_category_politics));
                CategoryList.add(new CategoryStruct(16, "Health", R.drawable.ic_category_health));
                CategoryList.add(new CategoryStruct(17, "Other", R.drawable.ic_category_other));
            }

            class ViewHolderCategory extends RecyclerView.ViewHolder
            {
                RelativeLayout Main;
                ImageView Icon;
                TextView Name;
                View Line;

                ViewHolderCategory(View view)
                {
                    super(view);
                    Main = (RelativeLayout) view.findViewById(ID_MAIN);
                    Icon = (ImageView) view.findViewById(ID_ICON);
                    Name = (TextView) view.findViewById(ID_NAME);
                    Line = view.findViewById(ID_LINE);
                }
            }

            @Override
            public void onBindViewHolder(ViewHolderCategory Holder, int p)
            {
                final int Position = Holder.getAdapterPosition();

                Holder.Main.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        FragmentMomentWrite Parent = (FragmentMomentWrite) getActivity().getSupportFragmentManager().findFragmentByTag("FragmentMomentWrite");
                        Parent.TextViewCategory.setText(CategoryList.get(Position).Name);
                        Parent.SelectCategory = CategoryList.get(Position).ID;

                        getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentCategory.this).commit();
                    }
                });

                Holder.Icon.setImageResource(CategoryList.get(Position).Image);
                Holder.Name.setText(CategoryList.get(Position).Name);

                if (Position == CategoryList.size() - 1)
                    Holder.Line.setVisibility(View.GONE);
                else
                    Holder.Line.setVisibility(View.VISIBLE);
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

                RelativeLayout Main = new RelativeLayout(context);
                Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                Main.setId(ID_MAIN);
                Main.setClickable(true);

                ImageView Icon = new ImageView(context);
                Icon.setPadding(MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10));
                Icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Icon.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(45), MiscHandler.ToDimension(45)));
                Icon.setId(ID_ICON);

                Main.addView(Icon);

                RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                NameParam.addRule(RelativeLayout.RIGHT_OF, ID_ICON);
                NameParam.setMargins(MiscHandler.ToDimension(15), 0, 0, 0);
                NameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                TextView Name = new TextView(context);
                Name.setLayoutParams(NameParam);
                Name.setTextColor(ContextCompat.getColor(context, R.color.Black));
                Name.setId(ID_NAME);
                Name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                Main.addView(Name);

                RelativeLayout.LayoutParams PositionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(1));
                PositionParam.addRule(RelativeLayout.BELOW, ID_ICON);

                View Line = new View(context);
                Line.setLayoutParams(PositionParam);
                Line.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray));
                Line.setId(ID_LINE);

                Main.addView(Line);

                return new ViewHolderCategory(Main);
            }

            class CategoryStruct
            {
                int ID;
                String Name;
                int Image;

                CategoryStruct(int id, String name, int image)
                {
                    ID = id;
                    Name = name;
                    Image = image;
                }
            }
        }
    }
}
