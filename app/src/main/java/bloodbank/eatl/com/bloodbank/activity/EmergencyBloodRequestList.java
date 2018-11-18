package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import bloodbank.eatl.com.bloodbank.R;

public class EmergencyBloodRequestList extends AppCompatActivity {
    ImageButton emergencySearch,emergencyRefresh;
    Spinner emergencyBloodGroupSpinner;

    RecyclerView recyclerView;

    DatabaseReference databaseReference,test;
    Query normalPostsQuery,searchQuery;
    String bloodGroupText = null;

    private ArrayList<String> userInterests = new ArrayList<String>();


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_blood_request_list);
        Initialization();
        SetActionBar();
        SetRecyclerView();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        test = databaseReference.child("Emergency_Blood_Request");
        normalPostsQuery = databaseReference.child("Emergency_Blood_Request");
        firebaseSearch();
        emergencyRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Loading");
                progressDialog.show();
                firebaseSearch();
                Calendar c = Calendar.getInstance();
                final long currentTime = c.getTimeInMillis();


                test.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot value = postSnapshot.child("Emergency_Blood_Request_Time");
                            String v = (String) value.getValue();

                            long estimatedTime = 30000;//30seconds
                            long eRequestTime = Long.valueOf(v);
                            long difference = currentTime - eRequestTime;


                            if(difference>0 && estimatedTime<difference)
                            {


                                String val = postSnapshot.getKey();
                                    test.child(val).removeValue();


                            }





                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                progressDialog.dismiss();






            }

        });
        emergencyBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroupText = parent.getItemAtPosition(position).toString().trim();
             //   Toast.makeText(EmergencyBloodRequestList.this, bloodGroupText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        emergencySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(EmergencyBloodRequestList.this, "Emergency", Toast.LENGTH_SHORT).show();
                searchQuery = databaseReference.child("Emergency_Blood_Request").
                        orderByChild("Emergency_BloodGroup").
                        equalTo(bloodGroupText);
                progressDialog.setTitle("Loading");
                progressDialog.show();
                firebaseSearchWithBloodGroup();
            }
        });


    }

    private void firebaseSearchWithBloodGroup() {

        FirebaseRecyclerAdapter<EmergencyBloodModelSchema, UserViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EmergencyBloodModelSchema, UserViewHolder>(
                EmergencyBloodModelSchema.class,
                R.layout.request_search_card,
                UserViewHolder.class,
                searchQuery
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, EmergencyBloodModelSchema model, int position) {
                viewHolder.setDetails(model.getEmergency_BloodGroup(),model.getName(),
                        model.getPhone(),model.getLocation());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        progressDialog.hide();
    }

    private void firebaseSearch() {



        FirebaseRecyclerAdapter<EmergencyBloodModelSchema, UserViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EmergencyBloodModelSchema, UserViewHolder>(
                EmergencyBloodModelSchema.class,
                R.layout.request_search_card,
                UserViewHolder.class,
                normalPostsQuery
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, EmergencyBloodModelSchema model, int position) {
                viewHolder.setDetails(model.getEmergency_BloodGroup(),model.getName(),
                        model.getLocation(),
                        model.getPhone());
                progressDialog.dismiss();
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        progressDialog.dismiss();


    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String emergencyBloodGroup,String emergencyName,String emergencyLocation,
                               String emergencyPhone)
        {
            TextView bloodGroup = (TextView) mView.findViewById(R.id.e_card_bloodgroup);
            TextView name = (TextView) mView.findViewById(R.id.e_card_name);
            TextView location = (TextView) mView.findViewById(R.id.e_card_location);
            TextView phone = (TextView) mView.findViewById(R.id.e_card_phone);


            bloodGroup.setText("Blood Group : "+emergencyBloodGroup);
            name.setText("Name : "+emergencyName);
            location.setText("Area : "+emergencyLocation);
            phone.setText("Phone number : "+emergencyPhone);

        }

    }

    private void SetRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void SetActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("List of blood request");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void Initialization() {
        emergencyRefresh = (ImageButton) findViewById(R.id.emergencyrefresh);
        emergencySearch = (ImageButton) findViewById(R.id.emergencysearchButton);
        emergencyBloodGroupSpinner = (Spinner) findViewById(R.id.emergencybloodgroupspinner);
        recyclerView = (RecyclerView) findViewById(R.id.emergencyrecyclerview);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("ডোনার লোড হচ্ছে !!!");
        progressDialog.show();
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
            Intent intent = new Intent(EmergencyBloodRequestList.this,SignIn.class);
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
