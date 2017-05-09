package trung.bitbucket.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by trung on 25/10/2016.
 */

public interface GetUserRepositories {
    @GET("repositories")
    Call<Repositories> getRepositories(@Query("role") String[] roles);
}
