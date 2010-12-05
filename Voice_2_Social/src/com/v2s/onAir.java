package com.v2s;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class onAir extends Activity {
    
    private static final 	int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private 				Button speakButton;
    private Bundle 			recordingResultsBundle;
    private Bundle 			networkSelectedBundle;
    private String 			networkSelected;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.onair);

        speakButton = (Button) findViewById(R.id.onAirRecordButton);  
        speakButton.setTextSize(30);
        
        Bundle incomingBundle = this.getIntent().getExtras();
        networkSelected = incomingBundle.getString("DEFAULTTEXT");
        
        TextView onAirTextView = (TextView) findViewById(R.id.onAirTextView);
        onAirTextView.setTextSize(25);
        onAirTextView.append(networkSelected);
        
   

        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    // Start new activity
                	startVoiceRecognitionActivity();
                }
            });
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
    }
    

    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                		RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    	if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
        	
        	TextView speechText = (TextView) findViewById(R.id.onAirRecordedText);
        	speechText.setTextSize(20);
        	ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        	
        	//append each word match to text view
        	for(int i = 0; i<matches.size();i++){
        		speechText.setText(" "+matches.get(i));
        	}	
        	speechText.setTextSize(20);
        	
        	recordingResultsBundle = new Bundle();
        	recordingResultsBundle.putString("DEFAULTTEXT", speechText.getText().toString());
        	recordingResultsBundle.putString("NETWORKSELECTED", networkSelected);
        	
        	speakButton.setText("Review and Post Recording");
        	speakButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    // Start new activity
                	goToReviewAndSendActivity();
                }
                
            });

    	}
        super.onActivityResult(requestCode, resultCode, data);
    }
    	
    	private void goToReviewAndSendActivity() {
    		Intent i = new Intent(this , reviewAndSend.class);
    		i.putExtras(recordingResultsBundle);
    		startActivity(i);
        }
}