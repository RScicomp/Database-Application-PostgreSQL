/*	Authored by:
 * 		Daniel Busuttil,	260608427
 *		Leila Erbay,		260672158 
 * 		Richard Gao,		260729805
 * 		Adam Gover,			?
 */

import java.util.Random;

public class nameGenerator {
	Random rng = new Random();
	
	// Each of the following arrays will be used to generate datapoints for our database
	private String firstNames[] = {	"Barbara", "Eva", "Yuriko", "Peace", "Jackie", "Natasha", "Inez",
									"Valentine", "Stacy", "Santiago", "Aaren", "Nour", "Yuu", "Steve",
									"Hakim", "Chester",	"Jorge", "Felipe", "Zack", "Owen", "Calvin", "Edgar",
									"Ellen", "Leila", "Susan", "Elliot" };
	private String lastNames[] = {	"Smith", "Darby", "Ikeda", "Williams", "Brown", "Jones", "Laszlo",
									"Harkin", "Miller", "Jackson", "Gao", "Ye", "Chang", "Ali", "Satoshi",
									"Davis", "Garcia", "Samuel", "Mendez", "Rodriguez", "Esteban", "Fry",
									"Tao", "Abdelnour", "Tazri", "Juarez" };
	public int firstNamesLen = firstNames.length;
	public int lastNamesLen = lastNames.length;
	
	// Used for address gen.
	private String addrNames[] = {	"College", "Maple", "Victoria", "Bellevue", "Douglas", "Main", "Church",
									"Park", "Winston", "Aspen", "Davis", "Windsor", "Station", "Littlegate",
									"Market", "River", "Bedford", "Exeter", "Needless", "Seventh" };
	private String addrSuffix[] = {	"Place", "Street", "Boulevard", "Way", "Square", "Plaza" };
	public int addrNamesLen = addrNames.length;
	public int addrSuffixLen = addrSuffix.length;
	
	// Used to pick a City at random
	private String cityNames[] = {	"London", "Ottowa", "Kansas", "Berlin", "Moscow", "Beijing", "Nairobi",
									"Mexico City", "Lima", "Riyadh", "Los Angeles", "Islamabad", "Beirut",
									"Singapore", "Cairo", "Accra", "Victoria", "Cape Town", "Prague", "Oslo" };
	public int cityNamesLen = cityNames.length;
	
	// Used to pick a Bank at random
	private String bankNames[] = {	"TD", "HSBC", "Scotiabank", "Barclays", "RBC" };
	public int bankNamesLen = bankNames.length;
	
	
	// Methods used to simulate data:
	
	// Generates a first and last name:
	public String genName() {
		String result = "";
		rng = new Random();
		result += firstNames[ rng.nextInt( firstNamesLen ) ] + " "; // Add a first name
		result += lastNames[ rng.nextInt( lastNamesLen) ];
		
		//System.out.println("Gen '" + result + "'");
		return result;
	}
	
	// 'Randomly' generates an address to be used
	public String genAddr() {
		String result = "";
		rng = new Random();
		result += Integer.toString(rng.nextInt(1000) + 1) + " ";
		result += addrNames[ rng.nextInt( addrNamesLen ) ] + " ";	// Add a the address name
		result += addrSuffix[ rng.nextInt( addrSuffixLen ) ];		// Add the suffix
		
		//System.out.println("Gen '" + result + "'");
		return result;
	}
	
	public String getCity() {
		rng = new Random();
		return cityNames[ rng.nextInt( cityNamesLen ) ];
	}
	
	public String getBank() {
		rng = new Random();
		return bankNames[ rng.nextInt( bankNamesLen ) ];
	}
	
	public static void main(String[] args){
	}  
}






