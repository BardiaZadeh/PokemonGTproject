package cs6310.Battle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cs6310.Exceptions.BattleLostException;
import cs6310.Exceptions.BattleMethodNotFoundException;

public class Battle {
    private Object p1;
    private Object p2;

    public Battle(Object p1, Object p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void handleInvalidPokemonForfeit(Object pokemon, Object other) throws BattleLostException {
        if (pokemon instanceof String pokeStr) {
            System.out.printf("Name: %s was invalid and has forfeited the battle%n", pokeStr);
            System.out.printf("%s has lost%n", pokeStr);
            if (other instanceof String otherStr) {
                System.out.printf("%s has won the battle%n", otherStr);
            } else {
                System.out.printf("%s has won the battle%n", getNameWithReflection(other));
            }

            throw new BattleLostException(pokeStr + " invalid therefore forfeit", pokeStr);
        }
    }

    private String getNameWithReflection(Object object) {
        String fullName = object.getClass().getName();
        String[] parts = fullName.split("\\.");
        return parts[parts.length - 1];
    }

    public void startBattle() throws BattleLostException, BattleMethodNotFoundException{
        handleInvalidPokemonForfeit(p1, p2);
        handleInvalidPokemonForfeit(p2, p1);
        try {
            Method battleMethod = p1.getClass().getMethod("battle", Object.class, Integer.class);
            battleMethod.invoke(p1, p2, 0);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new BattleMethodNotFoundException(p1.getClass().getName() + " does not have battle method with Object and int argument");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof BattleLostException x) {
                throw x;
            }
        }
    }
}

