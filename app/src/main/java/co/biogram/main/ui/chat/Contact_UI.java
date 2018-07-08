package co.biogram.main.ui.chat;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.CircleImageView;
import co.biogram.main.ui.view.CircleCheckBox;
import co.biogram.main.ui.view.EditTextTag;

import java.util.ArrayList;

/**
 * Created by soh_mil97
 */

public class Contact_UI extends FragmentView
{
    private ImageButton SendButton;
    private EditTextTag EditTextTaqs;

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.chat_contact, null);

        Activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RecyclerView RecyclerView = view.findViewById(R.id.RecyclerViewContacts);

        EditTextTaqs = view.findViewById(R.id.EditTextTaqContacts);
        SendButton = view.findViewById(R.id.ImageButtonCreate);
        EditText editTextSearch = view.findViewById(R.id.EditTextSearch);

        final ContactAdapter ContactAdapter = new ContactAdapter(null);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(ContactAdapter);

        ((TextView) view.findViewById(R.id.TextViewTo)).setTypeface(Misc.GetTypeface());
        editTextSearch.setTypeface(Misc.GetTypeface());
        Misc.SetCursorColor(editTextSearch, R.color.Primary);
        ViewCompat.setBackgroundTintList(editTextSearch, ColorStateList.valueOf(Misc.Color(R.color.Primary)));

        EditTextTaqs.hideEditText();

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
                Activity.GetManager().OpenView(new Chat_GroupCreate_UI(ContactAdapter.getSelectedData()), "Chat_Group", false);
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

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
    {

        private ArrayList<ContactEntity> Contacts = new ArrayList<>();
        private ArrayList<ContactEntity> DataCopy = new ArrayList<>();

        private int count = 0;

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
            CircleCheckBox SelectState;

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
                SelectState.setCheckedCode(model.isSelected);

                SelectState.setOnCheckedChangeListener(new CircleCheckBox.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CircleCheckBox view, boolean isChecked)
                    {
                        ContactEntity contactEntity = Contacts.get(getAdapterPosition());
                        contactEntity.isSelected = isChecked;

                        if (isChecked)
                        {
                            EditTextTaqs.Add(contactEntity.Username);
                            count++;
                        }
                        else
                        {
                            EditTextTaqs.RemoveTaq(contactEntity.Username);
                            count--;
                        }

                        // TODO Add Delete

                        if (count > 0)
                        {
                            EditTextTaqs.setVisibility(View.VISIBLE);
                            SendButton.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            EditTextTaqs.setVisibility(View.GONE);
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
