package at.fh_kufstein.tobias.softwareapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    String geschlecht;
    int meter = -1;
    int temperatur;
    int activity;

    RelativeLayout main;
    RadioGroup radios;
    Button maennlich;
    Button weiblich;
    Button dress;
    EditText text;
    SeekBar temp;

    RadioButton clicked;
    /*
    RadioButton wandern;
    RadioButton radfahren;
    RadioButton drachensteigen;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fonts
        setStyles();

        radios = (RadioGroup) findViewById(R.id.radioGroup);
        dress = (Button) findViewById(R.id.dress);
        maennlich = (Button) findViewById(R.id.button2);
        weiblich = (Button) findViewById(R.id.button);
        text = (EditText) findViewById(R.id.editText);
        temp = (SeekBar) findViewById(R.id.seekBar);
        main = (RelativeLayout) findViewById(R.id.activity_main);

        /*
        wandern = (RadioButton) findViewById(R.id.radioButton);
        radfahren = (RadioButton) findViewById(R.id.radioButton2);
        drachensteigen = (RadioButton) findViewById(R.id.radioButton3);
        */

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String standort = "";
                    standort = String.valueOf(text.getText());

                    if (!standort.isEmpty() && geschlecht != null && radios.getCheckedRadioButtonId() != -1 /*&& meter > -1*/){
                        temperatur = temp.getProgress();
                        Intent intent = new Intent(view.getContext(), ResultActivity.class);
                        intent.putExtra("gender", geschlecht);
                        intent.putExtra("sensibility",temperatur);
                        intent.putExtra("activity", activity);
                        intent.putExtra("altitude", meter);
                        intent.putExtra("location", standort);
                        startActivity(intent);

                    } else{
                        //System.out.println("alles ausfüllen" + temperatur);
                        Toast.makeText(MainActivity.this, "Bitte wähle alle deine Optionen aus", Toast.LENGTH_LONG).show();
                    }
            }
        });
        maennlich.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.parseColor("#00CA74"));
                geschlecht = "m";
                weiblich.setBackgroundColor(Color.parseColor("#C4F3DF"));
            }
        });

        weiblich.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.parseColor("#00CA74"));
                geschlecht = "w";
                maennlich.setBackgroundColor(Color.parseColor("#C4F3DF"));
            }
        });

    }

    public void setStyles() {
        Typeface adam = Typeface.createFromAsset(getAssets(),"fonts/adam.ttf");
        Typeface sourcesanspro = Typeface.createFromAsset(getAssets(),"fonts/sourcesanspro.ttf");

        //Labels
        TextView dialog = (TextView) findViewById(R.id.dialog);
        dialog.setTypeface(sourcesanspro);
        dialog.setTextSize(18);

        TextView button = (TextView) findViewById(R.id.button);
        button.setTypeface(adam);
        button.setTextSize(18);

        TextView button2 = (TextView) findViewById(R.id.button2);
        button2.setTypeface(adam);
        button2.setTextSize(18);

        TextView txt2 = (TextView) findViewById(R.id.textView2);
        txt2.setTypeface(sourcesanspro);
        txt2.setTextSize(16);

        TextView txt3 = (TextView) findViewById(R.id.textView3);
        txt3.setTypeface(sourcesanspro);
        txt3.setTextSize(16);

        TextView txt4 = (TextView) findViewById(R.id.text4);
        txt4.setTypeface(sourcesanspro);
        txt4.setTextSize(16);

        TextView radio = (TextView) findViewById(R.id.radioButton);
        radio.setTypeface(adam);
        radio.setTextSize(14);

        TextView radio2 = (TextView) findViewById(R.id.radioButton2);
        radio2.setTypeface(adam);
        radio2.setTextSize(14);

        TextView radio3 = (TextView) findViewById(R.id.radioButton3);
        radio3.setTypeface(adam);
        radio3.setTextSize(14);

        TextView standort = (TextView) findViewById(R.id.standort);
        standort.setTypeface(adam);
        standort.setTextSize(16);

        TextView dress = (TextView) findViewById(R.id.dress);
        dress.setTypeface(adam);
        dress.setTextSize(18);
    }

    public void radioClick(View view){
        view.setBackgroundColor(Color.parseColor("#F46652"));
        final View tempor = view;
        switch (radios.getCheckedRadioButtonId()) {
            case R.id.radioButton:
                activity = 1;
                break;
            case R.id.radioButton2:
                activity = 2;
                break;
            case R.id.radioButton3:
                activity = 3;
                break;
            default:
                System.out.println("ein Fehler");
                break;
        }






        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(main, Gravity.CENTER,0,0);

        Button k3 = (Button)popupView.findViewById(R.id.k3);
        Button k2 = (Button)popupView.findViewById(R.id.k2);
        Button k1 = (Button)popupView.findViewById(R.id.k1);
        Button k0 = (Button)popupView.findViewById(R.id.k0);


        k3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meter = 3;
                popupWindow.dismiss();
                tempor.setBackgroundColor(Color.parseColor("#008955"));
            }
        });

        k2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meter = 2;
                popupWindow.dismiss();
                tempor.setBackgroundColor(Color.parseColor("#008955"));
            }
        });

        k1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meter = 1;
                popupWindow.dismiss();
                tempor.setBackgroundColor(Color.parseColor("#008955"));
            }
        });

        k0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meter = 0;
                popupWindow.dismiss();
                tempor.setBackgroundColor(Color.parseColor("#008955"));
            }
        });


    }
}
