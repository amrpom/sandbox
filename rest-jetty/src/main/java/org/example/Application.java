package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

public class Application {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);

        ServletContextHandler context =
                new ServletContextHandler();

        context.setContextPath("/");

        UserService userService = new UserService();

        context.addServlet(
                new ServletHolder(
                        new UserServlet(userService)),
                "/users/*"
        );

        server.setHandler(context);

        server.start();

        System.out.println(
                "REST API running at http://localhost:8080");

        server.join();
    }
}
