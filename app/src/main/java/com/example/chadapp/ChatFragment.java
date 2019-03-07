package com.example.chadapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //Account
    static String NameUser,DobUser;
    static Bitmap bitmap;
  //  static int Method_Used = 1;

    static ProgressDialog dialog;

    public ChatFragment() {
        // Required empty public constructor
    }

    View view;
   //List
    ListView chatlist;
    ChatAdapter adapter1;

    String User;
    DatabaseReference reference;

    static ArrayList<String[]> list = new ArrayList<>();

    static int Opened=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chat, container, false);
        InitializeFields();


        //ListVIew Setup
        adapter1 = new ChatAdapter();
        chatlist.setAdapter(adapter1);


        //User is not null confirmation
        User = "There Goes my Baby";
            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                User = FirebaseAuth.getInstance().getCurrentUser().getUid();

          //Retrieving data from database
            reference = FirebaseDatabase.getInstance().getReference().child("Users");

                AtStartUp();
               UpdateAccount();
             //   UpdateContacts();


        return view;
    }

    void AtStartUp()
    {
        dialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                //Deleting Previous Data on List
                list.clear();
                //Retrieving data
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getKey().equals(User)) {
                        continue;
                    }
                    //Adding names of friends to list
                    try {
                        String name;
                        String  uid;
                        String url =null;
                        name= data.child("name").getValue(String.class);
                        uid= data.getKey();

                        if(data.child("image").exists()) {
                            url = data.child("image").getValue().toString();
                            while(url==null);
                        }
                        while(name==null || uid==null );
                        list.add(new String[]{name, uid, url});
                    }
                    catch (Exception e){
                        Log.d("ChatFragmentGet",e.getMessage());
                    }
                    }
                //updating listview
                adapter1.notifyDataSetChanged();
               if(i==0)
                  dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    void UpdateAccount()
    {

        dialog.show();
        reference.child(User).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(Account2Activity.imageUri== null && dataSnapshot.child("image").exists()) {
                    dialog.show();

                    String Url1 = dataSnapshot.child("image").getValue(String.class);
                    while(TextUtils.isEmpty(Url1));

                    new GetImageFromURL(Url1,MainActivity.UserImg).execute();
                }
                //If image is recently uploaded
                else if(Account2Activity.imageUri!= null)
                {
                    MainActivity.SetImage(1);

                }
                NameUser = dataSnapshot.child("name").getValue().toString();
                DobUser = dataSnapshot.child("dob").getValue().toString();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getContext(), "I was here", Toast.LENGTH_SHORT).show();
        adapter1.notifyDataSetChanged();
    }


    private void InitializeFields() {
        chatlist = view.findViewById(R.id.Chat_List);
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Please Wait");
        dialog.setCanceledOnTouchOutside(false);

    }

    class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            //Setting up view on the list
            Context context = getContext();
            convertView = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.chatdesign,parent,false);
            TextView text = convertView.findViewById(R.id.Design_text);
            text.setText(list.get(position)[0]);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ChatFragment.Opened != 0)
                        return;
                    ChatFragment.Opened = 1;
                    Intent intent = new Intent(MainActivity.context,ChatActivity.class);
                    intent.putExtra("User",User);
                    intent.putExtra("Reciever",list.get(position));
                    intent.putExtra("Number",position);
                    startActivity(intent);
                }
            });

            ImageView imageView = convertView.findViewById(R.id.Design_image);
            new GetImageFromURL1(list.get(position)[2],imageView).execute();
            return convertView;
        }

    }

    //Downloading image
    class GetImageFromURL extends AsyncTask<String,Void, Bitmap>{


        ImageView imv;
        String Url;

        GetImageFromURL(String Url,ImageView imv)
        {
            this.imv = imv;
            this.Url = Url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            ChatFragment.bitmap = null;
            try{
             //   Toast.makeText(getContext(), "Downloading", Toast.LENGTH_SHORT).show();
                InputStream inputStream = new java.net.URL(Url).openStream();
                ChatFragment.bitmap = BitmapFactory.decodeStream(inputStream);
               // Toast.makeText(getContext(), "Image available", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("DownloadingImage",e.getMessage());
            }
            return ChatFragment.bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(ChatFragment.bitmap);
           //Toast.makeText(getContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();
           imv.setImageBitmap(ChatFragment.bitmap);
           ChatFragment.dialog.dismiss();
        }
    }

}



