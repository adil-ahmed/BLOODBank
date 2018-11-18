package bloodbank.eatl.com.bloodbank.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import bloodbank.eatl.com.bloodbank.R;

public class ConfirmPage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    String key = null;
    String value = null;
    String p = null;
    String q = null;


    Button yes,no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);
        /** ActionBar This is back button*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Confirm page");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /** ActionBar */


        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        Initialization();

        final String userID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference mroot = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference users = mroot.child("User_Information");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren())
                {
                    Map<String, String> model = (Map<String, String>) child.getValue();


                    String userid = model.get("UserID");

                    if(userid.equals(userID))
                    {
                        key = child.getKey();
                        //value = model.get("Available");
                        break;


                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Firebase m_objFireBaseRef = new Firebase("https://blood-bank-e90a7.firebaseio.com/");
                Firebase objRef = m_objFireBaseRef.child("User_Information");
                if(key == null)
                {
                    Toast.makeText(ConfirmPage.this, "You already said yes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmPage.this,MainActivity.class);
                    startActivity(intent);

                }
                else {
                    Firebase taskRef = objRef.child(key);
                    Firebase statusRef = taskRef.child("Available");
                    final Firebase availableBloodArea = taskRef.child("Available_BloodGroup_Area");
                    Firebase area = taskRef.child("Area");
                    Firebase bloodGroup = taskRef.child("Blood_Group");


                    bloodGroup.addValueEventListener(new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            p = dataSnapshot.getValue().toString();

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    area.addValueEventListener(new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            String k = dataSnapshot.getValue().toString();
                            Log.d("Area : ",k);
                            Log.d("Blood: ", p);
                            availableBloodArea.setValue("Yes_"+p+"_"+k);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    statusRef.setValue("Yes");
                    Toast.makeText(ConfirmPage.this, "Congratulations", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmPage.this,MainActivity.class);
                    startActivity(intent);
                }



            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Firebase m_objFireBaseRef = new Firebase("https://blood-bank-e90a7.firebaseio.com/");
                Firebase objRef = m_objFireBaseRef.child("User_Information");
                if(key == null)
                {
                    Toast.makeText(ConfirmPage.this, "You already said no", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmPage.this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Firebase taskRef = objRef.child(key);
                    Firebase statusRef = taskRef.child("Available");
                    final Firebase availableBloodArea = taskRef.child("Available_BloodGroup_Area");
                    Firebase area = taskRef.child("Area");
                    Firebase bloodGroup = taskRef.child("Blood_Group");


                    bloodGroup.addValueEventListener(new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            q = dataSnapshot.getValue().toString();

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    area.addValueEventListener(new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            String k = dataSnapshot.getValue().toString();
                          //  Log.d("Area : ",k);
                          //  Log.d("Blood: ", q);
                            availableBloodArea.setValue("No_"+q+"_"+k);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    statusRef.setValue("No");
                    Toast.makeText(ConfirmPage.this, "Congratulations", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmPage.this,MainActivity.class);
                    startActivity(intent);
                }




            }
        });

    }





    private void Initialization() {
        yes = (Button)findViewById(R.id.yes);
        no = (Button)findViewById(R.id.no);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            onBackPressed();
            //Toast.makeText(getApplicationContext(),"To sign out tap on action bar!",Toast.LENGTH_LONG).show();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ConfirmPage.this,SignIn.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
