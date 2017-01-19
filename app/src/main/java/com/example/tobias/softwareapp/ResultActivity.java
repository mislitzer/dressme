package com.example.tobias.softwareapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {

    DownloadTask task;
    String object;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Bundle extras = getIntent().getExtras();

        setStyles();
        doRequest(extras);
        getSupportActionBar().hide();
    }

    public void doRequest(Bundle extras) {
        this.location = extras.getString("location");
        task = new DownloadTask(extras);
        task.execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + this.location + "&appid=3802203926e764716c849e47883fa652&units=metric&cnt=1");
    }

    public void setStyles() {
        Typeface adam = Typeface.createFromAsset(getAssets(),"fonts/adam.ttf");
        Typeface sourcesanspro = Typeface.createFromAsset(getAssets(),"fonts/sourcesanspro.ttf");

        //Adam
        TextView state = (TextView) findViewById(R.id.state);
        state.setTypeface(adam);
        state.setTextSize(14);

        TextView temperature = (TextView) findViewById(R.id.temperature);
        temperature.setTypeface(adam);
        temperature.setTextSize(70);

        TextView morning = (TextView) findViewById(R.id.morningTempU);
        morning.setTypeface(adam);
        morning.setTextSize(25);

        TextView evening = (TextView) findViewById(R.id.eveningTempU);
        evening.setTypeface(adam);
        evening.setTextSize(25);

        TextView morninglbl = (TextView) findViewById(R.id.morningTemp);
        morninglbl.setTypeface(sourcesanspro);
        morninglbl.setTextSize(15);

        TextView eveninglbl = (TextView) findViewById(R.id.eveningTemp);
        eveninglbl.setTypeface(sourcesanspro);
        eveninglbl.setTextSize(15);

        //Labels
        TextView feelTempLbl = (TextView) findViewById(R.id.feelTempLabel);
        feelTempLbl.setTypeface(sourcesanspro);
        feelTempLbl.setTextSize(14);

        TextView windLabel = (TextView) findViewById(R.id.windLabel);
        windLabel.setTypeface(sourcesanspro);
        windLabel.setTextSize(14);

        TextView humidityLabel = (TextView) findViewById(R.id.humidityLabel);
        humidityLabel.setTypeface(sourcesanspro);
        humidityLabel.setTextSize(14);

        //Werte
        TextView feelTemp = (TextView) findViewById(R.id.feelTemp);
        feelTemp.setTypeface(sourcesanspro);
        feelTemp.setTextSize(14);

        TextView wind = (TextView) findViewById(R.id.windSpeed);
        wind.setTypeface(sourcesanspro);
        wind.setTextSize(14);

        TextView humidity = (TextView) findViewById(R.id.humidity);
        humidity.setTypeface(sourcesanspro);
        humidity.setTextSize(14);

        String actualDate = new SimpleDateFormat("d.M.yyyy").format(Calendar.getInstance().getTime());
        TextView dateHeader = (TextView) findViewById(R.id.dateHeader);
        dateHeader.setTypeface(adam);
        dateHeader.setText("Heute, " + actualDate);
        dateHeader.setTextSize(18);
        //dateHeader.setCompoundDrawablesWithIntrinsicBounds(getResources().getIdentifier("cog","drawable","com.example.tobias.softwareapp"), 0, 0, 0);
    }

    public void backToRoot(View view) {
        finish();
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //Aufruf wird ausgeführt
            conn.connect();
            int response = conn.getResponseCode();

            System.out.println("The response is: " + response);
            is = conn.getInputStream();

            String contentAsString = convertInputStreamToString(is, length);

            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                System.out.println("ERROR IMG");
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        String output;
        TextView dataView;
        String mediaMIMEType = ".png";
        String gender = "m";
        int sensibility = 1;
        //1 Wandern;2 Radfahren; 3 Drachensteigen
        int activity = 3;
        int altitude = 1;
        int avgTemp;

        public DownloadTask(Bundle extras) {
            this.gender = extras.getString("gender");
            this.sensibility = extras.getInt("sensibility");
            this.activity = extras.getInt("activity");
            this.altitude = extras.getInt("altitude");

            System.out.println("gender: " + this.gender);
            System.out.println("sensibility: " + this.sensibility);
            System.out.println("activity: " + this.activity);
            System.out.println("altitude: " + this.altitude);
            System.out.println("location: " + location);
        }

        @Override
        protected String doInBackground(String... params) {
            //dataView = (TextView) findViewById(R.id.dataReturn);
            try {
                object = downloadContent(params[0]);
                return object;
            } catch (IOException e) {
                return "Keine Daten gefunden";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            setContent();
        }

        public void setContent() {
            try {
                //dataView.setText(object);
                //Ergebnisse anzeigen
                System.out.println(object);
                validateResult(object);

                //Toast.makeText(MainActivity.this, "Infos wurden geladen", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {
                //Toast.makeText(MainActivity.this, "Fehler beim Laden der Infos", Toast.LENGTH_LONG).show();
            }
        }

        private float getFloat(String tagName, JSONObject jObj) throws JSONException {
            return (float) jObj.getDouble(tagName);
        }

        private int getInt(String tagName, JSONObject jObj) throws JSONException {
            return (int) jObj.getInt(tagName);
        }

        public void validateResult(String object) throws JSONException {
            try {
                System.out.println(object);
                JSONObject jObj = new JSONObject(object);
                JSONObject tempObject = getTemp(object);

                //Gefühlte Temperatur
                double tempFactor = round(getTempFactor(jObj, tempObject), 1);
                TextView feeledTemp = (TextView) findViewById(R.id.feelTemp);
                feeledTemp.setText((int) tempFactor + "°C");

                //Weather-Temp-Sense Category
                String wtsCategory = getWTSCategory(tempFactor, tempObject, jObj);

                //MediaName
                String mediaName = this.gender + "_" + this.activity + "_" + wtsCategory + this.mediaMIMEType;
                setResultView(mediaName);

                //Output
                addToOutput("TEMP: " + tempFactor);
                addToOutput("MEDIA: " + mediaName);
                printData();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void setResultView(String media) {
            String baseUrl = "http://www.martinislitzer.at/dressme/";
            String obenL1 = baseUrl + this.activity + "/" + this.gender + "/oben/L1/" + media;
            String obenL2 = baseUrl + this.activity + "/" + this.gender + "/oben/L2/" + media;
            String obenL3 = baseUrl + this.activity + "/" + this.gender + "/oben/L3/" + media;

            String mitte = baseUrl + this.activity + "/" + this.gender + "/mitte/" + media;
            String unten = baseUrl + this.activity + "/" + this.gender + "/unten/" + media;

            String sCaps = baseUrl + this.activity + "/" + this.gender + "/sonstiges/Caps/" + media;
            String sGloves = baseUrl + this.activity + "/" + this.gender + "/sonstiges/Handschuhe/" + media;
            String sUmbrella = baseUrl + this.activity + "/" + this.gender + "/sonstiges/Schirm/" + media;

            //Oben
            //L1
            ImageView oben1V = (ImageView) findViewById(R.id.resultViewTopL1);
            new DownloadImageTask(oben1V).execute(obenL1);

            //L2
            ImageView oben2V = (ImageView) findViewById(R.id.resultViewTopL2);
            new DownloadImageTask(oben2V).execute(obenL2);

            //L3
            ImageView oben3V = (ImageView) findViewById(R.id.resultViewTopL3);
            new DownloadImageTask(oben3V).execute(obenL3);

            //Mitte
            ImageView mitteV = (ImageView) findViewById(R.id.resultViewMiddle);
            new DownloadImageTask(mitteV).execute(mitte);

            //Unten
            ImageView bottomV = (ImageView) findViewById(R.id.resultViewBottom);
            new DownloadImageTask(bottomV).execute(unten);

            //Sonstiges
            //Kappen
            ImageView add1V = (ImageView) findViewById(R.id.resultViewAddL1);
            new DownloadImageTask(add1V).execute(sCaps);

            //Handschuhe
            ImageView add2V = (ImageView) findViewById(R.id.resultViewAddL2);
            new DownloadImageTask(add2V).execute(sGloves);

            //Regenschirm
            ImageView add3V = (ImageView) findViewById(R.id.resultViewAddL3);
            new DownloadImageTask(add3V).execute(sUmbrella);

            String activityLabel;
            switch (this.activity) {
                case 1:
                    activityLabel = "Wandern";
                    break;

                case 2:
                    activityLabel = "Radfahren";
                    break;

                case 3:
                    activityLabel = "Casual";
                    break;

                default:
                    activityLabel = "Casual";
                    break;
            }

            TextView resultInfo = (TextView) findViewById(R.id.resultMesssage);
            resultInfo.setText("Hier deine Ergebnisse für " + activityLabel + " in " + location);
        }

        private String getWTSCategory(double tempFactor, JSONObject tempObject, JSONObject jObj) throws JSONException {
            JSONArray jArr = jObj.getJSONArray("list");
            JSONObject json = jArr.getJSONObject(0);
            JSONArray weather = json.getJSONArray("weather");
            JSONObject wKindObject = weather.getJSONObject(0);
            int weatherId = getInt("id", wKindObject);

            String condition = getCondition(weatherId);
            TextView wicon = (TextView) findViewById(R.id.state);

            String iconCondition = condition;
            if (this.avgTemp < 0 && iconCondition == "n") {
                iconCondition = "nw";
            }

            wicon.setCompoundDrawablesWithIntrinsicBounds(getResources().getIdentifier(iconCondition,"drawable","com.example.tobias.softwareapp"), 0, 0, 0);

            if (condition == "n") {
                wicon.setText(" Niederschlag");
                wicon.setTextSize(13);
            }
            else if (condition == "h") {
                wicon.setText(" Heiter");
            }
            else if (condition == "s") {
                wicon.setText(" Sonnenschein");
                wicon.setTextSize(13);
            }

            int conditionDeflator;
            if (tempFactor < 0) {
                conditionDeflator = 1;
            }
            else if (tempFactor >= 0 && tempFactor <= 10) {
                conditionDeflator = 2;
            }
            else {
                conditionDeflator = 3;
            }

            return condition + conditionDeflator;
        }

        private String getCondition(int weatherId) {
            String condition;
            //Niederschlag
            if ((weatherId >= 200 && weatherId <= 781) || (weatherId >= 900 && weatherId <= 962)) {
                condition = "n";
            }
            //Sonnenschein
            else if (weatherId == 800) {
                condition = "s";
            }
            //Heiter
            else if (weatherId >= 801 && weatherId <= 804) {
                condition = "h";
            }
            //Heiter
            else {
                condition = "h";
            }

            return condition;
        }

        public JSONObject getTemp(String object) throws JSONException {
            JSONObject jObj = new JSONObject(object);
            JSONArray jArr = jObj.getJSONArray("list");
            JSONObject jsonWeather = jArr.getJSONObject(0);
            JSONObject jsonTemp = jsonWeather.getJSONObject("temp");

            return jsonTemp;
        }

        public double getTempFactor(JSONObject object, JSONObject temp) throws JSONException {
            double tempFactor = 0;
            float tempWithoutChill = getAverageTempWithoutWindChill(temp);
            float windSpeed = getWind(object);
            double tempWithChill = getWindChillTemperature(tempWithoutChill, windSpeed);
            double tempWithAltitude = getTempWithAltitude(tempWithChill);
            tempFactor = getTempWithSense(tempWithAltitude);

            //Beim Radfahren zusätzlich noch den Wind wegrechen
            if (this.activity == 2) {
                tempFactor = tempFactor - windSpeed;
            }

            return tempFactor;
        }

        private double getTempWithAltitude(double temp) {
            return temp - altitudeTempMap();
        }

        private double getTempWithSense(double temp) {
            return temp + senseTempMap();
        }

        private int altitudeTempMap() {
            int decrease;
            switch (this.altitude) {
                //1000-2000
                case 1:
                    decrease = 5;
                    break;

                //2000-3000m
                case 2:
                    decrease = 13;
                    break;

                //3000-4000
                case 3:
                    decrease = 20;
                    break;

                default:
                    decrease = 0;
                    break;
            }

            return decrease;
        }

        private int senseTempMap() {
            int decrease;
            switch (this.sensibility) {
                //leicht zu heiss
                case 3:
                    decrease = -2;
                    break;

                //sehr zu heiss
                case 4:
                    decrease = -4;
                    break;

                //neutral
                case 2:
                    decrease = 0;
                    break;

                //leicht zu kalt
                case 1:
                    decrease = 2;
                    break;

                //sehr zu kalt
                case 0:
                    decrease = 4;
                    break;

                default:
                    decrease = 0;
                    break;
            }

            return decrease;
        }

        public double getWindChillTemperature(float tempWithoutChill, float windSpeed) {
            double chillTemp = 13.12 + 0.6215 * tempWithoutChill - 11.37 * Math.pow(windSpeed, 0.16) + 0.3965 * tempWithoutChill * Math.pow(windSpeed, 0.16);
            return chillTemp;
        }

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }

        public float getWind(JSONObject object) throws JSONException {
            JSONArray jArr = object.getJSONArray("list");
            JSONObject json = jArr.getJSONObject(0);
            float wind = getFloat("speed", json);
            float humidity = getFloat("humidity", json);

            TextView windVal = (TextView) findViewById(R.id.windSpeed);
            windVal.setText(round(wind, 1) + " km/h");

            TextView humidityVal = (TextView) findViewById(R.id.humidity);
            humidityVal.setText((int) humidity + "%");

            return wind;
        }

        public float getAverageTempWithoutWindChill(JSONObject tempObject) throws JSONException {
            float morning = getFloat("morn", tempObject);
            float day = getFloat("day", tempObject);
            float evening = getFloat("eve", tempObject);
            float averageTemp = (morning + day + evening) / 3;

            TextView tempLabel = (TextView) findViewById(R.id.temperature);
            tempLabel.setText((int) averageTemp + "°C");
            this.avgTemp = (int) averageTemp;

            TextView morningVal = (TextView) findViewById(R.id.morningTempU);
            morningVal.setText((int) morning + "°C");

            TextView eveningVal = (TextView) findViewById(R.id.eveningTempU);
            eveningVal.setText((int) evening + "°C");

            return averageTemp;
        }

        public void addToOutput(String value) {
            this.output = this.output + "\n\r" + value;
        }

        public void printData() {
            dataView.setText(this.output);
        }
    }
}
