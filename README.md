Cafebazaar Intents and Services
===============================

Cafebazaar Intents and Services Plugin for Cordova 3.0+ Android

This plugin in based on documentation from https://cafebazaar.ir/developers/docs/?l=en

Installation
===========

For Cordova CLI -
`cordova plugin add cordova-plugin-bazaar`

For PhoneGap Build -
Add `<gap:plugin name="cordova-plugin-bazaar" version="1.0.5" />` to config.xml

Usages (in javascript anywhere in your project)
===============================================

- Bazaar.rate("com.your.package", successCallback, errorCallback);
- Bazaar.show("com.your.package", successCallback, errorCallback);
- Bazaar.developer("yourDeveloperId", successCallback, errorCallback);
- Bazaar.update("com.your.package", function(result){ if (result > 0) {/* has update \*/} else { /* no update \*/} }, errorCallback);
