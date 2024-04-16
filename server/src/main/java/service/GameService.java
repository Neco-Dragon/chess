package service;

import RequestClasses.CreateGameRequest;
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
    private int nextGameid;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        nextGameid = (gameDAO.listGames()).size();
    }

    public void joinGame(String authToken, JoinGameRequest request) throws BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException {
        if (authDAO.getAuth(authToken) == null || authDAO.getUsername(authToken) == null){
            throw new UnauthorizedException();
        }
        GameData gameData = gameDAO.getGameData(request.gameID());
        if (gameData == null){
            throw new BadRequestException();
        }
        if (gameData.whiteUsername() == null && gameData.blackUsername() == null){
            if (request.playerColor() == null){
                return;
            }
        }
        if (request.playerColor() == ChessGame.TeamColor.WHITE){
            if (gameData.whiteUsername() != null){
                throw new AlreadyTakenException();
            }
        }
        else if (request.playerColor() == ChessGame.TeamColor.BLACK) {
            if (gameData.blackUsername() != null){
                throw new AlreadyTakenException();
            }
        }
        gameDAO.joinGame(request.gameID(), request.playerColor(), authDAO.getUsername(authToken));
    }

    public InsertGameResult insertGame(String authToken, CreateGameRequest request) throws DataAccessException, UnauthorizedException, AlreadyTakenException, BadRequestException {
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        int gameID = nextGameid; nextGameid++;
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
