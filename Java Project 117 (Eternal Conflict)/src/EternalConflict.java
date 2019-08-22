/**
 * A turn-based space combat game.
 * 
 * 0.4.0 alpha 8/21/2019
 * Added engines and shields as subsystems (alongside PD), which can be damaged via enemy fire.
 * Repairing your ship now gives you the option to repair a single damaged subsystem, or your armor.
 * Repairing armor now restores 15 health, from 10.
 * Critical hits now double the chance of damaging a subsystem.
 * Subsystem damage rate reduced to 10%, from 20%.
 * Added heat as a new mechanic.
 * In general, thermal weapons add the most heat, followed by EM, followed by kinetic. However, thermal
 * weapons also tend to add heat to the target ship.
 * Option to shut down systems and bleed off more heat than usual, but this uses up the turn.
 * An overheating ship must shut down its systems; the player can choose to override this, but at a 20%
 * chance of destroying their ship.
 * 
 * @author Michael Yang
 * @version 0.4.0 alpha
 * @since   7/28/2019
 * @updated 8/21/2019
 */

import java.util.Scanner;
import java.util.ArrayList;

public class EternalConflict {
	
	public static final String VERSION = "0.4.0 alpha";
	
	/*
	 * Damage types:
	 * 0 Kinetic
	 * 1 Thermal
	 * 2 EM
	 * 3 Omni
	 */
	
	/*
	 * Weapons:
	 * RG Railgun
	 * PL Plasma Cannon
	 * RC Rotary Cannon
	 * LZ Beam Laser
	 * ML Missile Launcher
	 * DN Drone Swarm
	 * DL Drone Laser
	 */
	
	/*
	 * Munitions:
	 * 0 Missiles
	 */
	
	/*
	 * Subsystems:
	 * PDF Point-Defenses (PD)
	 * ENG Engines
	 * SHL Shields
	 */
	
	private Ship player;
	private Ship enemy;
	private double standardEvasion;
	private double standardCrit;
	private double standardSubDamage;
	
	public EternalConflict() {
		standardEvasion = .15;
		standardCrit = .05;
		standardSubDamage = .1;
		player = new Ship(20, 100, 40, 2, new double[] {0, .25, .5, 0}, new double[] {.5, .25, 0, 0}, standardEvasion, 100, 5, new int[1], null);
		enemy = new Ship(20, 100, 40, 2, new double[] {0, .25, .5, 0}, new double[] {.5, .25, 0, 0}, standardEvasion, 100, 5, new int[1], new String[] {"RG", "PL", "RC", "DN", "ML"});
		enemy.setMunitions(0, 10);
		enemy.setDrones(5);
	}
	
	public static void main(String[] args) {
		EternalConflict stuff = new EternalConflict();
		stuff.run();
	}
	
	public void run() {
		System.out.println("\n\n\n");
		printIntro();
		boolean done = false;
		do {
			System.out.println("  1 - Start Game");
			System.out.println("  2 - Help");
			System.out.println("  3 - Lore");
			System.out.println("  4 - Version History");
			System.out.println("  5 - Quit\n");
			int choice = Prompt.getInt("  -> ", 1, 5);
			switch(choice) {
			case 1:
				beginGame();
				done = true;
				break;
			case 2:
				printHelp();
				break;
			case 3:
				printLore();
				break;
			case 4:
				printChangelog();
				break;
			case 5:
				done = true;
				System.out.println("\n  You should at least try to put up a fight before surrendering...");
				break;
			}
		} while(!done);
		printOutro();
	}
	
	public void printIntro() {
		System.out.println("version " + VERSION + "");
		System.out.println("ETERNAL CONFLICT: A TURN-BASED SPACE COMBAT GAME\n");
		System.out.println("  Since the year 2572, the Terran Federation and the Free Worlds have been");
		System.out.println("  at odds over sovereignty of the Antollare system. What should've been a");
		System.out.println("  quick skirmish that promised to bring troops home by Christmas ended up");
		System.out.println("  grinding down into what seemed to be eternal conflict. The year is now");
		System.out.println("  2578, and you, as a captain in the Terran armed forces, are in command of");
		System.out.println("  the heavy frigate Peacemaker. Setting a course for the Antollare system, ");
		System.out.println("  you power up your hyperdrive. When your ship's warp bubble breaks, you");
		System.out.println("  spot an enemy frigate. Immediately, targetting pings light up on your");
		System.out.println("  display as the enemy ship gets a lock. You yell \"All hands to action");
		System.out.println("  stations!\" into the PA as you maneuver your ship into position. For the");
		System.out.println("  glory of the Federation, and to victory!\n");
	}
	
	public void printHelp() {
		System.out.println("\nHELP");
		Scanner infile = OpenFile.openToRead("Help.txt");
		int totalPages = 1;
		while(infile.hasNext()) {
			if(infile.nextLine().equals("**BREAK**")) {
				totalPages++;
			}
		}
		infile = OpenFile.openToRead("Help.txt");
		String temp = null;
		int page = 1;
		infile.nextLine();
		while(infile.hasNext()) {
			temp = infile.nextLine();
			if(temp.equals("**BREAK**")) {
				Prompt.getString("  Press ENTER to continue (" + page + " of " + totalPages + ")");
				page++;
			} else {
				System.out.println(temp);
			}
		}
		infile.close();
		Prompt.getString("\n  Press ENTER to continue (" + totalPages + " of " + totalPages + ")");
		System.out.println();
	}
	
