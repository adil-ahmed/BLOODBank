package bloodbank.eatl.com.bloodbank.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bloodbank.eatl.com.bloodbank.R;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private ArrayList<bloodModel> items;
    private int itemLayout;
    private int switchVal;

    public MyRecyclerAdapter(ArrayList<bloodModel> items, int itemLayout,int switchVal) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.switchVal=switchVal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int poposition) {


        if(switchVal==0) {

            holder.name.setText("Name : " + items.get(poposition).getName());
            holder.phone.setText("Phone : " + items.get(poposition).getPhone());
            holder.email.setText("Email : " + items.get(poposition).getEmail());
            holder.area.setText("Area : " + items.get(poposition).getArea());
            holder.bloodgroup.setText("Blood group : " + items.get(poposition).getBloodgroup());
        }else {

            holder.name.setText("Name : " + items.get(poposition).getName());
            holder.phone.setText("Phone : " + items.get(poposition).getPhone());
            holder.email.setText("Date : " + items.get(poposition).getDate());
            holder.area.setText("Area : " + items.get(poposition).getArea());
            holder.bloodgroup.setText("Blood group : " + items.get(poposition).getBloodgroup());

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView phone;
        public TextView email;
        public TextView area;
        public TextView bloodgroup;



        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.card_name);
            phone = (TextView) itemView.findViewById(R.id.card_phone);
            email = (TextView) itemView.findViewById(R.id.card_email);
            area = (TextView) itemView.findViewById(R.id.card_address);
            bloodgroup = (TextView) itemView.findViewById(R.id.card_bloodgroup);



        }
    }
}