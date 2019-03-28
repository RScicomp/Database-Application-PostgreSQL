
import java.sql.*;
import java.util.*;
import java.lang.Exception.*;
import java.text.*;


/* Project Deliverable 3 (for McGill COMP 421) submitted by:
 *	Adam Gover			260657531
 *	Daniel Busuttil		260608427
 *	Leila Erbay			260672158
 *	Richard Gao			260729805
 */

/* Order of Functions
 * branchstats(Connection con, String inputBankName, PreparedStatement ps);
 * validateUserID(Connection con, Statement statement, int userID);
 * validateBankerID(Connection con, Statement statement, int bankerID); 		<--- needs to get done
 * 
 * validateCheckingsAcct(Connection con, Statement st, int acctID, int userID);
 * validateSavingsAcct(int acctID, int userID, Statement st);
 * validateWithdrawalAmt(double withdrawalAmt, int checkAcct, int userID);
 * validateDepositAmt(double depAmt, int acct, int userID, Statement st);
 * 
 * return int : displaySavingsAccts(int userID, Statement st) ;
 * return int : displaySavingsAccts(int userID, Statement st); 
 * return int : displayChkAcctBalance(int chkAcct, int userID, Scanner sc, Statement st);
 * return int : displaySavAcctBalance(int savAcct, int userID, Scanner sc, Statement st)
 * 
 * 
 * makeWithdrawal(double withdrawalAmt, int chkAcct, int userID, Statement st);
 * makeDeposit(double withdrawalAmt, int chkAcct, int userID, Statement st);
 * 
 * 
 * withdraw(Connection con, Statement statement, int userID, Scanner sc);
 * depositSavings(int savAcct, int userID, Scanner sc,Connection con,Statement st);
 * depositCheckings(int chkAcct, int userID, Scanner sc,Connection con,Statement st)
 * transfer(Connection con, Statement statement, int userID, Scanner sc);
 * 
 * validateNewName(String newName, int userID);							<-- needs to get done
 * validateUserNumber(int newNum);										<-- needs to get done
 * updateUserAddr(String newAddr, int userID, Connection con);			<-- needs to get done
 * 
 * updateInterestRate(Connection con, PreparedStatement p_stm)
 * 
 * main:
 * 	- customer (6 options)
 * 	- banker (5 options)
 * 
 * */


public class TextMenu {
	
