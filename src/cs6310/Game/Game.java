package cs6310.Game;

import java.util.List;

import cs6310.Battle.Battle;
import cs6310.Battle.BattleBuilder;
import cs6310.Core.DisplayInfo;
import cs6310.Exceptions.BattleLostException;
import cs6310.Exceptions.BattleMethodNotFoundException;
import cs6310.Exceptions.PokemonNotFoundException;
import cs6310.Exceptions.PokemonNotInitializedException;
import cs6310.Exceptions.SeedNotInitializedException;

public class Game {
    private BattleBuilder battleBuilder;
    private Tournament tournament;
    private DisplayInfo displayInfo;

    public Game(BattleBuilder battleBuilder, Tournament tournament, DisplayInfo displayInfo) {
        this.battleBuilder = battleBuilder;
        this.tournament = tournament;
        this.displayInfo = displayInfo;
    }

    public void setSeed(Integer seed) {
        battleBuilder.setSeed(seed);
    }

    public void removeSeed() {
        battleBuilder.removeSeed();
    }

    public void battle(String p1, String p2) throws BattleLostException, PokemonNotFoundException, SeedNotInitializedException, PokemonNotInitializedException, BattleMethodNotFoundException {
        battleBuilder.setP1(p1);
        battleBuilder.setP2(p2);

        Battle battle = battleBuilder.build();

        battle.startBattle();
    }

    public void tournament(List<String> pokemon) throws PokemonNotFoundException, SeedNotInitializedException, PokemonNotInitializedException, BattleMethodNotFoundException {
        tournament.tournament(pokemon);
    }

    public void displayInfo(String pokemon) {
        displayInfo.displayInfo(pokemon);
    }

    public void stop() {

    }
}