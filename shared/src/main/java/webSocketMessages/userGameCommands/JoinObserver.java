package webSocketMessages.userGameCommands;

public class JoinObserver extends UserGameCommand {
    public int gameID;
    public JoinObserver(String authToken, int gameID, CommandType commandType) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = commandType;
    }
}
