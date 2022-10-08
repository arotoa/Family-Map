package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.SinglePersonRequest;
import result.SinglePersonResult;
import service.SinglePersonService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SinglePersonHandler extends JSONHelper implements HttpHandler {
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
                    SinglePersonRequest request = new SinglePersonRequest(reqBody[2], authToken);

                    //complete request
                    SinglePersonService service = new SinglePersonService();
                    SinglePersonResult result = service.getPerson(request);

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
