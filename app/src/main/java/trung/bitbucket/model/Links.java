
package trung.bitbucket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links implements Serializable {
    @SerializedName("avatar")
    @Expose
    public Avatar avatar;

}
