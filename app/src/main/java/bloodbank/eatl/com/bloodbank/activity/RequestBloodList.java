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

public class RequestBloodList extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView rv;
    String URL = "http://www.eatlapps.com/bloodservice/index.php?r=api/PosAll";
    private SearchView mSearchView;
    ArrayList<bloodModel> all_value = new ArrayList<>();
    MyRecyclerAdapter adapter;
    Toolbar mytoolbar;
    String query = "";
    Spinner location_filter;
    ArrayList<String> all_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_list);
        Initialization();
        setToolbar();
        setupSearchView();
        new PopulateRequestList().execute("");
        
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

    private void setupSearchView() {

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(RequestBloodList.this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Enter Blood Group ... ");
    }

    private void Initialization() {
        mytoolbar=(Toolbar)findViewById(R.id.tool_bar);
        rv = (RecyclerView) findViewById(R.id.rv1);
        rv.setLayoutManager(new LinearLayoutManager(RequestBloodList.this));
        mSearchView = (SearchView) findViewById(R.id.search_view);
        location_filter = (Spinner) findViewById(R.id.request_spinner);
        all_location = new ArrayList<>();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        query=newText;
        SearchWithQuery(newText, location_filter.getSelectedItem().toString(), false);
        return true;
    }

    public class PopulateRequestList extends AsyncTask<String, Void, Void> {

        String response;
        OkHttpClient client = new OkHttpClient();
ProgressDialog pdialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog=new ProgressDialog(RequestBloodList.this);
            pdialog.setMessage("ব্লাড রিকোয়েস্ট লিস্ট লোড হচ্ছে ...");
            pdialog.show();

        }


        @Override
        protected Void doInBackground(String... params) {
            try {
                response = run(URL);
                Log.e("==>Response", response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("Bloodrequest");
                all_value.clear();
                all_location.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    bloodModel model = new bloodModel();
                    all_location.add(jsonArray.getJSONObject(i).optString("area"));
                    model.setName(jsonArray.getJSONObject(i).optString("name"));
                    model.setArea(jsonArray.getJSONObject(i).optString("area"));
                    model.setBloodgroup(jsonArray.getJSONObject(i).optString("blood"));
                    model.setPhone(jsonArray.getJSONObject(i).optString("phone"));
                    model.setDate(jsonArray.getJSONObject(i).optString("datetime"));
                    all_value.add(model);
                }

            } catch (Exception e) {
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter = new MyRecyclerAdapter(all_value, R.layout.search_card,1);
            rv.setAdapter(adapter);



            Set<String> hs = new HashSet<>();
            hs.addAll(all_location);
            all_location.clear();
            all_location.addAll(hs);


            String[] LocationArray = new String[all_location.size()];
            LocationArray = all_location.toArray(LocationArray);


            ArrayAdapter<String> spinnerArrayAdapter_child_death = new ArrayAdapter<String>(RequestBloodList.this, android.R.layout.simple_spinner_item, LocationArray);
            spinnerArrayAdapter_child_death.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            location_filter.setAdapter(spinnerArrayAdapter_child_death);

            pdialog.cancel();
        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
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















    private void setToolbar() {
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ব্লাড রিকোয়েস্ট লিস্ট");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
