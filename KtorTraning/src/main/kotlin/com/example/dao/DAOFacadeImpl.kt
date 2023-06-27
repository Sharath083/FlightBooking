package com.example.dao

import com.example.model.UserDetails
import com.example.model.UserReq
import com.example.model.Details


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl:DAOFacade {
    override suspend fun allUsers(): List<UserDetails?> = DatabaseFactory.dbQuery {

        Details.selectAll().map{resultRowToArticle(it)}
    }
    override suspend fun addNewUser(d: UserReq): UserDetails? = DatabaseFactory.dbQuery {
        val insertStatement = Details.insert {
            it[name]=d.name
            it[password]=d.password

        }
        insertStatement.resultedValues?.singleOrNull()?.let{resultRowToArticle(it)}
    }

    override suspend fun findById(id:Int): UserDetails? {
        return DatabaseFactory.dbQuery {

            Details.select(Details.id eq id).map{resultRowToArticle(it)}.singleOrNull()

        }
    }

    override suspend fun editUser(details: UserDetails): Boolean =
        DatabaseFactory.dbQuery {
            Details.update ({ Details.id eq details.id }){//returns 1 if update is success
                it[name]=details.name
                it[password]=details.password
            }>0  //1>0 true

    }

    override suspend fun userLogin(details: UserReq): UserDetails? {

        return DatabaseFactory.dbQuery {
            Details.select{(Details.password eq details.password) and (Details.name eq details.name)}.map { resultRowToArticle(it)}.firstOrNull()
        }
    }

    private fun resultRowToArticle(row:ResultRow): UserDetails {
        return  UserDetails(row[Details.id], row[Details.name],row[Details.password])

    }


}

