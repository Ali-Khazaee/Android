package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

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

public class FragmentProfileComment extends Fragment
{
    private String Username;
    private boolean LoadingBottom = true;
    private AdapterComment Adapter;
    private List<Struct> CommentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final LinearLayoutManager LinearLayoutManagerComment = new LinearLayoutManager(context);

        Adapter = new AdapterComment(context);
        Username = SharedHandler.GetString(context, "Username");

        if (getArguments() != null && !getArguments().getString("Username", "").equals(""))
            Username = getArguments().getString("Username");

        Struct CommentStruct = new Struct();
        CommentStruct.PostID = "";
        CommentStruct.Username = "alir";
        CommentStruct.Avatar = "";
        CommentStruct.Target = "";
        CommentStruct.Comment = "";
        CommentStruct.Time = 0;

        //CommentList.add(CommentStruct);
        //CommentList.add(CommentStruct);
        //CommentList.add(CommentStruct);

        RecyclerView Root = new RecyclerView(context);
        Root.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setClickable(true);
        Root.setLayoutManager(LinearLayoutManagerComment);
        Root.setAdapter(Adapter);
        Root.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int DX, int DY)
            {
                if (!LoadingBottom && (LinearLayoutManagerComment.findLastVisibleItemPosition() + 5) > LinearLayoutManagerComment.getItemCount())
                {
                    LoadingBottom = true;
                    CommentList.add(null);
                    Adapter.notifyItemInserted(CommentList.size());

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.PROFILE_POST_GET))
                    .addBodyParameter("Skip", String.valueOf(CommentList.size()))
                    .addBodyParameter("Username", Username)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FragmentProfileComment")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingBottom = false;
                            CommentList.remove(CommentList.size() - 1);
                            Adapter.notifyItemRemoved(CommentList.size());

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < ResultList.length(); K++)
                                    {
                                        JSONObject Comment = ResultList.getJSONObject(K);

                                        Struct CommentStruct = new Struct();
                                        CommentStruct.PostID = Comment.getString("PostID");
                                        CommentStruct.Username = Comment.getString("Username");
                                        CommentStruct.Avatar = Comment.getString("Avatar");
                                        CommentStruct.Target = Comment.getString("Target");
                                        CommentStruct.Comment = Comment.getString("Comment");
                                        CommentStruct.Time = Comment.getInt("Time");

                                        CommentList.add(CommentStruct);
                                    }

                                    Adapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingBottom = false;
                            CommentList.remove(CommentList.size() - 1);
                            Adapter.notifyItemRemoved(CommentList.size());
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            }
        });

        RetrieveDataFromServer(context);

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentProfileComment");
    }

    private void RetrieveDataFromServer(final Context context)
    {
        CommentList.add(null);
        Adapter.notifyItemInserted(CommentList.size());

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.PROFILE_POST_GET))
        .addBodyParameter("Username", Username)
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("FragmentProfileComment")
        .build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                CommentList.remove(CommentList.size() - 1);
                Adapter.notifyItemRemoved(CommentList.size());
                Struct CommentStruct = new Struct();
                CommentStruct.PostID = "";
                CommentStruct.Username = "alir";
                CommentStruct.Avatar = "";
                CommentStruct.Target = "";
                CommentStruct.Comment = "";
                CommentStruct.Time = 0;

                CommentList.add(CommentStruct);
                CommentList.add(CommentStruct);
                CommentList.add(CommentStruct);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < ResultList.length(); K++)
                        {
                            JSONObject Comment = ResultList.getJSONObject(K);

                            Struct PostStruct = new Struct();
                            PostStruct.PostID = Comment.getString("PostID");
                            PostStruct.Username = Comment.getString("Username");
                            PostStruct.Avatar = Comment.getString("Avatar");
                            PostStruct.Target = Comment.getString("Target");
                            PostStruct.Comment = Comment.getString("Comment");
                            PostStruct.Time = Comment.getInt("Time");

                            CommentList.add(PostStruct);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
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
        private final Context context;

        private final int ID_Profile = MiscHandler.GenerateViewID();
        private final int ID_Username = MiscHandler.GenerateViewID();
        private final int ID_Target = MiscHandler.GenerateViewID();
        private final int ID_More = MiscHandler.GenerateViewID();
        private final int ID_Comment = MiscHandler.GenerateViewID();
        private final int ID_Time = MiscHandler.GenerateViewID();

        AdapterComment(Context c)
        {
            context = c;
        }

        class ViewHolderComment extends RecyclerView.ViewHolder
        {
            ViewHolderComment(View view, boolean Content)
            {
                super(view);


            }
        }

        @Override
        public void onBindViewHolder(ViewHolderComment Holder, int Position)
        {
            if (getItemViewType(Position) != 0)
                return;

            final int RealPosition = Holder.getAdapterPosition();

        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout Root = new RelativeLayout(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_Profile);

                Root.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTypeface(null, Typeface.BOLD);
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_Username);
                TextViewUsername.setText("ali");

                Root.addView(TextViewUsername);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_Username);

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewMessage.setId(MiscHandler.GenerateViewID());
                TextViewMessage.setText("Commented in post");

                Root.addView(TextViewMessage);

                RelativeLayout.LayoutParams TextViewTargetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTargetParam.setMargins(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewTargetParam.addRule(RelativeLayout.RIGHT_OF, TextViewMessage.getId());

                TextView TextViewTarget = new TextView(context);
                TextViewTarget.setLayoutParams(TextViewTargetParam);
                TextViewTarget.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTarget.setTypeface(null, Typeface.BOLD);
                TextViewTarget.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewTarget.setId(ID_Target);
                TextViewTarget.setText("alireza");

                Root.addView(TextViewTarget);

                RelativeLayout.LayoutParams ImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 32), MiscHandler.ToDimension(context, 32));
                ImageViewMoreParam.setMargins(0, MiscHandler.ToDimension(context, 10), 0, 0);
                ImageViewMoreParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageViewCircle ImageViewMore = new ImageViewCircle(context);
                ImageViewMore.SetBorderWidth(MiscHandler.ToDimension(context, 3));
                ImageViewMore.setLayoutParams(ImageViewMoreParam);
                ImageViewMore.setPadding(MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1));
                ImageViewMore.setImageResource(R.drawable.ic_option);
                ImageViewMore.setId(ID_More);

                Root.addView(ImageViewMore);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewTimeParam.addRule(RelativeLayout.LEFT_OF, ID_More);

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(TextViewTimeParam);
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.Gray3));

                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_Time);
                TextViewTime.setText("1h");

                Root.addView(TextViewTime);

                RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewCommentParam.addRule(RelativeLayout.RIGHT_OF, ID_Profile);
                TextViewCommentParam.addRule(RelativeLayout.BELOW, ID_More);

                TextView TextViewComment = new TextView(context);
                TextViewComment.setLayoutParams(TextViewCommentParam);
                TextViewComment.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewComment.setId(ID_Comment);
                TextViewComment.setText("hi ali, manam khobam #QQ Ridia dada");

                Root.addView(TextViewComment);

                return new ViewHolderComment(Root, true);
            }
            else if (ViewType == 1)
            {
                LoadingView Root = new LoadingView(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
                Root.Start();

                return new ViewHolderComment(Root, false);
            }
            else
            {
                TextView Root = new TextView(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
                Root.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
                Root.setText(context.getString(R.string.AdapterPostNoContent));
                Root.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                Root.setTypeface(null, Typeface.BOLD);
                Root.setGravity(Gravity.CENTER);

                return new ViewHolderComment(Root, false);
            }
        }

        @Override
        public int getItemCount()
        {
            if (CommentList.size() == 0)
                return 1;

            return CommentList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            if (CommentList.size() == 0)
                return 2;

            return CommentList.get(position)!= null ? 0 : 1;
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
