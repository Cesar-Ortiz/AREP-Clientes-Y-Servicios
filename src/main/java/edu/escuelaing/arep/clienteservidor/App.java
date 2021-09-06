package edu.escuelaing.arep.clienteservidor;

import edu.escuelaing.arep.clienteservidor.httpserver.HttpServer;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, URISyntaxException {
        HttpServer._instance.setPort(getPort());
        HttpServer._instance.start(args);
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
