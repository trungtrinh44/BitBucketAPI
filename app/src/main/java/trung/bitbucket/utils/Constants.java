package trung.bitbucket.utils;

import trung.bitbucket.model.RequestAccessToken;
import trung.bitbucket.model.UserInfo;

/**
 * Created by trung on 10/05/2017.
 */

public class Constants {
    public static final String key = "MzRPAhrGedueH5vXRj";
    public static final String secret = "deJW7AQV26BbZeHHj2fu4DG2Sx63SLF7";
    public static final String tokenUrl = "https://bitbucket.org/";
    public static final String apiUrl = "https://api.bitbucket.org/2.0/";
    public static final String grantType = "password";
    public static final String grantTypeRefresh = "refresh_token";
    public static RequestAccessToken.Token token = null;
    public static UserInfo userInfo;
}
