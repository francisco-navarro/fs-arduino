package com.pakonat.http;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.json.JSONObject;

import com.pakonat.avionics.HSI;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new IndexHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static String readIndex() throws IOException {
    	File f = new File("./WebContent/index.html");
    	FileReader in = new FileReader(f);
        BufferedReader br = new BufferedReader(in);
        StringBuffer sb = new StringBuffer();
        String line;
        do {
        	line = br.readLine();
        	if (line != null) {
        		sb.append(line);
        		sb.append("\n");
        	}
        } while(line != null);
        
        br.close();
        in.close();
        return sb.toString();
	}

	static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response ="";

            if (t.getRequestURI().toString().equals("/data")) {
            	response = serializeData();
            	t.getResponseHeaders().add("Content-Type", "application/json");
            } else {            	
            	response = readIndex();
            	t.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

	public static String serializeData() {
		JSONObject jo = new JSONObject();
		jo.put("heading", HSI.lastHeading);
		jo.put("obs", HSI.lastOBS);
		jo.put("deviation", HSI.lastDeviation - 90);
		
		return jo.toString();
	}
}