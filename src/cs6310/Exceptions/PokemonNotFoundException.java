package cs6310.Exceptions;

public class PokemonNotFoundException extends Exception {
    public PokemonNotFoundException(String msg) {
        super(msg);
    }
}
