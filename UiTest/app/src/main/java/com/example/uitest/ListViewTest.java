package com.example.uitest;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/*
1.创建一个ArrayAdapter
2.list.setAdapter
 */
public class ListViewTest extends AppCompatActivity {

    private ListView listView;
    private String data[] = {"1", "2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
    private Vehicle vehicles[] = {new Vehicle("car", R.drawable.car),
                                  new Vehicle("bike", R.drawable.bike),
                                  new Vehicle("plane", R.drawable.plane),
                                  new Vehicle("ship", R.drawable.ship),
                                  new Vehicle("plane", R.drawable.plane),
                                  new Vehicle("bike", R.drawable.bike),
                                  new Vehicle("car", R.drawable.car),
                                  new Vehicle("car", R.drawable.car)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_view);

        listView = (ListView)findViewById(R.id.list_view);

/*        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListViewTest.this,
                android.R.layout.simple_list_item_1, data);
                listView.setAdapter(arrayAdapter);
                */
//自定义Adapter
        VehicleAdapter vehicleAdapter = new VehicleAdapter(ListViewTest.this, R.layout.layout_vehile, vehicles);
        listView.setAdapter(vehicleAdapter);

    }

    private class Vehicle{

        public String name;
        public int id;

        public Vehicle(String name, int id){
            this.name = name;
            this.id = id;
        }
    }

    private class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
    //自定义Adapter
    private class VehicleAdapter extends ArrayAdapter<Vehicle>{

        private int resourceId;
        public VehicleAdapter(@NonNull Context context, int resource, @NonNull Vehicle[] objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Vehicle vehicle = getItem(position);
            View view = null;
            ViewHolder viewHolder = null;

            if(convertView == null){
                 view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

                 viewHolder = new ViewHolder();
                 viewHolder.imageView = (ImageView)view.findViewById(R.id.image_pic);
                 viewHolder.textView = (TextView)view.findViewById(R.id.text_name);
                 view.setTag(viewHolder);
            }else{
                 view = convertView;
                 viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.textView.setText(vehicle.name);
            viewHolder.imageView.setImageResource(vehicle.id);

            return view;
        }
    }
}
