package co.biogram.main.fragment;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
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
        LoadingViewNotification.setVisibility(View.VISIBLE);
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
        private final int ID_Main = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterNotification(Context c)
        {
            context = c;
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            ViewHolderMain(View view, boolean Content)
            {
                super(view);

                if (Content)
                {

                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderMain Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();


        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayoutMain.setId(ID_Main);

                return new ViewHolderMain(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new LoadingView.LayoutParams(LoadingView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
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

    }
}
