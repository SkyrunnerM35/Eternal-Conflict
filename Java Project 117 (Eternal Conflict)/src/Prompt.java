/**
 *  Prompt.java
 *  Provides utilities for user input.
 *  "Enhances" the Scanner class, so that
 *  our programs can recover from "bad" input,
 *  and also provide a way to limit numerical
 *  input to a range of values.  Methods for
 *  reading in Strings, ints, and doubles.
 *  @author Michael Yang
 *  @version 1.0
 *  @since 8/22/2017
 */
 
 import java.util.Scanner;

 public class Prompt {
	
	/**
	 *  Prompts the user and picks up a String.
	 *  @param ask       The String prompt to be displayed to the user.
	 *  @return          The String entered by the user.
	 */
	public static String getString (String ask) {
		Scanner in = new Scanner(System.in);
		System.out.print(ask);
		String input = in.nextLine();
		return input;
	}
	
	/**
	 *  Prompts the user and picks up an int.  Checks for
	 *  "bad" input and reprompts if not an int.
	 *  @param ask       The String prompt to be displayed to the user.
	 *  @return          The int entered by the user.
	 */
	public static int getInt (String ask) {
		boolean badInput = false;
		String input = new String("");
		int value = 0;
		do {
			badInput = false;
			input = getString(ask);
			try {
				value = Integer.parseInt(input);
			} catch(NumberFormatException e) {
				badInput = true;
			}
		} while(badInput);
		return value;
	}

	/**
	 *  Prompts the user and picks up an int.  Checks for
	 *  "bad" input and reprompts if not an int.  Also checks
	 *  for input within a given range, and reprompts if outside
	 *  that range.
	 *  @param ask       The String prompt to be displayed to the user.
	 *  @param min       The minimum integer value to be allowed as input.
	 *  @param max       The maximum integer value to be allowed as input.
	 *  @return          The int entered by the user.
	 */
	public static int getInt (String ask, int min, int max) {
		int value = 0;
		do {
			value = getInt(ask);
		} while(value < min || value > max);
		return value;
	}
	
	public static double getDouble (String ask) {
		boolean badInput = false;
		String input = new String("");
		double value = 0;
		do {
			badInput = false;
			input = getString(ask);
			try {
				value = Double.parseDouble(input);
			} catch(NumberFormatException e) {
				badInput = true;
			}
		} while(badInput);
		return value;
	}
	
	public static double getDouble (String ask, double min, double max) {
		double value = 0;
		do {
			value = getDouble(ask + " (" + min + "-" + max + "): ");
		} while(value < min || value > max);
		return value;
	}
	
	public static char getChar(String ask){
		boolean invalid = false;
		char term = ' ';
		do {
			String input = getString(ask);
			invalid = false;
			if(input.length() > 0) {
				term = input.charAt(0);
			} else {
				invalid = true;
			}
		} while(invalid);
		return term;
	}
	
	public static char getChar(String ask, char[] possible) {
		boolean invalid = true;
		char term = ' ';
		do {
			term = getChar(ask);
			for(int i = 0; i < possible.length; i++) {
				if(possible[i] == term) {
					invalid = false;
				}
			}
		} while(invalid);
		return term;
	}
	
		public static char getChar(String ask, String possible) {
		boolean invalid = true;
		char term = ' ';
		do {
			term = getChar(ask);
			for(int i = 0; i < possible.length(); i++) {
				if(possible.charAt(i) == term) {
					invalid = false;
				}
			}
		} while(invalid);
		return term;
	}
}
