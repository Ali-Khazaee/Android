package co.biogram.main.ui.chat;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.CircleImageView;

import java.util.ArrayList;

public class Message_UI extends FragmentView
{

    @Override
    public void OnResume()
    {
        super.OnResume();

        Activity.findViewById(R.id.LinearLayoutMenu).setVisibility(View.VISIBLE);
        Activity.findViewById(R.id.ViewLine).setVisibility(View.VISIBLE);
    }

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.messenger_chat_list, null);

        final CircleImageView CircleImageViewWrite = view.findViewById(R.id.CircleImageViewWrite);

        CircleImageViewWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new Contact_UI(), "Contact_UI", true);

            }
        });

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        view.findViewById(R.id.ImageViewSearch).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Open Search
                Misc.ToastOld("Search Click Shod");
            }
        });

        ((TextView) view.findViewById(R.id.TextViewToolBar)).setTypeface(Misc.GetTypeface());

        RecyclerView RecyclerViewMain = view.findViewById(R.id.RecyclerViewMain);
        RecyclerViewMain.setLayoutManager(new LinearLayoutManager(Activity));
        RecyclerViewMain.setAdapter(new ContactListAdapter());
        RecyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            private boolean IsLoading = false;

            @Override
            public void onScrolled(RecyclerView view, int DX, int DY)
            {
                if (IsLoading)
                    return;

                if (DY > 0)
                {
                    ViewCompat.animate(CircleImageViewWrite).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setListener(new ViewPropertyAnimatorListener()
                    {
                        @Override
                        public void onAnimationStart(View view)
                        {
                            IsLoading = true;
                        }

                        @Override
                        public void onAnimationEnd(View view)
                        {
                            IsLoading = false;
                        }

                        @Override
                        public void onAnimationCancel(View view)
                        {

                        }
                    }).withLayer().start();
                }
                else if (DY < 0)
                {
                    ViewCompat.animate(CircleImageViewWrite).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setListener(new ViewPropertyAnimatorListener()
                    {
                        @Override
                        public void onAnimationStart(View view)
                        {
                            IsLoading = true;
                        }

                        @Override
                        public void onAnimationEnd(View view)
                        {
                            IsLoading = false;
                        }

                        @Override
                        public void onAnimationCancel(View view)
                        {

                        }
                    }).withLayer().start();
                }
            }
        });

        ViewMain = view;
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolderMain>
    {
        private ArrayList<ContactEntity> ContactList = new ArrayList<>();

        ContactListAdapter()
        {
            ContactList.add(new ContactEntity("1", "http://198.50.232.192:9000/1.jpg", "Ali", "Test Message For Fake List", 0, 1528008709));
            ContactList.add(new ContactEntity("2", "http://198.50.232.192:9000/2.jpg", "Hesam", "Test Message For Fake List", 551, 1529001709));
            ContactList.add(new ContactEntity("3", "http://198.50.232.192:9000/3.jpg", "Ali Ebne Sina", "Test Message For Fake List", 31, 1527008709));
            ContactList.add(new ContactEntity("4", "", "Ali e Karar", "Test Message For Fake List", 22, 1528508709));
            ContactList.add(new ContactEntity("5", "http://198.50.232.192:9000/5.jpg", "Ali e Dodol Tala", "Test Message For Fake List", 1, 1529008709));
            ContactList.add(new ContactEntity("6", "http://198.50.232.192:9000/1.jpg", "Salam Sosis", "Test Message For Fake List", 0, 1429008709));
            ContactList.add(new ContactEntity("7", "http://198.50.232.192:9000/2.jpg", "Edraki", "Test Message For Fake List", 1, 1529008709));
            ContactList.add(new ContactEntity("8", "http://198.50.232.192:9000/3.jpg", "QQ Bang Bang", "Test Message For Fake List", 0, 1529008709));
            ContactList.add(new ContactEntity("9", "http://198.50.232.192:9000/4.jpg", "...", "Test Message For Fake List", 0, 1529008709));
            ContactList.add(new ContactEntity("10", "http://198.50.232.192:9000/5.jpg", "09385454000", "Test Message For Fake List", 552, 1529008709));
            ContactList.add(new ContactEntity("11", "http://198.50.232.192:9000/1.jpg", "UserName", "Test Message For Fake List", 0, 1529008709));
            ContactList.add(new ContactEntity("12", "http://198.50.232.192:9000/2.jpg", "UserName", "Test Message For Fake List", 0, 1529008709));
            ContactList.add(new ContactEntity("13", "http://198.50.232.192:9000/3.jpg", "UserName", "Test Message For Fake List", 0, 1529008709));
            ContactList.add(new ContactEntity("14", "http://198.50.232.192:9000/4.jpg", "سوسیسسوسیسسوسیسسوسیسسوسیسسوسیسسوسیس سلام", "کی بریم بیرونسوسیسسوسیسسوسیسسوسیسسوسیسسوسیس", 1000, 1529008709));
            ContactList.add(new ContactEntity("15", "http://198.50.232.192:9000/5.jpg", "UserName", "Test Message For Fake List", 0, 1529008709));
            ContactList.add(new ContactEntity("16", "http://198.50.232.192:9000/1.jpg", "UserName", "Test Message For Fake List", 0, 1529008709));
        }

        @NonNull
        @Override
        public ViewHolderMain onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

            return new ViewHolderMain(LayoutInflater.from(parent.getContext()).inflate(R.layout.messenger_chat_list_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderMain holder, int position)
        {
            holder.BindView(position);
        }

        @Override
        public int getItemCount()
        {
            return ContactList.size();
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            CircleImageView CircleImageViewProfile;
            TextView TextViewUsername;
            TextView TextViewMessage;
            TextView TextViewTime;
            TextView TextViewCount;
            View ViewLine;

            ViewHolderMain(View view)
            {
                super(view);

                CircleImageViewProfile = view.findViewById(R.id.CircleImageViewProfile);
                TextViewUsername = view.findViewById(R.id.TextViewUsername);
                TextViewMessage = view.findViewById(R.id.TextViewMessage);
                TextViewTime = view.findViewById(R.id.TextViewTime);
                TextViewCount = view.findViewById(R.id.TextViewCount);
                ViewLine = view.findViewById(R.id.ViewLine);

                TextViewMessage.setTypeface(Misc.GetTypeface());
                TextViewCount.setTypeface(Misc.GetTypeface());
                TextViewTime.setTypeface(Misc.GetTypeface());
                TextViewUsername.setTypeface(Misc.GetTypeface());
            }

            void BindView(final int Pos)
            {
                final ContactEntity Chat = ContactList.get(Pos);

                //GlideApp.with(Activity).load(Chat.Profile).placeholder(R.color.Primary).into(CircleImageViewProfile);

                TextViewUsername.setText(Chat.Username.length() > 19 ? (Chat.Username.substring(0, 19) + "...") : Chat.Username);
                TextViewMessage.setText(Chat.Message.length() > 24 ? (Chat.Message.substring(0, 24) + "...") : Chat.Message);
                TextViewTime.setText(Misc.TimeAgo(Chat.Time));
                TextViewCount.setVisibility(Chat.Count == 0 ? View.GONE : View.VISIBLE);
                TextViewCount.setText(Chat.Count > 99 ? "99+" : String.valueOf(Chat.Count));
                ViewLine.setVisibility(ContactList.size() - 1 == Pos ? View.VISIBLE : View.GONE);

                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Activity.GetManager().OpenView(new Chat_UI(Pos % 2), "Chat_UI", true);
                    }
                });
            }
        }
    }

    private class ContactEntity
    {
        String ID;
        String Profile;
        String Username;
        int Time;
        String Message;
        int Count;

        ContactEntity(String id, String profile, String username, String message, int count, int time)
        {
            ID = id;
            Profile = profile;
            Username = username;
            Message = message;
            Count = count;
            Time = time;
        }
    }
}