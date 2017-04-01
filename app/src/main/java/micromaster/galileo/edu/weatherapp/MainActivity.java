package micromaster.galileo.edu.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import micromaster.galileo.edu.weatherapp.API.WeatherInterface;
import micromaster.galileo.edu.weatherapp.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final static String BASE_URL = "http://api.wunderground.com/api/";
    private final static String API_KEY = "202b429096052090";

    private TextView tv_country;
    private TextView tv_temp;
    private TextView tv_pressure;
    private TextView tv_humidity;
    private TextView tv_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_country = (TextView) findViewById(R.id.countryName);
        tv_temp = (TextView) findViewById(R.id.temperature);
        tv_pressure = (TextView) findViewById(R.id.pressure);
        tv_humidity = (TextView) findViewById(R.id.humidity);
        tv_weather = (TextView) findViewById(R.id.weather);

        new GetInfo().execute();
    }

    private class GetInfo extends AsyncTask<Void, Void, WeatherResponse> {

        @Override
        protected WeatherResponse doInBackground(Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WeatherInterface weatherInterface = retrofit.create(WeatherInterface.class);
            Call<WeatherResponse> call = weatherInterface.getWeatherFromSanFrancisco(API_KEY);
            WeatherResponse weatherResponse = null;
            try {
                weatherResponse = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherResponse;
        }

        @Override
        protected void onPostExecute(WeatherResponse weatherResponse) {
            super.onPostExecute(weatherResponse);

            if (weatherResponse != null) {
                tv_country.setText(weatherResponse.getWeatherData().getDisplayLocation().getCityName());
                tv_temp.setText(weatherResponse.getWeatherData().getTemp());
                tv_pressure.setText(weatherResponse.getWeatherData().getPressure().toString());
                tv_humidity.setText(weatherResponse.getWeatherData().getHumidity());
                tv_weather.setText(weatherResponse.getWeatherData().getWeather());
            }
        }
    }
}
