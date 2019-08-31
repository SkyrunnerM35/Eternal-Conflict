/**
 * @author Michael Yang
 * @since   8/18/2019
 */
public class StatusEffect {

	private String type;
	private int turns;
	
	public StatusEffect(String typeI, int turnsI) {
		type = typeI;
		turns = turnsI;
	}
	
	public String getType() {
		return type;
	}
	
	public int getTurns() {
		return turns;
	}
	
	public void setTurns(int newTurns) {
		turns = newTurns;
	}
	
	public void decrementTurns() {
		turns--;
	}
}
