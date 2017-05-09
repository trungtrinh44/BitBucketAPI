package trung.bitbucket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by trung on 25/10/2016.
 */

public interface RequestAccessToken {
    @FormUrlEncoded
    @POST("site/oauth2/access_token")
    Call<Token> makeRequest(@Field("password") String password, @Field("username") String username, @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @POST("site/oauth2/access_token")
    Call<Token> refreshToken(@Field("refresh_token") String refreshToken, @Field("grant_type") String grant_type);

    class Token implements Serializable {
        @SerializedName("access_token")
        @Expose
        public String accessToken;
        @SerializedName("scopes")
        @Expose
        public String scopes;
        @SerializedName("expires_in")
        @Expose
        public Integer expiresIn;
        @SerializedName("refresh_token")
        @Expose
        public String refreshToken;
        @SerializedName("token_type")
        @Expose
        public String tokenType;
    }
}
