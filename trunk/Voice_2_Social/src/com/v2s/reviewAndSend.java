package com.v2s;

import java.util.Locale;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class reviewAndSend extends Activity implements TextToSpeech.OnInitListener {
	
	private EditText textForReview;
    private Button speakButton;
    private Button recordAgainButton;
    private Button postRecordingButton;
    private TextToSpeech mTts; 
    private String networkSelected;
    Twitter twitter;
    SharedPreferences prefs;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewandsend);
        

    	Bundle incomingBundle = this.getIntent().getExtras();
    	final String text = incomingBundle.getString("DEFAULTTEXT");
    	networkSelected = incomingBundle.getString("NETWORKSELECTED");        
    
        textForReview = (EditText) findViewById(R.id.EditText01);
        textForReview.setText(text);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
       
        //Deal with selected social network
        
        if(networkSelected == "Twitter"){
        //Get login data for twitter account from prefs and log into twitter.
	        String username = prefs.getString("t_user", "n/a");
	        String password = prefs.getString("t_password", "n/a");
	        if (username != null && password != null){
	          twitter = new Twitter(username, password);
	          twitter.setAPIRootUrl("http://identi.ca/api");
	        }else{
	        	Builder builder = new AlertDialog.Builder(this);
	        	builder.setTitle("Set Twitter Login Info");
	        	builder.setMessage("Please set your Twitter username and password using the menu button");
	        	builder.setPositiveButton("OK",null);
	        }
        }else if(networkSelected == "Facebook"){
        	
        	//TODO: Implement this network, remove toast and activity launch
        	Toast.makeText(reviewAndSend.this, 
	        		  "Facebook not yet configured", 
	        		  Toast.LENGTH_SHORT).show();
        	launchActivity(5);
        	
        }else if(networkSelected == "Buzz"){
        	
        	//TODO: Implement this network, remove toast and activity launch
        	Toast.makeText(reviewAndSend.this, 
	        		  "Buzz not yet configured", 
	        		  Toast.LENGTH_SHORT).show();
        	launchActivity(5);
        	
        }

        
        speakButton = (Button) findViewById(R.id.button1);
        speakButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		sayit(textForReview.getText().toString());
	    	}
	    	});
        
        mTts = new TextToSpeech(this,this); //init text to speech
        
        recordAgainButton = (Button) findViewById(R.id.button2);
        recordAgainButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//launch record for twitter.
	            if(networkSelected == "Twitter"){
	            	launchActivity(1);
	             }else if(networkSelected == "Facebook"){
		            launchActivity(2);
	      	
	              }else if(networkSelected == "Buzz"){
		            launchActivity(3);
	                	
	              }
	    	}
	    	});
        
        
        postRecordingButton = (Button) findViewById(R.id.button3);
        postRecordingButton.setText("Post Recording to "+networkSelected);
        postRecordingButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
		           // set twitter status, catch exceptions. 
	    		try{
	              twitter.setStatus(text);
	              Toast.makeText(reviewAndSend.this, 
	            		  "Twitter Post Success!", Toast.LENGTH_SHORT).show();
	    		}catch(TwitterException.E401 e){
		          Toast.makeText(reviewAndSend.this, 
		        		  "Wrong Username or Password. Please check logins.", 
		        		  Toast.LENGTH_SHORT).show();

	    		}catch(Exception e){
	    			Toast.makeText(reviewAndSend.this, 
	    					"Network Host not responding",Toast.LENGTH_SHORT).show();
	    		}
	    		//return to main menu
	    		launchActivity(4);
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
			bundle.putString("DEFAULTTEXT", "Buzz");
			intent = new Intent(this, onAir.class);
			break;
		case 4:
			intent = new Intent(this, mainMenu.class);
			break;
		case 5:
			intent = new Intent(this, mediaSelect.class);
			break;

		}// end switch
		
		intent.putExtras(bundle);
		startActivity(intent);
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
	    		
        
       
        
       
        