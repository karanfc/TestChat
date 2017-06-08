package com.kaneri.admin.mywhatsapp.chat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.kaneri.admin.mywhatsapp.R;
import com.kaneri.admin.mywhatsapp.finalchatapp.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class GroupChatRoom extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        String s = getIntent().getStringExtra("CURRENT_CHAT");
        setTitle(s);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        },1);

        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Firebase.setAndroidContext(this);
        reference2 = new Firebase("https://my-whatsapp-678e8.firebaseio.com/groupmessages/" + UserDetails.chatWith );

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.name);
                   // reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference2.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.name)){
                    addMessageBox("You\n" + message, 1);
                }
                else{
                    addMessageBox(userName + "\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        } );
    }

    public void addMessageBox(String message, int type){
        LinearLayout layout2 = new LinearLayout(GroupChatRoom.this);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(GroupChatRoom.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);
        View v = new View(GroupChatRoom.this);
        // LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(0,0,1f);
        v.setLayoutParams(new LinearLayout.LayoutParams(0,0,10));


        if(type == 1) {

            textView.setBackgroundResource(R.drawable.bubble2);
            layout2.addView(v);
            layout2.addView(textView);
            layout.addView(layout2);

        }
        else{
            textView.setBackgroundResource(R.drawable.bubble1);
            layout2.addView(textView);
            layout2.addView(v);
            layout.addView(layout2);
        }

    }

}
