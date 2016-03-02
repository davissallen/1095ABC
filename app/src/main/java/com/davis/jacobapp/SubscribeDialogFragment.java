/*  Created by Davis Allen
 * 
 *  Class: ${CLASS_NAME}.java
 *  Project: JacobApp
 */


package com.davis.jacobapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseObject;

public class SubscribeDialogFragment extends DialogFragment {

    private Context context;

    public void initSubscribeDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText emailInput = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        emailInput.setLayoutParams(lp);
        emailInput.setTextColor(Color.BLACK);

        builder.setMessage("Email Address")
                .setView(emailInput)
                .setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // save email to parse
                        ParseObject email = new ParseObject("MailingListEntry");
                        email.put("email", emailInput.getText().toString().trim());
                        email.saveInBackground();

                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Your email was saved successfully!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        return;
                    }
                });

        return builder.create();

    }

}
