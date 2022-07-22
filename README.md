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
As a foodie who often experiences decision fatigue when it comes to figuring out where I want to eat, I created this app so that the decision would be made for me. Users are then pushed to try new restaurants in their area. In WaWe, users can filter what type of restaurant they want to eat at by indicating the cuisine type, price range, distance, and whether or not they want to try a new place. Based on the filters, WaWe pulls up a restaurant in the area for the user to go to. Users can mark restaurants as visited and/or favorite them and view them off and online. This allows the WaWe algorithm to give users the best recommendation possible based on their favorited restaurants. Users can also create groups and post in them to facilitate meetups. 

### App Evaluation
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
* [x] users are able to see their location and the restaurant's location on the Map Screen 
* [x] users are able to mark a restaurant as visited and favorite them
* [x] the restaurant recommended to the user is based on their applied filters and favorited restaurants
* [x] users can view restaurants they've visited and favorited on offline mode


**Stretch Features**
* [x] users are able to see the route from their location to the restaurant's location on the Map Screen 
* [x] user can shake their phone/mobile device for the roulette to start
* [x] users can create groups or search for an already existing group and post in them to create meetups, allowing connections between each other
* [x] incorporate data persistence into the groups feature 

### 2. Screen Archetypes

* Splash Screen 
   * will be displayed when the app is opened 
* Login Screen 
   * Users are able to login/sign-up from the application
* Sign Up Screen
   * Users are able to sign-up from the application
* Roulette Screen 
   * Users are able to add filters to the application
       * filters include: price range, cuisine, dietary restriction, location,
* Restaurant Detail Screen 
    * Screen with the chosen restaurant 
* Map Screen 
    * will include a map to showcase the route from the user's location to the chosen restaurant
* Profile Screen
    * user's profile 
* Favorites Screen
    * list of user's favorite restaurants
* Visited Screen
    * list of restaurant's the user has visited 
* Settings Screen
    * user can update their profile 
* Groups Screen
    * user can view already existing groups or create a new group
* Group Detail Screen
    * user can see and like posts and/or create a post in the group


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Roulette Screen
* Profile Screen
* Groups Screen

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Roulette Screen
* Signup Screen
   * Login Screen
       * Roulette Screen 
* Roulette Screen
   * Detail Screen
       * Map Screen 
* Profile Screen
   * Settings Screen
   * Favorites Screen
       * Detail Screen
              * Map Screen
   * Visited Screen
       * Detail Screen
              * Map Screen
* Groups Screen
   * Group Detail Screen

### 4. Required Components 
* Your app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale) 
    * double tap to add a restaurant to favorites 
* Your app incorporates at least one external library to add visual polish
    * Glide to add images 
    * Lottie for animations 
* Your app uses at least one animation (e.g. fade in/out, e.g. animating a view growing and shrinking)
    * Loading animation
    * Animation when user likes a restaurant 
  
### Technical Problems
* Filtering: After making the API call to Yelp I will take the 50 restaurants that the API gives me. I create an algorithm that uses the user’s favorite restaurants to give the user the best possible restaurant recommendation based on their filters. This is a complex problem because the algorithm tailors itself to each user.
* User and Data Persistence: I will be caching information so that if there is a network issue, the user can still see their favorite restaurants, restaurants they have visited, already existing groups, and posts that have been posted in each group. This is a complex problem because I used SQLite and Room both of which I have not used before.

## Wireframes

<img src='https://github.com/atluriml/WaWe/raw/master/digital_wireframe_wawe.png' title='Digital Wireframes' width='250' alt='Wireframes'/>

## Schema 

### Models

#### Parse Database Objects

##### 1. User:
| Property|Type  |Description |
| --------|--------| -------- |
| username|String|user's username|
| password|String|user's password|
| location|String|users's image|
| profileImage|File|user's profile image|
| dietaryRestriction|String|user's dietary restriction|

##### 2. Restaurant:
| Property|Type  |Description |
| --------|--------| -------- |
| name|String|restaurant's name|
| price|String|restaurant price|
| image|String|restaurant's image|
| restID|String|restaurant's unique yelpId|
| address|String|restaurant's street address|
| rating|Double|rating of the restaurant|
| categories|Array|contains all of the categories the restaurant falls under|

##### 3. UserFavorites:
| Property|Type  |Description |
| --------|--------| -------- |
| user|ParseUser|user that favorited a restaurant|
| favoritedRestaurant|Restaurant|restaurant that the user favorited|

##### 4. UserVisited:
| Property|Type  |Description |
| --------|--------| -------- |
| user|ParseUser|user that marked a restaurant as visited|
| visitedRestaurant|Restaurant|restaurant that the user marked as visited|

##### 5. Groups:
| Property|Type  |Description |
| --------|--------| -------- |
| name|String|group's name|
| description|String|group's description|
| location|String|group's location|

