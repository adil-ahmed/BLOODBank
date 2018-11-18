package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bloodbank.eatl.com.bloodbank.R;

public class SignIn extends AppCompatActivity {
    EditText email,password;
    Button signin,signup;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null )
                {

                    Intent intent = new Intent(SignIn.this,MainActivity.class);
                    startActivity(intent);

                }

            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setMessage("Sign in is on progress");
                progressDialog.show();

                String emailText = email.getText().toString().trim();//ommit fore space
                String passText = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passText)) {
                    progressDialog.hide();
                    Toast.makeText(SignIn.this, "Give Data", Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(emailText, passText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.hide();
                            if (!task.isComplete()) {
                                Toast.makeText(SignIn.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(SignIn.this, "Wrong email and password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