	public void printLore() {
		Scanner infile = OpenFile.openToRead("Lore.txt");
		int totalPages = 1;
		while(infile.hasNext()) {
			if(infile.nextLine().equals("")) {
				totalPages++;
			}
		}
		System.out.println("\nLORE");
		System.out.println("  The lore is very extensive and it may take some time to read; if you just");
		System.out.println("  want a brief summary of the backstory, read the abridged version.");
		System.out.println("    1 - Full (" + totalPages + " pages)");
		System.out.println("    2 - Abridged (1 page)\n");
		int choice = Prompt.getInt("  -> ", 1, 2);
		String file = "";
		switch(choice) {
		case 1:
			file = "Lore.txt";
			break;
		case 2:
			file = "LoreAbridged.txt";
			totalPages = 1;
			break;	
		}
		System.out.println();
		infile = OpenFile.openToRead(file);
		String temp = null;
		int page = 1;
		infile.nextLine();
		while(infile.hasNext()) {
			temp = infile.nextLine();
			if(temp.equals("")) {
				Prompt.getString("\n  Press ENTER to continue (" + page + " of " + totalPages + ")");
				System.out.println();
				page++;
			} else {
				System.out.println("  " + temp);
			}
		}
		infile.close();
		Prompt.getString("\n  Press ENTER to continue (" + totalPages + " of " + totalPages + ")");
		System.out.println();
	}
	
	public void printChangelog() {
		boolean latest = false;
		System.out.println("\nVERSION HISTORY");
		Scanner infile = OpenFile.openToRead("Changelog.txt");
		String temp = null;
		while(infile.hasNext()) {
			temp = infile.nextLine();
			if(!latest && temp.equals("")) {
				Prompt.getString("\n  Press ENTER for full changelog");
				System.out.println();
				latest = true;
			} else {
				System.out.println("  " + temp);
			}
		}
		infile.close();
		Prompt.getString("\n  Press ENTER to continue");
		System.out.println();
	}
	

	public void printOutro() {
		System.out.println("\nETERNAL CONFLICT: A TURN-BASED SPACE COMBAT GAME\n");
		System.out.println("  Version			" + VERSION);
		System.out.println("  Design			Michael Yang");
		System.out.println("  Programming			Michael Yang");
		System.out.println("  Story / Writing		Michael Yang");
		System.out.println("  VERY LOOSELY inspired by FTL: Faster than Light");
		System.out.println("\nThank you for playing, and have a nice day!\n\n\n");
	}
	
