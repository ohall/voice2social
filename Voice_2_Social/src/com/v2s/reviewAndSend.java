package com.v2s;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import winterwell.jtwitter.Twitter;

public class reviewAndSend extends Activity implements TextToSpeech.OnInitListener {
	
	private EditText textForReview;
    private Button speakButton;
    private Button recordAgainButton;
    private TextToSpeech mTts; 
    Twitter twitter;
    SharedPreferences prefs;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewandsend);
        //get bundle from intent and assign it to a string;
        Bundle incomingBundle = this.getIntent().getExtras();
        String text = incomingBundle.getString("DEFAULTTEXT");
        
        textForReview = (EditText) findViewById(R.id.EditText01);

        //assign string to our textview
        textForReview.setText(text);
        
        //Initialize twitter
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String username = prefs.getString("username", "n/a");
        //String password = prefs.getString("password", "n/a");
        //if (username != null && password != null){
          twitter = new Twitter("v2s", "voice2social");
          twitter.setAPIRootUrl("http://identi.ca/api");
        //}
        
        speakButton = 
        	(Button) findViewById(R.id.button1);
        
        speakButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		sayit(textForReview.getText().toString());
	    		
	    			String status = textForReview.getText().toString();
	           // set twitter status 
	              twitter.setStatus(status);
	    	}
	    	});
        mTts = new TextToSpeech(this,this); //init text to speech
        
        recordAgainButton = 
        	(Button) findViewById(R.id.button2);
        
        recordAgainButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		
	    		//launch record for twitter.
	    		launchActivity(1);
	    		
	    	}
	    	});
        
        

    }
    
    private void sayit(String x) {
		mTts.speak(x, TextToSpeech.QUEUE_FLUSH, null);
    }
	
    
 //   @Override
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			
			int result = mTts.setLanguage(Locale.getDefault());
			
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				mTts.setLanguage(Locale.US);
			} 
		//	test=textForReview;
			sayit(textForReview.getText().toString());
			
		}
		
	}
	
	
	private void launchActivity(int activity) {

		Intent intent = null;
		Bundle bundle = new Bundle();

		switch (activity) {

		case 1:
			bundle.putString("DEFAULTTEXT", "Twitter");
			intent = new Intent(this, onAir.class);
			break;
		case 2:
			bundle.putString("DEFAULTTEXT", "Facebook");
			intent = new Intent(this, onAir.class);
			break;
		case 3:
			bundle.putString("DEFAULTTEXT", "Google Buzz");
			intent = new Intent(this, onAir.class);
			break;

		}// end switch
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
	    		
        
       
        
       
        