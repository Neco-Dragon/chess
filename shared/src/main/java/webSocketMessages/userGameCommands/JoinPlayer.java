package webSocketMessages.userGameCommands;

import chess.ChessGame;


public class JoinPlayer extends UserGameCommand {
    public int gameID;
    public ChessGame.TeamColor playerColor;
    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, CommandType commandType) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.commandType = commandType;
    }
}
