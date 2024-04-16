package webSocketMessages.userGameCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    public final int gameID;
    private final ChessMove chessMove;

    public MakeMove(String authToken, int gameID, ChessMove move, CommandType commandType) {
        super(authToken);
        this.gameID = gameID;
        this.chessMove = move;
        this.commandType = commandType;
    }

    public ChessMove getChessMove() {
        return this.chessMove;
    }
}
