package webSocketMessages.userGameCommands;

public class Resign extends UserGameCommand{
    public int gameID;
    public Resign(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
