package tests;

import api.TypicodeService;
import io.restassured.http.ContentType;
import models.Post;
import models.User;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static rest_assured.RestClient.getBaseSpec;
import retrofit2.*;
import utils.JsonMapper;

public class ApiTests {
    private static final String POSTS_PATCH = "/posts/";
    private static final String USERS_PATCH = "/users/";
    private static final File PATH_TO_TEST_USER = new File("src/test/resources/user.json");
    @Test(groups = "rest_assured")
    public void test_rest_assured(){
        List<Post> postsStep1 = given()
                .spec(getBaseSpec())
                .when()
                .get(POSTS_PATCH)
                .then()
                .assertThat().statusCode(SC_OK)
                .assertThat().contentType(ContentType.JSON)
                .extract().body().jsonPath().getList("", Post.class);
        List<Integer> ids = postsStep1.stream().map(Post::getId).collect(Collectors.toList());
        List<Integer> sortedIds = ids.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(sortedIds, ids);

        Post postStep2 = given()
                .spec(getBaseSpec())
                .when()
                .get(POSTS_PATCH + 99)
                .then()
                .assertThat().statusCode(SC_OK)
                .extract().body().jsonPath().getObject("", Post.class);
        Assert.assertEquals(postStep2.getUserId(), 10);
        Assert.assertEquals(postStep2.getId(), 99);
        Assert.assertNotNull(postStep2.getTitle());
        Assert.assertNotNull(postStep2.getBody());

        given()
                .spec(getBaseSpec())
                .when()
                .get(POSTS_PATCH + 150)
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("isEmpty()", Matchers.is(true));

        Post newPostStep4 = new Post(1, "test", "example_test");
        given()
                .spec(getBaseSpec())
                .body(newPostStep4)
                .when()
                .post(POSTS_PATCH)
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .body("userId", equalTo(newPostStep4.getUserId()))
                .body("id", notNullValue())
                .body("title", equalTo(newPostStep4.getTitle()))
                .body("body", equalTo(newPostStep4.getBody()));

        User testUserStep5 = JsonMapper.readValue(PATH_TO_TEST_USER, User.class);
        List<User> users = given()
                .spec(getBaseSpec())
                .get(USERS_PATCH)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(SC_OK)
                .extract().body().jsonPath().getList("", User.class);
        User foundUser = users.stream().filter(user -> user.getId().equals(5)).findFirst().orElse(null);
        Assert.assertEquals(foundUser, testUserStep5);

        User testUserStep6 = JsonMapper.readValue(PATH_TO_TEST_USER, User.class);
        User user = given()
                .spec(getBaseSpec())
                .get(USERS_PATCH + 5)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().body().jsonPath().getObject("", User.class);
        Assert.assertEquals(testUserStep6, user);
    }

    @Test(groups ="retrofit2")
    public void test_retrofit2() {
        TypicodeService service = RetrofitBuilder.build().create(TypicodeService.class);

        Response<List<Post>> responseListPosts = RetrofitClientFactory.executeCall(service.getListPosts());
        List<Integer> ids = responseListPosts.body().stream().map(Post::getId).collect(Collectors.toList());
        List<Integer> sortedIds = ids.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(sortedIds, ids, "Message is not sorted by id");

        Response<Post> responsePost99 = RetrofitClientFactory.executeCall(service.getPost(99));
        Assert.assertEquals(responsePost99.code(), SC_OK, "Status code");
        Assert.assertEquals(responsePost99.body().getUserId(), 10, "userId is not match");
        Assert.assertEquals(responsePost99.body().getId(), 99, "id is not match");
        Assert.assertNotNull(responsePost99.body().getBody(), "Body is empty");
        Assert.assertNotNull(responsePost99.body().getTitle(), "Title is empty");

        Response<Post> responseEmptyPost = RetrofitClientFactory.executeCall(service.getPost(150));
        Assert.assertEquals(responseEmptyPost.code(), SC_NOT_FOUND, "Status code");
        Assert.assertNull(responseEmptyPost.body(), "Body is not empty");

        Post newPostStep4 = new Post(1,  "test", "example_test");
        Response<Post> responseTestPost = RetrofitClientFactory.executeCall(service.createPost(newPostStep4));
        Assert.assertEquals(responseTestPost.code(), SC_CREATED, "Status Code");
        Assert.assertEquals(responseTestPost.body().getTitle(), newPostStep4.getTitle(), "Title is not match");
        Assert.assertEquals(responseTestPost.body().getBody(), newPostStep4.getBody(), "Body is not match");
        Assert.assertEquals(responseTestPost.body().getUserId(), newPostStep4.getUserId(), "userId is not match");
        Assert.assertNotNull(responseTestPost.body().getId(), "id is null");

        User testUserStep5 = JsonMapper.readValue(PATH_TO_TEST_USER, User.class);
        Response<List<User>> responseListUser = RetrofitClientFactory.executeCall(service.getListUsers());
        Assert.assertEquals(responseListUser.code(), SC_OK, "Status Code");
        Assert.assertTrue(responseListUser.headers().get("Content-type").contains(ContentType.JSON.toString()), "Response body is not json");
        User foundUser = responseListUser.body().stream().filter(user -> user.getId().equals(5)).findFirst().orElse(null);
        Assert.assertEquals(foundUser, testUserStep5, "Users is not match");

        User testUserStep6 = JsonMapper.readValue(PATH_TO_TEST_USER, User.class);
        Response<User> responseTestUser = RetrofitClientFactory.executeCall(service.getUser(5));
        Assert.assertEquals(responseTestUser.code(), SC_OK, "Status Code");
        Assert.assertEquals(responseTestUser.body(), testUserStep6, "Users is not match");
    }
}
