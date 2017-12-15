package com.example.corelib.model.auth.signinwithemail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prakh on 04-12-2017.
 */

public class UserInfo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("nicename")
    @Expose
    private String nicename;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("registered")
    @Expose
    private String registered;
    @SerializedName("displayname")
    @Expose
    private String displayname;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("wsl_current_provider")
    @Expose
    private String wslCurrentProvider;
    @SerializedName("wsl_current_user_image")
    @Expose
    private String wslCurrentUserImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWslCurrentProvider() {
        return wslCurrentProvider;
    }

    public void setWslCurrentProvider(String wslCurrentProvider) {
        this.wslCurrentProvider = wslCurrentProvider;
    }

    public String getWslCurrentUserImage() {
        return wslCurrentUserImage;
    }

    public void setWslCurrentUserImage(String wslCurrentUserImage) {
        this.wslCurrentUserImage = wslCurrentUserImage;
    }
}
