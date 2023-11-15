with cf1 as ( select
                  f2.id as f2_id,
                  f2.code as f2_code,
                  f2.flight_status as f2_flight,
                  f2.arrival_date as f2_arr,
                  f2.departure_date as f2_dep,
                  f2.aircraft_id as f2_air,
                  f2.from_id as f2_from,
                  f2.to_id as f2_to,
                  f3.id as f3_id,
                  f3.code as f3_code,
                  f3.flight_status as f3_flight,
                  f3.arrival_date as f3_arr,
                  f3.departure_date as f3_dep,
                  f3.aircraft_id as f3_air,
                  f3.from_id f3_from,
                  f3.to_id as f3_to
              from
                  (select
                       *
                   from
                       flights f
                   where
                           f.from_id = ?
                     and f.to_id = ?
                     and cast(f.departure_date as date) = ?) fp
                      left join
                  flights f2
                  on (f2.from_id = fp.from_id
                      and cast(f2.departure_date as date) = cast(fp.departure_date as date))
                      join
                  flights f3
                  on f3.from_id = f2.to_id
                      and (f3.departure_date - f2.arrival_date between interval'2 hour' and interval'12 hour')
                      and f3.to_id = fp.to_id) select
                                                   f2_id as id,
                                                   f2_code as code,
                                                   f2_flight as flight_status,
                                                   f2_arr as arrival_date,
                                                   f2_dep as departure_date,
                                                   f2_air as aircraft_id,
                                                   f2_from as from_id,
                                                   f2_to as to_id
from
    cf1
union
select
    f3_id as id,
    f3_code as code,
    f3_flight as flight_status,
    f3_arr as arrival_date,
    f3_dep as departure_date,
    f3_air as aircraft_id,
    f3_from as from_id,
    f3_to as to_id
from
    cf1;
