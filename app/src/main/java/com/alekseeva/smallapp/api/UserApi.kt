package com.alekseeva.smallapp.api

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.alekseeva.smallapp.api.PreferencesKeys.USER
import com.alekseeva.smallapp.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface IUserApi {
    suspend fun saveUser(user: UserProfile): Boolean
    suspend fun getUser(): Flow<UserProfile?>
}

private object PreferencesKeys {
    val USER = stringPreferencesKey("user")
}

class UserApi @Inject constructor(@ApplicationContext private val context: Context) : IUserApi {

    override suspend fun saveUser(user: UserProfile): Boolean {
        putPreference(USER, Json.encodeToString(UserProfile.serializer(), user))
        return true
    }

    override suspend fun getUser() = getPreference(USER, "").map {
        Json.decodeFromStringOrNull<UserProfile>(it)
    }

    private inline fun <reified T> Json.decodeFromStringOrNull(string: String): T? {
        return try {
            decodeFromString(string)
        } catch (e: SerializationException) {
            Log.e(UserApi::class.java.simpleName, e.message ?: "error occurred")
            null
        }
    }

    private suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
            Flow<T> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[key] ?: defaultValue
        result
    }

    private suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
