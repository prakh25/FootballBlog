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
import com.like.OnLikeListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 20-11-2017.
 */

public class CategoriesAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoriesOrTag> categoriesList;

    private CategoriesInteractionListener listener;

    public static final int VIEW_TYPE_LIST = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    @IntDef({VIEW_TYPE_LOADING, VIEW_TYPE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    private  @interface ViewType {
    }

    @CategoriesAdapter.ViewType
    private int viewType;

    public CategoriesAdapter() {
        categoriesList = new ArrayList<>();
        viewType = VIEW_TYPE_LIST;
        listener = null;
    }

    @Override
    public int getItemViewType(int position) {
        return categoriesList.get(position) == null ?
                VIEW_TYPE_LOADING : categoriesList.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return categoriesList.size() >= position ?
                categoriesList.get(position).getId() : -1;
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
                .inflate(R.layout.item_progress_bar_vertical, parent, false);
        return new CategoriesAdapter.ProgressViewHolder(view);
    }

    private RecyclerView.ViewHolder onGenericViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);
        return new CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_LOADING) {
            return; // no-op
        }
        onBindGenericItemViewHolder((CategoriesHolder) holder, position);
    }

    private void onBindGenericItemViewHolder(final CategoriesHolder holder, int position) {
        holder.categoryName.setText(holder.itemView.getContext().getResources()
                .getString(R.string.categories_list_item, categoriesList.get(position).getName(),
                        categoriesList.get(position).getCount()));

        holder.followBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                listener.saveCategory(categoriesList.get(position));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                listener.deleteCategory(categoriesList.get(position));
            }
        });
    }

    public void add(CategoriesOrTag item) {
        add(null, item);
    }

    public void add(@Nullable Integer position, CategoriesOrTag item) {
        if (position != null) {
            categoriesList.add(position, item);
            notifyItemInserted(position);
        } else {
            categoriesList.add(item);
            notifyItemInserted(categoriesList.size() - 1);
        }
    }

    public void addItems(List<CategoriesOrTag> list) {
        this.categoriesList.addAll(list);
        notifyItemRangeInserted(getItemCount(), this.categoriesList.size());
    }

    public void remove(int position) {
        if (categoriesList.size() < position) {
            return;
        }
        categoriesList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        categoriesList.clear();
        notifyDataSetChanged();
    }

    public boolean addLoadingView() {
        if (getItemViewType(categoriesList.size() - 1) != VIEW_TYPE_LOADING) {
            add(null);
            return true;
        }
        return false;
    }

    public boolean removeLoadingView() {
        if (categoriesList.size() > 1) {
            int loadingViewPosition = categoriesList.size() - 1;
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

    public void setViewType(@CategoriesAdapter.ViewType int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
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
        TextView categoryName;
        @BindView(R.id.btn_follow)
        LikeButton followBtn;

        public CategoriesHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface CategoriesInteractionListener {
        void saveCategory(CategoriesOrTag item);

        void deleteCategory(CategoriesOrTag item);
    }

    public void setListener(CategoriesInteractionListener categoriesInteractionListener) {
        listener = categoriesInteractionListener;
    }
}

