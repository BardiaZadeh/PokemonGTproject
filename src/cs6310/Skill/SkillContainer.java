package cs6310.Skill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SkillContainer implements ISkillContainer {
    private List<Skill> skills;

    public SkillContainer() {
        this.skills = new ArrayList<>();
    }

    @Override
    public void addSkill(Skill skill) {
        skills.add(skill);
        skills.sort(Comparator.comparingInt(Skill::getPower));
    }

    @Override
    public Skill choose(int choice) {
        // The choice parameter is used as an index to select a skill from the sorted list
        if (choice >= 0 && choice < skills.size()) {
            return skills.get(choice);
        }
        return null; // If the choice is out of bounds, return null or throw an exception
    }

    @Override
    public int size() {
        return skills.size();
    }

    @Override
    public Iterator<Skill> iterator() {
        return skills.iterator();
    }
}
