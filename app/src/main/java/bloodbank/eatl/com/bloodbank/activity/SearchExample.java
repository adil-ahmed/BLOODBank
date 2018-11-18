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
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import bloodbank.eatl.com.bloodbank.R;

public class SearchExample extends AppCompatActivity {
    Button searchButton,refresh,searchByBlood,searchByArea;
    Spinner blood_group, location;

    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    Query normalPostsQuery,searchQuery,searchByBloodQuery,searchByAreaQuery;
    ArrayList<String> all_location;
    String locationText = null;
    String bloodGroupText = null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_example);
        /** ActionBar This is back button*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Search Donar");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /** ActionBar */

        Initialization();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        normalPostsQuery = databaseReference.child("User_Information").orderByChild("Available").equalTo("Yes");

        firebaseSearch();
        SetLocationSpinner();

        blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroupText = parent.getItemAtPosition(position).toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationText = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Loading");
                progressDialog.show();
                firebaseSearch();
                SetLocationSpinner();
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = databaseReference.child("User_Information").
                        orderByChild("Available_BloodGroup_Area").
                        equalTo("Yes_"+bloodGroupText+"_"+locationText);



                progressDialog.setTitle("Loading");
                progressDialog.show();
                firebaseSearchWithQuery();
                progressDialog.hide();

            }
        });

        searchByBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByBloodQuery = databaseReference.child("User_Information").
                        orderByChild("Available_BloodGroup").
                        equalTo("Yes_"+bloodGroupText);



                progressDialog.setTitle("Loading");
                progressDialog.show();
                firebaseSearchWithBloodQuery();
                progressDialog.hide();

            }
        });

        searchByArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByAreaQuery = databaseReference.child("User_Information").
                        orderByChild("Available_Area").
                        equalTo("Yes_"+locationText);



                progressDialog.setTitle("Loading");
                progressDialog.show();
                firebaseSearchWithAreaQuery();
                progressDialog.hide();
            }
        });



    }

    private void firebaseSearchWithBloodQuery() {
        FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder>(
                        BloodModelSchema.class,
                        R.layout.search_card,
                        UserViewHolder.class,
                        searchByBloodQuery

                ) {
                    @Override
                    protected void populateViewHolder(final UserViewHolder viewHolder, final BloodModelSchema model, int position) {
                        viewHolder.setDetails(model.getBlood_Group(),model.getName(),
                                model.getArea(),model.getMobile(),
                                model.getEmail());
                    }


                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    private void firebaseSearchWithAreaQuery() {
        FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder>(
                        BloodModelSchema.class,
                        R.layout.search_card,
                        UserViewHolder.class,
                        searchByAreaQuery

                ) {
                    @Override
                    protected void populateViewHolder(final UserViewHolder viewHolder, final BloodModelSchema model, int position) {
                        viewHolder.setDetails(model.getBlood_Group(),model.getName(),
                                model.getArea(),model.getMobile(),
                                model.getEmail());
                    }


                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    private void SetLocationSpinner() {
        normalPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                all_location.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren())
                {
                    BloodModelSchema bloodModelSchema = studentSnapshot.getValue(BloodModelSchema.class);


                    all_location.add(bloodModelSchema.getArea()); // from student class taking the list of name

                }

                if (all_location.size() > 0)
                {

                    ArrayAdapter<String> spinnerArrayAdapter_child_death = new ArrayAdapter<String>(SearchExample.this, android.R.layout.simple_spinner_item, all_location);
                    spinnerArrayAdapter_child_death.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    location.setAdapter(spinnerArrayAdapter_child_death);
                    progressDialog.hide();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firebaseSearch() {

        FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder>(
                        BloodModelSchema.class,
                        R.layout.search_card,
                        UserViewHolder.class,
                        normalPostsQuery

                ) {
                    @Override
                    protected void populateViewHolder(final UserViewHolder viewHolder, final BloodModelSchema model, int position) {
                            viewHolder.setDetails(model.getBlood_Group(),model.getName(),
                                    model.getArea(),model.getMobile(),
                                    model.getEmail());
                    }


                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    private void Initialization() {
        searchButton = (Button) findViewById(R.id.searchButton);
        blood_group = (Spinner) findViewById(R.id.bloodGroup);
        location = (Spinner) findViewById(R.id.location);
        refresh = (Button) findViewById(R.id.refresh);
        searchByBlood = (Button) findViewById(R.id.searchByBlood);
        searchByArea = (Button) findViewById(R.id.searchByArea);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        all_location = new ArrayList<>();
        all_location.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("ডোনার লোড হচ্ছে !!!");
        progressDialog.show();

    }

    private void firebaseSearchWithQuery() {

        FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BloodModelSchema, UserViewHolder>(
                        BloodModelSchema.class,
                        R.layout.search_card,
                        UserViewHolder.class,
                        searchQuery

                ) {
                    @Override
                    protected void populateViewHolder(final UserViewHolder viewHolder, final BloodModelSchema model, int position) {
                        viewHolder.setDetails(model.getBlood_Group(),model.getName(),
                                model.getArea(),model.getMobile(),
                                model.getEmail());
                    }


                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        progressDialog.hide();

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String userbloodGroup,String userName, String userArea,
                               String userPhone,String userEmail)
        {
            TextView bloodGroup = (TextView) mView.findViewById(R.id.card_bloodgroup);
            TextView name = (TextView) mView.findViewById(R.id.card_name);
            TextView area = (TextView) mView.findViewById(R.id.card_address);
            TextView phone = (TextView) mView.findViewById(R.id.card_phone);
            TextView email = (TextView) mView.findViewById(R.id.card_email);

            bloodGroup.setText("Blood Group : "+userbloodGroup);
            name.setText("Name : "+userName);
            area.setText("Area : "+userArea);
            phone.setText("Phone number : "+userPhone);
            email.setText("Email : "+userEmail);
        }

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
            Intent intent = new Intent(SearchExample.this,SignIn.class);
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
