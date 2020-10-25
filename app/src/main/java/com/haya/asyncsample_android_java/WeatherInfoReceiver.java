package com.haya.asyncsample_android_java;

import android.os.AsyncTask;
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

public abstract class WeatherInfoReceiver extends AsyncTask<String, String, String> {

  private TextView tvWeatherTelop;
  private TextView tvWeatherDesc;

  public WeatherInfoReceiver(TextView tvWeatherTelop, TextView tvWeatherDesc) {
    this.tvWeatherTelop = tvWeatherTelop;
    this.tvWeatherDesc = tvWeatherDesc;
  }

  protected String doInBackground(String params) {
    String id = params[0];
    String urlStr = "" + id;
    String result = "";

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

      JSONObject forecastsNow = forecasts.getJSONObject(0);

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
