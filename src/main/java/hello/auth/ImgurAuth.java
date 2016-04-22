package hello.auth;

import java.net.HttpURLConnection;

public interface ImgurAuth {

    void authorizeConnection(HttpURLConnection conn);

    void requestNewAccessToken();

    void sendAuthenticationCode(String code);

    void authorizeClient();

}
