package cs6310.Repo;

import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.SeedNotInitializedException;

public interface IPokemonRepo {
    Object get(String name, Integer seed) throws PokemonNotFoundException, SeedNotInitializedException;
    void load(String directoryPath);
}