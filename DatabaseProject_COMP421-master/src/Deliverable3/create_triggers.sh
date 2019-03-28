#!/bin/sh
USERNAME=$1
export PGPASSWORD=$2
prefix=$3

# Below code creates a trigger (and it's function) for ${prefix}withdrawals, ${prefix}deposits and ${prefix}transfers: 
psql -U $USERNAME cs421 << EOF
    -- Triggers for "${prefix}" tables (i.e. '${prefix}withdrawals'):

    -- ${prefix}withdrawals:
        CREATE OR REPLACE FUNCTION w_upd_bal_fn() RETURNS TRIGGER
        AS '
            << outerblock >>
            DECLARE
                r_amt INTEGER;
                new_balance INTEGER;
            BEGIN
                SELECT (t.amt * -1) INTO r_amt
                    FROM ${prefix}transactions t, ${prefix}withdrawals w
                    WHERE t.tid = NEW.tidw
                ;

                SELECT (a.balance + r_amt) INTO new_balance
                    FROM ${prefix}accounts a, ${prefix}withdrawals w
                    WHERE a.aid = NEW.aidc
                ;

                UPDATE ${prefix}accounts SET (balance) = (new_balance) WHERE (aid = NEW.aidc);
                RETURN NEW;
            END;
        '
        LANGUAGE plpgsql
        ;

        -- Remove old trigger & create new one:
        DROP TRIGGER IF EXISTS w_upd_balance ON ${prefix}withdrawals;
        CREATE TRIGGER w_upd_balance
            BEFORE INSERT ON ${prefix}withdrawals
            FOR EACH ROW
            EXECUTE PROCEDURE w_upd_bal_fn()
        ;





    -- ${prefix}deposits:
        CREATE OR REPLACE FUNCTION d_upd_bal_fn() RETURNS TRIGGER
        AS '
            << outerblock >>
            DECLARE
                r_amt INTEGER;
                new_balance INTEGER;
            BEGIN
                SELECT t.amt INTO r_amt
                    FROM ${prefix}transactions t, ${prefix}deposits d
                    WHERE t.tid = NEW.tidd
                ;

                SELECT (a.balance + r_amt) INTO new_balance
                    FROM ${prefix}accounts a, ${prefix}deposits d
                    WHERE a.aid = NEW.aid
                ;

                UPDATE ${prefix}accounts SET (balance) = (new_balance) WHERE (aid = NEW.aid);
                RETURN NEW;
            END;
        '
        LANGUAGE plpgsql
        ;

        -- Remove old trigger & create new one:
        DROP TRIGGER IF EXISTS d_upd_balance ON ${prefix}deposits;
        CREATE TRIGGER d_upd_balance
            BEFORE INSERT ON ${prefix}deposits
            FOR EACH ROW
            EXECUTE PROCEDURE d_upd_bal_fn()
        ;





    -- ${prefix}transfers:
        CREATE OR REPLACE FUNCTION tr_upd_bal_fn() RETURNS TRIGGER
        AS '
            << outerblock >>
            DECLARE
                src_amt INTEGER;
                dest_amt INTEGER;
                new_balance_src INTEGER;
                new_balance_dest INTEGER;
            BEGIN
                -- Setup:
                SELECT (ta.amt * -1) INTO src_amt
                    FROM ${prefix}transactions ta, ${prefix}transfers tr
                    WHERE ta.tid = NEW.tidt
                ;
                SELECT ta.amt INTO dest_amt
                    FROM ${prefix}transactions ta, ${prefix}transfers tr
                    WHERE ta.tid = NEW.tidt
                ;

                -- Calculate new balances:
                SELECT (a.balance + src_amt - NEW.fee) INTO new_balance_src
                    FROM ${prefix}accounts a, ${prefix}deposits d
                    WHERE a.aid = NEW.aidsrc
                ;
                SELECT (a.balance + dest_amt) INTO new_balance_dest
                    FROM ${prefix}accounts a, ${prefix}deposits d
                    WHERE a.aid = NEW.aiddest
                ;

                -- Final update:
                UPDATE ${prefix}accounts SET (balance) = (new_balance_src) WHERE (aid = NEW.aidsrc);
                UPDATE ${prefix}accounts SET (balance) = (new_balance_dest) WHERE (aid = NEW.aiddest);
                RETURN NEW;
            END;
        '
        LANGUAGE plpgsql
        ;

        -- Remove old trigger & create new one:
        DROP TRIGGER IF EXISTS tr_upd_balance ON ${prefix}transfers;
        CREATE TRIGGER tr_upd_balance
            BEFORE INSERT ON ${prefix}transfers
            FOR EACH ROW
            EXECUTE PROCEDURE tr_upd_bal_fn()
        ;
EOF
