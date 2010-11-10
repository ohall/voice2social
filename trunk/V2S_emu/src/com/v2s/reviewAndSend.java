package com.v2s;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class reviewAndSend extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewandsend);
        
        
        //get bundle from intent and assign it to a string;
        Bundle incomingBundle = this.getIntent().getExtras();
        String text = incomingBundle.getString("DEFAULTTEXT");
        
       
        TextView textForReview = (TextView) findViewById(R.id.textForReview);

        //assign string to our textview
        textForReview.setText(text);
        
    }
}