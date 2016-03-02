/*  Created by Davis Allen
 * 
 *  Class: ${CLASS_NAME}.java
 *  Project: JacobApp
 */


package com.davis.jacobapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

public class FragmentSurvey extends Fragment {

    Activity activity;

    public void initFragmentSurvey(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.layout_survey, container, false);
        fragmentView.setClickable(true);

        Spinner spinnerImportant = (Spinner) fragmentView.findViewById(R.id.spinner_important);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterImportant = ArrayAdapter.createFromResource(
                activity.getApplicationContext(), R.array.string_array_important,
                R.layout.spinner_item_dark);
        // Specify the layout to use when the list of choices appears
        adapterImportant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerImportant.setAdapter(adapterImportant);

        Spinner spinnerGeography = (Spinner) fragmentView.findViewById(R.id.spinner_geography);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterGeography = ArrayAdapter.createFromResource(
                activity.getApplicationContext(), R.array.string_array_geography,
                R.layout.spinner_item_dark);
        // Specify the layout to use when the list of choices appears
        adapterGeography.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGeography.setAdapter(adapterGeography);

        Spinner spinnerGender = (Spinner) fragmentView.findViewById(R.id.spinner_gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(
                activity.getApplicationContext(), R.array.string_array_gender,
                R.layout.spinner_item_dark);
        // Specify the layout to use when the list of choices appears
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapterGender);

        Spinner spinnerAge = (Spinner) fragmentView.findViewById(R.id.spinner_age);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterAge = ArrayAdapter.createFromResource(
                activity.getApplicationContext(), R.array.string_array_age,
                R.layout.spinner_item_dark);
        // Specify the layout to use when the list of choices appears
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerAge.setAdapter(adapterAge);

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button buttonSubmit = (Button) activity.findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner answerQ1 = (Spinner) activity.findViewById(R.id.spinner_important);
                String a1 = answerQ1.getSelectedItem().toString();
                Spinner answerQ2 = (Spinner) activity.findViewById(R.id.spinner_geography);
                String a2 = answerQ2.getSelectedItem().toString();
                Spinner answerQ3 = (Spinner) activity.findViewById(R.id.spinner_gender);
                boolean a3 = false;
                if (answerQ3.getSelectedItem().toString().equalsIgnoreCase("male")) { a3 = true; }
                Spinner answerQ4 = (Spinner) activity.findViewById(R.id.spinner_age);
                String a4 = answerQ4.getSelectedItem().toString();

                // send data to parse
                ParseObject surveyResult = new ParseObject("SurveyAnswer");
                surveyResult.put("formChosen", a1);
                surveyResult.put("geoRegion", a2);
                surveyResult.put("isMale", a3);
                surveyResult.put("ageBand", a4);
                surveyResult.saveInBackground();

                Context context = activity.getApplicationContext();
                CharSequence text = "Thank you for your survey response!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Button surveyButton = (Button) activity.findViewById(R.id.button_survey);
                surveyButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = activity.getApplicationContext();
                        CharSequence text = "We've already got your survey submission. " +
                                "If you'd like to further comment please check the app in the Play Store to leave a review!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });

                activity.onBackPressed();
            }
        });

        Button buttonCancel = (Button) activity.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
