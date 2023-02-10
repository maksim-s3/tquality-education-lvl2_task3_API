package tests;

import models.Post;
import models.User;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.io.File;
import java.util.List;
import utils.JsonMapper;
import tests.steps.StepsApiTests;
import utils.Random;

public class ApiTests {
    private static final File FILE_TEST_USER = new File("src/test/resources/user.json");
    private final User testUser = JsonMapper.readValue(FILE_TEST_USER, User.class);
    private StepsApiTests steps = new StepsApiTests();



    @Parameters({"idForResponsePost", "idForExpectedRequestPost", "userIdForExpectedRequestPost", "idNonExistentPost", "userIdForNewPost", "idForSearchInListUsers", "idForSearchSingleUsers"})
    @Test(groups = "rest_assured")
    public void test_rest_assured(int idForResponsePost, int idForExpectedRequestPost, int userIdForExpectedRequestPost, int idNonExistentPost, int userIdForNewPost, int idForSearchInListUsers, int idForSearchSingleUsers) {
        List<Post> allPosts = steps.getAllPostsRA();
        steps.checkSortingListPostsByIdRA(allPosts);

        Post post = steps.getPostRA(idForResponsePost);
        steps.checkInformationInPost(post, idForExpectedRequestPost, userIdForExpectedRequestPost);

        steps.checkNonExistentPostRA(idNonExistentPost);

        Post testPost = new Post(userIdForNewPost, Random.getRandomText(), Random.getRandomText());
        Post newResponsePost = steps.createNewPostWithUserIdRA(testPost);
        steps.checkInformationInNewPost(newResponsePost, testPost.getUserId(), testPost.getTitle(), testPost.getBody());

        List<User> allUsers = steps.getListAllUsersRA();
        steps.checkUserDataById(allUsers, testUser, idForSearchInListUsers);

        User user = steps.getUserWithIdRA(idForSearchSingleUsers);
        Assert.assertEquals(user, testUser, String.format("User data users/%d is not equal", idForSearchSingleUsers));
    }

    @Parameters({"idForResponsePost", "idForExpectedRequestPost", "userIdForExpectedRequestPost", "idNonExistentPost", "userIdForNewPost", "idForSearchInListUsers", "idForSearchSingleUsers"})
    @Test(groups = "retrofit2")
    public void test_retrofit2(int idForResponsePost, int idForExpectedRequestPost, int userIdForExpectedRequestPost, int idNonExistentPost, int userIdForNewPost, int idForSearchInListUsers, int idForSearchSingleUsers){
        List<Post> allPosts = steps.getAllPostsR2();
        steps.checkSortingListPostsById(allPosts);

        Post post = steps.getPostR2(idForResponsePost);
        steps.checkInformationInPost(post, idForExpectedRequestPost, userIdForExpectedRequestPost);

        steps.checkNonExistentPostR2(idNonExistentPost);

        Post testPost = new Post(userIdForNewPost, Random.getRandomText(), Random.getRandomText());
        Post newResponsePost = steps.createNewPostWithUserIdR2(testPost);
        steps.checkInformationInNewPost(newResponsePost, testPost.getUserId(), testPost.getTitle(), testPost.getBody());

        List<User> allUsers = steps.getListAllUsersR2();
        steps.checkUserDataById(allUsers, testUser, idForSearchInListUsers);

        User user = steps.getUserWithIdR2(idForSearchSingleUsers);
        Assert.assertEquals(user, testUser, String.format("User data users/%d is not equal", idForSearchSingleUsers));
    }
}
