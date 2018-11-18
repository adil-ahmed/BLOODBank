package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bloodbank.eatl.com.bloodbank.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BloodDonarRegistrationActivity extends AppCompatActivity {

    EditText _name, _email, _phone, _area;
    Toolbar mytoolbar;

    String RegistrationURL = "http://eatlapps.com/bloodservice/index.php?r=api/Registrationapps";
    Spinner _bloodGroup;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donar);
        Initialization();
        setToolbar();

    }
    private void setToolbar() {
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ডোনার নিবন্ধন করুন");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    private void Initialization() {
        mytoolbar = (Toolbar) findViewById(R.id.tool_bar);
        _name = (EditText) findViewById(R.id.reg_name);
        _email = (EditText) findViewById(R.id.reg_email);
        _phone = (EditText) findViewById(R.id.reg_phone);
        _area = (EditText) findViewById(R.id.reg_area);
        _bloodGroup = (Spinner) findViewById(R.id.bloodgrpspinner);

    }

    public void RegistrationMethod(View v) {


        if (isConnected()) {

            JSONObject jsonObject;

            if (!_name.getText().toString().equals("")) {
                if (!_email.getText().toString().equals("")) {
                    if (!_phone.getText().toString().equals("")) {
                        if (!_area.getText().toString().equals("")) {

                            try {
                                jsonObject = new JSONObject();
                                jsonObject.put("full_name", _name.getText().toString());
                                jsonObject.put("email", _email.getText().toString());
                                jsonObject.put("phone", _phone.getText().toString());
                                jsonObject.put("area", _area.getText().toString());
                                jsonObject.put("blood", _bloodGroup.getSelectedItem().toString());

                                Log.e("OBJECT", "" + jsonObject);
                                new RegisterWithGivenData().execute("" + jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Provide area !!!", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Provide Phone !!!", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Provide Email !!!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Provide Name !!!", Toast.LENGTH_LONG).show();
            }


        } else {

        }
    }

    public class RegisterWithGivenData extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialogue = new ProgressDialog(BloodDonarRegistrationActivity.this);
        OkHttpClient client = new OkHttpClient();
        String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogue.setMessage("Registering as Blood Donor..");
            pDialogue.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                Log.e("==>URL: ", RegistrationURL);
                Log.e("==>OBJECT TO PASS: ", params[0]);
                jsonData = post(RegistrationURL, "" + params[0]);
                Log.e("==>Respond", jsonData.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                JSONObject respond = new JSONObject(jsonData);
                if (respond.optString("success").equals("true")) {
                    CreateToastDialogue("Success !!!" + "\n" + respond.optString("message"));
                    finish();
                } else {
                    CreateToastDialogue("Failed to Registration !!!");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialogue.cancel();
        }

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    public boolean isConnected() {
        boolean status = false;
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting() || mWifi.isConnected()) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    private void CreateToastDialogue(String s) {
        LayoutInflater myInflator = getLayoutInflater();
        View myLayout = myInflator.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toastlayout));
        TextView myMessage = (TextView) myLayout.findViewById(R.id.txtvdisplay);
        myMessage.setText(s);
        myMessage.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        Toast myToast = new Toast(getApplicationContext());
        myToast.setDuration(Toast.LENGTH_LONG);
        myToast.setView(myLayout);
        myToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        myToast.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}


