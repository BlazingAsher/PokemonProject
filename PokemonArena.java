import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
/*
 * File: PokemonArena.java
 * Author: David Hui
 * Description: Handles main game execution and logic and serves as entry point to game
 */
public class PokemonArena {
    private static final ArrayList<Pokemon> badPokemon = new ArrayList<Pokemon>();
    private static final ArrayList<Pokemon> goodPokemon = new ArrayList<Pokemon>();
    private static final Random random = new Random();
    private static final Scanner in = new Scanner(System.in);
    private static Pokemon playerPoke;
    private static Pokemon opponent;

	/**
	 * Loads Pokemon into an ArrayList
	 */
	private static void loadPokemon() throws IOException{
    	Scanner inF = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
		
		int num = inF.nextInt();
		inF.nextLine(); // Space skip
		for(int i=0;i<num;i++){
		    badPokemon.add(new Pokemon(inF.nextLine()));
		}
	}

	/**
	 * Allows the user to choose their initial Pokemon team
	 */
	private static void createTeam(){
		// Print out all the available Pokemon
		for(int i=0;i<badPokemon.size();i++){
			System.out.printf("%d - %s\n", i+1, badPokemon.get(i).toPrettyString(false, true));
		}

		int[] chosenInds = new int[4]; // Stores the chosen indices
		
		boolean chooseError = true; // Whether there has been an error choosing a Pokemon
		
		while(chooseError){ // Loop until we have chosen all 4 Pokemon
			System.out.print("Please choose your pokemon (space separated): ");
			chooseError = false;
			chosenInds = new int[4]; // Clear the old indicies
			
			String[] line = in.nextLine().split(" ");
			
			for(int i = 0 ;i<chosenInds.length;i++){
				if(line.length < 4){ // Ensure they have entered at least 4 (prevent index error)
					System.out.println("Please enter all four Pokemon on one line!");
					chooseError = true;
					break;
				}
				int temp = Integer.parseInt(line[i]); // Stores the chosen index temporarily

				// Check within range
				if(temp-1 >= 0 && temp-1 < badPokemon.size() && Utilities.indexOf(chosenInds, temp) == -1){
					chosenInds[i] = temp;
				}
				else{
					System.out.println("Invalid option!");
					chooseError = true;
					break;
				}
			}
			
		}

		// Sort the array so that we delete Pokemon backwards, avoids changing the indices of the elements before we remove them
		Arrays.sort(chosenInds);

		for(int i=chosenInds.length-1;i>=0;badPokemon.remove(chosenInds[i--]-1)){
			goodPokemon.add(badPokemon.get(chosenInds[i]-1));
		}


	}

	/**
	 * Allows the player the choose a Pokemon during battle
	 * @param canCancel Whether the user can cancel the choosing
	 * @return the index value of the Pokemon in the goodPokemon ArrayList
	 */
	private static int choosePokemon(boolean canCancel){
    	boolean[] disallowed = new boolean[goodPokemon.size()]; // Pokemon that the player cannot choose
    	boolean hasNone = true; // Whether the player has no more Pokemon
    	StringBuilder bufferOut = new StringBuilder(); // Buffers the output until we are sure we need it

		// Print out all available Pokemon
    	for(int i=0;i<goodPokemon.size();i++){
    		if(goodPokemon.get(i).getHP() <= 0){ // Disllow if fainted
    			disallowed[i] = true;
    		}
    		else{
    			hasNone = false;
			}
			bufferOut.append(String.format("%d - %s\n", i + 1, goodPokemon.get(i).toPrettyString(true, true)));
		}

    	if(hasNone){ // Player has no Pokemon
			System.out.println("You are out of usable Pokemon!");
			return -1;
		}
    	System.out.print(bufferOut);

		if(canCancel){ // Print an additional option if the player can cancel
			System.out.printf("%d - %s\n", goodPokemon.size()+1, "Cancel");
		}
		
		int chosenPokeInd; // Index of the chosen Pokemon
		
		while(true){ // Keep going until we choose a valid one
			System.out.print("Please choose the Pokemon to fight with: ");

			if(in.hasNextInt()){ // Prevent crash if something other than an int is entered
				chosenPokeInd = in.nextInt()-1; // Undo the offset
				if(chosenPokeInd >= 0 && chosenPokeInd < goodPokemon.size()) { // Check within range
					if (disallowed[chosenPokeInd]) { // Check if the Pokemon can be chosen
						System.out.printf("\n%s cannot be chosen! He is faint!\n", goodPokemon.get(chosenPokeInd).getName());
					} else {
						LevelLogger.log(goodPokemon.get(chosenPokeInd));
						break;
					}
				}
				else if(canCancel && chosenPokeInd == goodPokemon.size()){ // Player canceled
					return -1;
				}
			}
			else{
				System.out.println("Please enter a valid number!");
				in.next();
			}

			
		}

		// Say the phrase and return the Pokemon chosen
		System.out.printf("%s, I choose you!\n\n\n", goodPokemon.get(chosenPokeInd).getName());
		return chosenPokeInd;
    }

