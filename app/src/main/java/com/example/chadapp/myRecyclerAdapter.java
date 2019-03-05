package com.example.chadapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class myRecyclerAdapter extends RecyclerView.Adapter<myRecyclerAdapter.myViewHolder>{

    List<Messages> chats;
    Context context;
    String User,Reciever;
    ChatActivity activity;
    myRecyclerAdapter(List<Messages> chats, Context context, String User, String Reciever, ChatActivity activity)
    {
        this.context = context;
        this.chats = chats;
        this.User = User;
        this.Reciever = Reciever;
        this.activity = activity;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        if(i == ChatActivity.Right)
            view = LayoutInflater.from(context).inflate(R.layout.message_right,viewGroup,false);
        else if(i == ChatActivity.Left)
            view = LayoutInflater.from(context).inflate(R.layout.message_left,viewGroup,false);

        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {
        if(viewHolder.view == null)
            return;
        final int j = i;
        TextView textView = viewHolder.view.findViewById(R.id.Message);
        textView.setText(chats.get(i).message);
       textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.DeleteMessage(j);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(User.equals(chats.get(position).sender))
            return ChatActivity.Right;
        else //if(chats.get(position).sender == Reciever[1])
            return ChatActivity.Left;

    }

    class myViewHolder extends RecyclerView.ViewHolder{

        View view;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}

