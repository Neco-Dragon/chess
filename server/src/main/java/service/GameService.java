package service;

import RequestClasses.InsertGameRequest;
import RequestClasses.JoinGameRequest;
import RequestClasses.ListGamesRequest;
import ResultClasses.InsertGameResult;
import ResultClasses.ListGamesResult;
import chess.ChessGame;
import dataAccess.*;
import model.GameData;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void joinGame(JoinGameRequest request) throws DataNotFoundException, DataAccessException, DataAccessUnauthorizedException {

        gameDAO.joinGame(request.gameID(), request.playerColor(), authDAO.getUsername(request.authToken()));
    }

    public InsertGameResult insertGame(InsertGameRequest request) throws DataAccessException, DataAccessUnauthorizedException {
        authDAO.confirmAuth(request.authToken());
        int gameID = gameDAO.generateNewGameID();
        String gameName = request.gameName();
        ChessGame chessGame = new ChessGame();
        GameData myGameData = new GameData(gameID, null, null, gameName, chessGame);
        gameDAO.insertGame(myGameData);
        return new InsertGameResult(gameID);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException, DataAccessUnauthorizedException {
        authDAO.confirmAuth(request.authToken());
        return new ListGamesResult(gameDAO.listGames());
    }
}
