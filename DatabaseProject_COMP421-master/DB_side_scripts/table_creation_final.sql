DROP TABLE Banks CASCADE;
DROP TABLE Branches CASCADE;
DROP TABLE Bankers CASCADE;
DROP TABLE Customers CASCADE;
DROP TABLE Savings CASCADE;
DROP TABLE Transactions CASCADE;
DROP TABLE Withdrawals CASCADE;
DROP TABLE Deposits CASCADE;
DROP TABLE Accounts CASCADE;
DROP TABLE Transfers CASCADE;

--Institutions
-- missing attributes: reserveAmt , countryHQ
CREATE TABLE Banks(
nameB VARCHAR(20) NOT NULL,

PRIMARY KEY(nameB)
);

CREATE TABLE Branches(
brID INTEGER NOT NULL,
nameB VARCHAR(20) NOT NULL,
city VARCHAR(20) NOT NULL,

PRIMARY KEY(brID, nameB),
FOREIGN KEY(nameB) REFERENCES Banks
);

--People

CREATE TABLE Bankers(
pidB INTEGER NOT NULL,		
brID INTEGER NOT NULL,
nameB VARCHAR(20) NOT NULL,
addr VARCHAR(30),
name VARCHAR(20),
phoneNum INTEGER,

PRIMARY KEY (pidB),
FOREIGN KEY(brID, nameB) REFERENCES Branches(brID, nameB)
);

CREATE TABLE Customers(
pidC INTEGER NOT NULL,
pidB INTEGER NOT NULL,
addr VARCHAR(30),
name VARCHAR(20),
phoneNum INTEGER,

PRIMARY KEY(pidC),
FOREIGN KEY(pidB) REFERENCES Bankers(pidB)
);

--Accounts
CREATE TABLE Accounts(
aID INTEGER NOT NULL,
currency VARCHAR(3) NOT NULL,
openDate DATE NOT NULL,
balance INTEGER NOT NULL,	-- Assume balance <= 10,000,000
nameB VARCHAR(20) NOT NULL,
pidC INTEGER NOT NULL,
pidB INTEGER NOT NULL,
checking BOOLEAN NOT NULL,		-- true : checking & false : saving 
interestRate INTEGER,		-- Similar to balance, INT is more lightweight

PRIMARY KEY(aID),
FOREIGN KEY(pidB) REFERENCES Bankers(pidB),
FOREIGN KEY(pidC) REFERENCES Customers(pidC),
FOREIGN KEY(nameB) REFERENCES Banks(nameB)
);


--Transactions

CREATE TABLE Transactions(
tID INTEGER NOT NULL,
dateOf DATE NOT NULL,
timeOf TIME NOT NULL, 
amt INTEGER NOT NULL,
statusOf INT NOT NULL,	

PRIMARY KEY(tID)
);


--Withdrawals
CREATE TABLE Withdrawals(
tidW INTEGER NOT NULL,
aidC INTEGER NOT NULL,

PRIMARY KEY(tidW),
FOREIGN KEY(tidW) REFERENCES Transactions(tID),
FOREIGN KEY(aidC) REFERENCES Accounts(aID)
);

-- Deposits
CREATE TABLE Deposits(
tidD INTEGER NOT NULL,
aID INTEGER NOT NULL,

PRIMARY KEY(tidD),
FOREIGN KEY(tidD) REFERENCES Transactions(tID),
FOREIGN KEY(aID) REFERENCES Accounts(aID)
);


--Transfers
CREATE TABLE Transfers(
tidT INTEGER NOT NULL,
aidSrc INTEGER NOT NULL,
aidDest INTEGER NOT NULL,
fee INTEGER NOT NULL,

PRIMARY KEY(tidT),
FOREIGN KEY(tidT) REFERENCES Transactions(tID),
FOREIGN KEY(aidSrc) REFERENCES Accounts(aID),
FOREIGN KEY (aidDest) REFERENCES Accounts(aID)
);






