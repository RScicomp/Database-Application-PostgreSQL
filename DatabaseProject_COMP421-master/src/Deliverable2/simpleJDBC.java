/*	Authored by:
 * 		Daniel Busuttil
 */

import java.sql.* ;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


class simpleJDBC {
	// RNG Instances:
	private final nameGenerator nameGen = new nameGenerator();
	private final numGenerator numGen = new numGenerator();
	
	
	// Constants used during generation:
	private final String prefix = "";
	private final String bankTableName = prefix + "banks";
	private final int bankTableLength = 5;
	private final String branchTableName = prefix + "branches";
	private final int branchTableLength = bankTableLength * 3;
	private final String bankerTableName = prefix + "bankers";
	private final int bankerTableLength = branchTableLength * 4;
	private final String customerTableName = prefix + "customers";
	private final int customerTableLength = 200;
	private final String accountTableName = prefix + "accounts";
	private final int accountTableLength = 300;
	private final String transactionTableName = prefix + "transactions";
	private final int transactionTableLength = 400;
	private final String withdrawalTableName = prefix + "withdrawals";
	private final int withdrawalTableLength = (transactionTableLength * 2) / 5;
	private final String depositTableName = prefix + "deposits";
	private final int depositTableLength = (transactionTableLength * 2) / 5;
	private final String transferTableName = prefix + "transfers";
	private final int transferTableLength = transactionTableLength / 5;
	
	
	// Nested classes to aid with generation:
	class Banks {
		private String name;
		
		public Banks( String in_name ) {
			this.name = in_name;
		}
	}
	
	class Branch {
		String bankName;
		int branch_ID;
		String city;
		
		public Branch( int in_ID, String in_name, String in_city ) {
			this.bankName = in_name;
			this.branch_ID = in_ID;
			this.city = in_city;
		}
	}

	class Banker {
		int banker_ID;
		int branch_ID;
		String bankName;
		String address;
		String personalName;
		String phoneNumber;
		
		public Banker( int in_banker_ID, int in_branch_ID, String in_bankName,
						String in_address, String in_name, String in_phNumber ) {
			this.banker_ID = in_banker_ID;
			this.branch_ID = in_branch_ID;
			this.bankName = in_bankName;
			this.address = in_address;
			this.personalName = in_name;
			this.phoneNumber = in_phNumber;
		}
	}
	
	class Customer {
		int customer_ID;
		int myBankers_ID;
		String address;
		String personalName;
		String phoneNumber;
		
		public Customer( int in_cust_ID, int in_banker_ID, String in_address,
							String in_name, String in_phNumber ) {
			this.customer_ID = in_cust_ID;
			this.myBankers_ID = in_banker_ID;
			this.address = in_address;
			this.personalName = in_name;
			this.phoneNumber = in_phNumber;
		}
	}
	
	class Account {
		int account_ID;
		String currency = "CAD";
		String openDate;
		int balance = 5000000;	// Last two digits are 'actually' decimal places
		String myBankName;
		int owner_ID;			// ref to cust_ID
		int manager_ID;			// ref to banker_ID
		String type;			// True for checking, False for savings
		int interestRate;		// 0 if type == True
		
		public Account( int in_account_ID, String in_openDate, String in_myBankName,
						int in_owner_ID, int in_manager_ID, String in_type ) {
			this.account_ID = in_account_ID;
			this.openDate = in_openDate;
			this.myBankName = in_myBankName;
			this.owner_ID = in_owner_ID;
			this.manager_ID = in_manager_ID;
			this.type = in_type;
			
		}
	}
	
	// Should be an abstract class but it'll be useful as its own table
	class Transaction {
		int tran_ID;
		String dateMade;
		String timeMade;
		int amount = 500000;
		int status;				// 1: completed, 2: pending, -1: rejected
		
		public Transaction(int in_tran_ID, String in_dateMade, String in_timeMade,
							int in_amount, int in_status ) {
			this.tran_ID = in_tran_ID;
			this.dateMade = in_dateMade;
			this.timeMade = in_timeMade;
			this.amount = in_amount;
			this.status = in_status;
		}
	}
	
	// ISA-heirachy from Transaction:
	class Withdrawal {
		int myTran_ID;
		int srcAcc_ID;
		
		public Withdrawal( int in_tran_ID, int in_srcAcc_ID ) {
			this.myTran_ID = in_tran_ID;
			this.srcAcc_ID = in_srcAcc_ID;
		}
	}
	class Deposit {
		int myTran_ID;
		int destAcc_ID;
		
