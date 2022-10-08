package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.SingleEventRequest;
import result.SingleEventResult;
import service.SingleEventService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SingleEventHandler extends JSONHelper implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                //get auth token
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    //create request
                    String[] reqBody = exchange.getRequestURI().toString().split("/");
                    SingleEventRequest request = new SingleEventRequest(reqBody[2], authToken);

                    //complete request
                    SingleEventService service = new SingleEventService();
                    SingleEventResult result = service.getEvent(request);

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
