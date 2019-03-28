
SELECT COALESCE(firsthalf.nameb, secondhalf.nameb) as nameb,COALESCE(firsthalf.brid, secondhalf.brid) as brid, firsthalf.numaccounts, firsthalf.numbankers, firsthalf.numcustomers, 
			firsthalf.numsavingAccts, firsthalf.totalsavings, firsthalf.numcheckAccts, firsthalf.totalcheckbalance, 
			secondhalf.numwithdrawals, secondhalf.totalwithdrawalamt, secondhalf.numdeposits, secondhalf.totaldepositamt, 
				secondhalf.numtransfersOUT, secondhalf.totaltransferOUTAmt, secondhalf.numtransfersIN, secondhalf.totaltransferINAmt
	FROM
		(SELECT COALESCE(accCustBnkr.nameb,savcheck.nameb) as nameb, COALESCE(accCustBnkr.brid,savcheck.brid) as brid, accCustBnkr.numaccounts,  accCustBnkr.numbankers, accCustBnkr.numcustomers, 
				savcheck.numsavingAccts, savcheck.totalsavings, savcheck.numcheckAccts, savcheck.totalcheckbalance 
		FROM
			(SELECT bks.nameb as nameb, bks.brid, COUNT(*) as numaccounts, COUNT(DISTINCT a.pidb) as numbankers, COUNT(DISTINCT pidc) as numcustomers
			FROM ACCOUNTS a, bankers bks
			WHERE a.pidb = bks.pidb  
			GROUP BY (bks.nameb,bks.brid)
			ORDER BY (bks.nameb,bks.brid)) as accCustBnkr
			FULL OUTER JOIN 
			--total # savings, total savings value, total # checkings, total checkings value
			(SELECT COALESCE(sav.nameb, checking.nameb) as nameb,COALESCE(sav.brid, checking.brid) as brid, sav.numsavingAccts, sav.totalsavings, checking.numcheckAccts, checking.totalcheckbalance FROM
				(SELECT b.nameb, b.brid, COUNT(*) as numsavingAccts, CAST(CAST(SUM(balance) AS REAL)/100 AS DECIMAL(16,2)) as totalsavings
				FROM accounts a, bankers b
				WHERE checking = 'false' AND a.pidb =b.pidb 
				GROUP BY (b.nameb,b.brid)
				ORDER BY (b.nameb,b.brid) ) as sav
				FULL OUTER JOIN
				(SELECT  b.nameb, b.brid, COUNT(*) as numcheckAccts, CAST(CAST(SUM(balance) AS REAL)/100 AS DECIMAL(16,2)) as totalcheckbalance
				FROM accounts a, bankers b
				WHERE checking = 'true' AND a.pidb =b.pidb 
				GROUP BY (b.nameb,b.brid)
				ORDER BY (b.nameb,b.brid)) checking
				ON sav.brid = checking.brid and sav.nameb= checking.nameb) savcheck
			 ON accCustBnkr.brid = savcheck.brid AND accCustBnkr.nameb = savcheck.nameb) firsthalf
		FULL OUTER JOIN 														  
		(SELECT COALESCE(withdrawdeposit.nameb,transfer.nameb) as nameb, COALESCE(withdrawdeposit.brid,transfer.brid) as brid, withdrawdeposit.numwithdrawals, withdrawdeposit.totalwithdrawalamt,
		 withdrawdeposit.numdeposits, withdrawdeposit.totaldepositamt, transfer.numtransfersOUT, transfer.totaltransferOUTAmt, transfer.numtransfersIN, transfer.totaltransferINAmt FROM
			(SELECT COALESCE(withdraw.nameb,deposit.nameb) as nameb,COALESCE(withdraw.brid,deposit.brid) as brid, withdraw.numwithdrawals, withdraw.totalwithdrawalamt, deposit.numdeposits, 
			deposit.totaldepositamt 
			 FROM
				(SELECT withdrawalInfo.nameb, withdrawalInfo.brid, COUNT(*) as numWithdrawals, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2))*-1 totalwithdrawalAmt
				FROM
					(SELECT tidw, aidc, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb 
					FROM withdrawals w, transactions t, accounts a, bankers b 
					WHERE w.tidw = t.tid  AND w.aidc = a.aid AND a.pidb = b.pidb
					 AND t.statusof = 0) WithdrawalInfo
				GROUP BY (WithdrawalInfo.brid, WithdrawalInfo.nameb) 
				ORDER BY (WithdrawalInfo.brid , WithdrawalInfo.nameb)) as withdraw
				FULL OUTER JOIN
				(SELECT depositInfo.nameb,depositInfo.brid, COUNT(*) as numDeposits, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2)) totalDepositAmt
				FROM	
					(SELECT tidd, d.aid, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb
					FROM deposits d, transactions t, accounts a, bankers b 
					WHERE d.tidd = t.tid  AND d.aid = a.aid AND a.pidb = b.pidb
					AND t.statusof = 0) depositInfo
				GROUP BY (depositInfo.brid, depositInfo.nameb) 
				ORDER BY (depositInfo.brid, depositInfo.nameb))  as deposit
				on withdraw.brid = deposit.brid and withdraw.nameb = deposit.nameb) withdrawdeposit
			FULL OUTER JOIN
			(SELECT COALESCE(transfout.nameb, transfin.nameb) as nameb,COALESCE(transfout.brid, transfin.brid) as brid, transfOut.numtransfersOUT,transfOut.totaltransferOUTAmt,
			 transfIN.numtransfersIN, transfIN.totaltransferINAmt FROM
				(SELECT transferOUTInfo.nameb, transferOUTInfo.brid, COUNT(*) as numtransfersOUT, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2))*-1 totaltransferOUTAmt
				FROM	
					(SELECT tidt, tr.aidsrc, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb 
					FROM transfers tr, transactions t, accounts a, bankers b 
					WHERE tr.tidt = t.tid  AND tr.aidsrc = a.aid AND a.pidb = b.pidb
					AND t.statusof = 0) transferOUTInfo
				GROUP BY (transferOUTInfo.brid, transferOUTInfo.nameb)
				ORDER BY (transferOUTInfo.brid, transferOUTInfo.nameb)) as transfOut																	 
				FULL OUTER JOIN 	
				(SELECT transferINInfo.nameb, transferINInfo.brid, COUNT(*) as numtransfersIN, CAST(CAST(SUM(amt) AS REAL)/100 AS DECIMAL(16,2)) totaltransferINAmt
				FROM	
					(SELECT tidt, tr.aiddest, tid, dateof, timeof, amt, statusof, currency, pidc, a.pidb, b.brid, b.nameb
					FROM transfers tr, transactions t, accounts a, bankers b 
					WHERE tr.tidt = t.tid  AND tr.aiddest = a.aid AND a.pidb = b.pidb
					AND t.statusof = 0) transferINInfo
				GROUP BY (transferINInfo.brid, transferInInfo.nameb)
				ORDER BY (transferINInfo.brid, transferInInfo.nameb)) as transfIN
				ON transfOut.brid = transfin.brid and transfOut.nameb = transfin.nameb) transfer
		ON withdrawdeposit.brid = transfer.brid and withdrawdeposit.nameb = transfer.nameb) secondhalf
		ON firsthalf.brid = secondhalf.brid