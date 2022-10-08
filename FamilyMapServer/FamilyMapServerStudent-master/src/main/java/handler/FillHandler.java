package handler;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.FillRequest;
import result.FillResult;
import service.FillService;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.lang.Integer.parseInt;

public class FillHandler extends JSONHelper implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                //get all parts of request
                String[] reqBody = exchange.getRequestURI().toString().split("/");
                String username = reqBody[2];
                FillRequest request;
                if (reqBody.length > 3) {
                    request = new FillRequest(username, reqBody[3]);
                }
                else {
                    request = new FillRequest(username);
                }

                //get request
                FillService service = new FillService();
                FillResult result = service.fill(request);

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
