package com.example.prakh.footballblog.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakh.footballblog.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 26-12-2017.
 */

public class SearchSuggestionAdapter extends
        RecyclerView.Adapter<SearchSuggestionAdapter.SuggestionViewHolder> {

    private List<SearchResultItem> itemList;
    private SuggestionInteractionListener listener;

    public SearchSuggestionAdapter(List<SearchResultItem> itemList) {
        this.itemList = new ArrayList<>();
        this.itemList.addAll(itemList);
    }

    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuggestionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_suggestion, parent, false));
    }

    @Override
    public void onBindViewHolder(SuggestionViewHolder holder, int position) {
        holder.leftIcon.setImageResource(itemList.get(position).getLeftIcon());
        holder.title.setText(itemList.get(position).getTitle());
        holder.rightIcon.setImageResource(itemList.get(position).getRightIcon());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public SearchResultItem getItem(Integer position) {
        return itemList.get(position);
    }

    public class SuggestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_suggestion_item)
        View suggestionItem;
        @BindView(R.id.search_suggestion_left_icon)
        ImageView leftIcon;
        @BindView(R.id.search_suggestion_title)
        TextView title;
        @BindView(R.id.search_suggestion_right_icon)
        ImageView rightIcon;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            suggestionItem.setOnClickListener(view ->
                    listener.onSuggestionClicked(itemList.get(getAdapterPosition())));
        }
    }

    public void setListener(SuggestionInteractionListener listener) {
        this.listener = listener;
    }

    public interface SuggestionInteractionListener {
        void onSuggestionClicked(SearchResultItem item);
    }
}
