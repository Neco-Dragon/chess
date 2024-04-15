import Repl.Repl;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: ");
        Repl repl = new Repl("http://localhost:8080");
        repl.run();
    }
}