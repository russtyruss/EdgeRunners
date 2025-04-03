package com.Azcuna.platformer;
import java.util.Random;

public class Lucy extends Character{
    Random random = new Random();
    public Lucy(boolean isPlayer) {
        // Generate the random skill values first
        super("Lucy", 100, 25, isPlayer, 0, 0, 0, 10, 20, 50, 100);  // Default values for skills and mana

        // Now generate random skill values after calling the super constructor
        int skill1 = random.nextInt(10) + 8;   // Skill 1 damage between 5 and 14
        int skill2 = random.nextInt(10) + 5;  // Skill 2 damage between 15 and 34
        int skill3 = random.nextInt(10) + 5;  // Skill 3 damage between 30 and 59

        // Update the skills for this specific David object
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
    }
}
