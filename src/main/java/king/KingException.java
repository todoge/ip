package king;

public class KingException extends Exception {

    public final String message;
    public final Throwable error;

    /**
     * Create a KingException generated from the King Program.
     *
     * @param message message for the exception generated.
     * @param error   throwable error resulting in KingException generated.
     * @return KingException
     */
    public KingException(String message, Throwable error) {
        this.error = error;
        this.message = message;
    }
}
