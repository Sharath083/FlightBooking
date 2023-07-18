package com.example.logic

import com.example.dao.BookDetailsDao
import com.example.dao.DatabaseFactory
import com.example.dao.FLightDetailsDao
import com.example.dao.PassengerDao
import com.example.data.request.BookDetails
import com.example.data.request.Flight
import com.example.data.request.Passenger
import com.example.data.response.BookDetailsOut
import com.example.data.response.PassengerId
import com.example.data.request.Filter
import com.example.data.response.PassengerLogin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class DAOImplementation:DAOInterface {


    override suspend fun addNewFlight(d: Flight): Flight?= DatabaseFactory.dbQuery {
        val insertStatement = FLightDetailsDao.insert {
            it[flightNumber]=d.flightNumber
            it[airline]=d.airline
            it[price]=d.price
            it[airport]=d.source
            it[destination]=d.destination
            it[departureTime]=d.departureTime
            it[arrivalTime]=d.arrivalTime
        }
        insertStatement.resultedValues?.singleOrNull()?.let{resultRowToFlight(it)}
    }

    override suspend fun removeFLight(id: String): Boolean = DatabaseFactory.dbQuery {
        FLightDetailsDao.deleteWhere { flightNumber eq id }>0
    }

    override suspend fun bookFlight(details: BookDetailsOut): BookDetails? =
        DatabaseFactory.dbQuery {
            val insertStatement= BookDetailsDao.insert {
                it[passengerId]=details.passengerId
                it[flightNumber]=details.flightNumber
            }
            insertStatement.resultedValues?.singleOrNull()?.let { resultRowBooking(it) }
    }

    override suspend fun getFlight(fId: String): List<Flight> = DatabaseFactory.dbQuery {
        FLightDetailsDao.select(FLightDetailsDao.flightNumber eq fId).map { resultRowToFlight(it) }
    }

    override suspend fun getFlightAll(): List<Flight> = DatabaseFactory.dbQuery {
        FLightDetailsDao.selectAll().map { resultRowToFlight(it) }
    }

    override suspend fun getPassengerCountByFlight(flightId: String): Long = DatabaseFactory.dbQuery{
        BookDetailsDao.select(BookDetailsDao.flightNumber eq flightId).count()

    }

    override suspend fun userRegistration(details: Passenger): Passenger? = DatabaseFactory.dbQuery{
        val insertStatement= PassengerDao.insert {
            it[name]=details.name
            it[email]=details.email
            it[password]=details.password
        }
        insertStatement.resultedValues?.singleOrNull()?.let { resultRowPassenger(it) }
    }

    override suspend fun getPassengerId(name: String): PassengerId? = DatabaseFactory.dbQuery{
        PassengerDao.slice(PassengerDao.id).select(PassengerDao.name eq name)
            .map { resultRowId(it) }.firstOrNull()
    }

    override suspend fun getFlightId(id: Int): List<BookDetailsOut?> = DatabaseFactory.dbQuery{
        BookDetailsDao.select(BookDetailsDao.passengerId eq id)
            .map { resultRowBookingOut(it) }
    }
    override suspend fun getFlightById(passengerId:Int):List<Flight> = DatabaseFactory.dbQuery{
        ( BookDetailsDao innerJoin FLightDetailsDao).select(BookDetailsDao.passengerId eq passengerId)
            .map { resultRowToFlight(it) }
    }

    override suspend fun filterBySourceDestination(details: Filter): List<Flight> = DatabaseFactory.dbQuery {
        FLightDetailsDao.select(FLightDetailsDao.destination eq details.destination and (FLightDetailsDao.airport eq details.source) )
            .map { resultRowToFlight(it) }
    }

    override suspend fun userLogin(login: PassengerLogin): List<PassengerLogin> = DatabaseFactory.dbQuery {
        PassengerDao.select(PassengerDao.name eq login.name and (PassengerDao.password eq login.password ))
            .map { resultRowLogin(it) }
    }

    override suspend fun removeUser(id: Int): Boolean = DatabaseFactory.dbQuery {
        PassengerDao.deleteWhere { PassengerDao.id eq id }>0
    }


    private fun resultRowToFlight(row: ResultRow): Flight {
        return  Flight(row[FLightDetailsDao.flightNumber], row[FLightDetailsDao.airline], row[FLightDetailsDao.price], row[FLightDetailsDao.airport],row[FLightDetailsDao.destination],row[FLightDetailsDao.departureTime],row[FLightDetailsDao.arrivalTime])

    }
    private fun resultRowPassenger(row: ResultRow): Passenger {
        return Passenger(row[PassengerDao.name],row[PassengerDao.email],row[PassengerDao.password])
    }
    private fun resultRowBooking(row: ResultRow): BookDetails {
        return BookDetails(row[BookDetailsDao.passengerId],row[BookDetailsDao.flightNumber])
    }
    private fun resultRowId(row: ResultRow): PassengerId {
        return PassengerId(row[PassengerDao.id])
    }
    private fun resultRowBookingOut(row: ResultRow): BookDetailsOut {
        return BookDetailsOut(row[BookDetailsDao.ticketNo],row[BookDetailsDao.passengerId],row[BookDetailsDao.flightNumber])
    }
    private fun resultRowLogin(row: ResultRow):PassengerLogin{
        return PassengerLogin(row[PassengerDao.name],row[PassengerDao.password])
    }
}