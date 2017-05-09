package trung.bitbucket.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

/**
 * Created by trung on 26/10/2016.
 */

public interface DeleteRepo {
    @DELETE("repositories/{fullname}")
    Call<ResponseBody> delete(@Path("fullname") String username);
}
