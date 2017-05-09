
package trung.bitbucket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfo implements Serializable {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("links")
    @Expose
    public Links links;

}
