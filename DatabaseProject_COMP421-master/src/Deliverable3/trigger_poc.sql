-- P.O.C. to use triggers 

-- Wipe relevant/existing info
DROP TABLE IF EXISTS bal_change;
DROP TABLE IF EXISTS person;
DROP FUNCTION IF EXISTS ch_balance(INTEGER, INTEGER, INTEGER);

-- CREATE statements
CREATE TABLE person(
    id INTEGER,
    balance INTEGER,
    PRIMARY KEY(id)
);
CREATE TABLE bal_change(
    id INTEGER,
    num INTEGER,
    delta INTEGER,
    PRIMARY KEY(id, num),
    FOREIGN KEY (id) REFERENCES person
);

-- Basic populate
INSERT INTO person VALUES(1, 0);
INSERT INTO person VALUES(2, 0);
INSERT INTO person VALUES(3, 0);
INSERT INTO person VALUES(4, 0);
INSERT INTO person VALUES(5, 0);


-- Replace function with changes 
CREATE OR REPLACE FUNCTION ch_bal() RETURNS TRIGGER
AS $$
    << outerblock >>
    BEGIN
        -- Sanity check to make sure correct variables are being provided:
        RAISE NOTICE 'Run: % %', NEW.id, NEW.delta;
        UPDATE person SET (balance) = (balance + NEW.delta) WHERE (id = NEW.id);
        RETURN NEW;
    END;
$$
LANGUAGE plpgsql
;

-- Remove old trigger & create new one:
DROP TRIGGER IF EXISTS upd_person ON bal_change;
CREATE TRIGGER upd_person
    BEFORE INSERT ON bal_change
    FOR EACH ROW
    EXECUTE PROCEDURE ch_bal()
;




