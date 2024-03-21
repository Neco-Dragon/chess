import RequestClasses.*;
import chess.ChessGame;

import java.util.Arrays;
import java.util.Scanner;

public class Repl {

    private enum LoginState {
        LOGGED_IN,
        LOGGED_OUT
    }
    private ServerFacade facade;

    private LoginState loginState = LoginState.LOGGED_OUT;

    public Repl(String serverUrl) {
        facade = new ServerFacade(8080); //TODO: is this the right port?
    }

    public void run() throws Exception {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to Chess 240!");
            System.out.println(help());

            //Main Logged Out Loop
            while (loginState == LoginState.LOGGED_OUT){
                String[] tokens = scanner.nextLine().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                switch (cmd) {
                    case "register":
                        facade.register(new RegisterRequest(params[0], params[1], params[2]));
//                        if facade.errorMessage
//                        this.loginState = LoginState.LOGGED_IN;
                        break;
                    case "login":
                        facade.login(new LoginRequest(params[0], params[1]));
                        break;
                    case "quit":
                        facade.quit();
                        break;
                    case "help":
                        System.out.println(help());
                        break;
                    default:
                        System.out.println("Invalid Command");
                        break;
                };
            }
            //Main Logged In Loop
            while (loginState == LoginState.LOGGED_IN){
                var tokens = scanner.nextLine().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                switch (cmd) {
                    case "logout" -> facade.logout(new LogoutRequest(facade.authToken));
                    case "createGame" -> facade.createGame(new CreateGameRequest(params[0]));
                    case "listGames" -> facade.listGames(new ListGamesRequest(facade.authToken));

                    //TODO: cast the joinGame inputs to valid join game request objects
                    case "joinGame" -> facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1)); //TODO: Change filler values
                    case "quit" -> facade.quit();
                    case "help" -> System.out.println(help());
                    default -> System.out.println("Invalid Command");
                };
            }
        }
    }

    public String help(){
        if (this.loginState == LoginState.LOGGED_OUT){
            return """
        Please type one of the following commands:
    
        register <username> <password> <email>
            - Registers a new user with the provided username, password, and email.
        login <username> <password>
            - Logs in with the specified username and password.
        quit
            - Exits the application.
        help
            - Print this menu again.
    """;

        }
        else{
            return """
        Please type one of the following commands:
    
        logout
            - Logs out from the current session.
        createGame <gameName>
            - Creates a new game with the given name.
        listGames
            - Displays a list of available games.
        joinGame <gameID>
            - Joins the game with the specified ID.
        quit
            - Exits the application.
        help
            - Print this menu again.
    """;
        }
    }


}
