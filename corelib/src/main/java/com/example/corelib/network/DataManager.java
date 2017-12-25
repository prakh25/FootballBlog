package com.example.corelib.network;

import com.example.corelib.model.post_new.PostListResponse;
import com.example.corelib.model.splash.notification.CallBackDevice;
import com.example.corelib.model.splash.notification.DeviceInfo;
import com.example.corelib.model.post.Post;
import com.example.corelib.model.auth.UserObject;
import com.example.corelib.model.auth.UserRegisterNonce;
import com.example.corelib.model.auth.emailvalidator.EmailExists;
import com.example.corelib.model.auth.usernamevalidator.UsernameExists;
import com.example.corelib.model.related_post.RelatedPostsList;
import com.example.corelib.model.splash.ValidateCookie;
import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

/**
 * Created by prakh on 16-11-2017.
 */

public class DataManager {
    private static final String INSECURE = "cool";

    private static DataManager instance;

    private final MyBlogApi myBlogApi;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {
        myBlogApi = NetworkService.provideBlogPost();
    }

    public void getAllPostsList(String includeFields, Integer pageNo,
                                RemoteCallback<List<Post>> callback) {
        myBlogApi.getAllPostsList(pageNo, includeFields).enqueue(callback);
    }

    public void getRecentPosts(RemoteCallback<PostListResponse> callback) {
        myBlogApi.getRecentPosts().enqueue(callback);
    }

    public void getPostDetails(Integer postId, String includeFields,
                               RemoteCallback<Post> callback) {
        myBlogApi.getPostDetails(postId, includeFields).enqueue(callback);
    }

    public void getRelatedPosts(Integer postId, RemoteCallback<RelatedPostsList> callback) {
        myBlogApi.getRelatedPosts(postId).enqueue(callback);
    }

    public void getAllTagsList(String includeFields, Integer pageNo,
                                RemoteCallback<List<CategoriesOrTag>> callback) {
        myBlogApi.getTagsList(pageNo, includeFields).enqueue(callback);
    }

    public void getAllCategoriesList(String includeFields, Integer pageNo,
                                RemoteCallback<List<CategoriesOrTag>> callback) {
        myBlogApi.getCategoriesList(pageNo, includeFields).enqueue(callback);
    }

    public void getUserCategoriesPostList(String includeFields, Integer pageNo,
                                          String includeCategories,
                                          RemoteCallback<List<Post>> callback) {
        myBlogApi.getUserSelectedPostList(pageNo, includeCategories, includeFields)
                .enqueue(callback);
    }

    public void getPostFromSearch(String includeFields, String searchString,
                                RemoteCallback<List<Post>> callback) {
        myBlogApi.getPostFromSearch(searchString, includeFields).enqueue(callback);
    }

    public void getPostsFromSearch(String query, RemoteCallback<PostListResponse> callback) {
        myBlogApi.getPostsFromSearch(query).enqueue(callback);
    }

    public void getTagsFromSearch(String includeFields, String search,
                               RemoteCallback<List<CategoriesOrTag>> callback) {
        myBlogApi.getTagsFromSearch(search, includeFields).enqueue(callback);
    }

    public void getCategoriesFromSearch(String includeFields, String searchQuery,
                               RemoteCallback<List<CategoriesOrTag>> callback) {
        myBlogApi.getCategoriesFromSearch(searchQuery, includeFields).enqueue(callback);
    }

    public void ifEmailExists(String email, RemoteCallback<EmailExists> callback) {
        myBlogApi.getIfEmailExists(INSECURE, email).enqueue(callback);
    }

    public void ifUsernameExists(String username, RemoteCallback<UsernameExists> callback) {
        myBlogApi.getIfUsernameExists(INSECURE, username).enqueue(callback);
    }

    public void getRegisterNonce(String controller, String method,
                                 RemoteCallback<UserRegisterNonce> callback) {
        myBlogApi.getRegisterNonce(controller, method).enqueue(callback);
    }

    public void registerUserWithEmail(String email, String username, String nonce,
                                      String user_pass, String firstName, String lastName,
                                      String displayName, Integer seconds, String providerId,
                                      RemoteCallback<UserObject> callback) {
        myBlogApi.registerUserWithEmail(INSECURE, email, username, nonce, user_pass, firstName, lastName,
                displayName, seconds, providerId).enqueue(callback);
    }

    public void loginUser(String email, String password, RemoteCallback<UserObject> callback) {
        myBlogApi.loginUserWithEmail(INSECURE, email, password).enqueue(callback);
    }

    public void loginUserIdp(String email, RemoteCallback<UserObject> callback) {
        myBlogApi.loginUserUsingIdp(INSECURE, email).enqueue(callback);
    }

    public void registerUserUsingIdp(String email, String username,
                                     String nonce, String firstName, String lastName,
                                     String displayName, String providerId,
                                     String avatarUrl, RemoteCallback<UserObject> callback) {
        myBlogApi.registerUserWithIdp(INSECURE, email, username, nonce, firstName,
                lastName, displayName, providerId, avatarUrl).enqueue(callback);
    }

    public void isValidCookie(String cookie, RemoteCallback<ValidateCookie> callback) {
        myBlogApi.isCookieValid(INSECURE, cookie).enqueue(callback);
    }

    public void getCurrentUser(String cookie, RemoteCallback<UserObject> callback) {
        myBlogApi.getCurrentUserData(INSECURE, cookie).enqueue(callback);
    }

    public void registerForFcm(DeviceInfo deviceInfo, RemoteCallback<CallBackDevice> callback) {
        myBlogApi.registerForFcm(deviceInfo).enqueue(callback);
    }
}
