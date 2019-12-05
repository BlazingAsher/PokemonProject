import java.util.*;
/*
 * File: Pokemon.java
 * Author: David Hui
 * Description: Contains fields about the Pokemon and methods to update such fields and perform actions onto other Pokemon
 */
public class Pokemon {
    private final String name;
    private int hp;
    
    private int energy = 50;
    private final String type;
    private final String resistance;
    private final String weakness;
    private final Attack[] attacks;
    private boolean stunned = false;
    private boolean disabled = false;
    
    
    private final Random random = new Random();
    private final int MAX_HP;
    private final int MAX_ENERGY = 50;
    
    
    public Pokemon(String initString){
        String[] pokeInfo = initString.split(",");
        this.name = pokeInfo[0];
        this.hp = Integer.parseInt(pokeInfo[1]);
        this.MAX_HP = this.hp;
        this.type = pokeInfo[2];
        this.resistance = pokeInfo[3];
        this.weakness = pokeInfo[4];
        
        int numAttacks = Integer.parseInt(pokeInfo[5]);
        this.attacks = new Attack[numAttacks];
        
        for(int i=0;i<numAttacks;i++){
            this.attacks[i] = new Attack(pokeInfo[6+4*i], Integer.parseInt(pokeInfo[6+4*i+1]), Integer.parseInt(pokeInfo[6+4*i+2]), pokeInfo[6+4*i+3]);
            LevelLogger.log(this.attacks[i]);
        }
    }

    /**
     * Adds energy to the Pokemon, up to the maximum amount
     * @param addAmt The amount of energy to add
     * @return the amount of energy the Pokemon now has
     */
    public int addEnergy(int addAmt) {
        this.energy = Math.min(this.energy+addAmt, this.MAX_ENERGY);
        return this.energy;
    }

    /**
     * Removes energy from the Pokemon
     * @param useAmt The amount of energy to use
     * @return the amount of energy the Pokemon now has
     */
    public int useEnergy(int useAmt){
        this.energy = this.energy - useAmt;
        return this.energy;
    }

    /**
     * Gets whether the Pokemon is stunned
     * @return whether the Pokemon is stunned
     */
    public boolean getStunned(){
        return this.stunned;
    }

    /**
     * Gets the name of the Pokemon
     * @return the name of the Pokemon
     */
    public String getName() {
        return this.name;
    }

    /**
     * Adds HP to the Pokemon, up to the maximum amount
     * @param addAmt The amount of HP to add
     * @return the amount of HP the Pokemon now has
     */
    public int addHP(int addAmt) {
        this.hp = Math.min(this.MAX_HP, this.hp+addAmt);
        return this.hp;
    }

    /**
     * Gets whether the Pokemon is stunned
     * @return the Pokemon's energy
     */
    public int getEnergy(){
        return this.energy;
    }

    /**
     * Gets whether the Pokemon is stunned
     * @return the Pokemon's HP
     */
    public int getHP () {
        return this.hp;
    }

    /**
     * Gets the Pokemon's attacks
     * @return the Pokemon's attacks
     */
    public Attack[] getAttacks() {
        return this.attacks;
    }

    /**
     * Stuns the Pokemon
     */
    public void stun(){
        this.stunned = true;
    }

    /**
     * Unstuns the Pokemon
     */
    public void unstun(){
        this.stunned = false;
    }

    /**
     * Disables the Pokemon
     */
    public void disable(){
        this.disabled = true;
    }

    /**
     * Undisables the Pokemon
     */
    public void undisable(){
        this.disabled = false;
    }

    /**
     * Removes HP from the Pokemon
     * @param oppType The type of the opposing Pokemon
     * @param decAmt The amount of HP to remove
     * @return the amount of HP the Pokemon now has
     */
    public int decHP(String oppType, int decAmt){
        if(this.resistance.equals(oppType)){
            System.out.println("It's not very effective...");
            decAmt*=0.5;
        }
        if(this.weakness.equals(oppType)){
            System.out.println("It's super effective!");
            decAmt*=2;
        }
        this.hp-=decAmt;
        if(this.hp <= 0){
            System.out.printf("%s fainted!\n", this.name);
        }
        LevelLogger.log("Dec: "+decAmt+" Now HP: "+this.hp);
        return this.hp;
    }

