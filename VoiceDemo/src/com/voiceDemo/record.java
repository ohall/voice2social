package com.voiceDemo;

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
//import android.widget.ListView;
import android.widget.TextView;

public class record extends Activity {
    
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    
    //private ListView mList;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        Button speakButton = (Button) findViewById(R.id.button);
        
   

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
    
//    public void onResults(){
//    	
//    	TextView speechText = (TextView) findViewById(R.id.text);
//    	
//    	speechText = getStringArrayList(RESULT_RECOGNITION);
//    	
//    }

    /**
     * Handle the click on the start recognition button.
     */
//    public void onClick(View v) {
//        
//            
//    }

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
        	
        	TextView speechText = (TextView) findViewById(R.id.text);
        	speechText.setTextSize(75);
        	ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        	
        	//append each word match to text view
        	for(int i = 0; i<matches.size();i++){
        		speechText.append(" "+matches.get(i));
        	}
        	
        	
        	
        	
        	
        	
            // Fill the list view with the strings the recognizer thought it could have heard
//            ArrayList<String> matches = data.getStringArrayListExtra(
//                    RecognizerIntent.EXTRA_RESULTS);
//            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                    matches));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}