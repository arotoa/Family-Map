package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import java.io.*;
import java.net.*;

public class LoginHandler extends JSONHelper implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                //create request
                InputStream reqBody = exchange.getRequestBody();
                LoginRequest request = (LoginRequest) decodeFromJSON(reqBody, LoginRequest.class);

                //complete request
                LoginService service = new LoginService();
                LoginResult result = service.login(request);

                //send response headers
                if (!result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }

                encodeToJSON(exchange, result);
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
