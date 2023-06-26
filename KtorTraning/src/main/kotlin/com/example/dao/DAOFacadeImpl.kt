package com.example.dao

import com.example.model.UserDetails
import com.example.model.UserReq
import com.example.model.Articles


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl:DAOFacade {
    override suspend fun allArticles(): List<UserDetails> = DatabaseFactory.dbQuery {

        Articles.selectAll().map(::resultRowToArticle)
    }
    override suspend fun addNewArticle(d: UserReq): UserDetails? = DatabaseFactory.dbQuery {
        val insertStatement = Articles.insert {
            it[name]=d.name
            it[password]=d.password

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    override suspend fun Article(id:Int): UserDetails? {
        return DatabaseFactory.dbQuery {

            Articles.select(Articles.id eq id).map(::resultRowToArticle).singleOrNull()

        }
    }

    override suspend fun editArticle(details: UserDetails): Boolean =
        DatabaseFactory.dbQuery {
            Articles.update ({ Articles.id eq details.id }){//returns 1 if update is success
                it[name]=details.name
                it[password]=details.password
            }>0  //1>0 true

    }


    private fun resultRowToArticle(row:ResultRow): UserDetails {
        return  UserDetails(row[Articles.id], row[Articles.name],row[Articles.password])

    }


}

