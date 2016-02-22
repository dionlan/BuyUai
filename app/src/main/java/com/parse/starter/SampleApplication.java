package com.parse.starter;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by rufflez on 7/8/14.
 */
public class SampleApplication extends Application {

    public void onCreate(){
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }
}
