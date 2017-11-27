package locationservice.ramblintreks.getcurrentlocation;

import android.app.Application;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by rudy on 11/27/17.
 */

public class App extends Application {

    private RequestQueue queue;
    @Override
    public void onCreate(){
        super.onCreate();
        queue = Volley.newRequestQueue(this);

    }

    public RequestQueue getQueue(){
        return queue;
    }
}

