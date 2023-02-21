package tests.steps;

import api.TypicodeService;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import models.Post;
import models.User;
import org.hamcrest.Matchers;
import org.testng.Assert;
import retrofit2.Response;
import retrofit2.RetrofitBuilder;
import retrofit2.RetrofitClientFactory;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static rest_assured.RestClient.getBaseSpec;

public class StepsApiTests {
    private static final String POSTS_PATCH = "/posts/";
    private static final String USERS_PATCH = "/users/";
    private TypicodeService service = RetrofitBuilder.build().create(TypicodeService.class);

    @Step("Get list all posts, check status code 200 and response body in json format")
    public List<Post> getAllPostsRA(){
        return  given()
                    .spec(getBaseSpec())
                .when()
                    .get(POSTS_PATCH)
                .then()
                    .assertThat()
                    .statusCode(SC_OK)
                    .contentType(ContentType.JSON)
                    .extract().body().jsonPath().getList("", Post.class);
    }

    @Step("Check list of posts sorting by id")
    public void checkSortingListPostsByIdRA(List<Post> listPosts){
        List<Integer> ids = listPosts.stream().map(Post::getId).collect(Collectors.toList());
        Assert.assertEquals(ids, ids.stream().sorted().collect(Collectors.toList()), "List posts is not sorted by id");
    }

    @Step("Get post and check status code 200")
    public Post getPostRA(int responseId){
        return  given()
                    .spec(getBaseSpec())
                .when()
                .   get(POSTS_PATCH + responseId)
                .then()
                    .assertThat()
                    .statusCode(SC_OK)
                    .extract().body().jsonPath().getObject("", Post.class);
    }

    @Step("Check information in the posts/{0}, should be status code 200, title and body are not empty")
    public void checkInformationInPost(Post post, int expectedId, int expectedUserId){
        Assert.assertEquals(post.getUserId(), expectedUserId);
        Assert.assertEquals(post.getId(), expectedId);
        Assert.assertNotNull(post.getTitle());
        Assert.assertNotNull(post.getBody());
    }

    @Step("Сheck posts/{0} should be empty and status code 404")
    public void checkNonExistentPostRA(int responseId){
                given()
                        .spec(getBaseSpec())
                .when()
                        .get(POSTS_PATCH + responseId)
                .then()
                        .assertThat()
                        .statusCode(SC_NOT_FOUND)
                        .body("isEmpty()", Matchers.is(true));
    }

    @Step("Create and get new post with userid={0}, should be status code 201")
    public Post createNewPostWithUserIdRA(Post post) {
        return  given()
                        .spec(getBaseSpec())
                        .body(post)
                .when()
                        .post(POSTS_PATCH)
                .then()
                        .assertThat()
                        .statusCode(SC_CREATED)
                        .extract().body().jsonPath().getObject("", Post.class);
    }

    @Step("Check information in the posts/{1}, should be status code 200, id are not empty")
    public void checkInformationInNewPost(Post post, int userId, String title, String body){
        Assert.assertEquals(post.getUserId(), userId, "userId is not match");
        Assert.assertEquals(post.getTitle(), title, "title is not match");
        Assert.assertEquals(post.getBody(), body, "body is not match");
        Assert.assertNotNull(post.getId(), "id must not be empty");
    }

    @Step("Get list all users and check status code 200 and response body in json format")
    public List<User> getListAllUsersRA() {
        return  given()
                        .spec(getBaseSpec())
                        .get(USERS_PATCH)
                .then()
                        .assertThat().statusCode(SC_OK)
                        .contentType(ContentType.JSON)
                        .extract().body().jsonPath().getList("", User.class);
    }

    @Step("Get user with id={0} and check status code 200")
    public User getUserWithIdRA(int id){
        return  given()
                        .spec(getBaseSpec())
                        .get(USERS_PATCH + id)
                .then()
                        .assertThat().statusCode(SC_OK)
                        .extract().body().jsonPath().getObject("", User.class);
    }

    @Step("Get list all posts, check status code 200 and response body in json format")
    public List<Post> getAllPostsR2() {
        Response<List<Post>> response = RetrofitClientFactory.executeCall(service.getListPosts());
        Assert.assertEquals(response.code(), SC_OK, "Status code is not equals");
        Assert.assertTrue(response.headers().get("Content-type").contains(ContentType.JSON.toString()), "Response body is not json");
        return response.body();
    }

    @Step("Check posts sorting by id")
    public void checkSortingListPostsById(List<Post> listPosts){
        List<Integer> ids = listPosts.stream().map(Post::getId).collect(Collectors.toList());
        Assert.assertEquals(ids, ids.stream().sorted().collect(Collectors.toList()), "List posts is not sorted by id");
    }

    @Step("Get post and check status code 200")
    public Post getPostR2(int id) {
        Response<Post> response = RetrofitClientFactory.executeCall(service.getPost(id));
        Assert.assertEquals(response.code(), SC_OK, "Status code is not equals");
        return response.body();
    }

    @Step("Сheck posts/{0} should be empty and status code 404")
    public void checkNonExistentPostR2(int idNonExistentPost) {
        Response<Post> response = RetrofitClientFactory.executeCall(service.getPost(idNonExistentPost));
        Assert.assertEquals(response.code(), SC_NOT_FOUND, "Status code is not equals");
        Assert.assertNull(response.body(), "Response body is not empty");
    }

    @Step("Create and get new post with userid={0}, should be status code 201")
    public Post createNewPostWithUserIdR2(Post testPost) {
        Response<Post> response = RetrofitClientFactory.executeCall(service.createPost(testPost));
        Assert.assertEquals(response.code(), SC_CREATED, "Status code is not equals");
        return response.body();
    }

    @Step("Get list all users and check status code 200 and response body in json format")
    public List<User> getListAllUsersR2() {
        Response<List<User>> response = RetrofitClientFactory.executeCall(service.getListUsers());
        Assert.assertEquals(response.code(), SC_OK, "Status code is not equals");
        Assert.assertTrue(response.headers().get("Content-type").contains(ContentType.JSON.toString()), "Response body is not json");
        return response.body();
    }

    @Step("Get user with id={0} and check status code 200")
    public User getUserWithIdR2(int idForSearchSingleUsers) {
        return service.getUser(idForSearchSingleUsers);
    }

    @Step("Find a user in the list by ID and check user data")
    public void checkUserDataById(List<User> listUsers, User testUser, int idForSearchInListUsers) {
        User foundUser = listUsers.stream().filter(user -> user.getId().equals(idForSearchInListUsers)).findFirst().orElse(null);
        Assert.assertEquals(foundUser, testUser, "User data is not equals");
    }
}
