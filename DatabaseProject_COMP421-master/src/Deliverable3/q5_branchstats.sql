CREATE OR REPLACE FUNCTION branchstats3(bankname character varying)
    RETURNS TABLE(brid integer, numaccounts bigint, numbankers bigint, numcustomers bigint, numsavingaccts bigint, totalsavings numeric, numcheckaccts bigint, totalcheckbalance numeric, numwithdrawals bigint, totalwithdrawalamt numeric, numdeposits bigint, totaldepositamt numeric, numtransfersout bigint, totaltransferoutamt numeric, numtransfersin bigint, totaltransferinamt numeric) 
AS $BODY$
BEGIN
	RETURN QUERY								
	SELECT COALESCE(firsthalf.brid, secondhalf.brid) as brid, firsthalf.numaccounts, firsthalf.numbankers, firsthalf.numcustomers, 
			firsthalf.numsavingAccts, firsthalf.totalsavings, firsthalf.numcheckAccts, firsthalf.totalcheckbalance, 
			secondhalf.numwithdrawals, secondhalf.totalwithdrawalamt, secondhalf.numdeposits, secondhalf.totaldepositamt, 
				secondhalf.numtransfersOUT, secondhalf.totaltransferOUTAmt, secondhalf.numtransfersIN, secondhalf.totaltransferINAmt
	FROM
		(SELECT COALESCE(accCustBnkr.brid,savcheck.brid) as brid, accCustBnkr.numaccounts,  accCustBnkr.numbankers, accCustBnkr.numcustomers, 
				savcheck.numsavingAccts, savcheck.totalsavings, savcheck.numcheckAccts, savcheck.totalcheckbalance 
		FROM
			(SELECT bks.brid, COUNT(*) as numaccounts, COUNT(DISTINCT a.pidb) as numbankers, COUNT(DISTINCT pidc) as numcustomers
			FROM ACCOUNTS a, bankers bks
			WHERE a.pidb = bks.pidb  
			AND bks.nameb = bankname 
			GROUP BY bks.brid
			ORDER BY brid ASC) accCustBnkr
			FULL OUTER JOIN 
			--total # savings, total savings value, total # checkings, total checkings value
			(SELECT COALESCE(sav.brid, checking.brid) as brid, sav.numsavingAccts, sav.totalsavings, checking.numcheckAccts, checking.totalcheckbalance FROM
				(SELECT b.brid, COUNT(*) as numsavingAccts, CAST(CAST(SUM(balance) AS REAL)/100 AS DECIMAL(16,2)) as totalsavings
				FROM accounts a, bankers b
				WHERE checking = 'false' AND a.pidb =b.pidb AND b.nameb = bankname
				GROUP BY b.brid
				ORDER BY b.brid ASC) as sav
				FULL OUTER JOIN
				(SELECT b.brid, COUNT(*) as numcheckAccts, CAST(CAST(SUM(balance) AS REAL)/100 AS DECIMAL(16,2)) as totalcheckbalance
				FROM accounts a, bankers b
				WHERE checking = 'true' AND a.pidb =b.pidb AND b.nameb = bankname
				GROUP BY b.brid
				ORDER BY b.brid ASC) checking
				ON sav.brid = checking.brid) savcheck
			 ON accCustBnkr.brid = savcheck.brid) firsthalf
		FULL OUTER JOIN 														  
		(SELECT COALESCE(withdrawdeposit.brid,transfer.brid) as brid, withdrawdeposit.numwithdrawals, withdrawdeposit.totalwithdrawalamt, withdrawdeposit.numdeposits, 
		 		withdrawdeposit.totaldepositamt, transfer.numtransfersOUT, transfer.totaltransferOUTAmt, transfer.numtransfersIN, transfer.totaltransferINAmt FROM
			(SELECT COALESCE(withdraw.brid,deposit.brid) as brid, withdraw.numwithdrawals, withdraw.totalwithdrawalamt, deposit.numdeposits, deposit.totaldepositamt 
			 FROM
				(SELECT withdrawalInfo.brid, COUNT(*) as numWithdrawals, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2))*-1 totalwithdrawalAmt
				FROM
					(SELECT tidw, aidc, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb
					FROM withdrawals w, transactions t, accounts a, bankers b 
					WHERE w.tidw = t.tid  AND w.aidc = a.aid AND a.pidb = b.pidb
					AND b.nameb = bankname AND t.statusof = 0) WithdrawalInfo
				GROUP BY WithdrawalInfo.brid
				ORDER BY WithdrawalInfo.brid ASC) as withdraw
				FULL OUTER JOIN
				(SELECT depositInfo.brid, COUNT(*) as numDeposits, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2)) totalDepositAmt
				FROM	
					(SELECT tidd, d.aid, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb
					FROM deposits d, transactions t, accounts a, bankers b 
					WHERE d.tidd = t.tid  AND d.aid = a.aid AND a.pidb = b.pidb
					AND b.nameb =bankname AND t.statusof = 0) depositInfo
				GROUP BY depositInfo.brid
				ORDER BY depositInfo.brid ASC) as deposit
				on withdraw.brid = deposit.brid) withdrawdeposit
			FULL OUTER JOIN
			(SELECT COALESCE(transfout.brid, transfin.brid) as brid, transfOut.numtransfersOUT,transfOut.totaltransferOUTAmt, transfIN.numtransfersIN, transfIN.totaltransferINAmt FROM
				(SELECT transferOUTInfo.brid, COUNT(*) as numtransfersOUT, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2))*-1 totaltransferOUTAmt
				FROM	
					(SELECT tidt, tr.aidsrc, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb
					FROM transfers tr, transactions t, accounts a, bankers b 
					WHERE tr.tidt = t.tid  AND tr.aidsrc = a.aid AND a.pidb = b.pidb
					AND b.nameb =bankname AND t.statusof = 0) transferOUTInfo
				GROUP BY transferOUTInfo.brid
				ORDER BY transferOUTInfo.brid ASC) as transfOut																	 
				FULL OUTER JOIN 	
				(SELECT transferINInfo.brid, COUNT(*) as numtransfersIN, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2)) totaltransferINAmt
				FROM	
					(SELECT tidt, tr.aiddest, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb
					FROM transfers tr, transactions t, accounts a, bankers b 
					WHERE tr.tidt = t.tid  AND tr.aiddest = a.aid AND a.pidb = b.pidb
					AND b.nameb =bankname AND t.statusof = 0) transferINInfo
				GROUP BY transferINInfo.brid
				ORDER BY transferINInfo.brid ASC) as transfIN
				ON transfOut.brid = transfin.brid) transfer
		ON withdrawdeposit.brid = transfer.brid) secondhalf
		ON firsthalf.brid = secondhalf.brid;
	  END;
$BODY$
LANGUAGE 'plpgsql';
																				
SELECT * FROM branchstats3('TD');
SELECT * FROM branchstats3('HSBC');