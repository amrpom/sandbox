package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() < 2) {
            response.sendError(400, "Missing user ID");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1)); // isolates ID from the leading slash (so "id" instead of "/id")

            User user = userService.getUser(id);

            if (user == null) {
                response.sendError(404, "User not found");
                return;
            }

            writeJson(response, 200, user);

        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid user ID");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!isServletPath(request)) {
            response.sendError(404, "Invalid endpoint");
            return;
        }

        User input = readUser(request, response);

        if (input == null) {
            return;
        }

        if (!isValid(input)) {
            response.sendError(400, "Name is required");
            return;
        }

        User created = userService.createUser(input.getName());

        response.setHeader(
                "Location",
                request.getContextPath() + "/users/" + created.getId()
        );

        writeJson(response, 201, created);
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Long id = getUserId(request, response);

        if (id == null) {
            return;
        }

        User input = readUser(request, response);

        if (input == null) {
            return;
        }

        if (!isValid(input)) {
            response.sendError(400, "Name is required");
            return;
        }

        User updated = userService.updateUser(
                id,
                input.getName()
        );

        if (updated == null) {
            response.sendError(404, "User not found");
            return;
        }

        writeJson(response, 200, updated);
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        Long id = getUserId(request, response);

        if (id == null) {
            return;
        }

        boolean deleted = userService.deleteUser(id);

        if (!deleted) {
            response.sendError(404, "User not found");
            return;
        }

        response.setStatus(204);
    }

    private User readUser(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        try {
            return objectMapper.readValue(
                    request.getInputStream(),
                    User.class
            );

        } catch (JsonProcessingException e) {
            response.sendError(400, "Malformed JSON request body");
            return null;
        }
    }

    private boolean isValid(User user) {
        return user != null &&
                user.getName() != null &&
                !user.getName().isBlank();
    }

    private boolean isServletPath(
            HttpServletRequest request) {

        String pathInfo = request.getPathInfo();

        return pathInfo == null ||
                pathInfo.isEmpty() ||
                pathInfo.equals("/");
    }

    private Long getUserId(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null ||
                pathInfo.length() < 2 ||
                pathInfo.indexOf('/', 1) >= 0) {

            response.sendError(400, "Missing or invalid user ID");
            return null;
        }

        try {
            long id = Long.parseLong(pathInfo.substring(1)); // isolates user ID

            if (id <= 0) {
                response.sendError(400, "Invalid user ID");
                return null;
            }

            return id;

        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid user ID");
            return null;
        }
    }

    private void writeJson(
            HttpServletResponse response,
            int status,
            Object body)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                response.getOutputStream(),
                body
        );
    }
}
