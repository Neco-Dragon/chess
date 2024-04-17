package Repl;

import RequestClasses.*;
import ServerFacade.ServerFacade;
import Websocket.WebSocketFacade;
import chess.ChessGame;

import java.util.Arrays;
import java.util.Scanner;
import ServerFacade.*;

public class Repl {

    private enum ReplState {
        LOGGED_IN,
        LOGGED_OUT,
        IN_GAME
    }
    private final ServerFacade facade;
    private WebSocketFacade socketFacade;

    private Repl.Repl.ReplState replState = ReplState.LOGGED_OUT;
    private Boolean runProgram = Boolean.TRUE;
    private final Scanner scanner = new Scanner(System.in);

    //TODO: Save the most recent loaded game;

    public Repl(String serverUrl) throws Exception {
        this.socketFacade = new WebSocketFacade();
        this.facade = new ServerFacade(8080);
    }

    public void run() throws Exception {
        while (runProgram) {
            System.out.println("Welcome to Chess 240!");
            System.out.println(help());

            //Main Logged Out Loop
            while (replState == ReplState.LOGGED_OUT){
                loggedOutLoop();
            }
            //Main Logged In Loop
            while (replState == ReplState.LOGGED_IN){
                loggedInLoop();
            }
            //Game UI Loop
            while (replState == ReplState.IN_GAME){
                inGameLoop();
            }
        }
    }

    private static ChessGame.TeamColor getTeamColor(String teamColorString) throws FacadeException {
        ChessGame.TeamColor teamColor;
        if (teamColorString.equalsIgnoreCase("white")){
            teamColor = ChessGame.TeamColor.WHITE;
        } else if (teamColorString.equalsIgnoreCase("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        }
        else {
            throw new FacadeException("Not a valid team color");
        }
        return teamColor;
    }

    public String help(){
        if (this.replState == ReplState.LOGGED_OUT){
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
        if (this.replState == ReplState.LOGGED_IN){
            return """
        Please type one of the following commands:
    
        logout
            - Logs out from the current session.
        createGame <gameName>
            - Creates a new game with the given name.
        listGames
            - Displays a list of available games.
        joinGame <gameID> <WHITE/BLACK>[optional]
            - Joins the game with the specified ID.
            - if just a gameID is supplied, joins game as observer.
        quit
            - Exits the application.
        help
            - Print this menu again.
    """;
        }
        else{
            return """
        Please type one of the following commands:
    
        highlightLegalMoves <(SquareContainingPiece)>
            - Shows the legal moves of the chess piece on the current square as if it were that players turn.
        makeMove <(startingSquare)> <(EndingSquare)>
            - Performs the desired move on the chess board and updates the database accordingly.
        redrawChessBoard
            - Prints out the current state of the board. 
            - Does not show who's turn it is or if the game is over.
        resign
            - Resigns and ends the game.
            - Does not make you leave.
        leave
            - Leave the gave you're in.
            - Does not make you forfeit.
        help
            - Print this menu again.
    """;
        }
    }

    private void loggedInLoop() throws Exception {
        var tokens = scanner.nextLine().split(" ");

        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case ("logout"):
                try{
                    facade.logout(new LogoutRequest(facade.authToken));
                    replState = ReplState.LOGGED_OUT;
                    System.out.println("Logout successful. You are now logged out.");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case ("createGame"):
                try {
                    facade.createGame(new CreateGameRequest(params[0]));
                    System.out.println("Game creation successful. Type listGames to see your game. Type joinGame to join.");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case ("listGames"):
                try {
                    facade.listGames(new ListGamesRequest(facade.authToken));
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case ("joinGame"):
                try {
                    if (params.length == 0){
                        System.out.println("Please enter at least a gameID");
                        break;
                    }
                    int id = Integer.parseInt(params[0]);
                    if (params.length == 1){ //Join as observer
                        facade.joinGame(new JoinGameRequest(null, id));
                        System.out.println("Joined as an observer successfully.");
                        this.socketFacade = new WebSocketFacade();
                        //send a join game
                        this.replState = ReplState.IN_GAME;
                        break;
                    }
                    ChessGame.TeamColor teamColor = getTeamColor(params[1]);
                    facade.joinGame(new JoinGameRequest(teamColor, id));
                    System.out.println("Game Join successful.");
                    this.replState = ReplState.IN_GAME;
                    break;
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case ("quit"):
                ServerFacade.quit();
                replState = ReplState.LOGGED_OUT;
                runProgram = Boolean.FALSE;
                break;
            case ("help"):
                System.out.println(help());
                break;
            default: System.out.println("Invalid Command");
        }
    }

    private void loggedOutLoop() throws Exception {
        String[] tokens = scanner.nextLine().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case "register":
                try{
                    facade.register(new RegisterRequest(params[0], params[1], params[2]));
                    replState = ReplState.LOGGED_IN;
                    System.out.println("Registration successful. You are now logged in.");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case "login":
                try{
                    facade.login(new LoginRequest(params[0], params[1]));
                    replState = ReplState.LOGGED_IN;
                    System.out.println("Login successful. You are now logged in.");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case "quit":
                ServerFacade.quit();
                replState = ReplState.LOGGED_OUT;
                runProgram = Boolean.FALSE;
                break;
            case "help":
                System.out.println(help());
                break;
            default:
                System.out.println("Invalid Command");
                break;
        }
    }

    private void inGameLoop() throws Exception{
        var tokens = scanner.nextLine().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd){
            case ("help"):
                System.out.println(help());
                break;
            case ("redrawChessBoard"):
                try{
                    socketFacade.(new LogoutRequest(facade.authToken()));
                    replState = ReplState.LOGGED_OUT;
                    System.out.println("Logout successful. You are now logged out.");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case ("leave"):
                break;
            case ("makeMove"):
                break;
            case ("resign"):
                break;
            case ("highlightLegalMoves"):
                break; //This is a local operation
            default: System.out.println("Invalid Command");
        }
    }
}
