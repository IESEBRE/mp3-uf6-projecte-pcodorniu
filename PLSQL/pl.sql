-- Poseu el codi dels procediments/funcions emmagatzemats, triggers, ..., usats al projecte

--Trigger per a autoincrementar el ID de la taula PILOTS
CREATE OR REPLACE TRIGGER pilot_tri
BEFORE INSERT ON PILOTS
FOR EACH ROW
BEGIN
    SELECT NVL(MAX(ID), 0) + 1 INTO :NEW.ID FROM PILOTS;
END;
/
