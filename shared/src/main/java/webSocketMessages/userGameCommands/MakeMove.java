package webSocketMessages.userGameCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    public final int gameID;
    private final ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move, CommandType commandType) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.commandType = commandType;
    }

    public ChessMove getMove() {
        return this.move;
    }
}
