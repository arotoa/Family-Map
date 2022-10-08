package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import result.ClearResult;
import service.ClearService;

import java.io.*;
import java.net.HttpURLConnection;

public class ClearHandler extends JSONHelper implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                //complete request
                ClearService service = new ClearService();
                ClearResult result = service.clear();

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
        catch (DataAccessException e) {
            e.printStackTrace();
        }

    }
}
