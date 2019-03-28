/*	Authored by:
 * 		Daniel Busuttil,	260608427
 *		Leila Erbay,		260672158
 * 		Richard Gao,		260729805
 * 		Adam Gover,			?
 */

import java.util.Random;

public class numGenerator {
	Random rng = new Random();
	
	// Used to generate phone numbers (taken from USA state codes)
	int[] areaCodes = { 514, 907, 334, 432, 307, 303, 808, 518, 701, 906 };
	int areaCodesLen = areaCodes.length;
	
	// Generates a 9-digit long
	public long genPhoneNum() {
		rng = new Random();
		// Picks the first 3 digits
		long result = 1000000 * areaCodes[ rng.nextInt( areaCodesLen ) ];
		result += rng.nextInt( 1000 ) * 1000;
		return result += rng.nextInt( 1000 );
	}
	
	// Generates a YYYY-MM-DD date String to be used later
	public String genDate() {
		String result = "";
		rng = new Random();
		result += Integer.toString( rng.nextInt(25) + 1980 ) + "-";
		
		// Perform this logic to avoid problems with months having different days
		int month = rng.nextInt(12) + 1;
		int dayLim = ( month == 2 ) ? 27 : 30;
		result += Integer.toString( month ) + "-";
		return result += Integer.toString( rng.nextInt( dayLim ) + 1);
	}
	
	// Generates a HH:MM:SS (24-hour thus) time String to be used later
	public String genTime() {
		rng = new Random();
		
		// If we gen. numbers less than 10 we'll need to pad the string to legally pass to SQL
		int hour = rng.nextInt(24);
		String result = (( hour < 10 ) ? "0" + Integer.toString( hour ) :  Integer.toString( hour ) ) + ":";
		
		// Ditto
		int minute = rng.nextInt(60);
		return result += (( minute < 10 ) ? "0" + Integer.toString( minute ) :  Integer.toString( minute ) ) + ":00";
	}

	
	
	public static void main(String[] args){
		numGenerator numGen = new numGenerator();
		for( int i = 0; i < 60; i++) {
			System.out.println( "Generated: '" + numGen.genPhoneNum() + "'");
		}
	}  
}
