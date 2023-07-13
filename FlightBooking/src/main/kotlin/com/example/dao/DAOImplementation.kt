package com.example.dao

import com.example.objects.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class DAOImplementation:DAOInterface {


    override suspend fun addNewFlight(d: Flight): Flight?=DatabaseFactory.dbQuery {
        val insertStatement = FLightDetailsDao.insert {
            it[flightNumber]=d.flightNumber
            it[airport]=d.source
            it[destination]=d.destination
            it[departureTime]=d.departureTime
            it[arrivalTime]=d.arrivalTime
        }
        insertStatement.resultedValues?.singleOrNull()?.let{resultRowToFlight(it)}
    }

    override suspend fun removeFLight(id: String): Boolean =DatabaseFactory.dbQuery {
        FLightDetailsDao.deleteWhere { FLightDetailsDao.flightNumber eq id }>0
    }

    override suspend fun bookFlight(details: BookDetailsOut): BookDetails? =
        DatabaseFactory.dbQuery {
            val insertStatement=BookDetailsDao.insert {
//                it[ticketNo]=(9999..99999).shuffled().last()
//                it[ticketNo]=0
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

    override suspend fun getPassengerCountByFlight(flightId: String): Long =DatabaseFactory.dbQuery{
        BookDetailsDao.select(BookDetailsDao.flightNumber eq flightId).count()

    }

//    override suspend fun getFlight(id: Passenger): List<Flight> = DatabaseFactory.dbQuery {

//        Actors.join(StarWarsFilms, JoinType.INNER, onColumn = Actors.sequelId, otherColumn = StarWarsFilms.sequelId)
//            .slice(Actors.name.count(), StarWarsFilms.name)
//            .selectAll()
//            .groupBy(StarWarsFilms.name)
//        BookDetailsDao.join(FLightDetailsDao, JoinType.INNER, onColumn = BookDetailsDao.flightNumber, otherColumn = FLightDetailsDao.flightNumber)
//            .select { BookDetailsDao.passengerId eq id.name}
//            .toList()
//            .map { resultRowToFlight(it) }


//        BookDetailsDao.join(FLightDetailsDao, JoinType.INNER, additionalConstraint = {
//            BookDetailsDao.flightNumber eq FLightDetailsDao.flightNumber })
//                .select { BookDetailsDao.passengerName eq id.name}
//                .groupBy(FLightDetailsDao.flightNumber).toList()

//    }

    override suspend fun addNewPassenger(d: Passenger): Passenger? =DatabaseFactory.dbQuery{
        val insertStatement=PassengerDao.insert {
            it[name]=d.name
        }
        insertStatement.resultedValues?.singleOrNull()?.let { resultRowPassenger(it) }
    }

    override suspend fun getPassengerId(name: String): PassengerId? = DatabaseFactory.dbQuery{
        PassengerDao.slice(PassengerDao.id).select(PassengerDao.name eq name).map { resultRowId(it) }.firstOrNull()
    }

    override suspend fun getFlightId(id: Int): List<BookDetailsOut?> =DatabaseFactory.dbQuery{
        BookDetailsDao.select(BookDetailsDao.passengerId eq id).map { resultRowBookingOut(it) }
    }
    suspend fun dds(s:String):List<Flight> =DatabaseFactory.dbQuery{
        ( FLightDetailsDao innerJoin BookDetailsDao).select(BookDetailsDao.flightNumber eq s).map { resultRowToFlight(it) }
    }






//        val startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()
//        val endTime=LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm")).toSecondOfDay()
//        return ((endTime-startTime)/3600).toString()



    private fun resultRowToFlight(row: ResultRow): Flight {
        return  Flight(row[FLightDetailsDao.flightNumber], row[FLightDetailsDao.airport],row[FLightDetailsDao.destination],row[FLightDetailsDao.departureTime],row[FLightDetailsDao.arrivalTime])

    }
    private fun resultRowPassenger(row: ResultRow): Passenger {
        return Passenger(row[PassengerDao.id],row[PassengerDao.name])
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
}