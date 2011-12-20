->DuneDevs by Bozo © 1999

OPT MODULE
OPT EXPORT
OPT PREPROCESS

->PROC Modules
MODULE 'intuition/intuition',
       'hardware/dmabits'
->ENDPROC

ENUM ERR_NON,ERR_ARG,ERR_OSV,ERR_LOW,ERR_AGA,ERR_SCR,ERR_WIN,ERR_BUF,ERR_MSG,ERR_IO,ERR_DB,ERR_COD,ERR_LIB,ERR_MEM,ERR_REQ,ERR_SR,ERR_NO

->PROC Defines (Enviroment)
      CONST DMAB=DMAB_BLTDONE-8
      CONST BM_PLANES=8

      ->Main images constants.
      CONST BLSIZE=16          ->Size of block(square)
      CONST BLOCKLEN=256       ->File size(bytes) of RAW blocks
      CONST SHADELEN=32
      ->Rawkey codes.
      CONST RIGHT=$4E, ->These are the used rawkeys.
            LEFT=$4F,
            DOWN=$4D,
            UP=$4C,
            PLUS=$5E,
            MINUS=$4A,
            ESC=$45,
            HELP=$5F,
            REQ1=$50
      ->Other constants.
      CONST PLANES=8

      CONST URIGHT=0,
            UDOWN=6,
            ULEFT=12,
            UUP=18

      CONST NOSUPPORT=0,
            PLANAR=1,
            CHUNKY=2,
            IPLANAR=3

      CONST GUI=0,
            BLOCKMAP=1,
            TINYMAP=2
->ENDPROC

->PROC Defines (Misc)

->ENDPROC
->PROC Defines (Ground)

CONST DESERT=0,   -> Desert piece.
      ROCK=1,     -> Rock piece.
      DUNE=2,      -> Dune piece.
      SPICE=3
CONST VISIBLE=0,
      SHADED=1,
      DARK=2

->ENDPROC
->PROC Defines (Buildings)

->ENDPROC
->PROC Defines (Units)
    CONST UNITRIGHT=0,
          UNITRIGHTDOWN=1,
          UNITDOWN=2,
          UNITLEFTDOWN=3,
          UNITLEFT=4,
          UNITLEFTUP=5,
          UNITUP=6,
          UNITRIGHTUP=7

    CONST ATANK=1,
          AHARVESTER=2
->ENDPROC

->PROC Objects
OBJECT map
    imageorg:LONG
    image:LONG
    orgshade:LONG
    shade:LONG
    orgdark:LONG
    dark:LONG
    building:PTR TO building
ENDOBJECT
OBJECT building ->Building object
    type:LONG
    img[9]:ARRAY OF LONG  -> ptrs to building images (blocks)
    life:LONG
    power:LONG  ->Buildings with firepower.
    men:LONG    ->How many soldiers if destroyed.
ENDOBJECT
OBJECT unit
    type:LONG
    dir:LONG -> Unit direction
    life:LONG
    speed:LONG
    srcx:LONG
    srcy:LONG
    destx:LONG
    desty:LONG
    countx:LONG
    county:LONG
    power:LONG
ENDOBJECT
OBJECT atreides
    tank[8]:ARRAY OF LONG
    tankmask[8]:ARRAY OF LONG
    harvester[8]:ARRAY OF LONG
    harvestermask[8]:ARRAY OF LONG
ENDOBJECT
->ENDPROC

