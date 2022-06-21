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
WaWe is an app that pulls up a restaurant in the area for the user to try. Users can filter by indicating the cusine type, price range, and distance. The app will keep track of whether or not the user has visited a restaurant to help ensure that it does not reccomend the user the same restaurant over and over again. When the user is given a recomendation, the location of the resaurant will be given, and the app will give the user directions on how to get there.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Food
- **Mobile:** This app is suitable for mobile use because users can easily get their restaurant recomendation and the directions to the location.
- **Story:** Do you hate being asked "Where do you wanna go to eat?" Or do you feel like you've been going to the same restaurants over and over again and want to try something new? Well, you're in the right place because with Restaurant Roulette this decision is made for you!
- **Market:** This app is targetted towards people who like to eat out and either have trouble deciding where to eat or want to try new restaurants 
- **Habit:** Users would use this app every time they don't know where to eat out--this depends on how often users eat out 
- **Scope:**

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [fill in your required user stories here]
* [] users are able to login/sign-up/logout from the application
* [] users are able to apply price range, cusine, dietary restriction, and location location filters before the application chooses a restaurant for the user 
* [] users are able to get directions/navigate to the chosen resturant 
* [] users are able to mark a restaurant as visited 
* [] users can add resturants to a favorites subcategory that the app will also incorporate as a filter 

**Optional Nice-to-have Stories**

* [fill in your option user stories here]
* [] social media aspect where users can recommend restaurants to their friends and can share their meals with their friends 
* [] user can shake their phone/mobile device for the roulette to start
* [] user can make reservations (if applicable) 

### 2. Screen Archetypes

* Splash Screen 
   * will be displayed for 2 seconds when the app is opened 
* Login Screen 
   * Users are able to login/sign-up/logout from the application
* Sign Up Screen
   * Users are able to login/sign-up/logout from the application
* Main Screen 
   * Users are able to add filters to the application
       * filters include: price range, cusine, dietary restriction, location,
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
       * Map Screen (may incorpate this in the detail screen)
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

## Wireframes

<img src='https://github.com/atluriml/WeWa/raw/master/CapstoneWireFrameDraft1.JPG' title='Wireframes' width='250' alt='Wireframes'/>

### [BONUS] Digital Wireframes & Mockups

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
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
- Existing API Endpoints 
    - Yelp
