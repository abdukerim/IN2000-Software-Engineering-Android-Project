package kerim.android.bade.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kerim.android.bade.data.*
import kerim.android.bade.directions.Directions
import kerim.android.bade.weatherDto.Weathers
import java.util.*

class DataSource {

    private val ifiPosition = "59.9432474139966, 10.717752907860755"
    private var tsery : List<Tsery> = listOf()
    private val googleKey = "AIzaSyC22Y4vgnXIG0d0ApAIxa9TarV_WIV14Fk"

    private val baseUrl = "https://havvarsel-frost.met.no/api/v1/obs/badevann/get?parameters=temp%2A&time=latest&latestmaxage=86400&incobs=true"
    private val gson = Gson()
    private val weatherUrl = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete?"

    suspend fun fetchBadeInfo(): List<Tsery>? {

        return try { // try catch to not crash when we get null
            val response =
                gson.fromJson(Fuel.get(baseUrl).timeout(60000).timeoutRead(60000).awaitString(), BadeInfo::class.java)
            tsery = response.data.tseries
            tsery

        } catch (exception: Exception) {
            Log.d("DATA SOURCE", "A network request exception was thrown: ${exception.message}")
            null
        }


    }
    suspend fun fetchWeather() : List<Tsery> {
        for (i in tsery) {

            try {
                val weatherResponse = gson.fromJson(Fuel.get(weatherUrl +
                        "lat=" + i.header.extra.pos.lat + "&lon=" + i.header.extra.pos.lon).timeout(5000).timeoutRead(5000).awaitString(),
                    Weathers::class.java)

                //val default : LatLng = DefaultPosition.defaultPosition()

                //i.header.extra.pos.distance = CoordinateDistance.distance(default.latitude, default.longitude, i.header.extra.pos.lat.toDouble(), i.header.extra.pos.lon.toDouble())
                i.header.extra.pos.airTemp = weatherResponse.properties.timeseries[0].data.instant.details.air_temperature.toString()

                i.header.extra.pos.humidity = weatherResponse.properties.timeseries[0].data.instant.details.relative_humidity.toString()
                i.header.extra.pos.vindDirection = weatherResponse.properties.timeseries[0].data.instant.details.wind_from_direction.toString()
                i.header.extra.pos.windSpeed = weatherResponse.properties.timeseries[0].data.instant.details.wind_speed.toString()
                i.header.extra.pos.summary = weatherResponse.properties.timeseries[0].data.next_1_hours.summary.symbol_code

            } catch (exception: Exception) {
                Log.d("DATA SOURCE: WEATHER", "A network request exception was thrown: ${exception.message}")
            }
        }
        return tsery
    }

    companion object {
        fun getComparator(type: String): Comparator<Tsery> {

            when(type) {
                "Vann Temp" -> return compareByDescending {

                    it.observations[0].body.value.toDouble()
                }
                "Distanse" -> return compareBy {
                    it.header.extra.pos.distance
                }
                "Luft Temp" -> return compareByDescending {

                    it.header.extra.pos.airTemp.toDouble()
                }
            }

            Log.d("COMPARE", "Bad parameter: $type" )
            return compareBy { // default comparator if we get a bad parameter
                it.observations[0].body.value.toDouble()
            }
        }
    }

    suspend fun getDistance() : List<Tsery> {
        for (i in tsery) {
            val destination = "${i.header.extra.pos.lat},${i.header.extra.pos.lon}"
            try { // try catch so it does not crash when no response

                val distanceResponse =
                    gson.fromJson(Fuel.get("https://maps.googleapis.com/maps/api/directions/json?destination=${destination}&origin=${ifiPosition}&key=${googleKey}")
                        .awaitString(),
                        Directions::class.java)
                i.header.extra.pos.distance = distanceResponse.routes[0].legs[0].distance.value
                i.header.extra.pos.time = distanceResponse.routes[0].legs[0].duration.text

            } catch (exception: Exception) {
                Log.d("DATA SOURCE: DISTANSE", "Could not find destination: ${exception.message}")
            }
        }

        tsery = tsery.sortedWith(getComparator("Vann Temp")).toMutableList()
        return tsery
    }
}

