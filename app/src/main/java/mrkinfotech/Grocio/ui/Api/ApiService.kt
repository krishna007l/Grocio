package mrkinfotech.Grocio.ui.Api

import mrkinfotech.Grocio.ui.Datamodel.Post
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>
}