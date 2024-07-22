# Large file reading challenge

This application reads CSV file containing temperature data in different cities and calculates its average.

## Running

`./mvnw spring-boot:run`

## Query average temperature in city
Visit webpage:
`http://localhost:8080?city=warszawa`

## Change CSV file
CSV file used to fetch data from is defined in `application.properties` under `filepath` key.