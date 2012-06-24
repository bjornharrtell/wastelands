#!/usr/bin/python

import os
import glob

import Image
import ImageDraw

blocksize = 32
width = (blocksize*50)+(50*1)
height = blocksize+2

resource = Image.new("RGBA", (width,height), None)
resourceDraw = ImageDraw.Draw(resource)

count = 0
col = 1
row = 1

path = '/tmp/'
files = glob.glob( os.path.join(path, '*.png') )
files.sort()
for file in files:
  source = Image.open(file)  
  block = source.transform((32,32), Image.EXTENT, (0,0,64,64), Image.BICUBIC)
  resourceDraw.rectangle([(col-1,row-1),(col+blocksize+1, row+blocksize+1)], fill="rgb(255,0,255)")
  resource.paste(block, (col,1))
  col += 32+1
  count += 1

resource.save("test.png")

