package com.v2s;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class mainMenu extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        

        Button mediaSelectButton = 
        	(Button) findViewById(R.id.mediaSelectButton);
        
        Button newUserLoginButton = 
        	(Button) findViewById(R.id.newUserLoginButton);
        
        Button onAirButton = 
        	(Button) findViewById(R.id.onAirButton);
        
        Button viewFriends = 
        	(Button) findViewById(R.id.viewFriends);
        
        Button reviewAndSendButton = 
        	(Button) findViewById(R.id.reviewAndSendButton);
        
        
        	
        	
        mediaSelectButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//start review and send activity
	    		launchActivity(1);
	    	}
	    });
    	
        onAirButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//start review and send activity
	    		launchActivity(2);
	    	}
	    });
        
        newUserLoginButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//start review and send activity
	    		launchActivity(3);
	    	}
	    });
        
        reviewAndSendButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//start review and send activity
	    		launchActivity(4);
	    	}
	    });
        
        viewFriends.setOnClickListener(new OnClickListener() {
	    	public void onClick(View view){
	    		//start review and send activity
	    		launchActivity(5);
	    	}
	    });
	    
    	


        }
    

	private void launchActivity(int activity) {
	
		Intent reviewAndSendIntent = null;
	
		switch(activity){
		
			case 1:
				reviewAndSendIntent = new Intent(this , mediaSelect.class);
				break;
			case 2:
				reviewAndSendIntent = new Intent(this , onAir.class);
				break;
			case 3:
				reviewAndSendIntent = new Intent(this , newUserLogin.class);
				break;
			case 4:
				reviewAndSendIntent = new Intent(this , reviewAndSend.class);
				break;
			case 5:
				reviewAndSendIntent = new Intent(this , viewFriends.class);
				break;
				
		}
		
		startActivity(reviewAndSendIntent);
	}

	
	
}//end activity
