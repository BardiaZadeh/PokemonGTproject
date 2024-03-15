package cs6310.Core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.SeedNotInitializedException;
import cs6310.Repo.IPokemonRepo;

public class DisplayInfo {
    private IPokemonRepo pokemonRepo;

    public DisplayInfo(IPokemonRepo pokemonRepo) {
        this.pokemonRepo = pokemonRepo;
    }

    public void displayInfo(String pokemon) {
        try {
            // TODO: check to make sure just setting the seed to 1 is okay
            Object pokeFound = pokemonRepo.get(pokemon, 1);
            reflectDisplayMethod(pokeFound);
        } catch (PokemonNotFoundException | SeedNotInitializedException e) {
            System.out.println("Invalid Pokemon.");
        }
    }

    public void reflectDisplayMethod(Object pokemon) {
        try {
            Method displayMethod = pokemon.getClass().getMethod("DisplayInfo");
            displayMethod.invoke(pokemon);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
