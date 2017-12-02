package com.example.prakh.footballblog.post_detail;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.corelib.model.related_post.RelatedPost;
import com.example.prakh.footballblog.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 13-11-2017.
 */

public class SimilarPostsWrapper extends LinearLayout {

    @BindView(R.id.tag_list)
    RecyclerView recyclerView;

    private SimilarPostsAdapter adapter;

    public SimilarPostsWrapper(Context context) {
        super(context);
    }

    public SimilarPostsWrapper(Context context, List<RelatedPost> relatedPostList,
                               SimilarPostsAdapter.SimilarPostsListener listener) {
        super(context);
        adapter = new SimilarPostsAdapter(relatedPostList, listener);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.wrrapper_related_posts, this);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setMotionEventSplittingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
}
