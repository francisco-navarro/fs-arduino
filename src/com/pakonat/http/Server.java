package com.pakonat.http;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new IndexHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        readIndex();
    }

    private static void readIndex() {
    	FileReader in = new FileReader("/test.txt");
        BufferedReader br = new BufferedReader(in);
	}

	static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response ="";
            System.out.println(t.getRequestURI());

            if (t.getRequestURI().toString().equals("/data")) {
            	response = serializeData();
            } else {            	
            	response = "This is the response";
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

	public static String serializeData() {
		JSONObject jo = new JSONObject();
		jo.put("name", "jon doe");
		jo.put("age", "22");
		jo.put("city", "chicago");
		
		return jo.toString();
	}
}