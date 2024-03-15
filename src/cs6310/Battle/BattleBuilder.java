package cs6310.Battle;

import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.PokemonNotInitializedException;
import cs6310.Exceptions.SeedNotInitializedException;
import cs6310.Repo.IPokemonRepo;

/**
 * Responsible for keeping track of the seed value before any battle has started
 * Seed is null when unitialized
 */
public class BattleBuilder {
    private Integer seed;
    private IPokemonRepo pokemonRepo;
    private Object p1;
    private Object p2;

    public BattleBuilder(IPokemonRepo pokemonRepo) {
        this.pokemonRepo = pokemonRepo;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public int getSeed() {
        return this.seed;
    }

    public void removeSeed() {
        this.seed = null;
    }

    public void setP1(String name) throws SeedNotInitializedException {
        if (seed == null) {
            throw new SeedNotInitializedException("Seed not initialized");
        }
        try {
            p1 = pokemonRepo.get(name, seed);
        } catch (PokemonNotFoundException e) {
            p1 = name;
        }
    }

    public void setP2(String name) throws SeedNotInitializedException {
        if (seed == null) {
            throw new SeedNotInitializedException("Seed not initialized");
        }
        try {
            p2 = pokemonRepo.get(name, seed + 1);
        } catch (PokemonNotFoundException e) {
            p2 = name;
        }
    }

    public String getP1Name() {
        return getNameWithReflection(p1);
    }

    public String getP2Name() {
        return getNameWithReflection(p2);
    }

    private String getNameWithReflection(Object object) {
        String fullName = object.getClass().getName();
        String[] parts = fullName.split("\\.");
        return parts[parts.length - 1];
    }

    public Battle build() throws SeedNotInitializedException, PokemonNotInitializedException {
        if (seed == null) {
            throw new SeedNotInitializedException("Seed must be initialized first");
        }

        if (p1 == null || p2 == null) {
            throw new PokemonNotInitializedException("Must set pokemon p1 and pokemon p2 first");
        }

        return new Battle(p1, p2);
    }
}
