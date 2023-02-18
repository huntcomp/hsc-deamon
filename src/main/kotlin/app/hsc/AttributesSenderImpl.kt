package app.hsc

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.functions.functions
import io.ktor.client.statement.*
import io.ktor.http.*

class AttributesSenderImpl(
    private val client: SupabaseClient
) : AttributesSender {
    override suspend fun sendMatch(player: String, body: String): HttpResponse? =
        try {
            println("Sending data to supabase")
            val response = client.functions(
                function = "load-match",
                body = body,
                headers = Headers.build {
                    append(HttpHeaders.ContentType, "application/xml")
                    append("X-Played-As", player)
                }
            )
            println("Request: ${response.request}")
            println("Response: $response")
            response
        } catch (ex: UnauthorizedRestException) {
            println(ex)
            if(ex.message?.contains("Game was already registered") != true) {
                throw ex
            }
            null
        }

}
