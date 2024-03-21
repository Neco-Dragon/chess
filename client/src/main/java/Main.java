import chess.*;
import server.Server;
import ui.EscapeSequences;
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: ");
        Server server = new Server(); //TODO: Where do I need to stick my server?
        Repl repl = new Repl("http://localhost:8080"); //TODO: is this right?
        repl.run();
    }
}