package com.example.prakh.footballblog.post_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.corelib.model.Terms;
import com.example.prakh.footballblog.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 13-11-2017.
 */

public class PostTagsAdapter extends RecyclerView.Adapter<PostTagsAdapter.TagViewHolder> {

    private List<Terms> tagList;
    private TagClickListener listener;

    public PostTagsAdapter(List<Terms> tags, TagClickListener listener) {
        tagList = new ArrayList<>();
        tagList.addAll(tags);
        this.listener = listener;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag_list, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.tag.setText(tagList.get(position).getName());
        holder.tag.setBackground(holder.itemView.getContext().getResources()
        .getDrawable(R.drawable.tag_background));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tag_name)
        TextView tag;

        public TagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tag.setOnClickListener(view ->
            listener.onTagChipClicked(tagList.get(getAdapterPosition()).getId(),
                    tagList.get(getAdapterPosition()).getName()));
        }
    }

    public interface TagClickListener {
        void onTagChipClicked(Integer tagId, String tagName);
    }
}
