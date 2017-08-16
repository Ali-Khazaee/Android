package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;
import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationFragment extends Fragment
{
    private final List<Struct> NotificationList = new ArrayList<>();
    private RecyclerViewScroll RecyclerViewScrollMain;
    private AdapterNotification Adapter;

    private boolean NotificationTurn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
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

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(getString(R.string.NotificationFragment));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewMoreParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        final ImageView ImageViewMore = new ImageView(context);
        ImageViewMore.setLayoutParams(ImageViewMoreParam);
        ImageViewMore.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewMore.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewMore.setImageResource(R.drawable.ic_more_blue);
        ImageViewMore.setId(MiscHandler.GenerateViewID());
        ImageViewMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu PopMenu = new PopupMenu(context, ImageViewMore);

                if (NotificationTurn)
                    PopMenu.getMenu().add(getString(R.string.NotificationFragmentTurnOn));
                else
                    PopMenu.getMenu().add(getString(R.string.NotificationFragmentTurnOff));

                PopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("Notification"))
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("NotificationFragment")
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
                                        NotificationTurn = Result.getBoolean("Notification");

                                        if (NotificationTurn)
                                            SharedHandler.SetBoolean(context, "Notification", true);
                                        else
                                            SharedHandler.SetBoolean(context, "Notification", false);
                                    }
                                }
                                catch (Exception e)
                                {
                                    MiscHandler.Debug("NotificationFragment-RequestNotification: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        return false;
                    }
                });
                PopMenu.show();
            }
        });

        RelativeLayoutHeader.addView(ImageViewMore);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSearchParam.addRule(RelativeLayout.LEFT_OF, ImageViewMore.getId());

        ImageView ImageViewSearch = new ImageView(context);
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
        ImageViewSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, new SearchFragment()).addToBackStack("SearchFragment").commit();
            }
        });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(context);

        RecyclerViewScrollMain = new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {
                NotificationList.add(null);
                Adapter.notifyItemInserted(NotificationList.size());

                AndroidNetworking.post(MiscHandler.GetRandomServer("NotificationList"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Skip", String.valueOf(NotificationList.size() - 1))
                .setTag("NotificationFragment")
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
                            RecyclerViewScrollMain.ResetLoading(false);
                            MiscHandler.Debug("Notification-RequestMore: " + e.toString());
                        }

                        NotificationList.remove(NotificationList.size() - 1);
                        Adapter.notifyItemRemoved(NotificationList.size());
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        RecyclerViewScrollMain.ResetLoading(false);
                        NotificationList.remove(NotificationList.size() - 1);
                        Adapter.notifyItemRemoved(NotificationList.size());
                    }
                });
            }
        };

        RecyclerView RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.setAdapter(Adapter = new AdapterNotification(context));
        RecyclerViewMain.addOnScrollListener(RecyclerViewScrollMain);

        RelativeLayoutMain.addView(RecyclerViewMain);

        RelativeLayout.LayoutParams LoadingViewNotificationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LoadingViewNotificationParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewNotificationParam);

        RelativeLayoutMain.addView(LoadingViewMain);

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context, LoadingViewMain, TextViewTryAgain); } });

        RelativeLayoutMain.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("NotificationFragment");
    }

    private void RetrieveDataFromServer(final Context context, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("NotificationList"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("NotificationFragment")
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
                            JSONObject Notification = ResultList.getJSONObject(I);

                            Struct NotificationStruct = new Struct();
                            NotificationStruct.Username = Notification.getString("Username");
                            NotificationStruct.Avatar = Notification.getString("Avatar");
                            NotificationStruct.Type = Notification.getInt("Type");
                            NotificationStruct.Time = Notification.getInt("Time");

                            NotificationList.add(NotificationStruct);
                        }

                        NotificationTurn = Result.getBoolean("Notification");

                        if (NotificationTurn)
                            SharedHandler.SetBoolean(context, "Notification", true);
                        else
                            SharedHandler.SetBoolean(context, "Notification", false);
                    }

                    Adapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    RecyclerViewScrollMain.ResetLoading(false);
                    MiscHandler.Debug("Notification-RequestStart: " + e.toString());
                }

                LoadingViewMain.setVisibility(View.GONE);
                TextViewTryAgain.setVisibility(View.GONE);
                ShortcutBadger.removeCount(context);
            }

            @Override
            public void onError(ANError anError)
            {
                RecyclerViewScrollMain.ResetLoading(false);
                LoadingViewMain.setVisibility(View.GONE);
                TextViewTryAgain.setVisibility(View.VISIBLE);
            }
        });
    }

    private class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolderMain>
    {
        private final int ID_Profile = MiscHandler.GenerateViewID();
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
            private TextView TextViewMessage;
            private TextView TextViewTime;

            ViewHolderMain(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_Profile);
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

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            String Message = NotificationList.get(Position).Username + " ";

            switch (NotificationList.get(Position).Type)
            {
                case 1: Message += context.getString(R.string.NotificationFragmentPostTag);     break;
                case 2: Message += context.getString(R.string.NotificationFragmentPostLike);    break;
                case 3: Message += context.getString(R.string.NotificationFragmentFollow);      break;
                case 4: Message += context.getString(R.string.NotificationFragmentCommentLike); break;
                case 5: Message += context.getString(R.string.NotificationFragmentComment);     break;
                case 6: Message += context.getString(R.string.NotificationFragmentCommentTag);  break;
            }

            SpannableString SpanMessage = new SpannableString(Message);
            SpanMessage.setSpan(new StyleSpan(Typeface.BOLD), 0, NotificationList.get(Position).Username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Holder.TextViewMessage.setText(SpanMessage);

            Holder.TextViewTime.setText(MiscHandler.GetTimeName(NotificationList.get(Position).Time));
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                TextView TextViewTime = new TextView(context);

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

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);
                TextViewMessageParam.addRule(RelativeLayout.LEFT_OF, ID_Time);

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewMessage.setId(ID_Message);

                RelativeLayoutMain.addView(TextViewMessage);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(0, MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 10), 0);
                TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

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
