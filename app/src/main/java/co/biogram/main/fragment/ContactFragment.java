package co.biogram.main.fragment;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.PermissionHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;

public class ContactFragment extends Fragment
{
    private final List<Struct> ContactList = new ArrayList<>();
    private AdapterContact ContactAdapter;

    private PermissionHandler _PermissionHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setClickable(true);
        RelativeLayoutMain.setBackgroundResource(R.color.White);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.ContactFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setId(MiscHandler.GenerateViewID());
        ViewLine.setBackgroundResource(R.color.Gray2);

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(context);

        RecyclerView RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.setAdapter(ContactAdapter = new AdapterContact(context));
        RecyclerViewMain.addOnScrollListener(new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {

            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final RelativeLayout RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        RelativeLayoutMain.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewDataParam);

        RelativeLayoutLoading.addView(LoadingViewMain);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryParam);
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain); } });

        RelativeLayoutLoading.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("ContactFragment");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (_PermissionHandler != null)
            _PermissionHandler.GetRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void RetrieveDataFromServer(final Context context, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        _PermissionHandler = new PermissionHandler(Manifest.permission.READ_CONTACTS, 100, this, new PermissionHandler.PermissionEvent()
        {
            @Override
            public void OnGranted()
            {
                String ContactNumber = "";
                Cursor Contacts = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

                if (Contacts != null)
                {
                    while (Contacts.moveToNext())
                    {
                        ContactNumber += Contacts.getString(Contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + ",";
                    }

                    Contacts.close();
                }

                TextViewTryAgain.setVisibility(View.GONE);
                LoadingViewMain.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("ContactList"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Contacts", ContactNumber)
                .setTag("ContactFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int I = 0; I < ResultList.length(); I++)
                                {
                                    JSONObject Contact = ResultList.getJSONObject(I);

                                    Struct ContactStruct = new Struct();
                                    ContactStruct.Username = Contact.getString("Username");
                                    ContactStruct.Avatar = Contact.getString("Avatar");
                                    ContactStruct.Follow = Contact.getBoolean("Follow");

                                    ContactList.add(ContactStruct);
                                }

                                ContactAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }

                        LoadingViewMain.Stop();
                        TextViewTryAgain.setVisibility(View.GONE);
                        RelativeLayoutLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        LoadingViewMain.Stop();
                        TextViewTryAgain.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }

            @Override
            public void OnFailed()
            {
                MiscHandler.Toast(context, getString(R.string.PermissionReadContact));
            }
        });
    }

    private class AdapterContact extends RecyclerView.Adapter<AdapterContact.ViewHolderFollowing>
    {
        private final int IDProfile = MiscHandler.GenerateViewID();
        private final int IDUsername = MiscHandler.GenerateViewID();
        private final int IDLayout = MiscHandler.GenerateViewID();
        private final int IDFollow = MiscHandler.GenerateViewID();
        private final int IDLoading = MiscHandler.GenerateViewID();
        private final int IDLine = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterContact(Context c)
        {
            context = c;
        }

        class ViewHolderFollowing extends RecyclerView.ViewHolder
        {
            ImageViewCircle ImageViewCircleProfile;
            TextView TextViewUsername;
            RelativeLayout RelativeLayoutFollow;
            TextView TextViewFollow;
            LoadingView LoadingViewFollow;
            View ViewLine;

            ViewHolderFollowing(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(IDProfile);
                    TextViewUsername = (TextView) view.findViewById(IDUsername);
                    RelativeLayoutFollow = (RelativeLayout) view.findViewById(IDLayout);
                    TextViewFollow = (TextView) view.findViewById(IDFollow);
                    LoadingViewFollow = (LoadingView) view.findViewById(IDLoading);
                    ViewLine = view.findViewById(IDLine);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderFollowing Holder, int position)
        {
            if (getItemViewType(position) == 1)
                return;

            final int Position = Holder.getAdapterPosition();

            Glide.with(context)
            .load(ContactList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .override(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40))
            .dontAnimate()
            .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SharedHandler.GetString(context, "Username").equals(ContactList.get(Position).Username))
                        return;

                    Bundle bundle = new Bundle();
                    bundle.putString("Username", ContactList.get(Position).Username);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commit();
                }
            });

            Holder.TextViewUsername.setText(ContactList.get(Position).Username);
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SharedHandler.GetString(context, "Username").equals(ContactList.get(Position).Username))
                        return;

                    Bundle bundle = new Bundle();
                    bundle.putString("Username", ContactList.get(Position).Username);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commit();
                }
            });

            if (ContactList.get(Position).Follow)
            {
                Holder.TextViewFollow.setText(getString(R.string.ContactFragmentFollowing));
                Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray6));
            }
            else
            {
                Holder.TextViewFollow.setText(getString(R.string.ContactFragmentFollow));
                Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            }

            Holder.RelativeLayoutFollow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Holder.TextViewFollow.setVisibility(View.GONE);
                    Holder.LoadingViewFollow.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("Follow"))
                    .addBodyParameter("Username", ContactList.get(Position).Username)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("ContactFragment")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    if (Result.getBoolean("Follow"))
                                    {
                                        Holder.TextViewFollow.setText(getString(R.string.ContactFragmentFollowing));
                                        Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray6));
                                    }
                                    else
                                    {
                                        Holder.TextViewFollow.setText(getString(R.string.ContactFragmentFollow));
                                        Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                                    }

                                    ContactAdapter.notifyItemChanged(Position);
                                }
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("ContactFragment: " + e.toString());
                            }

                            Holder.LoadingViewFollow.Stop();
                            Holder.TextViewFollow.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            Holder.LoadingViewFollow.Stop();
                            Holder.TextViewFollow.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            if (Position == ContactList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderFollowing onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutFollow = new RelativeLayout(context);

                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(IDProfile);

                RelativeLayoutMain.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                TextViewUsernameParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutFollow.getId());
                TextViewUsernameParam.addRule(RelativeLayout.CENTER_VERTICAL);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(IDUsername);

                RelativeLayoutMain.addView(TextViewUsername);

                RelativeLayout.LayoutParams RelativeLayoutFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
                RelativeLayoutFollowParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                RelativeLayoutFollowParam.addRule(RelativeLayout.CENTER_VERTICAL);
                RelativeLayoutFollowParam.setMargins(0, 0, MiscHandler.ToDimension(context, 10), 0);

                RelativeLayoutFollow.setLayoutParams(RelativeLayoutFollowParam);
                RelativeLayoutFollow.setId(MiscHandler.GenerateViewID());
                RelativeLayoutFollow.setId(IDLayout);

                RelativeLayoutMain.addView(RelativeLayoutFollow);

                RelativeLayout.LayoutParams TextViewFollowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(TextViewFollowParam);
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewFollow.setTypeface(null, Typeface.BOLD);
                TextViewFollow.setId(IDFollow);

                RelativeLayoutFollow.addView(TextViewFollow);

                RelativeLayout.LayoutParams LoadingViewFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 60), RelativeLayout.LayoutParams.WRAP_CONTENT);
                LoadingViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                LoadingView LoadingViewFollow = new LoadingView(context);
                LoadingViewFollow.setLayoutParams(LoadingViewFollowParam);
                LoadingViewFollow.SetScale(1.7f);
                LoadingViewFollow.SetColor(R.color.BlueLight);
                LoadingViewFollow.setId(IDLoading);

                RelativeLayoutFollow.addView(LoadingViewFollow);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ImageViewCircleProfile.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(IDLine);

                RelativeLayoutMain.addView(ViewLine);

                return new ViewHolderFollowing(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderFollowing(LoadingViewMain, false);
        }

        @Override
        public int getItemViewType(int position)
        {
            return ContactList.get(position)!= null ? 0 : 1;
        }

        @Override
        public int getItemCount()
        {
            return ContactList.size();
        }
    }

    private class Struct
    {
        String Username;
        String Avatar;
        boolean Follow;
    }
}
