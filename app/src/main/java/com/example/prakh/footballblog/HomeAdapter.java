package com.example.prakh.footballblog;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.corelib.model.Post;
import com.example.prakh.footballblog.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 08-11-2017.
 */

public class HomeAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> postList;

    private PostInteractionListener listener;

    public static final int VIEW_TYPE_LIST = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    @IntDef({VIEW_TYPE_LOADING, VIEW_TYPE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ViewType {
    }

    @ViewType
    private int viewType;

    public HomeAdapter() {
        postList = new ArrayList<>();
        viewType = VIEW_TYPE_LIST;
        listener = null;
    }

    @Override
    public int getItemViewType(int position) {
        return postList.get(position) == null ?
                VIEW_TYPE_LOADING : postList.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return postList.size() >= position ?
                postList.get(position).getId() : -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return onIndicationViewHolder(parent);
        }
        return onGenericViewHolder(parent);
    }

    private RecyclerView.ViewHolder onIndicationViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_progress_bar, parent, false);
        return new ProgressViewHolder(view);
    }

    private RecyclerView.ViewHolder onGenericViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_list, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_LOADING) {
            return; // no-op
        }
        onBindGenericItemViewHolder((PostViewHolder) holder, position);
    }

    private void onBindGenericItemViewHolder(final PostViewHolder holder, int position) {

        String featureImageUrl = postList.get(holder.getAdapterPosition()).getEmbedded()
                .getFeaturedMedia().getFirst().getSourceUrl()
                .replace("localhost", "192.168.0.23");

        String postTitle = postList.get(holder.getAdapterPosition()).getTitle().getRendered();
        String postContent = postList.get(holder.getAdapterPosition()).getContent().getRendered();
        String postCategory = postList.get(holder.getAdapterPosition()).getEmbedded().getWpTerm()
                .get(0).get(0).getName();
        String authorName = postList.get(holder.getAdapterPosition()).getEmbedded().getAuthor().get(0).getName();
        String authorAvatarUrl = postList.get(holder.getAdapterPosition()).getEmbedded().getAuthor()
                .get(0).getAvatarUrls().get96();
        String publishDate = postList.get(holder.getAdapterPosition()).getDateGmt();

        GlideApp.with(holder.itemView.getContext())
                .load(featureImageUrl)
                .centerCrop()
                .into(holder.postFeatureImage);

        holder.postTitle.setText(Utils.fromHtml(postTitle));

        holder.postContent.setText(Utils.fromHtml(postContent));

        holder.postCategory.setBackground(
                holder.itemView.getContext().getResources()
                        .getDrawable(Utils.selectCategoryBackground(postCategory)));

        holder.postCategory.setText(postCategory);

        GlideApp.with(holder.itemView.getContext())
                .load(authorAvatarUrl)
                .circleCrop()
                .into(holder.postAuthorAvatar);

        holder.authorName.setText(authorName);

        holder.postDate.setText(Utils.getFormatedDateSimple(publishDate));
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
        notifyItemRangeInserted(getItemCount(), this.postList.size());
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

    public boolean addLoadingView() {
        if (getItemViewType(postList.size() - 1) != VIEW_TYPE_LOADING) {
            add(null);
            return true;
        }
        return false;
    }

    public boolean removeLoadingView() {
        if (postList.size() > 1) {
            int loadingViewPosition = postList.size() - 1;
            if (getItemViewType(loadingViewPosition) == VIEW_TYPE_LOADING) {
                remove(loadingViewPosition);
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(@ViewType int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_progress_bar)
        ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.post_list_layout)
        ConstraintLayout itemLayout;
        @BindView(R.id.postFeatureImage)
        ImageView postFeatureImage;
        @BindView(R.id.post_title)
        TextView postTitle;
        @BindView(R.id.post_content)
        TextView postContent;
        @BindView(R.id.post_category)
        TextView postCategory;
        @BindView(R.id.post_author_name)
        TextView authorName;
        @BindView(R.id.author_avatar)
        ImageView postAuthorAvatar;
        @BindView(R.id.post_date)
        TextView postDate;

        public PostViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            itemLayout.setOnClickListener(view1 -> listener
                    .getPostDetail(postList.get(getAdapterPosition()).getId()));
        }
    }

    public interface PostInteractionListener {
        void getPostDetail(Integer postId);
    }

    public void setListener(PostInteractionListener postInteractionListener) {
        listener = postInteractionListener;
    }
}
