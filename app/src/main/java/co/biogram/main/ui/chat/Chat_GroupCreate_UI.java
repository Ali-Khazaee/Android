package co.biogram.main.ui.chat;

import android.Manifest;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.CropViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.EditTextTag;
import co.biogram.main.ui.view.PermissionDialog;

import java.util.ArrayList;
import java.util.List;

public class Chat_GroupCreate_UI extends FragmentView
{

    private List<String> temp = new ArrayList<>();

    public Chat_GroupCreate_UI(ArrayList<Contact_UI.ContactEntity> members)
    {

        for (Contact_UI.ContactEntity item : members)
        {
            temp.add(item.Username);
        }

    }

    @Override
    public void OnCreate()
    {

        final View view = View.inflate(Activity, R.layout.chat_group_create, null);

        ((TextView) view.findViewById(R.id.TextViewName)).setTypeface(Misc.GetTypeface());
        ((TextView) view.findViewById(R.id.TextViewDescription)).setTypeface(Misc.GetTypeface());
        ((TextView) view.findViewById(R.id.TextViewGroup)).setTypeface(Misc.GetTypeface());

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();

            }
        });
        view.findViewById(R.id.ImageButtonCreate).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Misc.ToastOld("Created");
            }
        });
        view.findViewById(R.id.ImageViewProfile).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View imageViewProfile)
            {

                final Dialog dialog = new Dialog(Activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_avatar, null);

                dialogView.findViewById(R.id.ImageViewClose).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.TextViewCamera).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        new PermissionDialog(Activity).SetContentView(R.drawable.z_general_permission_camera, R.string.SocialProfileUIAvatarCameraMessage, Manifest.permission.CAMERA, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.ToastOld(R.string.SocialProfileUIAvatarCameraMessage);
                                    return;
                                }

                                Activity.GetManager().OpenView(new CameraViewUI(Misc.ToDP(250), Misc.ToDP(250), true, new CameraViewUI.OnCaptureListener()
                                {
                                    @Override
                                    public void OnCapture(Bitmap bitmap)
                                    {
                                        ((ImageView) view.findViewById(R.id.ImageViewProfile)).setImageBitmap(bitmap);
                                    }
                                }), "CameraViewUI", true);
                            }
                        });
                    }
                });

                dialogView.findViewById(R.id.TextViewGallery).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        new PermissionDialog(Activity).SetContentView(R.drawable.z_general_permission_storage, R.string.SocialProfileUIAvatarGalleryMessage, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionDialog.OnChoiceListener()
                        {
                            @Override
                            public void OnChoice(boolean Result)
                            {
                                if (!Result)
                                {
                                    Misc.ToastOld(R.string.SocialProfileUIAvatarGalleryMessage);
                                    return;
                                }

                                Activity.GetManager().OpenView(new GalleryViewUI(1, GalleryViewUI.TYPE_IMAGE, new GalleryViewUI.GalleryListener()
                                {
                                    private String Path = "";

                                    @Override
                                    public void OnSelection(String path)
                                    {
                                        Path = path;
                                    }

                                    @Override
                                    public void OnRemove(String path)
                                    {
                                        Path = path;
                                    }

                                    @Override
                                    public void OnSave()
                                    {
                                        ((ImageView) view.findViewById(R.id.ImageViewProfile)).setImageBitmap(BitmapFactory.decodeFile(Path));

                                        Activity.GetManager().OpenView(new CropViewUI(Path, false, new CropViewUI.OnCropListener() {
                                            @Override
                                            public void OnCrop(Bitmap bitmap) {
                                            }
                                        }), "CropViewUI", true);

                                        Log.d("KIRI","RUN MISHI?");
                                    }
                                }), "GalaryViewUI", true);
                            }
                        });
                    }
                });

                dialogView.findViewById(R.id.TextViewDelete).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        final EditText EditTextName = view.findViewById(R.id.EditTextName);
        Misc.SetCursorColor(EditTextName, R.color.Primary);
        EditTextName.setTypeface(Misc.GetTypeface());
        ViewCompat.setBackgroundTintList(EditTextName, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

        EditTextName.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    final Dialog dialog = new Dialog(Activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);

                    View dialogView = View.inflate(Activity, R.layout.social_profile_dialog_name, null);

                    dialogView.findViewById(R.id.ImageViewClose).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
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

                    dialogView.findViewById(R.id.TextViewSubmit).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
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
