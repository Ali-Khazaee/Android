package co.biogram.main.ui.chat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.FlowLayoutManager;
import co.biogram.main.ui.general.CameraViewUI;
import co.biogram.main.ui.general.CropViewUI;
import co.biogram.main.ui.general.GalleryViewUI;
import co.biogram.main.ui.view.PermissionDialog;

import java.util.ArrayList;

public class Chat_GroupCreate_UI extends FragmentView
{

    private ArrayList<String> temp = new ArrayList<>();

    public Chat_GroupCreate_UI(ArrayList<Contact_UI.ContactEntity> members)
    {

        for (Contact_UI.ContactEntity item : members)
        {
            temp.add(item.Username);
        }

    }

    @Override
    public void OnPause()
    {
        super.OnPause();
        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Activity.findViewById(android.R.id.content).getWindowToken(), 0);

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

                                        Activity.GetManager().OpenView(new CropViewUI(Path, false, new CropViewUI.OnCropListener()
                                        {
                                            @Override
                                            public void OnCrop(Bitmap bitmap)
                                            {
                                            }
                                        }), "CropViewUI", true);

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

                        ((ImageView) view.findViewById(R.id.ImageViewProfile)).setImageResource(R.drawable.camera_bluegray2);
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
                            InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(Activity.findViewById(android.R.id.content).getWindowToken(), 0);
                            EditTextName.setText(editTextName.getText());
                        }
                    });

                    dialog.setContentView(dialogView);
                    dialog.show();
                }

                return true;
            }

        });
        RecyclerView RecyclerViewMembers = view.findViewById(R.id.MembersList);
        FlowLayoutManager manager = new FlowLayoutManager();
        manager.setAutoMeasureEnabled(true);
        manager = manager.setAlignment(FlowLayoutManager.Alignment.RIGHT);
        RecyclerViewMembers.setLayoutManager(manager);
        RecyclerViewMembers.setAdapter(new MembersAdapter(temp));
        RecyclerViewMembers.addItemDecoration(new RecyclerView.ItemDecoration()
        {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
            {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(8, 8, 8, 8);
            }
        });

        Misc.SetCursorColor(EditTextName, R.color.Primary);
        ViewCompat.setBackgroundTintList(EditTextName, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

        ViewMain = view;

    }

    private class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder>
    {

        private ArrayList<String> members = new ArrayList<>();

        public MembersAdapter(ArrayList<String> members)
        {
            this.members.addAll(members);
        }
        @NonNull
        @Override
        public MembersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new MembersAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_selected_contact_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MembersAdapter.ViewHolder holder, int position)
        {
            holder.bind(position);
        }

        @Override
        public int getItemCount()
        {
            return members.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView Username;

            public ViewHolder(View itemView)
            {
                super(itemView);
                Username = itemView.findViewById(R.id.TextViewUsername);
                Username.setTypeface(Misc.GetTypeface());
            }

            public void bind(final int position)
            {
                Username.setText(members.get(position));
            }

        }
    }

}
