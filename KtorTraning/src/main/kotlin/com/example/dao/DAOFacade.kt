package com.example.dao

import com.example.model.UserDetails
import com.example.model.UserReq

interface DAOFacade {

    suspend fun allUsers(): List<UserDetails>
    suspend fun addNewUser(d:UserReq): UserDetails?
    suspend fun findById(id:Int):UserDetails?
    suspend fun editUser(details:UserDetails): Boolean
}