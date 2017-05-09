package trung.bitbucket.model;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trung.bitbucket.interfaces.ModelOps;
import trung.bitbucket.interfaces.PresenterModelOps;

/**
 * Created by trung on 25/10/2016.
 */

public class MainModel implements ModelOps {
    private static final String key = "MzRPAhrGedueH5vXRj";
    private static final String secret = "deJW7AQV26BbZeHHj2fu4DG2Sx63SLF7";
    private static final String tokenUrl = "https://bitbucket.org/";
    private static final String apiUrl = "https://api.bitbucket.org/2.0/";
    private static final String grantType = "password";
    private static final String grantTypeRefresh = "refresh_token";
    private static RequestAccessToken.Token token = null;
    private PresenterModelOps presenter;

    public MainModel(PresenterModelOps presenter) {
        this.presenter = presenter;
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
                    if (presenter != null) presenter.storeToken(token);
                    Log.d("refreshToken", token.accessToken);
                }
            }

            @Override
            public void onFailure(Call<RequestAccessToken.Token> call, Throwable t) {

            }
        });
    }

    @Override
    public void getAccessToken(final String username, String password) {
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
                    if (presenter != null) presenter.showToast("Login failed");
                }
            }

            @Override
            public void onFailure(Call<RequestAccessToken.Token> call, Throwable t) {

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
                        if (presenter != null) presenter.returnRepositories(response.body());
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
                        if (presenter != null) presenter.repoCreated(response.body());
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
    public void setToken(RequestAccessToken.Token token) {
        MainModel.token = token;
    }

    @Override
    public void deleteRepo(final Repository repo) {
        final DeleteRepo deleteRepo = new ServiceGenerator(apiUrl).createService(DeleteRepo.class, token);
        Call<ResponseBody> call = deleteRepo.delete(repo.fullName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (presenter != null) presenter.repoDeleted(repo);
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

    private void getUserInfo(String username, final RequestAccessToken.Token token) {
        GetPublicUserInfo getPublicUserInfo = new ServiceGenerator(apiUrl).createService(GetPublicUserInfo.class);
        Call<UserInfo> call = getPublicUserInfo.getInfo(username);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    if (presenter != null) presenter.loginSuccessful(response.body(), token);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
            }
        });
    }
}
