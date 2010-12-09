package com.v2s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class onAir extends Activity implements TextToSpeech.OnInitListener,
TextToSpeech.OnUtteranceCompletedListener {
    
    private static final 	int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private static final 	String ON_AIR_INSTRUCTIONS 		= "Record your post now";
    private 				Button speakButton;
    private Bundle 			recordingResultsBundle;
    private Bundle 			networkSelectedBundle;
    private String 			networkSelected;
	private	SharedPreferences 	prefs;
	private Boolean 			voiceEnabled;
	private TextToSpeech 	mTts;

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
        
		prefs=PreferenceManager.getDefaultSharedPreferences(this);
		voiceEnabled = prefs.getBoolean("voice_on", false);
   

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
        
		/**
		 * Text to Speech for Instructions
		 */
		mTts = new TextToSpeech(this, this); // init text to speech
        
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
        	
        	//if we're in voice mode go straight to review and send
        	if(voiceEnabled){
        		goToReviewAndSendActivity();
        		
        	}
        	
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
    	
    	/* Handlers for the Menu button */
    	@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		new MenuInflater(getApplication()).inflate(R.menu.option, menu);

    		return(super.onCreateOptionsMenu(menu));
    	}
    	
    	@Override
    	public boolean onOptionsItemSelected(MenuItem item) {
    		if (item.getItemId()==R.id.prefs) {
    			//sayit("do the prefs now!");
    			startActivity(new Intent(this, prefsActivity.class));
    			
    			return(true);
    		}
    		return(super.onOptionsItemSelected(item));
    	}
    	
    	private void sayit(String x) {

    		if(voiceEnabled){
    			HashMap<String, String> myHashAlarm = new HashMap();
    			myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
    			        String.valueOf(AudioManager.STREAM_ALARM));		
    			
    			mTts.setOnUtteranceCompletedListener(this);
    			myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
    			        String.valueOf(AudioManager.STREAM_ALARM));
    			myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
    			        "end of voice intructions");
    			// myHashAlarm now contains two optional parameters
    			mTts.speak(x, TextToSpeech.QUEUE_ADD, myHashAlarm);
    		}
    	}
    	
    	public void onUtteranceCompleted(String uttId) {
    			startVoiceRecognitionActivity();
    	}
    	
    	@Override
    	public void onInit(int status) {

    		if (status == TextToSpeech.SUCCESS) {
    			int result = mTts.setLanguage(Locale.getDefault());
    			if (result == TextToSpeech.LANG_MISSING_DATA
    					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
    				mTts.setLanguage(Locale.US);
    			}
    			sayit(ON_AIR_INSTRUCTIONS);
    		}
    	}
    	
    	@Override
    	public void onDestroy() {
    		// kill it!
    		if (mTts != null) {
    			mTts.stop();
    			mTts.shutdown();
    		}
    		super.onDestroy();
    	}
    	
}