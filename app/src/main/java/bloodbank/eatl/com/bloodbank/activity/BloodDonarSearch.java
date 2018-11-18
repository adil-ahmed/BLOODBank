package bloodbank.eatl.com.bloodbank.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import bloodbank.eatl.com.bloodbank.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BloodDonarSearch extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView rv;
    String URL_PullAllData = "http://eatlapps.com/bloodservice/index.php?r=api/UserinfoAll";
    ArrayList<bloodModel> all_value = new ArrayList<>();
    private SearchView mSearchView;
    MyRecyclerAdapter adapter;
    ArrayList<String> all_location;
    Toolbar mytoolbar;
    String query = "";
    Spinner location_filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donar_search);
        Initialization();
        setToolbar();
        setupSearchView();
        new ShowAllDonerList().execute("");




        location_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SearchWithQuery(query, location_filter.getSelectedItem().toString(), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void setToolbar() {
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ডোনার লিস্ট ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    public class ShowAllDonerList extends AsyncTask<String, String, String> {
        String response;
        OkHttpClient client = new OkHttpClient();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BloodDonarSearch.this);
            progressDialog.setTitle("ডোনার লোড হচ্ছে !!!");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                response = run(URL_PullAllData);
                Log.e("==>Response", response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("Userinfo");
                all_value.clear();
                all_location.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    all_location.add(jsonArray.getJSONObject(i).optString("area"));
                    bloodModel model = new bloodModel();
                    model.setName(jsonArray.getJSONObject(i).optString("full_name"));
                    model.setEmail(jsonArray.getJSONObject(i).optString("email"));
                    model.setArea(jsonArray.getJSONObject(i).optString("area"));
                    model.setBloodgroup(jsonArray.getJSONObject(i).optString("blood"));
                    model.setPhone(jsonArray.getJSONObject(i).optString("phone"));
                    all_value.add(model);
                }

            } catch (Exception e) {
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("==>POST ", "-->>>" + all_value.size());
            adapter = new MyRecyclerAdapter(all_value, R.layout.search_card, 0);
            rv.setAdapter(adapter);


            Set<String> hs = new HashSet<>();
            hs.addAll(all_location);
            all_location.clear();
            all_location.addAll(hs);


            String[] LocationArray = new String[all_location.size()];
            LocationArray = all_location.toArray(LocationArray);


            ArrayAdapter<String> spinnerArrayAdapter_child_death = new ArrayAdapter<String>(BloodDonarSearch.this, android.R.layout.simple_spinner_item, LocationArray);
            spinnerArrayAdapter_child_death.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            location_filter.setAdapter(spinnerArrayAdapter_child_death);


            progressDialog.cancel();
        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    private void Initialization() {
        mytoolbar = (Toolbar) findViewById(R.id.tool_bar);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(BloodDonarSearch.this));
        mSearchView = (SearchView) findViewById(R.id.search_view);
        location_filter = (Spinner) findViewById(R.id.location_spinner);
        all_location = new ArrayList<>();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        query = newText;
        SearchWithQuery(newText, location_filter.getSelectedItem().toString(), false);
        return true;
    }

    private void SearchWithQuery(String s, String s1, boolean nofilter) {
        Log.e("POSSSS", "METHOD");
        Log.e("POSSSS", "" + nofilter);
        if (nofilter) {
            adapter = new MyRecyclerAdapter(all_value, R.layout.search_card, 0);
            rv.setAdapter(adapter);
        } else {
            s = s.toString().toLowerCase();
            s1 = s1.toString().toLowerCase();
            final ArrayList<bloodModel> filteredList = new ArrayList<>();

            for (int i = 0; i < all_value.size(); i++) {
                final String text = all_value.get(i).getBloodgroup().toLowerCase();
                final String text1 = all_value.get(i).getArea().toLowerCase();
                if (text.contains(s)) {
                    if (text1.equals(s1)) {
                        filteredList.add(all_value.get(i));
                    }


                }
            }
            adapter = new MyRecyclerAdapter(filteredList, R.layout.search_card, 0);
            rv.setAdapter(adapter);

        }

    }

    public void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(BloodDonarSearch.this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Enter Blood Group ... ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
