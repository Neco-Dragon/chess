import RequestClasses.*;
import ResultClasses.InsertGameResult;
import ResultClasses.ListGamesResult;
import ResultClasses.LoginResult;
import ResultClasses.RegisterResult;
import com.google.gson.Gson;
import model.GameData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;

public class ServerFacade {
    private final int port;
    public String authToken;
    public String errorMessage;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void register(RegisterRequest registerRequest) throws Exception {
        HttpURLConnection http = makeRequest("/user", "POST", Boolean.TRUE, registerRequest);
        // Output the response body
        switch (http.getResponseCode()){
            case (200): //Code for a successful request
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    RegisterResult registerResult = new Gson().fromJson(inputStreamReader, ResultClasses.RegisterResult.class);
                    this.authToken = registerResult.authToken();
                }
                break;
            case (400):
                throw new FacadeException("[400] Error: bad request");
            case (403):
                throw new FacadeException("[403] Error: already taken");
            default:
                throw new FacadeException("[" + http.getResponseCode() + "] Error: An unknown error occurred. Try again.");
        }

    }
    public void login(LoginRequest loginRequest) throws Exception{
        HttpURLConnection http = makeRequest("/session", "POST", Boolean.TRUE, loginRequest);
        // Output the response body
        switch (http.getResponseCode()){
            case (200): //Code for a successful request
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    LoginResult loginResult = new Gson().fromJson(inputStreamReader, ResultClasses.LoginResult.class);
                    this.authToken = loginResult.authToken();
                }
                break;
            case (401):
                throw new FacadeException("[401] Error: unauthorized");
            default:
                throw new FacadeException("[" + http.getResponseCode() + "] Error: An unknown error occurred. Try again.");
        }
    }
    public void logout(LogoutRequest logoutRequest) throws Exception{
        HttpURLConnection http = makeRequest("/session", "DELETE", Boolean.FALSE, logoutRequest);
        // Output the response body
        switch (http.getResponseCode()){
            case (200): //Code for a successful request
                try (InputStream respBody = http.getInputStream()) {
                    authToken = null; //TODO: depending on how the tests work, they might expect the authToken to still be there
                }
                break;
            case (401):
                throw new FacadeException("[401] Error: unauthorized");
            default:
                throw new FacadeException("[" + http.getResponseCode() + "] Error: An unknown error occurred. Try again.");
        }
    }
    public void createGame(CreateGameRequest createGameRequest) throws Exception{
        HttpURLConnection http = makeRequest("/game", "POST", Boolean.TRUE, createGameRequest);
        // Output the response body
        switch (http.getResponseCode()){
            case (200): //Code for a successful request
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    InsertGameResult insertGameResult = new Gson().fromJson(inputStreamReader, ResultClasses.InsertGameResult.class);
                    System.out.println(insertGameResult.gameID());
                }
                break;
            case (400):
                throw new FacadeException("[400] Error: bad request");
            case (401):
                throw new FacadeException("[401] Error: unauthorized");
            default:
                throw new FacadeException("[" + http.getResponseCode() + "] Error: An unknown error occurred. Try again.");
        }
    }
    public void listGames(ListGamesRequest listGamesRequest) throws Exception{
        HttpURLConnection http = makeRequest("/game", "GET", Boolean.FALSE, listGamesRequest);
        // Output the response body
        switch (http.getResponseCode()){
            case (200): //Code for a successful request
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    ListGamesResult listGamesResult = new Gson().fromJson(inputStreamReader, ResultClasses.ListGamesResult.class);
                    System.out.println(this.printGames(listGamesResult.games()));
                }
                break;
            case (401):
                throw new FacadeException("[401] Error: unauthorized");
            default:
                throw new FacadeException("[" + http.getResponseCode() + "] Error: An unknown error occurred. Try again.");
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws Exception{
        HttpURLConnection http = makeRequest("/game", "PUT", Boolean.FALSE, joinGameRequest);
        // Output the response body
        switch (http.getResponseCode()){
            case (200): //Code for a successful request
                break; //This request has no body in the successful case
            case (400):
                throw new FacadeException("[400] Error: bad request");
            case (401):
                throw new FacadeException("[401] Error: unauthorized");
            case (403):
                throw new FacadeException("[403] Error: already taken");
            default:
                throw new FacadeException("[" + http.getResponseCode() + "] Error: An unknown error occurred. Try again.");
        }
    }
    public static void quit() throws Exception{
        return;
    }

    private HttpURLConnection makeRequest(String urlStub, String requestType, Boolean hasBody, Object bodyData) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + this.port + urlStub); //TODO: Change for each connection
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(requestType); //TODO: Change for each connection

        if (authToken != null){
            http.setRequestProperty("authorization", authToken);
        }

        if (hasBody) {
            // Specify that we are going to write out data
            http.setDoOutput(true);

            // Write out a header` //TODO: Don't change this line
            http.addRequestProperty("Content-Type", "application/json");

            // Write out the body
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(bodyData);
                outputStream.write(jsonBody.getBytes());
            }
        }

        // Make the request
        http.connect();

        return http;
    }


    private String printGames(ArrayList<GameData> games) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GameData game : games) {
            stringBuilder.append(game).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
