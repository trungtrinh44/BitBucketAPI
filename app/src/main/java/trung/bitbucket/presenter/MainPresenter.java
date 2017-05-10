package trung.bitbucket.presenter;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trung.bitbucket.interfaces.PresenterViewOps;
import trung.bitbucket.interfaces.ViewOps;
import trung.bitbucket.model.CreateNewRepo;
import trung.bitbucket.model.DeleteRepo;
import trung.bitbucket.model.GetPublicUserInfo;
import trung.bitbucket.model.GetUserRepositories;
import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.RequestAccessToken;
import trung.bitbucket.model.ServiceGenerator;
import trung.bitbucket.model.UserInfo;

import static trung.bitbucket.utils.Constants.apiUrl;
import static trung.bitbucket.utils.Constants.grantType;
import static trung.bitbucket.utils.Constants.grantTypeRefresh;
import static trung.bitbucket.utils.Constants.key;
import static trung.bitbucket.utils.Constants.secret;
import static trung.bitbucket.utils.Constants.token;
import static trung.bitbucket.utils.Constants.tokenUrl;

/**
 * Created by trung on 25/10/2016.
 */

public class MainPresenter implements PresenterViewOps {
    private ViewOps view;


    public MainPresenter(ViewOps view) {
        this.view = view;
    }

    private void loginSuccessful(UserInfo userInfo, RequestAccessToken.Token token) {
        if (view != null) view.loginSuccessful(userInfo, new Gson().toJson(token));
    }

    private void showToast(String message) {
        if (view != null) view.showToast(message);
    }

    private void returnRepositories(Repositories repos) {
        if (view != null) view.returnRepositories(repos);
    }

    private void repoCreated(Repository repository) {
        if (view != null) view.repoCreated(repository);
    }

    private void storeToken(RequestAccessToken.Token token) {
        if (view != null) {
            view.storeToken(new Gson().toJson(token));
        }
    }

    private void repoDeleted(Repository repo) {
        if (view != null) view.repoDeleted(repo);
    }

    @Override
    public void login(final String username, String password) {

        RequestAccessToken requestAccessToken = new ServiceGenerator(tokenUrl).createService(RequestAccessToken.class, key, secret);
        Call<RequestAccessToken.Token> call = requestAccessToken.makeRequest(password, username, grantType);
        call.enqueue(new Callback<RequestAccessToken.Token>() {
            @Override
            public void onResponse(Call<RequestAccessToken.Token> call, Response<RequestAccessToken.Token> response) {
                if (response.isSuccessful()) {
                    token = response.body();
                    token.tokenType = token.tokenType.substring(0, 1).toUpperCase() + token.tokenType.substring(1);
                    getUserInfo(username, token);
                } else {
                    showToast("Login failed");
                }
            }

            @Override
            public void onFailure(Call<RequestAccessToken.Token> call, Throwable t) {

            }
        });
    }

    private void getUserInfo(String username, final RequestAccessToken.Token token) {
        GetPublicUserInfo getPublicUserInfo = new ServiceGenerator(apiUrl).createService(GetPublicUserInfo.class);
        Call<UserInfo> call = getPublicUserInfo.getInfo(username);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    loginSuccessful(response.body(), token);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
            }
        });
    }

    @Override
    public void getRepositories(final String username) {
        if (token != null) {
            GetUserRepositories getUserRepositories = new ServiceGenerator(apiUrl).createService(GetUserRepositories.class, token);
            Call<Repositories> call = getUserRepositories.getRepositories(new String[]{"owner", "member"});
            call.enqueue(new Callback<Repositories>() {
                @Override
                public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                    if (response.isSuccessful()) {
                        returnRepositories(response.body());
                    } else {
                        refreshToken();
                        getRepositories(username);
                    }
                }

                @Override
                public void onFailure(Call<Repositories> call, Throwable t) {

                }
            });
        }
    }

    private void refreshToken() {
        RequestAccessToken requestAccessToken = new ServiceGenerator(tokenUrl).createService(RequestAccessToken.class, key, secret);
        Call<RequestAccessToken.Token> call = requestAccessToken.refreshToken(token.refreshToken, grantTypeRefresh);
        call.enqueue(new Callback<RequestAccessToken.Token>() {
            @Override
            public void onResponse(Call<RequestAccessToken.Token> call, Response<RequestAccessToken.Token> response) {
                if (response.isSuccessful()) {
                    token = response.body();
                    token.tokenType = token.tokenType.substring(0, 1).toUpperCase() + token.tokenType.substring(1);
                    storeToken(token);
                    Log.d("refreshToken", token.accessToken);
                }
            }

            @Override
            public void onFailure(Call<RequestAccessToken.Token> call, Throwable t) {

            }
        });
    }

    @Override
    public void createRepository(final CreateNewRepo.RepoInfo repoInfo, final String userName) {
        if (token != null) {
            CreateNewRepo createNewRepo = new ServiceGenerator(apiUrl).createService(CreateNewRepo.class, token);
            String slug = repoInfo.name.replaceAll("[^a-zA-Z0-9]+", "-");
            Call<Repository> call = createNewRepo.create(userName, slug, repoInfo);
            call.enqueue(new Callback<Repository>() {
                @Override
                public void onResponse(Call<Repository> call, Response<Repository> response) {
                    if (response.isSuccessful()) {
                        repoCreated(response.body());
                    } else {
                        refreshToken();
                        createRepository(repoInfo, userName);
                    }
                }

                @Override
                public void onFailure(Call<Repository> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void setToken(String tokenString) {
        token = new Gson().fromJson(tokenString, RequestAccessToken.Token.class);
    }

    @Override
    public void deleteRepo(final Repository repo) {
        final DeleteRepo deleteRepo = new ServiceGenerator(apiUrl).createService(DeleteRepo.class, token);
        Call<ResponseBody> call = deleteRepo.delete(repo.fullName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    repoDeleted(repo);
                } else {
                    refreshToken();
                    deleteRepo(repo);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
