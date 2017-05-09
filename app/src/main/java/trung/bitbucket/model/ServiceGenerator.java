package trung.bitbucket.model;

import android.util.Log;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by trung on 25/10/2016.
 */

public class ServiceGenerator {

    private final String baseURL;
    private final OkHttpClient.Builder httpClient;
    private final Retrofit.Builder builder;

    public ServiceGenerator(String baseURL) {
        this.baseURL = baseURL;
        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create());
    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            final String basic = Credentials.basic(username, password);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass, final RequestAccessToken.Token token) {
        if (token != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", token.tokenType + " " + token.accessToken)
                            .header("Accept", "application/json");
                    Request request = requestBuilder.build();
                    Log.e("Header", request.headers().toString());
                    return chain.proceed(request);
                }
            });
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
