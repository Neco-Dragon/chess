package webSocketMessages.userGameCommands;

public class Leave extends UserGameCommand {
    public int gameID;
    public Leave(String authToken, int gameID, CommandType commandType) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = commandType;
    }
}
