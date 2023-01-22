package edu.cs371m.reddit.api

import android.text.SpannableString
import android.util.Log
import com.google.gson.GsonBuilder
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.api.RedditPost

class RedditPostRepository(private val redditApi: RedditApi) {
    // NB: This is for our testing.
    val gson = GsonBuilder().registerTypeAdapter(
            SpannableString::class.java, RedditApi.SpannableDeserializer()
        ).create()

    private fun unpackPosts(response: RedditApi.ListingResponse): List<RedditPost> {
        // XXX Write me.

        //subreddits --> post data object
         var redditposts = mutableListOf<RedditPost>()
        response.data.children.map {
            redditposts.add(it.data)
        }

        return redditposts
    }


    suspend fun getPosts(subreddit: String): List<RedditPost> {
        if (MainActivity.globalDebug)
        {
            val response = gson.fromJson(
                MainActivity.jsonAww100,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        }
        else
        {
            // XXX Write me.
            return unpackPosts(redditApi.getPosts(subreddit))
        }
    }

    suspend fun getSubreddits(): List<RedditPost> {
        if (MainActivity.globalDebug)
        {
            val response = gson.fromJson(
                MainActivity.subreddit1,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        }
        else
        {
            // XXX Write me.
            return unpackPosts(redditApi.getSubreddits())
        }
    }
}
