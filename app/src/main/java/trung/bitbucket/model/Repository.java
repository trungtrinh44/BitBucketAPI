package trung.bitbucket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trung on 25/10/2016.
 */

public class Repository implements Serializable {


    @SerializedName("scm")
    @Expose
    public String scm;
    @SerializedName("website")
    @Expose
    public String website;
    @SerializedName("has_wiki")
    @Expose
    public boolean hasWiki;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("links")
    @Expose
    public Links links;
    @SerializedName("fork_policy")
    @Expose
    public String forkPolicy;
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("created_on")
    @Expose
    public String createdOn;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("has_issues")
    @Expose
    public boolean hasIssues;
    @SerializedName("owner")
    @Expose
    public UserInfo owner;
    @SerializedName("updated_on")
    @Expose
    public String updatedOn;
    @SerializedName("size")
    @Expose
    public int size;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("is_private")
    @Expose
    public boolean isPrivate;
    @SerializedName("description")
    @Expose
    public String description;

}

