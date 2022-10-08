package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import result.AllEventsResult;
import service.AllEventsService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AllEventsHandler extends JSONHelper implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                //get auth token
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    //complete request
                    AllEventsService service = new AllEventsService();
                    AllEventsResult result = service.getEvents(authToken);

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
