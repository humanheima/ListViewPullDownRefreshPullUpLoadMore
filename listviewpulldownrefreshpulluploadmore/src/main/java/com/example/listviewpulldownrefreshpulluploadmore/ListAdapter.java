package com.example.listviewpulldownrefreshpulluploadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ListAdapter extends ArrayAdapter<String> {


    protected List<String> items;
    private Context contex;

    public ListAdapter(Context context, List<String> items) {
        super(context, 0);
        this.items = items;
        this.contex = context;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        VH holder;
        String text = items.get(position);
        if (convertView == null) {
            holder = new VH();
            view = LayoutInflater.from(contex).inflate(R.layout.item, parent, false);
            holder.textView = (TextView) view.findViewById(R.id.textView);
            view.setTag(holder);
        } else {
            view=convertView;
            holder= (VH) view.getTag();
        }
        holder.textView.setText(text);
        return view;


    }

    static class VH {
        TextView textView;
    }


}
