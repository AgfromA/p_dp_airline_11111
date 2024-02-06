DO $$
            DECLARE
ticket_id INT;
                new_flight_seat_id INT;
BEGIN
FOR ticket_id IN 1..10 LOOP
                    new_flight_seat_id := 339 + (ticket_id - 1);
EXECUTE 'UPDATE tickets SET flight_seat_id=' || new_flight_seat_id || ' WHERE id=' || ticket_id;
END LOOP;
END;
            $$;