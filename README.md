# Dory

## The idea
Have you ever travelled to a different city, maybe even a different country and thought: "Hm, don't I know somebody who lives here?". Dory is the solution to this. It is a social network which allows you to keep track of where your friends live. The features will include:
- Show all your friends on a map
- Search for friends living close to a city
- Search for friends living close to your current position
- Add friends by scanning a code (just like in snapchat. See image below)

<p align="center">
  <img src="http://i.imgur.com/6BcplFw.jpg" width="150"/>
</p>

- Login via Facebook, Google Plus or simple Email-Address
- Simple buttons to contact friends (via Facebook chat, email, or SMS)



## The name
"Dory" is a work in progress name. The project is named after the new Pixar-movie "Finding Dory" and its precessor "Finding Nemo".

## How to build:
- Download & Install [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- Download & Install the latest version of [Android Studio](https://developer.android.com/studio/index.html)
- Download and extract the latest version of the Android SDK (in case it is not automatically downloaded with Android Studio)
- Download and extract the [Google App Engine SDK](https://cloud.google.com/appengine/downloads) (Java Version)
- Set up Android Studio to use the downloaded JDK, Android SDK and Google App Engine (GAE)
- Open the Dory-project and build
- Fail miserably and ask me (Lo) how to fix it
- Do whatever I say

## Project structure
This repository contains two separate things:
- The server backend (folder "backend")
- The Android client (folder "app")

The backend is implemented using the [Google App Engine (GAE)](https://cloud.google.com/appengine/) and the [Google Cloud Endpoints](https://cloud.google.com/appengine/docs/java/endpoints/)-architecture. This allows us to create a RESTful-Service with simple java-annotations while the client-side code is being automatically generated. What does that mean for us? The app can use simple methods in order to communicate with the server backend **AND** we don't have to care about the server OS or environment. Google takes care of that for *free* (up to a certain traffic limit). 

## Future 
- iOS - App which basically clones the behaviour of the Android app.
