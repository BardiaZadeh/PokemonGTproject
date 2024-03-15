package cs6310.Pokemon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cs6310.Exceptions.BattleLostException;

class GeodudeSkill {
    public String Name;
    public Integer Damage;

    public Integer Defense;
}

class GeodudeAttackComparator implements java.util.Comparator<GeodudeSkill> {
    @Override
    public int compare(GeodudeSkill a, GeodudeSkill b) {
        return a.Damage - b.Damage;
    }
}

class GeodudeDefenseComparator implements java.util.Comparator<GeodudeSkill> {
    @Override
    public int compare(GeodudeSkill a, GeodudeSkill b) {
        return a.Defense - b.Defense;
    }
}

public class Geodude {
    protected int _maxHp;
    protected int _hp;
    protected List<GeodudeSkill> _attackSkills = new ArrayList<>();
    protected List<GeodudeSkill> _defenseSkills = new ArrayList<>();
    private GeodudeSkill lastAction;
    protected Random _classRandom = new Random();
    protected Integer _seed = null;

    protected String _name;

    public Geodude() {
        Setup(null);
    }

    public Geodude(Integer seed) {
        Setup(seed);
    }

    private void Setup(Integer seed) {
        _hp = 25;
        _maxHp = _hp;
        _name = "Geodude";
        //https://gaming.stackexchange.com/questions/260505/how-do-pokemon-stats-relate-to-attack-power
        //https://pokemondb.net/pokedex/
        _attackSkills.add(new GeodudeSkill() {{
            Damage = 1;
            Name = "Tackle";
        }});
        _attackSkills.add(new GeodudeSkill() {{
            Damage = 2;
            Name = "Rock Throw";
        }});
        _attackSkills.add(new GeodudeSkill() {{
            Damage = 3;
            Name = "Earth Quake";
        }});
        _attackSkills.add(new GeodudeSkill() {{
            Damage = 6;
            Name = "Rock Slide";
        }});

        _defenseSkills.add(new GeodudeSkill() {{
            Defense = 1;
            Name = "Endure";
        }});
        _defenseSkills.add(new GeodudeSkill() {{
            Defense = 2;
            Name = "Block";
        }});
        _defenseSkills.add(new GeodudeSkill() {{
            Defense = 3;
            Name = "Protect";
        }});

        if (seed != null) {
            _seed = seed;
            _classRandom.setSeed(_seed);
        }
    }

    public void battle(Object callingClass, Integer damage)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            BattleLostException, InstantiationException, IllegalAccessException {

        if (callingClass != null && damage != null && damage > 0) {
            var dmg = damage;

            if (lastAction != null && _defenseSkills.contains(lastAction)) {
                System.out.println(_name + " successfully reduced " + callingClass.getClass().getSimpleName() + "'s damage by " + lastAction.Defense.toString() + " with " + lastAction.Name);
                dmg -= lastAction.Defense;

                if (dmg < 0) {
                    dmg = 0;
                }
            }

            _hp -= dmg;

            if (_hp < 0) {
                _hp = 0;
            }
            System.out.println(_name + " has received " + String.valueOf(dmg) + " dmg, remaining hp is " + String.valueOf(_hp));
        }

        if (_hp > 0) {
            // 0 based, from 0 to 9 is 10 total choices.
            var attackOrDefendNumber = _classRandom.nextInt(10);
            var shouldAttack = false;

            if (((double) _hp / (double) _maxHp) >= 0.7 && attackOrDefendNumber >= 2) {
                shouldAttack = true;
            } else if (((double) _hp / (double) _maxHp) >= 0.3 && attackOrDefendNumber >= 5) {
                shouldAttack = true;
            } else if (attackOrDefendNumber >= 7) {
                shouldAttack = true;
            }

            if (shouldAttack) {
                var attackSelection = _classRandom.nextInt(_attackSkills.size());
                Collections.sort(_attackSkills, new GeodudeAttackComparator());
                var attack = _attackSkills.get(attackSelection);
                lastAction = attack;

                System.out.println(_name + " is attacking with " + attack.Name + " for " + Integer.toString(attack.Damage) + " damage to " + callingClass.getClass().getSimpleName());

                var args = new Object[]{
                        //battle
                        new Object() {
                        }.getClass().getEnclosingMethod().getName(),
                        // who to fight
                        callingClass,
                        // you
                        this,
                        attack.Damage
                };

                ProcessBattleCommand(args, false);
            } else {
                var defenseSelection = _classRandom.nextInt(_defenseSkills.size());
                Collections.sort(_defenseSkills, new GeodudeDefenseComparator());
                var defense = _defenseSkills.get(defenseSelection);
                lastAction = defense;

                System.out.println(_name + " is attempting to defend with " + defense.Name);

                var args = new Object[]{
                        //battle
                        new Object() {
                        }.getClass().getEnclosingMethod().getName(),
                        // who to fight
                        callingClass,
                        // you
                        this,
                        0
                };

                ProcessBattleCommand(args, false);
            }
        } else {
            System.out.println(_name + " has lost");
            System.out.println(callingClass.getClass().getSimpleName() + " has won the battle");
            throw new BattleLostException("I have Lost", _name);
        }
    }

