import cs6310.Battle.BattleBuilder;
import cs6310.Core.DisplayInfo;
import cs6310.CommandProcessor;
import cs6310.Game.Game;
import cs6310.Game.Tournament;
import cs6310.Repo.PokemonConstructorRepo;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the thunder dome!");

        var pokemonRepo = new PokemonConstructorRepo();
        pokemonRepo.load("cs6310/Pokemon/");

        var battleBuilder = new BattleBuilder(pokemonRepo);
        var tournament = new Tournament(battleBuilder);
        var displayInfo = new DisplayInfo(pokemonRepo);
        var game = new Game(battleBuilder, tournament, displayInfo);

        var methodCaller = new CommandProcessor(game);
        methodCaller.ProcessCommands(args);
    }
}
