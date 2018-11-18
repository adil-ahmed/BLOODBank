package bloodbank.eatl.com.bloodbank.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bloodbank.eatl.com.bloodbank.R;

public class MyRecyclerAdapterBloodBank extends RecyclerView.Adapter<MyRecyclerAdapterBloodBank.ViewHolder> {

    private Context context;
    private ArrayList<bloodbankmodel> items;
    private int itemLayout;
    private int switchVal;
    private ArrayList<String> latList;
    private ArrayList<String> longList;
    public MyRecyclerAdapterBloodBank(
            Context context,
            ArrayList<bloodbankmodel> items,
            ArrayList<String> latList,
            ArrayList<String> longList,
            int itemLayout, int switchVal) {
        this.context = context;
        this.items = items;
        this.latList = latList;
        this.longList = longList;
        this.itemLayout = itemLayout;
        this.switchVal = switchVal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int poposition) {


        if (switchVal == 0) {

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Adapter", ""+poposition);
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("lat", latList.get(poposition));
                    intent.putExtra("lon", longList.get(poposition));
                    intent.putExtra("name", items.get(poposition).getName());
                    view.getContext().startActivity(intent);
                }
            });
            holder.bloodbank.setText("Blood Bank : " + items.get(poposition).getName());
            holder.address.setText("Address : " + items.get(poposition).getAddress());
            holder.president_phone.setText("Phone President : " + items.get(poposition).getPhoneP());
            holder.gs_phone.setText("Phone GS : " + items.get(poposition).getPhoneG());
            holder.website.setText("Website  : " + items.get(poposition).getWebsite());
            holder.email.setText("Email  : " + items.get(poposition).getEmail());
            holder.facebook.setText("Facebook  : " + items.get(poposition).getFacebook());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bloodbank;
        public TextView address;
        public TextView president_phone;
        public TextView gs_phone;
        public TextView website;
        public TextView facebook;
        public TextView email;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            bloodbank = (TextView) itemView.findViewById(R.id.card_bloodbank);
            address = (TextView) itemView.findViewById(R.id.card_bloodbank_Address);
            president_phone = (TextView) itemView.findViewById(R.id.card_bloodbank_ps_phone);
            gs_phone = (TextView) itemView.findViewById(R.id.card_bloodbank_gs_phone);
            website = (TextView) itemView.findViewById(R.id.card_bloodbank_website);
            facebook = (TextView) itemView.findViewById(R.id.card_bloodbank_facebook);
            email=(TextView)itemView.findViewById(R.id.card_bloodbank_email);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}