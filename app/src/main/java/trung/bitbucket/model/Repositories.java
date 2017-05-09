package trung.bitbucket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 25/10/2016.
 */

public class Repositories implements Serializable {
    @SerializedName("values")
    @Expose
    public List<Repository> values = new ArrayList<>();
}
