Original App Design Project - README
===

# WaWe - Where are we eating?

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
WaWe is an app that pulls up a restaurant in the area for the user to try. Users can filter by indicating the cuisine type, price range, and distance. The app will keep track of whether or not the user has visited a restaurant to help ensure that it does not reccomend the user the same restaurant over and over again. When the user is given a recomendation, the location of the resaurant will be given, and the app will give the user directions on how to get there.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Food
- **Mobile:** This app is suitable for mobile use because users can easily get their restaurant recommendation and the directions to the location.
- **Story:** Do you hate being asked "Where do you wanna go to eat?" Or do you feel like you've been going to the same restaurants over and over again and want to try something new? Well, you're in the right place because with Restaurant Roulette this decision is made for you!
- **Market:** This app is targeted towards people who like to eat out and either have trouble deciding where to eat or want to try new restaurants 
- **Habit:** Users would use this app every time they don't know where to eat out--this depends on how often users eat out 
- **Scope:** This app will take out the decision process that comes along with trying to figure out where to eat: users will be able to use this app to try new restaurants they've never been to before

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] users are able to login/sign-up/logout from the application
* [x] users are able to apply price range, cuisine, dietary restriction, and location filters before the application chooses a restaurant for the user 
* [x] users are able to get see the route to the chosen restaurant from their location
* [x] users are able to mark a restaurant as visited and favorite them
* [x] the restaurant recommended to the user is based on their applied filters and favorited restaurants
* [x] users can view restaurants they've visited and favorited on offline mode


**Optional Nice-to-have Stories**
* [] user can shake their phone/mobile device for the roulette to start
* [] users can create groups and post in them to create meetups and connections between each other

### 2. Screen Archetypes

* Splash Screen 
   * will be displayed for 2 seconds when the app is opened 
* Login Screen 
   * Users are able to login/sign-up/logout from the application
* Sign Up Screen
   * Users are able to login/sign-up/logout from the application
* Main Screen 
   * Users are able to add filters to the application
       * filters include: price range, cuisine, dietary restriction, location,
* Detail Screen 
    * Screen with the chosen restaurant 
    * will include a map to showcase the directions and distance to the chosen restaurant
* Profile Screen
    * user's profile 
* Favorites Screen
    * list of user's favorite restaurants
* Visited Screen
    * list of restaurant's the user has visited 

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Roulette Screen
* Profile Screen

**Flow Navigation** (Screen to Screen)

* Roulette Screen
   * Detail Screen
       * Map Screen (may incorporate this in the detail screen)
* Profile Screen
   * Favorites Screen
   * Visited Screen

### 4. Required Components 
* Your app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale) 
    * double tap to add a restaurant to favorites 
* Your app incorporates at least one external library to add visual polish
    * will incorporate Glide to add images 
* Your app uses at least one animation (e.g. fade in/out, e.g. animating a view growing and shrinking)
    * a spinnable wheel for the roulette
  
### 5. Planned Problems 
* Implementing a filtering algorithm to allow users to apply price range, cuisine, dietary restriction, and location filters before the app selects a restaurant for the users to try
* Combining the YELP API and Google Maps SDK with the ParseUser Database

## Wireframes

<img src='https://github.com/atluriml/WaWe/raw/master/digital_wireframe_wawe.png' title='Digital Wireframes' width='250' alt='Wireframes'/>

### [BONUS] Interactive Prototype

## Schema 

### Models

USER Object - Parse Database 
| Property|Type  |Description |
| --------|--------| -------- |
| username|String|the user's username credentials|
| password|String|the user's password credentials|
| profileImage| File|the user's profile image |
| favorited| Boolean|whether or not the user added a restaurant to their favorites|
| visited| Boolean|whether or not the user has visited a restaurant|
| lastVisited| DateTime|the last time the user visited a restaurant|
| userLocation| String|user's current location|


| Property|Type  |Description |
| --------|--------| -------- |
| cuisine| String|type of cuisine|
| restaurantName| String|name of the restaurant|
| restaurantImage| File|image of the restaurant|
| address| String|restaurant's address|
| price-range| String|how expensive the restaurant is|
| location| String|how far away the restaurant is from the user|


### Networking
* Splash Screen
  * no network calls 
* Login Screen
  * (Read/GET) Get user information based on login information
* Sign Up Screen
  * (Create/POST) Create a new user object
* Main Screen
  * (Read/GET) restaurant based on the filters the user chose 
* Detail Screen
  * (Read/GET) location and direction of the restaurant using Google Maps 
* Profile Screen
  * (Read/GET) Query logged in user object
* Favorites Screen
  * (Read/GET) Query list of logged in user's favorite restaurants
* Visited Screen
  * (Read/GET) Query list of logged in user's visited restaurants

- Existing API Endpoints 
    - Yelp [BASE_URL: https://api.yelp.com/v3]
      - | Very|Endpoint  |Description |
        | --------|--------| -------- |
        | GET|businesses/search|search for businesses|

### Technical Problems
* Filtering: After making the API call to Yelp I will take the 50 restaurants that the API gives me. I create an algorithm that uses the user’s favorite restaurants to give the user the best possible restaurant recommendation based on their filters. 
* User and Data Persistence: I will be caching information so that if there is a network issue, the user can still see their favorite restaurants and the restaurants they have visited. This is a complex problem because I will be using SQLite and Room both of which I have not used before.

