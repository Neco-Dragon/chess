import RequestClasses.LoginRequest;
import RequestClasses.LogoutRequest;
import RequestClasses.RegisterRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Repl {
    private enum LoginState {
        LOGGED_IN,
        LOGGED_OUT
    }
    private ServerFacade s;

    private LoginState loginState = LoginState.LOGGED_OUT;
//    TODO: Add a way to call ServerFacade handler methods.
    public Repl(String serverUrl) {
        s = new ServerFacade();
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
                    case "register" -> s.register(new RegisterRequest(params[0], params[1], params[2]));
                    case "login" -> s.login(new LoginRequest(params[0], params[1]));
                    case "quit" -> s.quit();
                    case "help" -> System.out.println(help());
                    default -> System.out.println("Invalid Command");
                };
            }
            //Main Logged In Loop
            while (loginState == LoginState.LOGGED_IN){
                var tokens = scanner.nextLine().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                switch (cmd) {
                    case "logout" -> s.logout(new LogoutRequest(params[0], params[1], params[2]));
                    case "createGame" -> s.createGame(params);
                    case "listGames" -> s.listGames(params);
                    case "joinGame" -> s.joinGame(params);
                    case "quit" -> s.quit();
                    case "help" -> System.out.println(help());
                    default -> System.out.println("Invalid Command");
                };
            }
        }
    }

    public String help(){
        if (this.loginState == LoginState.LOGGED_IN){
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
