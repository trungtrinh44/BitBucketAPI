package trung.bitbucket.interfaces;

import trung.bitbucket.model.CreateNewRepo;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.RequestAccessToken;

/**
 * Created by trung on 25/10/2016.
 */

public interface ModelOps {
    void getAccessToken(String username, String password);

    void getRepositories(String username);

    void createRepository(CreateNewRepo.RepoInfo repoInfo, String userName);

    void setToken(RequestAccessToken.Token token);

    void deleteRepo(Repository repo);
}
