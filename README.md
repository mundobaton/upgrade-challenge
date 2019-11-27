#upgrade-challenge

##Endpoints

* (GET) /availability

Gets the availability to perform reservations

"from" and "to" query parameters are optional

Returns an array of intervals where the reservation may be placed

* (PUT) /reservations

Saves a new reservation

The body requires the following schema:

``
{ "from": "2019-12-01",
  "to": "2019-12-03",
  "email": "some@email.com",
  "fullname": "Some fullname"
}
``

Returns:

``
{"reservation_id":1}
``

* (POST) /reservations/:id

Modifies a reservation

The body requires the following schema:

``
{ "from": "2019-12-03",
  "to": "2019-12-04",
  "email": "some@email.com"
}
``

Returns the same reservation id as confirmation

* Cancel Reservation

* (DELETE) /reservations/:id

Cancels a reservation

Requires the following body schema:

``
{"email":"some@email.com"}
``