package webSocketMessages.userGameCommands;

public class Leave extends UserGameCommand {
    public int gameID;
    public Leave(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
