package co.biogram.main.ui.chat;

import android.Manifest;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.CropViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.EditTextTag;
import co.biogram.main.ui.view.PermissionDialog;

public class Chat_GroupCreate_UI extends FragmentView {


    private PermissionDialog PermissionRequest;

    private List<String> temp = new ArrayList<>();

    public Chat_GroupCreate_UI(ArrayList<Contact_UI.ContactEntity> members) {

        for (Contact_UI.ContactEntity item : members) {
            temp.add(item.Username);
        }

    }


    @Override
    public void OnCreate() {

        final View view = View.inflate(Activity, R.layout.chat_group_create, null);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();

            }
        });
        view.findViewById(R.id.ImageButtonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Misc.ToastOld("Created");
            }
        });
        view.findViewById(R.id.ImageViewProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View imageViewProfile) {

//                final GalleryViewUI.GalleryListener L = new GalleryViewUI.GalleryListener() {
//                    String ImageURL;
//
//                    @Override
//                    public void OnSelection(String URL) {
//                        ImageURL = URL;
//                    }
//
//                    @Override
//                    public void OnRemove(String URL) {
//                        ImageURL = null;
//                    }
//
//                    @Override
//                    public void OnSave() {
//                        if (ImageURL == null)
//                            return;
//
//
//                        AsyncTask.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    int Size = Misc.ToDP(320);
//                                    BitmapFactory.Options O = new BitmapFactory.Options();
//                                    O.inJustDecodeBounds = true;
//
//                                    BitmapFactory.decodeFile(ImageURL, O);
//
//                                    O.inSampleSize = Misc.SampleSize(O, Size, Size);
//                                    O.inJustDecodeBounds = false;
//
//                                    Bitmap bitmap = BitmapFactory.decodeFile(ImageURL, O);
//
//                                    final Bitmap finalBitmap = Misc.scale(bitmap);
//
//                                    File selectedFilePath = new File(Misc.Temp(), System.currentTimeMillis() + ".jpg");
//                                    selectedFilePath.createNewFile();
//
//
//                                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
//                                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);
//
//                                    FileOutputStream FOS = new FileOutputStream(selectedFilePath);
//                                    FOS.write(BAOS.toByteArray());
//                                    FOS.flush();
//                                    FOS.close();
//
//
//                                    Misc.UIThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ImageViewPhoto.setImageBitmap(finalBitmap);
//
//                                        }
//                                    }, 0);
//
//
//                                } catch (Exception e) {
//                                    Misc.Debug("WriteUI-Compress: " + e.toString());
//                                }
//                            }
//                        });
//
//
//                    }
//                };
//
//                if (!Misc.CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    if (PermissionRequest == null)
//                        PermissionRequest = new PermissionDialog(Activity);
//                    if (!PermissionRequest.isShowing()) {
//                        PermissionRequest.SetContentView(R.drawable.z_general_permission_storage, R.string.WriteUIPermissionStorage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener() {
//                            @Override
//                            public void OnChoice(boolean Allow) {
//                                if (!Allow) {
//                                    Misc.ToastOld(Misc.String(R.string.PermissionStorage));
//                                    return;
//                                }
//                            }
//                        });
//                        break;
//                    }
//                }
//                Activity.GetManager().OpenView(new GalleryViewUI(1, GalleryViewUI.TYPE_IMAGE, L), "GalleryViewUI", true);

                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_avatar, null);

                dialogView.findViewById(R.id.ImageViewClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.TextViewCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        new PermissionDialog(Activity).SetContentView(R.drawable.z_general_permission_camera, R.string.SocialProfileUIAvatarCameraMessage, Manifest.permission.CAMERA, new PermissionDialog.OnChoiceListener() {
                            @Override
                            public void OnChoice(boolean Result) {
                                if (!Result) {
                                    Misc.ToastOld(R.string.SocialProfileUIAvatarCameraMessage);
                                    return;
                                }

                                Activity.GetManager().OpenView(new CameraViewUI(Misc.ToDP(250), Misc.ToDP(250), true, new CameraViewUI.OnCaptureListener() {
                                    @Override
                                    public void OnCapture(Bitmap bitmap) {
                                        ((ImageView) view.findViewById(R.id.ImageViewProfile)).setImageBitmap(bitmap);
                                    }
                                }), "CameraViewUI", true);
                            }
                        });
                    }
                });

                dialogView.findViewById(R.id.TextViewGallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        new PermissionDialog(Activity).SetContentView(R.drawable.z_general_permission_storage, R.string.SocialProfileUIAvatarGalleryMessage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener() {
                            @Override
                            public void OnChoice(boolean Result) {
                                if (!Result) {
                                    Misc.ToastOld(R.string.SocialProfileUIAvatarGalleryMessage);
                                    return;
                                }

                                Activity.GetManager().OpenView(new GalleryViewUI(1, GalleryViewUI.TYPE_IMAGE, new GalleryViewUI.GalleryListener() {
                                    private String Path = "";

                                    @Override
                                    public void OnSelection(String path) {
                                        Path = path;
                                    }

                                    @Override
                                    public void OnRemove(String path) {
                                        Path = path;
                                    }

                                    @Override
                                    public void OnSave() {
                                        Activity.GetManager().OpenView(new CropViewUI(Path, true, new CropViewUI.OnCropListener() {
                                            @Override
                                            public void OnCrop(Bitmap bitmap) {
                                                Activity.onBackPressed();
                                                ((ImageView) imageViewProfile).setImageBitmap(bitmap);

                                            }
                                        }), "CropViewUI", true);
                                    }
                                }), "CameraViewUI", true);
                            }
                        });
                    }
                });

                dialogView.findViewById(R.id.TextViewDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //CircleImageViewProfile.setImageResource(R.drawable.z_social_profile_avatar);

                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });


        final EditText EditTextName = view.findViewById(R.id.EditTextName);
        Misc.SetCursorColor(EditTextName, R.color.Primary);
        ViewCompat.setBackgroundTintList(EditTextName, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

        EditTextName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final Dialog dialog = new Dialog(Activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);

                    View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_name, null);

                    dialogView.findViewById(R.id.ImageViewClose).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final EditText editTextName = dialogView.findViewById(R.id.EditTextName);
                    editTextName.setText(EditTextName.getText());
                    editTextName.setSelection(EditTextName.length());

                    Misc.SetCursorColor(editTextName, R.color.Primary);
                    ViewCompat.setBackgroundTintList(editTextName, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

                    if (dialog.getWindow() != null)
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                    dialogView.findViewById(R.id.TextViewSubmit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            EditTextName.setText(editTextName.getText());
                        }
                    });

                    dialog.setContentView(dialogView);
                    dialog.show();
                }

                return true;
            }

        });
        EditTextTag EditTagMembers = view.findViewById(R.id.MembersList);

        EditTagMembers.hideEditText();

        EditTagMembers.AddTag(temp, null);


        Misc.SetCursorColor(EditTextName, R.color.Primary);
        ViewCompat.setBackgroundTintList(EditTextName, ColorStateList.valueOf(Misc.Color(R.color.Primary)));


        ViewMain = view;

    }

}
