#  REST API + Jetty Example

A tiny sample for a RESTful API with a Jetty server managing servlets.

## Structure
- `Application` creates the Jetty server, servlet context, defines the path the servlet is responsible for, starts the http server, and keeps main thread waiting until server stops
- `User` holds the model
- `UserService` holds the business logic
- `UserServlet` handles requests

## Servlet Notes
- Servlet class extends HTTPServlet, which has method definitions for HTTP calls. Automatically called when Jetty receives requests matching the URL mapping to the servlet
- Servlet instances are generally shared across requests (so don't store request-specific info in it), but support multi-threading
- Servlet container controls the lifecycle and invocation of the servlet
- `HttpServletRequest` and `HttpServletResponse` contain information about the IO objects, so you can read path info, URL params, headers, body, etc
- ObjectMapper helps switch between JSON and the User model

### Test Notes
- JSON is a byte-based output stream, so the output stream mock needs to reflect that

### POST
Invoke-RestMethod -Uri "http://localhost:8080/users" `
-Method POST `
-ContentType "application/json" `
-Body '{"name":"John"}'

### GET
Invoke-RestMethod -Uri "http://localhost:8080/users/1" `
-Method Get

### PUT
$body = @{
name = "Jane"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/users/1" `
    -Method Put `
-ContentType "application/json" `
-Body $body

### DELETE
Invoke-RestMethod -Uri "http://localhost:8080/users/1" `
-Method Delete

### Testing malformed JSON
Invoke-RestMethod -Uri "http://localhost:8080/users" `
    -Method Post `
-ContentType "application/json" `
-Body '{"name": "John"'
