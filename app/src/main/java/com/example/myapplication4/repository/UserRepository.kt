package com.example.myapplication4.repository

import com.example.myapplication4.clases.Usuario
import com.example.myapplication4.model.UserDao
import com.example.myapplication4.model.toDomain
import com.example.myapplication4.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface UserRepository {
    fun getAllUsers(): Flow<List<Usuario>>
    suspend fun getUserByUsername(username: String): Usuario?
    suspend fun addUser(usuario: Usuario)
    suspend fun updateUser(usuario: Usuario)
    suspend fun deleteUser(usuario: Usuario)
    suspend fun initializeDefaultUsers()
    suspend fun authenticateUser(username: String, password: String): Usuario?
}

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<Usuario>> =
        userDao.getAllUsers().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getUserByUsername(username: String): Usuario? =
        userDao.getUserByUsername(username)?.toDomain()

    override suspend fun addUser(usuario: Usuario) {
        userDao.insertUser(usuario.toEntity())
    }

    override suspend fun updateUser(usuario: Usuario) {
        userDao.updateUser(usuario.toEntity())
    }

    override suspend fun deleteUser(usuario: Usuario) {
        userDao.deleteUser(usuario.toEntity())
    }

    override suspend fun initializeDefaultUsers() {
        val currentUsers = userDao.getAllUsers().first()

        if (currentUsers.isEmpty()) {
            val defaultUsers = listOf(
                Usuario(1, "Administrador", "admin", "admin", 0f),
                Usuario(2, "Sebastian", "seba", "seba", 0f),
                Usuario(3, "Santiago", "santi", "santi", 0f)
            )

            defaultUsers.forEach { user ->
                addUser(user)
            }
        }
    }

    override suspend fun authenticateUser(username: String, password: String): Usuario? {
        val user = userDao.getUserByUsername(username)
        return if (user?.pwd == password) user.toDomain() else null
    }
}