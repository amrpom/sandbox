import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.User;
import org.example.UserService;
import org.example.UserServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserServletTest {

    private UserService userService;
    private UserServlet userServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private ByteArrayOutputStream responseBody;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userServlet = new UserServlet(userService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        responseBody = new ByteArrayOutputStream();

        try {
            when(response.getOutputStream())
                    .thenReturn(new TestServletOutputStream(responseBody));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnUserWhenFound() throws Exception {

        User user = new User(1L, "John");

        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn("/1");
        when(userService.getUser(1L)).thenReturn(user);

        userServlet.service(request, response);

        verify(response).setStatus(200);
        verify(response).setContentType("application/json");

        assertTrue(responseBody.toString(StandardCharsets.UTF_8).contains("John"));
    }
}
