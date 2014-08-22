package space.works.quakealert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.parse.Parse;
import com.parse.PushService;


public class MainActivity extends ActionBarActivity {

	private WebView myWebView;
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    myWebView = (WebView) findViewById(R.id.webView1);
	    myWebView.loadUrl("http://spacedotworks.com/earthquake/mobile.php");
	    // Normal case behavior follows
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		if (isNetworkAvailable()){}
        //do whatever you want to do 
    else
        {
        try {
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("Sorry!");
    alertDialog.setMessage("Network connectivity is not available. This app requires an internet connection to work.");
    //alertDialog.setIcon(R.drawable.alerticon);
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int which) {
         finish();

       }
    });

    alertDialog.show();
    }
    catch(Exception e)
    {
        //Log.d(Constants.TAG, "Show Dialog: "+e.getMessage());
    }
        };
		
		setContentView(R.layout.main);
		
		
		
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());
	    
		Parse.initialize(this, "9YTKUraUo9bSipOAjLkv3SvCNeC1LvLTptbxKKSj", "0ChZ1fsYFce2LdiN6JpDeAlsPhRGthULfDBfXsUG");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		
		SharedPreferences mPrefs = getSharedPreferences("quake", 0);
 	   	String mString = mPrefs.getString("push", "0");
 	   	if (mString == "0"){
 	   	PushService.subscribe(MainActivity.this,"push" , MainActivity.class);
 	   	}
 	   	
		myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.setWebViewClient(new WebViewClient(){
			  public void onPageFinished(WebView view, String url) {
			        if (url.contains("/jp/quake")){
				  	String newurl = url.replace("/jp/","/en/");
			        //System.out.println(url);
			        view.loadUrl(newurl);
			        }
			        if (url.contains("/en/quake")){
				        float scale = 600f/view.getWidth();			        
				        String scalestr = Float.toString(scale);			       
				        view.loadUrl(String.format("javascript:document.getElementById('sidebar').style.display = 'None';" +
				        		"document.getElementById('top').style.display = 'None';" +
				        		"document.getElementById('foot').style.display = 'None';" +
				        		"document.getElementById('explain').style.display = 'None';" +
				        		"document.getElementById('goTop').style.display = 'None';" +
				        		"document.getElementById('guide').style.display = 'None';" + 
				        		"document.getElementsByTagName('body')[0].style.width='600px';" +
				        		"document.getElementsByTagName('body')[0].style.margin='0';" +
				        		"document.getElementById('base').style.minWidth='600px';" +
				        		"document.getElementById('base').style.width='600px';" +
				        		"document.documentElement.style.zoom = %s;"
				        		, scalestr));	
				        
				        }
			        if (url.contains("transrain")){
		        	//System.out.println(scale);
			        view.loadUrl("javascript:document.getElementsByClassName('navbar')[0].style.display = 'None';" +
			        		"document.getElementsByClassName('adsbygoogle earthquake-respons')[0].style.display = 'None';" 
			        		
			        		);	
			        	System.out.println("no");
			        }
			        int scale=view.getWidth()*100/600;
			        view.setInitialScale(scale);
			    }
		});
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myWebView.getSettings().setLoadWithOverviewMode(true);
	    myWebView.getSettings().setUseWideViewPort(true);
	    myWebView.getSettings().setBuiltInZoomControls(true);
	    if (Build.VERSION.SDK_INT > 10) {
	    myWebView.getSettings().setDisplayZoomControls(false);
	    }
	  
		myWebView.loadUrl("http://spacedotworks.com/earthquake/mobile.php");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.options:
	        	CharSequence colors[] = new CharSequence[] {"Enabled", "Disabled"};
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	
	        	SharedPreferences mPrefs = getSharedPreferences("quake", 0);
         	   	String mString = mPrefs.getString("push", "0");
                
         	   builder.setIcon(R.drawable.example);
         	  builder.setInverseBackgroundForced(true);
	        	builder.setTitle("Enable Push Notification?");
	        	builder.setSingleChoiceItems(colors, Integer.parseInt(mString), new DialogInterface.OnClickListener() {
	        	    @Override
	        	    public void onClick(DialogInterface dialog, int which) {
	        	    	   
	        	    	SharedPreferences sharedPref= getSharedPreferences("quake", 0);
                 	   SharedPreferences.Editor editor= sharedPref.edit();
	        	    		switch(which)
	                       {
	        	    	   	
	                           case 0:                     	   
	                        	   editor.putString("push", "0");
	                        	   PushService.subscribe(MainActivity.this,"push" , MainActivity.class);
	                        	        break;
	                           case 1:	                        	  
	                        	   editor.putString("push", "1");
	                        	   PushService.unsubscribe(MainActivity.this,"push");
	                                   break;
	                           
	                       }
	        	    		editor.commit();
	                       dialog.dismiss();    
	        	    }
	        	});
	        	builder.show();
	            return true;
	        case R.id.help:
	        	ImageView image = new ImageView(this);
	            image.setImageResource(R.drawable.help);
	        	AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
	        	//builder2.setMessage("Message above the image");
	        	builder2.setPositiveButton("OK",new DialogInterface.OnClickListener()
	            {

	                @Override
	                public void onClick(DialogInterface dialog, int which) 
	                {
	                    dialog.dismiss();
	                }
	            });
	        	builder2.setView(image);
	        	builder2.create().show();
	            return true;
	            
	        case R.id.refresh:
	        	myWebView = (WebView) findViewById(R.id.webView1);
	    	    myWebView.loadUrl("http://spacedotworks.com/earthquake/mobile.php");
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
	    	Intent intent = getIntent();
	    	finish();
	    	startActivity(intent);
	    	//myWebView.loadUrl("http://spacedotworks.com/earthquake/mobile.php");
	    	//myWebView.setInitialScale(100);
	        return true;
	    }
	    else
	    {
	       // finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
