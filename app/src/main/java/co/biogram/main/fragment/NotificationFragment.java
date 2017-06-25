package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;

public class NotificationFragment extends Fragment
{
    private RecyclerView RecyclerViewNotification;
    private LoadingView LoadingViewNotification;
    private TextView TextViewTryAgain;

    private AdapterNotification Adapter;
    private final List<Struct> NotificationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        LinearLayoutManager LinearLayoutManagerNotification = new LinearLayoutManager(context);

        RecyclerViewNotification = new RecyclerView(context);
        RecyclerViewNotification.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewNotification.setLayoutManager(LinearLayoutManagerNotification);
        RecyclerViewNotification.setAdapter(Adapter = new AdapterNotification(context));
        RecyclerViewNotification.addOnScrollListener(new RecyclerViewScroll(LinearLayoutManagerNotification)
        {
            @Override
            public void OnLoadMore()
            {
                MiscHandler.Debug("Called OnLoadMore");
            }
        });

        RelativeLayoutMain.addView(RecyclerViewNotification);

        RelativeLayout.LayoutParams LoadingViewNotificationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LoadingViewNotificationParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNotification = new LoadingView(context);
        LoadingViewNotification.setLayoutParams(LoadingViewNotificationParam);

        RelativeLayoutMain.addView(LoadingViewNotification);

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context); } });

        RelativeLayoutMain.addView(TextViewTryAgain);

        RetrieveDataFromServer(context);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("NotificationFragment");
    }

    private void RetrieveDataFromServer(final Context context)
    {
        RecyclerViewNotification.setVisibility(View.GONE);
        LoadingViewNotification.Start();
        TextViewTryAgain.setVisibility(View.GONE);

        AndroidNetworking.post(URLHandler.GetURL("Notification"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("NotificationFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                RecyclerViewNotification.setVisibility(View.VISIBLE);
                LoadingViewNotification.setVisibility(View.GONE);
                TextViewTryAgain.setVisibility(View.GONE);
                MiscHandler.Debug(Response);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject Comment = ResultList.getJSONObject(I);

                            Struct NotificationStruct = new Struct();
                            NotificationStruct.Username = Comment.getString("Username");
                            NotificationStruct.Avatar = Comment.getString("Avatar");
                            NotificationStruct.Type = Comment.getInt("Type");
                            NotificationStruct.Time = Comment.getInt("Time");

                            NotificationList.add(NotificationStruct);
                        }
                    }

                    Adapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("FragmentProfileComment - L225 - " + e.toString());
                }
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewNotification.setVisibility(View.GONE);
                TextViewTryAgain.setVisibility(View.VISIBLE);

                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }

    private class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolderMain>
    {
        private final int ID_Profile = MiscHandler.GenerateViewID();
        private final int ID_Username = MiscHandler.GenerateViewID();
        private final int ID_Message = MiscHandler.GenerateViewID();
        private final int ID_Time = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterNotification(Context c)
        {
            context = c;
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            private ImageViewCircle ImageViewCircleProfile;
            private TextView TextViewUsername;
            private TextView TextViewMessage;
            private TextView TextViewTime;

            ViewHolderMain(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_Profile);
                    TextViewUsername = (TextView) view.findViewById(ID_Username);
                    TextViewMessage = (TextView) view.findViewById(ID_Message);
                    TextViewTime = (TextView) view.findViewById(ID_Time);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderMain Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();

            Glide.with(context)
                    .load(NotificationList.get(Position).Avatar)
                    .placeholder(R.color.BlueGray)
                    .dontAnimate()
                    .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", NotificationList.get(Position).Username);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            String Message = "";

            switch (NotificationList.get(Position).Type)
            {
                case 1: Message = context.getString(R.string.NotificationFragmentPostTag);     break;
                case 2: Message = context.getString(R.string.NotificationFragmentPostLike);    break;
                case 3: Message = context.getString(R.string.NotificationFragmentFollow);      break;
                case 4: Message = context.getString(R.string.NotificationFragmentCommentLike); break;
                case 5: Message = context.getString(R.string.NotificationFragmentComment);     break;
                case 6: Message = context.getString(R.string.NotificationFragmentCommentTag);  break;
            }

            Holder.TextViewMessage.setText(Message);

            Holder.TextViewUsername.setText(NotificationList.get(Position).Username);
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", NotificationList.get(Position).Username);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewTime.setText(MiscHandler.GetTimeName(NotificationList.get(Position).Time));
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_Profile);

                RelativeLayoutMain.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTypeface(null, Typeface.BOLD);
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_Username);

                RelativeLayoutMain.addView(TextViewUsername);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_Username);

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewMessage.setId(ID_Message);
                TextViewMessage.setText(getString(R.string.FragmentProfileCommentMessage));

                RelativeLayoutMain.addView(TextViewMessage);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(TextViewTimeParam);
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_Time);

                RelativeLayoutMain.addView(TextViewTime);

                return new ViewHolderMain(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new LoadingView.LayoutParams(LoadingView.LayoutParams.MATCH_PARENT, LoadingView.LayoutParams.MATCH_PARENT));
            LoadingViewMain.Start();

            return new ViewHolderMain(LoadingViewMain, false);
        }

        @Override
        public int getItemCount()
        {
            return NotificationList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return NotificationList.get(position) == null ? 1 : 0;
        }
    }

    private class Struct
    {
        private String Username;
        private String Avatar;
        private int Type;
        private int Time;
    }
}