	public void beginGame() {
		int choice = 0;
		boolean overheat = true;
		boolean override = false;
		boolean suicide = false;
		boolean overheatFailure = false;
		loadWeapons();
		do {
			startOfTurn(player);
			overheat = false;
			override = false;
			if(player.getHeat() > player.getHeatCapacity()) {
				overheat = true;
				override = overheatDecision();
			}
			if(override) {
				if(Math.random() < .2) {
					overheatFailure = true;
					override = false;
				}
			} else if(overheat) {
				System.out.println("\nSHUT DOWN SYSTEMS");
				System.out.println("  Your ship's automatic shutdown triggers, disabling all onboard systems");
				System.out.println("  save for critical ones such as shields and life support. You lament losing");
				System.out.println("  your chance to act, but as the alternative could entail destruction of");
				System.out.println("  your ship, you deem this to be acceptable.");
				System.out.println("    Your Ship    - -20 Heat");
				player.removeHeat(20);
			}
			if(!overheat || override) {
				choice = makeDecision();
				switch(choice) {
				case 1:
					choice = 1;
					if(enemy.dronesActive()) {
						choice = chooseTarget();
					}
					switch(choice) {
					case 1:
						do {
							choice = selectWeapon(1);
						} while(!attackShip(enemy, choice - 1));
						break;
					case 2:
						do {
							choice = selectWeapon(2);
						} while(!attackShip(enemy.getDrone((int)(Math.random() * enemy.getActiveDrones())), choice - 1));
						enemy.removeDestroyedDrones();
						if(enemy.dronesActive() && enemy.getActiveDrones() <= 0) {
							enemy.toggleDrones();
						}
						break;
					}
					break;
				case 2:
					choice = chooseRepairTarget();
					switch(choice) {
					case 1:
						System.out.println("\nREPAIR ARMOR");
						System.out.println("  You send a team to scour your ship's armor for any damage, making repairs");
						System.out.println("  along the way. Half the battle is keeping your ship together, after all.");
						System.out.println("    Your Armor   -  15 Repair");
						player.healArmor(15);
						break;
					case 2:
						System.out.println("\nREPAIR PD");
						System.out.println("  You send a team to check on your point-defenses, ordering them to make any");
						System.out.println("  necessary repairs. Your adversary may be packing drones or missiles, after");
						System.out.println("  all, and you want to make sure you're protected against them.");
						System.out.println("    Your Point-Defenses Have Been Repaired (One Turn Needed to Re-Activate)");
						player.togglePDRepair();
						break;
					case 3:
						System.out.println("\nREPAIR ENGINES");
						System.out.println("  You order a repair team to get your engines back in working order. You");
						System.out.println("  seem to be getting sick of giving your enemy easy hits, and want to be");
						System.out.println("  able to start dodging shots again.");
						System.out.println("    Your Engines Have Been Repaired (One Turn Needed to Re-Activate)");
						player.toggleEngineRepair();
						break;
					case 4:
						System.out.println("\nREPAIR SHIELDS");
						System.out.println("  The shields are the first line of defense on any ship, but unfortunately,");
						System.out.println("  yours seem to be disabled at this moment. Seeking to rectify this, you");
						System.out.println("  order a team to check out your shield projector and get it functioning");
						System.out.println("  again.");
						System.out.println("    Your Shield Projector Has Been Repaired (One Turn Needed to Re-Activate)");
						player.toggleShieldRepair();
						break;
					}
					break;
				case 3:
					System.out.println("\nEVASIVE MANEUVERS");
					System.out.println("  Ordering your crew to hold on tight, you divert additional power to your");
					System.out.println("  maneuvering thrusters in preparation for incoming fire.");
					System.out.println("    Your Evasion Rate is Doubled for One Turn");
					player.setEvasion(2 * player.getEvasion());
					break;
				case 4:
					System.out.println("\nSHUT DOWN SYSTEMS");
					System.out.println("  Opting not to make a move, you shut down all systems aboard your ship save");
					System.out.println("  for critical ones such as shields and life support. This will allow your");
					System.out.println("  onboard cooling systems to bleed off more heat than usual, but will cost");
					System.out.println("  you your chance to act.");
					System.out.println("    Your Ship    - -20 Heat");
					player.removeHeat(20);
					break;
				case 5:
					player.setHull(0);
					suicide = true;
					break;
				}
			}
			if(player.dronesActive()) {
				System.out.println("\nDRONES");
				System.out.println("  In the heat of combat, you notice a barrage of laser fire coming from your");
				System.out.println("  drones, aimed at the enemy ship. Whether or not they hit is another");
				System.out.println("  question.");
				for(int i = 0; i < player.getActiveDrones(); i++) {
					damageShip(enemy, 1, 2, standardCrit, false);
				}
			}
			if(enemy.dronesActive() && enemy.getActiveDrones() <= 0) {
				enemy.toggleDrones();
			}
			if(enemy.dronesActive() && player.getPDState()) {
				System.out.println("\nPOINT-DEFENSE");
				System.out.println("  As your window of opportunity closes, your point-defenses manage to shoot");
				System.out.println("  down one of the enemy's drones.");
				enemy.getDrone((int)(Math.random() * enemy.getActiveDrones())).setHull(0);
				enemy.removeDestroyedDrones();
				if(enemy.dronesActive() && enemy.getActiveDrones() <= 0) {
					enemy.toggleDrones();
				}
			}
			if(suicide || overheatFailure || enemy.getHull() <= 0) {
				break;
			}
			Prompt.getString("\n  Press ENTER to continue");
			startOfTurn(enemy);
			System.out.println("\n  Opportunities come and go quickly in the heat of battle; the moment you");
			System.out.println("  took advantage of the last chance, the situation changed and now the enemy");
			System.out.println("  ship is in position to act.");
			Prompt.getString("\n  Press ENTER to continue");
			if(enemy.getHeat() > enemy.getHeatCapacity()) {
				System.out.println("\nSHUT DOWN SYSTEMS");
				System.out.println("  Your adversary doesn't seem to be acting on their opportunity; running a");
				System.out.println("  scan on their ship, you notice above-average onboard temperatures, as well");
				System.out.println("  as most systems save for critical ones having been shut down. The enemy");
				System.out.println("  ship must've overheated during the engagement, and has shut down their");
				System.out.println("  systems to bleed off heat.");
				System.out.println("    Enemy Ship   - -20 Heat");
				enemy.removeHeat(20);
			} else {
				if(Math.random() < .35 && (enemy.getArmor() <= 25 || !enemy.getPDState() || !enemy.getEngineState() || !enemy.getShieldState())) {
					if(enemy.getArmor() <= 25) {
						System.out.println("\nREPAIR ARMOR");
						System.out.println("  The enemy ship doesn't seem to be doing much; suspicious, you observe");
						System.out.println("  your opponent more closely. You notice the sparks of welding torches and");
						System.out.println("  the movement of nanites on their frigate; they seem to be concerned about");
						System.out.println("  their survival.");
						System.out.println("    Enemy Armor  -  15 Repair");
						enemy.healArmor(15);
					} else if(!enemy.getShieldRepairStatus() && !enemy.getShieldState()) {
						System.out.println("\nREPAIR SHIELDS");
						System.out.println("  The enemy ship doesn't seem to be doing much; suspicious, you observe");
						System.out.println("  your opponent more closely. You notice a quick pulse of their shields; ");
						System.out.println("  they must've repaired their shield projector, though you deduce you have");
						System.out.println("  some time before they fully re-activate.");
						System.out.println("    Enemy Shield Projector Has Been Repaired (One Turn Needed to Re-Activate)");
						enemy.toggleShieldRepair();
					} else if(!enemy.getPDRepairStatus() && !enemy.getPDState()) {
						System.out.println("\nREPAIR PD");
						System.out.println("  The enemy ship doesn't seem to be doing much; suspicious, you observe");
						System.out.println("  your opponent more closely. You notice clusters of nanites swarm around");
						System.out.println("  their point-defense turrets; they seem to want to get their PD online");
						System.out.println("  again.");
						System.out.println("    Enemy Point-Defenses Have Been Repaired (One Turn Needed to Re-Activate)");
						enemy.togglePDRepair();
					} else if(!enemy.getEngineRepairStatus() && !enemy.getEngineState()) {
						System.out.println("\nREPAIR ENGINES");
						System.out.println("  The enemy ship doesn't seem to be doing much; suspicious, you observe");
						System.out.println("  your opponent more closely. You notice the sparks of welding torches");
						System.out.println("  clustered towards the rear of their ship; they appear to be repairing");
						System.out.println("  their engines.");
						System.out.println("    Enemy Engines Have Been Repaired (One Turn Needed to Re-Activate)");
						enemy.toggleEngineRepair();
					} else {
						System.out.println("    THIS MESSAGE SHOULD NEVER APPEAR");
					}
				} else if(Math.random() < .20 && enemy.getEngineState() && enemy.getShield() <= 15) {
					System.out.println("\nEVASIVE MANEUVERS");
					System.out.println("  You notice your adversary maneuver his ship in a much more erratic manner.");
					System.out.println("  Landing your next shot will be tricky.");
					System.out.println("    Enemy Evasion Rate is Doubled for One Turn");
					enemy.setEvasion(2 * enemy.getEvasion());
				} else {
					underAttack(enemy);
				}
			}
			if(enemy.dronesActive()) {
				System.out.println("\nDRONES");
				System.out.println("  As you closely observe the actions of the enemy ship, a series of bright");
				System.out.println("  flashes appear in your view. The enemy's drones have opened fire on your");
				System.out.println("  ship, and you hope your PD is operational.");
				for(int i = 0; i < enemy.getActiveDrones(); i++) {
					damageShip(player, 1, 2, standardCrit, false);
				}
			}
			if(player.dronesActive() && player.getActiveDrones() <= 0) {
				player.toggleDrones();
			}
			if(player.dronesActive() && enemy.getPDState()) {
				System.out.println("\nPOINT-DEFENSE");
				System.out.println("  During the enemy ship's window of opportunity, their point-defenses");
				System.out.println("  managed to shoot down one of your drones.");
				player.getDrone((int)(Math.random() * player.getActiveDrones())).setHull(0);
				player.removeDestroyedDrones();
				if(player.dronesActive() && player.getActiveDrones() <= 0) {
					player.toggleDrones();
				}
			}
			if(player.getHull() <= 0) {
				break;
			}
			Prompt.getString("\n  Press ENTER to continue");
			printPlayerInfo();
			printEnemyInfo();
			System.out.println("\n  You sigh in relief when your opponent loses his time to act. You prepare");
			System.out.println("  to move into position to retaliate.");
			Prompt.getString("\n  Press ENTER to continue");
		} while(player.getHull() > 0 && enemy.getHull() > 0);
		printEnding(suicide, overheatFailure);
	}
	