    public void DisplayInfo() {
        System.out.println("Pokemon: " + _name + " has " + _maxHp + " hp");
        System.out.println("Attack Skills:");

        Collections.sort(_attackSkills, new GeodudeAttackComparator());

        for (var i = 0; i < _attackSkills.size(); i++) {
            var skill = _attackSkills.get(i);
            System.out.println("Name: " + skill.Name + " Damage: " + skill.Damage.toString());
        }

        Collections.sort(_defenseSkills, new GeodudeDefenseComparator());

        System.out.println("Defense Skills:");
        for (var i = 0; i < _defenseSkills.size(); i++) {
            var skill = _defenseSkills.get(i);
            System.out.println("Name: " + skill.Name + " Damage: " + skill.Defense.toString());
        }
    }

    public void ProcessBattleCommand(Object[] args, Boolean isStart)
            throws ClassNotFoundException, NoSuchMethodException, BattleLostException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        var packageName = "cs6310.Pokemon.";
        var className = packageName + (isStart ? args[1] : args[1].getClass().getSimpleName());
        var c = Class.forName(className);
        var methods = c.getMethods();

        for (Method m : methods) {
            String name = m.getName();

            if (name.equalsIgnoreCase(args[0].toString().toLowerCase()) && Modifier.isPublic(m.getModifiers())) {
                var ins = getInstance(className, args, isStart);

                if (isStart) {
                    var className2 = packageName + args[2];
                    var ins2 = getInstance(className2, args, true);
                    args = new Object[]{args[0], args[1], ins2};
                }

                var params = getParams(m, args);

                try {
                    m.invoke(ins, params);
                } catch (InvocationTargetException exception) {
                    var target = exception.getTargetException();
                    if (target instanceof BattleLostException) {
                       throw new BattleLostException(exception.getMessage(), ((BattleLostException) target).getLosingPokemonName());
                    } else {
                        throw exception;
                    }
                }
            }
        }
    }
    private Object getInstance(String className, Object[] args, Boolean isStart)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        if (!isStart && args[1] != null) {
            return args[1];
        }

        var c = Class.forName(className);
        var i = _seed != null
                ? c.getConstructor(Integer.class)
                : c.getConstructor();

        return _seed != null
                ? i.newInstance(_seed)
                : i.newInstance();
    }

    private static Object[] getParams(Method m, Object[] args)
            throws ClassNotFoundException {
        var p = m.getParameters();
        var params = new Object[p.length];
        var counter = 0;

        for (Parameter param : p) {
            var type = param.getType();
            var offset = 2;
            if (args.length - 1 >= counter + offset) {
                params[counter] = Class.forName(type.getName()).cast(args[counter + offset]);
            } else {
                params[counter] = null;
            }
            counter++;
        }

        return params;
    }
}
