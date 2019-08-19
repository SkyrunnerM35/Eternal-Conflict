import java.util.ArrayList;

public class Ship extends Craft {

	private boolean isDodging;
	private boolean pdActive;
	private int[] munitions;
	private String[] weapons;
	private ArrayList<Drone> drones;
	private boolean dronesLaunched;
	private boolean justRepaired;
	
	public Ship(double hullI, double armorI, double shieldI, double shieldRegenI, double[] armorResistI, double[] shieldResistI, double evasionI, int[] munitionsI, String[] weaponsI) {
		super(hullI, armorI, shieldI, shieldRegenI, armorResistI, shieldResistI, evasionI);
		isDodging = false;
		pdActive = true;
		munitions = munitionsI;
		weapons = weaponsI;
		drones = new ArrayList<Drone>();
		setDrones(5);
		dronesLaunched = false;
		justRepaired = false;
	}
	
	public void toggleDodging() {
		isDodging = !isDodging;
	}
	
	public boolean getPDState() {
		return pdActive;
	}
	
	public void togglePD() {
		pdActive = !pdActive;
	}
	
	public int getMunitions(int i) {
		return munitions[i];
	}
	
	public void setMunitions(int i, int munitionCount) {
		munitions[i] = munitionCount;
	}
	
	public void consumeMunitions(int i) {
		if(munitions[i] == 0) {
			return;
		}
		munitions[i]--;
	}
	
	public String getWeapon(int i) {
		return weapons[i];
	}
	
	public void setWeapons(String[] newWeapons) {
		weapons = newWeapons;
	}
	
	public boolean hasWeapon(String weaponID) {
		for(int i = 0; i < weapons.length; i++) {
			if(weapons[i].equals(weaponID)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean dronesActive() {
		return dronesLaunched;
	}
	
	public void toggleDrones() {
		dronesLaunched = !dronesLaunched;
	}
	
	public Drone getDrone(int i) {
		return drones.get(i);
	}
	
	public int getActiveDrones() {
		return drones.size();
	}
	
	public void setDrones(int droneCount) {
		drones.clear();
		for(int i = 0; i < droneCount; i++) {
			drones.add(new Drone(1, 2, 0, 0, new double[] {0, .25, .5, 0}, new double[] {.5, .25, 0, 0}, .75));
		}
	}
	
	public void removeDestroyedDrones() {
		for(int i = 0; i < drones.size(); i++) {
			if(drones.get(i).getHull() <= 0) {
				drones.remove(i);
				i--;
				System.out.println("    Drone Destroyed");
			}
		}
	}
	
	public boolean getRepairStatus() {
		return justRepaired;
	}
	
	public void toggleRepair() {
		justRepaired = !justRepaired;
	}
	
	public void repairPD() {
		if(justRepaired && !getPDState()) {
			togglePD();
			toggleRepair();
		}
	}
	
	public String getDroneString() {
		String result = "";
		for(int i = 0; i < drones.size(); i++) {
			result += drones.get(i).toString() + " ";
		}
		return result;
	}
	
}
