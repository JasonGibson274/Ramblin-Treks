package pathing_svc;

public class NoPathException extends RuntimeException {
    public NoPathException(String message) {
        super("No path found " + message);
    }
}