	public void loadWeapons() {
		System.out.println("\nLOAD WEAPONS");
		System.out.println("  As your ship approaches the enemy, you try to recollect what weapon");
		System.out.println("  loadout you equipped before leaving base.");
		System.out.println("    0 - " + expandWeaponSymbol("RG"));
		System.out.println("    1 - " + expandWeaponSymbol("LZ"));
		System.out.println("    2 - " + expandWeaponSymbol("PL"));
		System.out.println("    3 - " + expandWeaponSymbol("RC"));
		System.out.println("    4 - " + expandWeaponSymbol("DN"));
		System.out.println("    5 - " + expandWeaponSymbol("ML"));
		System.out.println("  Shield Resistances	50% Kinetic, 25% Thermal,  0% EM");
		System.out.println("  Armor Resistances	 0% Kinetic, 25% Thermal, 50% EM");
		System.out.println("\n  Type in a three-digit number to choose your weapons. For instance, type");
		System.out.println("  \"023\" to load a railgun into slot 1, a plasma cannon into slot 2, and a");
		System.out.println("  rotary cannon in slot 3.\n");
		String option = "";
		do {
			option = Prompt.getString("  -> ");
		} while(!validLoadout(option));
		player.setWeapons(new String[] {assignWeapons(Integer.parseInt(option.charAt(0) + "")), assignWeapons(Integer.parseInt(option.charAt(1) + "")), assignWeapons(Integer.parseInt(option.charAt(2) + ""))});
	}
	
	public boolean validLoadout(String option) {
		boolean taken[] = new boolean[10];
		if(option.length() != 3) {
			return false;
		}
		for(int i = 0; i < option.length(); i++) {
			if("012345".indexOf(option.charAt(i) + "") == -1 || taken[Integer.parseInt(option.charAt(i) + "")]) {
				return false;
			} else {
				taken[Integer.parseInt(option.charAt(i) + "")] = true;
			}
		}
		return true;
	}
	
	public String assignWeapons(int choice) {
		switch(choice) {
		case 0:
			return "RG";
		case 1:
			return "LZ";
		case 2:
			return "PL";
		case 3:
			return "RC";
		case 4:
			player.setDrones(5);
			return "DN";
		case 5:
			player.setMunitions(0, player.getMunitions(0) + 10);
			return "ML";
		}
		return null;
	}
	
	public void startOfTurn(Ship ship) {
		ship.revertEvasion();
		ship.regenShield();
		ship.coolShip();
		ship.repairPD();
		ship.repairEngines();
		ship.repairShields();
		printPlayerInfo();
		printEnemyInfo();
	}
	
	public void printPlayerInfo() {
		System.out.println("\n  Your Ship");
		printShipInfo(player);
	}
	
	public void printEnemyInfo() {
		System.out.println("\n  Enemy Ship");
		printShipInfo(enemy);
	}
	
