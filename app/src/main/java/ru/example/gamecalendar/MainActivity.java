package ru.example.gamecalendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.example.gamecalendar.model.Game;
import ru.example.gamecalendar.model.Month;
import ru.example.gamecalendar.parse.GamesParse;

public class MainActivity extends AppCompatActivity {
    LinearLayout contentLinearLayout;
    Spinner yearSpinner, monthSpinner;
    Context context;
    Map<Integer,ArrayList<Month>> mapDate;
    int curYear;
    Month curMonth;
    String curUrl;
    boolean isLoadedYearSpinner, isLoadedMonthSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curUrl = "2020/january/";
        curYear = 2020;
        curMonth = Month.equalsName("january");
        isLoadedYearSpinner = false;
        isLoadedMonthSpinner = false;
        yearSpinner = (Spinner) findViewById(R.id.spinnerYear);
        monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!isLoadedYearSpinner) {
                    isLoadedYearSpinner = true;
                    return;
                }
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                curYear = year;
                ArrayList<Month> months = mapDate.get(year);
                List<String> monthsString = new ArrayList<>();
                for(Month month: months) {
                    monthsString.add(month.getTranslate());
                }

                ArrayAdapter<String> spinnerArrayAdapterMonth = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, monthsString);
                spinnerArrayAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                monthSpinner.setAdapter(spinnerArrayAdapterMonth);
                monthSpinner.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!isLoadedMonthSpinner) {
                    isLoadedMonthSpinner = true;
                    return;
                }
                Month month = Month.parse(monthSpinner.getSelectedItem().toString());
                curUrl = curYear+"/"+month.toString()+"/";
                new GameTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        contentLinearLayout = (LinearLayout) findViewById(R.id.contentLinLayout);
        context = this;
        new DateGameTask().execute();
        new GameTask().execute();
    }

    class DateGameTask extends AsyncTask<Void, Void, Void> {

        List<String> keysString;
        List<String> monthsString;

        @Override
        protected Void doInBackground(Void... voids) {
            mapDate = GamesParse.parsingListDate("https://www.igromania.ru/games-calendar");

            Set<Integer> keys = mapDate.keySet();
            keysString = new ArrayList<>();
            for(Integer i : keys) {
                keysString.add(i.toString());
            }

            ArrayList<Month> months = mapDate.get(curYear);
            monthsString = new ArrayList<>();
            for(Month month: months) {
                monthsString.add(month.getTranslate());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keysString);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            yearSpinner.setAdapter(spinnerArrayAdapter);
            yearSpinner.setSelection(1);

            ArrayAdapter<String> spinnerArrayAdapterMonth = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, monthsString);
            spinnerArrayAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            monthSpinner.setAdapter(spinnerArrayAdapterMonth);
        }
    }

    class GameTask extends AsyncTask<Void, Void, Void> {

        ArrayList<LinearLayout> itemsLinearLayout;

        @Override
        protected void onPreExecute() {
            contentLinearLayout.removeAllViews();
            TextView textView = new TextView(context);
            textView.setText("\n\n\n\n\n\nИдет загрузка, пожалуйста подождите.");
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextSize(25);
            contentLinearLayout.addView(textView);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<Game> gameArrayList = GamesParse.parsingGames("https://www.igromania.ru/games-calendar/"+curUrl);

            itemsLinearLayout = new ArrayList<>();

            for(Game game : gameArrayList) {
                //item
                LinearLayout itemLinearLayout = new LinearLayout(context);
                itemLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                itemLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                //image
                ImageView iv = new ImageView(context);
                new DownloadImageTask(iv).execute(game.getPicURL());
                itemLinearLayout.addView(iv);
                iv.getLayoutParams().width = 300;
                iv.getLayoutParams().height = 300;
                iv.setPadding(20, 0, 20, 0);


                //info
                LinearLayout layoutItemRight = new LinearLayout(context);
                layoutItemRight.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layoutItemRight.setOrientation(LinearLayout.VERTICAL);


                itemLinearLayout.addView(layoutItemRight);
                TextView name = new TextView(context);
                TextView date = new TextView(context);
                TextView platform = new TextView(context);
                TextView genre = new TextView(context);
                name.setText(game.getName());
                date.setText(game.getDate());
                platform.setText(game.getPlatform());
                genre.setText(game.getGenre());
                layoutItemRight.addView(name);
                layoutItemRight.addView(date);
                layoutItemRight.addView(platform);
                layoutItemRight.addView(genre);
                itemsLinearLayout.add(itemLinearLayout);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            contentLinearLayout.removeAllViews();
            for(LinearLayout itemLinearLayout: itemsLinearLayout){
                contentLinearLayout.addView(itemLinearLayout);
            }
        }
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
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
