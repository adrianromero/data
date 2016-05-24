/**
 * Author:  adrian
 */

create table c_country(
    c_country_id varchar(32),
    name varchar(60),
    countrycode varchar(2),
    hasregion boolean,
    primary key (c_country_id)
);