    /**
     * Attacks another Pokemon
     * @param other The Pokemon to attack
     * @param attackID The ID of the attack to use
     */
    public void attack(Pokemon other, int attackID){
        if(this.stunned){
            return;
        }
        Attack chosenAttack = this.attacks[attackID];
        useEnergy(chosenAttack.getEnergy());
        
        String modifier = chosenAttack.getModifier();
        
        int damage = chosenAttack.getDamage();
        
        if(disabled){
            damage = Math.max(0, damage-10);
        }
        
        switch(modifier) {
            case "stun":
                LevelLogger.log("stun");
                if(random.nextBoolean()){
                    System.out.printf("%s stunned %s!\n", this.name, other.getName());
                    other.stun();
                }
                break;
            case "wild card":
                LevelLogger.log("wild card");
                if(random.nextBoolean()){
                    LevelLogger.log("negate");
                    System.out.printf("%s missed!\n",this.name);
                    damage = 0;
                }
                break;
            case "wild storm":
                LevelLogger.log("wild storm");
                int olddamage = damage;
                damage = 0;
                int i = 0;
                while(random.nextBoolean()){
                    damage+=olddamage;
                    i++;
                }
                if(i == 0){
                    System.out.printf("%s missed!\n",this.name);
                }
                else{
                    System.out.printf("Hit %d times!\n",i);
                }
                break;
            case "disable":
                LevelLogger.log("disable");
                System.out.printf("%s disabled %s! All attacks by %s will now do 10 HP less damage!\n", this.name, other.getName(), other.getName());
                other.disable();
                break;
            case "recharge":
                LevelLogger.log("recharge");
                System.out.printf("%s recharged 20 energy!\n", this.name);
                addEnergy(20);
                break;
        }

        other.decHP(this.type, damage);
    }

    /**
     * Returns a String representation of the Pokemon for display
     * @param playerDisplay Whether to display information about the Pokemon that only the player should know
     * @param  showMoves Whether to display the Pokemon's moves
     * @return a String representation of the Pokemon
     */
    public String toPrettyString(boolean playerDisplay, boolean showMoves){
        StringBuilder temp = new StringBuilder(String.format("%s (%d HP)", this.name, Math.max(0, this.hp)));
        if(playerDisplay){
            temp.append(String.format(" (%d Energy)", this.energy));
        }
        
        if(this.hp <= 0){
            temp.append(" - FNT");
        }
        
        if(this.stunned){
            temp.append(" - Stunned");
        }
        
        if(this.disabled){
            temp.append(" - Disabled");
        }
        
        if(showMoves){
            temp.append("\n");
            for(Attack pokeAttack : this.attacks){
                temp.append(String.format("\t%s\n", pokeAttack.toPrettyString()));
            }
        }
        return temp.toString();
    }
    /**
     * Returns a String representation of the Pokemon for display
     * @param playerDisplay Whether to display information about the Pokemon that only the player should know
     * @see #toPrettyString(boolean, boolean)
     * @return a String representation of the Pokemon
     */
    public String toPrettyString(boolean playerDisplay){
        return toPrettyString(playerDisplay, false);
    }

    /**
     * Returns a String representation of the Pokemon for display
     * @see #toPrettyString(boolean, boolean)
     * @return a String representation of the Pokemon
     */
    public String toPrettyString(){
        return toPrettyString(false, false);
    }

    /**
     * Returns a String representation of the Pokemon in JSON
     * @return a String representation of the Pokemon in JSON
     */
    @Override
    public String toString(){
        return String.format("{\"name\": \"%s\", \"hp\": %d, \"energy\": %d, \"type\": \"%s\", \"resistance\": \"%s\", \"weakness\": \"%s\", \"stunned\": %b, \"disabled\": %b}", this.name, this.hp, this.energy, this.type, this.resistance, this.weakness, this.stunned, this.disabled);
    }
}