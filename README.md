# Reddit Clone
This Reddit Clone Android App allows you to search for topics of your interest, view related threads, read posts in details, favorite posts you like. 

## Features
* Search for topics of interest and view related threads, posts
* Like/ add to favorites list interesting posts to read them later
* Read post in detail

## Tech Stack
* MVVM architecture used for separation of concerns and efficient data flow
* Jetpack Compose UI Toolkit for building UI components
* Retrofit2 for networking
* Glide for loading different image formats and image caching
* Kotlin Coroutines for handling async tasks
* ListAdapter for displaying sub-reddits and reddit posts
* Room database for data persistence

## External dependencies
* Reddit APIs 

## App Functionality
* App loads with "r/aww" Reddit thread as deafult thread. It has a search bar where user can type, app will perform text filtering over post heading and post content to filter out posts containg search words. Each post will be displayed with a post heading, post text (if any), post image(if any), a bottom status tile with options to - to star a post, comment on the post, add/ remove a post to/ from favorites list. Users can scroll through the posts in the "r/aww" thread, the app in the background will be fetching more and more posts as user scrolls down. App makes use of Reddit APIs to interact with Reddit database. 
<p align="center">
  <img width="300" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943328-8fa32c30-31b1-45ad-b5eb-46049b589ad3.png">
</p>

* The user can switch to a thread, when in the Pick fragment the user can scroll through all Reddit threads to pick a thread or type a thread type he would be interested in. App will filter threads with search keyword and display filtered ones. In the second image user is looking for news threads.
<p align="center">
  <img width="200" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943377-b5060296-9c21-4d6a-89f2-8d75d2a3cec6.png">      <space>   </space>       
  <img width="200" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943385-88930c17-f243-4b31-a3f4-42db5c40d495.png">
</p>

* In a thread say "r/soccer" user can scroll through all posts or look for posts of a kind by making use of search. In the image the user is looking for "news" posts in the "r/soccer" thread.
<p align="center">
  <img width="200" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943389-97e6b552-2d75-4ed2-8fbe-cdfba88dcd8a.png">
  <img width="200" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943393-435ddfde-a643-481d-ba19-b30c5281e54a.png">
</p>

* User can favourite posts to save/review them later. All the favourited posts can be accessed by clicking on favourite icon in action bar. In the first image user has favorited the first three posts in "r/soccer" thread. In the second image usef is accessing his favourited posts in the favourites tab.
<p align="center">
  <img width="200" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943404-099a3e61-c6be-47b0-88de-a3f3c7e63d50.png">
  <img width="200" height="380" alt="image" src="https://user-images.githubusercontent.com/98439391/213943405-7984c059-7fbf-409c-9449-76823adcf7c1.png">
 </p>

