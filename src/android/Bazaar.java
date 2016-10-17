/*
       Cafebazaar.ir helper for Cordova Projects

       Created by Mohammad Reza Maghoul (IVIR3zaM)

       Home Page: https://github.com/ivir3zam/bazaar


       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context; 
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.net.Uri;

import com.farsitel.bazaar.IUpdateCheckService;

public class Bazaar extends CordovaPlugin {
    IUpdateCheckService service;
    UpdateServiceConnection connection;

    class UpdateServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IUpdateCheckService.Stub.asInterface((IBinder) boundService);
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("update")) {
            try{
                String packageName = args.getString(0);
                connection = new UpdateServiceConnection();
                Intent intent = new Intent("com.farsitel.bazaar.service.UpdateCheckService.BIND");
                intent.setPackage("com.farsitel.bazaar");
                boolean ret = this.cordova.getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
                if (ret == false) {
                    callbackContext.error("Intent not bind");
                    return false;
                }

                if (service == null) {
                    callbackContext.error("Bazaar service destroyed");
                    return false;
                }

                long vCode = 0;
                try {
                    vCode = service.getVersionCode(packageName);
                } catch (Exception e) {
                    callbackContext.error(e.getMessage());
                    return false;
                }
                callbackContext.success(Long.toString(vCode));
                this.cordova.getActivity().unbindService(connection);
                connection = null;
                return true;
            } catch (JSONException e) {
                callbackContext.error(e.getMessage());
                return false;
            }
        }

        if (action.equals("show")) {
            try{
                String packageName = args.getString(0);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("bazaar://details?id=" + packageName));
                intent.setPackage("com.farsitel.bazaar");
                this.cordova.getActivity().startActivity(intent);
                callbackContext.success("View Intent Sent");
                return true;
            } catch (JSONException e) {
                callbackContext.error(e.getMessage());
                return false;
            }
        }

        if (action.equals("rate")) {
            try{
                String packageName = args.getString(0);
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=" + packageName));
                intent.setPackage("com.farsitel.bazaar");
                this.cordova.getActivity().startActivity(intent);
                callbackContext.success("Rate Intent Sent");
                return true;
            } catch (JSONException e) {
                callbackContext.error(e.getMessage());
                return false;
            }
        }

        if (action.equals("developer")) {
            try{
                String developerId = args.getString(0);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("bazaar://collection?slug=by_author&aid=" + developerId));
                intent.setPackage("com.farsitel.bazaar");
                this.cordova.getActivity().startActivity(intent);
                callbackContext.success("Rate Intent Sent");
                return true;
            } catch (JSONException e) {
                callbackContext.error(e.getMessage());
                return false;
            }
        }

        return false;
    }
}
