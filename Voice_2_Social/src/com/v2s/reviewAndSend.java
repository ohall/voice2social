package com.v2s;
import com.facebook.android.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.HttpClient;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class reviewAndSend extends Activity implements TextToSpeech.OnInitListener,
TextToSpeech.OnUtteranceCompletedListener {
	
	private EditText textForReview;
    private Button speakButton;
    private Button recordAgainButton;
    private Button postRecordingButton;
    private TextToSpeech mTts; 
    private String networkSelected;
    private String username;
    private String password;
  //  private Twitter twitter;
    private SharedPreferences prefs;
	private Boolean 			voiceEnabled;
	
    public static final	String applicationId = "163165113694638";		/////////////
    private Facebook v2sfacebook;
    
    // twitter
	public static final String TWITTER_CONSUMER_KEY = "VCAyWqLcWLN4CNr9yeFEMw";
	public static final String TWITTER_CONSUMER_SECRET = "5Sj4avGxLYcNKSTw2LQjYkONeqVwA5eVRSO0uQxjBm8"; 
	 
	private static final 	int VOICE_RECOGNITION_REQUEST_CODE 	= 1234;
	private static final 	String REVIEW_AND_SEND_INSTRUCTIONS 		
	= "say, post to post. record, to record again. or say speak, to hear your post read.";
    private static final	int GOTO_TWITTER		= 1;
	private static final 	int GOTO_FACEBOOK 		= 2;
	private static final 	int GOTO_BUZZ 			= 3;
	private static final 	int GOTO_MAIN_MENU 		= 4;
	private static final 	int GOTO_MEDIA_SELECT 	= 5;
    
	HttpClient mClient;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.reviewandsend);
        
    	Bundle incomingBundle 	= this.getIntent().getExtras();
    	final String text 		= incomingBundle.getString("DEFAULTTEXT");
    	
    	networkSelected 		= incomingBundle.getString("NETWORKSELECTED");        
        textForReview 			= (EditText) findViewById(R.id.EditText01);
        textForReview.setText(text);
        prefs 					= PreferenceManager.getDefaultSharedPreferences(this);
    //    mSettings = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		voiceEnabled = prefs.getBoolean("voice_on", false);
              
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
	            if(networkSelected.compareTo("Twitter") == 0){
	            	launchActivity(GOTO_TWITTER);
	             }else if(networkSelected.compareTo("Facebook") == 0){
		            launchActivity(GOTO_FACEBOOK);
	      	
	              }else if(networkSelected.compareTo("Buzz") == 0){
		            launchActivity(GOTO_BUZZ);
	              }
	    	}
	    	});
        
        
        postRecordingButton = (Button) findViewById(R.id.button3);
        postRecordingButton.setText("Post Recording to "+networkSelected);
        postRecordingButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		
	    		postToNetwork();
		          
	    	}
	    	});
     
    }
    
    
    private void postToNetwork(){
    	
        if(networkSelected.compareTo("Twitter") == 0){
    		//if we're in Twitter, try a twitter post
        	try{ // set twitter status, catch exceptions. 
	        //      twitter.setStatus(text);
	              Toast.makeText(reviewAndSend.this,"Twitter Post Success!", Toast.LENGTH_SHORT).show();
	    		}catch(TwitterException.E401 e){
		          Toast.makeText(reviewAndSend.this,"Wrong Username or Password. Please check logins.", 
		        		  Toast.LENGTH_SHORT).show();
	    		}catch(Exception e){
	    				Toast.makeText(reviewAndSend.this, 
	    					"Network Host not responding. Check User Name and Password in Preferences Menu",
	    					Toast.LENGTH_LONG).show();
	    		}
	    		//return to main menu
	    		launchActivity(GOTO_MAIN_MENU);
	    		
         }else if(networkSelected.compareTo("Facebook") == 0){
        	 
             try {
                 String response = v2sfacebook.request("me");
                 Bundle parameters = new Bundle();
                 parameters.putString("message", textForReview.getText().toString());
                 response = v2sfacebook.request("me/feed", parameters, 
                         "POST");
          } catch(Exception e) {
              e.printStackTrace();
          }	                
          
          /*	TO DO s
           * we should add here some message that message was uploaded
           * and return to other screen
           */
        	 
         }else if(networkSelected.compareTo("Buzz") == 0){
        	 
        	 /*
        	  * PUT CODE FOR UPDATING BUZZ HERE
        	  */
        	  
         }
    	
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
	
	private void startVoiceRecognitionActivity() {

		Intent commandIntent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		commandIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        try {
        	startActivityForResult(commandIntent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
        	// say the exception!!! :-)
        	//sayit("Voice recognizer not present!");
        	//prefs.edit().putBoolean("voice_on", false);
        	//Toast.makeText(this, "No voice recognizer!", Toast.LENGTH_SHORT).show();
        	voiceEnabled = false;
        }
	}
    
 //   @Override
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.getDefault());
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				mTts.setLanguage(Locale.US);
			}
			sayit(REVIEW_AND_SEND_INSTRUCTIONS);	
		}
		
        //Deal with selected social network
        if(networkSelected.compareTo("Twitter") == 0){
    
        //Get login data for twitter account from prefs and log into twitter.
//	        username = prefs.getString("t_user", null);
	//        password = prefs.getString("t_password", null);
	        if (username != null && password != null){
//	    
/*	        	final DefaultOAuthProvider provider = new DefaultOAuthProvider(
	    	            "http://twitter.com/oauth/request_token",
	    	            "http://twitter.com/oauth/access_token",
	    	            "http://twitter.com/oauth/authorize");
*/	        	
//	        twitter = new Twitter(username, password);
//	          //Set Re-rout to identi while resolving twitter issues.
//	          twitter.setAPIRootUrl("http://identi.ca/api");
	        }else{
//	        	Builder builder = new AlertDialog.Builder(this);
//	        	builder.setTitle("Set Twitter Login Info");
//	        	builder.setMessage("Please set your Twitter username and password using the menu button");
//	        	builder.setPositiveButton("OK",null);
//	        	builder.show();
	        }
        }else if(networkSelected.compareTo("Facebook") == 0){
        	
        	/*	Here we are creating new facebook variable and getting permission to post (and read also)
        	 * 	In application there will be small facebook dialog window showing up for 2 seconds
        	 * 	but it will disappear. If this is first time user is login in it should ask for credentials         	
        	 */
        	 v2sfacebook = new Facebook(applicationId);
        	 v2sfacebook.authorize(this, new String[] {"publish_stream", "read_stream", "offline_access"},
        	                    new AuthorizeListener());
        	
        }else if( networkSelected.compareTo("Buzz") == 0){
        	
        	//TODO: Implement this network, remove toast and activity launch
        	Toast.makeText(reviewAndSend.this, 
	        		  "Buzz not yet configured", 
	        		  Toast.LENGTH_LONG).show();
        	launchActivity(GOTO_MEDIA_SELECT);	
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		int selection = 0;
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {

			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String selectCommand = "";

			selectCommand = matches.get(0);

			if (selectCommand.compareToIgnoreCase("Record") == 0) {
				
		        if(networkSelected.compareTo("Twitter") == 0){
		        	selection = GOTO_TWITTER;
		    	}else if(networkSelected.compareTo("Facebook") == 0){
		    		selection = GOTO_FACEBOOK;
		        }else if( networkSelected.compareTo("Buzz") == 0){
		        	selection = GOTO_BUZZ;
		        }
				
			} else if (selectCommand.compareToIgnoreCase("Post") == 0) {
				
				postToNetwork();
				
			} else if (selectCommand.compareToIgnoreCase("Speak") == 0) {
				
				sayit(textForReview.getText().toString());
				
			}

			super.onActivityResult(requestCode, resultCode, data);

			if (selection != 0) {
				launchActivity(selection);
			} else {
				startVoiceRecognitionActivity();
			}
		}
	}
	
	private void launchActivity(int activity) {

		Intent intent = null;
		Bundle bundle = new Bundle();

		switch (activity) {

		case GOTO_TWITTER:
			bundle.putString("DEFAULTTEXT", "Twitter");
			intent = new Intent(this, onAir.class);
			break;
		case GOTO_FACEBOOK:
			bundle.putString("DEFAULTTEXT", "Facebook");
			intent = new Intent(this, onAir.class);
			break;
		case GOTO_BUZZ:
			bundle.putString("DEFAULTTEXT", "Buzz");
			intent = new Intent(this, onAir.class);
			break;
		case GOTO_MAIN_MENU:
			intent = new Intent(this, mainMenu.class);
			break;
		case GOTO_MEDIA_SELECT:
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
	
	class AuthorizeListener implements DialogListener {
		  public void onComplete(Bundle values) {
		   //  Handle a successful login
			  //try
	            //{
//	                Bundle parameters = new Bundle();
//	                parameters.putString("message", textForReview.getText().toString());// the message to post to the wall
//	                v2sfacebook.dialog(reviewAndSend.this, "stream.publish", parameters, this);// "stream.publish" is an API call
	            //}
	            //catch (Exception e)
	           // {
	                // TODO: handle exception
	             //   System.out.println(e.getMessage());
	           // }
//////////////////////////////////////////////////////////////////////////////////	        	  
	                
	                        
	                
		  }

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}
		}
	
	
}   