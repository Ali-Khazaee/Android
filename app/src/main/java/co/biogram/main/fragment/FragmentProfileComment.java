package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;

public class FragmentProfileComment extends Fragment
{
    private RecyclerView RecyclerViewMoment;
    private TextView TextViewEmpty;

    private String Username;
    private AdapterComment Adapter;
    private final List<Struct> CommentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        Adapter = new AdapterComment(context);
        Adapter.setHasStableIds(true);

        if (getArguments() != null && !getArguments().getString("Username", "").equals(""))
            Username = getArguments().getString("Username");
        else
            Username = SharedHandler.GetString(context, "Username");

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        LinearLayoutManager LinearLayoutManagerComment = new LinearLayoutManager(context);

        RecyclerViewMoment = new RecyclerView(context);
        RecyclerViewMoment.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewMoment.setLayoutManager(LinearLayoutManagerComment);
        RecyclerViewMoment.setNestedScrollingEnabled(false);
        RecyclerViewMoment.setAdapter(Adapter);
        RecyclerViewMoment.addOnScrollListener(new RecyclerViewScroll(LinearLayoutManagerComment)
        {
            @Override
            public void OnLoadMore()
            {
                MiscHandler.Debug("Loaded");
                CommentList.add(null);
                Adapter.notifyItemInserted(CommentList.size());

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.PROFILE_COMMENT_GET))
                .addBodyParameter("Skip", String.valueOf(CommentList.size()))
                .addBodyParameter("Username", Username)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("FragmentProfileComment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        CommentList.remove(CommentList.size() - 1);
                        Adapter.notifyItemRemoved(CommentList.size());

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int I = 0; I < ResultList.length(); I++)
                                {
                                    JSONObject Comment = ResultList.getJSONObject(I);

                                    Struct CommentStruct = new Struct();
                                    CommentStruct.PostID = Comment.getString("PostID");
                                    CommentStruct.Username = Comment.getString("Username");
                                    CommentStruct.Avatar = Comment.getString("Avatar");
                                    CommentStruct.Target = Comment.getString("Target");
                                    CommentStruct.Comment = Comment.getString("Comment");
                                    CommentStruct.Time = Comment.getInt("Time");

                                    CommentList.add(CommentStruct);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("FragmentProfileComment - L126 - " + e.toString());
                        }

                        CheckIfEmpty();
                        Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        CommentList.remove(CommentList.size() - 1);
                        Adapter.notifyItemRemoved(CommentList.size());
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMoment);

        TextViewEmpty = new TextView(context);
        TextViewEmpty.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        TextViewEmpty.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewEmpty.setText(context.getString(R.string.FragmentProfileCommentEmpty));
        TextViewEmpty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewEmpty.setTypeface(null, Typeface.BOLD);
        TextViewEmpty.setVisibility(View.GONE);
        TextViewEmpty.setGravity(Gravity.CENTER);

        RelativeLayoutMain.addView(TextViewEmpty);

