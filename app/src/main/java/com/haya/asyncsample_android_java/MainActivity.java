package com.haya.asyncsample_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  ListView mListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mListView = findViewById(R.id.listView);
    List<Map<String, String>> cityList = new ArrayList<>();

    Map<String, String> city = new HashMap<>();
    city.put("name", "大阪");
    city.put("id", "1");
    cityList.add(city);

    city = new HashMap<>();
    city.put("name", "静岡");
    city.put("id", "2");
    cityList.add(city);

    city = new HashMap<>();
    city.put("name", "東京");
    city.put("id", "3");
    cityList.add(city);

    String[] from = {"name"};
    int[] to = {android.R.id.text1};

    SimpleAdapter adapter = new SimpleAdapter(
        MainActivity.this,
        cityList,
        android.R.layout.simple_expandable_list_item_1,
        from,
        to
    );

    mListView.setAdapter(adapter);
    mListView.setOnItemClickListener(new ListItemClickListener());

  }

  private class ListItemClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
      String cityName = item.get("name");
      String cityId = item.get("id");

      TextView tvCityName = findViewById(R.id.tvCityName);
      tvCityName.setText(cityName + "の天気");

      TextView tvWeatherTelop = findViewById(R.id.tvWeatherTelop);
      TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);

      WeatherInfoReceiver receiver = new WeatherInfoReceiver(tvWeatherTelop, tvWeatherDesc) {
        @SuppressLint("StaticFieldLeak")
        @Override
        protected String doInBackground(String... strings) {
          return null;
        }
      };
      receiver.execute(cityId);

    }
  }
}