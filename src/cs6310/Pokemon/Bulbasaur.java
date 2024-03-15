package cs6310.Pokemon;

import cs6310.Core.Pokemon;
import cs6310.Skill.ISkillContainer;
import cs6310.Skill.SkillContainer;
import cs6310.Skill.Skill;

public class Bulbasaur extends Pokemon {
    private String name;
    private ISkillContainer attackSkills;
    private ISkillContainer defendSkills;

    public Bulbasaur(Integer seed) {
        super(seed);
        name = "Bulbasaur";
        attackSkills = initAttackSkills();
        defendSkills = initDefenseSkills();
    }

    private ISkillContainer initAttackSkills() {
        var container = new SkillContainer();

        container.addSkill(new Skill("Tackle", 1));
        container.addSkill(new Skill("Vine Whip", 2));
        container.addSkill(new Skill("Razor Leaf", 3));
        container.addSkill(new Skill("Leaf Storm", 6));
        

        return container;
    }

    private ISkillContainer initDefenseSkills() {
        var container = new SkillContainer();

        container.addSkill(new Skill("Endure", 1));
        container.addSkill(new Skill("Block", 2));
        container.addSkill(new Skill("Protect", 3));

        return container;
    }


    @Override
    public ISkillContainer getAttackSkills() {
        return attackSkills;
    }

    @Override
    public ISkillContainer getDefenseSkills() {
        return defendSkills;
    }

    @Override
    public String getName() {
        return name;
    }
}
