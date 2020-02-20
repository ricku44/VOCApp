package com.example.greapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> ids;
    private ArrayList<String> texts;
    private ArrayList<String> buttons;
    private ArrayList<GradientDrawable> clrs;
    private Context mContext;


    public RecyclerViewAdapter(Context context, ArrayList<String> iDs,
             ArrayList<String> texTs,
             ArrayList<String> butTons, ArrayList<GradientDrawable> clRs) {
        ids = iDs;
        texts = texTs;
        buttons = butTons;
        clrs = clRs;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_details, parent, false);
            return new ViewHolder(view);
        }

        else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if(holder.getItemViewType()==0) {
            Log.d("c","c");
        }
        else{
            holder.crdv.setBackground(clrs.get(position-1));
            holder.txtid.setText(ids.get(position-1));
            holder.txttext.setText(texts.get(position-1));
            holder.btnbtn.setText(buttons.get(position-1));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else return 1;
    }


    @Override
    public int getItemCount() {
        return ids.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtid, txttext;
        Button btnbtn;
        CardView crdv;

        public ViewHolder(View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.ids);
            txttext = itemView.findViewById(R.id.names);
            btnbtn = itemView.findViewById(R.id.btns);
            crdv = itemView.findViewById(R.id.post_card_view);
        }
    }
}