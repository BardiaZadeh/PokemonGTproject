package cs6310.Core;

import cs6310.BattleState.BattleStateFactory;
import cs6310.Exceptions.BattleLostException;
import cs6310.Skill.ISkillContainer;
import cs6310.Skill.Skill;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * Template pattern base class for all Pokemon
 */
public abstract class Pokemon {
    public static final int DEFAULT_MAX_HEALTH = 25;

    private int health;
    private int maxHealth;
    private Skill lastDefense;
    private Random rng;

    public Pokemon(Integer seed) {
        this.maxHealth = DEFAULT_MAX_HEALTH;
        this.health = this.maxHealth;
        setSeed(seed);
    }

    // TODO: Check that battle() instead of Battle() is still okay this time around
    public void battle(Object pokemon, Integer damage) throws BattleLostException {
        takeDamage(pokemon, damage);

        if (isFainted()) {
            String loserPokemon = getNameWithReflection(this);
            String winnerPokemon = getNameWithReflection(pokemon);
            System.out.printf("%s has lost%n", loserPokemon);
            System.out.printf("%s has won the battle%n", winnerPokemon);
            throw new BattleLostException(loserPokemon + " has lost", loserPokemon);
        }

        doDamage(pokemon);
    }

    public void DisplayInfo() {
        System.out.printf("Pokemon: %s has %d hp%n", getNameWithReflection(this), health);
        System.out.println("Attack Skills:");
        for (Skill skill : getAttackSkills()) {
            System.out.printf("Name: %s Damage: %d%n", skill.getName(), skill.getPower());
        }
        System.out.println("Defense Skills:");
        for (Skill skill : getDefenseSkills()) {
            System.out.printf("Name: %s Defense: %d%n", skill.getName(), skill.getPower());
        }
    }

    private void otherBattle(Object pokemon, Integer damage) throws BattleLostException {
        try {
            Method otherBattle = pokemon.getClass().getMethod("battle", Object.class, Integer.class);
            otherBattle.invoke(pokemon, this, damage);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof BattleLostException x) {
                throw x;
            }
        }
    }

    private void doDamage(Object pokemon) throws BattleLostException {

        if (shouldAttack()) {
            Skill attack = chooseAttack();
            int damage = attack.getPower();

            System.out.printf("%s is attacking with %s for %d damage to %s%n", getNameWithReflection(this), attack.getName(), damage, getNameWithReflection(pokemon));
            removeDefense();
            otherBattle(pokemon, damage);
        }
        else {
            Skill defense = chooseDefense();

            System.out.printf("%s is attempting to defend with %s%n", getNameWithReflection(this), defense.getName());
            setDefense(defense);
            otherBattle(pokemon, 0);
        }
    }
 
    private void takeDamage(Object pokemon, Integer damage) {
        if (damage == 0) {
            return;
        }

        int trueDamage = damage;

        if (lastDefense != null) {
            int damageReduction = lastDefense.getPower();
            trueDamage = Math.max(trueDamage - damageReduction, 0);
            System.out.printf("%s successfully reduced %s's damage by %d with %s%n", getNameWithReflection(this), getNameWithReflection(pokemon), damageReduction, lastDefense.getName());
        }

        health = Math.max(health - trueDamage, 0);

        System.out.printf("%s has received %d dmg, remaining hp is %d%n", getNameWithReflection(this), trueDamage, health);
    }

    private void setDefense(Skill defense) {
        lastDefense = defense;
    }

    private void removeDefense() {
        lastDefense = null;
    }

    private Skill chooseAttack() {
        ISkillContainer attackSkills = getAttackSkills();

        int choice = rng.nextInt(attackSkills.size());

        return attackSkills.choose(choice);
    }

    private Skill chooseDefense() {
        ISkillContainer defenseSkills = getDefenseSkills();

        int choice = rng.nextInt(defenseSkills.size());

        return defenseSkills.choose(choice);
    }

    private boolean shouldAttack() {
        var state = BattleStateFactory.getBattleState(health, maxHealth);
        return state.shouldAttack(rng);
    }

    private boolean isFainted() {
        return health <= 0;
    }

    private void setSeed(Integer seed) {
        if (seed == null) {
            throw new IllegalArgumentException("Seed cannot be null");
        }
        this.rng = new Random(seed);
    }

    private String getNameWithReflection(Object object) {
        String fullName = object.getClass().getName();
        String[] parts = fullName.split("\\.");
        return parts[parts.length - 1];
    }

    protected abstract ISkillContainer getAttackSkills();
    protected abstract ISkillContainer getDefenseSkills();
    protected abstract String getName();
}

