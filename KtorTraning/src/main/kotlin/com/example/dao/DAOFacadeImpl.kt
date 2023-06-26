package com.example.dao

import com.example.model.Article
import com.example.model.ArticleReq
import com.example.model.Articles


import org.jetbrains.exposed.sql.*

class DAOFacadeImpl:DAOFacade {
    override suspend fun allArticles(): List<Article> = DatabaseFactory.dbQuery {

        Articles.selectAll().map(::resultRowToArticle)
    }
    override suspend fun addNewArticle(d: ArticleReq): Article? = DatabaseFactory.dbQuery {
        val insertStatement = Articles.insert {
            it[name]=d.name
            it[password]=d.password

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    private fun resultRowToArticle(row:ResultRow): Article {
        return  Article(row[Articles.id], row[Articles.name],row[Articles.password])

    }
}

