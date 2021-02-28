# Cab Management Portal

# APIs

## Test apis for Postman
https://www.getpostman.com/collections/12bfb6c03ff8921a3f99

## Admin

POST /registerCity
```
Request:
{
    "city":"cityNameA"
}
Response:
{
    "cityId": "2",
    "status": "registered"
}
```

POST /registerCab
```
Request:
{
    "cityId":1,
    "driverName":"qwerty asdf",
    "cabNumber": "aws1",
    "lisenceId": "123"
}
Response:
{
    "cabId": "1",
    "status": "registered"
}
```

GET /listAllCabs
```
Response:
{
    "cabs": [
        {
            "id": 1,
            "cabNumber": "aws1",
            "cityId": 2,
            "state": "ON_TRIP",
            "idleTime": 76869
        },
        {
            "id": 2,
            "cabNumber": "aws1",
            "cityId": 1,
            "state": "IDLE",
            "idleTime": 23411
        }
    ],
    "status": "success"
}
```

GET /listAllCities
```
Response:
{
    "cities": [
        {
            "id": 1,
            "city": "Chennai"
        },
        {
            "id": 2,
            "city": "Delhi"
        }
    ],
    "status": "success"
}
```

GET /getCabStates?cabId={cabID}
```
Response:
{
    "status": "success",
    "states": [
        {
            "id": 1,
            "cabId": 1,
            "cityId": 1,
            "state": "IDLE",
            "date": "2021-02-28T10:37:06.849+00:00"
        },
        {
            "id": 2,
            "cabId": 1,
            "cityId": 1,
            "state": "ON_TRIP",
            "date": "2021-02-28T10:37:36.223+00:00"
        }
    ]
}
```

GET /getCityDemandStats
```
cityid1->
    hour1-> booking_count1
    hour2-> booking_count2
    hour3-> booking_count3
cityid2->
    hour1-> booking_count1
    hour2-> booking_count2
    hour3-> booking_count3

Response:
{
    "1": {
        "15": 7
        "16": 3
    },
    "2": {
        "16": 2
    }
}
```

GET /getCabIdleTime?from={fromdate}&to={todate}&cabId={cabID}
eg:
GET /getCabIdleTime?from=2021-02-02-01-01&to=2021-03-03-01-01&cabId=1
```
cityid1->
    hour1-> booking_count1
    hour2-> booking_count2
    hour3-> booking_count3
cityid2->
    hour1-> booking_count1
    hour2-> booking_count2
    hour3-> booking_count3

Response:
{
    "idleTime": {
        "units": "ms",
        "time": "76868"
    },
    "status": "success"
}
```

## Driver app

PUT /updateCabLocation
```
Request:
{
    "cityId": 2,
    "cabId": 1
}

Response:
{
    "status": "updated"
}
```

PUT /updateCabDuty
```
Request:
{
    "cabId": 1,
    "duty": "OFF"
}
OR
{
    "cabId": 1,
    "duty": "ON"
}       

Response:
{
    "status": "updated"
}
```

POST /endBooking
```
Request:
{
    "bookingId": 2
} 

Response:
{
    "booking": {
        "id": 2,
        "cabId": 1,
        "fromCityId": 2,
        "toCityId": 2,
        "startDate": "2021-02-28T10:39:13.210+00:00",
        "endDate": "2021-02-28T10:58:03.833+00:00"
    },
    "status": "trip_ended"
}
```

## Customer App

POST /book
```
Request:
{
    "fromCity": 2,
    "toCity":2
}      

Response:
{
    "booking": {
        "id": 2,
        "cabId": 1,
        "fromCityId": 2,
        "toCityId": 2,
        "startDate": "2021-02-28T10:39:13.210+00:00",
        "endDate": null
    },
    "status": "booked"
}
```


## Run project
```
Build:
./gradlew build --info

Run:
java -jar build/libs/rest-service-0.0.1-SNAPSHOT.jar --debug
```