package co.biogram.main.ui.general;

import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.io.File;
import java.util.ArrayList;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.TextView;

public class Gallery_UI extends FragmentView
{
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_FILE = 3;

    private String Folder = Misc.String(R.string.GeneralGalleryUI);
    private ArrayList<Struct> GalleryList = new ArrayList<>();
    private ArrayList<String> FolderList = new ArrayList<>();
    private AdapterGallery Adapter = new AdapterGallery();
    private OnGalleryListener Listener;
    private int GalleryCount;
    private int GalleryType;

    public Gallery_UI(int count, int type, OnGalleryListener l)
    {
        GalleryCount = count;
        GalleryType = type;
        Listener = l;
    }

    @Override
    public void OnCreate()
    {
        ViewMain = View.inflate(Activity, R.layout.general_gallery, null);

        ViewMain.findViewById(R.id.ImageViewClose).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        ViewMain.findViewById(R.id.ImageViewDone).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Listener.OnDone();
                Activity.onBackPressed();
            }
        });

        final TextView TextViewPage = ViewMain.findViewById(R.id.TextViewPage);
        TextViewPage.setText(Folder);
        TextViewPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (GalleryType == TYPE_FILE)
                    return;

                PopupMenu PopMenu = new PopupMenu(Activity, v);
                PopMenu.getMenu().add(0, 0, 0, Misc.String(R.string.GeneralGalleryUI));

                for (int I = 0; I < FolderList.size(); I++)
                    PopMenu.getMenu().add(0, I + 1, I + 1, FolderList.get(I));

                PopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Folder = item.getTitle().toString();
                        Adapter.notifyDataSetChanged();
                        TextViewPage.setText(Folder);
                        return false;
                    }
                });

                PopMenu.show();
            }
        });

        RecyclerView RecyclerViewMain = ViewMain.findViewById(R.id.RecyclerViewMain);
        RecyclerViewMain.setLayoutManager(GalleryType != TYPE_FILE ? new GridLayoutManager(Activity, 3) : new LinearLayoutManager(Activity));
        RecyclerViewMain.setAdapter(Adapter);

        if (GalleryType == TYPE_FILE)
        {
            ViewMain.findViewById(R.id.ImageViewArrow).setVisibility(View.GONE);
            ViewMain.findViewById(R.id.ImageViewDone).setVisibility(View.GONE);
        }
        else
            RecyclerViewMain.addItemDecoration(new GridSpacingItemDecoration());

        if (GalleryType == TYPE_FILE)
        {
            for (File file : ContextCompat.getExternalFilesDirs(Activity, null))
                for (File file2 : file.getParentFile().getParentFile().getParentFile().getParentFile().listFiles())
                    GalleryList.add(new Struct(file2.getName(), file2.getAbsolutePath(), true));
        }
        else if (GalleryType == TYPE_VIDEO)
        {
            Cursor[] Cursors = new Cursor[ 2 ];

            Cursors[ 0 ] = Activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
            Cursors[ 1 ] = Activity.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            Cursor cursor = new MergeCursor(Cursors);

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
        else if (GalleryType == TYPE_IMAGE)
        {
            Cursor[] Cursors = new Cursor[ 2 ];

            Cursors[ 0 ] = Activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
            Cursors[ 1 ] = Activity.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME }, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            Cursor cursor = new MergeCursor(Cursors);

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
        private ArrayList<Struct> ItemList = new ArrayList<>();
        private int SelectCount = 0;

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderMain Holder, int position)
        {
            final int Position = Holder.getAdapterPosition();

            if (Holder.getItemViewType() == TYPE_FILE)
            {
                /*
                Holder.TextViewName.setText(FileList.get(Position).Name);

                if (new File(FileList.get(Position).Path).isDirectory()) {
                    Holder.ImageViewFile.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                    GlideApp.with(Activity).load(R.drawable.__gallery_folder).dontAnimate().into(Holder.ImageViewFile);
                } else {
                    Holder.ImageViewFile.setPadding(Misc.ToDP(9), Misc.ToDP(9), Misc.ToDP(9), Misc.ToDP(9));
                    GlideApp.with(Activity).load(R.drawable.__gallery_file).dontAnimate().into(Holder.ImageViewFile);
                }

                Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File Path = new File(FileList.get(Position).Path);

                        if (Path.isDirectory()) {
                            FileList.clear();
                            FileList.add(new Struct("...", Environment.getExternalStorageDirectory().getAbsolutePath(), true));

                            for (File file : Path.listFiles())
                                FileList.add(new Struct(file.getName(), file.getAbsolutePath(), true));

                            notifyDataSetChanged();
                        } else {
                            Listener.OnSelection(Path.getAbsolutePath());
                            Activity.onBackPressed();
                        }
                    }
                });

                return;
                 */
            }
            else
            {
                Holder.ViewCircle.setBackgroundResource(ItemList.get(Position).Selection ? R.drawable.general_gallery_bg_fill : R.drawable.general_gallery_bg);
                Holder.ViewCircle.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (ItemList.get(Position).Selection)
                        {
                            SelectCount--;
                            ItemList.get(Position).Selection = false;
                            Listener.OnRemove(ItemList.get(Position).Path);
                            Holder.ViewCircle.setBackgroundResource(R.drawable.general_gallery_bg);
                        }
                        else
                        {
                            if (GalleryCount <= SelectCount)
                            {
                                Misc.Toast(Activity.getString(R.string.GeneralGalleryUIMaximum, GalleryCount));
                                return;
                            }

                            SelectCount++;
                            ItemList.get(Position).Selection = true;
                            Listener.OnAdd(ItemList.get(Position).Path);
                            Holder.ViewCircle.setBackgroundResource(R.drawable.general_gallery_bg_fill);
                        }
                    }
                });

                GlideApp.with(Activity).load(ItemList.get(Position).Path).thumbnail(0.1f).placeholder(R.color.Gray).centerCrop().dontAnimate().into(Holder.ImageViewMain);
                Holder.ImageViewMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (GalleryType == TYPE_VIDEO)
                        {
                            /*VideoPreviewUI vp = new VideoPreviewUI(FileList.get(Position).Path, true, false);
                            vp.SetType(FileList.get(Position).SelectCount, new VideoPreviewUI.OnSelectListener()
                            {
                                @Override
                                public void OnSelect()
                                {
                                    if (FileList.get(Position).SelectCount)
                                    {
                                        SelectCount--;
                                        Listener.OnRemove(FileList.get(Position).Path);
                                        Holder.ViewCircle.setBackground(DrawableSelect);
                                        FileList.get(Position).SelectCount = false;
                                    }
                                    else
                                    {
                                        if (Count <= SelectCount)
                                        {
                                            Misc.ToastOld(Misc.String(R.string.GalleryViewUIMaximum) + " " + Count);
                                            return;
                                        }

                                        SelectCount++;
                                        Listener.OnSelection(FileList.get(Position).Path);
                                        Holder.ViewCircle.setBackground(DrawableSelected);
                                        FileList.get(Position).SelectCount = true;
                                    }
                                }
                            });

                            Activity.GetManager().OpenView(vp, "VideoPreviewUI", true);*/
                        }
                        else if (GalleryType == TYPE_IMAGE)
                        {
                            ImagePreviewUI ImagePreview = new ImagePreviewUI();

                            Activity.GetManager().OpenView(ImagePreview, "ImagePreviewUI");

                            ImagePreview.SetForGallery(ItemList, Position, new ImagePreviewUI.OnChoiceListener()
                            {
                                @Override
                                public void OnChoice(int Position)
                                {
                                    if (ItemList.get(Position).Selection)
                                    {
                                        SelectCount--;
                                        ItemList.get(Position).Selection = false;
                                        Listener.OnRemove(ItemList.get(Position).Path);
                                        Holder.ViewCircle.setBackgroundResource(R.drawable.general_gallery_bg);
                                    }
                                    else
                                    {
                                        if (GalleryCount <= SelectCount)
                                        {
                                            Misc.Toast(Activity.getString(R.string.GeneralGalleryUIMaximum, GalleryCount));
                                            return;
                                        }

                                        SelectCount++;
                                        ItemList.get(Position).Selection = true;
                                        Listener.OnAdd(ItemList.get(Position).Path);
                                        Holder.ViewCircle.setBackgroundResource(R.drawable.general_gallery_bg_fill);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }

        @NonNull
        @Override
        public AdapterGallery.ViewHolderMain onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
        {
            return new ViewHolderMain(View.inflate(Activity, R.layout.general_gallery_row, null));
        }

        @Override
        public int getItemViewType(int Position)
        {
            return GalleryType;
        }

        @Override
        public int getItemCount()
        {
            ItemList.clear();

            if (GalleryType == TYPE_FILE || Folder.equals(Misc.String(R.string.GeneralGalleryUI)))
            {
                ItemList.addAll(GalleryList);
                return ItemList.size();
            }

            for (Struct Media : GalleryList)
                if (Folder.equals(Media.Album))
                    ItemList.add(Media);

            return ItemList.size();
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            private ImageView ImageViewMain;
            private View ViewCircle;

            ViewHolderMain(View v)
            {
                super(v);
                ImageViewMain = v.findViewById(R.id.ImageViewMain);
                ViewCircle = v.findViewById(R.id.ViewCircle);
            }
        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
    {
        private int Space = Misc.ToDP(8);

        @Override
        public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state)
        {
            int Column = parent.getChildAdapterPosition(view) % 3;

            rect.top = Space;
            rect.left = Column == 0 ? Space : 0;
            rect.right = Column == 2 ? Space : 0;
        }
    }

    public class Struct
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

    public interface OnGalleryListener
    {
        void OnAdd(String path);

        void OnRemove(String path);

        void OnDone();
    }
}
