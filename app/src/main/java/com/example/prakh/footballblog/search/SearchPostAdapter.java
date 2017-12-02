package com.example.prakh.footballblog.search;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.corelib.model.Post;
import com.example.prakh.footballblog.GlideApp;
import com.example.prakh.footballblog.R;
import com.example.prakh.footballblog.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 21-11-2017.
 */

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.SearchPostViewHolder> {

    private List<Post> postList;

    private SearchPostListener listener;

    public SearchPostAdapter() {
        postList = new ArrayList<>();
        listener  = null;
    }

    @Override
    public SearchPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_post, parent, false);
        return new SearchPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchPostViewHolder holder, int position) {

        String featureImageUrl = postList.get(holder.getAdapterPosition()).getEmbedded()
                .getFeaturedMedia().getFirst().getSourceUrl()
                .replace("localhost","192.168.0.23");

        String postTitle = postList.get(holder.getAdapterPosition()).getTitle().getRendered();
        String postContent = postList.get(holder.getAdapterPosition()).getContent().getRendered();

        GlideApp.with(holder.itemView.getContext())
                .load(featureImageUrl)
                .centerCrop()
                .into(holder.featureImage);

        holder.postTitle.setText(Utils.fromHtml(postTitle));

        holder.postContent.setText(Utils.fromHtml(postContent));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void add(Post item) {
        add(null, item);
    }

    public void add(@Nullable Integer position, Post item) {
        if (position != null) {
            postList.add(position, item);
            notifyItemInserted(position);
        } else {
            postList.add(item);
            notifyItemInserted(postList.size() - 1);
        }
    }

    public void addItems(List<Post> posts) {
        this.postList.addAll(posts);
        notifyItemRangeInserted(getItemCount(), this.postList.size() - 1);
    }

    public void remove(int position) {
        if (postList.size() < position) {
            return;
        }
        postList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        postList.clear();
        notifyDataSetChanged();
    }

    public class SearchPostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_card)
        CardView post;
        @BindView(R.id.search_feature_image)
        ImageView featureImage;
        @BindView(R.id.search_title)
        TextView postTitle;
        @BindView(R.id.search_content)
        TextView postContent;

        public SearchPostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            post.setOnClickListener(view ->
                    listener.onPostClicked(postList.get(getAdapterPosition()).getId()));
        }
    }

    public interface SearchPostListener {
        void onPostClicked(Integer postId);
    }

    public void setListener(SearchPostListener searchPostListener) {
        listener = searchPostListener;
    }
}