        RetrieveDataFromServer(context);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentProfileComment");
    }

    private void CheckIfEmpty()
    {
        if (CommentList.size() > 0)
        {
            RecyclerViewMoment.setVisibility(View.VISIBLE);
            TextViewEmpty.setVisibility(View.GONE);
        }
        else
        {
            RecyclerViewMoment.setVisibility(View.GONE);
            TextViewEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void RetrieveDataFromServer(final Context context)
    {
        CommentList.add(null);
        Adapter.notifyItemInserted(CommentList.size());

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.PROFILE_COMMENT_GET))
        .addBodyParameter("Username", Username)
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("FragmentProfileComment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                CommentList.remove(CommentList.size() - 1);
                Adapter.notifyItemRemoved(CommentList.size());

                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject Comment = ResultList.getJSONObject(I);

                            Struct PostStruct = new Struct();
                            PostStruct.PostID = Comment.getString("PostID");
                            PostStruct.Username = Comment.getString("Username");
                            PostStruct.Avatar = Comment.getString("Avatar");
                            PostStruct.Target = Comment.getString("Target");
                            PostStruct.Comment = Comment.getString("Comment");
                            PostStruct.Time = Comment.getInt("Time");

                            CommentList.add(PostStruct);
                        }
                    }

                    CheckIfEmpty();
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
                CommentList.remove(CommentList.size() - 1);
                Adapter.notifyItemRemoved(CommentList.size());
                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }

    private class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolderComment>
    {
        private final int ID_Main = MiscHandler.GenerateViewID();
        private final int ID_Profile = MiscHandler.GenerateViewID();
        private final int ID_Username = MiscHandler.GenerateViewID();
        private final int ID_Target = MiscHandler.GenerateViewID();
        private final int ID_More = MiscHandler.GenerateViewID();
        private final int ID_Comment = MiscHandler.GenerateViewID();
        private final int ID_Time = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterComment(Context c)
        {
            context = c;
        }

        class ViewHolderComment extends RecyclerView.ViewHolder
        {
            private RelativeLayout RelativeLayoutMain;
            private ImageViewCircle ImageViewCircleProfile;
            private TextView TextViewUsername;
            private TextView TextViewTarget;
            private ImageView ImageViewMore;
            private TextView TextViewTime;
            private TextView TextViewComment;

            ViewHolderComment(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    RelativeLayoutMain = (RelativeLayout) view.findViewById(ID_Main);
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_Profile);
                    TextViewUsername = (TextView) view.findViewById(ID_Username);
                    TextViewTarget = (TextView) view.findViewById(ID_Target);
                    ImageViewMore = (ImageView) view.findViewById(ID_More);
                    TextViewTime = (TextView) view.findViewById(ID_Time);
                    TextViewComment = (TextView) view.findViewById(ID_Comment);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();

            Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", CommentList.get(Position).PostID);

                    Fragment fragment = new FragmentPostDetails();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentPostDetails").commit();
                }
            });

            Glide.with(context)
                    .load(CommentList.get(Position).Avatar)
                    .placeholder(R.color.BlueGray)
                    .dontAnimate()
                    .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", CommentList.get(Position).Target);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewUsername.setText(CommentList.get(Position).Username);
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", CommentList.get(Position).Username);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewTarget.setText(CommentList.get(Position).Target);
            Holder.TextViewTarget.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", CommentList.get(Position).Target);

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewTime.setText(MiscHandler.GetTimeName(CommentList.get(Position).Time));

            Holder.ImageViewMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PopupMenu PopMenu = new PopupMenu(context, Holder.ImageViewMore);
                    PopMenu.getMenu().add(getString(R.string.FragmentProfileCommentReport));
                    PopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            return false;
                        }
                    });
                    PopMenu.show();
                }
            });

            Holder.TextViewComment.setText(CommentList.get(Position).Comment);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayoutMain.setId(ID_Main);

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
                TextViewMessage.setId(MiscHandler.GenerateViewID());
                TextViewMessage.setText(getString(R.string.FragmentProfileCommentMessage));

                RelativeLayoutMain.addView(TextViewMessage);

                RelativeLayout.LayoutParams TextViewTargetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTargetParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewTargetParam.addRule(RelativeLayout.RIGHT_OF, TextViewMessage.getId());

                TextView TextViewTarget = new TextView(context);
                TextViewTarget.setLayoutParams(TextViewTargetParam);
                TextViewTarget.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTarget.setTypeface(null, Typeface.BOLD);
                TextViewTarget.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewTarget.setId(ID_Target);

                RelativeLayoutMain.addView(TextViewTarget);

                RelativeLayout.LayoutParams ImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 32), MiscHandler.ToDimension(context, 32));
                ImageViewMoreParam.setMargins(0, MiscHandler.ToDimension(context, 10), 0, 0);
                ImageViewMoreParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewMore = new ImageView(context);
                ImageViewMore.setLayoutParams(ImageViewMoreParam);
                ImageViewMore.setPadding(MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1));
                ImageViewMore.setImageResource(R.drawable.ic_option);
                ImageViewMore.setId(ID_More);

                RelativeLayoutMain.addView(ImageViewMore);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewTimeParam.addRule(RelativeLayout.LEFT_OF, ID_More);

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(TextViewTimeParam);
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_Time);

                RelativeLayoutMain.addView(TextViewTime);

                RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewCommentParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);
                TextViewCommentParam.addRule(RelativeLayout.BELOW, ID_More);

                TextView TextViewComment = new TextView(context);
                TextViewComment.setLayoutParams(TextViewCommentParam);
                TextViewComment.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewComment.setId(ID_Comment);

                RelativeLayoutMain.addView(TextViewComment);

                return new ViewHolderComment(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new LoadingView.LayoutParams(LoadingView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderComment(LoadingViewMain, false);
        }

        @Override
        public int getItemCount()
        {
            return CommentList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return CommentList.get(position) == null ? 1 : 0;
        }
    }

    private class Struct
    {
        private String PostID;
        private String Username;
        private String Avatar;
        private String Target;
        private String Comment;
        private int Time;
    }
}