	public void printShipInfo(Ship ship) {
		System.out.printf("    Shield  -   %6.2f\n", ship.getShield());
		System.out.printf("    Armor   -   %6.2f\n", ship.getArmor());
		System.out.printf("    Hull    -   %6.2f\n", ship.getHull());
		System.out.printf("    Heat    -   %6.2f / %6.2f\n", ship.getHeat(), ship.getHeatCapacity());
		if(ship.getEngineState()) {
			System.out.printf("    Evasion -   %6.2f", 100 * ship.getEvasion());	System.out.println("%");
		} else {
			System.out.println("    Evasion -     0.00% (Engines Damaged)");
		}
		if(ship.getPDState()) {
			System.out.println("    PD      -   Active");
		} else {
			System.out.println("    PD      - Disabled");
		}
		if(ship.getShieldState()) {
			System.out.println("    Shields -   Active");
		} else {
			System.out.println("    Shields - Disabled");
		}
		if(ship.hasWeapon("ML")) {
			System.out.printf("    Missile -   %6d\n", ship.getMunitions(0));
		}
		if(ship.hasWeapon("DN")) {
			if(ship.dronesActive()) {
				System.out.println("    Drones  - " + ship.getDroneString());
			} else {
				System.out.println("    Drones  - Inactive");
			}
		}
	}
	
	public boolean overheatDecision() {
		System.out.println("\nOVERHEATING");
		System.out.println("  You look down at your ship's temperature readings, and realize that your");
		System.out.println("  frigate has overheated. Your ship will automatically go into shutdown for");
		System.out.println("  safety reasons, though you understand that this will cost you your window");
		System.out.println("  of opportunity. You contemplate the option to override the automatic");
		System.out.println("  shutdown, acknowledging the risk of catastrophic failure.");
		System.out.println("    1 - Override Shutdown");
		System.out.println("    2 - Do Nothing\n");
		return Prompt.getInt("  -> ", 1, 2) == 1;
	}
	
	public int makeDecision() {
		boolean noRepairsNecessary = player.getArmor() == player.getMaxHealth(1) && player.getPDState() && player.getEngineState() && player.getShieldState();
		System.out.println("\n  As you get another window of opportunity, you find yourself making a");
		System.out.println("  decision. You need to act quickly lest you lose your chance.");
		System.out.println("    1 - Attack Enemy");
		System.out.print("    2 - Repair Ship");
		if(noRepairsNecessary) {
			System.out.print(" (No Repairs Necessary)");
		}
		System.out.println();
		System.out.print("    3 - Evasive Maneuvers");
		if(!player.getEngineState()) {
			System.out.print(" (Engines Damaged)");
		}
		System.out.println();
		System.out.println("    4 - Shut Down Systems");
		System.out.println("    5 - Self-Destruct\n");
		int choice = 0;
		do {
			choice = Prompt.getInt("  -> ", 1, 5);
		} while(choice == 2 && noRepairsNecessary || choice == 3 && !player.getEngineState());
		return choice;
	}
	
	public int chooseTarget() {
		System.out.println("\n  You decide to engage your adversary, but they have drones deployed. You");
		System.out.println("  deduce that you only have enough time to engage either the enemy ship or");
		System.out.println("  their drones, but not both.");
		System.out.println("    1 - Ship");
		System.out.println("    2 - Drones\n");
		return Prompt.getInt("  -> ", 1, 2);
	}
	
	public int selectWeapon(int choice) {
		if(choice == 1) {
			System.out.println("\n  Opting to engage the enemy ship, you need to decide which weapon to use.");
		} else if(choice == 2) {
			System.out.println("\n  Turning your focus away from the enemy ship and onto their drones, you");
			System.out.println("  need to decide which weapon to use.");
		} else {
			System.out.println("\n  THIS MESSAGE SHOULD NEVER APPEAR");
		}
		System.out.println("    1 - " + expandWeaponSymbol(player.getWeapon(0)));
		System.out.println("    2 - " + expandWeaponSymbol(player.getWeapon(1))); 
		System.out.println("    3 - " + expandWeaponSymbol(player.getWeapon(2)));
		System.out.println("  Shield Resistances	50% Kinetic, 25% Thermal,  0% EM");
		System.out.println("  Armor Resistances	 0% Kinetic, 25% Thermal, 50% EM\n");
		return Prompt.getInt("  -> ", 1, 3);
	}
	
	public String expandWeaponSymbol(String weapon) {
		switch(weapon) {
		case "RG":
			return "Railgun              12 Kinetic";
		case "LZ":
			return "Beam Laser           12 Thermal";
		case "PL":
			return "Plasma Cannon        12 EM";
		case "RC":
			return "Rotary Cannon         1 x 10 Kinetic, x2 Crit Rate";
		case "DN":
			return "Drone Swarm           2 Thermal per Drone, per Turn";
		case "ML":
			return "Missile Launcher     15 Thermal, 100% Hit Rate, no PD";
		}
		return "THIS MESSAGE SHOULD NEVER APPEAR";
	}
	
