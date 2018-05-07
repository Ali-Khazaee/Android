package co.biogram.main.ui.messager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.biogram.main.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_chat_list extends RecyclerView.Adapter<Adapter_chat_list.ViewHolder> {

    private ArrayList<Model_chat_list> chat_list;

    public Adapter_chat_list(ArrayList<Model_chat_list> chat_list) {
        this.chat_list = chat_list;
    }


    @Override
    public Adapter_chat_list.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_chat_list.ViewHolder holder, int position) {
        Model_chat_list model=chat_list.get(position);
        holder.imageprofile.setImageResource(model.getProfile());
        holder.Uusername.setText(model.getUsername());
        holder.Endmessage.setText(model.Endmessage);
        holder.Etime.setText(model.Etime);
        holder.cunter.setText(model.cunter);

    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageprofile;
        TextView Uusername,Endmessage,Etime,cunter;

        public ViewHolder(View itemView) {
            super(itemView);

            imageprofile=(CircleImageView)itemView.findViewById(R.id.circleImageView_profile);
            Uusername=(TextView)itemView.findViewById(R.id.textView_user);
            Endmessage=(TextView)itemView.findViewById(R.id.textView_message);
            Etime=(TextView)itemView.findViewById(R.id.textView_time);
            cunter=(TextView)itemView.findViewById(R.id.textView_cunter_unread);

        }
    }
}
