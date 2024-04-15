package webSocketMessages.userGameCommand;

import chess.ChessGame;

public class JoinPlayer {
    private int gameID;
    private ChessGame.TeamColor playerColor;
    public JoinPlayer(int gameID, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