	public boolean attackShip(Craft target, int weapon) {
		switch(player.getWeapon(weapon)) {
		case "RG":
			System.out.println("\nRAILGUN");
			System.out.println("  Your ship's railgun charges and fires, releasing a loud boom followed by");
			System.out.println("  an electrical crackle. You can only briefly see the projectile as it");
			System.out.println("  streaks towards your target.");
			damageShip(target, 0, 12, standardCrit, false);
			System.out.println("    Your Ship    -   12 Heat");
			player.addHeat(12);
			break;
		case "PL":
			System.out.println("\nPLASMA CANNON");
			System.out.println("  You hear a distinctly electrical sound as your onboard plasma cannon");
			System.out.println("  discharges its payload, the bright blue cloud very visible on its path");
			System.out.println("  towards the enemy ship.");
			damageShip(target, 2, 12, standardCrit, false);
			System.out.println("    Your Ship    -  18 Heat");
			player.addHeat(18);
			break;
		case "RC":
			System.out.println("\nROTARY CANNON");
			System.out.println("  A buzzsaw. That is what your rotary cannon's firing sound is often");
			System.out.println("  described as. The torrent of 20mm shells is barely visible on their way");
			System.out.println("  towards the enemy.");
			for(int i = 0; i < 10; i++) {
				damageShip(target, 0, 1, 2 * standardCrit, false);
			}
			System.out.println("    Your Ship    -   8 Heat");
			player.addHeat(8);
			break;
		case "LZ":
			System.out.println("\nBEAM LASER");
			System.out.println("  You attempt to direct your ship's beam laser onto your enemy. Although");
			System.out.println("  the projectile speed of such a weapon is effectively infinte, it can be");
			System.out.println("  difficult to keep the weapon focused in a single spot long enough to do");
			System.out.println("  damage to the target, not to mention you've only got enough coolant for");
			System.out.println("  a few seconds of fire at a time.");
			damageShip(target, 1, 12, standardCrit, false);
			System.out.println("    Your Ship    -  20 Heat");
			System.out.println("    Enemy Ship   -   8 Heat");
			player.addHeat(20);
			enemy.addHeat(8);
			break;
		case "ML":
			System.out.println("\nMISSILE LAUNCHER");
			if(player.getMunitions(0) <= 0) {
				System.out.println("  The hatch on top of your frigate opens. Expecting the familiar rumble");
				System.out.println("  of a missile launch, you instead hear and feel nothing. You suddenly");
				System.out.println("  realize that you have run out of missiles. Fortunately, you still have");
				System.out.println("  a window of opportunity, but you must choose another weapon quickly.");
				return false;
			}
			System.out.println("  The hatch on top of your frigate opens, and a rumble emanates throughout");
			System.out.println("  the ship as a missile is launched. You know that its radar guidance means");
			System.out.println("  it will track the enemy ship with perfect accuracy, though you hope your");
			System.out.println("  opponent isn't packing active defenses.");
			player.consumeMunitions(0);
			if(!(target instanceof Ship) || target instanceof Ship && !((Ship)target).getPDState()) {
				damageShip(target, 1, 15, standardCrit, true);
			} else {
				System.out.println("    Miss         -  Enemy PD Destroyed Missile");
			}
			System.out.println("    Your Ship    -  15 Heat");
			System.out.println("    Enemy Ship   -   6 Heat");
			player.addHeat(15);
			enemy.addHeat(6);
			break;
		case "DN":
			System.out.println("\nDRONE SWARM");
			if(player.dronesActive()) {
				System.out.println("  The large hatch on the bottom of your frigate opens, but nothing is");
				System.out.println("  launched. Of course not; you've already deployed your drones and now the");
				System.out.println("  bay is empty. Rushing to not lose your chance to attack, you quickly");
				System.out.println("  select another weapon.");
				return false;
			}
			System.out.println("  The large hatch on the bottom of your frigate opens, and five attack");
			System.out.println("  drones are launched, immediately approaching the enemy ship with each one");
			System.out.println("  entering an orbit around your adversary. These unmanned spacecraft should");
			System.out.println("  be capable of dealing sutained damage provided your opponent's");
			System.out.println("  point-defenses are down.");
			player.toggleDrones();
			break;
		case "DL":
			System.out.println("\nDRONE LASER");
			System.out.println("  Somehow you managed to mount a drone laser on your ship, thus wasting a");
			System.out.println("  perfectly good hardpoint that could've been used to mount better weapons.");
			damageShip(enemy, 1, 2, standardCrit, false);
			break;
		}
		return true;
	}
	
