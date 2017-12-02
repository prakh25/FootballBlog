package com.example.prakh.footballblog.interests;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.corelib.model.tags_list.CategoriesOrTag;
import com.example.prakh.footballblog.R;
import com.like.LikeButton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 20-11-2017.
 */

public class TagsListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoriesOrTag> tagsList;

    private TagsListAdapter.TagsInteractionListener listener;

    private boolean isButton = false;

    public static final int VIEW_TYPE_LIST = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    @IntDef({VIEW_TYPE_LOADING, VIEW_TYPE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    private  @interface ViewType {
    }

    @TagsListAdapter.ViewType
    private int viewType;

    public TagsListAdapter() {
        tagsList = new ArrayList<>();
        viewType = VIEW_TYPE_LIST;
        listener = null;
    }

    @Override
    public int getItemViewType(int position) {
        return tagsList.get(position) == null ?
                VIEW_TYPE_LOADING : tagsList.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return tagsList.size() >= position ?
                tagsList.get(position).getId() : -1;
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
                .inflate(R.layout.item_progress_bar_horizontal, parent, false);
        return new TagsListAdapter.ProgressViewHolder(view);
    }

    private RecyclerView.ViewHolder onGenericViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);
        return new TagsListAdapter.CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_LOADING) {
            return; // no-op
        }
        onBindGenericItemViewHolder((TagsListAdapter.CategoriesHolder) holder, position);
    }

    private void onBindGenericItemViewHolder(final TagsListAdapter.CategoriesHolder holder, int position) {
        holder.tagName.setText(holder.itemView.getContext().getResources()
                .getString(R.string.tag_list_item, tagsList.get(position).getName(),
                        tagsList.get(position).getCount()));

    }

    public void add(CategoriesOrTag item) {
        add(null, item);
    }

    public void add(@Nullable Integer position, CategoriesOrTag item) {
        if (position != null) {
            tagsList.add(position, item);
            notifyItemInserted(position);
        } else {
            tagsList.add(item);
            notifyItemInserted(tagsList.size() - 1);
        }
    }

    public void addItems(List<CategoriesOrTag> list) {
        this.tagsList.addAll(list);
        notifyItemRangeInserted(getItemCount(), this.tagsList.size());
    }

    public void remove(int position) {
        if (tagsList.size() < position) {
            return;
        }
        tagsList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        tagsList.clear();
        notifyDataSetChanged();
    }

    public boolean addLoadingView() {
        if (getItemViewType(tagsList.size() - 1) != VIEW_TYPE_LOADING) {
            add(null);
            return true;
        }
        return false;
    }

    public boolean removeLoadingView() {
        if (tagsList.size() > 1) {
            int loadingViewPosition = tagsList.size() - 1;
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

    public void setViewType(@TagsListAdapter.ViewType int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_progress_bar)
        ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView tagName;
        @BindView(R.id.btn_follow)
        LikeButton followBtn;

        public CategoriesHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface TagsInteractionListener {
        void getPostList(Integer postId);
    }

    public void setListener(TagsListAdapter.TagsInteractionListener tagsInteractionListener) {
        listener = tagsInteractionListener;
    }
}
