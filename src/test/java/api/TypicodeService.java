package api;

import models.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import models.User;

import java.util.List;

public interface TypicodeService {
    @GET("/users/")
    Call<List<User>> getListUsers();

    @GET("/users/{id}")
    User getUser(@Path("id") int id);

    @GET("/posts/")
    Call<List<Post>> getListPosts();

    @GET("/posts/{id}")
    Call <Post> getPost(@Path("id") int id);

    @POST("/posts/")
    Call <Post> createPost(@Body Post post);
}
