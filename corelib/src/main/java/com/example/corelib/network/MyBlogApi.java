package com.example.corelib.network;

import com.example.corelib.model.Post;
import com.example.corelib.model.auth.RegisterUserWithEmail;
import com.example.corelib.model.auth.UserRegisterNonce;
import com.example.corelib.model.auth.emailvalidator.EmailExists;
import com.example.corelib.model.auth.signinwithemail.GenerateAuthCookie;
import com.example.corelib.model.auth.usernamevalidator.UsernameExists;
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

    @GET("?json=user/if_email_exists")
    Call<EmailExists> getIfEmailExists(@Query("insecure") String insecure,
                                       @Query("email") String email);

    @GET("?json=user/if_username_exists")
    Call<UsernameExists> getIfUsernameExists(@Query("insecure") String insecure,
                                             @Query("username") String username);

    @GET("?json=get_nonce")
    Call<UserRegisterNonce> getRegisterNonce(@Query("controller") String controller,
                                             @Query("method") String method);

    @GET("?json=user/register")
    Call<RegisterUserWithEmail> registerUser(@Query("insecure") String insecure,
                                             @Query("email") String email,
                                             @Query("username") String username,
                                             @Query("nonce") String nonce,
                                             @Query("user_pass") String password,
                                             @Query("first_name") String firstName,
                                             @Query("last_name") String lastName,
                                             @Query("display_name") String displayName,
                                             @Query("seconds") Integer seconds,
                                             @Query("provider") String providerId);

    @GET("?json=user/generate_auth_cookie")
    Call<GenerateAuthCookie> loginUserWithEmail(@Query("insecure") String insecure,
                                                @Query("email") String email,
                                                @Query("password") String password);

    @GET("?json=user/register_using_idp")
    Call<GenerateAuthCookie> loginUserWithIdp(@Query("insecure") String insecure,
                                              @Query("email") String email,
                                              @Query("username") String username,
                                              @Query("nonce") String nonce,
                                              @Query("first_name") String firstName,
                                              @Query("last_name") String lastName,
                                              @Query("display_name") String displayName,
                                              @Query("provider") String providerId,
                                              @Query("avatar") String avatarUrl);
}
