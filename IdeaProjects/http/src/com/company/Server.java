package com.company;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Server {
    private final static String BASE_URL = "/employee";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        EmployeeRepository employeeRepository = new EmployeeRepository(getConnection("testDB.s3db"));
        server.createContext(BASE_URL, new MyHandler(employeeRepository));
        server.setExecutor(null);
        server.start();
    }

    static private java.sql.Connection getConnection(String connectionString) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        java.sql.Connection connection = DriverManager.getConnection("jdbc:sqlite:" + connectionString);
        return connection;
    }

    static class MyHandler implements HttpHandler {
        private EmployeeRepository employeeRepository;

        public MyHandler(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        @Override
        public void handle(HttpExchange t) {
            try {
            switch (t.getRequestMethod()) {
                case "GET":
                    get(t);
                    break;
                case "POST":
                    post(t);
                    break;
                case "PUT":
                    put(t);
                    break;
                case "DELETE":
                    delete(t);
                    break;
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void delete(HttpExchange t) throws Exception {
            String email = getEmailFormUri(t.getRequestURI());
            employeeRepository.deleteEmployee(email);
            fillHandler(t, 200, "Ok");
        }

        private void put(HttpExchange t) throws Exception {
            Employee employee = parseEmployee(t.getRequestBody());
            employeeRepository.putEmployee(employee);
            fillHandler(t, 200, "Ok");
        }

        private void get(HttpExchange t) throws Exception {
            String email = getEmailFormUri(t.getRequestURI());
            Employee employee = employeeRepository.getEmployee(email);
            if (employee == null) {
                String message = "employee not found";
                fillHandler(t, 404, message);
            } else {
                Gson gson = new Gson();
                String jsonString = gson.toJson(employee);
                fillHandler(t, 200, jsonString);
            }
        }

        private void post(HttpExchange t) throws Exception {
            Employee employee = parseEmployee(t.getRequestBody());
            employeeRepository.addEmployee(employee);
            fillHandler(t, 200, "Ok");
        }

        private void fillHandler(HttpExchange t, int code, String content) throws IOException {
            t.sendResponseHeaders(code, content.length());
            OutputStream os = t.getResponseBody();
            os.write(content.getBytes());
            os.close();
        }

        private Employee parseEmployee(InputStream is) {
            String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            Employee employee = new Gson().fromJson(result, Employee.class);
            return employee;
        }

        private String getEmailFormUri(URI uri) {
            return uri.toString().substring(BASE_URL.length() + 1);
        }
    }
}
