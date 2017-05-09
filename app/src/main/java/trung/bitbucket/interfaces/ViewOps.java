package trung.bitbucket.interfaces;

import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.UserInfo;

/**
 * Created by trung on 25/10/2016.
 */

public interface ViewOps {
    void loginSuccessful(UserInfo userInfo, String token);

    void showToast(String message);

    void returnRepositories(Repositories repos);

    void repoCreated(Repository repository);

    void storeToken(String token);

    void repoDeleted(Repository repo);

    void deleteRepo(Repository repo);
}
