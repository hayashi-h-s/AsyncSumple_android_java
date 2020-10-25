package com.haya.asyncsample_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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


  private class WeatherInfoReceiver extends AsyncTask<String, String, String> {

    private TextView tvWeatherTelop;
    private TextView tvWeatherDesc;

    public WeatherInfoReceiver(TextView tvWeatherTelop, TextView tvWeatherDesc) {
      this.tvWeatherTelop = tvWeatherTelop;
      this.tvWeatherDesc = tvWeatherDesc;
    }

    @Override
    protected String doInBackground(String... params) {

      String id = params[0];
      String urlStr = "http://weather.livedoor.com/forecast/webservice/json/v1 + id";
      String result = "";

      // HTTP接続を行う
      HttpURLConnection con = null;

      InputStream is = null;
      try {
        URL url = new URL(urlStr);
        con = (HttpURLConnection) url.openConnection();
        con.connect();
        is = con.getInputStream();

        result = is2String(is);

      } catch (MalformedURLException e) {

      } catch (IOException e) {

      } finally {
        if (con != null) {
          con.disconnect();
        }

        if (is != null) {
          try {
            is.close();
          } catch (IOException e) {

          }
        }
      }
      return result;
    }

    @Override
    public void onPostExecute(String result) {
      String telop = "";
      String desc = "";
      try {
        JSONObject rootObject = new JSONObject(result);
        JSONObject description = rootObject.getJSONObject("description");
        JSONObject forecasts = rootObject.getJSONObject("forecasts");

        JSONObject forecastsNow = forecasts.getJSONObject(String.valueOf(0));

        telop = forecastsNow.getString("telop");
      } catch (JSONException ex) {

      }

      tvWeatherTelop.setText(telop);
      tvWeatherDesc.setText(desc);
    }

    private String is2String(InputStream is) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      StringBuffer sb = new StringBuffer();
      char[] b = new char[1024];
      int line;
      while (0 <= (line = reader.read(b))) {
        {
          sb.append(b, 0, line);
        }
      }
      return sb.toString();
    }
  }


}