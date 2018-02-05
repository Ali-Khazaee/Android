package co.biogram.main.ui.social;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.FontHandler;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.OnClickRecyclerView;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.general.CropViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.general.ImagePreviewUI;
import co.biogram.main.ui.general.VideoPreviewUI;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.ProgressDialog;
import co.biogram.main.ui.view.TextView;
import co.biogram.media.MediaTransCoder;

class WriteUI extends FragmentView
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
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
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
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_blue_rtl : R.drawable.back_blue);
        ImageViewBack.setId(Misc.ViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.WriteUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewWorldParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWorldParam.addRule(Misc.Align("L"));

        final ImageView ImageViewWorld = new ImageView(GetActivity());
        ImageViewWorld.setLayoutParams(ImageViewWorldParam);
        ImageViewWorld.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewWorld.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewWorld.setImageResource(R.drawable.global_bluegray);
        ImageViewWorld.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsWorld == 0)
                {
                    IsWorld = 1;
                    ImageViewWorld.setImageResource(R.drawable.global_blue);
                }
                else
                {
                    IsWorld = 0;
                    ImageViewWorld.setImageResource(R.drawable.global_bluegray);
                }
            }
        });

        RelativeLayoutHeader.addView(ImageViewWorld);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams EditTextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextMessageParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final EditText EditTextMessage = new EditText(GetActivity());
        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextMessage.setId(Misc.ViewID());
        EditTextMessage.setMaxLines(5);
        EditTextMessage.setHint(R.string.WriteUIMessage);
        EditTextMessage.setBackground(null);
        EditTextMessage.requestFocus();
        EditTextMessage.setTypeface(FontHandler.GetTypeface(GetActivity()));
        EditTextMessage.setScroller(new Scroller(GetActivity()));
        EditTextMessage.setVerticalScrollBarEnabled(true);
        EditTextMessage.setMovementMethod(new ScrollingMovementMethod());
        EditTextMessage.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextMessage.setHintTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.Gray : R.color.Gray));
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
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                TextView TextViewDelete = new TextView(GetActivity(), 14, false);
                TextViewDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewDelete.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewDelete.setText(GetActivity().getString(R.string.WriteUIDelete));
                TextViewDelete.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
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
                DeleteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                DeleteLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(DeleteLine);

                TextView TextViewPaste = new TextView(GetActivity(), 14, false);
                TextViewPaste.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewPaste.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewPaste.setText(GetActivity().getString(R.string.WriteUIPaste));
                TextViewPaste.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
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
                PasteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                PasteLine.setBackgroundResource(R.color.Gray);

                LinearLayoutMain.addView(PasteLine);

                TextView TextViewCopy = new TextView(GetActivity(), 14, false);
                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCopy.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewCopy.setText(GetActivity().getString(R.string.WriteUICopy));
                TextViewCopy.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewCopy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) GetActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("WriteMessage", EditTextMessage.getText().toString());

                        if (clipboard != null)
                            clipboard.setPrimaryClip(clip);

                        Misc.Toast(GetActivity().getString(R.string.WriteUIClipboard));
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

        RelativeLayout.LayoutParams LinearLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        LinearLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutBottom = new LinearLayout(GetActivity());
        LinearLayoutBottom.setLayoutParams(LinearLayoutBottomParam);
        LinearLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutBottom.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        LinearLayoutBottom.setId(Misc.ViewID());

        RelativeLayoutMain.addView(LinearLayoutBottom);

        ImageViewImage = new ImageView(GetActivity());
        ImageViewImage.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        ImageViewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewImage.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewImage.setImageResource(Misc.IsDark() ? R.drawable.camera_gray : R.drawable.camera_bluegray);
        ImageViewImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (SelectImage.size() >= 3)
                {
                    Misc.Toast(GetActivity().getString(R.string.WriteUIMaximumImage));
                    return;
                }

                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener()
                {
                    List<String> ImageURL = new ArrayList<>();

                    @Override
                    public void OnSelection(String URL)
                    {
                        ImageURL.add(URL);
                    }

                    @Override
                    public void OnRemove(String URL)
                    {
                        ImageURL.remove(URL);
                    }

                    @Override
                    public void OnSave()
                    {
                        if (ImageURL.size() <= 0)
                            return;

                        ChangeType(1);
                        ViewPagerImage.setVisibility(View.VISIBLE);

                        AsyncTask.execute(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    int Size = Misc.ToDP(150);

                                    for (String i : ImageURL)
                                    {
                                        BitmapFactory.Options O = new BitmapFactory.Options();
                                        O.inJustDecodeBounds = true;

                                        BitmapFactory.decodeFile(i, O);

                                        O.inSampleSize = Misc.SampleSize(O, Size, Size);
                                        O.inJustDecodeBounds = false;

                                        Bitmap bitmap = BitmapFactory.decodeFile(i, O);

                                        File file = new File(CacheHandler.CacheDir(GetActivity()), System.currentTimeMillis() + ".jpg");
                                        file.createNewFile();

                                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                                        FileOutputStream FOS = new FileOutputStream(file);
                                        FOS.write(BAOS.toByteArray());
                                        FOS.flush();
                                        FOS.close();

                                        SelectImage.add(file.getAbsolutePath());
                                    }

                                    Misc.RunOnUIThread(new Runnable() { @Override public void run() { ViewPagerAdapterImage.notifyDataSetChanged(); } }, 2);
                                }
                                catch (Exception e)
                                {
                                    Misc.Debug("WriteUI-Compress: " + e.toString());
                                }
                            }
                        });
                    }
                };

                if (Misc.HasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    GetActivity().GetManager().OpenView(new GalleryViewUI(3, 1, L), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(GetActivity());
                PermissionDialogGallery.SetContentView(R.drawable.permission_storage_white, GetActivity().getString(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(GetActivity().getString(R.string.PermissionStorage));
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnResult(boolean Granted)
                            {
                                if (Granted)
                                    GetActivity().GetManager().OpenView(new GalleryViewUI(3, 1, L), R.id.ContainerFull, "GalleryViewUI");
                                else
                                    Misc.Toast(GetActivity().getString(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        ImageViewVideo = new ImageView(GetActivity());
        ImageViewVideo.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        ImageViewVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewVideo.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewVideo.setImageResource(Misc.IsDark() ? R.drawable.video_gray : R.drawable.video_bluegray);
        ImageViewVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener()
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
                                Misc.Toast(GetActivity().getString(R.string.WriteUIVideoLength));
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
                };

                if (Misc.HasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, 2, L), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(GetActivity());
                PermissionDialogGallery.SetContentView(R.drawable.permission_storage_white, GetActivity().getString(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(GetActivity().getString(R.string.PermissionStorage));
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnResult(boolean Granted)
                            {
                                if (Granted)
                                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, 2, L), R.id.ContainerFull, "GalleryViewUI");
                                else
                                    Misc.Toast(GetActivity().getString(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        ImageViewVote = new ImageView(GetActivity());
        ImageViewVote.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        ImageViewVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewVote.setPadding(Misc.ToDP(16), Misc.ToDP(16), Misc.ToDP(16), Misc.ToDP(16));
        ImageViewVote.setImageResource(Misc.IsDark() ? R.drawable.vote_gray : R.drawable.vote_bluegray);
        ImageViewVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(3);
                ScrollViewVote.setVisibility(View.VISIBLE);
            }
        });

        ImageViewFile = new ImageView(GetActivity());
        ImageViewFile.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        ImageViewFile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewFile.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewFile.setImageResource(Misc.IsDark() ? R.drawable.attach_gray : R.drawable.attach_bluegray);
        ImageViewFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener()
                {
                    @Override
                    public void OnSelection(String URL)
                    {
                        SelectFile = new File(URL);
                        double Size = (double) SelectFile.length() / 1048576.0;

                        ChangeType(4);
                        RelativeLayoutFile.setVisibility(View.VISIBLE);
                        TextViewFileName.setText(SelectFile.getName());
                        TextViewFileDetail.setText((new DecimalFormat("#.##").format(Size) + " " + GetActivity().getString(R.string.WriteUIMB) + " / " + SelectFile.getName().substring(SelectFile.getName().lastIndexOf(".")).substring(1).toUpperCase()));
                    }

                    @Override public void OnRemove(String URL) { }
                    @Override public void OnSave() { }
                };

                if (Misc.HasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, 3, L), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(GetActivity());
                PermissionDialogGallery.SetContentView(R.drawable.permission_storage_white, GetActivity().getString(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(GetActivity().getString(R.string.PermissionStorage));
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnResult(boolean Granted)
                            {
                                if (Granted)
                                    GetActivity().GetManager().OpenView(new GalleryViewUI(1, 3, L), R.id.ContainerFull, "GalleryViewUI");
                                else
                                    Misc.Toast(GetActivity().getString(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        TextViewCount.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        TextViewCount.SetColor(R.color.Gray);
        TextViewCount.setGravity(Gravity.CENTER);
        TextViewCount.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewCount.setText(("300"));

        TextView TextViewSend = new TextView(GetActivity(), 14, false);
        TextViewSend.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        TextViewSend.SetColor(R.color.Primary);
        TextViewSend.setGravity(Gravity.CENTER);
        TextViewSend.setText(GetActivity().getString(R.string.WriteUISend));

        if (Misc.IsRTL())
        {
            LinearLayoutBottom.addView(TextViewSend);
            LinearLayoutBottom.addView(TextViewCount);
            LinearLayoutBottom.addView(ImageViewFile);
            LinearLayoutBottom.addView(ImageViewVote);
            LinearLayoutBottom.addView(ImageViewVideo);
            LinearLayoutBottom.addView(ImageViewImage);
        }
        else
        {
            LinearLayoutBottom.addView(ImageViewImage);
            LinearLayoutBottom.addView(ImageViewVideo);
            LinearLayoutBottom.addView(ImageViewVote);
            LinearLayoutBottom.addView(ImageViewFile);
            LinearLayoutBottom.addView(TextViewCount);
            LinearLayoutBottom.addView(TextViewSend);
        }

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine2Param.addRule(RelativeLayout.ABOVE, LinearLayoutBottom.getId());

        View ViewLine2 = new View(GetActivity());
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine2.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(40));
        LinearLayoutCategoryParam.setMargins(Misc.ToDP(5), 0, Misc.ToDP(5), Misc.ToDP(3));
        LinearLayoutCategoryParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

        LinearLayout LinearLayoutCategory = new LinearLayout(GetActivity());
        LinearLayoutCategory.setLayoutParams(LinearLayoutCategoryParam);
        LinearLayoutCategory.setId(Misc.ViewID());
        LinearLayoutCategory.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCategory.setGravity(Misc.IsRTL() ? Gravity.END : Gravity.START);
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
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
                RelativeLayoutHeader.setId(Misc.ViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewBackParam.addRule(Misc.Align("L"));

                ImageView ImageViewBack = new ImageView(GetActivity());
                ImageViewBack.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.close_blue);
                ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogCategory.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(Misc.Align("R"));
                TextViewNameParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewNameParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);

                TextView TextViewName = new TextView(GetActivity(), 16, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewName.setText(GetActivity().getString(R.string.WriteUICategory));

                RelativeLayoutHeader.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
                ViewLine.setId(Misc.ViewID());

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
        ImageViewCategory.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(40), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCategory.setImageResource(R.drawable.category_blue);

        TextView TextViewCategory = new TextView(GetActivity(), 16, false);
        TextViewCategory.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        TextViewCategory.setText(GetActivity().getString(R.string.WriteUICategory2));
        TextViewCategory.SetColor(R.color.Primary);
        TextViewCategory.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewCategory.setGravity(Gravity.CENTER);

        TextViewCategorySelect.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        TextViewCategorySelect.setText(GetActivity().getString(R.string.WriteUICategoryNone));
        TextViewCategorySelect.SetColor(R.color.Primary);
        TextViewCategorySelect.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), 0);
        TextViewCategorySelect.setGravity(Gravity.CENTER);

        ImageView ImageViewArrow = new ImageView(GetActivity());
        ImageViewArrow.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(20), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewArrow.setImageResource(R.drawable.arrow_down_blue);

        if (Misc.IsRTL())
        {
            LinearLayoutCategory.addView(ImageViewArrow);
            LinearLayoutCategory.addView(TextViewCategorySelect);
            LinearLayoutCategory.addView(TextViewCategory);
            LinearLayoutCategory.addView(ImageViewCategory);
        }
        else
        {
            LinearLayoutCategory.addView(ImageViewCategory);
            LinearLayoutCategory.addView(TextViewCategory);
            LinearLayoutCategory.addView(TextViewCategorySelect);
            LinearLayoutCategory.addView(ImageViewArrow);
        }

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

        RelativeLayout.LayoutParams ImageViewCloseVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewCloseVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewCloseVote = new ImageView(GetActivity());
        ImageViewCloseVote.setLayoutParams(ImageViewCloseVoteParam);
        ImageViewCloseVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewCloseVote.setImageResource(R.drawable.close_black);
        ImageViewCloseVote.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewCloseVote.setId(Misc.ViewID());
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
        DrawableEnable.setCornerRadius(Misc.ToDP(4));
        DrawableEnable.setStroke(Misc.ToDP(1), ContextCompat.getColor(GetActivity(), R.color.Primary));

        final GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(4));
        DrawableDisable.setStroke(Misc.ToDP(1), ContextCompat.getColor(GetActivity(), R.color.Gray));

        View.OnFocusChangeListener OnFocus = new View.OnFocusChangeListener() { @Override public void onFocusChange(View view, boolean hasFocus) { view.setBackground(hasFocus ? DrawableEnable : DrawableDisable); } };

        RelativeLayout.LayoutParams EditTextVote1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote1Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote1Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());

        final EditText EditTextVote1 = new EditText(GetActivity());
        EditTextVote1.setLayoutParams(EditTextVote1Param);
        EditTextVote1.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote1.setId(Misc.ViewID());
        EditTextVote1.setHint(R.string.WriteUIChoice1);
        EditTextVote1.setBackground(DrawableDisable);
        EditTextVote1.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
        EditTextVote1.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote1.setOnFocusChangeListener(OnFocus);

        RelativeLayoutVote.addView(EditTextVote1);

        RelativeLayout.LayoutParams EditTextVote2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote2Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote2Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote2Param.addRule(RelativeLayout.BELOW, EditTextVote1.getId());

        final EditText EditTextVote2 = new EditText(GetActivity());
        EditTextVote2.setLayoutParams(EditTextVote2Param);
        EditTextVote2.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote2.setId(Misc.ViewID());
        EditTextVote2.setHint(R.string.WriteUIChoice2);
        EditTextVote2.setBackground(DrawableDisable);
        EditTextVote2.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
        EditTextVote2.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote2.setOnFocusChangeListener(OnFocus);

        RelativeLayoutVote.addView(EditTextVote2);

        RelativeLayout.LayoutParams EditTextVote3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote3Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote3Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote3Param.addRule(RelativeLayout.BELOW, EditTextVote2.getId());

        final EditText EditTextVote3 = new EditText(GetActivity());
        EditTextVote3.setLayoutParams(EditTextVote3Param);
        EditTextVote3.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote3.setId(Misc.ViewID());
        EditTextVote3.setHint(R.string.WriteUIChoice3);
        EditTextVote3.setBackground(DrawableDisable);
        EditTextVote3.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
        EditTextVote3.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote3.setOnFocusChangeListener(OnFocus);
        EditTextVote3.setVisibility(View.GONE);

        RelativeLayoutVote.addView(EditTextVote3);

        RelativeLayout.LayoutParams EditTextVote4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote4Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote4Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote4Param.addRule(RelativeLayout.BELOW, EditTextVote3.getId());

        final EditText EditTextVote4 = new EditText(GetActivity());
        EditTextVote4.setLayoutParams(EditTextVote4Param);
        EditTextVote4.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote4.setId(Misc.ViewID());
        EditTextVote4.setHint(R.string.WriteUIChoice4);
        EditTextVote4.setBackground(DrawableDisable);
        EditTextVote4.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
        EditTextVote4.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote4.setOnFocusChangeListener(OnFocus);
        EditTextVote4.setVisibility(View.GONE);

        RelativeLayoutVote.addView(EditTextVote4);

        RelativeLayout.LayoutParams EditTextVote5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote5Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote5Param.addRule(RelativeLayout.LEFT_OF, ImageViewCloseVote.getId());
        EditTextVote5Param.addRule(RelativeLayout.BELOW, EditTextVote4.getId());

        final EditText EditTextVote5 = new EditText(GetActivity());
        EditTextVote5.setLayoutParams(EditTextVote5Param);
        EditTextVote5.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote5.setHint(R.string.WriteUIChoice5);
        EditTextVote5.setBackground(DrawableDisable);
        EditTextVote5.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
        EditTextVote5.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote5.setOnFocusChangeListener(OnFocus);
        EditTextVote5.setVisibility(View.GONE);
        EditTextVote5.setId(Misc.ViewID());

        RelativeLayoutVote.addView(EditTextVote5);

        RelativeLayout.LayoutParams TextViewLengthParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLengthParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), 0);
        TextViewLengthParam.addRule(RelativeLayout.BELOW, EditTextVote5.getId());

        final TextView TextViewLength = new TextView(GetActivity(), 14, false);
        TextViewLength.setLayoutParams(TextViewLengthParam);
        TextViewLength.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
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
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
                RelativeLayoutHeader.setId(Misc.ViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewBackParam.addRule(Misc.Align("L"));

                ImageView ImageViewBack = new ImageView(GetActivity());
                ImageViewBack.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.close_blue);
                ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogVote.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(Misc.Align("R"));
                TextViewNameParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewNameParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);

                TextView TextViewName = new TextView(GetActivity(), 16, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewName.setText(GetActivity().getString(R.string.WriteUILength));

                RelativeLayoutHeader.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
                ViewLine.setId(Misc.ViewID());

                LinearLayoutMain.addView(ViewLine);

                RelativeLayout.LayoutParams EditTextTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
                EditTextTimeParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                final EditText EditTextTime = new EditText(GetActivity());
                EditTextTime.setLayoutParams(EditTextTimeParam);
                EditTextTime.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                EditTextTime.setHint(R.string.WriteUITime);
                EditTextTime.setBackground(null);
                EditTextTime.setGravity(Gravity.CENTER);
                EditTextTime.setHintTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
                EditTextTime.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
                EditTextTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                EditTextTime.setId(Misc.ViewID());
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

                RelativeLayout.LayoutParams TextViewSetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
                TextViewSetParam.addRule(RelativeLayout.BELOW, EditTextTime.getId());

                TextView TextViewSet = new TextView(GetActivity(), 16, false);
                TextViewSet.setLayoutParams(TextViewSetParam);
                TextViewSet.setGravity(Gravity.CENTER);
                TextViewSet.SetColor(R.color.TextDark);
                TextViewSet.setText(GetActivity().getString(R.string.WriteUISet));
                TextViewSet.setBackgroundColor(ContextCompat.getColor(GetActivity(), R.color.Primary));
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

        RelativeLayout.LayoutParams ImageViewAddVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewAddVoteParam.addRule(RelativeLayout.BELOW, EditTextVote1.getId());
        ImageViewAddVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewAddVote = new ImageView(GetActivity());
        ImageViewAddVote.setLayoutParams(ImageViewAddVoteParam);
        ImageViewAddVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewAddVote.setImageResource(R.drawable.plus_blue);
        ImageViewAddVote.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
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

        RelativeLayout.LayoutParams ImageViewRemoveVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewRemoveVoteParam.addRule(RelativeLayout.BELOW, EditTextVote2.getId());
        ImageViewRemoveVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageViewRemoveVote.setLayoutParams(ImageViewRemoveVoteParam);
        ImageViewRemoveVote.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveVote.setImageResource(R.drawable.close_bluegray);
        ImageViewRemoveVote.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
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

        RelativeLayout.LayoutParams RelativeLayoutFileParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(70));
        RelativeLayoutFileParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayoutFile.setLayoutParams(RelativeLayoutFileParam);
        RelativeLayoutFile.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutFile);

        RelativeLayout.LayoutParams ImageViewFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewFileParam.setMargins(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        ImageViewFileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        GradientDrawable DrawableFile = new GradientDrawable();
        DrawableFile.setCornerRadius(Misc.ToDP(4));
        DrawableFile.setColor(ContextCompat.getColor(GetActivity(), R.color.Primary));

        ImageView ImageViewFile = new ImageView(GetActivity());
        ImageViewFile.setLayoutParams(ImageViewFileParam);
        ImageViewFile.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewFile.setImageResource(R.drawable._inbox_download);
        ImageViewFile.setId(Misc.ViewID());
        ImageViewFile.setBackground(DrawableFile);

        RelativeLayoutFile.addView(ImageViewFile);

        RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileNameParam.setMargins(0, Misc.ToDP(12), 0, 0);
        TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewFile.getId());

        TextViewFileName.setLayoutParams(TextViewFileNameParam);
        TextViewFileName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewFileName.setId(Misc.ViewID());

        RelativeLayoutFile.addView(TextViewFileName);

        RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, ImageViewFile.getId());
        TextViewFileDetailParam.addRule(RelativeLayout.BELOW, TextViewFileName.getId());

        TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
        TextViewFileDetail.SetColor(R.color.Gray);

        RelativeLayoutFile.addView(TextViewFileDetail);

        RelativeLayout.LayoutParams ImageViewRemoveFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(25), Misc.ToDP(25));
        ImageViewRemoveFileParam.setMargins(0, Misc.ToDP(10), Misc.ToDP(10), 0);
        ImageViewRemoveFileParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewRemoveFile = new ImageView(GetActivity());
        ImageViewRemoveFile.setLayoutParams(ImageViewRemoveFileParam);
        ImageViewRemoveFile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveFile.setImageResource(R.drawable.close_black);
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
        RelativeLayoutVideoParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
        RelativeLayoutVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayoutVideo.setLayoutParams(RelativeLayoutVideoParam);
        RelativeLayoutVideo.setBackgroundResource(R.color.TextWhite);
        RelativeLayoutVideo.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutVideo);

        ImageViewThumbVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewThumbVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().GetManager().OpenView(new VideoPreviewUI(SelectVideo.getAbsolutePath(), true), R.id.ContainerFull, "VideoPreviewUI"); } });
        ImageViewThumbVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayoutVideo.addView(ImageViewThumbVideo);

        RelativeLayout.LayoutParams ImageViewRemoveVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(25), Misc.ToDP(25));
        ImageViewRemoveVideoParam.setMargins(0, Misc.ToDP(10), Misc.ToDP(10), 0);
        ImageViewRemoveVideoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewRemoveVideo = new ImageView(GetActivity());
        ImageViewRemoveVideo.setLayoutParams(ImageViewRemoveVideoParam);
        ImageViewRemoveVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewRemoveVideo.setImageResource(R.drawable.close_black);
        ImageViewRemoveVideo.setId(Misc.ViewID());
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

        RelativeLayout.LayoutParams ImageViewEditVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(25), Misc.ToDP(25));
        ImageViewEditVideoParam.setMargins(0, Misc.ToDP(10), Misc.ToDP(10), 0);
        ImageViewEditVideoParam.addRule(RelativeLayout.BELOW, ImageViewRemoveVideo.getId());
        ImageViewEditVideoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewEditVideo = new ImageView(GetActivity());
        ImageViewEditVideo.setLayoutParams(ImageViewEditVideoParam);
        ImageViewEditVideo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewEditVideo.setImageResource(R.drawable.compress_white);
        ImageViewEditVideo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT < 18)
                {
                    Misc.Toast(GetActivity().getString(R.string.WriteUICantCompress));
                    return;
                }

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
                        Misc.RunOnUIThread(new Runnable()
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
                        Misc.RunOnUIThread(new Runnable() { @Override public void run() { Progress.cancel(); } }, 2);
                        Misc.Debug("WriteUI-VideoCompress: " + exception.toString());
                    }
                });
            }
        });

        RelativeLayoutVideo.addView(ImageViewEditVideo);

        GradientDrawable DrawableVideoSize = new GradientDrawable();
        DrawableVideoSize.setColor(Color.parseColor("#65000000"));
        DrawableVideoSize.setCornerRadius(Misc.ToDP(4));

        RelativeLayout.LayoutParams TextViewSizeVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSizeVideoParam.setMargins(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
        TextViewSizeVideoParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        TextViewSizeVideo.setLayoutParams(TextViewSizeVideoParam);
        TextViewSizeVideo.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), 0);
        TextViewSizeVideo.setBackground(DrawableVideoSize);
        TextViewSizeVideo.setId(Misc.ViewID());

        RelativeLayoutVideo.addView(TextViewSizeVideo);

        RelativeLayout.LayoutParams TextViewLengthVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLengthVideoParam.setMargins(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(8));
        TextViewLengthVideoParam.addRule(RelativeLayout.BELOW, TextViewSizeVideo.getId());

        TextViewLengthVideo.setLayoutParams(TextViewLengthVideoParam);
        TextViewLengthVideo.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), 0);
        TextViewLengthVideo.setBackground(DrawableVideoSize);

        RelativeLayoutVideo.addView(TextViewLengthVideo);

        RelativeLayout.LayoutParams ImageViewPlayVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(65), Misc.ToDP(65));
        ImageViewPlayVideoParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPlayVideo = new ImageView(GetActivity());
        ImageViewPlayVideo.setLayoutParams(ImageViewPlayVideoParam);
        ImageViewPlayVideo.setImageResource(R.drawable._general_play);

        RelativeLayoutVideo.addView(ImageViewPlayVideo);

        TextViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SelectCategory == 0)
                {
                    Misc.Toast( GetActivity().getString(R.string.WriteUIPickCategory));
                    return;
                }

                if (EditTextMessage.getText().length() <= 30 && SelectType == 0)
                {
                    Misc.Toast( GetActivity().getString(R.string.WriteUIStatement));
                    return;
                }

                JSONObject Vote = new JSONObject();
                Map<String, File> UploadFile = new HashMap<>();

                if (SelectType == 1)
                {
                    for (int I = 1; I <= SelectImage.size(); I++)
                        UploadFile.put(("Image" + I), new File(SelectImage.get(I - 1)));
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

                        Vote.put("Time", VoteTime);
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("WriteUI-Vote: " + e.toString());
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
                .addHeaders("token", SharedHandler.GetString(GetActivity(), "Token"))
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

                            switch (Result.getInt("Message"))
                            {
                                case 0:
                                    InboxUI inboxUI = (InboxUI) GetActivity().GetManager().FindByTag("InboxUI");

                                    if (inboxUI != null)
                                        inboxUI.Update(Result.getJSONObject("Result"));

                                    GetActivity().onBackPressed();
                                break;
                                case 1:
                                default:
                                    Misc.GeneralError(Result.getInt("Message"));
                                break;
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
                        Misc.Toast( GetActivity().getString(R.string.GeneralNoInternet));
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
            ImageViewImage.setImageResource(Misc.IsDark() ? R.drawable.camera_gray : R.drawable.camera_bluegray);
            ImageViewVideo.setEnabled(true);
            ImageViewVideo.setImageResource(Misc.IsDark() ? R.drawable.video_gray : R.drawable.video_bluegray);
            ImageViewVote.setEnabled(true);
            ImageViewVote.setImageResource(Misc.IsDark() ? R.drawable.vote_gray : R.drawable.vote_bluegray);
            ImageViewFile.setEnabled(true);
            ImageViewFile.setImageResource(Misc.IsDark() ? R.drawable.attach_gray : R.drawable.attach_bluegray);
            return;
        }

        ImageViewImage.setEnabled(false);
        ImageViewImage.setImageResource(Misc.IsDark() ? R.drawable.camera_bluegray : R.drawable.camera_gray);
        ImageViewVideo.setEnabled(false);
        ImageViewVideo.setImageResource(Misc.IsDark() ? R.drawable.video_bluegray : R.drawable.video_gray);
        ImageViewVote.setEnabled(false);
        ImageViewVote.setImageResource(Misc.IsDark() ? R.drawable.vote_bluegray : R.drawable.vote_gray);
        ImageViewFile.setEnabled(false);
        ImageViewFile.setImageResource(Misc.IsDark() ? R.drawable.attach_bluegray : R.drawable.attach_gray);

        switch (type)
        {
            case 1:
                SelectType = 1;
                ImageViewImage.setEnabled(true);
                ImageViewImage.setImageResource(Misc.IsDark() ? R.drawable.camera_gray : R.drawable.camera_bluegray);
            break;
            case 2:
                SelectType = 2;
                ImageViewVideo.setEnabled(true);
                ImageViewVideo.setImageResource(Misc.IsDark() ? R.drawable.video_gray : R.drawable.video_bluegray);
            break;
            case 3:
                SelectType = 3;
                ImageViewVote.setEnabled(true);
                ImageViewVote.setImageResource(Misc.IsDark() ? R.drawable.vote_gray : R.drawable.vote_bluegray);
            break;
            case 4:
                SelectType = 4;
                ImageViewFile.setEnabled(true);
                ImageViewFile.setImageResource(Misc.IsDark() ? R.drawable.attach_gray : R.drawable.attach_bluegray);
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
            ImageViewImageParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);

            ImageView ImageViewImage = new ImageView(GetActivity());
            ImageViewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewImage.setLayoutParams(ImageViewImageParam);
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

            GlideApp.with(GetActivity()).load(SelectImage.get(Position)).centerCrop().thumbnail(0.1f).into(ImageViewImage);

            RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(Misc.ToDP(30), Misc.ToDP(30));
            ImageViewRemoveParam.setMargins(0, Misc.ToDP(5), Misc.ToDP(20), 0);
            ImageViewRemoveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewRemove = new ImageView(GetActivity());
            ImageViewRemove.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewRemove.setLayoutParams(ImageViewRemoveParam);
            ImageViewRemove.setAlpha(0.75f);
            ImageViewRemove.setImageResource(R.drawable.close_bluegray);
            ImageViewRemove.setId(Misc.ViewID());
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

            RelativeLayout.LayoutParams ImageViewCropParam = new RelativeLayout.LayoutParams(Misc.ToDP(34), Misc.ToDP(34));
            ImageViewCropParam.setMargins(0, Misc.ToDP(5), Misc.ToDP(20), 0);
            ImageViewCropParam.addRule(RelativeLayout.BELOW, ImageViewRemove.getId());
            ImageViewCropParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewCrop = new ImageView(GetActivity());
            ImageViewCrop.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewCrop.setLayoutParams(ImageViewCropParam);
            ImageViewCrop.setImageResource(R.drawable.crop_white);
            ImageViewCrop.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    GetActivity().GetManager().OpenView(new CropViewUI(SelectImage.get(Position), new CropViewUI.OnCropListener()
                    {
                        @Override
                        public void OnCrop(String Path)
                        {
                            SelectImage.remove(Position);
                            SelectImage.add(Path);
                            ViewPagerAdapterImage.notifyDataSetChanged();
                        }
                    }), R.id.ContainerFull, "CropViewUI");
                }
            });

            RelativeLayoutMain.addView(ImageViewCrop);

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
        private final int ID_ICON = Misc.ViewID();
        private final int ID_NAME = Misc.ViewID();
        private final int ID_LINE = Misc.ViewID();

        AdapterCategory()
        {
            CategoryList.clear();
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryNews), !Misc.IsDark() ? R.drawable.news_black : R.drawable.news_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryFun), !Misc.IsDark() ? R.drawable.fun_black : R.drawable.fun_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryMusic), !Misc.IsDark() ? R.drawable.music_black : R.drawable.music_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategorySport), !Misc.IsDark() ? R.drawable.sport_black : R.drawable.sport_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryFashion), !Misc.IsDark() ? R.drawable.fashion_black : R.drawable.fashion_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryFood), !Misc.IsDark() ? R.drawable.food_black : R.drawable.food_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryTechnology), !Misc.IsDark() ? R.drawable.technology_black : R.drawable.technology_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryArt), !Misc.IsDark() ? R.drawable.art_black : R.drawable.art_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryArtist), !Misc.IsDark() ? R.drawable.artist_black : R.drawable.artist_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryMedia), !Misc.IsDark() ? R.drawable.media_black : R.drawable.media_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryBusiness), !Misc.IsDark() ? R.drawable.business_black : R.drawable.business_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryEconomy), !Misc.IsDark() ? R.drawable.echonomy_black : R.drawable.echonomy_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryLiterature), !Misc.IsDark() ? R.drawable.lilterature_black : R.drawable.lilterature_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryTravel), !Misc.IsDark() ? R.drawable.travel_black : R.drawable.travel_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryPolitics), !Misc.IsDark() ? R.drawable.politics_black : R.drawable.politics_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryHealth), !Misc.IsDark() ? R.drawable.health_black : R.drawable.health_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryReligious), !Misc.IsDark() ? R.drawable.religious_black : R.drawable.religious_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryKnowledge), !Misc.IsDark() ? R.drawable.knowledge_black : R.drawable.knowledge_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryNature), !Misc.IsDark() ? R.drawable.nature_black : R.drawable.nature_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryWeather), !Misc.IsDark() ? R.drawable.weather_black : R.drawable.weather_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryHistorical), !Misc.IsDark() ? R.drawable.historical_black : R.drawable.historical_white));
            CategoryList.add(new Struct(GetActivity().getString(R.string.CategoryOther), !Misc.IsDark() ? R.drawable.other_black : R.drawable.other_white));
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

            RelativeLayout.LayoutParams ImageViewIconParam = new RelativeLayout.LayoutParams(Misc.ToDP(45), Misc.ToDP(45));
            ImageViewIconParam.addRule(Misc.Align("R"));

            ImageView ImageViewIcon = new ImageView(GetActivity());
            ImageViewIcon.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
            ImageViewIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageViewIcon.setLayoutParams(ImageViewIconParam);
            ImageViewIcon.setId(ID_ICON);

            RelativeLayoutMain.addView(ImageViewIcon);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(Misc.AlignTo("R"), ID_ICON);
            TextViewNameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            TextView TextViewName = new TextView(GetActivity(), 16, false);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
            TextViewName.setId(ID_NAME);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
            ViewLineParam.addRule(RelativeLayout.BELOW, ID_ICON);

            View ViewLine = new View(GetActivity());
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
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
