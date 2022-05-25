from functools import reduce

import PIL
from PIL import Image
import numpy as np

width = 0
height = 0


def load_image(filename):
    global width, height
    image = Image.open(filename)
    width, height = image.size
    scale = 1
    image = image.resize((width * scale, height * scale),
                          PIL.Image.Resampling.NEAREST)
    width, height = image.size
    image = np.array(list(image.getdata()), dtype="i,i,i").astype(object).reshape((height, width))
    return image

def main():
    img = load_image("./cave_small.png")
    bool_pic = np.zeros(shape=img.shape, dtype=bool)
    for ndindex, rgb in np.ndenumerate(img):
        bool_pic[ndindex] = (rgb == (255, 255, 255) or 0 in ndindex)
    bool_pic = bool_pic.T
    height, width = bool_pic.shape

    with open("./cave.store", "w") as file:
        file.write(str(height)+" "+str(width)+"\n")
        for i in range(height):
            file.write(reduce(lambda x, y: x+" "+str(y), bool_pic[i], ""))
        file.write("\n")
        for i in range(height):
            for j in range(3*width):
                # if j < 10:
                #     file.write(" 1.0")
                # else:
                #     file.write(" NaN")
                file.write(" NaN")


if __name__ == "__main__":
    main()