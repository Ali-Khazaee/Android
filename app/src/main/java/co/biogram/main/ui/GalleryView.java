package co.biogram.main.ui;

import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.view.TextView;

class GalleryView extends FragmentBase
{
    private final List<Struct> GalleryList = new ArrayList<>();
    private final List<String> FolderList = new ArrayList<>();

    private String SelectionPath = "";

    private int Count = 1;
    private boolean IsVideo;
    private String Type = "Gallery";

    GalleryView(int count, boolean video)
    {
        Count = count;
        IsVideo = video;
    }

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();
        final AdapterGallery Adapter = new AdapterGallery();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewBack.setImageResource(MiscHandler.IsFa() ? R.drawable.ic_back_blue_fa : R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewTitle = new TextView(activity, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.Black));
        TextViewTitle.setText(activity.getString(R.string.GalleryView));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTitle.setId(MiscHandler.GenerateViewID());
        TextViewTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu PopMenu = new PopupMenu(activity, TextViewTitle);
                PopMenu.getMenu().add(0, 0, 0, activity.getString(R.string.GalleryViewGallery));

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

        RelativeLayout.LayoutParams ImageViewListParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 24), MiscHandler.ToDimension(activity, 24));
        ImageViewListParam.addRule(MiscHandler.AlignTo("R"), TextViewTitle.getId());
        ImageViewListParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewList = new ImageView(activity);
        ImageViewList.setLayoutParams(ImageViewListParam);
        ImageViewList.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewList.setPadding(MiscHandler.ToDimension(activity, 3), MiscHandler.ToDimension(activity, 3), MiscHandler.ToDimension(activity, 3), MiscHandler.ToDimension(activity, 3));
        ImageViewList.setImageResource(R.drawable.ic_arrow_down_blue);

        RelativeLayoutHeader.addView(ImageViewList);

        RelativeLayout.LayoutParams ImageViewSaveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSaveParam.addRule(MiscHandler.Align("L"));

        ImageView ImageViewSave = new ImageView(activity);
        ImageViewSave.setLayoutParams(ImageViewSaveParam);
        ImageViewSave.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSave.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewSave.setImageResource(R.drawable.ic_send_blue2);
        ImageViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SignUpDescription SignUpDescription = (SignUpDescription) activity.GetManager().FindByTag("SignUpDescription");

                if (SignUpDescription != null && !SelectionPath.equals(""))
                    SignUpDescription.Update(new File(SelectionPath));

                GetActivity().onBackPressed();


                /*EditFragment editFragment = ((EditFragment) getActivity().getSupportFragmentManager().findFragmentByTag("EditFragment"));

                if (editFragment != null)
                {
                    for (Struct item : GalleryList)
                        if (item.Selection)
                            editFragment.DoCrop(item.Path);
                }

                WriteFragment writeFragment = ((WriteFragment) getActivity().getSupportFragmentManager().findFragmentByTag("WriteFragment"));

                if (writeFragment != null)
                {
                    for (Struct item : GalleryList)
                        if (item.Selection)
                            writeFragment.GetData(item.Path, IsVideo);
                }

                getActivity().onBackPressed();*/
            }
        });

        RelativeLayoutHeader.addView(ImageViewSave);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewFollowersParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowersParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewMain = new RecyclerView(activity);
        RecyclerViewMain.setLayoutParams(RecyclerViewFollowersParam);
        RecyclerViewMain.setLayoutManager(new GridLayoutManager(activity, 3));
        RecyclerViewMain.setAdapter(Adapter);
        RecyclerViewMain.addItemDecoration(new GridSpacingItemDecoration());

        RelativeLayoutMain.addView(RecyclerViewMain);

        try
        {
            if (IsVideo)
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = activity.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

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

                        GalleryList.add(new Struct(Folder, cursor.getString(PathColumn)));
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
            }
            else
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = activity.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

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

                        GalleryList.add(new Struct(Folder, cursor.getString(PathColumn)));
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();
            }
        }
        catch (Exception e)
        {
            MiscHandler.Debug("GalleryFragment-MediaList: " + e.toString());
        }

        ViewMain = RelativeLayoutMain;
    }

    private class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.ViewHolderMain>
    {
        private final List<Struct> FileList = new ArrayList<>();

        private final int ID_MAIN = MiscHandler.GenerateViewID();
        private final int ID_CIRCLE = MiscHandler.GenerateViewID();

        private final FragmentActivity activity = GetActivity();

        private final GradientDrawable Select;
        private final GradientDrawable Selected;

        private int Selection = 0;

        AdapterGallery()
        {
            Select = new GradientDrawable();
            Select.setShape(GradientDrawable.OVAL);
            Select.setStroke(MiscHandler.ToDimension(activity, 2), Color.WHITE);

            Selected = new GradientDrawable();
            Selected.setShape(GradientDrawable.OVAL);
            Selected.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
            Selected.setStroke(MiscHandler.ToDimension(activity, 2), Color.WHITE);
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            private final ImageView ImageViewMain;
            private final View ViewCircle;

            ViewHolderMain(View view)
            {
                super(view);
                ImageViewMain = (ImageView) view.findViewById(ID_MAIN);
                ViewCircle = view.findViewById(ID_CIRCLE);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderMain Holder, int position)
        {
            final int Position = Holder.getAdapterPosition();

            Holder.ViewCircle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (FileList.get(Position).Selection)
                    {
                        Selection--;
                        SelectionPath = "";
                        Holder.ViewCircle.setBackground(Select);
                        FileList.get(Position).Selection = false;
                    }
                    else
                    {
                        if (Count <= Selection)
                        {
                            MiscHandler.Toast(activity, activity.getString(R.string.GalleryViewMaximum) + " " + Count);
                            return;
                        }

                        Selection++;
                        SelectionPath = FileList.get(Position).Path;
                        Holder.ViewCircle.setBackground(Selected);
                        FileList.get(Position).Selection = true;
                    }
                }
            });

            if (FileList.get(Position).Selection)
                Holder.ViewCircle.setBackground(Selected);
            else
                Holder.ViewCircle.setBackground(Select);

            GlideApp.with(activity)
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
                    /*if (IsVideo)
                    {
                        /* TODO Fix Video View
                        Bundle bundle = new Bundle();
                        bundle.putString("VideoURL", FileList.get(Position).Path);

                        VideoPreviewFragment fragment = new VideoPreviewFragment();
                        fragment.setArguments(bundle);
                        fragment.SetLocalVideo();

                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("VideoPreviewFragment").commitAllowingStateLoss();

                    }
                    else*/
                    {
                        ImagePreview imagePreview = new ImagePreview(FileList.get(Position).Path);
                        imagePreview.SetType(FileList.get(Position).Selection, new ImagePreview.OnSelectListener()
                        {
                            @Override
                            public void OnSelect()
                            {
                                if (FileList.get(Position).Selection)
                                {
                                    Selection--;
                                    SelectionPath = "";
                                    Holder.ViewCircle.setBackground(Select);
                                    FileList.get(Position).Selection = false;
                                }
                                else
                                {
                                    if (Count <= Selection)
                                    {
                                        MiscHandler.Toast(activity, activity.getString(R.string.GalleryViewMaximum) + " " + Count);
                                        return;
                                    }

                                    Selection++;
                                    SelectionPath = FileList.get(Position).Path;
                                    Holder.ViewCircle.setBackground(Selected);
                                    FileList.get(Position).Selection = true;
                                }
                            }
                        });

                        GetActivity().GetManager().OpenView(imagePreview, R.id.WelcomeActivityContainer, "ImagePreview");
                    }
                }
            });
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
            RelativeLayoutMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 90)));

            ImageView ImageViewMain = new ImageView(activity);
            ImageViewMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
            ImageViewMain.setId(ID_MAIN);

            RelativeLayoutMain.addView(ImageViewMain);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 24), MiscHandler.ToDimension(activity, 24));
            ViewCircleParam.setMargins(MiscHandler.ToDimension(activity, 10), MiscHandler.ToDimension(activity, 10), MiscHandler.ToDimension(activity, 10), 0);
            ViewCircleParam.addRule(MiscHandler.Align("R"));

            View ViewCircle = new View(activity);
            ViewCircle.setLayoutParams(ViewCircleParam);
            ViewCircle.setId(ID_CIRCLE);

            RelativeLayoutMain.addView(ViewCircle);

            return new ViewHolderMain(RelativeLayoutMain);
        }

        @Override
        public int getItemCount()
        {
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
            int Spacing = MiscHandler.ToDimension(parent.getContext(), 4);
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
        private final String Path;
        private final String Album;
        private boolean Selection = false;

        Struct(String album, String path)
        {
            Album = album;
            Path = path;
        }
    }
}
