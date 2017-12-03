package com.example.corelib.network;

import com.example.corelib.model.Post;
import com.example.corelib.model.auth.username_validator.UsernameExists;
import com.example.corelib.model.email_validator.EmailExists;
import com.example.corelib.model.related_post.RelatedPostsList;
import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

/**
 * Created by prakh on 16-11-2017.
 */

public class DataManager {
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

    public void getTagsFromSearch(String includeFields, String search,
                               RemoteCallback<List<CategoriesOrTag>> callback) {
        myBlogApi.getTagsFromSearch(search, includeFields).enqueue(callback);
    }

    public void getCategoriesFromSearch(String includeFields, String searchQuery,
                               RemoteCallback<List<CategoriesOrTag>> callback) {
        myBlogApi.getCategoriesFromSearch(searchQuery, includeFields).enqueue(callback);
    }

    public void ifEmailExists(String insecure, String email,
                              RemoteCallback<EmailExists> callback) {
        myBlogApi.getIfEmailExists(insecure, email).enqueue(callback);
    }

    public void ifUsernameExists(String insecure, String username,
                              RemoteCallback<UsernameExists> callback) {
        myBlogApi.getIfUsernameExists(insecure, username).enqueue(callback);
    }
}
