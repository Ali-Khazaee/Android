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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.util.TypedValue;
import android.view.Gravity;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import co.biogram.main.ui.view.ScrollNumber;
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
    private ViewPagerAdapter AdapterImage;
    private ViewPager ViewPagerImage;

    private List<String> SelectImage = new ArrayList<>();
    private int SelectCategory = 0;
    private int SelectType = 0;
    private File SelectFile;
    private File SelectVideo;
    private long VoteTime = 0;
    private int IsWorld = 0;

    @Override
    public void OnCreate()
    {
        final TextView TextViewSelect = new TextView(Activity, 16, false);
        final TextView TextViewCount = new TextView(Activity, 14, false);
        final RelativeLayout RelativeLayoutVideo = new RelativeLayout(Activity);
        final ImageView ImageViewThumb = new ImageView(Activity);
        final TextView TextViewSize = new TextView(Activity, 12, false);
        final TextView TextViewLength = new TextView(Activity, 12, false);
        final RelativeLayout RelativeLayoutFile = new RelativeLayout(Activity);
        final TextView TextViewFileName = new TextView(Activity, 14, false);
        final TextView TextViewFileDetail = new TextView(Activity, 12, false);
        final ScrollView ScrollViewVote = new ScrollView(Activity);
        final ImageView ImageViewRemove = new ImageView(Activity);

        RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        LayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            int LastDiffHeight = 0;

            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DiffHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DiffHeight > ScreenHeight / 3 && DiffHeight != LastDiffHeight)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DiffHeight));
                    LastDiffHeight = DiffHeight;
                }
                else if (DiffHeight != LastDiffHeight)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    LastDiffHeight = DiffHeight;
                }
                else if (LastDiffHeight != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDiffHeight)));
                    LastDiffHeight = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_blue_rtl : R.drawable.back_blue);
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });
        ImageViewBack.setId(Misc.ViewID());

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setText(Misc.String(R.string.WriteUI));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewWorldParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWorldParam.addRule(Misc.Align("L"));

        ImageView ImageViewWorld = new ImageView(Activity);
        ImageViewWorld.setLayoutParams(ImageViewWorldParam);
        ImageViewWorld.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewWorld.setImageResource(R.drawable._write_global_gray);
        ImageViewWorld.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsWorld == 0)
                {
                    IsWorld = 1;
                    ((ImageView) v).setImageResource(R.drawable._write_global_blue);
                    Misc.Toast(Misc.String(R.string.WriteUIWorld));
                }
                else
                {
                    IsWorld = 0;
                    ((ImageView) v).setImageResource(R.drawable._write_global_gray);
                    Misc.Toast(Misc.String(R.string.WriteUIWorld2));
                }
            }
        });

        RelativeLayoutHeader.addView(ImageViewWorld);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams EditTextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextMessageParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final EditText EditTextMessage = new EditText(Activity);
        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextMessage.setId(Misc.ViewID());
        EditTextMessage.setMaxLines(5);
        EditTextMessage.setHint(R.string.WriteUIMessage);
        EditTextMessage.setBackground(null);
        EditTextMessage.setTypeface(FontHandler.GetTypeface(Activity));
        EditTextMessage.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextMessage.setHintTextColor(Misc.Color(Misc.IsDark() ? R.color.Gray : R.color.Gray));
        EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(300) });
        EditTextMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        EditTextMessage.setCustomSelectionActionModeCallback(null);
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
                final Dialog DialogOption = new Dialog(Activity);
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(56));
                TextViewTitleParam.addRule(Misc.Align("R"));

                TextView TextViewTitle = new TextView(Activity, 16, false);
                TextViewTitle.setLayoutParams(TextViewTitleParam);
                TextViewTitle.setText(Misc.String(R.string.WriteUIOptions));
                TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);
                TextViewTitle.SetColor(R.color.TextWhite);

                RelativeLayoutHeader.addView(TextViewTitle);

                RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewCloseParam.addRule(Misc.Align("L"));

                ImageView ImageViewClose = new ImageView(Activity);
                ImageViewClose.setLayoutParams(ImageViewCloseParam);
                ImageViewClose.setImageResource(R.drawable.close_blue);
                ImageViewClose.setPadding(Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7));
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogOption.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewClose);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine);

                TextView TextViewDelete = new TextView(Activity, 14, false);
                TextViewDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewDelete.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewDelete.setText(Misc.String(R.string.WriteUIDelete));
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

                View DeleteLine = new View(Activity);
                DeleteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                DeleteLine.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(DeleteLine);

                TextView TextViewPaste = new TextView(Activity, 14, false);
                TextViewPaste.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewPaste.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewPaste.setText(Misc.String(R.string.WriteUIPaste));
                TextViewPaste.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewPaste.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) Activity.getSystemService(Context.CLIPBOARD_SERVICE);

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

                View PasteLine = new View(Activity);
                PasteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                PasteLine.setBackgroundResource(R.color.LineWhite);

                LinearLayoutMain.addView(PasteLine);

                TextView TextViewCopy = new TextView(Activity, 14, false);
                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCopy.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewCopy.setText(Misc.String(R.string.WriteUICopy));
                TextViewCopy.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
                TextViewCopy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("WriteMessage", EditTextMessage.getText().toString());

                        if (clipboard != null)
                            clipboard.setPrimaryClip(clip);

                        Misc.Toast(Misc.String(R.string.WriteUIClipboard));
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

        LinearLayout LinearLayoutBottom = new LinearLayout(Activity);
        LinearLayoutBottom.setLayoutParams(LinearLayoutBottomParam);
        LinearLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutBottom.setId(Misc.ViewID());

        RelativeLayoutMain.addView(LinearLayoutBottom);

        ImageViewImage = new ImageView(Activity);
        ImageViewImage.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        ImageViewImage.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewImage.setImageResource(Misc.IsDark() ? R.drawable.camera_gray : R.drawable.camera_bluegray);
        ImageViewImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (SelectImage.size() >= 3)
                {
                    Misc.Toast(Misc.String(R.string.WriteUIMaximumImage));
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

                                    for (String I : ImageURL)
                                    {
                                        BitmapFactory.Options O = new BitmapFactory.Options();
                                        O.inJustDecodeBounds = true;

                                        BitmapFactory.decodeFile(I, O);

                                        O.inSampleSize = Misc.SampleSize(O, Size, Size);
                                        O.inJustDecodeBounds = false;

                                        Bitmap bitmap = BitmapFactory.decodeFile(I, O);

                                        File file = new File(CacheHandler.TempDir(Activity), System.currentTimeMillis() + ".jpg");
                                        file.createNewFile();

                                        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                                        FileOutputStream FOS = new FileOutputStream(file);
                                        FOS.write(BAOS.toByteArray());
                                        FOS.flush();
                                        FOS.close();

                                        SelectImage.add(file.getAbsolutePath());
                                    }

                                    Misc.UIThread(new Runnable() { @Override public void run() { AdapterImage.notifyDataSetChanged(); } }, 2);
                                }
                                catch (Exception e)
                                {
                                    Misc.Debug("WriteUI-Compress: " + e.toString());
                                }
                            }
                        });
                    }
                };

                if (Misc.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Activity.GetManager().OpenView(new GalleryViewUI(3, 1, L), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(Activity);
                PermissionDialogGallery.SetContentView(R.drawable.permission_storage_white, Misc.String(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(Misc.String(R.string.PermissionStorage));
                            return;
                        }

                        Activity.RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnResult(boolean Granted)
                            {
                                if (Granted)
                                    Activity.GetManager().OpenView(new GalleryViewUI(3, 1, L), R.id.ContainerFull, "GalleryViewUI");
                                else
                                    Misc.Toast(Misc.String(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        ImageViewVideo = new ImageView(Activity);
        ImageViewVideo.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
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
                                Misc.Toast(Misc.String(R.string.WriteUIVideoLength));
                                return;
                            }

                            ChangeType(2);
                            SelectVideo = new File(VideoURL);
                            ImageViewThumb.setImageBitmap(Retriever.getFrameAtTime(100));
                            RelativeLayoutVideo.setVisibility(View.VISIBLE);

                            TextViewSize.setText((new DecimalFormat("#.##").format((double) SelectVideo.length() / 1048576.0) + " " + Misc.String(R.string.WriteUIMB)));
                            TextViewLength.setText((String.valueOf(Time) + " " + Misc.String(R.string.WriteUISeconds)));
                        }
                    }
                };

                if (Misc.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Activity.GetManager().OpenView(new GalleryViewUI(1, 2, L), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(Activity);
                PermissionDialogGallery.SetContentView(R.drawable.permission_storage_white, Misc.String(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(Misc.String(R.string.PermissionStorage));
                            return;
                        }

                        Activity.RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnResult(boolean Granted)
                            {
                                if (Granted)
                                    Activity.GetManager().OpenView(new GalleryViewUI(1, 2, L), R.id.ContainerFull, "GalleryViewUI");
                                else
                                    Misc.Toast(Misc.String(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        ImageViewVote = new ImageView(Activity);
        ImageViewVote.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
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

        ImageViewFile = new ImageView(Activity);
        ImageViewFile.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
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
                        ChangeType(4);
                        SelectFile = new File(URL);
                        RelativeLayoutFile.setVisibility(View.VISIBLE);
                        TextViewFileName.setText(SelectFile.getName().length() <= 25 ? SelectFile.getName() : SelectFile.getName().substring(0, Math.min(SelectFile.getName().length(), 25)) + "...");
                        TextViewFileDetail.setText((new DecimalFormat("#.##").format((double) SelectFile.length() / 1048576.0) + " " + Misc.String(R.string.WriteUIMB) + " / " + SelectFile.getName().substring(SelectFile.getName().lastIndexOf(".")).substring(1).toUpperCase()));
                    }

                    @Override public void OnRemove(String URL) { }
                    @Override public void OnSave() { }
                };

                if (Misc.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Activity.GetManager().OpenView(new GalleryViewUI(1, 3, L), R.id.ContainerFull, "GalleryViewUI");
                    return;
                }

                PermissionDialog PermissionDialogGallery = new PermissionDialog(Activity);
                PermissionDialogGallery.SetContentView(R.drawable.permission_storage_white, Misc.String(R.string.WriteUIPermissionStorage), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            Misc.Toast(Misc.String(R.string.PermissionStorage));
                            return;
                        }

                        Activity.RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnResult(boolean Granted)
                            {
                                if (Granted)
                                    Activity.GetManager().OpenView(new GalleryViewUI(1, 3, L), R.id.ContainerFull, "GalleryViewUI");
                                else
                                    Misc.Toast(Misc.String(R.string.PermissionStorage));
                            }
                        });
                    }
                });
            }
        });

        TextViewCount.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        TextViewCount.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewCount.setGravity(Gravity.CENTER);
        TextViewCount.SetColor(R.color.Gray);
        TextViewCount.setText(("300"));

        TextView TextViewSend = new TextView(Activity, 14, true);
        TextViewSend.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(56), 1.0f));
        TextViewSend.setText(Misc.String(R.string.WriteUISend));
        TextViewSend.setGravity(Gravity.CENTER);
        TextViewSend.SetColor(R.color.Primary);

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

        View ViewLine2 = new View(Activity);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine2.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(40));
        LinearLayoutCategoryParam.setMargins(Misc.ToDP(5), 0, Misc.ToDP(5), Misc.ToDP(3));
        LinearLayoutCategoryParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

        LinearLayout LinearLayoutCategory = new LinearLayout(Activity);
        LinearLayoutCategory.setLayoutParams(LinearLayoutCategoryParam);
        LinearLayoutCategory.setId(Misc.ViewID());
        LinearLayoutCategory.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCategory.setGravity(Misc.IsRTL() ? Gravity.END : Gravity.START);
        LinearLayoutCategory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogCategory = new Dialog(Activity);
                DialogCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCategory.setCancelable(false);

                RelativeLayout LinearLayoutMain = new RelativeLayout(Activity);
                LinearLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
                RelativeLayoutHeader.setId(Misc.ViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewBackParam.addRule(Misc.Align("L"));

                ImageView ImageViewBack = new ImageView(Activity);
                ImageViewBack.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.close_blue);
                ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogCategory.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(Misc.Align("R"));
                TextViewNameParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewNameParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);

                TextView TextViewName = new TextView(Activity, 16, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewName.setText(Misc.String(R.string.WriteUICategory));

                RelativeLayoutHeader.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
                ViewLine.setId(Misc.ViewID());

                LinearLayoutMain.addView(ViewLine);

                RelativeLayout.LayoutParams RecyclerViewCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RecyclerViewCategoryParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                RecyclerView RecyclerViewCategory = new RecyclerView(Activity);
                RecyclerViewCategory.setLayoutManager(new LinearLayoutManager(Activity));
                RecyclerViewCategory.setAdapter(new AdapterCategory());
                RecyclerViewCategory.setLayoutParams(RecyclerViewCategoryParam);
                RecyclerViewCategory.addOnItemTouchListener(new OnClickRecyclerView(Activity, new OnClickRecyclerView.OnItemClickListener()
                {
                    @Override
                    public void OnClick(int Position)
                    {
                        switch (Position)
                        {
                            case 0:  TextViewSelect.setText(Misc.String(R.string.CategoryNews));       SelectCategory = 1;  break;
                            case 1:  TextViewSelect.setText(Misc.String(R.string.CategoryFun));        SelectCategory = 2;  break;
                            case 2:  TextViewSelect.setText(Misc.String(R.string.CategoryMusic));      SelectCategory = 3;  break;
                            case 3:  TextViewSelect.setText(Misc.String(R.string.CategorySport));      SelectCategory = 4;  break;
                            case 4:  TextViewSelect.setText(Misc.String(R.string.CategoryFashion));    SelectCategory = 5;  break;
                            case 5:  TextViewSelect.setText(Misc.String(R.string.CategoryFood));       SelectCategory = 6;  break;
                            case 6:  TextViewSelect.setText(Misc.String(R.string.CategoryTechnology)); SelectCategory = 7;  break;
                            case 7:  TextViewSelect.setText(Misc.String(R.string.CategoryArt));        SelectCategory = 8;  break;
                            case 8:  TextViewSelect.setText(Misc.String(R.string.CategoryArtist));     SelectCategory = 9;  break;
                            case 9:  TextViewSelect.setText(Misc.String(R.string.CategoryMedia));      SelectCategory = 10; break;
                            case 10: TextViewSelect.setText(Misc.String(R.string.CategoryBusiness));   SelectCategory = 11; break;
                            case 11: TextViewSelect.setText(Misc.String(R.string.CategoryEconomy));    SelectCategory = 12; break;
                            case 12: TextViewSelect.setText(Misc.String(R.string.CategoryLiterature)); SelectCategory = 13; break;
                            case 13: TextViewSelect.setText(Misc.String(R.string.CategoryTravel));     SelectCategory = 14; break;
                            case 14: TextViewSelect.setText(Misc.String(R.string.CategoryPolitics));   SelectCategory = 15; break;
                            case 15: TextViewSelect.setText(Misc.String(R.string.CategoryHealth));     SelectCategory = 16; break;
                            case 16: TextViewSelect.setText(Misc.String(R.string.CategoryReligious));  SelectCategory = 17; break;
                            case 17: TextViewSelect.setText(Misc.String(R.string.CategoryKnowledge));  SelectCategory = 18; break;
                            case 18: TextViewSelect.setText(Misc.String(R.string.CategoryNature));     SelectCategory = 19; break;
                            case 19: TextViewSelect.setText(Misc.String(R.string.CategoryWeather));    SelectCategory = 20; break;
                            case 20: TextViewSelect.setText(Misc.String(R.string.CategoryHistorical)); SelectCategory = 21; break;
                            case 21: TextViewSelect.setText(Misc.String(R.string.CategoryRomantic));   SelectCategory = 22; break;
                            default: TextViewSelect.setText(Misc.String(R.string.CategoryOther));      SelectCategory = 100; break;
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

        ImageView ImageViewCategory = new ImageView(Activity);
        ImageViewCategory.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(40), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewCategory.setImageResource(R.drawable._write_category_blue);

        TextView TextViewCategory = new TextView(Activity, 16, false);
        TextViewCategory.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        TextViewCategory.setText(Misc.String(R.string.WriteUICategory2));
        TextViewCategory.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewCategory.setGravity(Gravity.CENTER);
        TextViewCategory.SetColor(R.color.Primary);

        TextViewSelect.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        TextViewSelect.setText(Misc.String(R.string.WriteUICategoryNone));
        TextViewSelect.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), 0);
        TextViewSelect.setGravity(Gravity.CENTER);
        TextViewSelect.SetColor(R.color.Primary);

        ImageView ImageViewArrow = new ImageView(Activity);
        ImageViewArrow.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(20), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewArrow.setImageResource(R.drawable._write_arrow);

        if (Misc.IsRTL())
        {
            LinearLayoutCategory.addView(ImageViewArrow);
            LinearLayoutCategory.addView(TextViewSelect);
            LinearLayoutCategory.addView(TextViewCategory);
            LinearLayoutCategory.addView(ImageViewCategory);
        }
        else
        {
            LinearLayoutCategory.addView(ImageViewCategory);
            LinearLayoutCategory.addView(TextViewCategory);
            LinearLayoutCategory.addView(TextViewSelect);
            LinearLayoutCategory.addView(ImageViewArrow);
        }

        RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutContentParam.addRule(RelativeLayout.ABOVE, LinearLayoutCategory.getId());
        RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextMessage.getId());

        RelativeLayout RelativeLayoutContent = new RelativeLayout(Activity);
        RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

        RelativeLayoutMain.addView(RelativeLayoutContent);

        ScrollViewVote.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));
        ScrollViewVote.setVisibility(View.GONE);

        RelativeLayoutContent.addView(ScrollViewVote);

        RelativeLayout RelativeLayoutVote = new RelativeLayout(Activity);
        RelativeLayoutVote.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewVote.addView(RelativeLayoutVote);

        RelativeLayout.LayoutParams ImageViewCloseVoteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewCloseVoteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewClose = new ImageView(Activity);
        ImageViewClose.setLayoutParams(ImageViewCloseVoteParam);
        ImageViewClose.setImageResource(R.drawable._write_remove);
        ImageViewClose.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
        ImageViewClose.setId(Misc.ViewID());
        ImageViewClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeType(0);
                ScrollViewVote.setVisibility(View.GONE);
            }
        });

        RelativeLayoutVote.addView(ImageViewClose);

        final GradientDrawable Enable = new GradientDrawable();
        Enable.setStroke(Misc.ToDP(1), Misc.Color(R.color.Primary));
        Enable.setCornerRadius(Misc.ToDP(4));

        final GradientDrawable Disable = new GradientDrawable();
        Disable.setStroke(Misc.ToDP(1), Misc.Color(R.color.Gray));
        Disable.setCornerRadius(Misc.ToDP(4));

        View.OnFocusChangeListener OnFocus = new View.OnFocusChangeListener() { @Override public void onFocusChange(View view, boolean hasFocus) { view.setBackground(hasFocus ? Enable : Disable); } };

        RelativeLayout.LayoutParams EditTextVote1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote1Param.addRule(RelativeLayout.LEFT_OF, ImageViewClose.getId());
        EditTextVote1Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));

        final EditText EditTextVote1 = new EditText(Activity);
        EditTextVote1.setLayoutParams(EditTextVote1Param);
        EditTextVote1.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote1.setId(Misc.ViewID());
        EditTextVote1.setHint(R.string.WriteUIChoice1);
        EditTextVote1.setBackground(Disable);
        EditTextVote1.setHintTextColor(Misc.Color(R.color.Gray));
        EditTextVote1.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(24) });
        EditTextVote1.setOnFocusChangeListener(OnFocus);

        RelativeLayoutVote.addView(EditTextVote1);

        RelativeLayout.LayoutParams EditTextVote2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote2Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote2Param.addRule(RelativeLayout.LEFT_OF, ImageViewClose.getId());
        EditTextVote2Param.addRule(RelativeLayout.BELOW, EditTextVote1.getId());

        final EditText EditTextVote2 = new EditText(Activity);
        EditTextVote2.setLayoutParams(EditTextVote2Param);
        EditTextVote2.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote2.setId(Misc.ViewID());
        EditTextVote2.setHint(R.string.WriteUIChoice2);
        EditTextVote2.setBackground(Disable);
        EditTextVote2.setHintTextColor(Misc.Color(R.color.Gray));
        EditTextVote2.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(24) });
        EditTextVote2.setOnFocusChangeListener(OnFocus);

        RelativeLayoutVote.addView(EditTextVote2);

        RelativeLayout.LayoutParams EditTextVote3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote3Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote3Param.addRule(RelativeLayout.LEFT_OF, ImageViewClose.getId());
        EditTextVote3Param.addRule(RelativeLayout.BELOW, EditTextVote2.getId());

        final EditText EditTextVote3 = new EditText(Activity);
        EditTextVote3.setLayoutParams(EditTextVote3Param);
        EditTextVote3.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote3.setId(Misc.ViewID());
        EditTextVote3.setHint(R.string.WriteUIChoice3);
        EditTextVote3.setBackground(Disable);
        EditTextVote3.setHintTextColor(Misc.Color(R.color.Gray));
        EditTextVote3.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(24) });
        EditTextVote3.setOnFocusChangeListener(OnFocus);
        EditTextVote3.setVisibility(View.GONE);

        RelativeLayoutVote.addView(EditTextVote3);

        RelativeLayout.LayoutParams EditTextVote4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote4Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote4Param.addRule(RelativeLayout.LEFT_OF, ImageViewClose.getId());
        EditTextVote4Param.addRule(RelativeLayout.BELOW, EditTextVote3.getId());

        final EditText EditTextVote4 = new EditText(Activity);
        EditTextVote4.setLayoutParams(EditTextVote4Param);
        EditTextVote4.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote4.setHint(R.string.WriteUIChoice4);
        EditTextVote4.setBackground(Disable);
        EditTextVote4.setHintTextColor(Misc.Color(R.color.Gray));
        EditTextVote4.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        EditTextVote4.setOnFocusChangeListener(OnFocus);
        EditTextVote4.setVisibility(View.GONE);
        EditTextVote4.setId(Misc.ViewID());

        RelativeLayoutVote.addView(EditTextVote4);

        RelativeLayout.LayoutParams EditTextVote5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        EditTextVote5Param.setMargins(Misc.ToDP(15), 0, Misc.ToDP(5), Misc.ToDP(5));
        EditTextVote5Param.addRule(RelativeLayout.LEFT_OF, ImageViewClose.getId());
        EditTextVote5Param.addRule(RelativeLayout.BELOW, EditTextVote4.getId());

        final EditText EditTextVote5 = new EditText(Activity);
        EditTextVote5.setLayoutParams(EditTextVote5Param);
        EditTextVote5.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextVote5.setHint(R.string.WriteUIChoice5);
        EditTextVote5.setBackground(Disable);
        EditTextVote5.setHintTextColor(Misc.Color(R.color.Gray));
        EditTextVote5.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextVote5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextVote5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(24) });
        EditTextVote5.setOnFocusChangeListener(OnFocus);
        EditTextVote5.setVisibility(View.GONE);
        EditTextVote5.setId(Misc.ViewID());

        RelativeLayoutVote.addView(EditTextVote5);

        RelativeLayout.LayoutParams TextViewLengthParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLengthParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), 0);
        TextViewLengthParam.addRule(RelativeLayout.BELOW, EditTextVote5.getId());

        final TextView TextViewLengthVote = new TextView(Activity, 14, false);
        TextViewLengthVote.setLayoutParams(TextViewLengthParam);
        TextViewLengthVote.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewLengthVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogLength = new Dialog(Activity);
                DialogLength.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLength.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
                TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewTitleParam.addRule(Misc.Align("R"));

                TextView TextViewTitle = new TextView(Activity, 16, false);
                TextViewTitle.setLayoutParams(TextViewTitleParam);
                TextViewTitle.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), 0);
                TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewTitle.setText(Misc.String(R.string.WriteUILength2));

                LinearLayoutMain.addView(TextViewTitle);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine);

                View ViewLine1 = new View(Activity);
                ViewLine1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(15)));

                LinearLayoutMain.addView(ViewLine1);

                LinearLayout LinearLayoutTime = new LinearLayout(Activity);
                LinearLayoutTime.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutTime.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayoutMain.addView(LinearLayoutTime);

                TextView TextViewDays = new TextView(Activity, 14, false);
                TextViewDays.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextViewDays.SetColor(R.color.TextWhite);
                TextViewDays.setText(Misc.String(R.string.WriteUILengthDays));
                TextViewDays.setGravity(Gravity.CENTER);

                LinearLayoutTime.addView(TextViewDays);

                TextView TextViewHours = new TextView(Activity, 14, false);
                TextViewHours.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextViewHours.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewHours.setText(Misc.String(R.string.WriteUILengthHours));
                TextViewHours.setGravity(Gravity.CENTER);

                LinearLayoutTime.addView(TextViewHours);

                TextView TextViewMins = new TextView(Activity, 14, false);
                TextViewMins.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextViewMins.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewMins.setText(Misc.String(R.string.WriteUILengthMins));
                TextViewMins.setGravity(Gravity.CENTER);

                LinearLayoutTime.addView(TextViewMins);

                LinearLayout LinearLayoutTime2 = new LinearLayout(Activity);
                LinearLayoutTime2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutTime2.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayoutMain.addView(LinearLayoutTime2);

                final ScrollNumber Days = new ScrollNumber(Activity);
                Days.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(105), 1.0f));
                Days.SetValues(1, 365);

                LinearLayoutTime2.addView(Days);

                final ScrollNumber Hours = new ScrollNumber(Activity);
                Hours.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(105), 1.0f));
                Hours.SetValues(0, 23);

                LinearLayoutTime2.addView(Hours);

                final ScrollNumber Mins = new ScrollNumber(Activity);
                Mins.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(105), 1.0f));
                Mins.SetValues(0, 59);

                LinearLayoutTime2.addView(Mins);

                View ViewLine2 = new View(Activity);
                ViewLine2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine2.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);

                LinearLayoutMain.addView(ViewLine2);

                LinearLayout LinearLayoutButton = new LinearLayout(Activity);
                LinearLayoutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                LinearLayoutButton.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayoutMain.addView(LinearLayoutButton);

                TextView TextViewCancel = new TextView(Activity, 14, false);
                TextViewCancel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                TextViewCancel.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewCancel.setText(Misc.String(R.string.WriteUILengthCancel));
                TextViewCancel.setGravity(Gravity.CENTER);
                TextViewCancel.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogLength.dismiss(); } });

                TextView TextViewSet = new TextView(Activity, 14, false);
                TextViewSet.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                TextViewSet.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewSet.setText(Misc.String(R.string.WriteUILengthSet));
                TextViewSet.setGravity(Gravity.CENTER);
                TextViewSet.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        VoteTime = (System.currentTimeMillis() + (((Days.GetValue() * 86400) +  (Hours.GetValue() * 3600) + (Mins.GetValue() * 60)) * 1000));
                        TextViewLengthVote.setText((Misc.String(R.string.WriteUILength) + " " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(VoteTime)), TextView.BufferType.SPANNABLE);

                        Spannable Span = (Spannable) TextViewLengthVote.getText();
                        CharacterStyle SpanMessage = new CharacterStyle()
                        {
                            @Override
                            public void updateDrawState(TextPaint t)
                            {
                                t.setColor(Misc.Color(R.color.Primary));
                            }
                        };
                        Span.setSpan(SpanMessage, Misc.String(R.string.WriteUILength).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        DialogLength.dismiss();
                    }
                });

                if (Misc.IsRTL())
                {
                    LinearLayoutButton.addView(TextViewSet);
                    LinearLayoutButton.addView(TextViewCancel);
                }
                else
                {
                    LinearLayoutButton.addView(TextViewCancel);
                    LinearLayoutButton.addView(TextViewSet);
                }

                DialogLength.setContentView(LinearLayoutMain);
                DialogLength.show();
            }
        });

        TextViewLengthVote.setText((Misc.String(R.string.WriteUILength) + " " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())), TextView.BufferType.SPANNABLE);

        Spannable Span = (Spannable) TextViewLengthVote.getText();
        CharacterStyle SpanMessage = new CharacterStyle()
        {
            @Override
            public void updateDrawState(TextPaint t)
            {
                t.setColor(Misc.Color(R.color.Primary));
            }
        };
        Span.setSpan(SpanMessage, Misc.String(R.string.WriteUILength).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutVote.addView(TextViewLengthVote);

        RelativeLayout.LayoutParams ImageViewAddParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewAddParam.addRule(RelativeLayout.BELOW, EditTextVote1.getId());
        ImageViewAddParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewAdd = new ImageView(Activity);
        ImageViewAdd.setLayoutParams(ImageViewAddParam);
        ImageViewAdd.setImageResource(R.drawable._write_addition);
        ImageViewAdd.setPadding(Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17));
        ImageViewAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageViewRemove.setVisibility(View.VISIBLE);

                if (EditTextVote3.getVisibility() != View.VISIBLE)
                    EditTextVote3.setVisibility(View.VISIBLE);
                else if (EditTextVote4.getVisibility() != View.VISIBLE)
                    EditTextVote4.setVisibility(View.VISIBLE);
                else if (EditTextVote5.getVisibility() != View.VISIBLE)
                    EditTextVote5.setVisibility(View.VISIBLE);
            }
        });

        RelativeLayoutVote.addView(ImageViewAdd);

        RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewRemoveParam.addRule(RelativeLayout.BELOW, EditTextVote2.getId());
        ImageViewRemoveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageViewRemove.setLayoutParams(ImageViewRemoveParam);
        ImageViewRemove.setImageResource(R.drawable._write_negative);
        ImageViewRemove.setPadding(Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17));
        ImageViewRemove.setVisibility(View.GONE);
        ImageViewRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (EditTextVote5.getVisibility() == View.VISIBLE)
                    EditTextVote5.setVisibility(View.GONE);
                else if (EditTextVote4.getVisibility() == View.VISIBLE)
                    EditTextVote4.setVisibility(View.GONE);
                else if (EditTextVote3.getVisibility() == View.VISIBLE)
                {
                    v.setVisibility(View.GONE);
                    EditTextVote3.setVisibility(View.GONE);
                }
            }
        });

        RelativeLayoutVote.addView(ImageViewRemove);

        RelativeLayout.LayoutParams RelativeLayoutFileParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(80));
        RelativeLayoutFileParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
        RelativeLayoutFileParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        GradientDrawable Border = new GradientDrawable();
        Border.setStroke(Misc.ToDP(1), Misc.Color(R.color.LineWhite));
        Border.setCornerRadius(Misc.ToDP(6));

        RelativeLayoutFile.setLayoutParams(RelativeLayoutFileParam);
        RelativeLayoutFile.setVisibility(View.GONE);
        RelativeLayoutFile.setBackground(Border);

        RelativeLayoutContent.addView(RelativeLayoutFile);

        RelativeLayout.LayoutParams ImageViewFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(60), Misc.ToDP(60));
        ImageViewFileParam.setMargins(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        ImageViewFileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        GradientDrawable DrawableFile = new GradientDrawable();
        DrawableFile.setColor(Misc.Color(R.color.Primary));
        DrawableFile.setShape(GradientDrawable.OVAL);

        ImageView ImageViewFile = new ImageView(Activity);
        ImageViewFile.setLayoutParams(ImageViewFileParam);
        ImageViewFile.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewFile.setImageResource(R.drawable._general_download);
        ImageViewFile.setId(Misc.ViewID());
        ImageViewFile.setBackground(DrawableFile);

        RelativeLayoutFile.addView(ImageViewFile);

        RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewFile.getId());
        TextViewFileNameParam.setMargins(0, Misc.ToDP(12), 0, 0);

        TextViewFileName.setLayoutParams(TextViewFileNameParam);
        TextViewFileName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewFileName.setId(Misc.ViewID());

        RelativeLayoutFile.addView(TextViewFileName);

        RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, ImageViewFile.getId());
        TextViewFileDetailParam.addRule(RelativeLayout.BELOW, TextViewFileName.getId());

        TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
        TextViewFileDetail.setGravity(Gravity.LEFT);
        TextViewFileDetail.SetColor(R.color.Gray);

        RelativeLayoutFile.addView(TextViewFileDetail);

        RelativeLayout.LayoutParams ImageViewRemoveFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(25), Misc.ToDP(25));
        ImageViewRemoveFileParam.setMargins(0, Misc.ToDP(10), Misc.ToDP(10), 0);
        ImageViewRemoveFileParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewRemoveFile = new ImageView(Activity);
        ImageViewRemoveFile.setLayoutParams(ImageViewRemoveFileParam);
        ImageViewRemoveFile.setImageResource(R.drawable._write_remove);
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

        ViewPagerImage = new ViewPager(Activity);
        ViewPagerImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ViewPagerImage.setAdapter(AdapterImage = new ViewPagerAdapter());
        ViewPagerImage.setVisibility(View.GONE);

        RelativeLayoutContent.addView(ViewPagerImage);

        RelativeLayout.LayoutParams RelativeLayoutVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutVideoParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
        RelativeLayoutVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayoutVideo.setLayoutParams(RelativeLayoutVideoParam);
        RelativeLayoutVideo.setBackgroundColor(Color.BLACK);
        RelativeLayoutVideo.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutVideo);

        ImageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ImageViewThumb.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Activity.GetManager().OpenView(new VideoPreviewUI(SelectVideo.getAbsolutePath(), true, false), R.id.ContainerFull, "VideoPreviewUI"); } });
        ImageViewThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayoutVideo.addView(ImageViewThumb);

        RelativeLayout.LayoutParams ImageViewRemoveVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(25), Misc.ToDP(25));
        ImageViewRemoveVideoParam.setMargins(0, Misc.ToDP(10), Misc.ToDP(10), 0);
        ImageViewRemoveVideoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewRemoveVideo = new ImageView(Activity);
        ImageViewRemoveVideo.setLayoutParams(ImageViewRemoveVideoParam);
        ImageViewRemoveVideo.setImageResource(R.drawable._write_remove);
        ImageViewRemoveVideo.setId(Misc.ViewID());
        ImageViewRemoveVideo.setOnClickListener(new View.OnClickListener()
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

        RelativeLayoutVideo.addView(ImageViewRemoveVideo);

        RelativeLayout.LayoutParams ImageViewCompressParam = new RelativeLayout.LayoutParams(Misc.ToDP(25), Misc.ToDP(25));
        ImageViewCompressParam.setMargins(0, Misc.ToDP(10), Misc.ToDP(10), 0);
        ImageViewCompressParam.addRule(RelativeLayout.BELOW, ImageViewRemoveVideo.getId());
        ImageViewCompressParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewCompress = new ImageView(Activity);
        ImageViewCompress.setLayoutParams(ImageViewCompressParam);
        ImageViewCompress.setImageResource(R.drawable._write_compress);
        ImageViewCompress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Build.VERSION.SDK_INT <= 17)
                {
                    Misc.Toast(Misc.String(R.string.WriteUICantCompress));
                    return;
                }

                String OldPath = SelectVideo.getAbsolutePath();

                SelectVideo = new File(CacheHandler.TempDir(Activity), "video." + String.valueOf(System.currentTimeMillis()) + ".mp4");

                final ProgressDialog Progress = new ProgressDialog(Activity);
                Progress.setMessage(Misc.String(R.string.WriteUICompress));
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
                        Progress.cancel();

                        MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
                        Retriever.setDataSource(SelectVideo.getAbsolutePath());

                        TextViewSize.setText((new DecimalFormat("#.##").format((double) SelectVideo.length() / 1048576.0) + " " + Misc.String(R.string.WriteUIMB)));
                        ImageViewThumb.setImageBitmap(Retriever.getFrameAtTime(100));
                    }

                    @Override
                    public void OnFailed(Exception e)
                    {
                        Progress.cancel();
                        Misc.Debug("WriteUI-VideoCompress: " + e.toString());
                    }
                });
            }
        });

        RelativeLayoutVideo.addView(ImageViewCompress);

        GradientDrawable DrawableVideo = new GradientDrawable();
        DrawableVideo.setColor(Color.parseColor("#65000000"));
        DrawableVideo.setCornerRadius(Misc.ToDP(4));

        RelativeLayout.LayoutParams TextViewSizeVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSizeVideoParam.setMargins(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
        TextViewSizeVideoParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        TextViewSize.setLayoutParams(TextViewSizeVideoParam);
        TextViewSize.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), 0);
        TextViewSize.setBackground(DrawableVideo);
        TextViewSize.setId(Misc.ViewID());

        RelativeLayoutVideo.addView(TextViewSize);

        RelativeLayout.LayoutParams TextViewLengthVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLengthVideoParam.setMargins(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(8));
        TextViewLengthVideoParam.addRule(RelativeLayout.BELOW, TextViewSize.getId());

        TextViewLength.setLayoutParams(TextViewLengthVideoParam);
        TextViewLength.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), 0);
        TextViewLength.setBackground(DrawableVideo);

        RelativeLayoutVideo.addView(TextViewLength);

        RelativeLayout.LayoutParams ImageViewPlayVideoParam = new RelativeLayout.LayoutParams(Misc.ToDP(65), Misc.ToDP(65));
        ImageViewPlayVideoParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPlay = new ImageView(Activity);
        ImageViewPlay.setLayoutParams(ImageViewPlayVideoParam);
        ImageViewPlay.setImageResource(R.drawable._general_play);

        RelativeLayoutVideo.addView(ImageViewPlay);

        TextViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (SelectCategory == 0)
                {
                    Misc.Toast(Misc.String(R.string.WriteUIPickCategory));
                    return;
                }

                if (SelectType == 3 && (VoteTime == 0 || VoteTime < System.currentTimeMillis()))
                {
                    Misc.Toast(Misc.String(R.string.WriteUISetLength));
                    return;
                }

                if (EditTextMessage.getText().length() <= 30 && SelectType == 0)
                {
                    Misc.Toast(Misc.String(R.string.WriteUIStatement));
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
                        if (EditTextVote1.getText().toString().isEmpty() || EditTextVote2.getText().toString().isEmpty())
                        {
                            Misc.Toast(Misc.String(R.string.WriteUISetVote));
                            return;
                        }

                        Vote.put("Vote1", EditTextVote1.getText().toString());
                        Vote.put("Vote2", EditTextVote2.getText().toString());

                        if (EditTextVote3.getVisibility() == View.VISIBLE)
                            Vote.put("Vote3", EditTextVote3.getText().toString());

                        if (EditTextVote4.getVisibility() == View.VISIBLE)
                            Vote.put("Vote4", EditTextVote4.getText().toString());

                        if (EditTextVote5.getVisibility() == View.VISIBLE)
                            Vote.put("Vote5", EditTextVote5.getText().toString());

                        Vote.put("Time", (VoteTime / 1000));
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

                final ProgressDialog Progress = new ProgressDialog(Activity);
                Progress.setMessage(Misc.String(R.string.WriteUIUploading));
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
                .addHeaders("token", SharedHandler.GetString("Token"))
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
                                    InboxUI inboxUI = (InboxUI) Activity.GetManager().FindByTag("InboxUI");

                                    if (inboxUI != null)
                                        inboxUI.Update(Result.getJSONObject("Result"));

                                    Activity.onBackPressed();
                                break;
                                case 3:
                                    // TODO Add Message for all types
                                    Misc.Toast(Misc.String(R.string.WriteUISetVote));
                                    break;
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
                        Misc.Toast( Misc.String(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayout RelativeLayoutBack = new RelativeLayout(Activity);
        RelativeLayoutBack.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutBack.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutBack.setClickable(true);
        RelativeLayoutBack.addView(RelativeLayoutMain);

        ViewMain = RelativeLayoutBack;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(LayoutListener);
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("WriteUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            RelativeLayout.LayoutParams ImageViewImageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewImageParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);

            ImageView ImageViewImage = new ImageView(Activity);
            ImageViewImage.setLayoutParams(ImageViewImageParam);
            ImageViewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SelectImage.size() > 0)
                        Activity.GetManager().OpenView(new ImagePreviewUI(SelectImage.get(Position), false), R.id.ContainerFull, "ImagePreviewUI");
                }
            });

            RelativeLayoutMain.addView(ImageViewImage);

            GlideApp.with(Activity).load(SelectImage.get(Position)).centerCrop().thumbnail(0.1f).into(ImageViewImage);

            RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(Misc.ToDP(30), Misc.ToDP(30));
            ImageViewRemoveParam.setMargins(0, Misc.ToDP(5), Misc.ToDP(20), 0);
            ImageViewRemoveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewRemove = new ImageView(Activity);
            ImageViewRemove.setLayoutParams(ImageViewRemoveParam);
            ImageViewRemove.setImageResource(R.drawable._write_remove);
            ImageViewRemove.setId(Misc.ViewID());
            ImageViewRemove.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    SelectImage.remove(Position);
                    AdapterImage.notifyDataSetChanged();

                    if (SelectImage.size() <= 0)
                    {
                        ChangeType(0);
                        ViewPagerImage.setVisibility(View.GONE);
                    }
                }
            });

            RelativeLayoutMain.addView(ImageViewRemove);

            RelativeLayout.LayoutParams ImageViewCropParam = new RelativeLayout.LayoutParams(Misc.ToDP(30), Misc.ToDP(30));
            ImageViewCropParam.addRule(RelativeLayout.BELOW, ImageViewRemove.getId());
            ImageViewCropParam.setMargins(0, Misc.ToDP(5), Misc.ToDP(20), 0);
            ImageViewCropParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewCrop = new ImageView(Activity);
            ImageViewCrop.setLayoutParams(ImageViewCropParam);
            ImageViewCrop.setImageResource(R.drawable._write_crop);
            ImageViewCrop.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Activity.GetManager().OpenView(new CropViewUI(SelectImage.get(Position), new CropViewUI.OnCropListener()
                    {
                        @Override
                        public void OnCrop(String Path)
                        {
                            SelectImage.remove(Position);
                            SelectImage.add(Path);
                            AdapterImage.notifyDataSetChanged();
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
        private List<Struct> CategoryList = new ArrayList<>();
        private int ID_ICON = Misc.ViewID();
        private int ID_NAME = Misc.ViewID();
        private int ID_LINE = Misc.ViewID();

        AdapterCategory()
        {
            CategoryList.clear();
            CategoryList.add(new Struct(Misc.String(R.string.CategoryNews), !Misc.IsDark() ? R.drawable._category_news_black : R.drawable._category_news_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryFun), !Misc.IsDark() ? R.drawable._category_fun_black : R.drawable._category_fun_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryMusic), !Misc.IsDark() ? R.drawable._category_music_black : R.drawable._category_music_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategorySport), !Misc.IsDark() ? R.drawable._category_sport_black : R.drawable._category_sport_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryFashion), !Misc.IsDark() ? R.drawable._category_fashion_black : R.drawable._category_fashion_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryFood), !Misc.IsDark() ? R.drawable._category_food_black : R.drawable._category_food_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryTechnology), !Misc.IsDark() ? R.drawable._category_technology_black : R.drawable._category_technology_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryArt), !Misc.IsDark() ? R.drawable._category_art_black : R.drawable._category_art_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryArtist), !Misc.IsDark() ? R.drawable._category_artist_black : R.drawable._category_artist_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryMedia), !Misc.IsDark() ? R.drawable._category_media_black : R.drawable._category_media_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryBusiness), !Misc.IsDark() ? R.drawable._category_business_black : R.drawable._category_business_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryEconomy), !Misc.IsDark() ? R.drawable._category_echonomy_black : R.drawable._category_echonomy_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryLiterature), !Misc.IsDark() ? R.drawable._category_lilterature_black : R.drawable._category_lilterature_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryTravel), !Misc.IsDark() ? R.drawable._category_travel_black : R.drawable._category_travel_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryPolitics), !Misc.IsDark() ? R.drawable._category_politics_black : R.drawable._category_politics_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryHealth), !Misc.IsDark() ? R.drawable._category_health_black : R.drawable._category_health_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryReligious), !Misc.IsDark() ? R.drawable._category_religious_black : R.drawable._category_religious_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryKnowledge), !Misc.IsDark() ? R.drawable._category_knowledge_black : R.drawable._category_knowledge_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryNature), !Misc.IsDark() ? R.drawable._category_nature_black : R.drawable._category_nature_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryWeather), !Misc.IsDark() ? R.drawable._category_weather_black : R.drawable._category_weather_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryHistorical), !Misc.IsDark() ? R.drawable._category_historical_black : R.drawable._category_historical_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryRomantic), !Misc.IsDark() ? R.drawable._category_romantic_black : R.drawable._category_romantic_white));
            CategoryList.add(new Struct(Misc.String(R.string.CategoryOther), !Misc.IsDark() ? R.drawable._category_other_black : R.drawable._category_other_white));
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

            GlideApp.with(Activity).load(CategoryList.get(Position).Image).dontAnimate().into(Holder.ImageViewIcon);

            Holder.TextViewName.setText(CategoryList.get(Position).Name);

            if (Position == CategoryList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutMain.setClickable(true);

            RelativeLayout.LayoutParams ImageViewIconParam = new RelativeLayout.LayoutParams(Misc.ToDP(45), Misc.ToDP(45));
            ImageViewIconParam.addRule(Misc.Align("R"));

            ImageView ImageViewIcon = new ImageView(Activity);
            ImageViewIcon.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
            ImageViewIcon.setLayoutParams(ImageViewIconParam);
            ImageViewIcon.setId(ID_ICON);

            RelativeLayoutMain.addView(ImageViewIcon);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            TextViewNameParam.addRule(Misc.AlignTo("R"), ID_ICON);

            TextView TextViewName = new TextView(Activity, 16, false);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
            TextViewName.setId(ID_NAME);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
            ViewLineParam.addRule(RelativeLayout.BELOW, ID_ICON);

            View ViewLine = new View(Activity);
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
