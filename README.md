## About ##

As part of CSC 518 OO Programming with Java course, EarthQuakeStats android app was developed for final term project.

This project demonstrates key features of Java Language 
	-	Object Oriented Programming
	-	Encapsulation
	-	Abstraction
	-	Inheritance
	-	Collection Framework
	-	Generics


## Introduction ##

EarthQuakeStats Android Application retrieves earthquake information from the free API available at https://earthquake.usgs.gov/ and displays it in user-readable formats like list view, map view and statistics view.

EarthQuakeStats uses advanced data structure (Trie) for recommending the city names where earthquake has occurred as per user-selected filters.


## How to Use? ##
  1. Clone the repository and import the project in Android Studio
  2. Get your google api key by following steps on https://developers.google.com/maps/documentation/android-api/signup
  3. Save your YOUR_API_KEY in AndroidManifest.xml under <br>
		    **meta-data <br>
				android:name="com.google.android.geo.API_KEY" <br>
				android:value="YOUR_API_KEY"** <br>
  4. Save your YOUR_API_KEY in gradle.properties as GOOGLE_MAPS_API_KEY=YOUR_API_KEY


## Summary ##

### Activities ###

EarthQuakeStats has total 6 screens (views/activities) ...

  1. **MainActivity (Latest Earthquakes)** :
      - Displays the latest quakes information as per filters defined in settings page
	  - User can also refresh the data by click on refresh button in tool bar 
	  - Depending on the magnitude of the earthquake, the earthquake information is colored
	  - User can click on individual earthquake entry to get detailed information about the earthquake
      - From this screen user can go to ... 
	      - About
		  - Settings
		  - View Earthquakes
		  - Earthquake Statistics
		  - Earthquake Details
		  - Search City

  2. **MapActivity (View Earthquakes)** :
      - Plots the earthquakes on google map as per filters defined in settings page
	  - User can also refresh the data by click on refresh button in tool bar
	  - Depending on the magnitude of the earthquake, the earthquake marker is colored
	  - User can click on individual earthquake marker to get high-level information about the earthquake
	  - User can click on individual earthquake info window to get detailed information about the earthquake
      - From this screen user can go to ... 
		  - Latest Earthquakes
		  - Settings

  3. **StatisticsActivity (Earthquake Statistics)** :
      - Displays the no. of earthquakes today, this week and this month as per filters defined in settings page
	  - User can also refresh the data by click on refresh button in tool bar 
      - From this screen user can go to ... 
		  - Latest Earthquakes
		  - Settings

  4. **DetailsActivity (Earthquake Details)** :
      - Displays the detailed information about the earthquake on Latest Earthquake screen
	  - Provides a link to USGS website for more information about the selected earthquake
      - From this screen user can only go back to Latest Earthquakes (MainActivity)

  5. **SearchActivity (Search City)** :
	  - Gives capability to search city (uses Trie for recommendations) with earthquake
	  - User can also refresh the data by click on refresh button in tool bar
	  - Depending on the magnitude of the earthquake, the earthquake marker is colored
	  - User can click on individual earthquake info window to get detailed information about the earthquake
      - From this screen user can only go back to Latest Earthquakes (MainActivity)
      - From this screen user can go to ... 
		  - Earthquake Details
		  - Settings

  6. **SettingsActivity (Settings)** :
      - Displays the following user settings
	      - EARTHQUAKE FILTERS
		      - Default Magnitude Filter
			      - 1.0+
				  - 2.0+
				  - 3.0+
				  - 4.0+
				  - 4.5+
				  - 5.0+
				  - 5.5+
				  - 6.0+
				  - 6.5+
				  - 7.0+
				  - 7.5+
              - Default Date Filter
			      - Last Hour
				  - Today
				  - Last 24 Hr
				  - This Week
		  - DATA CUSTOMIZATION
		      - Distance Units
			      - Miles
				  - Kilometers
      - From this screen user can go to the screen from where settings was selected.
	  - Settings are available on following pages ... 
	      - Latest Earthquakes
		  - View Earthquakes
		  - Earthquake Statistics
		  - Search City

  7. **AboutActivity (About Author and App)** :
      - Displays information about author and android application
      - From this screen user can only go back to Latest Earthquakes (MainActivity)


### Package Explorer ###



### Database ###

EarthQuakeStats stores the information about when the app is installed on the mobile device and when did the user last reviewed earthquake data in the app.

This information is displayed in about screen of the android app.

**NOTE**: Installed date is captured when the app is opened for the first time and not actual installation date.


### Color Codes ###

Depending on the magnitude of the earthquake, data in MainActivity and google markers in MapActivity are color coded as follows.

| Sr. No. | Magnitude | MainActivity Text Color | MapActivity Google Marker |
| ------- | --------- | ----------------------- | ------------------------- |
| 1. | 0.0 – 3.5 | #00FF00 | HUE_GREEN |
| 2. | 3.5 – 5.5 | #FF6347 | HUE_ORANGE |
| 3. | 5.5 – Above | #FF0000 | HUE_RED |


### Refresh Icon ###

All main screens i.e. MainActivity, MapActivity, StatisticsActivity and SearchActivity have refresh icon in the tool bar, which refreshes data on the screen.


### Smart Footer ###

MainActivity, StatisticsActivity and SearchActivity have smart footer, which displays the last updated time and filters set in settings.


## Screenshots ##

| Screen | ScreenShot |
| ------------- | ------------- |
| Launcher |  |
| Navigation Drawer |  |
| MainActivity |  |
| MapActivity |  |
| StatisticsActivity |  |
| DetailsActivity |  |
| SearchActivity |  |
| SettingsActivity |  |
| AboutActivity |  |


## Credits ##

This course project has been influenced from https://play.google.com/store/apps/details?id=com.joshclemm.android.quake 

It is not a replica or replacement of the above-mentioned android app but it is just an attempt by a student to learn java and android programming.


## DISCLAIMER ##

This is just a course project and source of data is the free APIs https://earthquake.usgs.gov/.

Please do not use this for reference for earthquake alert or analysis, instead directly go to https://earthquake.usgs.gov/

