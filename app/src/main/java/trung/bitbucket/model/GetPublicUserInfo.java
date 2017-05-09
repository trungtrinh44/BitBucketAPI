package trung.bitbucket.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by trung on 25/10/2016.
 */

public interface GetPublicUserInfo {
    @GET("users/{username}")
    Call<UserInfo> getInfo(@Path("username") String username);
}
