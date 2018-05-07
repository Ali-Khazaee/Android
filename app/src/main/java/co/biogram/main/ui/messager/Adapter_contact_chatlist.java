package co.biogram.main.ui.messager;

import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import co.biogram.main.R;
import co.biogram.main.ui.view.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_contact_chatlist extends RecyclerView.Adapter<Adapter_contact_chatlist.ViewHolder>{

    private ArrayList<Model_contact_chatlist> contactlist;

    public Adapter_contact_chatlist(ArrayList<Model_contact_chatlist> contactlist) {
        this.contactlist = contactlist;
    }

    @NonNull
    @Override
    public Adapter_contact_chatlist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_contactpage_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_contact_chatlist.ViewHolder holder, int position) {
        Model_contact_chatlist model=contactlist.get(position);

        holder.profile.setImageResource(model.getProfile());
        holder.Username.setText(model.Username);
        holder.UsernameId.setText(model.UserID);

      //  holder.select.setVisibility(model.select);
    }

    @Override
    public int getItemCount() {
        return contactlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        android.widget.TextView Username,UsernameId;
        ImageView select;
        public ViewHolder(View itemView) {
            super(itemView);
            profile=(CircleImageView)itemView.findViewById(R.id.modelcontactpage_circleimageview_profile);
            Username=(android.widget.TextView) itemView.findViewById(R.id.modelcontactpage_textview_Username);
            UsernameId=(android.widget.TextView) itemView.findViewById(R.id.modelcontactpage_textview_id);
            select=(ImageView)itemView.findViewById(R.id.modelcontactpage_imageview_select);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (select.getVisibility()==View.VISIBLE)
                    {
                      select.setVisibility(View.INVISIBLE);
                    } else {
                        select.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }
}
