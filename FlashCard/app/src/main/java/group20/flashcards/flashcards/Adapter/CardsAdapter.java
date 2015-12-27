package group20.flashcards.flashcards.Adapter;

/**
 * Created by root on 11/7/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group20.flashcards.flashcards.CardStructure.FlashCard;
import group20.flashcards.flashcards.CardStructure.Subject;


public class CardsAdapter extends ArrayAdapter<FlashCard> {

    private static class ViewHolder {
        private TextView itemView;
    }

    public CardsAdapter(Context context, int textViewResourceId, ArrayList<FlashCard> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FlashCard item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(String.format("%s", item.getTitle()));
        }

        return convertView;
    }
}