	public static String accounts = "t_accounts";
	/*------------------------------------- ACCESS STORED PROCEDURE ---------------------------------------------------*/
	// Answer to Q1 of PD3.pdf
    public static void branchstats(Connection con, String inputBankName, PreparedStatement ps){ 
        
        try{
            ps = con.prepareStatement("SELECT * FROM branchstats(?)");
            ps.setString(1, inputBankName);
            
            ResultSet rs = ps.executeQuery();
            
            System.out.println("\n|---------------------|");
            System.out.println("  Bank Name: " + inputBankName );
            System.out.println("|---------------------|");
            
            System.out.println("\n|----------------------------------------------------------------------------------------|");
            //ERROR: saying that brid doesn't exist in query.... don't know how to fix.... -_-
            while(rs.next()){
                int brid = rs.getInt(1);
                int numAccounts = rs.getInt(2);
                int numBankers = rs.getInt(3);
                int numCustomers = rs.getInt(4);
                int numSavingsAccts = rs.getInt(5);
                int totalSavings = rs.getInt(6);
                int numCheckAccts = rs.getInt(7);
                int totalCheckBalance = rs.getInt(8);
                int numWithdrawals = rs.getInt(9);
                int totalWithdrawalAmt = rs.getInt(10);
                int numDeposits = rs.getInt(11);
                int totalDepositAmt = rs.getInt(12);
                int numTransfersOUT = rs.getInt(13);
                int totalTransferOUTAmt = rs.getInt(14);
                int numTransfersIN = rs.getInt(15);
                int totalTransferInAmt = rs.getInt(16);
                
               
                System.out.println("\t Branch: "+ brid);
                System.out.println("\t number of accounts: "+ numAccounts);
                System.out.println("\t number of bankers: "+ numBankers);
                System.out.println("\t number of customers: "+ numCustomers);
                System.out.println("\t number of savings accounts: "+ numSavingsAccts + "\t total savings: "+ totalSavings);
                System.out.println("\t number of checking accounts: "+ numCheckAccts + "\t total checkings: "+ totalCheckBalance);
                System.out.println("\t number of withdrawals : "+ numWithdrawals + "\t total checkings: "+ totalWithdrawalAmt);
                System.out.println("\t number of deposits : "+ numDeposits + "\t total deposits: "+ totalDepositAmt);
                System.out.println("\t number of transfers out of system : "+ numTransfersOUT + 
                                   "\t total amount of transfers out of system: "+ totalTransferOUTAmt);
                System.out.println("\t number of transfers into system : "+ numTransfersIN + 
                                   "\t total amount of transfers into system: "+ totalTransferInAmt);
                System.out.println("|-----------------------------------------------------------------------------------------------|\n");
                //MISSING total # of transactions and total amt of transactions
                
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    //Removed any statement.close()
    /*------------------------------------- VALIDATE EXISTENCE FUNCTIONS ---------------------------------------------------*/
    public static boolean validateUserID(Connection con, Statement statement, int userID){
    	int sqlCode = 0;
		String sqlState = "00000";
		String insertSQL = "INIT";
		boolean valid = false;
		
		// Inserting Data into the table
	    try {
	    	//insertSQL = "SELECT pidc FROM Customers WHERE pidC = " + userID + " ; ";
	    	insertSQL = "SELECT pidc FROM Customers;";
	    	ResultSet rs = statement.executeQuery ( insertSQL );
	    	while (rs.next())
	    	{
	    		if (rs.getInt(1) == userID) {
	    			valid = true;
	    		}
	    	
	    	}
	    	rs.close();

	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
        
        return valid;
    }
    
    
    
    public static boolean validateBankerID(Connection con, Statement statement, int bankerID){
    	int sqlCode = 0;
		String sqlState = "00000";
		String insertSQL = "INIT";
		boolean valid = false;
		
		// Inserting Data into the table
	    try {
	    	//insertSQL = "SELECT pidc FROM Customers WHERE pidC = " + userID + " ; ";
	    	insertSQL = "SELECT pidb FROM Bankers;";
	    	ResultSet rs = statement.executeQuery ( insertSQL );
	    	while (rs.next())
	    	{
	    		if (rs.getInt(1) == bankerID) {
	    			valid = true;
	    		}
	    	
	    	}
	    	rs.close();

	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
        
        return valid;
    }
    
    //Removed any statement.close()
    public static boolean validateCheckingsAcct(Connection con, Statement st, int acctID, int userID){
    	String query = "SELECT * FROM " + accounts + " WHERE aid = "+ acctID + " AND " + " pidc = "+userID+" AND checking = TRUE";
   	 	boolean valid = false;
    	try{
	    	
	    	// Execute query
	    	ResultSet rs = st.executeQuery(query);
	    	
	    	// iterate through resultset
	    	
	    	while(rs.next()){
	    		int aid = rs.getInt("aid");
	    		if (aid == acctID){
	    			valid = true;
	    			break;
	    		}
	    	}
	    	
	    	rs.close();
    	}
    	catch(SQLException e){
    		  int sqlCode = e.getErrorCode(); // Get SQLCODE
    	      String sqlState = e.getSQLState(); // Get SQLSTATE
    	      
    	      // Error handling
    	      System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
    	}
    	return valid;
    }
    
    /**
     * Validate Savings Account
     * @param acctID
     * @param userID
     * @param con
     * @return boolean
     * By: RG
     */
    public static boolean validateSavingsAcct(int acctID, int userID, Statement st){
    	//verify that the acct specified belongs to userID
    	String query = "SELECT * FROM " + accounts + " WHERE aid = "+ acctID + " AND " + " pidc = "+userID+" AND checking = FALSE";
   	 	boolean valid = false;
    	try{
	    	
	    	// Execute query
	    	ResultSet rs = st.executeQuery(query);
	    	
	    	// iterate through resultset
	    	
	    	while(rs.next()){
	    		int aid = rs.getInt("aid");
	    		if (aid == acctID){
	    			valid = true;
	    			break;
	    		}
	    	}
	    	rs.close();
    	}
    	catch(SQLException e){
    		  int sqlCode = e.getErrorCode(); // Get SQLCODE
    	      String sqlState = e.getSQLState(); // Get SQLSTATE
    	      
    	      // Error handling
    	      System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
    	}
    	return valid;
    	
    }
    
    /*------------------------------------- VALIDATE AMT FUNCTIONS ---------------------------------------------------*/
    
    public static boolean validateWithdrawalAmt(Connection con, Statement statement,double withdrawalAmt, int checkAcct, int userID){
        //connect to db to determine if withdrawalAmt < balance of checking account 
    	int sqlCode = 0;
		String sqlState = "00000";
		String insertSQL = "INIT";
		boolean valid = false;
		
		
	    try {
	    	insertSQL = "SELECT DISTINCT balance FROM "+accounts + " WHERE pidC = " + userID + " AND aID = " + checkAcct + " ; ";
	    	ResultSet rs = statement.executeQuery ( insertSQL );
	    	while (rs.next())
	    	{
	    	
	    		System.out.println(((double) rs.getInt(1))/100);
	    		if (((double) rs.getInt(1))/100 >= withdrawalAmt) {			
	    			valid = true;
	    		}
	    	
	    	}
	    	rs.close();

	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
        
        return valid;
	}
	
   /**
    * 
    * @param depAmt
    * @param acct
    * @param userID
    * @param con
    * @return boolean
    * By: RG
    */
   public static boolean  validateDepositAmt(double depAmt, int acct, int userID, Statement st){
	    //connect to db to determine if deposit amt in proportion to balance is valid? DONT KNOW IF THIS CHECK IS NECESSARY
	   double ratio = .5;
	   String query = "SELECT * FROM " + accounts + " WHERE aid = "+ acct + " AND " + " pidc = "+ userID;
  	   
	   boolean valid = true;
  	   try{

	    	
	    	// Execute query
	    	ResultSet rs = st.executeQuery(query);
	    	rs.next();
	    	int aid = rs.getInt("aid");
	    	int pidc = rs.getInt("pidc");
	    	double balance = rs.getDouble("balance");
	    	if(acct == aid && userID == pidc){
	    		if ((depAmt/rs.getDouble("balance")) > ratio){
	    			valid = false;
	    		}
	    	}
  	   }
  	   catch(Exception e){
  		   System.err.println("Exception: ");
  		   System.err.println(e.getMessage());
  	   }
  	   return valid;
	   
   }
    
    
    /*------------------------------------- DISPLAY FUNCTIONS ---------------------------------------------------*/
    public static int displayCheckingsAccts(int userID, Statement st){
        //print out all checkings account associated to userID
    	String query = "SELECT * FROM " + accounts+" WHERE pidc = "+ userID + " AND checking = TRUE";
   	 //	System.out.println(query);
    	
    	try{

    		
	    	// Execute query
	    	ResultSet rs = st.executeQuery(query);
	    	
	    	// iterate through resultset
	    	if(!rs.isBeforeFirst()){
	    		System.out.println("No accounts.Press enter to continue.\n");
	    		return -1;
	    	}

	    	//System.out.println("Displayed are your account's ID, currency, opendate, balance, name of bank, customer ID, banker ID and interestrate.");
	    	System.out.println("Displayed are your account's ID and currency\n");
	    	while(rs.next()){

				/*System.out.format("%s, %s\n", rs.getInt("aid"), rs.getString("currency"),
				rs.getDate("opendate"),rs.getInt("balance"),rs.getString("nameb"),rs.getInt("pidc"),
				rs.getInt("pidb"),rs.getInt("interestrate"));
				*/
				
				System.out.format("%s, %s\n", rs.getInt("aid"), rs.getString("currency"));
			}
			System.out.println("\n No other accounts available");
			
    	}
    	catch(Exception e){
    		System.err.println("Exception: ");
    		System.err.println(e.getMessage());
    		return -1;
    	}
    	return 1;
    }
    
    
    /**
     * Display Savings Acc
     * @param userID
     * @param con
     * By: RG
     */
    public static int displaySavingsAccts(int userID, Statement st) {
    	//print out all savings account associated to userID
    	String query = "SELECT * FROM "+accounts+ " WHERE pidc = "+ userID + " AND checking = FALSE";

    	try{

	    	// Execute query
	    	ResultSet rs= st.executeQuery(query);
	    	if(!rs.isBeforeFirst()){
	    		System.out.println("No accounts. Press enter to continue.\n");
	    		return -1;
	    	}

	    	
	    	//System.out.println("Displayed are your account's ID, currency, opendate, balance, name of bank, customer ID, banker ID and interestrate.");
	    	System.out.println("Displayed are your account's ID and currency\n");
	    	
	    	
	    	// iterate through resultset
			while(rs.next()){
				
				/*System.out.format("%s, %s\n", rs.getInt("aid"), rs.getString("currency"),
				rs.getDate("opendate"),rs.getInt("balance"),rs.getString("nameb"),rs.getInt("pidc"),
				rs.getInt("pidb"),rs.getInt("interestrate"));*/
				
				System.out.format("%s, %s\n", rs.getInt("aid"), rs.getString("currency"));
			}
			System.out.println("\n No other accounts available");
    	}
    	catch(Exception e){
    		System.err.println("Exception: ");
    		System.err.println(e.getMessage());
    		return -1;
    	}
    	return 1;
    }
    
    
    public static int displayChkAcctBalance(int chkAcct, int userID, Statement st){
    	//query to display info related to a checking account of a user
		//display everything except checking boolean in sql
		String query = "SELECT * FROM " + accounts + " WHERE pidc = "+ userID + " AND aid = "+chkAcct + " AND checking = TRUE";
   	 
    	try{

	    	// Execute query
	    	ResultSet rs = st.executeQuery(query);
	    	if(!rs.isBeforeFirst()){
	    		System.out.println("No accounts. \n");
	    		return -1;
	    	}

	    	// iterate through resultset
			//System.out.println("Displayed are your account's ID and currency\n");
	    	System.out.println("Displayed are your account's ID, currency, balance, name of bank, customer ID, banker ID and interestrate.\n");
	    	
	    	while(rs.next()){

	    		System.out.format("%s, %s,  %f, %s, %d, %d, %d \n", rs.getInt("aid"), rs.getString("currency"),
	    				 ((double)rs.getInt("balance"))/100,rs.getString("nameb"),rs.getInt("pidc"),
				rs.getInt("pidb"),rs.getInt("interestrate"));
	    	}
	    	System.out.println("\n No more data");
    	}
    	catch(Exception e){
    		System.err.println("Exception: ");
    		System.err.println(e.getMessage());
    		return -1;
    	}
    	return 1;
    }
    
    public static int displaySavAcctBalance(int savAcct, int userID, Statement st){
    	//query to display info related to a savings account of a user
    	//display everything except checking boolean in sql
		String query = "SELECT * FROM "+accounts+ " WHERE pidc = "+ userID + " AND aid = "+ savAcct +" AND checking = FALSE";
    	
    	try{

	    	// Execute query
	    	ResultSet rs= st.executeQuery(query);
	    	
	    	if(!rs.isBeforeFirst()){
	    		System.out.println("No accounts. \n");
	    		return -1;
	    	}
	    	
	    	// iterate through resultset

	    	System.out.println("Displayed are your account's ID, currency, balance, name of bank, customer ID, banker ID and interestrate.\n");
			while(rs.next()){
				
				//System.out.format("%s, %s\n", rs.getInt("balance"));
				System.out.format("%s, %s, %f, %s, %d, %d, %d \n", rs.getInt("aid"), rs.getString("currency"),
						((double)rs.getInt("balance"))/100,rs.getString("nameb"),rs.getInt("pidc"),
						rs.getInt("pidb"),rs.getInt("interestrate"));
				
			}
			System.out.println("\n No more data");
    	}
    	catch(Exception e){
    		System.err.println("Exception: ");
    		System.err.println(e.getMessage());
    		return -1;
    	}
    	return 1;
	}
    
    
    /*------------------------------------- SQL UPDATE FUNCTIONS ---------------------------------------------------*/   
    /**
     * Make withdrawal
     * 
     * @param depAmt
     * @param acct
     * @param userID
     * @param con
     * @return boolean
     * By: RG
     */
   public static void makeWithdrawal(double withdrawalAmt, int chkAcct, int userID, Statement st){
	   //connect to server to actually update withdrawals associated to that customer
	   //need to convert withdrawal amt to int = withdrawalAmt *10
	   String query = "UPDATE " + accounts +" SET balance = balance -" + (int)(withdrawalAmt*100 )+ " WHERE aid = " + chkAcct + " AND pidc = " + userID;
  	   try{
	    	
	    	// Execute update
	    	int executeReturn = st.executeUpdate(query);
	    	
  	   }
  	   catch(Exception e){
  		   System.err.println("Exception: ");
  		   System.err.println(e.getMessage());
  	   }
   }
   /**
    * Make Deposit
    * @param depositAmt
    * @param acctID
    * @param userID
    * @param con
    * By: RG
    */
   public static void makeDeposit(double depositAmt, int acctID, int userID, Statement st){
	 //connect to server to actually update withdrawals associated to that customer
	   //need to convert dep amt to int = depAmt *10
	   String query = "UPDATE " + accounts +" SET balance = balance +" + (int)(depositAmt*100)+ " WHERE aid = " + acctID + " AND pidc = " + userID;
  	   try{
	    	
	    	// Execute update
	    	int executeReturn = st.executeUpdate(query);
	    	
  	   }
  	   catch(Exception e){
  		   System.err.println("Exception: ");
  		   System.err.println(e.getMessage());
  	   }

	 
   }
   
   //Modified By: RG. Added Statement as parameter. Removed any statement.close()
   /*------------------------------------- WITHDRAW (COMBINED COMPONENTS) ---------------------------------------------------*/
  public static int withdraw(Connection con, Statement statement, int userID, Scanner sc){
	   System.out.println("Please pick a checkings account that you own and wish to withdraw from");
       displayCheckingsAccts(userID,statement);
       
      	
      int chkAcct = -1;
      try{ 
    	  chkAcct = sc.nextInt();
      } catch(InputMismatchException e){
         	System.out.println("E1:There was an error with your selection\n") ;
           //	sc.next();        	        
           	return -1;
      }     
      
       //validate chk account belongs to user ID
       if (validateCheckingsAcct(con, statement, chkAcct, userID) == false){
       	System.out.println(chkAcct + " does not belong to you. You did not enter a valid checking account.");
       	return -1;
       }

       System.out.println("List the amount of money that you will be withdrawing from your account");
       
       double withdrawalAmt = -1;
       try{
			withdrawalAmt =  sc.nextDouble();
		}catch(InputMismatchException e){
			System.out.println("There was an error in the amount you entered");
			return -1;
		}
    		  
      
       //determine if withdrawal amount will cause negative balance
       if (validateWithdrawalAmt(con, statement, withdrawalAmt, chkAcct, userID) == false) {
       	System.out.println("You did not enter a valid withdrawal amount. "
       				+ "No changes have been made to your account.");
       	return -1;
       }

       	makeWithdrawal(withdrawalAmt, chkAcct, userID,statement);
       	System.out.println("You're account " + chkAcct + " has been updated. " + withdrawalAmt +
       			" has been deducted from your account. " );
       
       return 1;
   }
  
  //Modified By: RG. Added Statement as parameter.Removed any statement.close()
  /*------------------------------------- DEPOSIT SAVINGS (COMBINED COMPONENTS) ---------------------------------------------------*/
  public static int depositSavings(int savAcct, int userID, Scanner sc,Connection con,Statement st){	
		System.out.println("Please enter the amount of money you would like to deposit");			//is there a limit to how money can be deposited?
		double depAmt = -1;
		
		try{
			depAmt = sc.nextInt();
		}catch(InputMismatchException e){
			System.out.println("There was an error in the amount you entered");
			return -1;
		}
		
		if (validateDepositAmt(depAmt, savAcct, userID,st) == false) {
			System.out.println("You did not enter a valid amount to deposit.");
			return -1;
		}
				
		makeDeposit(depAmt, savAcct, userID,st);
		System.out.println("Your account "+ savAcct + " has been updated.");
		return 1;
  }
 
 //Modified By: RG. Added Statement as parameter
  /*------------------------------------- DEPOSIT CHECKINGS (COMBINED COMPONENTS) ---------------------------------------------------*/
	 public static int depositCheckings(int chkAcct, int userID, Scanner sc, Connection con,Statement st){
		System.out.println("Please enter the amount of money you would like to deposit");			//is there a limit to how money can be deposited?
		double depAmt = -1;
		
		try{
			depAmt = sc.nextInt();
		}catch(InputMismatchException e){
			System.out.println("There was an error in the amount you entered");
			return -1;
		}
				
		if (validateDepositAmt(depAmt, chkAcct, userID,st) == false) {
			System.out.println("You did not enter a valid amount to deposit.");	
			return -1;
		}
	
		makeDeposit(depAmt, chkAcct, userID,st);
		System.out.println("Your account "+ chkAcct + " has been updated.");
		return 1;
	 		                     
	 }
    //Modified By: RG. Added Statement as parameter
	/*------------------------------------- TRANSFER (COMBINED COMPONENTS) ---------------------------------------------------*/
	public static int transfer(Connection con, Statement statement, int userID, Scanner sc){
		//some amount of money is withdrawn. 
		System.out.println("Please pick a checkings account that you own and wish to withdraw from for your transfer");
	    displayCheckingsAccts(userID,statement);
	    
	    int chkAcct = -1;
	      try{ 
	    	  chkAcct = sc.nextInt();
	      } catch(InputMismatchException e){
	         	System.out.println("E2:There was an error with your selection\n") ;
	           //	sc.next();        	        
	           	return -1;
	      }     
	    
	    //validate chk account belongs to user ID
	    if (validateCheckingsAcct(con, statement, chkAcct, userID) == false){
	    	System.out.println(chkAcct + " does not belong to you. You did not enter a valid checking account.");
	    	return -1;
	    }
	
	    System.out.println("List the amount of money that you will be withdrawing from your account for your transfer");
		double withdrawalAmt = -1;
	      try{ 
	    	  withdrawalAmt = sc.nextDouble();;
	      } catch(InputMismatchException e){
	         	System.out.println("E3:There was an error with your selection\n") ;
	           //	sc.next();        	        
	           	return -1;
	      }     
		
	   
	    //determine if withdrawal amount will cause negative balance
	    if (validateWithdrawalAmt(con, statement, withdrawalAmt, chkAcct, userID) == false) {
	    	System.out.println("You did not enter a valid withdrawal amount. "
	    				+ "No changes have been made to your account.");
	    	return -1;
	    }
	    					
	    makeWithdrawal(withdrawalAmt, chkAcct, userID,statement);
	    System.out.println("You're account " + chkAcct + "has been updated. " + withdrawalAmt +
	    			" has been deducted from your account. " );
	    
	    
	    System.out.println("Who will you be transferring to? Please enter their customerID");
		int receiverID = -1;
		
		try{
			receiverID = sc.nextInt();
		}catch(InputMismatchException e){
			System.out.println("There was an error in the ID you entered");
			return -1;
		}
		if(validateUserID(con, statement, receiverID)==false) return -1;
		
		 System.out.println("What account will you be depositing to? Please enter account number");
		 int receiverAcct = sc.nextInt();
		
		 //if receiver account doesn't exist in his set of checkings or savings -- invalid  
		 if (validateCheckingsAcct(con, statement, receiverAcct, receiverID) == false && validateSavingsAcct(receiverAcct, receiverID,statement) == false){
			 System.out.println("Account " + receiverAcct + " does not exist under customer" + receiverID);
			 return -1;
		 }
		 else if (validateCheckingsAcct(con, statement, receiverAcct, receiverID)){	//transferring into a checking acct
			depositCheckings(receiverAcct, receiverID, sc,con,statement);
		 }	
	   
		 else if (validateSavingsAcct(receiverAcct, receiverID,statement)){		//transferring into a savings acct
			depositSavings(receiverAcct, receiverID,  sc,con,statement);
		 }
	   
	    else{
	        System.out.println("You did not enter a correct option");
	    }
	    
		return 1;
	}
 
	
	
	
	
/*------------------------------------------------ USER and BANK PERSONAL SETTINGS -----------------------------------------------*/	
	
	/*------------------------------------- VALIDATE SETTINGS ---------------------------------------------------*/
	public static boolean validateNewName(String newName, int userID){
		boolean valid = false;
		if (newName.length() > 10 && newName.matches("\\w{2,}\\s\\w{2,}")){
			valid = true;
		}
		else{
			System.out.println("The name that you entered is invalid. Names must have a first and last compoent separated by a white space and total length of at least ten");
		}
		//first / name is necessary? <-- requires at least 1 white space and length of 10
		//parse by space, if there is no space or more than 3 spaces --> invalid,
		//check that length of each component check has at least 2 chars
	
		return valid;
	}

	public static boolean validateUserNumber(long newNum){
		String len = "" + newNum;
		if (len.length() != 10){
 			System.out.println("The number that you entered is invalid");
 			return false;
		}
		return true;
	}
	
	
	public static boolean validateUserAddr(String newAddr){
		//parse into 3 parts
 		//part 1 = number // part 2= name of street  // part 3 = suffix (street, blvd, ave, etc)
		boolean valid = false;
		if(newAddr.matches("\\d+[ ](?:[A-Za-z0-9.-]+[ ]?)+(?:Avenue|Lane|Road|Boulevard|Drive|Street|Ave|Dr|Rd|Blvd|Ln|St|ave|blvd|ln|st|avenue|lane|road|boulevard|drive|street|ave)\\.?")) {
			valid = true;
		}
		else {
			System.out.println("The address that you entered is invalid. Format:Number String [St|Ave|dr|etc]");
		
		}
		return valid;
	}
	
	/*------------------------------------- UPDATE USER SETTINGS ---------------------------------------------------*/
	/**
	 * Update name
	 * @param newName
	 * @param userID
	 * @param con
	 * By: Richard Gao
	 */
	public static void updateName(String newName, int userID, Connection con, PreparedStatement stm){
		//sql update
		
		String update = "UPDATE customers SET name = ? WHERE pidc =?";
		try{
			stm = con.prepareStatement(update);
			stm.setString(1,newName);
			stm.setInt(2,userID);
		}
		catch(SQLException e){
			int sqlCode = e.getErrorCode(); // Get SQLCODE
   	    	String sqlState = e.getSQLState(); // Get SQLSTATE
   	      
   	      	// Error handling
   	    	System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
   	
		}
	}
	
	/**
	 * By: RG
	 */
	public static void updateUserNumber(long newNum, int userID, Connection con, PreparedStatement stm){
		//sql query into customers and update phone number
		//sql query into customers and update phone number
		String update = "UPDATE customers SET phonenum = ? WHERE pidc =?";
		try{
			 stm = con.prepareStatement(update);
			stm.setLong(1,newNum);
			stm.setInt(2,userID);
			int state = stm.executeUpdate();
			
			stm.close();
		}
		catch(SQLException e){
			int sqlCode = e.getErrorCode(); // Get SQLCODE
   	    	String sqlState = e.getSQLState(); // Get SQLSTATE
   	      
   	      	// Error handling
   	    	System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
   	
		}
	}
    
	/**
	 * By: RG
	 */
	public static void updateUserAddr(String newAddr, int userID, Connection con){
		//sql update
		//sql update
		String update = "UPDATE customers SET addr = ? WHERE pidc =?";
		try{
			PreparedStatement stm = con.prepareStatement(update);
			stm.setString(1,newAddr);
			stm.setInt(2,userID);
			int state = stm.executeUpdate();
			
			stm.close();
		}
		catch(SQLException e){
			int sqlCode = e.getErrorCode(); // Get SQLCODE
   	    	String sqlState = e.getSQLState(); // Get SQLSTATE
   	      
   	      	// Error handling
   	    	System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
   	
		}
		
	}
	
/*-------------------------------------------- BANKER HELPER FXNS ------------------------------------------------------*/	
	public static void updateInterestRate(Connection con, PreparedStatement p_stm){
		String prepStmt2 = "SELECT * FROM updateInterestRate();";
		
		try{
			p_stm = con.prepareStatement(prepStmt2);
			int state = p_stm.executeUpdate();
			if (state == 0 ) System.out.println("No changes were needed to be made. All records already up to date");
			if (state > 0) System.out.println("Records updated");
			
		}
		catch(SQLException e){
			int sqlCode = e.getErrorCode(); // Get SQLCODE
   	    	String sqlState = e.getSQLState(); // Get SQLSTATE
   	      
   	      	// Error handling
   	    	System.out.println("BANKERS: Code: " + sqlCode + "  sqlState: " + sqlState);
   	
		}
		
	}
	
	
	
	
	// Driver code:
	public static void main(String args []) throws SQLException, InputMismatchException {
        // Register driver

        try{ 
            DriverManager.registerDriver(new org.postgresql.Driver());
        }
        catch (Exception cnfe) {
            System.out.println("Class not found");
        }
        
        // Connect to server
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        Connection con = DriverManager.getConnection (url, "cs421g21", "N2a0r1ut6o"); // Password was removed for security reasons
        Statement statement = con.createStatement();
        
        
        
        
        // Declare variables needed inside the loop
        Scanner sc = new Scanner(System.in).useDelimiter("\\n");;
        int opt = 0;
        int userID = -1;
        boolean continue_loop = true;
        int ctr = -1;
        String prefix = "t_";
        PreparedStatement p_stm = con.prepareStatement("");
  
        
    	// Prompt user for identification
        do {
        	
	        try{
		        System.out.println("Are you a customer (1) or working for a bank (2)?");
		        // TODO: VALIDATE USER INPUT
		        opt = sc.nextInt();
	        }catch(RuntimeException e){
	        	System.out.println("Please enter either (1) for customer or (2) for banker\n") ;
	        	sc.next();
	            continue;
	        }
	        
        }while(opt!= 1 && opt !=2);
        
        
 /*-------------------------------------------------- CUSTOMER ------------------------------------------------------*/
        if (opt == 1){
        	
        	ctr = 3;
        	do{	
        		 try{
        			 System.out.println("Please enter your customer ID");	        	 
     	        	userID = sc.nextInt();
     	        	if (validateUserID(con, statement, userID) == true) break;	
     	        	ctr--;
     	        }catch(RuntimeException e){
     	        	System.out.println("There was an error with your customer ID\n") ;
     	        	sc.next();
     	        	ctr--;
     	            continue;
     	        }        	
	           
	        }while(ctr > 0);
	        
	        if (ctr == 0){
	        	System.out.println("Sorry you've attempted too many times. Please rerun the application");
	        	 sc.close();
	        	 statement.close();
	             p_stm.close();
	             con.close();
	            
	             return;
	        }
            
	        opt = -1;
            boolean flag = false;
            while(continue_loop){
            	 //opt = -1;
            	// Loop until user provides valid option
                do{
                	try{
                		System.out.println("Please pick from the following options: ");
                        
                        System.out.println("\t 1. Withdrawal");
                        System.out.println("\t 2. Deposit");
                        System.out.println("\t 3. Transfer");
                        System.out.println("\t 4. Check Account Balances");
                        System.out.println("\t 5. Update Personal Settings");
                        System.out.println("\t 6. Quit");
                        
                        opt = sc.nextInt();
                		flag =true;
                        
        	        }catch(RuntimeException e){
        	        	//System.out.println("There was an error with your selection\n") ;
        	        	sc.next();        	        
        	        	continue;
        	        }        	

                } while((opt < 1 || opt > 6)  && flag == false);
                
                
                switch(opt){
                    case 1:			//WITHDRAW
                        
                    
                    	if (withdraw(con, statement, userID, sc) == -1) {
                    		//sc.next();
                    		continue;
                    	}
                    	
                    	break;
                    case 2:		//DEPOSIT
                    	System.out.println("Which kind of account would you like to deposit to? \t (1) Savings \t (2) Checkings");
                    	int userInput = -1;
                    	int savAcct = -1;
                    	
                    	//Check that input for savings or checking is valid
                    	
                    
	                    	try{
	                    		userInput = sc.nextInt();
	                    	}catch(RuntimeException e){
	            	        	System.out.println("E4:There was an error with your selection\n") ;
	            	        	sc.next(); 
	            	        	continue;
	            	        }    
                    	
                    	
                    		//deposit into Savings -- maybe add do while loop later
	                    if (userInput == 1) {
	                    	System.out.println("Which savings account would you like to deposit money into?");
	                    
	                    	
	                    	if (displaySavingsAccts(userID,statement) == -1) continue;
	                    	
	                    	//check that input for savings or checking is valid
	                    	
		                    	try{
		                    		savAcct = sc.nextInt(); 
		                    	
		                    	}catch(RuntimeException e){
		            	        	System.out.println("E5:There was an error with your selection\n") ;
		            	        	sc.next(); 
		            	        	continue;
		            	        }    
	                    
	                    	
	                    	
	                    	if (!validateSavingsAcct(savAcct, userID,statement)) {
	                    			System.out.println("We can not find the account number you entered");
	                    			sc.next(); 
	                    			continue;
	                    	}
	                    	depositSavings(savAcct, userID, sc,con,statement);
	                    }
	                        
	                    else if (userInput == 2) {
	                    	 
	                    	System.out.println("Which checking account would you like to deposit money into?");
	                    	if(displayCheckingsAccts(userID,statement) == -1) continue;
	                    	int chkAcct = -1;
	                    	
	                    	
		                    	try{
		                    		chkAcct = sc.nextInt();
		                    	
		                    	}catch(RuntimeException e){
		            	        	System.out.println("E6:There was an error with your selection\n") ;
		            	        	sc.next(); 
		            	        
		            	        	continue;
		            	        }    
	                    	
	                    	
	                    	 	
	                    	if (validateCheckingsAcct(con, statement, chkAcct, userID) == false){
	                    		System.out.println("We can not find the account number you entered");
	                    		sc.next(); 
	                    		continue;
	                    	 }
	                    	
	                    	depositCheckings(chkAcct, userID, sc,con,statement);
	                    }	
	                    else{
	                        System.out.println("You did not enter a correct option");
	                    }
	                    
                        break;
                        
                    case 3:		//TRANSFER
                    	System.out.println("1");
                    	if (transfer(con, statement, userID, sc) == -1){
                    		System.out.println("Sorry there was an error in processing your transfer\n");
                
                    		continue;
                    	}
                    	System.out.println("2");

                        break;
                        
                    case 4:	//DISPLAY ACCOUNT INFO
                    	
                    	System.out.println("Which kind of account would you like to review? \t (1) Savings \t (2) Checkings");
                    	savAcct = -1;
                    	int ctrDisplay = 3;
                    	userInput = -1;
                    	
                    	
	                    try{
	                    		userInput = sc.nextInt();
	                    	
	                    }catch(RuntimeException e){
	            	        System.out.println("E7:There was an error with your selection\n") ; 
	            	        sc.next(); 
	            	        continue;
	            	    }    
                    	
                    	
                        
                    		//deposit into Savings -- maybe add do while loop later
	                    if (userInput == 1) {
	                    	System.out.println("Which savings account would you like review");
	                    	displaySavingsAccts(userID, statement);
	                    	
	                    	
	                    	
		                    try{
		                    	savAcct = sc.nextInt(); 
		                    	
		                    }catch(RuntimeException e){
		            	        System.out.println("E8:There was an error with your selection\n") ;
		            	        continue;
		            	    }    
	                    	
	                    	
	                    	if (validateSavingsAcct(savAcct, userID,statement) == false) {
	                    			System.out.println("We can not find the account number you entered");
	                    			continue;
	                    	}
	                    	displaySavAcctBalance(savAcct, userID,statement);
	                    }
	                        
	                    else if (userInput == 2) {
	                    	int chkAcct = -1;
	                    	
	                    	System.out.println("Which checking account would you like to review");
	                    	displayCheckingsAccts(userID,statement);
	                    	
	                    	//check that the input is an integer
	                    	
		                    try{
		                    	chkAcct = sc.nextInt();
		                    	
		                    }catch(RuntimeException e){
		            	       	System.out.println("E9:There was an error with your selection\n") ;
		            	       	sc.next(); 
		            	       	continue;
		            	    }    
	                    	
	                    
		                  
	                    	 	
	                    	if (validateCheckingsAcct(con,statement,chkAcct, userID) == false) {
                    			System.out.println("We can not find the account number you entered");
                    			sc.next(); 
                    			continue;
	                    	}
	                    	
	                    	displayChkAcctBalance(chkAcct, userID,statement);
	                    }
	                    else{
	                    	 System.out.println("You did not enter a correct option");
	                    }
                        break;
                        
                        
                    case 5:
                    	//menu for different things of personal info that can be altered:
                    	int menuOpt= -1;
                    	int menuCtr = 3;
                    	boolean continue_loop2 = true;
                    	
                    	while(continue_loop2){
	                    	System.out.println("Please pick which of the following you would like to update or alter");
	                    	               
	                    	 System.out.println("\t 1. Name");
	                         System.out.println("\t 2. Address");
	                         System.out.println("\t 3. Phone number");
	                         System.out.println("\t 4. Return back to main menu");
	                         
	                       try{
			                   		menuOpt = sc.nextInt();
			                   	
			                   	}catch(RuntimeException e){
			           	        	System.out.println("E10:There was an error with your selection\n") ;
			           	        	continue;
			                   	}
	                         
	                         
	                         switch(menuOpt){
	                         	case 1:		//update name
	                         		System.out.println("Please enter a new name that you would like us keep on file");
	                         		String newName = "";	                         			                       
	                         		
	                         		
	     			                try{
	     			                   		newName = sc.next();
	     			                   	}catch(RuntimeException e){
	     			           	        	System.out.println("E11:There was an error with your selection\n") ;
	     			           	        	sc.next();         	  
	     			           	        	continue;
	     			           	        }    
	     		                   
	                         		
	                         		
	                         		if (validateNewName(newName, userID) == false) continue;
	                         		
	                         		updateName(newName, userID,con, p_stm);
	                         		
	                         		break;
	                         		
	                         
	                         	case 2:	//update street address
	                         		System.out.println("Please enter a new street address that you would like us keep on file");
	                         		String newAddr = sc.next();
	                         		
	                         		if (validateUserAddr(newAddr) == false) continue;
	                         		updateUserAddr(newAddr, userID, con);
	                         		
	                         		break;
	                         		
	                         		
	                         	case 3:		//update number
	                         		System.out.println("Please enter a new phone number that you would like us keep on file");
	                         		long newNum = -1;
	                         		
	                         		
	     			                   try{
	     			                   		newNum = sc.nextLong();
	     			                   	
	     			                   	}catch(RuntimeException e){
	     			           	        	System.out.println("E12:There was an error with your selection\n") ;
	     			           	        	sc.next(); 
	     			           	        	continue;
	     			           	        }    
	     		                   	
	                         		
	                         		
	                         		
	                         		if (validateUserNumber(newNum) == false) continue;
	                         		updateUserNumber(newNum, userID, con, p_stm);
	                         		
	                         		break;
	                         		
	                         		
	                         	case 4: 		//exit to main menu
	                         		continue_loop2 =false;
	                         		break;
	                         }
                    	}
                        break;
                        
                    case 6:
                        System.out.println("Thank you and have a good day!");
                        continue_loop = false;
                        
                }
            }
        }
        
        
        /*---------------------------------- BANK ---------------------------------------------*/
        else if(opt == 2){  
            // if we want banks to be able to look at their data :/
        	
        	ctr = 3;
        	do{	   
		        try{
		   			 System.out.println("Please enter your banker ID");	        	 
			        	userID = sc.nextInt();
			        	if (validateBankerID(con, statement, userID) == true) break;	
			        	ctr--;
			     }catch(RuntimeException e){
		        	System.out.println("There was an error with your banker ID\n") ;
		        	sc.next();
		        	ctr--;
		            continue;
			       }        	
	          
		     }while(ctr > 0);
	       
	       if (ctr == 0){
	    	   System.out.println("Sorry you've attempted too many times. Please rerun the application");
	       	 	sc.close();
	       	 	statement.close();
	            p_stm.close();
	            con.close();
	           
	            return;
	       }
	        	
            
            while(continue_loop){
            	// Loop until user provides valid option
                do{
                    System.out.println("Please pick from the following options: ");
                    
                    System.out.println("\t 1. See Managed Accounts");
                    System.out.println("\t 2. Check Bank Activity");
                    System.out.println("\t 3. Increase Interest Rate for Loyalty Customers");
                    System.out.println("\t 4. Update Personal Settings");
                    System.out.println("\t 5. Quit");
                    
                    // TODO: VALIDATE USER INPUT
                    
	                 try{
	                	 opt = sc.nextInt();

	                   	
	                  }catch(RuntimeException e){
	           	       	System.out.println("E12:There was an error with your selection\n") ;
	           	       	sc.next(); 
	           	       	continue;
	           	       }    
                    
                    
                }
                while(opt < 1 || opt > 5);
                
                
                switch(opt){
                	// Banker wants to monitor Accounts they are responsible for
                    case 1:
					try{
						
					
						//String query = "SELECT * FROM banker_"+userID+"_view";
						String query = "SELECT * FROM t_accounts WHERE pidb = "+userID + ";";
						p_stm = con.prepareStatement(query);
						ResultSet rs = p_stm.executeQuery();
					
						while(rs.next()){
						
						System.out.format("%s,%s,%s,%s,%s,\n", rs.getInt("aid"), rs.getString("currency"), rs.getDate("opendate"),rs.getInt("balance"),
								rs.getInt("pidc"),rs.getBoolean("checking"), rs.getInt("interestrate"));
					
						}
						System.out.print("\n");
						rs.close();
					}catch(SQLException e){
						int sqlCode = e.getErrorCode(); // Get SQLCODE
						   String sqlState = e.getSQLState(); // Get SQLSTATE
						 
							 // Error handling
						   System.out.println("BANKERS: Code: " + sqlCode + "  sqlState: " + sqlState);
						
					}
                        break;
                        
                    case 2:
						String query = "SELECT nameb FROM bankers WHERE pidb = " + userID + ";";
						p_stm = con.prepareStatement(query);
						ResultSet rs = p_stm.executeQuery();
						rs.next();
						String name_b = rs.getString("nameb");
						branchstats(con,name_b,p_stm);
						rs.close();
	
						break;
                    
                    	
                    // Leila's addition
                    case 3:
                    	//update interest rate cursor
                    	String prepStmt2 = "SELECT updateInterestRate();";
                		try{
                			p_stm = con.prepareStatement(prepStmt2);
                			ResultSet rsp = p_stm.executeQuery();
                			
                		}
                		catch(SQLException e){
                			int sqlCode = e.getErrorCode(); // Get SQLCODE
                   	    	String sqlState = e.getSQLState(); // Get SQLSTATE
                   	      
                   	      	// Error handling
                   	    	System.out.println("BANKERS: Code: " + sqlCode + "  sqlState: " + sqlState);
                   	
                		}
                  
                        break;
                    
                    case 4:
                    	int menuOpt;
                    	boolean continue_loop2 = true;
                    	while(continue_loop2){
                    		System.out.println("Please pick which of the following you would like to update or alter");
                    		System.out.println("\t 1. Name");
                			System.out.println("\t 2. Address");
                			System.out.println("\t 3. Phone number");
                			System.out.println("\t 4. Return back to main menu");
	                        
                			// TODO: exception wrap:
	                        
	                        
	                       try{
	                    	   menuOpt = sc.nextInt();

	   	                   	
	   	                  }catch(RuntimeException e){
	   	           	       	System.out.println("E12:There was an error with your selection\n") ;
	   	           	       	sc.next(); 
	   	           	       	continue;
	   	           	       }    
	                        
	                        
	                         
	                        switch(menuOpt) {
	                         	case 1:		// Update name
	                         		System.out.println("Please enter a new name that you would like us keep on file");
	                         		String newName = "";
	                         		
	                         		 try{
	                         			newName = sc.next();

	      	   	                   	
	      	   	                  }catch(RuntimeException e){
	      	   	           	       	System.out.println("E12:There was an error with your selection\n") ;
	      	   	           	       	sc.next(); 
	      	   	           	       	continue;
	      	   	           	       }  
	                         		
	                         		
	                         		if (validateNewName(newName, userID) == false) continue;
	                         		updateName(newName, userID,con, p_stm);
	                         		
	                         		break;
	                         		
	                         		
	                         	case 2:		// Update street address
	                         		System.out.println("Please enter a new street address that you would like us keep on file");
	                         		String newAddr = "";
	                         		
	                         		
	                         		try{
	                         			newAddr = sc.nextLine();
	      	   	                   	
	      	   	                  }catch(RuntimeException e){
	      	   	           	       	System.out.println("E12:There was an error with your selection\n") ;
	      	   	           	       	sc.next(); 
	      	   	           	       	continue;
	      	   	           	       }  
	                         		
	                         		
	                         		if (validateUserAddr(newAddr) == false) continue;
	                         		updateUserAddr(newAddr, userID, con);
	                         		
	                         		break;
	                         		
	                         		
	                         	case 3:		// Update phone_number
	                         		System.out.println("Please enter a new phone number that you would like us keep on file");
	                         		int newNum = -1;
	                         		
	                         		try{
	                         			newNum = sc.nextInt();
	      	   	                   	
	      	   	                  }catch(RuntimeException e){
	      	   	           	       	System.out.println("E12:There was an error with your selection\n") ;
	      	   	           	       	sc.next(); 
	      	   	           	       	continue;
	      	   	           	       }  
	                         		
	                         		if (validateUserNumber(newNum) == false) continue;
	                         		updateUserNumber(newNum, userID, con, p_stm);
	                         		
	                         		break;
	                         		
	                         		
	                         	case 4: 	// Exit to main menu
	                         		continue_loop2 =false;
	                         		break;
	                         }
                    	}
                        break;
                        
                    case 5:
                    	System.out.println("Thank you and have a good day!");
                        continue_loop = false;
                        break;
                }
            }
        }
        
        /* -------------------------------------- INVALID INTEGER ENTERED ---------------------------------------------*/
       else {
        	
        }
              
        
        sc.close();
        
        p_stm.close();
        statement.close();
        con.close();
        
        
    }
    
    
}