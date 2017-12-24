package co.biogram.main.ui.social;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.OnClickRecyclerView;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.general.ImagePreviewUI;
import co.biogram.main.ui.general.VideoPreviewUI;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.ProgressDialog;
import co.biogram.main.ui.view.TextView;
import co.biogram.media.MediaTransCoder;

class WriteUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;
    private ImageView ImageViewImage;
    private ImageView ImageViewVideo;
    private ImageView ImageViewVote;
    private ImageView ImageViewFile;
    private ViewPagerAdapter ViewPagerAdapterImage;
    private ViewPager ViewPagerImage;

    private final List<String> SelectImage = new ArrayList<>();
    private int SelectCategory = 0;
    private File SelectFile;
    private File SelectVideo;
    private int SelectType = 0;
    private int IsWorld = 0;
    private int VoteTime = 0;

    @Override
    public void OnCreate()
    {
        final TextView TextViewCategorySelect = new TextView(GetActivity(), 16, false);
        final TextView TextViewCount = new TextView(GetActivity(), 14, false);
        final EditText EditTextMessage = new EditText(GetActivity());
        final ImageView ImageViewThumbVideo = new ImageView(GetActivity());
        final RelativeLayout RelativeLayoutVideo = new RelativeLayout(GetActivity());
        final TextView TextViewSizeVideo = new TextView(GetActivity(), 12, false);
        final TextView TextViewLengthVideo = new TextView(GetActivity(), 12, false);
        final RelativeLayout RelativeLayoutFile = new RelativeLayout(GetActivity());
        final TextView TextViewFileName = new TextView(GetActivity(), 14, false);
        final TextView TextViewFileDetail = new TextView(GetActivity(), 12, false);
        final ScrollView ScrollViewVote = new ScrollView(GetActivity());
        final ImageView ImageViewRemoveVote = new ImageView(GetActivity());

        RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        LayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            int HeightDifference = 0;

            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (HeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(HeightDifference)));
                    HeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark(GetActivity()) ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.ic_back_blue_rtl : R.drawable.ic_back_blue);
        ImageViewBack.setId(Misc.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewTitle.setPadding(0, Misc.ToDP(GetActivity(), 6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.WriteUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewWorldParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewWorldParam.addRule(Misc.Align("L"));

        final ImageView ImageViewWorld = new ImageView(GetActivity());
        ImageViewWorld.setLayoutParams(ImageViewWorldParam);
        ImageViewWorld.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewWorld.setPadding(Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12));
        ImageViewWorld.setImageResource(R.drawable.i_global_gray);
        ImageViewWorld.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsWorld == 0)
                {
                    IsWorld = 1;
                    ImageViewWorld.setImageResource(R.drawable.i_global_blue);
                }
                else
                {
                    IsWorld = 0;
                    ImageViewWorld.setImageResource(R.drawable.i_global_gray);
                }
            }
        });

        RelativeLayoutHeader.addView(ImageViewWorld);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark(GetActivity()) ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams EditTextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextMessageParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        EditTextMessage.setId(Misc.GenerateViewID());
        EditTextMessage.setMaxLines(5);
        EditTextMessage.setHint(R.string.WriteUIMessage);
        EditTextMessage.setBackground(null);
        EditTextMessage.requestFocus();
        EditTextMessage.setScroller(new Scroller(GetActivity()));
        EditTextMessage.setVerticalScrollBarEnabled(true);
        EditTextMessage.setMovementMethod(new ScrollingMovementMethod());
        EditTextMessage.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        EditTextMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(300) });
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
                TextViewCount.setText(String.valueOf(300 - s.length()));
            }
        });
        EditTextMessage.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                final Dialog DialogOption = new Dialog(GetActivity());
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(GetActivity());
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewDelete = new TextView(GetActivity(), 14, false);
                TextViewDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewDelete.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewDelete.setText(GetActivity().getString(R.string.WriteUIDelete));
                TextViewDelete.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
                TextViewDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        EditTextMessage.setText("");
                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewDelete);

                View DeleteLine = new View(GetActivity());
                DeleteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1)));
                DeleteLine.setBackgroundResource(R.color.Gray2);

                LinearLayoutMain.addView(DeleteLine);

                TextView TextViewPaste = new TextView(GetActivity(), 14, false);
                TextViewPaste.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewPaste.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewPaste.setText(GetActivity().getString(R.string.WriteUIPaste));
                TextViewPaste.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
                TextViewPaste.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) GetActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                        if (clipboard != null && clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType("text/plain"))
                        {
                            ClipData.Item ClipItem = clipboard.getPrimaryClip().getItemAt(0);
                            String Message = EditTextMessage.getText().toString() + ClipItem.getText().toString();
                            EditTextMessage.setText(Message);
                        }

                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewPaste);

                View PasteLine = new View(GetActivity());
                PasteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1)));
                PasteLine.setBackgroundResource(R.color.Gray2);

                LinearLayoutMain.addView(PasteLine);

                TextView TextViewCopy = new TextView(GetActivity(), 14, false);
                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCopy.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewCopy.setText(GetActivity().getString(R.string.WriteUICopy));
                TextViewCopy.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
                TextViewCopy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) GetActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("WriteMessage", EditTextMessage.getText().toString());

                        if (clipboard != null)
                            clipboard.setPrimaryClip(clip);

                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.WriteUIClipboard));
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

        RelativeLayout.LayoutParams LinearLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
        LinearLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutBottom = new LinearLayout(GetActivity());
        LinearLayoutBottom.setLayoutParams(LinearLayoutBottomParam);
        LinearLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutBottom.setBackgroundResource(R.color.ActionBarWhite);
        LinearLayoutBottom.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(LinearLayoutBottom);

        ImageViewImage = new ImageView(GetActivity());
        ImageViewImage.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(GetActivity(), 56), 1.0f));
        ImageViewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewImage.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        ImageViewImage.setImageResource(R.drawable.ic_camera);
        ImageViewImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (SelectImage.size() >= 3)
                {
                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.WriteUIMaximumImage));
                    return;
                }

                if (Misc.HasPermission(GetActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    GetActivity().GetManager().OpenView(new GalleryViewUI(3, 1, new GalleryViewUI.GalleryListener()
                    {
                        List<String> IamgeURL = new ArrayList<>();

                        @Override
                        public void OnSelection(String URL)
                        {
                            IamgeURL.add(URL);
                        }

                        @Override
                        public void OnRemove(String URL)
                        {
                            IamgeURL.remove(URL);
                        }

                        @Override
                        public void OnSave()
                        {
                            if (IamgeURL.size() <= 0)
                                return;

                            for (String url : IamgeURL)
                                if (SelectImage.size() < 3)
                                    SelectImage.add(url);

                            ChangeType(1);
                            ViewPagerImage.setVisibility(View.VISIBLE);
                            ViewPagerAdapterImage.notifyDataSetChanged();
                        }
                    }), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(GetActivity());
                PermissionDialogGallery.SetContentView(R.drawable.ic_permission_storage, GetActivity().getString(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(GetActivity(), GetActivity().getString(R.string.PermissionStorage));
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnGranted()
                            {
                                GetActivity().GetManager().OpenView(new GalleryViewUI(3, 1, new GalleryViewUI.GalleryListener()
                                {
                                    List<String> IamgeURL = new ArrayList<>();

                                    @Override
                                    public void OnSelection(String URL)
                                    {
                                        IamgeURL.add(URL);
                                    }

                                    @Override
                                    public void OnRemove(String URL)
                                    {
                                        IamgeURL.remove(URL);
                                    }

                                    @Override
                                    public void OnSave()
                                    {
                                        if (IamgeURL.size() <= 0)
                                            return;

                                        ChangeType(1);
                                        SelectImage.addAll(IamgeURL);
                                        ViewPagerImage.setVisibility(View.VISIBLE);
                                        ViewPagerAdapterImage.notifyDataSetChanged();
                                    }
                                }), R.id.ContainerFull, "GalleryViewUI");
                            }

                            @Override
                            public void OnDenied()
                            {
                                Misc.Toast(GetActivity(), GetActivity().getString(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        LinearLayoutBottom.addView(ImageViewImage);

        ImageViewVideo = new ImageView(GetActivity());
        ImageViewVideo.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(GetActivity(), 56), 1.0f));
        ImageViewVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewVideo.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        ImageViewVideo.setImageResource(R.drawable.ic_comment);
        ImageViewVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Misc.HasPermission(GetActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, 2, new GalleryViewUI.GalleryListener()
                    {
                        String VideoURL;

                        @Override
                        public void OnSelection(String URL)
                        {
                            VideoURL = URL;
                        }

                        @Override
                        public void OnRemove(String URL)
                        {
                            VideoURL = "";
                        }

                        @Override
                        public void OnSave()
                        {
                            if (!VideoURL.equals(""))
                            {
                                MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                                Retriever.setDataSource(VideoURL);
                                int Time = Math.round(Integer.parseInt(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000);

                                if (Time > 240)
                                {
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.WriteUIVideoLength));
                                    return;
                                }

                                ChangeType(2);
                                SelectVideo = new File(VideoURL);
                                ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                                RelativeLayoutVideo.setVisibility(View.VISIBLE);

                                double Size = (double) SelectVideo.length() / 1048576.0;

                                TextViewSizeVideo.setText((new DecimalFormat("#.##").format(Size) + " " + GetActivity().getString(R.string.WriteUIMB)));
                                TextViewLengthVideo.setText((String.valueOf(Time) + " " + GetActivity().getString(R.string.WriteUISeconds)));
                            }
                        }
                    }), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(GetActivity());
                PermissionDialogGallery.SetContentView(R.drawable.ic_permission_storage, GetActivity().getString(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(GetActivity(), GetActivity().getString(R.string.PermissionStorage));
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnGranted()
                            {
                                GetActivity().GetManager().OpenView(new GalleryViewUI(1, 2, new GalleryViewUI.GalleryListener()
                                {
                                    String VideoURL;

                                    @Override
                                    public void OnSelection(String URL)
                                    {
                                        VideoURL = URL;
                                    }

                                    @Override
                                    public void OnRemove(String URL)
                                    {
                                        VideoURL = "";
                                    }

                                    @Override
                                    public void OnSave()
                                    {
                                        if (!VideoURL.equals(""))
                                        {
                                            MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                                            Retriever.setDataSource(VideoURL);
                                            int Time = Math.round(Integer.parseInt(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000);

                                            if (Time > 240)
                                            {
                                                Misc.Toast(GetActivity(), GetActivity().getString(R.string.WriteUIVideoLength));
                                                return;
                                            }

                                            ChangeType(2);
                                            SelectVideo = new File(VideoURL);
                                            ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                                            RelativeLayoutVideo.setVisibility(View.VISIBLE);

                                            double Size = (double) SelectVideo.length() / 1048576.0;

                                            TextViewSizeVideo.setText((new DecimalFormat("#.##").format(Size) + " " + GetActivity().getString(R.string.WriteUIMB)));
                                            TextViewLengthVideo.setText((String.valueOf(Time) + " " + GetActivity().getString(R.string.WriteUISeconds)));
                                        }
                                    }
                                }), R.id.ContainerFull, "GalleryViewUI");
                            }

                            @Override
                            public void OnDenied()
                            {
                                Misc.Toast(GetActivity(), GetActivity().getString(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        LinearLayoutBottom.addView(ImageViewVideo);

        ImageViewVote = new ImageView(GetActivity());
        ImageViewVote.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(GetActivity(), 56), 1.0f));
        ImageViewVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewVote.setPadding(Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12));
        ImageViewVote.setImageResource(R.drawable.ic_link);
        ImageViewVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(3);
                ScrollViewVote.setVisibility(View.VISIBLE);
            }
        });

        LinearLayoutBottom.addView(ImageViewVote);

        ImageViewFile = new ImageView(GetActivity());
        ImageViewFile.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(GetActivity(), 56), 1.0f));
        ImageViewFile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewFile.setPadding(Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12));
        ImageViewFile.setImageResource(R.drawable.ic_link);
        ImageViewFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Misc.HasPermission(GetActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, 3, new GalleryViewUI.GalleryListener()
                    {
                        @Override
                        public void OnSelection(String URL)
                        {
                            SelectFile = new File(URL);
                            double Size = (double) SelectFile.length() / 1048576.0;

                            RelativeLayoutFile.setVisibility(View.VISIBLE);
                            TextViewFileName.setText(SelectFile.getName());
                            TextViewFileDetail.setText((new DecimalFormat("#.##").format(Size) + " " + GetActivity().getString(R.string.WriteUIMB) + " / " + SelectFile.getName().substring(SelectFile.getName().lastIndexOf(".")).substring(1).toUpperCase()));
                        }

                        @Override public void OnRemove(String URL) { }
                        @Override public void OnSave() { }
                    }), R.id.ContainerFull, "GalleryViewUI");

                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(GetActivity());
                PermissionDialogGallery.SetContentView(R.drawable.ic_permission_storage, GetActivity().getString(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(GetActivity(), GetActivity().getString(R.string.PermissionStorage));
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnGranted()
                            {
                                GetActivity().GetManager().OpenView(new GalleryViewUI(1, 3, new GalleryViewUI.GalleryListener()
                                {
                                    @Override
                                    public void OnSelection(String URL)
                                    {
                                        SelectFile = new File(URL);
                                        double Size = (double) SelectFile.length() / 1048576.0;

                                        RelativeLayoutFile.setVisibility(View.VISIBLE);
                                        TextViewFileName.setText(SelectFile.getName());
                                        TextViewFileDetail.setText((new DecimalFormat("#.##").format(Size) + " " + GetActivity().getString(R.string.WriteUIMB) + " / " + SelectFile.getName().substring(SelectFile.getName().lastIndexOf(".")).substring(1).toUpperCase()));
                                    }

                                    @Override public void OnRemove(String URL) { }
                                    @Override public void OnSave() { }
                                }), R.id.ContainerFull, "GalleryViewUI");
                            }

                            @Override
                            public void OnDenied()
                            {
                                Misc.Toast(GetActivity(), GetActivity().getString(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        LinearLayoutBottom.addView(ImageViewFile);

        TextViewCount.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(GetActivity(), 56), 1.0f));
        TextViewCount.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        TextViewCount.setGravity(Gravity.CENTER);
        TextViewCount.setPadding(0, Misc.ToDP(GetActivity(), 6), 0, 0);
        TextViewCount.setText(("300"));

        LinearLayoutBottom.addView(TextViewCount);

        ImageView ImageViewSend = new ImageView(GetActivity());
        ImageViewSend.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(GetActivity(), 56), 1.0f));
        ImageViewSend.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSend.setPadding(Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8));
        ImageViewSend.setImageResource(R.drawable.ic_done_blue);

        LinearLayoutBottom.addView(ImageViewSend);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
        ViewLine2Param.addRule(RelativeLayout.ABOVE, LinearLayoutBottom.getId());

        View ViewLine2 = new View(GetActivity());
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(R.color.Gray2);
        ViewLine2.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 40));
        LinearLayoutCategoryParam.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 3));
        LinearLayoutCategoryParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

        LinearLayout LinearLayoutCategory = new LinearLayout(GetActivity());
        LinearLayoutCategory.setLayoutParams(LinearLayoutCategoryParam);
        LinearLayoutCategory.setId(Misc.GenerateViewID());
        LinearLayoutCategory.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCategory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogCategory = new Dialog(GetActivity());
                DialogCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCategory.setCancelable(false);

                RelativeLayout LinearLayoutMain = new RelativeLayout(GetActivity());
                LinearLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56)));
                RelativeLayoutHeader.setBackgroundResource(R.color.ActionBarWhite);
                RelativeLayoutHeader.setId(Misc.GenerateViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
                ImageViewBackParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewBack = new ImageView(GetActivity());
                ImageViewBack.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
                ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.ic_close_blue);
                ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogCategory.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewNameParam.setMargins(Misc.ToDP(GetActivity(), 15), 0, 0, 0);

                TextView TextViewName = new TextView(GetActivity(), 18, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewName.setText(GetActivity().getString(R.string.WriteUICategory));

                RelativeLayoutHeader.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray2);
                ViewLine.setId(Misc.GenerateViewID());

                LinearLayoutMain.addView(ViewLine);

                RelativeLayout.LayoutParams RecyclerViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RecyclerViewCategoryParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                RecyclerView RecyclerViewCategory = new RecyclerView(GetActivity());
                RecyclerViewCategory.setLayoutManager(new LinearLayoutManager(GetActivity()));
                RecyclerViewCategory.setAdapter(new AdapterCategory());
                RecyclerViewCategory.setLayoutParams(RecyclerViewCategoryParam);
                RecyclerViewCategory.addOnItemTouchListener(new OnClickRecyclerView(GetActivity(), new OnClickRecyclerView.OnItemClickListener()
                {
                    @Override
                    public void OnClick(int Position)
                    {
                        switch (Position)
                        {
                            case 0:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryNews));       SelectCategory = 1;  break;
                            case 1:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryFun));        SelectCategory = 2;  break;
                            case 2:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryMusic));      SelectCategory = 3;  break;
                            case 3:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategorySport));      SelectCategory = 4;  break;
                            case 4:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryFashion));    SelectCategory = 5;  break;
                            case 5:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryFood));       SelectCategory = 6;  break;
                            case 6:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryTechnology)); SelectCategory = 7;  break;
                            case 7:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryArt));        SelectCategory = 8;  break;
                            case 8:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryArtist));     SelectCategory = 9;  break;
                            case 9:  TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryMedia));      SelectCategory = 10; break;
                            case 10: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryBusiness));   SelectCategory = 11; break;
                            case 11: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryEconomy));    SelectCategory = 12; break;
                            case 12: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryLiterature)); SelectCategory = 13; break;
                            case 13: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryTravel));     SelectCategory = 14; break;
                            case 14: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryPolitics));   SelectCategory = 15; break;
                            case 15: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryHealth));     SelectCategory = 16; break;
                            case 16: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryReligious));  SelectCategory = 17; break;
                            case 17: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryKnowledge));  SelectCategory = 18; break;
                            case 18: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryNature));     SelectCategory = 19; break;
                            case 19: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryWeather));    SelectCategory = 20; break;
                            case 20: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryHistorical)); SelectCategory = 21; break;
                            default: TextViewCategorySelect.setText(GetActivity().getString(R.string.CategoryOther));      SelectCategory = 100; break;
                        }

                        DialogCategory.dismiss();
                    }
                }));

                LinearLayoutMain.addView(RecyclerViewCategory);

                DialogCategory.setContentView(LinearLayoutMain);
                DialogCategory.show();
            }
        });

        RelativeLayoutMain.addView(LinearLayoutCategory);

        ImageView ImageViewCategory = new ImageView(GetActivity());
        ImageViewCategory.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 40), Misc.ToDP(GetActivity(), 40)));
        ImageViewCategory.setImageResource(R.drawable.ic_category_blue);

        LinearLayoutCategory.addView(ImageViewCategory);

        TextView TextViewCategory = new TextView(GetActivity(), 16, false);
        TextViewCategory.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        TextViewCategory.setText(GetActivity().getString(R.string.WriteUICategory2));
        TextViewCategory.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        TextViewCategory.setPadding(0, Misc.ToDP(GetActivity(), 5), 0, 0);
        TextViewCategory.setGravity(Gravity.CENTER);

        LinearLayoutCategory.addView(TextViewCategory);

        TextViewCategorySelect.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewCategorySelect.setText(GetActivity().getString(R.string.WriteUICategoryNone));
        TextViewCategorySelect.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        TextViewCategorySelect.setPadding(Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5), 0);
        TextViewCategorySelect.setGravity(Gravity.CENTER);

        LinearLayoutCategory.addView(TextViewCategorySelect);

        ImageView ImageViewArrow = new ImageView(GetActivity());
        ImageViewArrow.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 20), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewArrow.setImageResource(R.drawable.ic_arrow_down_blue);

        LinearLayoutCategory.addView(ImageViewArrow);

        RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutContentParam.addRule(RelativeLayout.ABOVE, LinearLayoutCategory.getId());
        RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextMessage.getId());

        RelativeLayout RelativeLayoutContent = new RelativeLayout(GetActivity());
        RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

        RelativeLayoutMain.addView(RelativeLayoutContent);

        ScrollViewVote.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));
        ScrollViewVote.setVisibility(View.GONE);

        RelativeLayoutContent.addView(ScrollViewVote);

        RelativeLayout RelativeLayoutVote = new RelativeLayout(GetActivity());
        RelativeLayoutVote.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewVote.addView(RelativeLayoutVote);

        RelativeLayout.LayoutParams ImageViewCloseVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewCloseVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewCloseVote = new ImageView(GetActivity());
        ImageViewCloseVote.setLayoutParams(ImageViewCloseVoteParam);
        ImageViewCloseVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewCloseVote.setImageResource(R.drawable.ic_remove);
        ImageViewCloseVote.setPadding(Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13));
        ImageViewCloseVote.setId(Misc.GenerateViewID());
        ImageViewCloseVote.setAlpha(0.75f);
        ImageViewCloseVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(0);
                ScrollViewVote.setVisibility(View.GONE);
            }
        });

        RelativeLayoutVote.addView(ImageViewCloseVote);

        final GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setCornerRadius(Misc.ToDP(GetActivity(), 4));
        DrawableEnable.setStroke(Misc.ToDP(GetActivity(), 1), ContextCompat.getColor(GetActivity(), R.color.BlueLight));

        final GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(GetActivity(), 4));
        DrawableDisable.setStroke(Misc.ToDP(GetActivity(), 1), ContextCompat.getColor(GetActivity(), R.color.Gray2));

        View.OnFocusChangeListener OnFocus = new View.OnFocusChangeListener() { @Override public void onFocusChange(View view, boolean hasFocus) { view.setBackground(hasFocus ? DrawableEnable : DrawableDisable); } };

        RelativeLayout.LayoutParams EditTextVote1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
        EditTextVote1Param.setMargins(Misc.ToDP(GetActivity(), 15), 0, Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5));
        EditTextVote1Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());

        final EditText EditTextVote1 = new EditText(GetActivity());
        EditTextVote1.setLayoutParams(EditTextVote1Param);
        EditTextVote1.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        EditTextVote1.setId(Misc.GenerateViewID());
        EditTextVote1.setHint(R.string.WriteUIChoice1);
        EditTextVote1.setBackground(DrawableDisable);
        EditTextVote1.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        EditTextVote1.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        EditTextVote1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote1.setOnFocusChangeListener(OnFocus);

        RelativeLayoutVote.addView(EditTextVote1);

        RelativeLayout.LayoutParams EditTextVote2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
        EditTextVote2Param.setMargins(Misc.ToDP(GetActivity(), 15), 0, Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5));
        EditTextVote2Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote2Param.addRule(RelativeLayout.BELOW, EditTextVote1.getId());

        final EditText EditTextVote2 = new EditText(GetActivity());
        EditTextVote2.setLayoutParams(EditTextVote2Param);
        EditTextVote2.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        EditTextVote2.setId(Misc.GenerateViewID());
        EditTextVote2.setHint(R.string.WriteUIChoice2);
        EditTextVote2.setBackground(DrawableDisable);
        EditTextVote2.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        EditTextVote2.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        EditTextVote2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote2.setOnFocusChangeListener(OnFocus);

        RelativeLayoutVote.addView(EditTextVote2);

        RelativeLayout.LayoutParams EditTextVote3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
        EditTextVote3Param.setMargins(Misc.ToDP(GetActivity(), 15), 0, Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5));
        EditTextVote3Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote3Param.addRule(RelativeLayout.BELOW, EditTextVote2.getId());

        final EditText EditTextVote3 = new EditText(GetActivity());
        EditTextVote3.setLayoutParams(EditTextVote3Param);
        EditTextVote3.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        EditTextVote3.setId(Misc.GenerateViewID());
        EditTextVote3.setHint(R.string.WriteUIChoice3);
        EditTextVote3.setBackground(DrawableDisable);
        EditTextVote3.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        EditTextVote3.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        EditTextVote3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote3.setOnFocusChangeListener(OnFocus);
        EditTextVote3.setVisibility(View.GONE);

        RelativeLayoutVote.addView(EditTextVote3);

        RelativeLayout.LayoutParams EditTextVote4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
        EditTextVote4Param.setMargins(Misc.ToDP(GetActivity(), 15), 0, Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5));
        EditTextVote4Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote4Param.addRule(RelativeLayout.BELOW, EditTextVote3.getId());

        final EditText EditTextVote4 = new EditText(GetActivity());
        EditTextVote4.setLayoutParams(EditTextVote4Param);
        EditTextVote4.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        EditTextVote4.setId(Misc.GenerateViewID());
        EditTextVote4.setHint(R.string.WriteUIChoice4);
        EditTextVote4.setBackground(DrawableDisable);
        EditTextVote4.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        EditTextVote4.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        EditTextVote4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote4.setOnFocusChangeListener(OnFocus);
        EditTextVote4.setVisibility(View.GONE);

        RelativeLayoutVote.addView(EditTextVote4);

        RelativeLayout.LayoutParams EditTextVote5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
        EditTextVote5Param.setMargins(Misc.ToDP(GetActivity(), 15), 0, Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 5));
        EditTextVote5Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote5Param.addRule(RelativeLayout.BELOW, EditTextVote4.getId());

        final EditText EditTextVote5 = new EditText(GetActivity());
        EditTextVote5.setLayoutParams(EditTextVote5Param);
        EditTextVote5.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        EditTextVote5.setHint(R.string.WriteUIChoice5);
        EditTextVote5.setBackground(DrawableDisable);
        EditTextVote5.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
        EditTextVote5.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        EditTextVote5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote5.setOnFocusChangeListener(OnFocus);
        EditTextVote5.setVisibility(View.GONE);
        EditTextVote5.setId(Misc.GenerateViewID());

        RelativeLayoutVote.addView(EditTextVote5);

        RelativeLayout.LayoutParams TextViewLengthParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLengthParam.setMargins(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), 0);
        TextViewLengthParam.addRule(RelativeLayout.BELOW, EditTextVote5.getId());

        final TextView TextViewLength = new TextView(GetActivity(), 14, false);
        TextViewLength.setLayoutParams(TextViewLengthParam);
        TextViewLength.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewLength.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog DialogVote = new Dialog(GetActivity());
                DialogVote.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogVote.setCancelable(false);

                RelativeLayout LinearLayoutMain = new RelativeLayout(GetActivity());
                LinearLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56)));
                RelativeLayoutHeader.setBackgroundResource(R.color.ActionBarWhite);
                RelativeLayoutHeader.setId(Misc.GenerateViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
                ImageViewBackParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewBack = new ImageView(GetActivity());
                ImageViewBack.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
                ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.ic_close_blue);
                ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogVote.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewNameParam.setMargins(Misc.ToDP(GetActivity(), 15), 0, 0, 0);

                TextView TextViewName = new TextView(GetActivity(), 16, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewName.setText(GetActivity().getString(R.string.WriteUILength));

                RelativeLayoutHeader.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray2);
                ViewLine.setId(Misc.GenerateViewID());

                LinearLayoutMain.addView(ViewLine);

                RelativeLayout.LayoutParams EditTextTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
                EditTextTimeParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                final EditText EditTextTime = new EditText(GetActivity());
                EditTextTime.setLayoutParams(EditTextTimeParam);
                EditTextTime.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
                EditTextTime.setHint(R.string.WriteUITime);
                EditTextTime.setBackground(null);
                EditTextTime.setGravity(Gravity.CENTER);
                EditTextTime.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));
                EditTextTime.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                EditTextTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                EditTextTime.setId(Misc.GenerateViewID());
                EditTextTime.setInputType(InputType.TYPE_CLASS_PHONE);
                EditTextTime.requestFocus();
                EditTextTime.setFilters(new InputFilter[]
                {
                    new InputFilter.LengthFilter(4), new InputFilter()
                    {
                        @Override
                        public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de)
                        {
                            if (End > Start)
                            {
                                char[] AllowChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

                                for (int I = Start; I < End; I++)
                                {
                                    if (!new String(AllowChar).contains(String.valueOf(s.charAt(I))))
                                    {
                                        return "";
                                    }
                                }
                            }

                            return null;
                        }
                    }
                });

                LinearLayoutMain.addView(EditTextTime);

                RelativeLayout.LayoutParams TextViewSetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56));
                TextViewSetParam.addRule(RelativeLayout.BELOW, EditTextTime.getId());

                TextView TextViewSet = new TextView(GetActivity(), 16, false);
                TextViewSet.setLayoutParams(TextViewSetParam);
                TextViewSet.setGravity(Gravity.CENTER);
                TextViewSet.setTextColor(ContextCompat.getColor(GetActivity(), R.color.White));
                TextViewSet.setText(GetActivity().getString(R.string.WriteUISet));
                TextViewSet.setBackgroundColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
                TextViewSet.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        VoteTime = Integer.parseInt(EditTextTime.getText().toString()) * 3600;
                        TextViewLength.setText((GetActivity().getString(R.string.WriteUILength) + ": " + String.valueOf(VoteTime / 3600) + GetActivity().getString(R.string.WriteUIHour)));
                        DialogVote.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewSet);

                DialogVote.setContentView(LinearLayoutMain);
                DialogVote.show();
            }
        });

        VoteTime = 43200;

        TextViewLength.setText((GetActivity().getString(R.string.WriteUILength) + ": 12" + GetActivity().getString(R.string.WriteUIHour)));

        RelativeLayoutVote.addView(TextViewLength);

        RelativeLayout.LayoutParams ImageViewAddVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewAddVoteParam.addRule(RelativeLayout.BELOW, EditTextVote1.getId());
        ImageViewAddVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewAddVote = new ImageView(GetActivity());
        ImageViewAddVote.setLayoutParams(ImageViewAddVoteParam);
        ImageViewAddVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewAddVote.setImageResource(R.drawable.ic_play);
        ImageViewAddVote.setPadding(Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13));
        ImageViewAddVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ImageViewRemoveVote.setVisibility(View.VISIBLE);

                if (EditTextVote3.getVisibility() != View.VISIBLE)
                    EditTextVote3.setVisibility(View.VISIBLE);
                else if (EditTextVote4.getVisibility() != View.VISIBLE)
                    EditTextVote4.setVisibility(View.VISIBLE);
                else if (EditTextVote5.getVisibility() != View.VISIBLE)
                    EditTextVote5.setVisibility(View.VISIBLE);
            }
        });

        RelativeLayoutVote.addView(ImageViewAddVote);

        RelativeLayout.LayoutParams ImageViewRemoveVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewRemoveVoteParam.addRule(RelativeLayout.BELOW, EditTextVote2.getId());
        ImageViewRemoveVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageViewRemoveVote.setLayoutParams(ImageViewRemoveVoteParam);
        ImageViewRemoveVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveVote.setImageResource(R.drawable.ic_vote_gray);
        ImageViewRemoveVote.setPadding(Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13));
        ImageViewRemoveVote.setVisibility(View.GONE);
        ImageViewRemoveVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (EditTextVote5.getVisibility() == View.VISIBLE)
                    EditTextVote5.setVisibility(View.GONE);
                else if (EditTextVote4.getVisibility() == View.VISIBLE)
                    EditTextVote4.setVisibility(View.GONE);
                else if (EditTextVote3.getVisibility() == View.VISIBLE)
                {
                    view.setVisibility(View.GONE);
                    EditTextVote3.setVisibility(View.GONE);
                }
            }
        });

        RelativeLayoutVote.addView(ImageViewRemoveVote);

        RelativeLayout.LayoutParams RelativeLayoutFileParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 70));
        RelativeLayoutFileParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayoutFile.setLayoutParams(RelativeLayoutFileParam);
        RelativeLayoutFile.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutFile);

        RelativeLayout.LayoutParams ImageViewFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewFileParam.setMargins(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
        ImageViewFileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        GradientDrawable DrawableFile = new GradientDrawable();
        DrawableFile.setCornerRadius(Misc.ToDP(GetActivity(), 4));
        DrawableFile.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));

        ImageView ImageViewFile = new ImageView(GetActivity());
        ImageViewFile.setLayoutParams(ImageViewFileParam);
        ImageViewFile.setPadding(Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13));
        ImageViewFile.setImageResource(R.drawable.ic_download_white);
        ImageViewFile.setId(Misc.GenerateViewID());
        ImageViewFile.setBackground(DrawableFile);

        RelativeLayoutFile.addView(ImageViewFile);

        RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileNameParam.setMargins(0, Misc.ToDP(GetActivity(), 12), 0, 0);
        TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewFile.getId());

        TextViewFileName.setLayoutParams(TextViewFileNameParam);
        TextViewFileName.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewFileName.setId(Misc.GenerateViewID());

        RelativeLayoutFile.addView(TextViewFileName);

        RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, ImageViewFile.getId());
        TextViewFileDetailParam.addRule(RelativeLayout.BELOW, TextViewFileName.getId());

        TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
        TextViewFileDetail.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueGray2));

        RelativeLayoutFile.addView(TextViewFileDetail);

        RelativeLayout.LayoutParams ImageViewRemoveFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 25), Misc.ToDP(GetActivity(), 25));
        ImageViewRemoveFileParam.setMargins(0, Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), 0);
        ImageViewRemoveFileParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewRemoveFile = new ImageView(GetActivity());
        ImageViewRemoveFile.setLayoutParams(ImageViewRemoveFileParam);
        ImageViewRemoveFile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveFile.setImageResource(R.drawable.ic_remove);
        ImageViewRemoveFile.setAlpha(0.75f);
        ImageViewRemoveFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(0);
                SelectFile = null;
                RelativeLayoutFile.setVisibility(View.GONE);
            }
        });

        RelativeLayoutFile.addView(ImageViewRemoveFile);

        ViewPagerImage = new ViewPager(GetActivity());
        ViewPagerImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewPagerImage.setAdapter(ViewPagerAdapterImage = new ViewPagerAdapter());
        ViewPagerImage.setVisibility(View.GONE);

        RelativeLayoutContent.addView(ViewPagerImage);

        RelativeLayout.LayoutParams RelativeLayoutVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutVideoParam.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);
        RelativeLayoutVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayoutVideo.setLayoutParams(RelativeLayoutVideoParam);
        RelativeLayoutVideo.setBackgroundResource(R.color.Black);
        RelativeLayoutVideo.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutVideo);

        ImageViewThumbVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewThumbVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().GetManager().OpenView(new VideoPreviewUI(SelectVideo.getAbsolutePath(), true), R.id.ContainerFull, "VideoPreviewUI"); } });
        ImageViewThumbVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayoutVideo.addView(ImageViewThumbVideo);

        RelativeLayout.LayoutParams ImageViewRemoveVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 25), Misc.ToDP(GetActivity(), 25));
        ImageViewRemoveVideoParam.setMargins(0, Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), 0);
        ImageViewRemoveVideoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewRemoveVideo = new ImageView(GetActivity());
        ImageViewRemoveVideo.setLayoutParams(ImageViewRemoveVideoParam);
        ImageViewRemoveVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveVideo.setImageResource(R.drawable.ic_remove);
        ImageViewRemoveVideo.setId(Misc.GenerateViewID());
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

        RelativeLayout.LayoutParams ImageViewEditVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 25), Misc.ToDP(GetActivity(), 25));
        ImageViewEditVideoParam.setMargins(0, Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), 0);
        ImageViewEditVideoParam.addRule(RelativeLayout.BELOW, ImageViewRemoveVideo.getId());
        ImageViewEditVideoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewEditVideo = new ImageView(GetActivity());
        ImageViewEditVideo.setLayoutParams(ImageViewEditVideoParam);
        ImageViewEditVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewEditVideo.setImageResource(R.drawable.ic_remove);
        ImageViewEditVideo.setAlpha(0.75f);
        ImageViewEditVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String OldPath = SelectVideo.getAbsolutePath();
                File CacheFolder = CacheHandler.CacheDir(GetActivity());

                SelectVideo = new File(CacheFolder, "video." + String.valueOf(System.currentTimeMillis()) + ".mp4");

                final ProgressDialog Progress = new ProgressDialog(GetActivity());
                Progress.setMessage(GetActivity().getString(R.string.WriteUICompress));
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                MediaTransCoder.Start(OldPath, SelectVideo.getAbsolutePath(), new MediaTransCoder.MediaStrategy()
                {
                    @Override
                    public MediaFormat CreateVideo(MediaFormat Format)
                    {
                        int Frame = 30;
                        int BitRate = 500000;
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
                        Misc.RunOnUIThread(GetActivity(), new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Progress.cancel();

                                MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                                Retriever.setDataSource(SelectVideo.getAbsolutePath());

                                double Size = (double) SelectVideo.length() / 1048576.0;

                                TextViewSizeVideo.setText((new DecimalFormat("#.##").format(Size) + " " + GetActivity().getString(R.string.WriteUIMB)));
                                ImageViewThumbVideo.setImageBitmap(Retriever.getFrameAtTime(100));
                            }
                        }, 1);
                    }

                    @Override
                    public void OnFailed(Exception exception)
                    {
                        Misc.RunOnUIThread(GetActivity(), new Runnable() { @Override public void run() { Progress.cancel(); } }, 2);
                        Misc.Debug("WriteUI-VideoCompress: " + exception.toString());
                    }
                });
            }
        });

        RelativeLayoutVideo.addView(ImageViewEditVideo);

        GradientDrawable DrawableVideoSize = new GradientDrawable();
        DrawableVideoSize.setColor(Color.parseColor("#65000000"));
        DrawableVideoSize.setCornerRadius(Misc.ToDP(GetActivity(), 4));

        RelativeLayout.LayoutParams TextViewSizeVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSizeVideoParam.setMargins(Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8));
        TextViewSizeVideoParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        TextViewSizeVideo.setLayoutParams(TextViewSizeVideoParam);
        TextViewSizeVideo.setPadding(Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3), 0);
        TextViewSizeVideo.setBackground(DrawableVideoSize);
        TextViewSizeVideo.setId(Misc.GenerateViewID());

        RelativeLayoutVideo.addView(TextViewSizeVideo);

        RelativeLayout.LayoutParams TextViewLengthVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLengthVideoParam.setMargins(Misc.ToDP(GetActivity(), 8), 0, Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8));
        TextViewLengthVideoParam.addRule(RelativeLayout.BELOW, TextViewSizeVideo.getId());

        TextViewLengthVideo.setLayoutParams(TextViewLengthVideoParam);
        TextViewLengthVideo.setPadding(Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3), 0);
        TextViewLengthVideo.setBackground(DrawableVideoSize);

        RelativeLayoutVideo.addView(TextViewLengthVideo);

        RelativeLayout.LayoutParams ImageViewPlayVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 65), Misc.ToDP(GetActivity(), 65));
        ImageViewPlayVideoParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPlayVideo = new ImageView(GetActivity());
        ImageViewPlayVideo.setLayoutParams(ImageViewPlayVideoParam);
        ImageViewPlayVideo.setImageResource(R.drawable.ic_play);

        RelativeLayoutVideo.addView(ImageViewPlayVideo);

        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SelectCategory == 0)
                {
                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.WriteUIPickCategory));
                    return;
                }

                if (EditTextMessage.getText().length() <= 30 && SelectType == 0)
                {
                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.WriteUIStatement));
                    return;
                }
    
                Map<String, File> UploadFile = new HashMap<>();
                JSONObject Vote = new JSONObject();

                if (SelectType == 1)
                {
                    for (int I = 0; I < SelectImage.size(); I++)
                        UploadFile.put(("Image" + I), new File(SelectImage.get(I)));
                }
                else if (SelectType == 2)
                    UploadFile.put("Video", SelectVideo);
                else if (SelectType == 3)
                {
                    try
                    {
                        Vote.put("Vote1", EditTextVote1.getText().toString());
                        Vote.put("Vote2", EditTextVote2.getText().toString());

                        if (EditTextVote3.getVisibility() == View.VISIBLE)
                            Vote.put("Vote3", EditTextVote3.getText().toString());

                        if (EditTextVote4.getVisibility() == View.VISIBLE)
                            Vote.put("Vote4", EditTextVote4.getText().toString());

                        if (EditTextVote5.getVisibility() == View.VISIBLE)
                            Vote.put("Vote5", EditTextVote5.getText().toString());
                    }
                    catch (Exception e)
                    {
                        //
                    }
                }
                else if (SelectType == 4)
                    UploadFile.put("File", SelectFile);
                else
                    UploadFile = null;

                final ProgressDialog Progress = new ProgressDialog(GetActivity());
                Progress.setMessage(GetActivity().getString(R.string.WriteUIUploading));
                Progress.setIndeterminate(false);
                Progress.setCancelable(false);
                Progress.setMax(100);
                Progress.setProgress(0);
                Progress.show();

                AndroidNetworking.upload(Misc.GetRandomServer("PostWrite"))
                .addMultipartParameter("Message", EditTextMessage.getText().toString())
                .addMultipartParameter("Category", String.valueOf(SelectCategory))
                .addMultipartParameter("Type", String.valueOf(SelectType))
                .addMultipartParameter("Vote", Vote.toString())
                .addMultipartParameter("World", String.valueOf(IsWorld))
                .addHeaders("Token", SharedHandler.GetString(GetActivity(), "Token"))
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

                            // Todo Update Inbox and Errors
                            if (Result.getInt("Message") == 1000)
                            {
                                GetActivity().onBackPressed();
                            }
                        }
                        catch (Exception e)
                        {
                            Misc.Debug("WriteUI-RequestPost: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        Progress.cancel();
                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(LayoutListener);
        GetActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("WriteUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
        GetActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void ChangeType(int type)
    {
        if (type == 0)
        {
            SelectType = 0;
            ImageViewImage.setEnabled(true);
            ImageViewImage.setImageResource(R.drawable.ic_camera);
            ImageViewVideo.setEnabled(true);
            ImageViewVideo.setImageResource(R.drawable.ic_vote);
            ImageViewVote.setEnabled(true);
            ImageViewVote.setImageResource(R.drawable.ic_link);
            ImageViewFile.setEnabled(true);
            ImageViewFile.setImageResource(R.drawable.ic_link);
            return;
        }

        ImageViewImage.setEnabled(false);
        ImageViewImage.setImageResource(R.drawable.ic_camera_gray);
        ImageViewVideo.setEnabled(false);
        ImageViewVideo.setImageResource(R.drawable.ic_vote_gray);
        ImageViewVote.setEnabled(false);
        ImageViewVote.setImageResource(R.drawable.ic_link);
        ImageViewFile.setEnabled(false);
        ImageViewFile.setImageResource(R.drawable.ic_link);

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
                ImageViewVideo.setImageResource(R.drawable.ic_camera);
            break;
            case 3:
                SelectType = 3;
                ImageViewVote.setEnabled(true);
                ImageViewVote.setImageResource(R.drawable.ic_link);
            break;
            case 4:
                SelectType = 4;
                ImageViewFile.setEnabled(true);
                ImageViewFile.setImageResource(R.drawable.ic_link);
            break;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter
    {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup Container, final int Position)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            RelativeLayout.LayoutParams ImageViewImageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewImageParam.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

            ImageView ImageViewImage = new ImageView(GetActivity());
            ImageViewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewImage.setLayoutParams(ImageViewImageParam);
            ImageViewImage.setImageURI(Uri.parse(SelectImage.get(Position)));
            ImageViewImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SelectImage.size() > 0)
                        GetActivity().GetManager().OpenView(new ImagePreviewUI(SelectImage.get(Position)), R.id.ContainerFull, "ImagePreviewUI");
                }
            });

            RelativeLayoutMain.addView(ImageViewImage);

            RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 30), Misc.ToDP(GetActivity(), 30));
            ImageViewRemoveParam.setMargins(0, Misc.ToDP(GetActivity(), 5), Misc.ToDP(GetActivity(), 20), 0);
            ImageViewRemoveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewRemove = new ImageView(GetActivity());
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
        public void destroyItem(@NonNull ViewGroup Container, int position, @NonNull Object object)
        {
            Container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {
            return view == object;
        }

        @Override
        public int getCount()
        {
            return SelectImage.size();
        }
    }

    private class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolderMain>
    {
        private final List<Struct> CategoryList = new ArrayList<>();
        private final int ID_ICON = Misc.GenerateViewID();
        private final int ID_NAME = Misc.GenerateViewID();
        private final int ID_LINE = Misc.GenerateViewID();

        AdapterCategory()
        {

            CategoryList.clear();
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryNews), R.drawable.ic_category_news));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryFun), R.drawable.ic_category_fun));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryMusic), R.drawable.ic_category_music));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategorySport), R.drawable.ic_category_sport));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryFashion), R.drawable.ic_category_fashion));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryFood), R.drawable.ic_category_food));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryTechnology), R.drawable.ic_category_technology));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryArt), R.drawable.ic_category_art));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryArtist), R.drawable.ic_category_artist));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryMedia), R.drawable.ic_category_media));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryBusiness), R.drawable.ic_category_business));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryEconomy), R.drawable.ic_category_echonomy));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryLiterature), R.drawable.ic_category_lilterature));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryTravel), R.drawable.ic_category_travel));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryPolitics), R.drawable.ic_category_politics));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryHealth), R.drawable.ic_category_health));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryReligious), R.drawable.ic_category_religious));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryKnowledge), R.drawable.ic_category_knowledge));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryNature), R.drawable.ic_category_nature));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryWeather), R.drawable.ic_category_weather));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryHistorical), R.drawable.ic_category_historical));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryOther), R.drawable.ic_category_other));
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            ImageView ImageViewIcon;
            TextView TextViewName;
            View ViewLine;

            ViewHolderMain(View view)
            {
                super(view);
                ImageViewIcon = view.findViewById(ID_ICON);
                TextViewName = view.findViewById(ID_NAME);
                ViewLine = view.findViewById(ID_LINE);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolderMain Holder, int p)
        {
            int Position = Holder.getAdapterPosition();

            GlideApp.with(GetActivity()).load(CategoryList.get(Position).Image).dontAnimate().into(Holder.ImageViewIcon);

            Holder.TextViewName.setText(CategoryList.get(Position).Name);

            if (Position == CategoryList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutMain.setClickable(true);

            ImageView ImageViewIcon = new ImageView(GetActivity());
            ImageViewIcon.setPadding(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10));
            ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewIcon.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 45), Misc.ToDP(GetActivity(), 45)));
            ImageViewIcon.setId(ID_ICON);

            RelativeLayoutMain.addView(ImageViewIcon);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID_ICON);
            TextViewNameParam.setMargins(Misc.ToDP(GetActivity(), 15), 0, 0, 0);
            TextViewNameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            TextView TextViewName = new TextView(GetActivity(), 16, false);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
            TextViewName.setId(ID_NAME);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, ID_ICON);

            View ViewLine = new View(GetActivity());
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray);
            ViewLine.setId(ID_LINE);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderMain(RelativeLayoutMain);
        }

        @Override
        public int getItemCount()
        {
            return CategoryList.size();
        }

        class Struct
        {
            String Name;
            int Image;

            Struct(String name, int image)
            {
                Name = name;
                Image = image;
            }
        }
    }
}
