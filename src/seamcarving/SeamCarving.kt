package seamcarving

import java.awt.image.BufferedImage
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class SeamCarving {

    fun crop(image: BufferedImage, cropWidth: Int, cropHeight: Int): BufferedImage {
        var cropImage = image

        for (i in 0 until cropWidth) {
            val imageEnergy = getImageEnergy(cropImage)
            val imageEnergySum = getEnergySumArray(imageEnergy, Axis.VERTICAL)
            val seam = getSeam(imageEnergySum)
            cropImage = delSeam(cropImage, seam, Axis.VERTICAL)
        }

        for (i in 0 until cropHeight) {
            val imageEnergy = getImageEnergy(cropImage)
            val imageEnergySum = getEnergySumArray(imageEnergy, Axis.HORIZONTAL)
            val seam = getSeam(imageEnergySum)
            cropImage = delSeam(cropImage, seam, Axis.HORIZONTAL)
        }

        return cropImage
    }

    private fun delSeam(image: BufferedImage, seam: Array<Int>, axis: Axis): BufferedImage {
        val resImageWidth = if (axis == Axis.HORIZONTAL) image.width else image.width - 1
        val resImageHeight = if (axis == Axis.HORIZONTAL) image.height - 1 else image.height
        val resImage = BufferedImage(resImageWidth, resImageHeight, image.type)

        // TODO refactor
        if (axis == Axis.VERTICAL) {
            for (j in 0 until resImage.height) {
                for (i in 0 until resImage.width) {
                    val color = if (seam[j] > i) image.getRGB(i, j) else image.getRGB(i + 1, j)
                    resImage.setRGB(i, j, color)
                }
            }
        } else {
            for (i in 0 until resImage.width) {
                for (j in 0 until resImage.height) {
                    val color = if (seam[i] > j) image.getRGB(i, j) else image.getRGB(i, j + 1)
                    resImage.setRGB(i, j, color)
                }
            }
        }
        return resImage
    }

    private fun getSeam(imageEnergySum: Array<Array<Double>>): Array<Int> {
        val result = Array(imageEnergySum[0].size) { 0 }
        val lastY = imageEnergySum[0].size - 1
        var minEnergy = imageEnergySum[0][lastY]

        for (i in imageEnergySum.indices) {
            if (imageEnergySum[i][lastY] < minEnergy) {
                minEnergy = imageEnergySum[i][lastY]
                result[lastY] = i
            }
        }

        for (yIndex in lastY - 1 downTo 0) {
            val prevXIndex = result[yIndex + 1]
            result[yIndex] = prevXIndex
            if (prevXIndex > 0 && imageEnergySum[prevXIndex][yIndex] > imageEnergySum[prevXIndex - 1][yIndex]) result[yIndex] = prevXIndex - 1
            if (prevXIndex < imageEnergySum.size - 1 && imageEnergySum[result[yIndex]][yIndex] > imageEnergySum[prevXIndex + 1][yIndex]) result[yIndex] = prevXIndex + 1
        }
        return result
    }

    private fun getEnergySumArray(imageEnergy: Array<Array<Double>>, axis: Axis): Array<Array<Double>> {

        fun transMatrix(matrix: Array<Array<Double>>): Array<Array<Double>> {
            val res = Array(matrix[0].size) { Array(matrix.size) { 0.0 } }
            for (i in res.indices) {
                for (j in res[0].indices) res[i][j] = matrix[j][i]
            }
            return (res)
        }

        val energyArray = if (axis == Axis.HORIZONTAL) transMatrix(imageEnergy) else imageEnergy

        val dp = Array(energyArray.size) { Array(energyArray[0].size) { 0.0 } }

        for (i in energyArray.indices) {
            dp[i][0] = energyArray[i][0]
        }

        for (j in 1 until energyArray[0].size) {
            for (i in energyArray.indices) {
                dp[i][j] = dp[i][j - 1]
                if (i > 0) {
                    dp[i][j] = min(dp[i][j], dp[i - 1][j - 1])
                }
                if (i < energyArray.size - 1) {
                    dp[i][j] = min(dp[i][j], dp[i + 1][j - 1])
                }
                dp[i][j] += energyArray[i][j]
            }
        }
        return dp
    }

    private fun getImageEnergy(image: BufferedImage): Array<Array<Double>> {

        fun gradientFromPixels(a: Pixel, b: Pixel): Double {
            return (a.red - b.red).toDouble().pow(2.0) +
                    (a.green - b.green).toDouble().pow(2.0) +
                    (a.blue - b.blue).toDouble().pow(2.0)
        }

        fun getPixelEnergy(i: Int, j: Int): Double {
            val x = when (i) {
                0 -> 1
                image.width - 1 -> image.width - 2
                else -> i
            }
            val y = when (j) {
                0 -> 1
                image.height - 1 -> image.height - 2
                else -> j
            }

            val leftPixel = Pixel(image.getRGB(x - 1, j))
            val rightPixel = Pixel(image.getRGB(x + 1, j))
            val gradientX = gradientFromPixels(leftPixel, rightPixel)

            val downPixel = Pixel(image.getRGB(i, y - 1))
            val upPixel = Pixel(image.getRGB(i, y + 1))
            val gradientY = gradientFromPixels(upPixel, downPixel)

            return sqrt(gradientX + gradientY)
        }

        val energy: Array<Array<Double>> = Array(image.width) { Array(image.height) { 0.0 } }
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                energy[i][j] = getPixelEnergy(i, j)
            }
        }
        return energy
    }
}
