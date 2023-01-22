package edu.cs371m.reddit.ui


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ImageView
import androidx.core.text.clearSpans
import androidx.lifecycle.*
import edu.cs371m.reddit.api.RedditApi
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.api.RedditPostRepository
import edu.cs371m.reddit.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// XXX Much to write
class MainViewModel : ViewModel() {
    private var title = MutableLiveData<String>()
    private var searchTerm = MutableLiveData<String>()
    private var subreddit = MutableLiveData<String>().apply {
        value = "aww"
    }
    // XXX Write netPosts/searchPosts

    //TODO
    init {
        searchTerm.value = "aww"
    }

    private val redditApi = RedditApi.create()
    private val redditPostRepository = RedditPostRepository(redditApi)
    private var redditSubRedditsResponse = MutableLiveData<List<RedditPost>>()
    private var redditPostsResponse = MutableLiveData<List<RedditPost>>()
    private val subredditsFetchDone : MutableLiveData<Boolean> = MutableLiveData(false)
    private val postsFetchDone : MutableLiveData<Boolean> = MutableLiveData(false)

    private var postClicked = MutableLiveData<Boolean>()
    private var postTitle = MutableLiveData<String>()
    private var postSelfText = MutableLiveData<String>()
    private var postImageUrl = MutableLiveData<String>()
    private var postFallbackImageUrl = MutableLiveData<String>()

    private var favoritesClicked = MutableLiveData<Boolean>()
    private var favRedditPosts = mutableListOf<RedditPost>()

    private var favItemPos = -1
    private var favOnPostClicked = MutableLiveData<Boolean>()


    private var searchTermPost = MutableLiveData("")
    fun setSearchTermPost(s: String) {
        searchTermPost.value = s
    }
    private var list_favs = mutableListOf<RedditPost>()
    private var list_subreddit = mutableListOf<RedditPost>()
    private var list_posts = mutableListOf<RedditPost>()



