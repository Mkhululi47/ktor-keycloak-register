package com.arminganic

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        post("/api/user/register") {
            val keycloak = KeycloakBuilder.builder()
                .serverUrl("http://127.0.0.1:8180/auth")
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .clientSecret("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
                .build()

            val user = UserRepresentation()
            user.isEnabled = true
            user.username = "username"
            user.firstName = "First Name"
            user.lastName = "Last Name"
            user.email = "first@last.name"

            val password = CredentialRepresentation()
            password.isTemporary = false
            password.type = CredentialRepresentation.PASSWORD
            password.value = "this-is-a-very-strong-password"

            val realmResource = keycloak.realm("master")
            val usersResource = realmResource.users()
            usersResource.create(user)

            call.respond("User registration successful!")
        }
    }
}
