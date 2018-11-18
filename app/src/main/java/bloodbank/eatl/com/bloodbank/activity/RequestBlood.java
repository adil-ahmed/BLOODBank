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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestBlood extends AppCompatActivity {

    EditText _name, _phone, _address;
    Spinner bloodGrp;
    JSONObject jsonObject;

    Toolbar mytoolbar;

    public String URL = "http://eatlapps.com/bloodservice/index.php?r=api/Bloodrequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        Initialization();
        setToolbar();
    }

    private void Initialization() {
        _name = (EditText) findViewById(R.id.req_name);
        _phone = (EditText) findViewById(R.id.req_phone);
        _address = (EditText) findViewById(R.id.req_address);
        bloodGrp=(Spinner)findViewById(R.id.bloodreq_spinner);
        mytoolbar=(Toolbar)findViewById(R.id.tool_bar);
    }

    private void setToolbar() {

        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ইমারজেন্সি ব্লাড রিকোয়েস্ট করুন");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    public void RequestforBlood(View v) {

        if (isConnected()) {

            if (!_name.getText().toString().equals("")) {
                if (!_phone.getText().toString().equals("")) {
                    if (!_address.getText().toString().equals("")) {

                        try {
                            jsonObject = new JSONObject();
                            jsonObject.put("full_name", _name.getText().toString());
                            jsonObject.put("email", "");
                            jsonObject.put("phone", _phone.getText().toString());
                            jsonObject.put("area", _address.getText().toString());
                            jsonObject.put("blood", bloodGrp.getSelectedItem().toString());
                            Log.e("OBJECT", "" + jsonObject);
                            new BloodRequestAsynTask().execute("" + jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Provide area !!!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Provide phone !!!", Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(getApplicationContext(), "Provide Name !!!", Toast.LENGTH_LONG).show();
            }


        }
    }


    public class BloodRequestAsynTask extends AsyncTask<String, Void, Void> {
        OkHttpClient client = new OkHttpClient();
        String JsonString;
        ProgressDialog progressDialog=new ProgressDialog(RequestBlood.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Requesting  for Blood ..");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                JSONObject respond = new JSONObject(JsonString);

                Log.e("Respond",respond.toString());
                if (respond.optString("success").equals("true")) {
                    CreateToastDialogue("Success !!!" + "\n" + respond.optString("message"));
                    finish();
                } else {
                    CreateToastDialogue("Failed to Request !!!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            progressDialog.cancel();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                JsonString = post(URL, params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(BloodDonarRegistrationActivity.JSON, json);
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