    fun netSubredditsRefresh() = viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        val x = redditPostRepository.getSubreddits()
        list_subreddit = x as MutableList<RedditPost>
        redditSubRedditsResponse.postValue(x)
        subredditsFetchDone.postValue(true)

    }

    fun netPostsRefresh() = viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        val x = redditPostRepository.getPosts(searchTerm.value.toString())
        list_posts = x as MutableList<RedditPost>
        redditPostsResponse.postValue(x)
        postsFetchDone.postValue(true)
    }

    fun observePostsFetchDone(): LiveData<Boolean> {
        return postsFetchDone
    }

    fun observeSubredditsFetchDone(): LiveData<Boolean> {
        return subredditsFetchDone
    }

    fun observePosts(): LiveData<List<RedditPost>> {
        return redditPostsResponse
    }

    fun observeSubreddit(): LiveData<List<RedditPost>> {
        return redditSubRedditsResponse
    }

    fun netFetchImage(urlImg:String, urlThumbnail : String, imageView: ImageView) {
        Glide.fetch(urlImg, urlThumbnail, imageView)
    }

    fun netFetchImage(urlImg:String, fallbackUrl : Int, imageView: ImageView) {
        Glide.fetch(urlImg, fallbackUrl, imageView)
    }


    // Looks pointless, but if LiveData is set up properly, it will fetch posts
    // from the network
    fun repoFetch() {
        val fetch = subreddit.value
        subreddit.value = fetch
    }

    fun observeTitle(): LiveData<String> {
        return title
    }

    fun setTitle(newTitle: String) {
        title.value = newTitle
    }

    // The parsimonious among you will find that you can call this in exactly two places
    fun setTitleToSubreddit() {
        title.value = "r/${subreddit.value}"
    }

    fun observeSearchTerm() : LiveData<String> {
        return searchTerm
    }

    fun setSearchTerm (term : String) {
        searchTerm.value = term
    }

    fun getSearchTerm() : String {
        return searchTerm.value.toString()
    }

    // Convenient place to put it as it is shared
    companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
            //Log.d("view_model : ", "card clicked")
        }
    }

    fun postClickedListener(title: String, imgUrl : String, selfText : String, thumbnailUrl : String) {
        postTitle.value = title
        postSelfText.value = selfText
        postImageUrl.value = imgUrl
        postFallbackImageUrl.value = thumbnailUrl
        postClicked.value = false
    }

    fun getPostTitle() : LiveData<String> {
        return postTitle
    }

    fun getPostSelfText() : LiveData<String> {
        return postSelfText
    }

    fun getPostImgUrl() : LiveData<String> {
        return postImageUrl
    }

    fun getPostFallbackImgUrl() : LiveData<String> {
        return postFallbackImageUrl
    }

    fun observePostClicked() : LiveData<Boolean> {
        return postClicked
    }

    fun observeFavOnPostClicked() : LiveData<Boolean> {
         return favOnPostClicked
    }




    fun favClicked(post: RedditPost, pos : Int) {

        if(favRedditPosts.contains(post))
            favRedditPosts.remove(post)
        else
            favRedditPosts.add(post)

        favItemPos = pos
        favOnPostClicked.value = true
    }

    fun getFavsList() : List<RedditPost> {
        return favRedditPosts
    }

    fun getFavItemPos() : Int {
        return favItemPos
    }

    private fun filterList(list: List<RedditPost>): List<RedditPost> {
        removeAllCurrentSpans(list)
        val searchTermValue = searchTermPost.value!!

        return list.filter {
            var termFoundTitle  =  false
            var termFoundText  =  false
            var termFoundDisplayName  =  false
            var termFoundPublicDescription  =  false

            termFoundTitle = RedditPost.findAndSetSpan(it.title, searchTermValue)

            it.selfText?.let { selfText ->
                termFoundText = RedditPost.findAndSetSpan(selfText, searchTermValue)
            }

            it.displayName?.let { displayName ->
                termFoundDisplayName = RedditPost.findAndSetSpan(displayName, searchTermValue)
            }

            it.publicDescription?.let { publicDescription ->
                termFoundPublicDescription = RedditPost.findAndSetSpan(publicDescription, searchTermValue)
            }

            termFoundTitle || termFoundText || termFoundDisplayName || termFoundPublicDescription
        }

    }

    private fun removeAllCurrentSpans(livePosts: List<RedditPost>) {
        livePosts.forEach {
            it.apply {
               it.title.clearSpans()
               it.title.setSpan(
                   ForegroundColorSpan(Color.GRAY), 0, 0,
                   Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
               )
                it.selfText?.clearSpans()
                it.selfText?.setSpan(
                    ForegroundColorSpan(Color.GRAY), 0, 0,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                it.displayName?.clearSpans()
                it.displayName?.setSpan(
                    ForegroundColorSpan(Color.GRAY), 0, 0,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                it.publicDescription?.clearSpans()
                it.publicDescription?.setSpan(
                    ForegroundColorSpan(Color.GRAY), 0, 0,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        }
    }

    private var livePosts_posts = MediatorLiveData<List<RedditPost>>().apply {
        addSource(searchTermPost)  { value = filterList(list_posts)}
        addSource(redditPostsResponse) {value =
            redditPostsResponse.value?.let { it1 -> filterList(it1) }
        }
        value = list_posts
    }

    private var livePosts_favs = MediatorLiveData<List<RedditPost>>().apply {
        addSource(searchTermPost)  { value = filterList(favRedditPosts)}
        addSource(favOnPostClicked)  { value = filterList(favRedditPosts)}

        //value = list_favs
        value = favRedditPosts
    }


    private var livePosts_subreddits = MediatorLiveData<List<RedditPost>>().apply {
        addSource(searchTermPost)  { value =
            redditSubRedditsResponse.value?.let { it1 -> filterList(it1) }
        }
        addSource(redditSubRedditsResponse) { value =
            redditSubRedditsResponse.value?.let { it1 -> filterList(it1) }
        }

        value = redditSubRedditsResponse.value
    }

    fun observeLivePostsPosts(): LiveData<List<RedditPost>> {
        return livePosts_posts
    }

    fun observeLivePostsSubreddits(): LiveData<List<RedditPost>> {
        return livePosts_subreddits
    }

    fun observeLivePostsFavs(): LiveData<List<RedditPost>> {
        return livePosts_favs
    }

}

