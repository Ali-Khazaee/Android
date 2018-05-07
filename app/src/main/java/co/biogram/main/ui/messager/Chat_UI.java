package co.biogram.main.ui.messager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;

public class Chat_UI extends AppCompatActivity {

    private ImageView search;
    private FloatingActionButton plus;

    private RecyclerView recyclerView;
    private Adapter_chat_list adapter_chat_list;
    private ArrayList<Model_chat_list> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_chatlist);
        lists=new ArrayList<>();
        setlist();

        adapter_chat_list=new Adapter_chat_list(lists);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter_chat_list);


        search=(ImageView)findViewById(R.id.chat_Imageview_search);
        plus=(FloatingActionButton) findViewById(R.id.chat_floatingactionbuttom_add);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Chat_UI.this,Contact_Massage_Page.class);
                startActivity(intent);

            }
        });




    }

    public void setlist(){

        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","10","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","100","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam khobi che khabar kojaee che mikoni dastan chie dg che khabar khat por shodesh","1000","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));
        lists.add(new Model_chat_list(R.drawable.imageone,"Hessam","Sallam","1","1 MIN AGO"));

    }


    @Override
    public void onBackPressed() {
        Intent I=new Intent(Chat_UI.this, SocialActivity.class);
        startActivity(I);
        super.onBackPressed();
    }
}
