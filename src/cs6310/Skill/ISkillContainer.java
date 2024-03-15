package cs6310.Skill;

public interface ISkillContainer extends Iterable<Skill> {
    void addSkill(Skill skill);
    Skill choose(int choice);
    int size();
}
