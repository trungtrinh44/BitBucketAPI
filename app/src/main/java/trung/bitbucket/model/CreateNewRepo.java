package trung.bitbucket.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by trung on 26/10/2016.
 */

public interface CreateNewRepo {
    @POST("repositories/{username}/{repo_slug}")
    Call<Repository> create(@Path("username") String username, @Path("repo_slug") String reponame, @Body RepoInfo repoInfo);

    class RepoInfo {
        public final boolean is_private;
        public final String scm;
        public final String name;
        public final String fork_policy;
        public String description;

        public RepoInfo(boolean is_private, String description, String scm, String name, String fork_policy) {
            this.is_private = is_private;
            this.description = description;
            this.scm = scm;
            this.name = name;
            this.fork_policy = fork_policy;
        }
    }
}
