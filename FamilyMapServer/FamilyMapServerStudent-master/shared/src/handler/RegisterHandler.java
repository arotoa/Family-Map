package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class RegisterHandler extends JSONHelper implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                //create request
                InputStream reqBody = exchange.getRequestBody();
                RegisterRequest request = (RegisterRequest) decodeFromJSON(reqBody, RegisterRequest.class);

                //complete request
                RegisterService service = new RegisterService();
                RegisterResult result = service.register(request);

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
