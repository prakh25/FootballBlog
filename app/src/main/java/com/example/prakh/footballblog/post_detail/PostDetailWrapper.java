package com.example.prakh.footballblog.post_detail;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.corelib.model.post.Terms;
import com.example.prakh.footballblog.GlideApp;
import com.example.prakh.footballblog.R;
import com.example.prakh.footballblog.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prakh on 17-11-2017.
 */

public class PostDetailWrapper extends LinearLayout {

    @BindView(R.id.detail_post_title)
    TextView postTitle;
    @BindView(R.id.detail_post_feature_image)
    ImageView featureImage;
    @BindView(R.id.detail_post_content)
    WebView postContent;
    @BindView(R.id.tag_list)
    RecyclerView recyclerView;

    private PostTagsAdapter adapter;
    private String content;
    private String imageUrl;
    private String title;

    public PostDetailWrapper(Context context) {
        super(context);
    }

    public PostDetailWrapper(Context context, String title, String imageUrl,
                             String content, List<Terms> tags,
                             PostTagsAdapter.TagClickListener listener) {
        super(context);
        adapter = new PostTagsAdapter(tags, listener);

        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;

        init(context);

    }

    private void init(Context context) {
        inflate(context, R.layout.wrapper_detail_title, this);
        ButterKnife.bind(this);

        postTitle.setText(Utils.fromHtml(title));

        GlideApp.with(context).load(imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(featureImage);

        postContent.setBackgroundColor(Color.TRANSPARENT);
        postContent.getSettings().setBuiltInZoomControls(false);

        String html_data_beg = "<html><head><style>@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/arvo.ttf\")}body {font-family: MyFont;font-size:large;text-align: left;}img{max-width:100%;height:auto;} iframe{width:100%;}</style></head><body>";

        String html_data_end = "</body></html>";
        String detailContent = html_data_beg + content + html_data_end;

        postContent.loadDataWithBaseURL(null, detailContent,
                "text/html", "utf-8", null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setMotionEventSplittingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
}
