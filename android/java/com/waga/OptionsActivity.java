package com.waga;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OptionsActivity extends ActionBarActivity {

    EditText etSzerokosc,etWagaSkal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        etSzerokosc =(EditText) findViewById(R.id.editText);
        etWagaSkal=(EditText) findViewById(R.id.editText2);

////------get sharedPreferences
//
//        SharedPreferences pref = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
//
////-------get a value from them
//
//        pref.getString("NAME", "Android");
//
////--------modify the value
//
//        pref.edit().putString("NAME", "Simone").commit();
//
////--------reset preferences
//
//        pref.edit().clear().commit();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREFER",Context.MODE_PRIVATE);
//        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
//        etSzerokosc.setText("TEST");
        etSzerokosc.setText(String.valueOf(sharedPref.getInt("szerokosc",15)));
        etWagaSkal.setText(String.valueOf(sharedPref.getInt("waga",500)));
//        etSzerokosc.setText(sharedPref.getInt("waga",500));
    }

    public void btnResetClick(View view) {
        int waga= Integer.valueOf(etWagaSkal.getText().toString());
        int szer= Integer.valueOf(etSzerokosc.getText().toString());

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREFER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("szerokosc", 15);
        editor.putInt("waga", 500);
        editor.commit();
    }

    public void btnZaClick(View view) {

        int waga= Integer.valueOf(etWagaSkal.getText().toString());
        int szer= Integer.valueOf(etSzerokosc.getText().toString());

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREFER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("szerokosc", szer);
        editor.putInt("waga", waga);
        editor.commit();
    }
}
