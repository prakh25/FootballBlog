package com.example.corelib.network;

import com.example.corelib.model.auth.UserObject;
import com.example.corelib.model.auth.UserRegisterNonce;
import com.example.corelib.model.auth.emailvalidator.EmailExists;
import com.example.corelib.model.auth.usernamevalidator.UsernameExists;
import com.example.corelib.model.post.Post;
import com.example.corelib.model.post_new.PostDetailResponse;
import com.example.corelib.model.post_new.PostListResponse;
import com.example.corelib.model.post_new.PostViewCount;
import com.example.corelib.model.related_post.RelatedPostsList;
import com.example.corelib.model.splash.ValidateCookie;
import com.example.corelib.model.splash.notification.CallBackDevice;
import com.example.corelib.model.splash.notification.DeviceInfo;
import com.example.corelib.model.tags_list.CategoriesOrTag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by prakh on 16-11-2017.
 */

public interface MyBlogApi {

    //recent posts
    @GET("?json=get_recent_posts")
    Call<PostListResponse> getRecentPosts();

    @GET("wp-json/myFootballBlog/views/{id}")
    Call<PostViewCount> updatePostViewCount(@Path("id") Integer postId);

    //Post Details
    @GET("?json=get_post")
    Call<PostDetailResponse> getPostDetails(@Query("id") Integer postId);

    @GET("wp-json/related-posts-by-taxonomy/v1/posts/{post_id}")
    Call<RelatedPostsList> getRelatedPosts(@Path("post_id") Integer postId);

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

    @GET("?json=get_search_results")
    Call<PostListResponse> getPostsFromSearch(@Query("search") String query);

    @GET("wp-json/wp/v2/categories")
    Call<List<CategoriesOrTag>> getCategoriesFromSearch(@Query("search") String searchQuery,
                                                        @Query("fields") String includeFields);

    @GET("wp-json/wp/v2/tags")
    Call<List<CategoriesOrTag>> getTagsFromSearch(@Query("search") String searchQuery,
                                                  @Query("fields") String includeFields);

    // Auth Api
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
    Call<UserObject> registerUserWithEmail(@Query("insecure") String insecure,
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
    Call<UserObject> loginUserWithEmail(@Query("insecure") String insecure,
                                        @Query("email") String email,
                                        @Query("password") String password);

    @GET("?json=user/generate_auth_cookie_idp")
    Call<UserObject> loginUserUsingIdp(@Query("insecure") String insecure,
                                       @Query("email") String email);

    @GET("?json=user/register_using_idp")
    Call<UserObject> registerUserWithIdp(@Query("insecure") String insecure,
                                         @Query("email") String email,
                                         @Query("username") String username,
                                         @Query("nonce") String nonce,
                                         @Query("first_name") String firstName,
                                         @Query("last_name") String lastName,
                                         @Query("display_name") String displayName,
                                         @Query("provider") String providerId,
                                         @Query("avatar") String avatarUrl);

    @GET("?json=user/validate_auth_cookie")
    Call<ValidateCookie> isCookieValid(@Query("insecure") String insecure,
                                       @Query("cookie") String cookie);

    @GET("?json=user/get_current_user_info")
    Call<UserObject> getCurrentUserData(@Query("insecure") String insecure,
                                        @Query("cookie") String cookie);

    @Headers({"Cache-Control: max-age=0", "User-Agent: Wordpress"})
    @POST("?api-fcm=register")
    Call<CallBackDevice> registerForFcm(@Body DeviceInfo deviceInfo);
}
