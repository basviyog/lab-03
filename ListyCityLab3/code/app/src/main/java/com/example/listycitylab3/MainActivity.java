
package com.example.listycitylab3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listycitylab3.AddCityFragment;
import com.example.listycitylab3.City;
import com.example.listycitylab3.CityArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddCityFragment.AddCityDialogListener {
    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);

        cityAdapter.notifyDataSetChanged();
    }


    @Override
    public void editCity(City updatedCity, int position) {
        if (position >= 0 && position < dataList.size()) {
            dataList.set(position, updatedCity);
            cityAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] cities = {"Edmonton", "Vancouver", "Moscow"};        String[] provinces = {"AB", "BC", "ON"};

        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        // Use the custom CityArrayAdapter
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList = findViewById(R.id.city_list); // from activity_main.xml
        cityList.setAdapter(cityAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_city); // from activity_main.xml
        fab.setOnClickListener(v -> {

            AddCityFragment.newInstance().show(getSupportFragmentManager(), "ADD_CITY_DIALOG");
        });


        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City cityToEdit = dataList.get(position);

                AddCityFragment.newInstance(cityToEdit, position).show(getSupportFragmentManager(), "EDIT_CITY_DIALOG");
            }
        });
    }
}
