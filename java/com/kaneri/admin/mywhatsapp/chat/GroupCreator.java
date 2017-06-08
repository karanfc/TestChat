package com.kaneri.admin.mywhatsapp.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaneri.admin.mywhatsapp.R;
import com.kaneri.admin.mywhatsapp.finalchatapp.UserListingActivity;

import java.util.ArrayList;

import static com.kaneri.admin.mywhatsapp.R.id.groupname;
import static com.kaneri.admin.mywhatsapp.R.id.noUsersText;
import static com.kaneri.admin.mywhatsapp.R.id.usersList;

public class GroupCreator extends AppCompatActivity  {


    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    ArrayList<String> selectedUsers = new ArrayList<>();
    TextInputEditText groupname;
    Button creategroup;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessageDataReference1, mMessageDataReference2, getmMessageDataReference3,mMessageDataReference4;
    FirebaseAuth auth;
    int i;



    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creator);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        groupname = (TextInputEditText)findViewById(R.id.groupname);
        creategroup = (Button)findViewById(R.id.creategroup);

        usersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        usersList.setItemsCanFocus(false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDataReference1 = mFirebaseDatabase.getReference().child("groupnames");
        mMessageDataReference4 = mFirebaseDatabase.getReference().child("registerations");

        auth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        FirebaseDatabase.getInstance().getReference().child("registrations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(!snapshot.child("email").getValue().equals(user.getEmail()))
                            {
                                al.add(snapshot.child("name").getValue().toString());
                                totalUsers++;
                            }
                            else
                            {
                                UserDetails.name = snapshot.child("name").getValue().toString();
                            }
                        }
                        doOnSuccess();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

       usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(!selectedUsers.contains(usersList.getItemAtPosition(position).toString())) {
                   Toast.makeText(getApplicationContext(), usersList.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                   selectedUsers.add(usersList.getItemAtPosition(position).toString());
                   totalUsers++;
               }
               else
               {
                   selectedUsers.remove(usersList.getItemAtPosition(position).toString());
                   totalUsers--;
               }
           }
       });

        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupname.getText().toString();
                if(name.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter a group name!",Toast.LENGTH_SHORT).show();
                }
                GroupData groupData = new GroupData();
                groupData.setGroupname(name);
                mMessageDataReference1.push().setValue(name);

                mMessageDataReference2 = mFirebaseDatabase.getReference().child("groups").child(name);
                getmMessageDataReference3 = mFirebaseDatabase.getReference().child(name);
                for(i=0; i<selectedUsers.size(); i++) {
                    mMessageDataReference2.child("user" + i).setValue(selectedUsers.get(i));
                    getmMessageDataReference3.child("group" + i).setValue(name);
                }

                Intent intent = new Intent(GroupCreator.this, UserListingActivity.class);
                startActivity(intent);

            }
        });

    }

    public void doOnSuccess(){

            usersList.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, al));

        pd.dismiss();
    }
}
