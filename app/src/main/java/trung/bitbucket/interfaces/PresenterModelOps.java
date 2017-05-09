package trung.bitbucket.interfaces;

import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.RequestAccessToken;
import trung.bitbucket.model.UserInfo;

/**
 * Created by trung on 25/10/2016.
 */

public interface PresenterModelOps {
    void loginSuccessful(UserInfo userInfo, RequestAccessToken.Token token);

    void showToast(String message);

    void returnRepositories(Repositories repos);

    void repoCreated(Repository repository);

    void storeToken(RequestAccessToken.Token token);

    void repoDeleted(Repository repo);
}