	public void underAttack(Ship attacker) {
		if(attacker.hasWeapon("DN") && attacker.getActiveDrones() > 0 && !attacker.dronesActive() && !player.getPDState()) {
			System.out.println("\nDRONE SWARM");
			System.out.println("  You see a large hatch on the keel of the enemy frigate slowly open up. In");
			System.out.println("  rapid succession, five attack drones are deployed, and quickly close into");
			System.out.println("  a tight orbit around your ship. Hopefully, you can destroy them before");
			System.out.println("  they do the same.");
			attacker.toggleDrones();
		} else if (player.getEvasion() > standardEvasion) {
			if(attacker.hasWeapon("ML") && attacker.getMunitions(0) > 0 && !player.getPDState()) {
				System.out.println("\nMISSILE LAUNCHER");
				System.out.println("  You notice a hatch on top of the enemy ship open. A missile is then");
				System.out.println("  launched, its exhaust trail very visible as it streaks towards you. You");
				System.out.println("  hope your active defenses are online, because there is no way you're");
				System.out.println("  dodging this.");
				attacker.consumeMunitions(0);
				if(!player.getPDState()) {
					damageShip(player, 1, 15, standardCrit, true);
				} else {
					System.out.println("    Miss         -  Your PD Destroyed Missile");
				}
				System.out.println("    Enemy Ship   -  15 Heat");
				System.out.println("    Your Ship    -   6 Heat");
				enemy.addHeat(15);
				player.addHeat(6);
			} else if(attacker.hasWeapon("RC")) {
				System.out.println("\nROTARY CANNON");
				System.out.println("  You see a muzzle flash coming from the enemy ship, but unlike that of a");
				System.out.println("  railgun, this one is a little more sustained. You deduce that the your");
				System.out.println("  adversary fired his rotary cannon, and brace for a very deadly rainstorm.");
				for(int i = 0; i < 10; i++) {
					damageShip(player, 0, 1, 2 * standardCrit, false);
				}
				System.out.println("    Enemy Ship   -   8 Heat");
				enemy.addHeat(8);
			} else {
					System.out.println("\nNO WEAPON");
					System.out.println("  The enemy ship attempts to fire a weapon, but they soon realize that they");
					System.out.println("  forgot to arm their ship before leaving base. They quickly lose their");
					System.out.println("  chance to fire, not that they could fire anything anyway.");
			}
		} else if(attacker.hasWeapon("PL") && player.getShield() >= 6) {
			System.out.println("\nPLASMA CANNON");
			System.out.println("  A bright blue light appears in your view. A familiar sight; it's what");
			System.out.println("  you see when you fire your plasma cannon. This time, however, you weren't");
			System.out.println("  the one who pulled the trigger.");
			damageShip(player, 2, 12, standardCrit, false);
			System.out.println("    Enemy Ship   -  18 Heat");
			enemy.addHeat(18);
		} else if (attacker.hasWeapon("RG")) {
			System.out.println("\nRAILGUN");
			System.out.println("  A muzzle flash emanates from your opponent's railgun. You barely have");
			System.out.println("  time to blink before the projectile reaches the vicinity of your ship.");
			damageShip(player, 0, 12, standardCrit, false);
			System.out.println("    Enemy Ship   -   12 Heat");
			enemy.addHeat(12);
		} else {
			System.out.println("\nNO WEAPON");
			System.out.println("  The enemy ship attempts to fire a weapon, but they soon realize that they");
			System.out.println("  forgot to arm their ship before leaving base. They quickly lose their");
			System.out.println("  chance to fire, not that they could fire anything anyway.");
		}
	}
	
