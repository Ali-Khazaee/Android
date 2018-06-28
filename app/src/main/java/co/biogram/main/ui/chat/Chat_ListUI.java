package co.biogram.main.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import co.biogram.main.R;
import co.biogram.main.activity.ChatActivity;
import co.biogram.main.fragment.FragmentView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by soh_mil97
 */


public class Chat_ListUI extends FragmentView implements View.OnClickListener {

    private ImageButton ImageButtonSearch;
    private FloatingActionButton FABAdd;

    private RecyclerView RecyclerView;
    private ChatListAdapter ChatListAdapter;
    private ArrayList<ChatEntity> ChatItems;

    public static void start(Context context) {
        Intent start = new Intent(context, Chat_ListUI.class);
        context.startActivity(start);
    }

    @Override
    public void OnCreate() {

        View view = View.inflate(Activity, R.layout.social_chat_list, null);

        RecyclerView = view.findViewById(R.id.RecyclerViewChatList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity);

        ChatItems = new ArrayList<>();
        setData();

        ChatListAdapter = new ChatListAdapter(ChatItems);

        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(ChatListAdapter);


        ImageButtonSearch = view.findViewById(R.id.ImageButtonSearch);
        FABAdd = view.findViewById(R.id.FABNewMessage);

        FABAdd.setOnClickListener(this);

        ViewMain = view;
    }


    private void setData() {

        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "10", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "100", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1000", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));
        ChatItems.add(new ChatEntity(R.drawable.z_social_profile_avatar, "UserName", "Test Message For Fake List", "1", "1 MIN AGO"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FABNewMessage: {
                Activity.GetManager().OpenView(new ContactUI(), "ContactUI", true);
                break;
            }
        }
    }


    private class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

        private ArrayList<ChatEntity> ChatItems;

        public ChatListAdapter(ArrayList<ChatEntity> chat_list) {
            this.ChatItems = chat_list;
        }

        @Override
        public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_model, parent, false));
        }

        @Override
        public void onBindViewHolder(ChatListAdapter.ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return ChatItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            CircleImageView ProfileImage;
            TextView Username;
            TextView Message;
            TextView Time;
            TextView MessageCount;

            public ViewHolder(View itemView) {
                super(itemView);

                ProfileImage = itemView.findViewById(R.id.ProfileImage);
                Username = itemView.findViewById(R.id.Username);
                Message = itemView.findViewById(R.id.Message);
                Time = itemView.findViewById(R.id.Time);
                MessageCount = itemView.findViewById(R.id.NewMessageCount);

            }

            public void bind(int position) {
                ChatEntity model = ChatItems.get(position);
                ProfileImage.setImageResource(model.getProfile());
                Username.setText(model.getUsername());
                Message.setText(model.Message);
                Time.setText(model.Time);
                MessageCount.setText(model.MessageCount);
                itemView.setOnClickListener(this);


            }

            @Override
            public void onClick(View v) {
                ChatActivity.start(v.getContext());
            }
        }
    }


    private class ChatEntity {

        int ID;
        int Profile;
        String Username;
        String Time;
        String Message;
        String MessageCount;

        public ChatEntity(int profile, String username, String message, String count, String time) {
            this.Profile = profile;
            this.Username = username;
            this.Message = message;
            this.MessageCount = count;
            this.Time = time;
        }

        public int getProfile() {
            return Profile;
        }

        public void setProfile(int profile) {
            this.Profile = profile;
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public String getMessageCount() {
            return MessageCount;
        }

        public void setMessageCount(String messageCount) {
            this.MessageCount = messageCount;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String time) {
            Time = time;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }


}
