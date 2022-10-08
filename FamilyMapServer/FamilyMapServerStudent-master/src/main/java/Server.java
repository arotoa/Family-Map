import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    private void run(String portNumber) {

        //Initialize server
        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);
        //create handlers
        System.out.println("Creating contexts");
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new SinglePersonHandler());
        server.createContext("/person", new AllPersonsHandler());
        server.createContext("/event/", new SingleEventHandler());
        server.createContext("/event", new AllEventsHandler());
        server.createContext("/", new FileHandler());

        //start server
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }



    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
