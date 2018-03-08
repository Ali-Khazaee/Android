package co.biogram.main.ui.general;

import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;

import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.TextView;

public class GalleryViewUI extends FragmentView
{
    private final List<Struct> GalleryList = new ArrayList<>();
    private final List<String> FolderList = new ArrayList<>();
    private final GalleryListener Listener;
    private String Type = "Gallery";
    private final int GalleryType;
    private final int Count;

    public GalleryViewUI(int count, int type, GalleryListener l)
    {
        Count = count;
        GalleryType = type;
        Listener = l;
    }

    @Override
    public void OnCreate()
    {
        final AdapterGallery Adapter = new AdapterGallery();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_blue_rtl : R.drawable.back_blue);
        ImageViewBack.setId(Misc.ViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setText(GalleryType == 3 ? Misc.String(R.string.GalleryViewUIStorage) : Misc.String(R.string.GalleryViewUI));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setId(Misc.ViewID());
        TextViewTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (GalleryType == 3)
                    return;

                PopupMenu PopMenu = new PopupMenu(Activity, TextViewTitle);
                PopMenu.getMenu().add(0, 0, 0, Misc.String(R.string.GalleryViewUI2));

                int FolderCount = 1;

                for (String name : FolderList)
                {
                    PopMenu.getMenu().add(0, FolderCount, FolderCount, name);
                    FolderCount++;
                }

                PopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Type = item.getTitle().toString();
                        TextViewTitle.setText(Type);
                        Adapter.notifyDataSetChanged();
                        return false;
                    }
                });

                PopMenu.show();
            }
        });

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewListParam = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
        ImageViewListParam.addRule(Misc.AlignTo("R"), TextViewTitle.getId());
        ImageViewListParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewList = new ImageView(Activity);
        ImageViewList.setLayoutParams(ImageViewListParam);
        ImageViewList.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewList.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
        ImageViewList.setImageResource(R.drawable._write_arrow);

        if (GalleryType != 3)
            RelativeLayoutHeader.addView(ImageViewList);

        RelativeLayout.LayoutParams ImageViewSaveParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSaveParam.addRule(Misc.Align("L"));

        ImageView ImageViewSave = new ImageView(Activity);
        ImageViewSave.setLayoutParams(ImageViewSaveParam);
        ImageViewSave.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSave.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewSave.setImageResource(R.drawable.done_blue);
        ImageViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Listener.OnSave();
                Activity.onBackPressed();
            }
        });

        if (GalleryType != 3)
            RelativeLayoutHeader.addView(ImageViewSave);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewFollowersParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowersParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewMain = new RecyclerView(Activity);
        RecyclerViewMain.setLayoutParams(RecyclerViewFollowersParam);
        RecyclerViewMain.setLayoutManager(GalleryType != 3 ? new GridLayoutManager(Activity, 3) : new LinearLayoutManager(Activity));
        RecyclerViewMain.setAdapter(Adapter);

        if (GalleryType != 3)
            RecyclerViewMain.addItemDecoration(new GridSpacingItemDecoration());

        RelativeLayoutMain.addView(RecyclerViewMain);

        try
        {
            if (GalleryType == 3)
            {
                for (File file : ContextCompat.getExternalFilesDirs(Activity, null))
                    for (File file2 : file.getParentFile().getParentFile().getParentFile().getParentFile().listFiles())
                        GalleryList.add(new Struct(file2.getName(), file2.getAbsolutePath(), true));
            }
            else if (GalleryType == 2)
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = Activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = Activity.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                Cursor cursor = new MergeCursor(cursors);

                if (cursor.moveToFirst())
                {
                    String Folder;

                    int PathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                    int FolderColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

                    do
                    {
                        Folder = cursor.getString(FolderColumn);

                        if (!FolderList.contains(Folder))
                            FolderList.add(Folder);

                        GalleryList.add(new Struct(Folder, cursor.getString(PathColumn), false));
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
            }
            else if (GalleryType == 1)
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = Activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = Activity.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                Cursor cursor = new MergeCursor(cursors);

                if (cursor.moveToFirst())
                {
                    String Folder;

                    int PathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    int FolderColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                    do
                    {
                        Folder = cursor.getString(FolderColumn);

                        if (!FolderList.contains(Folder))
                            FolderList.add(Folder);

                        GalleryList.add(new Struct(Folder, cursor.getString(PathColumn), false));
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
            }
        }
        catch (Exception e)
        {
            Misc.Debug("GalleryViewUI-MediaList: " + e.toString());
        }

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        Activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        Activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.ViewHolderMain>
    {
        private List<Struct> FileList = new ArrayList<>();
        private int ID1_MAIN = Misc.ViewID();
        private int ID1_FILE = Misc.ViewID();
        private int ID1_NAME = Misc.ViewID();
        private int ID_MAIN = Misc.ViewID();
        private int ID_CIRCLE = Misc.ViewID();
        private GradientDrawable DrawableSelect;
        private GradientDrawable DrawableSelected;
        private int Selection = 0;

        AdapterGallery()
        {
            DrawableSelect = new GradientDrawable();
            DrawableSelect.setShape(GradientDrawable.OVAL);
            DrawableSelect.setStroke(Misc.ToDP(2), Color.WHITE);

            DrawableSelected = new GradientDrawable();
            DrawableSelected.setShape(GradientDrawable.OVAL);
            DrawableSelected.setColor(Misc.Color(R.color.Primary));
            DrawableSelected.setStroke(Misc.ToDP(2), Color.WHITE);
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            RelativeLayout RelativeLayoutMain;
            ImageView ImageViewFile;
            TextView TextViewName;

            ImageView ImageViewMain;
            View ViewCircle;

            ViewHolderMain(View view, int Type)
            {
                super(view);

                if (Type == 1)
                {
                    RelativeLayoutMain = view.findViewById(ID1_MAIN);
                    ImageViewFile = view.findViewById(ID1_FILE);
                    TextViewName = view.findViewById(ID1_NAME);
                }
                else
                {
                    ImageViewMain = view.findViewById(ID_MAIN);
                    ViewCircle = view.findViewById(ID_CIRCLE);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderMain Holder, int position)
        {
            final int Position = Holder.getAdapterPosition();

            if (Holder.getItemViewType() == 1)
            {
                Holder.TextViewName.setText(FileList.get(Position).Name);

                if (new File(FileList.get(Position).Path).isDirectory())
                {
                    Holder.ImageViewFile.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                    GlideApp.with(Activity).load(R.drawable._gallery_folder).dontAnimate().into(Holder.ImageViewFile);
                }
                else
                {
                    Holder.ImageViewFile.setPadding(Misc.ToDP(9), Misc.ToDP(9), Misc.ToDP(9), Misc.ToDP(9));
                    GlideApp.with(Activity).load(R.drawable._gallery_file).dontAnimate().into(Holder.ImageViewFile);
                }

                Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        File Path = new File(FileList.get(Position).Path);

                        if (Path.isDirectory())
                        {
                            FileList.clear();
                            FileList.add(new Struct("...", Environment.getExternalStorageDirectory().getAbsolutePath(), true));

                            for (File file : Path.listFiles())
                                FileList.add(new Struct(file.getName(), file.getAbsolutePath(), true));

                            notifyDataSetChanged();
                        }
                        else
                        {
                            Listener.OnSelection(Path.getAbsolutePath());
                            Activity.onBackPressed();
                        }
                    }
                });

                return;
            }

            Holder.ViewCircle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (FileList.get(Position).Selection)
                    {
                        Selection--;
                        Listener.OnRemove(FileList.get(Position).Path);
                        FileList.get(Position).Selection = false;
                        Holder.ViewCircle.setBackground(DrawableSelect);
                    }
                    else
                    {
                        if (Count <= Selection)
                        {
                            Misc.Toast( Misc.String(R.string.GalleryViewUIMaximum) + " " + Count);
                            return;
                        }

                        Selection++;
                        Listener.OnSelection(FileList.get(Position).Path);
                        FileList.get(Position).Selection = true;
                        Holder.ViewCircle.setBackground(DrawableSelected);
                    }
                }
            });

            if (FileList.get(Position).Selection)
                Holder.ViewCircle.setBackground(DrawableSelected);
            else
                Holder.ViewCircle.setBackground(DrawableSelect);

            GlideApp.with(Activity)
            .load(FileList.get(Position).Path)
            .thumbnail(0.1f)
            .placeholder(R.color.Gray)
            .centerCrop()
            .dontAnimate()
            .into(Holder.ImageViewMain);

            Holder.ImageViewMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (GalleryType == 2)
                    {
                        VideoPreviewUI vp = new VideoPreviewUI(FileList.get(Position).Path, true, false);
                        vp.SetType(FileList.get(Position).Selection, new VideoPreviewUI.OnSelectListener()
                        {
                            @Override
                            public void OnSelect()
                            {
                                if (FileList.get(Position).Selection)
                                {
                                    Selection--;
                                    Listener.OnRemove(FileList.get(Position).Path);
                                    Holder.ViewCircle.setBackground(DrawableSelect);
                                    FileList.get(Position).Selection = false;
                                }
                                else
                                {
                                    if (Count <= Selection)
                                    {
                                        Misc.Toast( Misc.String(R.string.GalleryViewUIMaximum) + " " + Count);
                                        return;
                                    }

                                    Selection++;
                                    Listener.OnSelection(FileList.get(Position).Path);
                                    Holder.ViewCircle.setBackground(DrawableSelected);
                                    FileList.get(Position).Selection = true;
                                }
                            }
                        });

                        Activity.GetManager().OpenView(vp, R.id.ContainerFull, "VideoPreviewUI");
                    }
                    else if (GalleryType == 1)
                    {
                        ImagePreviewUI ip = new ImagePreviewUI(FileList.get(Position).Path, false);
                        ip.SetType(FileList.get(Position).Selection, Count <= Selection, new ImagePreviewUI.OnSelectListener()
                        {
                            @Override
                            public void OnSelect()
                            {
                                if (FileList.get(Position).Selection)
                                {
                                    Selection--;
                                    Listener.OnRemove(FileList.get(Position).Path);
                                    Holder.ViewCircle.setBackground(DrawableSelect);
                                    FileList.get(Position).Selection = false;
                                }
                                else
                                {
                                    if (Count <= Selection)
                                    {
                                        Misc.Toast( Misc.String(R.string.GalleryViewUIMaximum) + " " + Count);
                                        return;
                                    }

                                    Selection++;
                                    Listener.OnSelection(FileList.get(Position).Path);
                                    Holder.ViewCircle.setBackground(DrawableSelected);
                                    FileList.get(Position).Selection = true;
                                }
                            }
                        });

                        Activity.GetManager().OpenView(ip, R.id.ContainerFull, "ImagePreviewUI");
                    }
                }
            });
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 1)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
                RelativeLayoutMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Misc.ToDP(57)));
                RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                RelativeLayoutMain.setId(ID1_MAIN);

                ImageView ImageViewIcon = new ImageView(Activity);
                ImageViewIcon.setLayoutParams(new RecyclerView.LayoutParams(Misc.ToDP(56), Misc.ToDP(56)));
                ImageViewIcon.setId(ID1_FILE);

                RelativeLayoutMain.addView(ImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID1_FILE);
                TextViewNameParam.setMargins(0, Misc.ToDP(12), 0, 0);

                TextView TextViewName = new TextView(Activity, 14, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                TextViewName.setId(ID1_NAME);

                RelativeLayoutMain.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ID1_FILE);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);

                RelativeLayoutMain.addView(ViewLine);

                return new ViewHolderMain(RelativeLayoutMain, 1);
            }
            else
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
                RelativeLayoutMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Misc.ToDP(90)));

                ImageView ImageViewMain = new ImageView(Activity);
                ImageViewMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
                ImageViewMain.setId(ID_MAIN);

                RelativeLayoutMain.addView(ImageViewMain);

                RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
                ViewCircleParam.setMargins(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), 0);
                ViewCircleParam.addRule(Misc.Align("R"));

                View ViewCircle = new View(Activity);
                ViewCircle.setLayoutParams(ViewCircleParam);
                ViewCircle.setId(ID_CIRCLE);

                RelativeLayoutMain.addView(ViewCircle);

                return new ViewHolderMain(RelativeLayoutMain, 0);
            }
        }

        @Override
        public int getItemViewType(int Position)
        {
            if (GalleryType == 3)
                return 1;

            return 0;
        }

        @Override
        public int getItemCount()
        {
            if (GalleryType == 3)
            {
                if (FileList.size() > 0)
                    return FileList.size();

                FileList.addAll(GalleryList);

                return FileList.size();
            }

            FileList.clear();

            if (Type.equals("Gallery"))
            {
                FileList.addAll(GalleryList);
                return FileList.size();
            }

            for (Struct item : GalleryList)
                if (item.Album.equals(Type))
                    FileList.add(item);

            return FileList.size();
        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
    {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
        {
            int SpanCount = 3;
            int Spacing = Misc.ToDP(4);
            int Position = parent.getChildAdapterPosition(view);
            int Column = Position % SpanCount;

            outRect.left = Spacing - Column * Spacing / SpanCount;
            outRect.right = (Column + 1) * Spacing / SpanCount;

            if (Position < SpanCount)
                outRect.top = Spacing;

            outRect.bottom = Spacing;
        }
    }

    private class Struct
    {
        String Name;
        String Path;
        String Album;
        boolean Selection = false;

        Struct(String p1, String p2, boolean isFolder)
        {
            if (isFolder)
            {
                Name = p1;
                Path = p2;
            }
            else
            {
                Album = p1;
                Path = p2;
            }
        }
    }

    public interface GalleryListener
    {
        void OnSelection(String URL);
        void OnRemove(String URL);
        void OnSave();
    }
}
