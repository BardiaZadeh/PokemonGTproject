package cs6310.Skill;

public class Skill {
    private String name;
    private int power;

    public Skill(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }
}
