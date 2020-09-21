package seamcarving

import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    var inputFilePath = ""
    var outFilePath = ""
    var cropWidth = 0
    var cropHeight = 0

    for (i in args.indices) {
        when (args[i]) {
            "-in" -> inputFilePath = args[i + 1]
            "-out" -> outFilePath = args[i + 1]
            "-width" -> cropWidth = args[i + 1].toInt()
            "-height" -> cropHeight = args[i + 1].toInt()
        }
    }
    val image = ImageIO.read(File(inputFilePath))

    ImageIO.write(SeamCarving().crop(image, cropWidth, cropHeight), "png", File(outFilePath))
}
