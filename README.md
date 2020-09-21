# SeamCarving
Seam carving an algorithm for content-aware image resizing.

The utility was written to compress photos without losing quality in order to lighten the load on the devices of PromoCat users.

More about seam carving algorithm: [wiki](https://en.wikipedia.org/wiki/Seam_carving)

Launching
---
Launch via CLI. Format: java Main -in {in} -out {out} -width {width} -height {heigth}

* in - input file name in .png format
* out - output file name in .png format
* width - the number of pixels to crop in width
* height - the number of pixels to crop in height

Examples
---
* **Input image (crop width 125, height 50)**

![Input image blue](https://github.com/maxim092001/SeamCarving/blob/master/resources/blue.png)


* **Output image**

![Output image blue](https://github.com/maxim092001/SeamCarving/blob/master/resources/blue-reduced.png)


* **Input image (crop width 100, heigth 30)**

![Input image trees](https://github.com/maxim092001/SeamCarving/blob/master/resources/trees.png)

* **Output image**

![Output image trees](https://github.com/maxim092001/SeamCarving/blob/master/resources/trees-reduced.png)


Compression process
---

* Calculate the energy of all pixels.
* Find the minimum energy path from top to bottom.
* Remove seam.
* Similarly, horizontally, paths are searched from left to right and the path is deleted.



* **Energy**:

![Energy](https://github.com/maxim092001/SeamCarving/blob/master/resources/blue-energy.png)

* **Seam**:

![Seam](https://github.com/maxim092001/SeamCarving/blob/master/resources/blue-seam.png)
