-- update interest rate if the person has an account that has been open longer than  XX number of years

CREATE OR REPLACE FUNCTION updateInterestRate()
	RETURNS VOID AS $$
	DECLARE 
		ptr REFCURSOR;
		cur_aid INT;
        cur_currency VARCHAR(3);
		cur_opendate DATE;
		cur_balance INT;
		cur_bankname VARCHAR(20);
		cur_pidc INT;
        cur_pidb INT;
        cur_checking BOOLEAN;
        cur_interestrate INT;
		
	 
BEGIN

	OPEN ptr FOR  
		SELECT * FROM accounts
		WHERE checking = 'false'
		AND (date_part('year',  current_date)- date_part('year', opendate) > 5 );

	
	LOOP
		FETCH NEXT FROM ptr INTO 
			cur_aid , cur_currency , cur_opendate, cur_balance, cur_bankname, 
			cur_pidc, cur_pidb, cur_checking, cur_interestrate;
		EXIT WHEN NOT FOUND;


		UPDATE accounts SET interestrate = 8
 		WHERE accounts.aid = cur_aid;
		
	END LOOP;
	
	CLOSE ptr;
END;
$$  LANGUAGE 'plpgsql';




		


