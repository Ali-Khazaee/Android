package co.biogram.main.ui.chat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.CircleImageView;
import co.biogram.main.ui.view.AnimatedCheckBox;

/**
 * Created by soh_mil97
 */

public class Contact_UI extends FragmentView
{
    private ImageButton SendButton;
    private SelectedContactAdapter mSelectedContactAdapter;
    private RecyclerView mRecyclerViewTaqContacts;

    @Override
    public void OnResume()
    {
        super.OnResume();
        Activity.findViewById(R.id.LinearLayoutMenu).setVisibility(View.INVISIBLE);
        Activity.findViewById(R.id.ViewLine).setVisibility(View.INVISIBLE);
    }

    @Override
    public void OnCreate()
    {
        final View view = View.inflate(Activity, R.layout.chat_contact, null);

        Activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RecyclerView RecyclerView = view.findViewById(R.id.RecyclerViewContacts);

        mSelectedContactAdapter = new SelectedContactAdapter();
        SendButton = view.findViewById(R.id.ImageButtonCreate);
        EditText editTextSearch = view.findViewById(R.id.EditTextSearch);

        mRecyclerViewTaqContacts = view.findViewById(R.id.RecyclerViewTaqContacts);
        mRecyclerViewTaqContacts.setLayoutManager(new LinearLayoutManager(Activity, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTaqContacts.setAdapter(mSelectedContactAdapter);

        final ContactAdapter ContactAdapter = new ContactAdapter(null);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(ContactAdapter);

        ((TextView) view.findViewById(R.id.TextViewTo)).setTypeface(Misc.GetTypeface());
        editTextSearch.setTypeface(Misc.GetTypeface());
        Misc.SetCursorColor(editTextSearch, R.color.Primary);
        ViewCompat.setBackgroundTintList(editTextSearch, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

        ((TextView) view.findViewById(R.id.TextViewName)).setTypeface(Misc.GetTypeface());

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        SendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Activity.findViewById(android.R.id.content).getWindowToken(), 0);
                Activity.GetManager().OpenView(new Chat_GroupCreate_UI(ContactAdapter.getSelectedData()), "Chat_Group", true);
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ContactAdapter.filter(String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        ViewMain = view;
    }

    private class SelectedContactAdapter extends RecyclerView.Adapter<SelectedContactAdapter.ViewHolder>
    {

        private ArrayList<String> SelectedContacts = new ArrayList<>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new SelectedContactAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_selected_contact_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            holder.bind(position);
        }

        @Override
        public int getItemCount()
        {
            return SelectedContacts.size();
        }

        public void addContact(String contactEntity)
        {
            SelectedContacts.add(contactEntity);
            notifyDataSetChanged();
        }

        public void removeContact(String contactEntity)
        {
            SelectedContacts.remove(contactEntity);
            notifyDataSetChanged();
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
                Username.setText(SelectedContacts.get(position));
            }

        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
    {

        private ArrayList<ContactEntity> Contacts = new ArrayList<>();
        private ArrayList<ContactEntity> DataCopy = new ArrayList<>();

        public ContactAdapter(ArrayList<ContactEntity> data)
        {
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "amir", "@hello"));
            Contacts.add(new ContactEntity("hes", "ali", "@mor"));
            Contacts.add(new ContactEntity("hes", "hassan", "@mor"));
            Contacts.add(new ContactEntity("hes", "farhad", "@far"));
            Contacts.add(new ContactEntity("hes", "hadi", "@hadi"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            Contacts.add(new ContactEntity("hes", "Hessam", "@hesam"));
            DataCopy.addAll(Contacts);

        }

        private void reset()
        {
            Contacts.clear();
            Contacts.addAll(DataCopy);
        }

        public ArrayList<ContactEntity> getSelectedData()
        {
            ArrayList<ContactEntity> result = new ArrayList<>();
            for (int i = 0; i < Contacts.size(); i++)
                if (Contacts.get(i).isSelected)
                {
                    result.add(Contacts.get(i));
                }
            return result;
        }

        public void filter(String query)
        {

            if (!query.isEmpty())
            {
                Contacts.clear();
                Contacts.addAll(DataCopy);
                ArrayList<ContactEntity> result = new ArrayList<>();
                for (ContactEntity contact : Contacts)
                {
                    if (contact.Username.toLowerCase().contains(query.trim().toLowerCase()) || contact.UserID.substring(1).toLowerCase().contains(query.toLowerCase()))
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

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            CircleImageView ProfileImage;
            TextView Username;
            TextView UserId;
            AnimatedCheckBox SelectState;

            public ViewHolder(View itemView)
            {
                super(itemView);
                ProfileImage = itemView.findViewById(R.id.CircleImageViewProfile);
                Username = itemView.findViewById(R.id.TextViewUsername);
                UserId = itemView.findViewById(R.id.TextViewUserID);
                SelectState = itemView.findViewById(R.id.CheckBoxSelectState);

                Username.setTypeface(Misc.GetTypeface());
                UserId.setTypeface(Misc.GetTypeface());

            }

            public void bind(final int position)
            {
                ContactEntity model = Contacts.get(position);

                GlideApp.with(Activity).load(model.Profile).placeholder(R.color.Primary).into(ProfileImage);
                Username.setText(model.Username);
                UserId.setText(model.UserID);
                SelectState.setChecked(model.isSelected);

                SelectState.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked)
                    {
                        ContactEntity contactEntity = Contacts.get(getAdapterPosition());
                        if (contactEntity.isSelected != isChecked)
                        {
                            contactEntity.isSelected = isChecked;

                            if (isChecked)
                            {
                                mSelectedContactAdapter.addContact(contactEntity.Username);
                            }
                            else
                            {
                                mSelectedContactAdapter.removeContact(contactEntity.Username);
                            }
                        }

                        // TODO Add Delete
                        if (mSelectedContactAdapter.getItemCount() > 0)
                        {
                            mRecyclerViewTaqContacts.setVisibility(View.VISIBLE);
                            SendButton.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            mRecyclerViewTaqContacts.setVisibility(View.GONE);
                            SendButton.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }

        }
    }

    public class ContactEntity
    {

        String Profile;
        String Username;
        String UserID;
        boolean isSelected;

        public ContactEntity(String profile, String username, String userID)
        {
            this.Profile = profile;
            this.Username = username;
            this.UserID = userID;

        }

    }

}
