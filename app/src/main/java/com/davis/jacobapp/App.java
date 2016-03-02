/*  Created by Davis Allen
 * 
 *  Class: ${CLASS_NAME}.java
 *  Project: JacobApp
 */


package com.davis.jacobapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
