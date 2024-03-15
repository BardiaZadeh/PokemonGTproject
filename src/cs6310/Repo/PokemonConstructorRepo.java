package cs6310.Repo;

import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.SeedNotInitializedException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PokemonConstructorRepo implements IPokemonRepo {
    // Stores key value pairs of PokemonNames to PokemonObjects
    final private Map<String, Constructor<?>> pokemonMap;

   public PokemonConstructorRepo() {
        pokemonMap = new HashMap<>();
        // this.load(directoryPath);
    }

    // This method is only used by BattleBuilder for getting the Pokemon objects for battle
    @Override
    public Object get(String name, Integer seed) throws PokemonNotFoundException, SeedNotInitializedException {
        if (!pokemonMap.containsKey(name.toLowerCase())) {
            throw new PokemonNotFoundException("Pokemon : " + name + " is not found in Pokemon Repo");
        }

        if (seed == null) {
            throw new SeedNotInitializedException("Must initialize seed first");
        }

        try {
            Constructor<?> constructor = pokemonMap.get(name.toLowerCase());
            Object pokemon = (Object) constructor.newInstance(seed);
            return pokemon;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new PokemonNotFoundException("Pokemon : " + name + " cannot be instantiated, has invalid signature");
        }
    }

    // Responsible for loading all Pokemon child classes from the input directoryPath
    @Override
    public void load(String directoryPath) {
        final String path = directoryPath;
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            final JarFile jar;
            try {
                jar = new JarFile(jarFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
//                System.out.println(name);
                if (name.contains(path) && name.length() > path.length()) { //filter according to the path
                    try {
                        String className = name.replace(".class", "");
                        className = className.replace("cs6310/Pokemon/", "");
                        Class<?> pokemonClass = Class.forName("cs6310.Pokemon." + className);

                        // Make sure the loaded class extends Object
                        if (Object.class.isAssignableFrom(pokemonClass)) {
                            Constructor<?> constructor = pokemonClass.getDeclaredConstructor(Integer.class);
                            constructor.setAccessible(true);
                            //Initialise all Pokemons in cs6310.Pokemon package with seed as 0
                            pokemonMap.put(className.toLowerCase(), constructor);
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
//                        e.printStackTrace();
                    }
                }
            }
            try {
                jar.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else { // Run locally in IDE
            String localPath = "src/" + directoryPath;
            File folder = new File(localPath);
            File[] javaFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".java"));
            if (javaFiles == null) {
                folder = new File(directoryPath);
                javaFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".java"));
            }

            if (javaFiles != null) {
                for (File javaFile : javaFiles) {
                    try {
                        String className = javaFile.getName().replace(".java", "");
                        Class<?> pokemonClass = Class.forName("cs6310.Pokemon." + className);

                        // Make sure the loaded class extends Object
                        if (Object.class.isAssignableFrom(pokemonClass)) {
                            Constructor<?> constructor = pokemonClass.getDeclaredConstructor(Integer.class);
                            constructor.setAccessible(true);
                            //Initialise all Pokemons in cs6310.Pokemon package with seed as 0
                            pokemonMap.put(className.toLowerCase(), constructor);
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        // e.printStackTrace();
                    }
                }
            }
        }
    }

    public void printPokemon() {
        pokemonMap.forEach((name, pokemon) -> {
            System.out.println("Found pokemon : " + name + " with value : " + pokemon.toString());
        });
    }
}
