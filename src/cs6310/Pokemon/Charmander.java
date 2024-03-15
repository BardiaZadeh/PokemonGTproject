package cs6310.Pokemon;

import cs6310.Core.Pokemon;
import cs6310.Skill.ISkillContainer;
import cs6310.Skill.SkillContainer;
import cs6310.Skill.Skill;

public class Charmander extends Pokemon {
    private String name;
    private ISkillContainer attackSkills;
    private ISkillContainer defendSkills;

    public Charmander(Integer seed) {
        super(seed);
        name = "Charmander";
        attackSkills = initAttackSkills();
        defendSkills = initDefenseSkills();
   }

    private ISkillContainer initAttackSkills() {
        var container = new SkillContainer();

        container.addSkill(new Skill("Attack", 1));
        container.addSkill(new Skill("Scratch", 2));
        container.addSkill(new Skill("Ember", 3));
        container.addSkill(new Skill("Flamethrower", 6));


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
