package bloodbank.eatl.com.bloodbank.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import bloodbank.eatl.com.bloodbank.R;

public class MainActivity extends AppCompatActivity {
    Toolbar mytoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mytoolbar = (Toolbar) findViewById(R.id.tool_bar);

        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ড্যাশবোর্ড");
        mytoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    public void RegistrationDonor(View v) {

        Intent gotoRegisterDonor = new Intent(this, BloodDonarRegistrationActivity.class);
        startActivity(gotoRegisterDonor);
    }
    public void Confirm(View v) {

        Intent confirm = new Intent(this, ConfirmPage.class);
        startActivity(confirm);
    }

    public void DonorList(View v) {
        //Intent gotoDonorList = new Intent(this, BloodDonarSearch.class);
        Intent gotoDonorList = new Intent(this, SearchExample.class);
        startActivity(gotoDonorList);
    }

    public void RequestList(View v) {


       // Intent gotoRequestList = new Intent(this, RequestBloodList.class);
        Intent gotoRequestList = new Intent(this, EmergencyBloodRequestList.class);
        startActivity(gotoRequestList);


    }

    public void BloodBank(View v) {
        Intent goto_bank = new Intent(this, BloodBankActivity.class);
        startActivity(goto_bank);

    }

    public void RequestBlood(View v) {
        //Intent gotoRequestBlood = new Intent(this, RequestBlood.class);
        Intent gotoRequestBlood = new Intent(this, EmergencyBloodRequest.class);
        startActivity(gotoRequestBlood);

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
            Intent intent = new Intent(MainActivity.this,SignIn.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