	/**
	 * Allows the player the choose a Pokemon during battle
	 * @see #choosePokemon(boolean)
	 * @return the index value of the Pokemon in the goodPokemon ArrayList
	 */
	private static int choosePokemon(){
    	return choosePokemon(false);
    }

	/**
	 * Allows the player to attack the opposing Pokemon during battle
	 * @return whether a move was chosen
	 */
    private static boolean playerAttack(){
    	int chosenMove; // The index of the move chosen
		while(true){ // Keep going until we choose a valid one

			// Print out all moves
			for(int i=0;i<playerPoke.getAttacks().length;i++){
				System.out.printf("%d - %s\n", i+1, playerPoke.getAttacks()[i].toPrettyString());
			}
			System.out.printf("%d - %s\n", playerPoke.getAttacks().length+1, "Cancel");
			
			System.out.print("Please choose the move to fight with: ");

			if(in.hasNextInt()){
				chosenMove = in.nextInt()-1;
				if(chosenMove == playerPoke.getAttacks().length){ // Player canceled
					return false;
				}

				// Check if out of range
				if(chosenMove >= 0 && chosenMove < playerPoke.getAttacks().length && playerPoke.getEnergy() - playerPoke.getAttacks()[chosenMove].getEnergy() >= 0){
					break;
				}
				else if(!(playerPoke.getEnergy() - playerPoke.getAttacks()[chosenMove].getEnergy() >= 0)){ // Check if player has enough energy
					System.out.println("You do not have enough energy to perform that move!");
				}
				else {
					System.out.println("Invalid move, please try again!");
				}
			}
			else{
				System.out.println("Please enter a valid number!");
				in.next();
			}


		}
		
		LevelLogger.log(playerPoke.getAttacks()[chosenMove]);
		System.out.printf("%s used %s!\n", playerPoke.getName(), playerPoke.getAttacks()[chosenMove].getName());

		// Perform the attack
		playerPoke.attack(opponent, chosenMove);

		return true;
    }

	/**
	 * Randomly chooses the action of the AI during battle
	 */
    private static void enemyMove(){
    	LevelLogger.log("It's the enemy!");
    	Attack[] opponentAttacks = opponent.getAttacks(); // Save typing and store the attacks in a local variable
    	ArrayList<Integer> attackInds = new ArrayList<Integer>(); // Indices of the attacks

		// Add all indices to attackInds
    	for(int i = 0;i<opponentAttacks.length;attackInds.add(i++)){

		}
    	
    	while(attackInds.size() > 0){ // Run until we attack or have nullified all attack options
    		int mapperInd = attackInds.size() < 2 ? 0 : random.nextInt(attackInds.size()-1); // index of the index to be chosen
    		int attackInd = attackInds.get(mapperInd); // Gets the index of the attack that was randomly generated
    		if(opponent.getEnergy() - opponentAttacks[attackInd].getEnergy() < 0){ // If not enough energy, remove from the available attacks
    			attackInds.remove(mapperInd);
    		}
    		else{
    			LevelLogger.log(opponentAttacks[attackInd]);

    			// Attack the player
    			System.out.printf("%s used %s!\n", opponent.getName(), opponentAttacks[attackInd].getName());
    			opponent.attack(playerPoke, attackInd);
    			return;
    		}
    	}

    	// Not enough energy, pass
    	System.out.printf("The foe %s does not have enough energy!\n", opponent.getName());
	}

