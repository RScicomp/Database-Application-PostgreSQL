-- update status of transactions that are waiting for a period of time 
-- as long as the specified time length
-- status updates to 3 so we can see that transactions of value 3 are no longer in pending
-- but rather terminated
CREATE OR REPLACE FUNCTION tempfxn2(timeframe character varying,timelength integer)
	RETURNS VOID AS $$
	DECLARE 
		ptr REFCURSOR;
		cur_tid INT;
		cur_dateOf DATE;
		cur_timeOf TIME WITHOUT TIME ZONE;
		cur_amt INT;
		cur_statusOf INT;
		
	 
BEGIN

	OPEN ptr FOR  
		SELECT * FROM t_transactions 
		WHERE statusof = 1
		GROUP BY t_transactions.tid
		HAVING (date_part(timeframe,  current_date)- date_part(timeframe, dateOf) > timelength);

	
	LOOP
		FETCH NEXT FROM ptr INTO 
			cur_tid , cur_dateOf , cur_timeOf , 
			cur_amt , cur_statusOf ;
		EXIT WHEN NOT FOUND;


		UPDATE t_transactions SET statusof = 3
 		WHERE t_transactions.tid = cur_tid;
		
	END LOOP;
	
	CLOSE ptr;
END;
$$  LANGUAGE 'plpgsql';


SELECT * FROM transactions 
		WHERE statusof = 1
		GROUP BY transactions.tid
		HAVING (date_part('year',  current_date)- date_part('year', dateOf) > 10);
		
SELECT * FROM tempfxn2('year', 10);

SELECT * FROM transactions 
		WHERE statusof = 3
		GROUP BY transactions.tid
		HAVING (date_part('year',  current_date)- date_part('year', dateOf) > 10);