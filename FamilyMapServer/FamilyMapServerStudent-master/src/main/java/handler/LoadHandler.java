package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.LoadRequest;
import result.LoadResult;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class LoadHandler extends JSONHelper implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                //create request
                InputStream reqBody = exchange.getRequestBody();
                LoadRequest request = (LoadRequest) decodeFromJSON(reqBody, LoadRequest.class);

                //complete request
                LoadService service = new LoadService();
                LoadResult result = service.load(request);

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
