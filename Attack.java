/*
 * File: Attack.java
 * Author: David Hui
 * Description: Represents a Pokemon's attack
 */
public class Attack {
    private final String name; // Name of the attack
    private final int energy; // Amount of energy needed for the attack
    private final int damage; // Amount of damage of the attack
    private final String modifier; // Modifier of the attack
    
    public Attack(String name, int energy, int damage, String modifier){
        // Set the values according to those passed to the constructor
        this.name = name;
        this.energy = energy;
        this.damage = damage;
        this.modifier = modifier;
        LevelLogger.log(this.name);
        LevelLogger.log(this.energy);
        LevelLogger.log(this.damage);
        LevelLogger.log(this.modifier);
    }

    /**
     * Gets the amount of energy need to perform the attack
     * @return the amount of energy need to perform the attack
     */
    public int getEnergy() {
        return this.energy;
    }

    /**
     * Gets the name of the attack
     * @return the name of the attack
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the modifier of the attack
     * @return the modifier of the attack
     */
    public String getModifier() {
        return this.modifier;
    }

    /**
     * Gets the damage of the attack
     * @return the damage of the attack
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Returns a String representation of the Attack for display
     * @return a String representation of the Attack
     */
    public String toPrettyString(){
        // Assemble all information
        StringBuilder temp = new StringBuilder(String.format("%s (%d damage, %d energy)", this.name, this.damage, this.energy));
        if(!this.modifier.equals(" ")){ // Only add modifier if there is one
            temp.append(String.format(" - %s", this.modifier.substring(0,1).toUpperCase() + this.modifier.substring(1)));
        }
        return temp.toString();
    }

    /**
     * Returns a String representation of the Attack in JSON
     * @return a String representation of the Attack in JSON
     */
    @Override
    public String toString(){
        return String.format("{\"name\": \"%s\", \"energy\": %d, \"damage\": %d, \"special\": \"%s\"}", this.name, this.energy, this.damage, this.modifier);
    }
}