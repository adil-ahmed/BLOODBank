package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import bloodbank.eatl.com.bloodbank.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BloodBankActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Firebase firebase;
    private ArrayList<String> latList = new ArrayList<String>();
    private ArrayList<String> longList = new ArrayList<String>();
    RecyclerView _bloodbankList;
    SearchView mSearchView;
    ArrayList<bloodbankmodel> alldata = new ArrayList<>();
    public String Url = "http://www.eatlapps.com/bloodservice/index.php?r=api/BloodbankAll";
    Toolbar mytoolbar;
    MyRecyclerAdapterBloodBank adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        Initialization();
        setToolbar();

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://blood-bank-e90a7.firebaseio.com/BloodBank");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){

                    LatLong latLong = childSnapshot.getValue(LatLong.class);

                    Log.e("lat", ""+latLong.getLatitude());
                    Log.e("long", ""+latLong.getLongitude());

                    latList.add(""+latLong.getLatitude());
                    longList.add(""+latLong.getLongitude());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (isConnected()) {
            new ParseBloodBankListJson().execute("");
        } else {


        }
        SetupSearchView();
    }

    private void SetupSearchView() {


        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(BloodBankActivity.this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Enter Area  ");

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        SearchTheList(newText);
        return false;
    }

    private void SearchTheList(String s) {

        s = s.toString().toLowerCase();

        final ArrayList<bloodbankmodel> tempDataList = new ArrayList<>();
        final ArrayList<String> tempLatList = new ArrayList<>();
        final ArrayList<String> tempLongList = new ArrayList<>();
        for (int i = 0; i < alldata.size(); i++) {

            String value = alldata.get(i).getAddress().toString().toLowerCase();

            if (value.contains(s)) {
                try{
                    tempDataList.add(alldata.get(i));
                    tempLatList.add(tempLatList.get(i));
                    tempLongList.add(tempLongList.get(i));
                }catch (Exception e)
                {

                }
//                tempDataList.add(alldata.get(i));
//                tempLatList.add(tempLatList.get(i));
//                tempLongList.add(tempLongList.get(i));
            }
            adapter = new MyRecyclerAdapterBloodBank(BloodBankActivity.this, tempDataList, tempLatList, tempLongList, R.layout.bloodbank_card, 0);
            _bloodbankList.setAdapter(adapter);

        }

    }

    public class ParseBloodBankListJson extends AsyncTask<String, String, String> {
        OkHttpClient client = new OkHttpClient();
        ProgressDialog pDialogue;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogue = new ProgressDialog(BloodBankActivity.this);
            pDialogue.setTitle("ব্লাড ব্যাংক লিস্ট লোডিং ... ");
            pDialogue.show();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                response = run(Url);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("BloodbankList");
                alldata.clear();

                for (int i = 0; i < jsonArray.length(); i++) {

                    bloodbankmodel model = new bloodbankmodel();

                    model.setName(jsonArray.getJSONObject(i).optString("name"));
                    model.setAddress(jsonArray.getJSONObject(i).optString("address"));
                    model.setEmail(jsonArray.getJSONObject(i).optString("email"));
                    model.setPhoneG(jsonArray.getJSONObject(i).optString("phoneG"));
                    model.setPhoneP(jsonArray.getJSONObject(i).optString("phoneP"));
                    model.setWebsite(jsonArray.getJSONObject(i).optString("website"));
                    model.setFacebook(jsonArray.getJSONObject(i).optString("facebook"));
                    alldata.add(model);

                }

                Log.e("BANK", "" + alldata.size());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new MyRecyclerAdapterBloodBank(BloodBankActivity.this, alldata, latList, longList, R.layout.bloodbank_card, 0);
            _bloodbankList.setAdapter(adapter);
            pDialogue.cancel();
        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    private void setToolbar() {
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ব্লাড ব্যাংক");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    private void Initialization() {
        _bloodbankList = (RecyclerView) findViewById(R.id.bloodbak);
        _bloodbankList.setLayoutManager(new LinearLayoutManager(BloodBankActivity.this));
        mSearchView = (SearchView) findViewById(R.id.searchViewbloodbank);
        mytoolbar = (Toolbar) findViewById(R.id.tool_bar);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
