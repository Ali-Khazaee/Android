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
import android.widget.ImageView;
import android.widget.TextView;
import co.biogram.main.BuildConfig;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.component.CircleImageView;

import java.util.ArrayList;

/**
 * Created by soh_mil97
 */

public class Contact_UI extends FragmentView
{

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.chat_contact, null);

        Activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RecyclerView RecyclerView = view.findViewById(R.id.RecyclerViewContacts);

        final EditText EditTextSearch = view.findViewById(R.id.EditTextSearch);
        Button SendButton = view.findViewById(R.id.ButtonNewMessage);

        final ContactAdapter ContactAdapter = new ContactAdapter(null);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(ContactAdapter);

        SendButton.setTypeface(Misc.GetTypeface());
        EditTextSearch.setTypeface(Misc.GetTypeface());

        view.findViewById(R.id.ImageButtonClear).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditTextSearch.setText("");
                ContactAdapter.filter("");
            }
        });

        view.findViewById(R.id.ImageButtonSearch).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ContactAdapter.filter(EditTextSearch.getText().toString());
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

        EditTextSearch.addTextChangedListener(new TextWatcher()
        {

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

        });

        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(EditTextSearch, InputMethodManager.SHOW_IMPLICIT);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        ViewMain = view;
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
    {

        private ArrayList<ContactEntity> Contacts = new ArrayList<>();
        private ArrayList<ContactEntity> DataCopy = new ArrayList<>();

        //TODO Change It When JSON Format Is Available
        private SparseBooleanArray CheckState = new SparseBooleanArray();

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
                if (CheckState.get(i))
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
            ImageView SelectState;

            public ViewHolder(View itemView)
            {
                super(itemView);
                ProfileImage = itemView.findViewById(R.id.CircleImageViewProfile);
                Username = itemView.findViewById(R.id.TextViewUsername);
                UserId = itemView.findViewById(R.id.TextViewUserID);
                SelectState = itemView.findViewById(R.id.ImageViewSelectState);

                Username.setTypeface(Misc.GetTypeface());
                UserId.setTypeface(Misc.GetTypeface());

                itemView.setOnClickListener(new View.OnClickListener()
                {
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
                });

            }

            public void bind(int position)
            {
                ContactEntity model = Contacts.get(position);

                GlideApp.with(Activity).load(model.Profile).placeholder(R.color.Primary).into(ProfileImage);
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

        }
    }

    public class ContactEntity
    {

        String Profile;
        String Username;
        String UserID;

        public ContactEntity(String profile, String username, String userID)
        {
            this.Profile = profile;
            this.Username = username;
            this.UserID = userID;

        }

    }

}
