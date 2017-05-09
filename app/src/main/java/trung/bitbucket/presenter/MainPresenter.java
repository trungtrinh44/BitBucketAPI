package trung.bitbucket.presenter;

import com.google.gson.Gson;

import trung.bitbucket.interfaces.ModelOps;
import trung.bitbucket.interfaces.PresenterModelOps;
import trung.bitbucket.interfaces.PresenterViewOps;
import trung.bitbucket.interfaces.ViewOps;
import trung.bitbucket.model.CreateNewRepo;
import trung.bitbucket.model.MainModel;
import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.RequestAccessToken;
import trung.bitbucket.model.UserInfo;

/**
 * Created by trung on 25/10/2016.
 */

public class MainPresenter implements PresenterModelOps, PresenterViewOps {
    private ModelOps model;
    private ViewOps view;

    public MainPresenter(ViewOps view) {
        this.view = view;
        model = new MainModel(this);
    }

    @Override
    public void loginSuccessful(UserInfo userInfo, RequestAccessToken.Token token) {
        if (view != null) view.loginSuccessful(userInfo, new Gson().toJson(token));
    }

    @Override
    public void showToast(String message) {
        if (view != null) view.showToast(message);
    }

    @Override
    public void returnRepositories(Repositories repos) {
        if (view != null) view.returnRepositories(repos);
    }

    @Override
    public void repoCreated(Repository repository) {
        if (view != null) view.repoCreated(repository);
    }

    @Override
    public void storeToken(RequestAccessToken.Token token) {
        if (view != null) {
            view.storeToken(new Gson().toJson(token));
        }
    }

    @Override
    public void repoDeleted(Repository repo) {
        if (view != null) view.repoDeleted(repo);
    }

    @Override
    public void login(String username, String password) {

        if (model != null) model.getAccessToken(username, password);
    }

    @Override
    public void getRepositories(String username) {
        if (model != null) model.getRepositories(username);
    }

    @Override
    public void createRepositories(CreateNewRepo.RepoInfo info, String userName) {

        if (model != null) model.createRepository(info, userName);
    }

    @Override
    public void setToken(String token) {
        if (model != null)
            model.setToken(new Gson().fromJson(token, RequestAccessToken.Token.class));
    }

    @Override
    public void deleteRepo(Repository repo) {
        if (model != null) model.deleteRepo(repo);
    }
}