		public Deposit( int in_tran_ID, int in_destAcc_ID ) {
			this.myTran_ID = in_tran_ID;
			this.destAcc_ID = in_destAcc_ID;
		}
	}
	class Transfer {
		int myTran_ID;
		int srcAcc_ID;
		int destAcc_ID;
		int fee = numGen.rng.nextInt(5000);
		
		public Transfer( int in_tran_ID, int in_srcAcc_ID, int in_destAcc_ID ) {
			this.myTran_ID = in_tran_ID;
			this.srcAcc_ID = in_srcAcc_ID;
			this.destAcc_ID = in_destAcc_ID;
		}
	}
	

	
	// Adds the 5 banks our DB uses to "Banks" table; returns 'SQLState'
	private String addToBanks( Connection con, Statement statement, Banks[] bank_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = bankTableName;
		String insertSQL = "INIT";
		
		// Insert data into Banks
	    try {
	    	for( Banks b : bank_arr) {
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( '" + b.name + "' ) ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	    }
	    catch (SQLException e) {
	    	sqlCode = e.getErrorCode();
	    	sqlState = e.getSQLState();
	      
	    	// Error handling
	    	System.out.println("BANKS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 10 branches
	public String addToBranches( Connection con, Statement statement, Banks[] bank_arr, Branch[] branch_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = branchTableName;
		String insertSQL = "INIT";
		int branchID = 0;
		
		// Inserting Data into the table
	    try {
	    	for( int elem = 0; elem < branch_arr.length; elem++ ) {
	    		branch_arr[elem] = new Branch( branchID, bank_arr[(elem / (branchTableLength / bankTableLength))].name, nameGen.getCity() );
	    		branchID++;
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						branch_arr[elem].branch_ID + ", '" +
	    						branch_arr[elem].bankName + "', '"  +
	    						branch_arr[elem].city +
	    					"' ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("BRANCHES: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 30 bankers
	public String addToBankers( Connection con, Statement statement, Branch[] branch_arr, Banker[] banker_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = bankerTableName;
		String insertSQL = "INIT";
		int bankerID = 0;
		
		// Inserting Data into the table
	    try {
	    	for( int elem = 0; elem < banker_arr.length; elem++ ) {
	    		banker_arr[elem] = new Banker(
					    				bankerID,
										branch_arr[(elem / (bankerTableLength / branchTableLength))].branch_ID,
										branch_arr[(elem / (bankerTableLength / branchTableLength))].bankName,			
					    				nameGen.genAddr(),
										nameGen.genName(),
										("" + numGen.genPhoneNum())
									);
	    		bankerID++;
	    		
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						banker_arr[elem].banker_ID + ", " +
	    						banker_arr[elem].branch_ID + ", '" +
	    						banker_arr[elem].bankName + "', '" + 
	    						banker_arr[elem].address + "', '" + 
	    						banker_arr[elem].personalName + "', '" +
	    						banker_arr[elem].phoneNumber +  
	    					"' ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("BANKERS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates Customers
	public String addToCustomers( Connection con, Statement statement, Banker[] banker_arr, Customer[] cust_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = customerTableName;
		String insertSQL = "INIT";
		int customerID = 0;
		
		// Inserting Data into the table
	    try {
	    	for( int elem = 0; elem < cust_arr.length; elem++ ) {
	    		cust_arr[elem] = new Customer(
					    				customerID,
										banker_arr[ numGen.rng.nextInt(banker_arr.length) ].banker_ID,	// Pick a random Banker	
					    				nameGen.genAddr(),
										nameGen.genName(),
										("" + numGen.genPhoneNum())
									);
	    		customerID++;
	    		
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						cust_arr[elem].customer_ID + ", " +
	    						cust_arr[elem].myBankers_ID + ", '" + 
	    						cust_arr[elem].address + "', '" + 
	    						cust_arr[elem].personalName + "', '" +
	    						cust_arr[elem].phoneNumber +  
	    					"' ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("CUSTOMERS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 50 accounts
	public String addToAccounts( Connection con, Statement statement, Banks[] bank_arr, Customer[] cust_arr, Banker[] banker_arr, Account[] acc_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = accountTableName;
		String insertSQL = "INIT";
		int accountID = 0;
		
		// Inserting Data into the table
	    try {
	    	for( int elem = 0; elem < acc_arr.length; elem++ ) {
	    		// Costly manual 'join' & 'select'
	    		int owner_index = numGen.rng.nextInt(cust_arr.length);
	    		int tmp_bankerID = cust_arr[ owner_index ].myBankers_ID;
	    		String bankName = banker_arr[tmp_bankerID].bankName;
	    		
	    		acc_arr[elem] = new Account(
					    				accountID,
										numGen.genDate(),
										bankName,
										cust_arr[ owner_index ].customer_ID,
										banker_arr[ tmp_bankerID ].banker_ID,
										( elem >= (acc_arr.length / 2)) ? "true" : "false"
									);
	    		accountID++;
	    		
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						acc_arr[elem].account_ID + ", '" +
	    						acc_arr[elem].currency + "', '" + 
	    						acc_arr[elem].openDate + "', " + 
	    						acc_arr[elem].balance + ", '" +
	    						acc_arr[elem].myBankName + "', " +
	    						acc_arr[elem].owner_ID + ", " +
	    						acc_arr[elem].manager_ID + ", '" +
	    						acc_arr[elem].type + "', " +
	    						( ( acc_arr[elem].type == "true") ? 0 : 2) + 
	    					" ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	      sqlCode = e.getErrorCode(); // Get SQLCODE
	      sqlState = e.getSQLState(); // Get SQLSTATE
	      
	      // Error handling
	      System.out.println("ACCOUNTS: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 60 Transactions
	private String addToTransactions( Connection con, Statement statement, Transaction[] trans_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = transactionTableName;
		String insertSQL = "INIT";
		int transID = 0;
		
		// Insert data into transactions
		try {
	    	for( int elem = 0; elem < trans_arr.length; elem++ ) {
	    		int randStatus = numGen.rng.nextInt(10) + 1;
	    		int resultStat;
	    		if( randStatus <= 6 ) {			// randStatus <= 10
	    			resultStat = 0;		// Successful
	    		} else if ( randStatus < 10) {	// 7 <= randStatus <= 9
	    			resultStat = 1;		// Pending
	    		} else {							// randStatus == 10
	    			resultStat = -1;	// Rejected
	    		}
	    		
	    		trans_arr[elem] = new Transaction(
	    							transID,
	    							numGen.genDate(),
	    							numGen.genTime(),
	    							numGen.rng.nextInt(1000000),
	    							resultStat	    							
	    						);
	    		transID++;
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						trans_arr[elem].tran_ID + ", '" +
	    						trans_arr[elem].dateMade + "', '"  +
	    						trans_arr[elem].timeMade + "', "  +
	    						trans_arr[elem].amount + ", "  +
	    						trans_arr[elem].status + 
	    					" ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	    }
	    catch (SQLException e) {
	    	sqlCode = e.getErrorCode();
	    	sqlState = e.getSQLState();
	      
	    	// Error handling
	    	System.out.println("TRANSACTION: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 25 withdrawals
	private String addToWithdrawals( Connection con, Statement statement, Transaction[] trans_arr, Account[] acc_arr, Withdrawal[] with_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = withdrawalTableName;
		String insertSQL = "INIT";
		
		// Insert data into withdrawals 
		try {
	    	for( int elem = 0; elem < with_arr.length; elem++ ) {
	    		with_arr[elem] = new Withdrawal(
	    							trans_arr[elem].tran_ID,
	    							acc_arr[ numGen.rng.nextInt(acc_arr.length) ].account_ID
	    						);
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						with_arr[elem].myTran_ID + ", " +
	    						with_arr[elem].srcAcc_ID +
	    					" ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	    	sqlCode = e.getErrorCode();
	    	sqlState = e.getSQLState();
	      
	    	// Error handling
	    	System.out.println("WITHDRAWAL: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 25 deposits
	private String addToDeposits( Connection con, Statement statement, Transaction[] trans_arr, Account[] acc_arr, Deposit[] depo_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = depositTableName;
		String insertSQL = "INIT";
		
		// Insert data into deposits
		try {
	    	for( int elem = 0; elem < depo_arr.length; elem++ ) {
	    		depo_arr[elem] = new Deposit(
	    							trans_arr[ elem + withdrawalTableLength ].tran_ID,
	    							acc_arr[ numGen.rng.nextInt(acc_arr.length) ].account_ID
	    						);
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
	    						depo_arr[elem].myTran_ID + ", " +
	    						depo_arr[elem].destAcc_ID +
	    					" ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	    	sqlCode = e.getErrorCode();
	    	sqlState = e.getSQLState();
	      
	    	// Error handling
	    	System.out.println("DEPOSIT: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Returns sqlState and populates 10 transfers
	private String addToTransfers( Connection con, Statement statement, Transaction[] trans_arr, Account[] acc_arr, Transfer[] trnsfr_arr ) {
		int sqlCode = 0;
		String sqlState = "00000";
		String tableName = transferTableName;
		String insertSQL = "INIT";
		
		// Insert data into transfers
		try {
	    	for( int elem = 0; elem < trnsfr_arr.length; elem++ ) {
	    		// Ensure src != src
	    		int src = numGen.rng.nextInt(acc_arr.length);
	    		int dest = src;
	    		while( dest == src ) {
	    			dest = numGen.rng.nextInt(acc_arr.length);
	    		}
	    		
	    		trnsfr_arr[elem] = new Transfer(
					    				trans_arr[ elem + (withdrawalTableLength + depositTableLength) ].tran_ID,
										acc_arr[ src ].account_ID,
										acc_arr[ dest ].account_ID
	    						);
	    		
	    		// Push to DB
	    		insertSQL = "INSERT INTO " + tableName + " VALUES ( " +
			    				trnsfr_arr[elem].myTran_ID + ", " +
			    				trnsfr_arr[elem].srcAcc_ID + ", " +
					    		trnsfr_arr[elem].destAcc_ID + ", " +
					    		trnsfr_arr[elem].fee + 
	    					" ); ";
	    		statement.executeUpdate ( insertSQL );
	    	}
	      
	    }
	    catch (SQLException e) {
	    	sqlCode = e.getErrorCode();
	    	sqlState = e.getSQLState();
	      
	    	// Error handling
	    	System.out.println("TRANSFER: Code: " + sqlCode + "  sqlState: " + sqlState);
	    }
		
		return sqlState;
	}
	
	// Driver:
	public static void main ( String [ ] args ) throws SQLException {
		// Instantiate the class:
		simpleJDBC ourBackEnd = new simpleJDBC();
		
		// Data structures to hold our objects during generation
		Banks[] bank_arr = { ourBackEnd.new Banks("TD"), ourBackEnd.new Banks("HSBC"), ourBackEnd.new Banks("Scotiabank"),
								ourBackEnd.new Banks("Barclays"), ourBackEnd.new Banks("RBC") };
		Branch[] branch_arr = new Branch[ourBackEnd.branchTableLength];
		Banker[] banker_arr = new Banker[ourBackEnd.bankerTableLength];
		Customer[] cust_arr = new Customer[ourBackEnd.customerTableLength];
		Account[] acc_arr = new Account[ourBackEnd.accountTableLength];
		Transaction[] trans_arr = new Transaction[ourBackEnd.transactionTableLength];
		Withdrawal[] with_arr = new Withdrawal[ourBackEnd.withdrawalTableLength];
		Deposit[] depo_arr = new Deposit[ourBackEnd.depositTableLength];
		Transfer[] trnsfr_arr = new Transfer[ourBackEnd.transferTableLength];
		
	    // Register the driver:
	    try {
	      DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
	    }
	    catch (Exception cnfe) {
	      System.out.println("Class not found");
	    }
	    
	    // Url to connect:
	    String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
	    Connection con = DriverManager.getConnection (url, "cs421g21", "N2a0r1ut6o"); // Password was removed for security reasons
	    Statement statement = con.createStatement();
	    
	    // Add our generated data:
	    ourBackEnd.addToBanks(con, statement, bank_arr);
	    ourBackEnd.addToBranches(con, statement, bank_arr, branch_arr);
	    ourBackEnd.addToBankers(con, statement, branch_arr, banker_arr);
	    ourBackEnd.addToCustomers(con, statement, banker_arr, cust_arr);
	    ourBackEnd.addToAccounts(con, statement, bank_arr, cust_arr, banker_arr, acc_arr);
	    ourBackEnd.addToTransactions(con, statement, trans_arr);
	    ourBackEnd.addToWithdrawals(con, statement, trans_arr, acc_arr, with_arr);
	    ourBackEnd.addToDeposits(con, statement, trans_arr, acc_arr, depo_arr);
	    ourBackEnd.addToTransfers(con, statement, trans_arr, acc_arr, trnsfr_arr);
	    	    
	    // Finally close the statement and connection
	    statement.close();
	    con.close();
	  }
}

