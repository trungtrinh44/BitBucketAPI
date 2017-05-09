package trung.bitbucket.interfaces;

import trung.bitbucket.model.CreateNewRepo;
import trung.bitbucket.model.Repository;

/**
 * Created by trung on 25/10/2016.
 */

public interface PresenterViewOps {
    void login(String username, String password);

    void getRepositories(String username);

    void createRepositories(CreateNewRepo.RepoInfo info, String userName);

    void setToken(String token);

    void deleteRepo(Repository repo);
}
