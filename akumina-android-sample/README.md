# Introduction 
Mobile app for Akumina Branch

# Branch Purpose 
This branch include application code for Android Akumina private App, this branch include code having MAM and MSAL
 
# Last release Version 
4.6.6

# Customer Name
Akumina Private App

# App manager URL - https://mainapp.akumina.dev/api/v2.0/token/preauth

# Graph Scope - {"https://graph.microsoft.com/.default"}

# Sharepoint scope - https://akuminadev.sharepoint.com/.default

# App Url - https://mysite.akumina.dev/?preauth=

# Authority Url - https://login.microsoftonline.com/15d05f6e-046b-4ed5-9ab8-4b6c25f719b5

# Application ID - com.mobile.akumina.sample.test

# Getting Started
1.	Installation process 
    # latest node version
    # Java 11/8
    # Android Studio
    # Android SDK
     
# Build and Test

# Creating new feature Branch 

Create a new branch from main and then follow the below steps to create the feature branch specific to customer.

# Rename the Android application 

App Name - Change the App name for Android in project => android => app => src => main => res => values => change value of property  "app_name" with new value in strings.xml file.

App ID - Change the app ID in file - android => app => build.gradle => change the value for 'applicationId' in android.defaultConfig.applicationId

VersionName - Update the version name to incremental - #file - android => app => build.gradle
VersionCode - Incremental - #file - android => app => build.gradle

# Build and Test

**Android Pipeline**

https://akuminadev.visualstudio.com/Akumina/_git/MobileDev?path=/azure-pipeline-apk.yaml&version=GBakumina&_a=contents

**===================================================**

# Configuration of Push Notification 

# Refer the firebase project
https://console.firebase.google.com/u/0/project/akumina-android/overview

# Firebase App
https://console.firebase.google.com/u/0/project/akumina-android/settings/general/android:com.mobile.akumina.sample.test

First sign in to https://console.firebase.google.com/ 
On console application click on add project
Enter the project name and click on continue 
Click continue on 'Google Analytics for your Firebase project'
Select account for Google analytics on Configure Google Analytics screen and click on continue
New project will be created will take 30 seconds of time.
After building the project check the screen 'Add an app to get started' on 'Get started by adding Firebase to your app' screen, then select the android for android application.

Please refer the below section for Android

# Android 

On Get started by adding Firebase to your app
Click on add android application on 'Get started by adding Firebase to your app' screen
On 'Add Firebase to your Android app' screen now fill all infomation
In Register app screen fill the app ID in Android package name
Add - App nickname (optional)
Add - Debug signing certificate SHA-1 (optional)
Click on 'Register app'
In next step download the file - google-services.json
Switch to the Project view in Android Studio to see your project root directory.
Move your downloaded google-services.json file into your module (app-level) root directory.
Now click on Next

On 'Add Firebase SDK' screen, see the details which needs to configure in the android application
To make the google-services.json config values accessible to Firebase SDKs, you need the Google services Gradle plugin.
dd the plugin as a buildscript dependency to your project-level build.gradle file:

# Root-level (project-level) Gradle file (<project>/build.gradle):

# Under buildscript.repositories add google() and mavenCentral()

# Under dependencies add 'classpath 'com.google.gms:google-services:4.3.13''

buildscript {
  repositories {
    // Make sure that you have the following two repositories
    google()  // Google's Maven repository // this should need to be added 
    mavenCentral()  // Maven Central repository // this should need to be added 
  }
  dependencies {
    // Add the dependency for the Google services Gradle plugin
    classpath 'com.google.gms:google-services:4.3.13' // this should need to be added 
  }
}

# Under allprojects.repositories add google() and mavenCentral()

allprojects {
  repositories {
    // Make sure that you have the following two repositories
    google()  // Google's Maven repository // this should need to be added 
    mavenCentral()  // Maven Central repository // this should need to be added 
  }
}

Now to the next step

# Module (app-level) Gradle file (<project>/<app-module>/build.gradle):

plugins {
  id 'com.android.application'

  // Add the Google services Gradle plugin
  id 'com.google.gms.google-services' // this should need to be added 
}

or 

apply plugin: "com.android.application" // this should be alredy there just add after this line
# apply plugin: "com.google.gms.google-services" // this should need to be added 

Then in dependencies section for the same file 

dependencies {
  // Import the Firebase BoM
  implementation platform('com.google.firebase:firebase-bom:31.1.1')


  // TODO: Add the dependencies for Firebase products you want to use
  // When using the BoM, don't specify versions in Firebase dependencies
  implementation 'com.google.firebase:firebase-analytics'
}

Now Click on next in console app
Finally Click on continue to console
Now in Console app select the generated android app and see the details under 'Cloud messaging'

# You all set for Push notification.

# Note - When you create app under the same project for push notification in console the file  google-services.json keep on updating (no need to change the file) this file gets override with new app every time we created new project (No need to update) please make sure to have same app id given in the project while creating the app over console.

# Generate Keystore run the below command, fill the other information

Company name, State, City country code etc and use the generated keystore.

keytool -genkey -v -keystore KEYSTORE_NAME.keystore -alias ALIAS -keyalg RSA -keysize 2048 -validity 10000

# Generate the base64 String

keytool -exportcert -alias ALIAS -keystore KEYSTORE_NAME.keystore | openssl sha1 -binary | openssl base64