package com.example.prakh.footballblog.post_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.corelib.model.Terms;
import com.example.corelib.model.related_post.RelatedPost;
import com.example.corelib.network.DataManager;
import com.example.corelib.ui.DetailContract;
import com.example.corelib.ui.DetailPresenter;
import com.example.prakh.footballblog.GlideApp;
import com.example.prakh.footballblog.R;
import com.example.prakh.footballblog.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prakh on 17-11-2017.
 */

public class DetailFragment extends Fragment implements DetailContract.DetailScreenView,
        PostTagsAdapter.TagClickListener, SimilarPostsAdapter.SimilarPostsListener {

    public static final String ARG_POST_ID = "argPostId";

    @BindView(R.id.detail_frame_wrapper)
    LinearLayout detailFrame;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.post_author_name)
    TextView authorName;
    @BindView(R.id.post_date)
    TextView postDate;
    @BindView(R.id.author_avatar)
    ImageView authorAvatar;
    @BindView(R.id.detail_nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.fragment_progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private Integer postId;
    private AppCompatActivity activity;
    private DetailPresenter detailPresenter;

    public static DetailFragment newInstance(Integer postId) {

        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        args.putInt(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            postId = getArguments().getInt(ARG_POST_ID);
        }
        detailPresenter = new DetailPresenter(DataManager.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        detailPresenter.attachView(this);
        init(view);
        detailPresenter.getPostDetails(postId);
        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        activity = (AppCompatActivity) getActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                activity.onBackPressed();
                return true;
            case R.id.action_share:
                shareAction(detailPresenter.getPostTitle(), detailPresenter.getPostUrl());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareAction(String postTitle, String postUrl) {
        String sb = "Read Article \'" + postTitle + "\'\n" +
                "Using app \'" + activity.getString(R.string.app_name) + "\'\n" +
                "Source : " + postUrl + "";

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sb);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showMessageLayout(boolean show) {

    }

    @Override
    public void showAuthorName(String avatar, String name, String date) {
        ActionBar actionBar = null;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            actionBar = activity.getSupportActionBar();
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        GlideApp.with(activity).load(avatar)
                .circleCrop()
                .into(authorAvatar);

        authorName.setText(name);
        postDate.setText(Utils.getFormatedDateSimple(date));
    }

    @Override
    public void showPostContent(String featureImageUrl, String postTitle, String postContent,
                                List<Terms> tagList) {
        PostDetailWrapper postDetailWrapper = new PostDetailWrapper(activity, postTitle,
                featureImageUrl, postContent, tagList, this);
        detailFrame.addView(postDetailWrapper);
    }

    @Override
    public void showRelatedPosts(List<RelatedPost> relatedPostList) {
        SimilarPostsWrapper similarPostsWrapper = new SimilarPostsWrapper(activity,
                relatedPostList, this);
        detailFrame.addView(similarPostsWrapper);
    }

    @Override
    public void onTagChipClicked(Integer tagId, String tagName) {

    }

    @Override
    public void onPostClicked(Integer postId) {

    }
}
