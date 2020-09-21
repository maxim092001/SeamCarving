package seamcarving


/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:06 14.09.2020
 */
class Pixel(rgb: Int) {


    var red: Int = 0
    var green: Int = 0
    var blue: Int = 0

    init {
        fillColors(rgb)
    }

    private fun fillColors(rgb: Int) {
        this.red = rgb shr 16 and 0x000000FF
        this.green = rgb shr 8 and 0x000000FF
        this.blue = rgb and 0x000000FF
    }

    fun toRGB(): Int {
        return red shl 16 or (green shl 8) or blue
    }
}
