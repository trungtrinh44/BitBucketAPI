
package trung.bitbucket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Avatar implements Serializable {

    @SerializedName("href")
    @Expose
    public String href;

}
