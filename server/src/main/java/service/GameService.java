package service;

import RequestClasses.InsertGameRequest;
import RequestClasses.JoinGameRequest;
import RequestClasses.ListGamesRequest;
import ResultClasses.InsertGameResult;
import ResultClasses.ListGamesResult;
import chess.ChessGame;
import Exceptions.*;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void joinGame(String authToken, JoinGameRequest request) throws BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException {
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        gameDAO.getGame(request.gameID());
        if (authDAO.getUsername(authToken) == null){
            throw new UnauthorizedException();
        }
        gameDAO.joinGame(request.gameID(), request.playerColor(), authDAO.getUsername(authToken));
    }

    public InsertGameResult insertGame(String authToken, InsertGameRequest request) throws DataAccessException, UnauthorizedException, AlreadyTakenException, BadRequestException {
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        int gameID = gameDAO.generateNewGameID();
        String gameName = request.gameName();
        ChessGame chessGame = new ChessGame();
        GameData myGameData = new GameData(gameID, null, null, gameName, chessGame);
        gameDAO.insertGame(myGameData);
        return new InsertGameResult(gameID);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException, UnauthorizedException, BadRequestException {
        if (authDAO.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException();
        }
        return new ListGamesResult(gameDAO.listGames());
    }
}
