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

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.R;

import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.TextView;

public class GalleryViewUI extends FragmentBase
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

        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextDark);
        RelativeLayoutMain.setClickable(true);

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
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.i_back_blue_rtl : R.drawable.i_back_blue);
        ImageViewBack.setId(Misc.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark(GetActivity()) ? R.color.TextDark : R.color.TextWhite));
        TextViewTitle.setText(GalleryType == 3 ? GetActivity().getString(R.string.GalleryViewUIStorage) : GetActivity().getString(R.string.GalleryViewUI));
        TextViewTitle.setPadding(0, Misc.ToDP(GetActivity(), 6), 0, 0);
        TextViewTitle.setId(Misc.GenerateViewID());
        TextViewTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (GalleryType == 3)
                    return;

                PopupMenu PopMenu = new PopupMenu(GetActivity(), TextViewTitle);
                PopMenu.getMenu().add(0, 0, 0, GetActivity().getString(R.string.GalleryViewUI2));

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

        RelativeLayout.LayoutParams ImageViewListParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 24), Misc.ToDP(GetActivity(), 24));
        ImageViewListParam.addRule(Misc.AlignTo("R"), TextViewTitle.getId());
        ImageViewListParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewList = new ImageView(GetActivity());
        ImageViewList.setLayoutParams(ImageViewListParam);
        ImageViewList.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewList.setPadding(Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3), Misc.ToDP(GetActivity(), 3));
        ImageViewList.setImageResource(R.drawable.ic_arrow_down_blue);

        if (GalleryType != 3)
            RelativeLayoutHeader.addView(ImageViewList);

        RelativeLayout.LayoutParams ImageViewSaveParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewSaveParam.addRule(Misc.Align("L"));

        ImageView ImageViewSave = new ImageView(GetActivity());
        ImageViewSave.setLayoutParams(ImageViewSaveParam);
        ImageViewSave.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSave.setPadding(Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6), Misc.ToDP(GetActivity(), 6));
        ImageViewSave.setImageResource(R.drawable.ic_done_blue);
        ImageViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Listener.OnSave();
                GetActivity().onBackPressed();
            }
        });

        if (GalleryType != 3)
            RelativeLayoutHeader.addView(ImageViewSave);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewFollowersParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowersParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity());
        RecyclerViewMain.setLayoutParams(RecyclerViewFollowersParam);
        RecyclerViewMain.setLayoutManager(GalleryType != 3 ? new GridLayoutManager(GetActivity(), 3) : new LinearLayoutManager(GetActivity()));
        RecyclerViewMain.setAdapter(Adapter);

        if (GalleryType != 3)
            RecyclerViewMain.addItemDecoration(new GridSpacingItemDecoration());

        RelativeLayoutMain.addView(RecyclerViewMain);

        try
        {
            if (GalleryType == 3)
            {
                for (File file : Environment.getExternalStorageDirectory().listFiles())
                    GalleryList.add(new Struct(file.getName(), file.getAbsolutePath(), true));
            }
            else if (GalleryType == 2)
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = GetActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = GetActivity().getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

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

                cursors[0] = GetActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = GetActivity().getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

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
        GetActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void OnPause()
    {
        GetActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.ViewHolderMain>
    {
        private final List<Struct> FileList = new ArrayList<>();
        private final int ID1_MAIN = Misc.GenerateViewID();
        private final int ID1_NAME = Misc.GenerateViewID();
        private final int ID_MAIN = Misc.GenerateViewID();
        private final int ID_CIRCLE = Misc.GenerateViewID();
        private final GradientDrawable DrawableSelect;
        private final GradientDrawable DrawableSelected;
        private int Selection = 0;

        AdapterGallery()
        {
            DrawableSelect = new GradientDrawable();
            DrawableSelect.setShape(GradientDrawable.OVAL);
            DrawableSelect.setStroke(Misc.ToDP(GetActivity(), 2), Color.WHITE);

            DrawableSelected = new GradientDrawable();
            DrawableSelected.setShape(GradientDrawable.OVAL);
            DrawableSelected.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
            DrawableSelected.setStroke(Misc.ToDP(GetActivity(), 2), Color.WHITE);
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            RelativeLayout RelativeLayoutMain;
            TextView TextViewName;

            ImageView ImageViewMain;
            View ViewCircle;

            ViewHolderMain(View view, int Type)
            {
                super(view);

                if (Type == 1)
                {
                    RelativeLayoutMain = view.findViewById(ID1_MAIN);
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
                            GetActivity().onBackPressed();
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
                            Misc.Toast(GetActivity(), GetActivity().getString(R.string.GalleryViewUIMaximum) + " " + Count);
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

            GlideApp.with(GetActivity())
            .load(FileList.get(Position).Path)
            .thumbnail(0.1f)
            .placeholder(R.color.BlueGray2)
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
                        VideoPreviewUI vp = new VideoPreviewUI(FileList.get(Position).Path, true);
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
                                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.GalleryViewUIMaximum) + " " + Count);
                                        return;
                                    }

                                    Selection++;
                                    Listener.OnSelection(FileList.get(Position).Path);
                                    Holder.ViewCircle.setBackground(DrawableSelected);
                                    FileList.get(Position).Selection = true;
                                }
                            }
                        });

                        GetActivity().GetManager().OpenView(vp, R.id.ContainerFull, "VideoPreviewUI");
                    }
                    else if (GalleryType == 1)
                    {
                        ImagePreviewUI ip = new ImagePreviewUI(FileList.get(Position).Path);
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
                                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.GalleryViewUIMaximum) + " " + Count);
                                        return;
                                    }

                                    Selection++;
                                    Listener.OnSelection(FileList.get(Position).Path);
                                    Holder.ViewCircle.setBackground(DrawableSelected);
                                    FileList.get(Position).Selection = true;
                                }
                            }
                        });

                        GetActivity().GetManager().OpenView(ip, R.id.ContainerFull, "ImagePreviewUI");
                    }
                }
            });
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 1)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
                RelativeLayoutMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 57)));
                RelativeLayoutMain.setId(ID1_MAIN);

                CircleImageView CircleImageViewIcon = new CircleImageView(GetActivity());
                CircleImageViewIcon.setLayoutParams(new RecyclerView.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56)));
                CircleImageViewIcon.setPadding(Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8), Misc.ToDP(GetActivity(), 8));
                CircleImageViewIcon.setImageResource(R.drawable.ic_comment);
                CircleImageViewIcon.SetCircleBackgroundColor(R.color.Gray);
                CircleImageViewIcon.setId(Misc.GenerateViewID());
                CircleImageViewIcon.SetWidthPadding();
                CircleImageViewIcon.SetBorderWidth(1);

                RelativeLayoutMain.addView(CircleImageViewIcon);

                RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewNameParam.setMargins(0, Misc.ToDP(GetActivity(), 12), 0, 0);
                TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewIcon.getId());

                TextView TextViewName = new TextView(GetActivity(), 14, true);
                TextViewName.setLayoutParams(TextViewNameParam);
                TextViewName.setTextColor(ContextCompat.getColor(GetActivity(), R.color.TextWhite));
                TextViewName.setId(ID1_NAME);

                RelativeLayoutMain.addView(TextViewName);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, CircleImageViewIcon.getId());

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);

                RelativeLayoutMain.addView(ViewLine);

                return new ViewHolderMain(RelativeLayoutMain, 1);
            }
            else
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
                RelativeLayoutMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 90)));

                ImageView ImageViewMain = new ImageView(GetActivity());
                ImageViewMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
                ImageViewMain.setId(ID_MAIN);

                RelativeLayoutMain.addView(ImageViewMain);

                RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 24), Misc.ToDP(GetActivity(), 24));
                ViewCircleParam.setMargins(Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), Misc.ToDP(GetActivity(), 10), 0);
                ViewCircleParam.addRule(Misc.Align("R"));

                View ViewCircle = new View(GetActivity());
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
            int Spacing = Misc.ToDP(parent.getContext(), 4);
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
