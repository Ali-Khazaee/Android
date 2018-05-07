package co.biogram.main.ui.messager;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;


public  class Contact_Massage_Page extends AppCompatActivity {

    RecyclerView recyclerViewcontect;
    Adapter_contact_chatlist adaptercontact;
    ArrayList<Model_contact_chatlist> contactlist;

    EditText searchcontact;
    ImageView search,clear;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_massage_page);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recyclerViewcontect=(RecyclerView)findViewById(R.id.contactpage_recyclerview);
        searchcontact =(EditText)findViewById(R.id.contactpage_edittext_search) ;
        search=(ImageView) findViewById(R.id.contactpage_imageview_search) ;
        clear=(ImageView) findViewById(R.id.contactpage_imageview_remove);

        Searchbar();



        contactlist=new ArrayList<>();
        setcontactlist();
        adaptercontact=new Adapter_contact_chatlist(contactlist);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerViewcontect.setLayoutManager(layoutManager);
        recyclerViewcontect.setAdapter(adaptercontact);



    }

    private void Searchbar(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchcontact.setText("");
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Contact_Massage_Page.this, "search", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setcontactlist() {
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
        contactlist.add(new Model_contact_chatlist(R.drawable.imageone,"Hessam","@hesam",R.drawable.ic_follow_block));
    }

}