	public void damageShip(Craft target, int damageType, double damage, double critRate, boolean alwaysAccurate) {
		String type = "";
		boolean crit = Math.random() < critRate;
		if(crit) {
			damage *= 2;
		}
		switch(damageType) {
		case 0:
			type = "Kinetic";
			break;
		case 1:
			type = "Thermal";
			break;
		case 2:
			type = "EM";
			break;
		case 3:
			type = "Omni";
			break;
		}
		if(alwaysAccurate || !target.getEngineState() || Math.random() > target.getEvasion()) {
			if(target.getShield() <= 0) {
				
			} else if(target.getShield() < (1 - target.getShieldResist(damageType)) * damage) {
				double shieldTemp = target.getShield();
				damage = damage - (shieldTemp / (1 - target.getShieldResist(damageType)));
				target.setShield(0);
				if(target == player) {
					System.out.printf("    Your Shield  -  %5.2f " + type, shieldTemp);
				} else {
					System.out.printf("    Enemy Shield -  %5.2f " + type, shieldTemp);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
			} else {
				target.damageShield(damage, damageType);
				if(target == player) {
					System.out.printf("    Your Shield  -  %5.2f " + type, (1 - target.getShieldResist(damageType)) * damage);
				} else {
					System.out.printf("    Enemy Shield -  %5.2f " + type, (1 - target.getShieldResist(damageType)) * damage);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
				return;
			}
			if(target.getArmor() == 0) {
				
			} else if(target.getArmor() < (1 - target.getArmorResist(damageType)) * damage) {
				double armorTemp = target.getArmor();
				damage = damage - (target.getArmor() / (1 - target.getArmorResist(damageType)));
				target.setArmor(0);
				if(target == player) {
					System.out.printf("    Your Armor   -  %5.2f " + type, armorTemp);
				} else {
					System.out.printf("    Enemy Armor  -  %5.2f " + type, armorTemp);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
			} else {
				target.damageArmor(damage, damageType);
				if(target == player) {
					System.out.printf("    Your Armor   -  %5.2f " + type, (1 - target.getArmorResist(damageType)) * damage);
				} else {
					System.out.printf("    Enemy Armor  -  %5.2f " + type, (1 - target.getArmorResist(damageType)) * damage);
				}
				if(crit) {
					System.out.print(" (Critical)");
				}
				System.out.println();
				if(target instanceof Ship) {
					if(crit) {
						rollSubsystemDamage((Ship)target, 2 * standardSubDamage);
					} else {
						rollSubsystemDamage((Ship)target, standardSubDamage);
					}
				}
				return;
			}
			target.damageHull(damage);
			if(target == player) {
				System.out.printf("    Your Hull    -  %5.2f " + type, damage);
			} else {
				System.out.printf("    Enemy Hull   -  %5.2f " + type, damage);
			}
			if(crit) {
				System.out.print(" (Critical)");
			}
			System.out.println();
			if(target instanceof Ship) {
				if(crit) {
					rollSubsystemDamage((Ship)target, 2 * standardSubDamage);
				} else {
					rollSubsystemDamage((Ship)target, standardSubDamage);
				}
			}
		} else {
				System.out.println("    Miss");
		}
	}
	
	public void rollSubsystemDamage(Ship target, double chance) {
		if(Math.random() < chance) {
			ArrayList<String> activeSubsystems = new ArrayList<String>();
			if(target.getPDState()) {
				activeSubsystems.add("PDF");
			}
			if(target.getEngineState()) {
				activeSubsystems.add("ENG");
			}
			if(target.getShieldState()) {
				activeSubsystems.add("SHL");
			}
			if(activeSubsystems.isEmpty()) {
				return;
			}
			String targetSystem = activeSubsystems.get((int)(Math.random() * activeSubsystems.size()));
			switch(targetSystem) {
			case "PDF":
				target.togglePD();
				if(target == player) {
					System.out.println("    Your Point-Defenses Have Been Damaged");
				} else {
					System.out.println("    Enemy Point-Defenses Have Been Damaged");
				}
				break;
			case "ENG":
				target.toggleEngines();
				if(target == player) {
					System.out.println("    Your Engines Have Been Damaged");
				} else {
					System.out.println("    Enemy Engines Have Been Damaged");
				}
				break;
			case "SHL":
				target.toggleShields();
				if(target == player) {
					System.out.println("    Your Shield Projector Has Been Damaged");
				} else {
					System.out.println("    Enemy Shield Projector Has Been Damaged");
				}
				break;
			}
 		}
	}
	
	public int chooseRepairTarget() {
		String damagedSubsystems = "";
		System.out.println("\nREPAIR SHIP");
		System.out.println("  Instead of focusing on the enemy, you choose to focus on your own ship; some");
		System.out.println("  of your onboard systems have been damaged, and you feel that it is important");
		System.out.println("  to make some repairs. Unfortunately, you only have time to repair one part of");
		System.out.println("  your ship, leaving you with a decision to make.");
		System.out.print("    1 - Armor");
		if(player.getArmor() == player.getMaxHealth(1)) {
			System.out.print(" (Full)");
		} else {
			damagedSubsystems += "1";
		}
		System.out.println();
		System.out.print("    2 - Point-Defenses");
		if(player.getPDState()) {
			System.out.print(" (No Repairs Needed)");
		}  else {
			damagedSubsystems += "2";
		}
		System.out.println();
		System.out.print("    3 - Engines");
		if(player.getEngineState()) {
			System.out.print(" (No Repairs Needed)");
		}  else {
			damagedSubsystems += "3";
		}
		System.out.println();
		System.out.print("    4 - Shield Projector");
		if(player.getShieldState()) {
			System.out.print(" (No Repairs Needed)");
		}  else {
			damagedSubsystems += "4";
		}
		System.out.println("\n");
		return Integer.parseInt(Prompt.getChar("  -> ", damagedSubsystems) + "");
	}
	
	public void printEnding(boolean suicide, boolean overheatFailure) {
		if(!suicide && !overheatFailure) {
			Prompt.getString("\n  Press ENTER to continue");
		}
		if(suicide) {
			System.out.println("\nSELF-DESTRUCT");
			System.out.println("  Deciding that it is better to die than to be killed, you overload your");
			System.out.println("  ship's reactor, barely giving you time for your final thoughts as your");
			System.out.println("  frigate is quickly and suddenly disintegrated.");
		} else if(overheatFailure) {
			System.out.println("\nCATASTROPHIC FAILURE");
			System.out.println("  Unfortunately, your decision to override your ship's automatic shutdown");
			System.out.println("  and push your frigate beyond its acceptable heat threshold had its");
			System.out.println("  consequences. The ensuing heat buildup causes your reactor's containment");
			System.out.println("  to fail, with devastating results as your frigate is quickly and suddenly");
			System.out.println("  disintegrated.");
		} else if(player.getHull() <= 0 && enemy.getHull() <= 0) {
			System.out.println("\nMUTUAL KILL");
			System.out.println("  As your ship's internal structures sustain critical damage, you look out");
			System.out.println("  through your fractured windscreen at the enemy frigate, likewise breaking");
			System.out.println("  apart. With your life support systems failing, you draw in one last");
			System.out.println("  breath, content that you did your duty to the Federation.");
		} else if(player.getHull() <= 0) {
			System.out.println("\nDEFEAT");
			System.out.println("  Your ship, on its last legs, creaks audibly amidst the blaring alarms");
			System.out.println("  as the internal structures begin to fail. As a raging inferno engulfs the");
			System.out.println("  cockpit, you begin to contemplate your decisions in the battle, what you");
			System.out.println("  could have done to ensure that you and your crew survive. The temperature");
			System.out.println("  gets higher, your breaths shorter. You suddenly start thinking about your");
			System.out.println("  first day at the academy, how excited you were to join the Terran armed");
			System.out.println("  forces and lead a ship, maybe even a fleet, to victory. About how your");
			System.out.println("  friends and family supported your decision, maybe reluctantly, to enlist.");
			System.out.println("  How will they handle the news of your death? You begin to hope the");
			System.out.println("  Federation can carry on the fight without you, without your ship, when a");
			System.out.println("  sudden blast splits your frigate in half, leaving you and your crew");
			System.out.println("  together in death, in peace.");
		} else { //if(enemy.getHull() <= 0)
			System.out.println("\nVICTORY");
			System.out.println("  Lining up your crosshairs on the target, you pull the trigger and");
			System.out.println("  deliver a coup de grace on the disintegrating enemy frigate. Running a");
			System.out.println("  scan on your ship's systems, you conclude that everything is still");
			System.out.println("  operational; you were the victor. You quietly celebrate to yourself before");
			System.out.println("  putting full power into your ship's impulse engines and searching for");
			System.out.println("  another enemy to engage. It's only the first engagement in the battle, of");
			System.out.println("  course, and you're confident in your ability to bring down another");
			System.out.println("  adversary in the name of the Federation.");
		}
	}
}