	/**
	 * Delays execution of code by seconds seconds
	 * @param seconds The time in seconds to delay the code
	 */
    private static void delay(int seconds){
    	try{
			TimeUnit.SECONDS.sleep(seconds);
		}
		catch (InterruptedException e){
			
		}
    }

	/**
	 * Runs the main game
	 * @return whether the player won
	 */
    private static boolean gameLoop(){
		while(badPokemon.size() > 0){
			int eneInd = random.nextInt(badPokemon.size()); // index of the enemy chosen
			opponent = badPokemon.get(eneInd); // set opponent

			System.out.printf("Foe %s appeared!\n", opponent.getName());

			boolean playerTurn = random.nextBoolean(); // randomly choose who goes first
			boolean completedMove = true; // whether a turn actually happened during the iteration of the loop

			while(opponent.getHP() > 0){
				if(completedMove && playerTurn){ // If we moved on a turn, and it is the player's turn, print some info
					System.out.print("\n\n");
					System.out.println("Foe: "+opponent.toPrettyString());
					System.out.println("You: "+playerPoke.toPrettyString(true, false));
					completedMove = !completedMove;
				}

				// Check whether it is player or AI turn
				if(playerTurn){
					if(playerPoke.getStunned()){ // whether player is stunned (force pass)
						System.out.println("You are stunned!\n\n");
					}
					else {
						System.out.println("Please choose your desired action: (A)ttack, (R)etreat, (P)ass");

						// Ask the player what to do
						switch(in.next().toUpperCase()){
							case "A":
								if(!playerAttack()){
									// Canceled, so display the menu again
									continue;
								}
								break;
							case "R":
								int playerPokeInd = choosePokemon(true); // stores chosen index
								if(playerPokeInd != -1){ // player has actually chosen a Pokemon
									playerPoke = goodPokemon.get(playerPokeInd);
									break;
								}
								else {
									// Canceled, so display the menu again
									continue;
								}

							case "P":
								break;
							default:
								System.out.println("Invalid option!");
								continue;
						}
					}

					// Unstun the player after the turn
					playerPoke.unstun();
				}
				else{
					if(opponent.getStunned()){ //whether AI is stunned (force pass)
						System.out.printf("The foe %s is stunned!", opponent.getName());
					}
					else{
						// Allow enemy to move
						enemyMove();
						if(playerPoke.getHP() <= 0){ // If the player died, choose a new one
							delay(2);
							int playerPokeInd = choosePokemon(); // stores chosen index
							if(playerPokeInd == -1){
								return false; // Ran out of Pokemon, terminate game.
							}
							playerPoke = goodPokemon.get(playerPokeInd); // Set the new player pokemon

							//Skip move when died
							playerTurn = !playerTurn;
						}
					}
					// Unstun the AI after the turn
					opponent.unstun();
				}

				// Add energy to all Pokemon after the round
				opponent.addEnergy(10);
				for(int i=0;i<goodPokemon.size();goodPokemon.get(i++).addEnergy(10)){

				}

				// We made it here, so a turn was completed
				completedMove = true;

				// Next turn
				playerTurn = !playerTurn;
				delay(2);
			}

			// Add HP to alive player Pokemon and undisable them after the battle
			for(int i=0;i<goodPokemon.size();i++){
				if(goodPokemon.get(i).getHP() > 0){
					goodPokemon.get(i).addHP(20);
				}
				goodPokemon.get(i).undisable();
			}

			// Remove the bad Pokemon
			badPokemon.remove(eneInd);

		}
		return true;
	}

	public static void main (String [] args) throws IOException{
		LevelLogger.setDefaultLogLevel(LevelLogger.DEBUG); // Sets the debug logger to mute debug output
		
		loadPokemon();
		LevelLogger.log("Begin choosing");
		
		createTeam(); // Create the player's team
		playerPoke = goodPokemon.get(choosePokemon(false));

		// Run the game and see who won
		boolean playerWon = gameLoop();

		System.out.print("\n\n"+ (playerWon ? "You are trainer supreme!" : "You failed!"));

	}
}