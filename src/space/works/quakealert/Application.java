package space.works.quakealert;

import android.content.Context;

import com.parse.Parse;
import com.parse.PushService;

public class Application extends android.app.Application {

  private static Application instance = new Application();
	
  public Application() {
	  instance = this;
  }
  
  public static Context getContext() {
      return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    	
        // Initialize the Parse SDK.
    	Parse.initialize(this, "9YTKUraUo9bSipOAjLkv3SvCNeC1LvLTptbxKKSj", "0ChZ1fsYFce2LdiN6JpDeAlsPhRGthULfDBfXsUG");

        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, MainActivity.class);
  }
}