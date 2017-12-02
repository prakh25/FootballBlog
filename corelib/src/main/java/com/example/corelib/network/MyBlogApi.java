package com.example.corelib.network;

import com.example.corelib.model.Post;
import com.example.corelib.model.related_post.RelatedPostsList;
import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by prakh on 16-11-2017.
 */

public interface MyBlogApi {

    @GET("wp-json/wp/v2/posts/?_embed")
    Call<List<Post>> getAllPostsList(@Query("page") Integer pageNo,
                                     @Query("fields") String includeFields);

    @GET("wp-json/wp/v2/posts/{id}/?_embed")
    Call<Post> getPostDetails(@Path("id") Integer postId,
                              @Query("fields") String includeFields);

    @GET("wp-json/related-posts-by-taxonomy/v1/posts/{post_id}")
    Call<RelatedPostsList> getRelatedPosts(@Path("post_id") Integer postId );

    @GET("wp-json/wp/v2/tags")
    Call<List<CategoriesOrTag>> getTagsList(@Query("page") Integer pageNo,
                                            @Query("fields") String includeFields);

    @GET("wp-json/wp/v2/categories")
    Call<List<CategoriesOrTag>> getCategoriesList(@Query("page") Integer pageNo,
                                            @Query("fields") String includeFields);

    @GET("wp-json/wp/v2/posts/?_embed")
    Call<List<Post>> getUserSelectedPostList(@Query("page") Integer pageNo,
                                             @Query("categories") String includeCategories,
                                             @Query("fields") String includeFields);

    //Search Apis

    @GET("wp-json/wp/v2/posts/?_embed")
    Call<List<Post>> getPostFromSearch(@Query("search") String searchQuery,
                                     @Query("fields") String includeFields);

    @GET("wp-json/wp/v2/categories")
    Call<List<CategoriesOrTag>> getCategoriesFromSearch(@Query("search") String searchQuery,
                                                  @Query("fields") String includeFields);

    @GET("wp-json/wp/v2/tags")
    Call<List<CategoriesOrTag>> getTagsFromSearch(@Query("search") String searchQuery,
                                            @Query("fields") String includeFields);
}
