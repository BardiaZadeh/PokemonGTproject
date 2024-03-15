package cs6310;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import cs6310.Exceptions.BattleLostException;
import cs6310.Exceptions.BattleMethodNotFoundException;
import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.PokemonNotInitializedException;
import cs6310.Exceptions.SeedNotInitializedException;
import cs6310.Game.Game;

public class CommandProcessor {
    private Game game;

    public CommandProcessor(Game game) {
        this.game = game;
    }

    public void ProcessCommands(String[] args) {
        var commandLineInput = new Scanner(System.in);
        var delimiter = ",";

        while (true) {
            var wholeInputLine = commandLineInput.nextLine();
            var tokens = wholeInputLine.split(delimiter);

            System.out.println("> " + wholeInputLine);

            if (tokens[0].equals("setseed")) {
                if (tokens.length != 2) {
                    System.out.println("setseed requires 1 argument separated by comma eg. setseed,10");
                    continue;
                }

                try {
                    Integer seed = Integer.parseInt(tokens[1].strip());
                    game.setSeed(seed);
                } catch (NumberFormatException e) {
                    System.out.println(tokens[1] + " must be a valid number");
                    continue;
                }

            } else if (tokens[0].equals("removeseed")) {
                if (tokens.length != 1) {
                    System.out.println("removeseed should have 0 arguments");
                    continue;
                }
                game.removeSeed();

            } else if (tokens[0].equals("displayinfo")) {
                if (tokens.length != 2) {
                    System.out.println("displayinfo requires 1 argument separated by comma eg. displayinfo,pikachu");
                    continue;
                }

                String p1 = tokens[1].strip();
                game.displayInfo(p1);

            } else if (tokens[0].equals("battle")) {
                if (tokens.length != 3) {
                    // System.out.println("battle requires 2 arguments separated by comma eg. battle,charmander,squirtle");
                    System.out.println("Participant size invalid.");
                    continue;
                }

                String p1 = tokens[1].strip();
                String p2 = tokens[2].strip();

                try {
                    game.battle(p1, p2);
                } catch (BattleMethodNotFoundException | SeedNotInitializedException | PokemonNotInitializedException | PokemonNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (BattleLostException e) {
                }
            } else if (tokens[0].equals("stop")) {
                if (tokens.length != 1) {
                    System.out.println("stop should have 0 arguments");
                    continue;
                }
                System.out.println("stop acknowledged");
                break;
            } else if (tokens[0].equals("tournament")) {

                // Make sure there are 2 to the n arguments where n > 1
                double log2 = Math.log(tokens.length-1) / Math.log(2);
                if (tokens.length - 1 < 4 || Math.pow(2, log2) != tokens.length-1) {
                    // System.out.println("tournament should have 2 to the n arguments where n is greater than 1 eg. 4,8,16,32,etc");
                    System.out.println("Participant size invalid.");
                    continue;
                }

                // Collect rest of strings into a list
                List<String> processedTokens = Arrays.stream(tokens)
                        .skip(1)
                        .map(String::trim)
                        .collect(Collectors.toList());

                try {
                    game.tournament(processedTokens);
                } catch (BattleMethodNotFoundException | SeedNotInitializedException | PokemonNotInitializedException | PokemonNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {

            }
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }
}
