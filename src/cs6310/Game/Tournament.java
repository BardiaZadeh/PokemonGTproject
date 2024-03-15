package cs6310.Game;

import java.util.ArrayList;
import java.util.List;

import cs6310.Battle.Battle;
import cs6310.Battle.BattleBuilder;
import cs6310.Exceptions.BattleLostException;
import cs6310.Exceptions.BattleMethodNotFoundException;
import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.PokemonNotInitializedException;
import cs6310.Exceptions.SeedNotInitializedException;

public class Tournament {
    private BattleBuilder battleBuilder;
    private int round;

    public Tournament(BattleBuilder battleBuilder) {
        this.battleBuilder = battleBuilder;
        this.round = 1;
    }

    private List<String> tournamentRound(List<String> pokemon) throws PokemonNotFoundException, SeedNotInitializedException, PokemonNotInitializedException, BattleMethodNotFoundException {
        List<String> winners = new ArrayList<>();

        for (int i = 0; i < pokemon.size() - 1; i+=2) {
            String p1 = pokemon.get(i);
            String p2 = pokemon.get(i+1);

            try {
                battleBuilder.setP1(p1);
                battleBuilder.setP2(p2);
                Battle battle = battleBuilder.build();
                System.out.printf("Starting tournament round %d with %s and %s%n", round, p1, p2);
                battle.startBattle();

            } catch (BattleLostException e) {
                if (e.getLosingPokemonName().equalsIgnoreCase(p1)) {
                    String winner = p2;
                    System.out.printf("%s has won round %d%n", winner, round);
                    winners.add(winner);
                } else {
                    String winner = p1;
                    System.out.printf("%s has won round %d%n", winner, round);
                    winners.add(winner);
                }
            }
            round++;
        }

        return winners;
    }

    public void tournament(List<String> pokemon) throws PokemonNotFoundException, SeedNotInitializedException, PokemonNotInitializedException, BattleMethodNotFoundException {
        round = 1;
        while (pokemon.size() > 1) {
            pokemon = tournamentRound(pokemon);
        }

        String winner = pokemon.get(0);
        System.out.printf("%s has won the tournament%n", winner);
    }
}
