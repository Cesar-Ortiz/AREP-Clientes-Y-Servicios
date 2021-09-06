package edu.escuelaing.arep.clienteservidor.httpserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class HttpServer {
    public static final HttpServer _instance = new HttpServer();

    private static HttpServer getInstance(){
        return _instance;
    }
    private HttpServer(){}

    public void start(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            serveConnection(clientSocket);
        }
        serverSocket.close();
    }

    public void serveConnection(Socket clientSocket) throws IOException, URISyntaxException {

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<String>();

        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }

        String uriStr= request.get(0).split(" ")[1];
        URI resourceURI = new URI(uriStr);

        outputLine = getResource(resourceURI);
        out.println(outputLine);

        out.close();
        in.close();
        clientSocket.close();
    }

    public String getResource(URI resourceURI) throws IOException {
        System.out.println("Received URI path: "+resourceURI.getPath());
        System.out.println("Received URI query: "+resourceURI.getQuery());
        //return computeDefaultResponse();
        return RequestResponseDisc();
    }

    public String RequestResponseDisc() throws IOException{
        File archivo = new File("src/main/resources/html_public/index.html");
        BufferedReader in = new BufferedReader(new FileReader(archivo));
        String str;
        String output = "HTTP/1.1 200 OK\r\nContent - Type: text/html\r\n\r\n";
        while((str = in.readLine())!= null){
            System.out.println(str);
            output+=str+"\n";
        }
        System.out.println(output);

        return output;
    }

    public String computeDefaultResponse(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Title of the document</title>\n"
                + "</head>"
                + "<body>"
                + "My Web Site"
                + "<img src=\"https://files.rcnradio.com/public/styles/image_834x569/public/2018-06/federacioncolombianadefutbol_0.jpg?itok=KhQ50TPY\"></img>"
                + "</body>"
                + "</html>";
        return outputLine;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer.getInstance().start(args);
    }
}

