-- update status of transactions that are waiting for a period of time 
-- as long as the specified time length
-- status updates to 3 so we can see that transactions of value 3 are no longer in pending
-- but rather terminated
CREATE OR REPLACE FUNCTION pend_to_exp(timeframe character varying,timelength integer)
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
		SELECT * FROM transactions 
		WHERE statusof = 1
		GROUP BY transactions.tid
		HAVING (date_part(timeframe,  current_date)- date_part(timeframe, dateOf) > timelength);

	
	LOOP
		FETCH NEXT FROM ptr INTO 
			cur_tid , cur_dateOf , cur_timeOf , 
			cur_amt , cur_statusOf ;
		EXIT WHEN NOT FOUND;


		UPDATE transactions SET statusof = -2
 		WHERE transactions.tid = cur_tid;
		
	END LOOP;
	
	CLOSE ptr;
END;
$$  LANGUAGE 'plpgsql';

