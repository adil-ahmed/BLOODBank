package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import bloodbank.eatl.com.bloodbank.R;

public class SignUp extends AppCompatActivity {
    EditText email,name,area,mobile,password;

    Button signup,signin;

    Spinner bloodGroup;

    Firebase root;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

   // String emailText,nameText,areaText,mobileText,passwordText,bloodGroupText;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Initialization();

        root = new Firebase("https://blood-bank-e90a7.firebaseio.com/User_Information");
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              //  FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Sign up is on progress");
                progressDialog.show();

                final String emailText = email.getText().toString().trim();
                final String passwordText = password.getText().toString().trim();
                final String areaText = area.getText().toString().trim();
                final String mobileText = mobile.getText().toString().trim();
                final String nameText = name.getText().toString().trim();
                final String bloodGroupText = bloodGroup.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(emailText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (TextUtils.isEmpty(passwordText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(areaText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter area!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(nameText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(mobileText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(bloodGroupText))
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Please select a blood group!", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (passwordText.length() < 6)
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (mobileText.length() < 11)
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Incorrect length of mobile number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if(task.isSuccessful())
                        {
                            String userID = firebaseAuth.getCurrentUser().getUid();
                            Intent intent = new Intent(SignUp.this,MainActivity.class);
                            final Map<String, String> info = new HashMap<String, String>();

                            info.put("Name",nameText); //inserting name
                            info.put("Area",areaText); //inserting area
                            info.put("Mobile",mobileText); // inserting mobile
                            info.put("Password",passwordText); //inserting password
                            info.put("Blood_Group",bloodGroupText); //inserting blood group
                            info.put("Email",emailText); //inserting email
                            info.put("Available","Yes"); //inserting available donor
                            info.put("UserID",userID); //inserting user id
                            info.put("Available_BloodGroup","Yes_"+bloodGroupText);
                            info.put("Available_Area","Yes_"+areaText);
                            info.put("Available_BloodGroup_Area","Yes_"+bloodGroupText+"_"+areaText);


                            root.push().setValue(info);
//                            Intent intent = new Intent(SignUp.this,ConfirmPage.class);
//                            intent.putExtra("name",nameText);
//                            intent.putExtra("email",emailText);
//                            intent.putExtra("password",passwordText);
//                            intent.putExtra("area",areaText);
//                            intent.putExtra("mobile",mobileText);
//                            intent.putExtra("bloodGroup",bloodGroupText);

                            startActivity(intent);
                        }

                        else if(!task.isSuccessful())
                        {
                            Log.d("Error : ", "onComplete: Failed= " + task.getException().getMessage());
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(SignUp.this, "User already exists", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



            }
        });



    }

    private void Initialization() {
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        area = (EditText)findViewById(R.id.area);
        password = (EditText)findViewById(R.id.password);
        mobile = (EditText)findViewById(R.id.mobilephone);

        bloodGroup = (Spinner) findViewById(R.id.bloodgrpspinner);

        signup = (Button) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.signin);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUp.this,SignIn.class));
        finish();
    }
}
