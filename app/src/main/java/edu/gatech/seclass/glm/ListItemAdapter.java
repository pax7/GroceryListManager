package edu.gatech.seclass.glm;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import java.util.HashSet;
import com.daimajia.swipe.SwipeLayout;


class ListItemAdapter extends ArrayAdapter<ListItem> {

    private final HashSet<SwipeLayout> _itemManager=new HashSet<>();
    private static ListItemAdapter _instance;

    ListItemAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, GroceryList.getCurrent().listItemList);
        _instance=this;
    }

    public static void refresh()
    {
        _instance.notifyDataSetChanged();
    }

    @Override
    @NonNull
    public View getView(int p_position, View convertView, @NonNull ViewGroup parent) {

        final ListItem listItem = GroceryList.getCurrent().listItemList.get(p_position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_item, parent, false);
        }

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id._checkBox);
        checkBox.setText(listItem.getItem().getName());

        LinearLayout cell = (LinearLayout) convertView.findViewById(R.id._cell);
        if(listItem.isNew())cell.setBackgroundColor(Color.parseColor("#E0E0FF"));
        else cell.setBackgroundColor(Color.parseColor("#FFFFFF"));


        final TextView _item_type_divider=(TextView) convertView.findViewById(R.id._item_type_divider);
        _item_type_divider.setText(listItem.getItem().getItemType().getName());
        if(p_position==0 || !GroceryList.getCurrent().listItemList.get(p_position-1).getItem().getItemType().equals(listItem.getItem().getItemType()))
        {
            _item_type_divider.setVisibility(View.VISIBLE);
        }
        else _item_type_divider.setVisibility(View.GONE);



        final NumberPicker _quantity= (NumberPicker) convertView.findViewById(R.id._quantity);
        _quantity.setMaxValue(99);
        _quantity.setMinValue(1);
        _quantity.setValue(listItem.getQuantity());
        _quantity.setWrapSelectorWheel(false);
        //Toast toast = Toast.makeText(convertView.getContext(), listItem.getQuantity()+"", Toast.LENGTH_SHORT);
        //toast.show();
        _quantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(listItem.getQuantity()==newVal) return;
                listItem.setQuantity(_quantity.getValue());
            }
        });

        final View delete = convertView.findViewById(R.id._delete);
        final SwipeLayout swipe= (SwipeLayout) convertView.findViewById(R.id._swipe);
        _itemManager.add(swipe);
        swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                for(SwipeLayout s: _itemManager)
                    if(!s.equals(layout))s.close();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        checkBox.setChecked(listItem.isChecked());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipe.close(false);
                GroceryList.getCurrent().deleteListItem(listItem);
                GroceryListView.update();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.check(checkBox.isChecked());
                GroceryListView.update();
                //Toast toast = Toast.makeText(v.getContext(), check.isChecked()+"", Toast.LENGTH_SHORT);
                //toast.show();
            }
        });

        return convertView;
    }
}