package co.biogram.main.ui.chat;

import android.Manifest;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.EditTextTag;
import co.biogram.main.ui.view.PermissionDialog;

public class Chat_GroupCreateUI extends FragmentView implements View.OnClickListener {


    private ImageButton ImageButtonBack;
    private ImageButton ImageButtonCreate;
    private ImageView ImageViewPhoto;
    private EditText EditTextName;
    private EditTextTag EditTagMembers;

    private PermissionDialog PermissionRequest;

    private List<String> temp = new ArrayList<>();

    public Chat_GroupCreateUI(List<ContactUI.ContactEntity> members) {

        for (ContactUI.ContactEntity item : members) {
            temp.add(item.Username);
        }

    }


    @Override
    public void OnCreate() {

        View view = View.inflate(Activity, R.layout.chat_group_create, null);

        ImageButtonBack = view.findViewById(R.id.ImageButtonBack);
        ImageButtonCreate = view.findViewById(R.id.ImageButtonCreate);
        ImageViewPhoto = view.findViewById(R.id.ImageViewProfile);
        EditTextName = view.findViewById(R.id.EditTextName);
        EditTagMembers = view.findViewById(R.id.MembersList);

        EditTagMembers.hideEditText();


        EditTagMembers.AddTag(temp, null);


        Misc.SetCursorColor(EditTextName, R.color.Primary);
        ViewCompat.setBackgroundTintList(EditTextName, ColorStateList.valueOf(Misc.Color(R.color.Primary)));


        ImageButtonBack.setOnClickListener(this);
        ImageButtonCreate.setOnClickListener(this);
        ImageViewPhoto.setOnClickListener(this);

        ViewMain = view;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ImageButtonBack: {
                Activity.onBackPressed();
                break;
            }
            case R.id.ImageViewProfile: {

                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener() {
                    String ImageURL;

                    @Override
                    public void OnSelection(String URL) {
                        ImageURL = URL;
                    }

                    @Override
                    public void OnRemove(String URL) {
                        ImageURL = null;
                    }

                    @Override
                    public void OnSave() {
                        if (ImageURL == null)
                            return;


                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int Size = Misc.ToDP(320);
                                    BitmapFactory.Options O = new BitmapFactory.Options();
                                    O.inJustDecodeBounds = true;

                                    BitmapFactory.decodeFile(ImageURL, O);

                                    O.inSampleSize = Misc.SampleSize(O, Size, Size);
                                    O.inJustDecodeBounds = false;

                                    Bitmap bitmap = BitmapFactory.decodeFile(ImageURL, O);

                                    final Bitmap finalBitmap = Misc.scale(bitmap);

                                    File selectedFilePath = new File(Misc.Temp(), System.currentTimeMillis() + ".jpg");
                                    selectedFilePath.createNewFile();


                                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);

                                    FileOutputStream FOS = new FileOutputStream(selectedFilePath);
                                    FOS.write(BAOS.toByteArray());
                                    FOS.flush();
                                    FOS.close();


                                    Misc.UIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ImageViewPhoto.setImageBitmap(finalBitmap);

                                        }
                                    }, 0);


                                } catch (Exception e) {
                                    Misc.Debug("WriteUI-Compress: " + e.toString());
                                }
                            }
                        });


                    }
                };

                if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (PermissionRequest == null)
                        PermissionRequest = new PermissionDialog(Activity);
                    if (!PermissionRequest.isShowing()) {
                        PermissionRequest.SetContentView(R.drawable.z_general_permission_storage, R.string.WriteUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener() {
                            @Override
                            public void OnChoice(boolean Allow) {
                                if (!Allow) {
                                    Misc.ToastOld(Misc.String(R.string.PermissionStorage));
                                    return;
                                }
                            }
                        });
                        break;
                    }
                }
                Activity.GetManager().OpenView(new GalleryViewUI(1, GalleryViewUI.TYPE_IMAGE, L), "GalleryViewUI", true);

                break;

            }

            case R.id.ImageButtonCreate: {

            }
        }


    }


}
