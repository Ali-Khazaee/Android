package co.biogram.main.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

public class GalleryFragment extends Fragment
{
    private final List<Struct> GalleryList = new ArrayList<>();
    private final List<String> FolderList = new ArrayList<>();

    private static int Count = 1;
    private static boolean IsVideo;
    private static String Type = "Gallery";

    public static GalleryFragment NewInstance(int count, boolean video)
    {
        Count = count;
        IsVideo = video;

        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final AdapterGallery Adapter = new AdapterGallery(context);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

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
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.GalleryFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setId(MiscHandler.GenerateViewID());
        TextViewTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu PopMenu = new PopupMenu(context, TextViewTitle);
                PopMenu.getMenu().add(0, 0, 0, getString(R.string.GalleryFragment));

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

        RelativeLayout.LayoutParams ImageViewListParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 24));
        ImageViewListParam.addRule(RelativeLayout.RIGHT_OF, TextViewTitle.getId());
        ImageViewListParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewList = new ImageView(context);
        ImageViewList.setLayoutParams(ImageViewListParam);
        ImageViewList.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewList.setPadding(MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3));
        ImageViewList.setImageResource(R.drawable.ic_arrow_down_blue);

        RelativeLayoutHeader.addView(ImageViewList);

        RelativeLayout.LayoutParams ImageViewSaveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSaveParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewSave = new ImageView(context);
        ImageViewSave.setLayoutParams(ImageViewSaveParam);
        ImageViewSave.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSave.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewSave.setImageResource(R.drawable.ic_send_blue2);
        ImageViewSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditFragment editFragment = ((EditFragment) getActivity().getSupportFragmentManager().findFragmentByTag("EditFragment"));

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

                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewSave);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewFollowersParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowersParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewFollowersParam);
        RecyclerViewMain.setLayoutManager(new GridLayoutManager(context, 3));
        RecyclerViewMain.setAdapter(Adapter);
        RecyclerViewMain.addItemDecoration(new GridSpacingItemDecoration());

        RelativeLayoutMain.addView(RecyclerViewMain);

        if (IsVideo)
        {
            try
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = getActivity().getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

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
            catch (Exception e)
            {
                MiscHandler.Debug("GalleryFragment-MediaVideo: " + e.toString());
            }
        }
        else
        {
            try
            {
                Cursor[] cursors = new Cursor[2];

                cursors[0] = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                cursors[1] = getActivity().getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

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
            catch (Exception e)
            {
                MiscHandler.Debug("GalleryFragment-MediaImage: " + e.toString());
            }
        }

        return RelativeLayoutMain;
    }

    private class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.ViewHolderMain>
    {
        private final List<Struct> FileList = new ArrayList<>();

        private final int ID_MAIN = MiscHandler.GenerateViewID();
        private final int ID_CIRCLE = MiscHandler.GenerateViewID();

        private final Context context;

        private final GradientDrawable Select;
        private final GradientDrawable Selected;

        private int Selection = 0;

        AdapterGallery(Context c)
        {
            context = c;

            Select = new GradientDrawable();
            Select.setShape(GradientDrawable.OVAL);
            Select.setStroke(MiscHandler.ToDimension(context, 2), Color.WHITE);

            Selected = new GradientDrawable();
            Selected.setShape(GradientDrawable.OVAL);
            Selected.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            Selected.setStroke(MiscHandler.ToDimension(context, 2), Color.WHITE);
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
                        Holder.ViewCircle.setBackground(Select);
                        FileList.get(Position).Selection = false;
                    }
                    else
                    {
                        if (Count <= Selection)
                        {
                            MiscHandler.Toast(context, getString(R.string.GalleryFragmentMaximum) + " " + Count);
                            return;
                        }

                        Selection++;
                        Holder.ViewCircle.setBackground(Selected);
                        FileList.get(Position).Selection = true;
                    }
                }
            });

            if (FileList.get(Position).Selection)
                Holder.ViewCircle.setBackground(Selected);
            else
                Holder.ViewCircle.setBackground(Select);

            /*Glide.with(context)
            .load(FileList.get(Position).Path)
            .thumbnail(0.1f)
            .placeholder(R.color.BlueGray2)
            .centerCrop()
            .dontAnimate()
            .into(Holder.ImageViewMain);*/

            Holder.ImageViewMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (IsVideo)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("VideoURL", FileList.get(Position).Path);

                        VideoPreviewFragment fragment = new VideoPreviewFragment();
                        fragment.setArguments(bundle);
                        fragment.SetLocalVideo();

                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("VideoPreviewFragment").commitAllowingStateLoss();
                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("URL", FileList.get(Position).Path);

                        Fragment fragment = new ImagePreviewFragment();
                        fragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ImagePreviewFragment").commitAllowingStateLoss();
                    }
                }
            });
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 90)));

            ImageView ImageViewMain = new ImageView(context);
            ImageViewMain.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
            ImageViewMain.setId(ID_MAIN);

            RelativeLayoutMain.addView(ImageViewMain);

            RelativeLayout.LayoutParams ViewCircleParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20));
            ViewCircleParam.setMargins(0, MiscHandler.ToDimension(context, 8), MiscHandler.ToDimension(context, 8), 0);
            ViewCircleParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            View ViewCircle = new View(context);
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
