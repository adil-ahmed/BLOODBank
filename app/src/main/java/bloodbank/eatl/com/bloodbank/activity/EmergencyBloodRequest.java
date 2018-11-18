package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bloodbank.eatl.com.bloodbank.R;

public class EmergencyBloodRequest extends AppCompatActivity {
    EditText emergencyName,emergencyPhone,emergencyLocation;
    Spinner emergencyBloodGroup;
    Button emergencyButton;

    Firebase root;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;




    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_blood_request);
        Initialization();
        SetActionBar();
        setFirebaseTable();
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(EmergencyBloodRequest.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();

                Calendar currentTime = Calendar.getInstance();
                long convertIntoMili = currentTime.getTimeInMillis();

                final String emergencyNameText = emergencyName.getText().toString().trim();
                final String emergencyPhoneText = emergencyPhone.getText().toString().trim();
                final String emergencyLocationText = emergencyLocation.getText().toString().trim();
                final String emergencyBloodGroupText = emergencyBloodGroup.getSelectedItem().toString().trim();
                final String emergencyBloodRequestTime = String.valueOf(convertIntoMili);

                if (TextUtils.isEmpty(emergencyNameText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter name please!", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (TextUtils.isEmpty(emergencyPhoneText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter phone number please!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(emergencyLocationText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter location please!", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (TextUtils.isEmpty(emergencyBloodGroupText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Please select a blood group!", Toast.LENGTH_SHORT).show();
                    return;
                }


                else if (emergencyPhoneText.length() < 11)
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Incorrect length of mobile number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(EmergencyBloodRequest.this,MainActivity.class);
                final Map<String, String> info = new HashMap<String, String>();

                info.put("Name",emergencyNameText);
                info.put("Location",emergencyLocationText);
                info.put("Phone",emergencyPhoneText);
                info.put("Emergency_BloodGroup",emergencyBloodGroupText);
                info.put("Emergency_Blood_Request_Time",emergencyBloodRequestTime);


                root.push().setValue(info);
                startActivity(intent);
            }
        });
    }

    private void setFirebaseTable() {
        root = new Firebase("https://blood-bank-e90a7.firebaseio.com/Emergency_Blood_Request");
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
    }

    private void SetActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Emergency Blood Request");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void Initialization() {
        emergencyName = (EditText) findViewById(R.id.emergencyname);
        emergencyPhone = (EditText) findViewById(R.id.emergencyphone);
        emergencyLocation = (EditText) findViewById(R.id.emergencylocation);

        emergencyBloodGroup = (Spinner) findViewById(R.id.emergencybloodgroup);

        emergencyButton = (Button) findViewById(R.id.emergencybutton);
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
            Intent intent = new Intent(EmergencyBloodRequest.this,SignIn.class);
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
