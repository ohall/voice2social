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
        
//        Button onAirButton = 
//        	(Button) findViewById(R.id.onAirButton);
        
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
    	
//        onAirButton.setOnClickListener(new OnClickListener() {
//	    	public void onClick(View view){
//	    		//start review and send activity
//	    		launchActivity(2);
//	    	}
//	    });
        
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
	    
    }//end onCreate
    

	private void launchActivity(int activity) {
	
		Intent intent = null;
	
		switch(activity){
		
			case 1:
				intent = new Intent(this , mediaSelect.class);
				break;
			case 2:
				intent = new Intent(this , onAir.class);
				break;
			case 3:
				intent = new Intent(this , newUserLogin.class);
				break;
			case 4:
				Bundle b = new Bundle();
				b.putString("DEFAULTTEXT","ATTENTION DEVELOPER!  NO TEXT INPUT IF SELECTED FROM MAIN MENU!!");
				intent = new Intent(this , reviewAndSend.class);
				intent.putExtras(b);
				break;
			case 5:
				intent = new Intent(this , viewFriends.class);
				break;
				
		}//end switch
		
		startActivity(intent);
	
	}//end launchActivity

	
	
}//end activity
