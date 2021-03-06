
package com.example.corelib.model.post_new;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_plain")
    @Expose
    private String titlePlain;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("excerpt")
    @Expose
    private String excerpt;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<>();
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = new ArrayList<>();
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<>();
    @SerializedName("attachments")
    @Expose
    private List<Attachment> attachments = new ArrayList<>();
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("comment_status")
    @Expose
    private String commentStatus;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("custom_fields")
    @Expose
    private CustomFields customFields;
    @SerializedName("thumbnail_size")
    @Expose
    private String thumbnailSize;
    @SerializedName("thumbnail_images")
    @Expose
    private ThumbnailImages thumbnailImages;
    public final static Parcelable.Creator<Post> CREATOR = new Creator<Post>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return (new Post[size]);
        }

    }
    ;

    protected Post(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.slug = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.titlePlain = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.excerpt = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.modified = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.categories, (com.example.corelib.model.post_new.Category.class.getClassLoader()));
        in.readList(this.tags, (com.example.corelib.model.post_new.Tag.class.getClassLoader()));
        this.author = ((Author) in.readValue((Author.class.getClassLoader())));
        in.readList(this.comments, (com.example.corelib.model.post_new.Comment.class.getClassLoader()));
        in.readList(this.attachments, (com.example.corelib.model.post_new.Attachment.class.getClassLoader()));
        this.commentCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
        this.customFields = ((CustomFields) in.readValue((CustomFields.class.getClassLoader())));
        this.thumbnailSize = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailImages = ((ThumbnailImages) in.readValue((ThumbnailImages.class.getClassLoader())));
    }

    public Post() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePlain() {
        return titlePlain;
    }

    public void setTitlePlain(String titlePlain) {
        this.titlePlain = titlePlain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public CustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }

    public String getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(String thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    public ThumbnailImages getThumbnailImages() {
        return thumbnailImages;
    }

    public void setThumbnailImages(ThumbnailImages thumbnailImages) {
        this.thumbnailImages = thumbnailImages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(type);
        dest.writeValue(slug);
        dest.writeValue(url);
        dest.writeValue(status);
        dest.writeValue(title);
        dest.writeValue(titlePlain);
        dest.writeValue(content);
        dest.writeValue(excerpt);
        dest.writeValue(date);
        dest.writeValue(modified);
        dest.writeList(categories);
        dest.writeList(tags);
        dest.writeValue(author);
        dest.writeList(comments);
        dest.writeList(attachments);
        dest.writeValue(commentCount);
        dest.writeValue(commentStatus);
        dest.writeValue(thumbnail);
        dest.writeValue(customFields);
        dest.writeValue(thumbnailSize);
        dest.writeValue(thumbnailImages);
    }

    public int describeContents() {
        return  0;
    }

}
