package com.example.prakh.footballblog.post_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.corelib.model.related_post.RelatedPost;
import com.example.prakh.footballblog.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 13-11-2017.
 */

public class SimilarPostsAdapter extends RecyclerView.Adapter<SimilarPostsAdapter.SimilarPostViewHolder> {

    private List<RelatedPost> relatedPosts;
    private SimilarPostsListener listener;

    public SimilarPostsAdapter(List<RelatedPost> relatedPostList,
                               SimilarPostsListener listener) {
        relatedPosts = new ArrayList<>();
        relatedPosts.addAll(relatedPostList);
        this.listener = listener;
    }

    @Override
    public SimilarPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_related_posts, parent, false);
        return new SimilarPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimilarPostViewHolder holder, int position) {
        holder.postTitle.setText(relatedPosts.get(position).getPostTitle());
        holder.postTitle.setBackground(holder.itemView.getContext()
                .getResources().getDrawable(R.drawable.tag_background));
    }

    @Override
    public int getItemCount() {
        return relatedPosts.size();
    }

    public class SimilarPostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.related_post_title)
        TextView postTitle;

        public SimilarPostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            postTitle.setOnClickListener(view ->
                    listener.onPostClicked(relatedPosts.get(getAdapterPosition()).getID()));
        }
    }

    public interface SimilarPostsListener {
        void onPostClicked(Integer postId);
    }
}
