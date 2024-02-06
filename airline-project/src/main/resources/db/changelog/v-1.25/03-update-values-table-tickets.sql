DO $$
BEGIN
FOR i IN 1..171 LOOP
                    EXECUTE 'UPDATE tickets SET booking_id=' || ((i-1) % 10 + 1) || ' WHERE id=' || i;
END LOOP;
END;
            $$;