package webSocketMessages.userGameCommands;

public class JoinObserver extends UserGameCommand {
    public int gameID;
    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
