package co.biogram.main.ui.social;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Scroller;

import com.androidnetworking.AndroidNetworking;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.OnClickRecyclerView;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.general.ImagePreviewUI;
import co.biogram.main.ui.general.VideoPreviewUI;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.TextView;

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
                    GetActivity().GetManager().OpenView(new GalleryViewUI(3, false, new GalleryViewUI.GalleryListener()
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
                                GetActivity().GetManager().OpenView(new GalleryViewUI(3, false, new GalleryViewUI.GalleryListener()
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
                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, true, new GalleryViewUI.GalleryListener()
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
                                GetActivity().GetManager().OpenView(new GalleryViewUI(1, true, new GalleryViewUI.GalleryListener()
                                {
                                    List<String> ImgeURL = new ArrayList<>();

                                    @Override
                                    public void OnSelection(String URL)
                                    {
                                        ImgeURL.add(URL);
                                    }

                                    @Override
                                    public void OnRemove(String URL)
                                    {
                                        ImgeURL.remove(URL);
                                    }

                                    @Override
                                    public void OnSave()
                                    {
                                        if (ImgeURL.size() <= 0)
                                            return;

                                        ChangeType(1);
                                        SelectImage.addAll(ImgeURL);
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
        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

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
        ImageViewThumbVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewThumbVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().GetManager().OpenView(new VideoPreviewUI(SelectVideo.getAbsolutePath(), true), R.id.ContainerFull, "VideoPreviewUI"); } });

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
        ImageViewVote.setEnabled(true);
        ImageViewVote.setImageResource(R.drawable.ic_link);
        ImageViewFile.setEnabled(true);
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

























/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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
                                        MomentFragment momentFragment = (MomentFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MomentFragment");

                                        if (momentFragment != null)
                                            momentFragment.Update(new JSONObject(Result.getString("Result")));

                                        MiscHandler.Toast(context, getString(R.string.WriteFragmentUploadSuccess));
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











        return RelativeLayoutMain;
    }



    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] Permissions, @NonNull int[] GrantResults)
    {
        super.onRequestPermissionsResult(RequestCode, Permissions, GrantResults);

        /*if (PermissionHandler != null)
            PermissionHandler.GetRequestPermissionResult(RequestCode, Permissions, GrantResults);*
    }

    public void GetData(final String Path, boolean IsVideo)
    {
        if (IsVideo)
        {
            final Context context = getActivity();

            /*PermissionHandler = new PermissionHandler(Manifest.permission.READ_EXTERNAL_STORAGE, 100, this, new PermissionHandler.PermissionEvent()
            {
                @Override
                public void OnGranted()
                {
                    final MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                    Retriever.setDataSource(Path);
                    int Time = Math.round(Integer.parseInt(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000);

                    if (Time > 240)
                    {
                        MiscHandler.Toast(context, getString(R.string.WriteFragmentVideoLength));
                        return;
                    }

                    SelectVideo = new File(Path);

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

                    MediaTransCoder.Start(Path, SelectVideo.getAbsolutePath(), new MediaTransCoder.MediaStrategy()
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

                            try { Frame = Format.getInteger(MediaFormat.KEY_FRAME_RATE); } catch (Exception e) {  }

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

                            try { Sample = Format.getInteger(MediaFormat.KEY_SAMPLE_RATE); } catch (Exception e) { }
                            try { Channel = Format.getInteger(MediaFormat.KEY_CHANNEL_COUNT); } catch (Exception e) {  }

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

                @Override
                public void OnFailed()
                {
                    MiscHandler.Toast(context, getString(R.string.PermissionStorage));
                }
            });*
        }
        else
        {
            if (SelectImage.size() > 2)
                return;

            try
            {
                File ImageFile = new File(Path);
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
                    int Orientation = new ExifInterface(Path).getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

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
            catch (Exception e)
            {
                MiscHandler.Debug("WriteFragment-ImageCompress: " + e.toString());
            }
        }
    }


}

*/