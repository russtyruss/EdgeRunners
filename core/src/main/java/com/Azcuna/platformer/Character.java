package com.Azcuna.platformer;

public class Character {
    private String name;
    private int health;
    private int maxHealth;
    private int maxMana;
    private int attackPower;
    private int skillChoice;
    public int skill1;
    public int skill2;
    public int skill3;
    private int mana;
    private int s1ManaCost;
    private int s2ManaCost;
    private int s3ManaCost;
    private boolean isPlayer;  // Flag to distinguish between player and enemy

    // Constructor to initialize a character (player or enemy)
    public Character(String name, int maxHealth, int attackPower, boolean isPlayer, int skill1, int skill2, int skill3, int s1Mana, int s2Mana, int s3Mana, int maxMana) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;  // Set the health to max initially
        this.attackPower = attackPower;
        this.isPlayer = isPlayer;
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
        s1ManaCost = s1Mana;
        s2ManaCost = s2Mana;
        s3ManaCost = s3Mana;
        this.mana = maxMana;
        this.maxMana = maxMana;
    }

    // Method to attack another character (player or enemy)
    public void attack(Character target) {
        int damage = skill1;

        switch(skillChoice){
            case 1:
                damage = skill1;
                mana-=s1ManaCost;
                break;
            case 2:
                damage = skill2;
                mana-=s2ManaCost;
                break;
            case 3:
                damage = skill3;
                mana-=s3ManaCost;
                break;
        }
        // Randomized attack damage
        target.takeDamage(damage);  // Return the damage dealt
    }

    // Method to take damage
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;  // Prevent the health from going below 0
        }
    }

    // Method to check if the character is alive
    public boolean isAlive() {
        return this.health > 0;
    }

    // Getters and Setters for character attributes
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMana(){
        return mana;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getS1ManaCost(){
        return s1ManaCost;
    }

    public int getS2ManaCost(){
        return s2ManaCost;
    }

    public int getS3ManaCost(){
        return s3ManaCost;
    }


    public boolean isPlayer() {
        return isPlayer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMana(int mana){
        this.mana = mana;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void setSkillChoice(int skillChoice) {
        this.skillChoice = skillChoice;
    }

    public void setPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }
}

