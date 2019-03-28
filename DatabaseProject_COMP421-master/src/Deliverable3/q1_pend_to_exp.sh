#!/bin/sh
USERNAME=$1
export PGPASSWORD=$2
prefix=$3
DATABASE=cs421

psql -U $USERNAME $DATABASE << EOF
    -- Show records pre-function
    SELECT tid, dateof, timeof, amt, statusof
    FROM t_transactions
    WHERE statusof = 1 AND (date_part('year',  current_date) - date_part('year', dateOf) > 10)
    ORDER BY tid
    LIMIT 10
    ;

    -- Run function to replace 'pending' transactions with 'expired' status
    SELECT pend_to_exp('year', 10);

    -- Show updated records
    SELECT tid, dateof, timeof, amt, statusof
    FROM t_transactions
    WHERE statusof = -2 AND (date_part('year',  current_date) - date_part('year', dateOf) > 10)
    ORDER BY tid
    LIMIT 10
    ;
EOF
