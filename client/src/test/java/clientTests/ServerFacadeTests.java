package clientTests;

import Exceptions.DataAccessException;
import RequestClasses.*;
import chess.ChessGame;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;



public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade.ServerFacade serverFacade;
    private static int port;

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade.ServerFacade(port);
    }

    @BeforeEach
    void stopServer() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.connect();
        try (InputStream respBody = http.getInputStream()){
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

        }
    }

    public static void setPort(int port) {
        ServerFacadeTests.port = port;
    }

    @Test
    public void registerSuccess() throws Exception {
        Assertions.assertDoesNotThrow(() -> serverFacade.register(new RegisterRequest("NewUser", "pass", "mail")));
    }

    @Test
    public void registerFailure() throws Exception {
        serverFacade.register(new RegisterRequest("NewUser", "pass", "mail"));
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(new RegisterRequest("NewUser", "pass", "mail")));
    }

    @Test
    public void loginSuccess() throws Exception {
        serverFacade.register(new RegisterRequest("NewUser", "pass", "mail"));
        Assertions.assertDoesNotThrow(() -> serverFacade.login(new LoginRequest("NewUser", "pass")));
    }

    @Test
    public void loginFailure() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.login(new LoginRequest("NewUser", "pass")));
    }

    @Test
    public void logoutSuccess() throws Exception {
        serverFacade.register(new RegisterRequest("NewUser", "pass", "mail"));
//        serverFacade.login(new LoginRequest("NewUser", "pass"));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(new LogoutRequest("")));

    }

    @Test
    public void logoutFailure() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout(new LogoutRequest(serverFacade.authToken)));
    }

    @Test
    public void createGameSuccess() throws Exception {
        serverFacade.register(new RegisterRequest("NewUser", "pass", "mail"));
//        serverFacade.login(new LoginRequest("NewUser", "pass"));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(new CreateGameRequest("myGame")));
    }

    @Test
    public void createGameFailure() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.createGame(new CreateGameRequest("myGame")));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        serverFacade.register(new RegisterRequest("NewUser", "pass", "mail"));
        serverFacade.createGame(new CreateGameRequest("myGame"));
//        serverFacade.login(new LoginRequest("NewUser", "pass"));
        Assertions.assertDoesNotThrow(() -> serverFacade.listGames(new ListGamesRequest(serverFacade.authToken)));
    }

    @Test
    public void listGamesFailure() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.listGames(new ListGamesRequest(serverFacade.authToken)));
    }

    @Test
    public void joinGameSuccess() throws Exception {
        serverFacade.register(new RegisterRequest("NewUser", "pass", "mail"));
        serverFacade.createGame(new CreateGameRequest("myGame"));

//        serverFacade.login(new LoginRequest("NewUser", "pass"));
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0)));
    }

    @Test
    public void joinGameFailure() {
        Assertions.assertThrows(Exception.class, () -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0)));
    }



}
