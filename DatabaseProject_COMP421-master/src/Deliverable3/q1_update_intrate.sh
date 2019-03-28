#!/bin/sh
USERNAME=$1
export PGPASSWORD=$2
prefix=$3
DATABASE=cs421

psql -U $USERNAME $DATABASE << EOF

    -- Show records pre-function
    SELECT * FROM $3accounts 
    WHERE
        checking = 'false'
    AND
        (date_part('year',  current_date)- date_part('year', opendate) > 5 )
    ORDER BY aid
    LIMIT 20
    ;

    -- Update interest rate for loyalty customers from 2% to 8%
    SELECT * FROM updateInterestRate();

    -- Show records post-function
    SELECT * FROM $3accounts 
    WHERE
        checking = 'false'
    AND
        (date_part('year',  current_date)- date_part('year', opendate) > 5 )
    ORDER BY aid
    LIMIT 20
    ;   
EOF
