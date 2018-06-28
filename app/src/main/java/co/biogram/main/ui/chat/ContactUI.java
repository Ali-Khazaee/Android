package co.biogram.main.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import co.biogram.main.BuildConfig;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.ui.component.CircleImageView;

public class ContactUI extends FragmentView implements View.OnClickListener, TextWatcher
{

    private RecyclerView RecyclerView;
    private ContactAdapter ContactAdapter;
    private ArrayList<ContactEntity> ContactList;

    private EditText EditTextSearch;
    private ImageButton ImageButtonSearch;
    private ImageButton ImageButtonClear;
    private Button ButtonMessage;

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.social_contact, null);

        Activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RecyclerView = view.findViewById(R.id.RecyclerViewContacts);
        EditTextSearch = view.findViewById(R.id.EditTextSearch);
        ImageButtonSearch = view.findViewById(R.id.ImageButtonSearch);
        ImageButtonClear = view.findViewById(R.id.ImageButtonClear);
        ButtonMessage = view.findViewById(R.id.ButtonNewMessage);

        ContactList = new ArrayList<>();
        setData();

        ContactAdapter = new ContactAdapter(ContactList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(ContactAdapter);

        ImageButtonClear.setOnClickListener(this);
        ImageButtonSearch.setOnClickListener(this);
        ButtonMessage.setOnClickListener(this);
        EditTextSearch.addTextChangedListener(this);

        //        EditTextSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(EditTextSearch, InputMethodManager.SHOW_IMPLICIT);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        ViewMain = view;
    }

    private void setData()
    {
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "amir", "@hello"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "ali", "@mor"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "hassan", "@mor"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "farhad", "@far"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "hadi", "@hadi"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));
        ContactList.add(new ContactEntity(R.drawable.z_social_profile_avatar, "Hessam", "@hesam"));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ImageButtonClear:
            {
                EditTextSearch.setText("");
                ContactAdapter.filter("");
                break;
            }
            case R.id.ImageButtonSearch:
            {
                ContactAdapter.filter(EditTextSearch.getText().toString());
                break;
            }

            case R.id.ButtonNewMessage:
            {
                Activity.GetManager().OpenView(new Chat_GroupCreateUI(ContactList), "Chat_Group", false);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        ContactAdapter.filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
    {

        private ArrayList<ContactEntity> Contacts = new ArrayList<>();
        private ArrayList<ContactEntity> DataCopy = new ArrayList<>();

        //TODO Change It When JSON Format Is Available
        private SparseBooleanArray CheckState = new SparseBooleanArray();

        public ContactAdapter(ArrayList<ContactEntity> data)
        {
            this.Contacts.addAll(data);
            this.DataCopy.addAll(data);
        }

        private void reset()
        {
            Contacts.clear();
            Contacts.addAll(DataCopy);
        }

        public void filter(String query)
        {

            if (!query.isEmpty())
            {
                ArrayList<ContactEntity> result = new ArrayList<>();
                for (ContactEntity contact : Contacts)
                {
                    if (contact.getUsername().toLowerCase().contains(query.trim().toLowerCase()) || contact.getUserID().substring(1).toLowerCase().contains(query.toLowerCase()))
                        result.add(contact);
                }
                Contacts.clear();
                Contacts.addAll(result);

            }
            else if (Contacts.size() != DataCopy.size())
            {
                reset();
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_model, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position)
        {
            holder.bind(position);
        }

        @Override
        public int getItemCount()
        {
            return Contacts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            CircleImageView ProfileImage;
            TextView Username;
            TextView UserId;
            ImageView SelectState;

            public ViewHolder(View itemView)
            {
                super(itemView);
                ProfileImage = itemView.findViewById(R.id.ProfileImage);
                Username = itemView.findViewById(R.id.Username);
                UserId = itemView.findViewById(R.id.UserID);
                SelectState = itemView.findViewById(R.id.SelectState);
                itemView.setOnClickListener(this);

            }

            public void bind(int position)
            {
                ContactEntity model = Contacts.get(position);
                ProfileImage.setImageResource(model.getProfile());
                Username.setText(model.Username);
                UserId.setText(model.UserID);

                if (CheckState.size() == 0 || !CheckState.get(position))
                {
                    SelectState.setVisibility(View.INVISIBLE);
                }
                else
                {
                    SelectState.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onClick(View v)
            {
                if (SelectState.getVisibility() == View.VISIBLE)
                {
                    CheckState.put(getAdapterPosition(), false);
                    SelectState.setVisibility(View.INVISIBLE);
                }
                else
                {
                    CheckState.put(getAdapterPosition(), true);
                    SelectState.setVisibility(View.VISIBLE);
                }
                if (BuildConfig.DEBUG)
                    Log.d("BooleanAssertion", "Position:" + getAdapterPosition() + "Value:" + (CheckState.get(getAdapterPosition()) ? CheckState.get(getAdapterPosition()) : "NO VALUE"));
            }

        }
    }

    public class ContactEntity
    {

        int Profile;
        String Username;
        String UserID;

        public ContactEntity(int profile, String username, String userID)
        {
            this.Profile = profile;
            this.Username = username;
            this.UserID = userID;

        }

        public int getProfile()
        {
            return Profile;
        }

        public void setProfile(int profile)
        {
            this.Profile = profile;
        }

        public String getUsername()
        {
            return Username;
        }

        public void setUsername(String username)
        {
            Username = username;
        }

        public String getUserID()
        {
            return UserID;
        }

        public void setUserID(String userID)
        {
            UserID = userID;
        }

    }

}
