package kerim.android.bade.data

data class BadeInfo(
    val data: Data
)
data class Data(
    val tseries: List<Tsery>,
    val tstype: String
)

data class Tsery(
    val header: Header,
    val observations: List<Observation>
)
    : Comparable<Tsery> {
    override fun compareTo(other: Tsery): Int { // default compares temperature
        return (this.observations[0].body.value
            .toDouble().compareTo(other.observations[0].body.value.toDouble()))
    }

}
data class Header(
    val extra: Extra,
    val id: Id
)
data class Observation(
    val body: Body,
    val time: String
)
data class Extra(
    val name: String,
    val pos: Pos
)
data class Pos(
    val lat: String,
    val lon: String,
    var airTemp : String,
    var humidity : String,
    var vindDirection : String,
    var windSpeed : String,
    var summary: String,
    var distance : Int,
    var time : String
)

data class Id(
    val buoyid: String,
    val parameter: String,
    val source: String
)
data class Body(
    val value: String,
    val airTemp : String,
    val humidity : String,
    val vindDirection : String,
    val windSpeed : String,
    val summary: String
)