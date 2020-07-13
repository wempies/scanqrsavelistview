package com.example.scanqrsavelistview.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanqrsavelistview.Model.ListItem;
import com.example.scanqrsavelistview.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {


    List<ListItem> listItemArrayList;
    Context context;
    public MyAdapter(List<ListItem> listItemsArrayList, Context context){
        this.listItemArrayList = listItemsArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout,parent,false);
        return new MyAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {

        ListItem listItem =  listItemArrayList.get(position);

        holder.textViewCode.setText(listItem.getCode());
        holder.textViewType.setText(listItem.getType());
        Linkify.addLinks(holder.textViewCode,Linkify.ALL);

    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView textViewCode, textViewType;
        CardView cardView;

        public MyAdapterViewHolder(final View itemView){
            super(itemView);
            textViewCode = (TextView)itemView.findViewById(R.id.textViewCode);
            textViewType = (TextView)itemView.findViewById(R.id.textViewType);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = listItemArrayList.get(getAdapterPosition()).getType();

                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_TEXT,type);
                    i.setType("text/plain");
                    itemView.getContext().startActivity(i);
                }
            });

        }

    }
}