##### 6. Post:
| Property|Type  |Description |
| --------|--------| -------- |
| title|String|post's title|
| description|String|post's description|
| image|File|post's image|
| groupId|String|group's Parse ObjectId that the post was created in|
| user|ParseUser|ParseUser that created the post|
| likedUsers|Array|indicates which users have liked the post|

#### Yelp Database Objects

##### 1. YelpRestaurant:
| Property|Type  |Description |
| --------|--------| -------- |
| name| String| restaurant's name|
| price| String| restaurant's price|
| rating| double| restaurant's rating|
| distanceMeters| double|restaurant's distance from user in meters|
| restaurantImage| String|restaurant's image|
| category| RestaurantCategories List|contains list of restaurant's categories|
| location| RestaurantLocation|restaurant's location|
| id| String| restaurant's unique yelpId|
| coordinates| RestaurantCoordinates|restaurants latitude and longitude coordinates|  

##### 2. RestaurantCategories:
| Property|Type  |Description |
| --------|--------| -------- |
| title|String|category title|

##### 3. RestaurantCoordinates:
| Property|Type  |Description |
| --------|--------| -------- |
| longitude|double|restaurant's longitude|
| latidude|double|restaurant's latidude|

##### 4. RestaurantLocation:
| Property|Type  |Description |
| --------|--------| -------- |
| address|String|restaurant's street address|

##### 5. RestaurantSearch:
| Property|Type  |Description |
| --------|--------| -------- |
| restaurants|YelpRestaurant List|contains list of restaurants from business search|  

#### Room Database Objects

##### 1. UserRoom:
| Property|Type  |Description |
| --------|--------| -------- |
| userId|String|primary key that holds the user's unique Parse ObjectId|

##### 2. RestaurantRoom:
| Property|Type  |Description |
| --------|--------| -------- |
| yelpId|String|primary key that holds restaurant's unique yelpId|
| name|String|restaurant's name|
| price|String|restaurant price|
| image|String|restaurant's image|
| address|String|restaurant's street address|
| rating|Double|rating of the restaurant|

##### 3. UserFavoritesRoom:
| Property|Type  |Description |
| --------|--------| -------- |
| id|String|primary key that holds the userFavorites's unique Parse ObjectId|
| user|UserRoon|user that favorited a restaurant|
| restaurant|RestaurantRoom|restaurant that the user favorited|
| userId|String|foreign key that holds the user's unique Parse ObjectId|
| restaurantId|String|foreign key that holds restaurant's unique yelpId|

##### 4. UserVisitedRoom:
| Property|Type  |Description |
| --------|--------| -------- |
| id|String|primary key that holds the userVisited's unique Parse ObjectId|
| user|UserRoon|user that marked a restaurant as visited|
| restaurant|RestaurantRoom|restaurant that the user marked as visited|
| userId|String|foreign key that holds the user's unique Parse ObjectId|
| restaurantId|String|foreign key that holds restaurant's unique yelpId|

##### 5. Groups:
| Property|Type  |Description |
| --------|--------| -------- |
| groupId|String|primary key that holds the group's unique Parse ObjectId|
| groupName|String|group's name|
| groupDescription|String|group's description|
| groupLocation|String|group's location|

##### 6. Post:
| Property|Type  |Description |
| --------|--------| -------- |
| postId|String|primary key that holds the post's unique Parse ObjectId|
| postTitle|String|post's title|
| postDescription|String|post's description|
| userProfileImage|String|user's profile image|
| postImage|String|post's image|
| groupId|String|group's Parse ObjectId that the post was created in|
| userName|String|username of the User that created the post|

### Networking
  
* Splash Screen
  * no network calls 
* Login Screen
  * (Read/GET) Get user information based on login information
* Sign Up Screen
  * (Create/POST) Create a new user object
* Roulette Screen
  * (Read/GET) restaurant based on the filters the user chose 
  * (Read/GET) Query list of logged in user's favorite restaurants for matching algorithm 
  * (Read/GET) Query list of logged in user's visited restaurants for matching algorithm 
* Detail Screen
  * (Read/GET) Restaurant's specific details using businesses/{id}
* Map Screen 
  * (Read/GET) route from user's location to the restaurant using Google Maps 
* Profile Screen
  * (Read/GET) Query logged in user object
* Settings Screen
  * (Create/POST) User's updated profile settings
* Favorites Screen
  * (Read/GET) Query list of logged in user's favorite restaurants
* Visited Screen
  * (Read/GET) Query list of logged in user's visited restaurants
* Groups Screen
  * (Read/GET) Query list of existing groups
  * (Create/POST) User can create new groups
* Group Detail Screen
  * (Read/GET) Specifc group that user clicked on
  * (Read/GET) Query list of existing posts for the group
  * (Create/POST) User can create new posts

- Existing API Endpoints 
    - Yelp [BASE_URL: https://api.yelp.com/v3]
      - | Network Request|Endpoint  |Description |
        | --------|--------| -------- |
        | GET|businesses/search|search for businesses|
        | GET|businesses/{id}|search for business based on the unique id|

