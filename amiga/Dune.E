->********** Dune III Beta 0.2 by Bozo © 1995-2000 ********************

->PROC Init Folder

OPT PREPROCESS,020->,REG=5

CONST KP7=$3D
CONST KP8=$3E
CONST KP9=$3F
CONST KP4=$2D
CONST KP5=$2E
CONST KP6=$2F
CONST KP1=$1D
CONST KP2=$1E
CONST KP3=$1F

->PROC Modules
MODULE 'dos/dos',
       'intuition/intuition','intuition/intuitionbase','intuition/screens',
       'exec/execbase','exec/ports','exec/memory','exec/ports','exec/nodes',
       'graphics/gfx','graphics/gfxbase','graphics/modeid','graphics/rastport','graphics/view','graphics/videocontrol','graphics/text',
       'asl','libraries/asl',
       'utility','utility/tagitem',
       'hardware/custom','hardware/dmabits','hardware/blit',
       'rtgmaster','rtgmaster/rtgmaster','rtgmaster/rtgsublibs','rtgmaster/rtgc2p',
       '*DuneDevs','*DuneGfx'
->ENDPROC
->PROC Defines

->Screen variables
DEF   mainS=NIL:PTR TO rtgscreen,
      cm[1000]:ARRAY OF LONG,
      backBuf,frontBuf,  -> Doublebuffered screenbitmap
      markerBuf, ->Buffer for marker handling
      gfxmode=0,-> Graphics mode handling.
      defpointer,
      mappointer,
      infotag:PTR TO tagitem
DEF   pixX=0,            ->Pixels/Resolution.
      pixY=0
DEF   row,rows,bytesperblock

->Block Varables
DEF   blockY,         ->Number of blocks in Y(down).
      blockX,         ->Number of blocks in X(right).
      blockOX,
      blockOY,
      blockrow
DEF   blockDB:PTR TO LONG      ->Holds current blockpointers viewed. (30*30 max)
DEF   darkDB:PTR TO LONG      ->Holds current blockpointers viewed.
DEF   shadeDB:PTR TO LONG
DEF   block[400]:ARRAY OF LONG         ->Holds blockpointers (for pointerderivation)
DEF   dark[101]:ARRAY OF LONG
DEF   shade[101]:ARRAY OF LONG
DEF   bitDef[256]:ARRAY OF CHAR        -> Bit definitons for block-calc.

->GUI variables
DEF   rightspace,
      lowerspace,
      textX,textY,
      textUX,textUY,textLX,textLY

->Map variables.
DEF   offY=0,offX=0  ->Current map offset. (viewed portion of map)
DEF   mapX=0,mapY=0  ->Map coords under the mousepointer.
DEF   map:PTR TO map
DEF   yP[200]:ARRAY OF LONG            -> Precalc.
DEF   ymapP[200]:ARRAY OF LONG         -> Precalc.
DEF   tinyX,tinyY,                     -> Tiny map size.
      tinyUX,tinyUY,tinyLX,tinyLY,     -> Tiny map coords.
    ->  mapSX=73,
    ->  mapSY=73
      mapSX=90,
      mapSY=88

->Misc variables
DEF ok,font
DEF blocknr=1
DEF col
DEF pri
DEF mouseX,mouseY,mouse

->Colors
DEF black=0,white=1,lightgrey=2,grey=5,darkgrey=9,verydarkgrey=12->,red,green,blue
DEF desert=14,rock=8,dunes=13,spice=29
->DEF desert,rock,dunes

->Files
DEF mapF,bitF,palF,markF,uDir
DEF desertF[256]:STRING,
    rockF[256]:STRING,
    dunesF[256]:STRING,
    spiceF[256]:STRING,
    darkF[256]:STRING,
    shadeF[256]:STRING,
    fString[256]:STRING

->Exceptionstrings
DEF asl,rtgmaster,utility

DEF atreides:atreides
DEF unit:PTR TO unit

->ENDPROC
->ENDPROC

->Main
PROC main() HANDLE
    DEF string[256]:STRING

    asl:='asl.library'
    utility:='utility.library'
    rtgmaster:='rtgmaster.library'

    ok:='Gfx/Blocks/Chunky/'
    StrCopy(desertF,ok,ALL)
    StrCopy(rockF,ok,ALL)
    StrCopy(dunesF,ok,ALL)
    StrCopy(spiceF,ok,ALL)
    StrCopy(shadeF,ok,ALL)
    StrCopy(darkF,ok,ALL)
    blocks(desertF,rockF,dunesF,spiceF,shadeF,darkF)

    mapF:=   'Data/Map.DAT'
    bitF:=   'Data/Blocks.DAT'
    palF:=   'Data/Palette.DAT'
    markF:=  'Data/Marker.DAT'
    uDir:=   'Units/'

    ok:='QUIT'

    IF wbmessage=NIL THEN args()

    SetTaskPri(FindTask(0),pri)
    IF KickVersion(39)=FALSE THEN Raise(ERR_OSV)
    IF (aslbase:=OpenLibrary(asl,NIL))=NIL THEN Throw(ERR_LIB,asl)
    IF (utilitybase:=OpenLibrary(utility,NIL))=NIL THEN Throw(ERR_LIB,utility)
    IF (rtgmasterbase:=OpenLibrary(rtgmaster,NIL))=NIL THEN Throw(ERR_LIB,rtgmaster)

    WriteF('Dune III Beta 0.2 by Bozo.\n\n'+
           'NOTE: This is only a testing verison.\n'+
           'Read the guide for more information.\n'+
           'Press HELP for key information inside game.\n\n')

    preCalc()
    init()

    WriteF('Thanx for testing Dune III.\n')
EXCEPT DO
    IF aslbase       THEN CloseLibrary(aslbase)
    IF utilitybase   THEN CloseLibrary(utilitybase)
    IF rtgmasterbase THEN CloseLibrary(rtgmasterbase)
    SELECT exception
        CASE ERR_OSV; req('Requires WB3.0+.',ok)
        CASE ERR_LIB; req(StringF(string,'Could not open \s.',exceptioninfo),ok)
        CASE ERR_MSG; req('Could not create input port.',ok)
        CASE ERR_SCR; req('Could not open main screen.',ok)
        CASE ERR_WIN; req('Could not open main window.',ok)
        CASE ERR_REQ; req('Could not open filerequester.',ok)
        CASE ERR_BUF; req('Could not create screenbuffer.',ok)
        CASE ERR_MEM; req('Out of memory.',ok)
        CASE ERR_ARG; req('Error in arguments, use ? or INFO.',ok)
        CASE ERR_IO ; req(StringF(string,'File ''\s'' is missing or corrupt.',exceptioninfo),ok)
        CASE ERR_DB ; req('Error in doublebuffering.',ok)
        CASE ERR_COD; req('A serious error has occured.',ok)
        CASE ERR_SR ; req('Screenmode not supported.',ok)
        DEFAULT;
    ENDSELECT
ENDPROC
PROC preCalc()
    DEF c

    FOR c:=0 TO 199  -> Precalc.
        yP[c]:=mapSX*c
    ENDFOR

    FOR c:=0 TO 199  -> Precalc.
        ymapP[c]:=blockX*c
    ENDFOR
ENDPROC
PROC args() HANDLE ->Normal OS2.0 CLI arguments parser.
    DEF args:PTR TO LONG,templ,rdargs,str[500]:STRING
    templ:='PRI/N' ; args:=[0]

    rdargs:=ReadArgs(templ,args,NIL)
    IF rdargs=NIL THEN Raise(ERR_ARG)
    
    pri:=  IF args[0] THEN Long(args[0]) ELSE 0

EXCEPT DO
    IF rdargs THEN FreeArgs(rdargs)
    ReThrow()
ENDPROC
PROC req(title,b)
ENDPROC EasyRequestArgs(NIL,[SIZEOF easystruct,0,'Dune III requester',title,b],0,NIL)
PROC fReq(title,oktext,canceltext,dir) HANDLE
    DEF fr:PTR TO filerequester,succ=TRUE
    IF fr:=AllocAslRequest(ASL_FILEREQUEST,
                       [ASL_HAIL,       title,
                        ASL_OKTEXT,     oktext,
                        ASL_CANCELTEXT, canceltext,
->                        ASL_FILE,       file,
                        ASL_DIR,        dir,
                        NIL])
        IF AslRequest(fr, NIL)
            StrCopy(fString,fr.drawer,ALL)
            AddPart(fString,fr.file,255)
            SetStr(fString,StrLen(fString))
        ELSE
            succ:=FALSE
        ENDIF
    ELSE
        Raise(ERR_REQ)
    ENDIF
EXCEPT DO
    ReThrow()
ENDPROC succ
PROC blocks(str1,str2,str3,str4,str5,str6)
    StrAdd(str1,'Desert.GFX',ALL)
    StrAdd(str2,'Rock.GFX',ALL)
    StrAdd(str3,'Dunes.GFX',ALL)
    StrAdd(str4,'Spice.GFX',ALL)
    StrAdd(str5,'Shade.GFX',ALL)
    StrAdd(str6,'Dark.GFX',ALL)
ENDPROC

->Program
PROC init() HANDLE -> Init procedure before and after game.
    DEF fh1,fh2,nn,n,c,heh

   ->Load palette.
    IF (fh1:=Open(palF,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,palF)
    cm[0]:=Shl(256,16) ; c:=0
    FOR n:=1 TO 128 -> Normal colors.
        FgetC(fh1)
        FOR nn:=1 TO 3
            INC c ; cm[c]:=to32(FgetC(fh1))
        ENDFOR
    ENDFOR
    Seek(fh1,0,OFFSET_BEGINNING)
    FOR n:=1 TO 128 -> Halfbrite colors.
        FgetC(fh1)
        FOR nn:=1 TO 3
            heh:=FgetC(fh1)
            INC c ; cm[c]:=to32(IF ((heh-35)<0) THEN 0 ELSE (heh-35))
        ENDFOR
    ENDFOR

    IF (fh2:=Open(bitF,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,bitF)
    IF (Read(fh2,bitDef,256))=NIL THEN Throw(ERR_IO,bitF)

    preCalc()
    
    openscreen()
    
    ->pens()

    IF gfxmode=NOSUPPORT THEN Raise(ERR_SR)
    backBuf:=GetBufAdr(mainS,1)
    frontBuf:=GetBufAdr(mainS,0)
    bytesperblock:=BLSIZE

   -> Setup gfx/block varables.
    blockX:=pixX-(pixX!*0.20!)/BLSIZE
    blockY:=pixY-(pixY!*0.07!)/BLSIZE
    blockOX:=BLSIZE*blockX
    blockOY:=BLSIZE*blockY
    rightspace:=pixX-blockOX
    lowerspace:=pixY-blockOY
    tinyX:=rightspace-7
    tinyY:=pixY!*0.23!
    blockrow:=blockX*bytesperblock
    textX:=blockOX-3
    textY:=pixY-blockOY-8
    textUX:=3
    textUY:=blockOY+4
    textLX:=textUX+textX
    textLY:=textUY+textY
    tinyUX:=blockOX+4
    tinyUY:=3
    tinyLX:=tinyUX+tinyX-1
    tinyLY:=tinyUY+tinyY-1

   -> Allocate buffers
    blockDB:=New((blockX+1)*(blockY+1)*4)
    shadeDB:=New((blockX+1)*(blockY+1)*4)
    darkDB:= New((blockX+1)*(blockY+1)*4)

    markerBuf:=New(mapSX*mapSY+1)

    loadMap()

   ->Load blocks.
    setupBlocks()
    ->setupUnits()

   ->Draw GUI
    gui()
    drawTiny()
    
/*    MOVE.L bitmapS,A0
    MOVE.L fastBuf,A1

    MOVE.L longsize,D0
    LSR.L  #1,D0
    SUBQ   #1,D0
copyfastbufferbig:
    MOVE.L (A1)+,D1
    MOVE.L (A1)+,D2
    MOVE.L D1,(A0)+
    MOVE.L D2,(A0)+
    
    DBRA.L D0,copyfastbufferbig*/

    defpointer:=loadPointer('Data/Pointer.DAT')
    mappointer:=loadPointer('Data/MapPointer.DAT')

->    Delay(200)

/*    font:=RtgOpenFont(mainS,['topaz.font',8,NIL,NIL]:textattr)
    RtgSetFont(mainS,font)
    RtgSetTextMode(mainS,0,1,0)*/

    handleInput()

/*    RtgCloseFont(mainS,font)   */

EXCEPT DO
    IF fh1 THEN Close(fh1)
    IF fh2 THEN Close(fh2)
    IF mainS THEN UnlockRtgScreen(mainS)
    IF mainS THEN CloseRtgScreen(mainS)
    ReThrow()
ENDPROC
PROC openscreen() HANDLE -> RTG mode.
    ->DEF infotag:PTR TO tagitem,
    DEF smrtag:PTR TO tagitem,
        scrtag:PTR TO tagitem,
        tag:PTR TO tagitem,
        sr,temp
->        sm:PTR TO screenmode,
->        sma:PTR TO LONG

    smrtag:=[smr_MinWidth,       320,
             smr_MinHeight,      200,
             smr_MaxWidth,       800,
             smr_MaxHeight,      600,
             smr_Buffers,          2,
             ->smr_ChunkySupport,    $FFFFFFFF,
             ->smr_PlanarSupport,    $FFFFFFFF,
             smr_ChunkySupport,  $200,
             smr_PlanarSupport,  $800080,
             smr_ProgramUsesC2P, FALSE,
             ->smr_PrefsFileName,  'Dune.prefs',
             NIL]

    scrtag:=[->rtg_PlanarSupport,  -1,
             ->rtg_ChunkySupport,  -1,
             rtg_Buffers,        2,
             NIL]

    infotag:=[grd_BytesPerRow,    0,
              grd_Width,          0,
              grd_Height,         0,
              grd_Depth,          0,
              grd_PixelLayout,    0,
              grd_ColorSpace,     0,
              grd_PlaneSize,      0,
              grd_BusSystem,      0,
              grd_MouseX,         0,
              grd_MouseY,         0,
              NIL]

    IF (sr:=RtgScreenModeReq(smrtag))=NIL THEN Raise(ERR_NO)
    mainS:=OpenRtgScreen(sr,scrtag)
    GetRtgScreenData(mainS,infotag)
    
    tag:=FindTagItem(grd_PixelLayout,infotag)
    WriteF('\nRTG Information:\nScreen pixel layout is ')
    temp:=tag.data
    SELECT temp
        CASE grd_PLANAR;        WriteF('planar\n') ; gfxmode:=NOSUPPORT
        CASE grd_PLANATI;       WriteF('interleaved planar\n') ; gfxmode:=NOSUPPORT
        CASE grd_CHUNKY;        WriteF('8-Bit Z-Ordered (chunky)\n') ; gfxmode:=CHUNKY
        CASE grd_HICOL15;       WriteF('15-Bit Chunky (2 Byte/pixel)\n') ; gfxmode:=NOSUPPORT
        CASE grd_HICOL16;       WriteF('16-Bit Chunky (2 Byte/pixel)\n') ; gfxmode:=NOSUPPORT
        CASE grd_TRUECOL24;     WriteF('24-Bit Chunky (3 Byte/pixel)\n') ; gfxmode:=NOSUPPORT
        CASE grd_TRUECOL24P;    WriteF('24-Bit Chunky (3 Byteplanes/pixel)\n') ; gfxmode:=NOSUPPORT
        CASE grd_TRUECOL32;     WriteF('24-Bit Chunky (4 Bytes/pixel)\n') ; gfxmode:=NOSUPPORT
        CASE grd_GRAFFITI;      WriteF('Graffiti 8 bit\n') ; gfxmode:=NOSUPPORT
        CASE grd_TRUECOL32B;    WriteF('24-Bit Chunky (4 Bytes/pixel)\n') ; gfxmode:=NOSUPPORT
        DEFAULT;                WriteF('unknown \d\n',tag.data) ; gfxmode:=NOSUPPORT
    ENDSELECT

    tag:=FindTagItem(grd_ColorSpace,infotag)
    WriteF('Color space is ')
    temp:=tag.data
    SELECT temp
        CASE grd_Palette;   WriteF('CLUT-Based\n')
        CASE grd_RGB;       WriteF('RGB (low-endian RGB)\n') ; gfxmode:=NOSUPPORT
        CASE grd_BGR;       WriteF('BGR (high-endian RGB)\n') ; gfxmode:=NOSUPPORT
        DEFAULT;            WriteF('unknown \d\n',tag.data) ; gfxmode:=NOSUPPORT
    ENDSELECT
    tag:=FindTagItem(grd_Depth,infotag)
    WriteF('Depth is \d\n',tag.data)

    tag:=FindTagItem(grd_BytesPerRow,infotag)
    WriteF('BytesPerRow is \d\n',tag.data)
    row:=tag.data
    rows:=row*16

    tag:=FindTagItem(grd_Width,infotag)
    WriteF('Width is \d pixels\n',tag.data)
    pixX:=tag.data

    tag:=FindTagItem(grd_Height,infotag)
    WriteF('Height is \d pixels\n',tag.data)
    pixY:=tag.data

    LoadRGBRtg(mainS,cm)
    LockRtgScreen(mainS)
    
EXCEPT DO
    IF sr THEN FreeRtgScreenModeReq(sr)
    ReThrow()
ENDPROC
PROC handleInput() HANDLE ->Handle input, in a plain, non-friendly way. :)
    DEF exit=FALSE,code,class,repeat,c=0,
        imsg:PTR TO intuimessage,
        msg:PTR TO rdcmpdata,
->        string[256]:STRING,
->        string1[256]:STRING,
        tag:PTR TO tagitem

    IF (msg:=RtgInitRDCMP(mainS))=NIL THEN Raise(ERR_MSG)

    REPEAT
        Delay(10)
    WHILE (RtgScreenAtFront(mainS)) AND (exit=FALSE)

        class:=NIL

        IF imsg:=RtgGetMsg(mainS)
            repeat:=FALSE
            class:=imsg.class
            code:=imsg.code
        ENDIF

        IF (gfxmode=PLANAR) OR (gfxmode=IPLANAR)
            GetRtgScreenData(mainS,infotag)
            tag:=FindTagItem(grd_MouseX,infotag)
            mouseX:=tag.data
            tag:=FindTagItem(grd_MouseY,infotag)
            mouseY:=tag.data
        ELSE
            mouseX:=Int(msg.mousex)
            mouseY:=Int(msg.mousey)
        ENDIF

        IF code=MENUDOWN THEN mouseRight()
        IF code=SELECTDOWN THEN mouseLeft()

        IF code=RIGHT THEN INC offX
        IF code=LEFT  THEN DEC offX
        IF code=DOWN  THEN INC offY
        IF code=UP    THEN DEC offY

        IF class=IDCMP_RAWKEY

            IF code=1 THEN blocknr:=0
            IF code=2 THEN blocknr:=1
            IF code=3 THEN blocknr:=2
            IF code=4 THEN blocknr:=3
            IF code=10 THEN saveMap()
        ->    IF code=PLUS THEN high(8)
        ->    IF code=MINUS THEN low(8)
        ->    IF code=REQ1 THEN duneReq('Dune³',['Load Game','Save Game','Options','Continue'],200)
            IF code=ESC THEN exit:=TRUE
            IF code=HELP
                req('Press 1-3 to change block/dark type.\n'+
                    'Press ECS to quit\n\n'+
                    'RMB - Paint block (on both maps).\n'+
                    'LMB - Move around on small map and paint dark on zoomed map.','OK')
            ENDIF
            mouseX:=imsg.mousex
            mouseY:=imsg.mousey
            RtgReplyMsg(mainS,imsg)
        ENDIF

        IF class=IDCMP_MOUSEBUTTONS
            mouseX:=imsg.mousex
            mouseY:=imsg.mousey
            RtgReplyMsg(mainS,imsg)
        ENDIF

        mouseCoords()

        IF mouseX<2 THEN DEC offX
        IF mouseX>(pixX-2) THEN INC offX
        IF mouseY<2 THEN DEC offY
        IF mouseY>(pixY-2) THEN INC offY
       
        IF offX>(mapSX-blockX-1) THEN offX:=mapSX-blockX-1
        IF offY>(mapSY-blockY-1) THEN offY:=mapSY-blockY-1
        IF offX<0 THEN offX:=0
        IF offY<0 THEN offY:=0

        moveMap()

/*        StringF(string,'X=\d',mouseX)
        StringF(string1,'Y=\d',mouseY)
        RtgText(mainS,bitmapS,string,5,100,100)
        RtgText(mainS,bitmapS,string1,5,100,110)*/
/*        placeUnit(50+c,50+c,atreides.tank[1],atreides.tankmask[1])
        placeUnit(mouseX,mouseY,atreides.tank[1],atreides.tankmask[1])

        INC c
        IF c=100 THEN c:=0*/
        switch()
    ENDWHILE
    UNTIL exit

EXCEPT DO
    ReThrow()
ENDPROC
PROC mouseLeft() -> Actions taken on left mouseclick.

    IF mouse=TINYMAP
   ->     unit[0].destx:=mapX
   ->     unit[0].desty:=mapY

    ELSEIF mouse=BLOCKMAP
        IF blocknr=1
            placeDark(mapX,mapY,TRUE)
        ELSEIF blocknr=2
            placeShade(mapX,mapY,TRUE)
        ELSE
            placeDark(mapX,mapY,FALSE)
        ENDIF
    ENDIF
ENDPROC
PROC mouseRight() -> Actions taken right mouseclick.
    col:=block2pen(blocknr)

    IF mouse=BLOCKMAP
        unit[0].destx:=mapX
        unit[0].desty:=mapY

    ->    placeBlock(mapX,mapY,blocknr)
    ELSEIF mouse=TINYMAP
        offX:=mapX-Shr(blockX,1)
        offY:=mapY-Shr(blockY,1)
    ENDIF
ENDPROC
PROC mouseCoords() -> Calculates map/block coord.

    IF (mouseX>(tinyUX-1)) AND (mouseX<(tinyLX+1)) AND (mouseY>(tinyUY-1)) AND (mouseY<(tinyLY+1))
        mapX:=tiny2MapX(mouseX-tinyUX) ; mapY:=tiny2MapY(mouseY-tinyUY)
/*        IF pointer=0
            RtgSetPointer(mainS,mappointer,16,18,-7,-6)
            pointer:=1
        ENDIF*/
        mouse:=TINYMAP
    ELSEIF (mouseX>-1) AND (mouseX<(Shl(blockX,4))) AND (mouseY>-1) AND (mouseY<(Shl(blockY,4)))
        mapX:=Shr(mouseX,4)+offX
        mapY:=Shr(mouseY,4)+offY
/*        IF pointer=1
            RtgSetPointer(mainS,defpointer,16,18,-7,-6)
            pointer:=0
        ENDIF*/
        mouse:=BLOCKMAP
    ELSE
/*        IF pointer=1
            RtgSetPointer(mainS,defpointer,16,18,-7,-6)
            pointer:=0
        ENDIF*/
  ->      mapX:=0
  ->      mapY:=0
        mouse:=GUI
    ENDIF

ENDPROC
PROC switch() -> Doublebuffer. (fastbuffer for AGA)
    RtgWaitTOF(mainS)
ENDPROC
/*PROC loadPal(filename) HANDLE
    DEF fh,n,nn,c

    IF (fh:=Open(filename,MODE_OLDFILE))=NIL THEN Raise(ERR_IO)
    cm[0]:=Shl(cols/3,16) ; c:=0
    FOR n:=1 TO 256
        FgetC(fh)
        FOR nn:=1 TO 3
            INC c ; cm[c]:=to32(FgetC(fh))
        ENDFOR
    ENDFOR
    Close(fh)
EXCEPT DO
    ReThrow()
ENDPROC*/
PROC to32(val) -> 32bit extender for the palette.
    MOVE.L val,D7
    LSL.L #8,D7
    LSL.L #8,D7
    LSL.L #8,D7
    MOVE.L D7,val
ENDPROC val
/*PROC pens() -> Find best pens for future drawing.
    white       :=getPen(255,255,255)
    lightgrey   :=getPen(230,230,230)
    grey        :=getPen(157,157,157)
    darkgrey    :=getPen(80,80,80)
    verydarkgrey:=getPen(50,50,50)
    red         :=getPen(255,0,0)
    green       :=getPen(0,255,0)
    blue        :=getPen(0,0,255)

    dunes       :=getPen(199,156,146)
    rock        :=getPen(102,93,86)
    desert      :=getPen(178,143,122)
ENDPROC*/
PROC gui()
    DEF guiOX,guiOY,pX,pY,t

    guiOX:=blockOX+1
    guiOY:=blockOY+1
    pX:=pixX-1
    pY:=pixY-1

   ->Backfill
   clear(grey)
/*    fill(guiOX,0,rightspace-1,pY,grey)
    fill(0,guiOY,pX,lowerspace-1,grey)
    fill(tinyUX,tinyUY,tinyLX,tinyLY,black)
    fill(textUX,textUY,textLX,textLY,black)*/

   -> Extra shit
    line(blockOX,0,blockOX,blockOY,black)
    line(0,blockOY,blockOX,blockOY,black)
    FOR t:=pX TO pixX-1
        line(t,0,t,pY,black)
    ENDFOR
    FOR t:=textUY TO textLY
        line(textUX,t,textLX,t,black)
    ENDFOR

   -> Primary GUIlines
    line(guiOX,0,pX-1 ,0    ,lightgrey)->   --
    line(guiOX,0,guiOX,guiOY,lightgrey)->   |

    line(0,guiOY,guiOX,guiOY,lightgrey)-> --
    line(0,guiOY,0    ,pY-1 ,lightgrey)-> |

    line(pX   ,1    ,pX,pY   ,darkgrey)->    |
    line(1,pY ,pX   ,pY      ,darkgrey)-> ----

   ->Tinymap and textwindow lines

    line(tinyUX-1,tinyUY-1,tinyLX+0,tinyUY-1,darkgrey) -> --
    line(tinyUX-1,tinyUY-1,tinyUX-1,tinyLY+0,darkgrey) -> |

    line(tinyLX+1,tinyUY-0,tinyLX+1,tinyLY+1,lightgrey)->  |
    line(tinyUX-0,tinyLY+1,tinyLX+1,tinyLY+1,lightgrey)-> --
    
    line(textUX-1,textUY-1,textLX+0,textUY-1,darkgrey) -> --
    line(textUX-1,textUY-1,textUX-1,textLY+0,darkgrey) -> |

    line(textLX+1,textUY-0,textLX+1,textLY+1,lightgrey)->  |
    line(textUX-0,textLY+1,textLX+1,textLY+1,lightgrey)-> --

ENDPROC
PROC loadPointer(filename) HANDLE ->Loads a pointer.
    DEF fh,var

    IF (fh:=Open(filename,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,filename)
    var:=New(BLOCKLEN)
    IF var=NIL THEN Raise(ERR_MEM)
    IF (Read(fh,var,80))=NIL THEN Throw(ERR_IO,filename)

EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC var
/*PROC duneReq(title,stringtags:PTR TO LONG,x)
    DEF w,n,y,cen,c=0,wx,wy

    y:=ListLen(stringtags)-1*17+4+17+8

    wx:=(pixX-x)/2
    wy:=(pixY-y)/2

    IF w:=OpenW(wx,wy,x,y,IDCMP_RAWKEY OR IDCMP_MOUSEBUTTONS,WFLG_ACTIVATE OR WFLG_BORDERLESS OR WFLG_BACKDROP,NIL,mainS,15,0)
        Box(0,0,x,y,grey) ; reqBox(0,0,x,y,lightgrey,shadegrey,TRUE)

        FOR n:=0 TO ListLen(stringtags)-1*16 STEP 15
            reqBox(4,2+14+2+n,x-4,2+14+2+13+n,lightgrey,shadegrey,FALSE)
        ENDFOR

        Colour(white,grey)
        cen:=(x/2)-(StrLen(title)*8/2)
        TextF(cen,12,title)

        Colour(lightgrey,grey)
        FOR n:=0 TO ListLen(stringtags)-1
            cen:=(x/2)-(StrLen(stringtags[n])*8/2)
            TextF(cen,27+c,stringtags[n])
            c:=c+15
        ENDFOR

        WHILE WaitIMessage(w)<>IDCMP_MOUSEBUTTONS
        ENDWHILE
    CloseW(w)
    ELSE

    ENDIF
ENDPROC
PROC reqBox(x1,y1,x2,y2,c1,c2,double)
    Line(x1,y1,x1  ,y2-2,c1)
    Line(x1,y1,x2-2,y1  ,c1)
    IF double
    Line(x1+1,y1+1,x1+1,y2-3,c1)
    Line(x1+1,y1+1,x2-3,y1+1,c1)
    ENDIF

    Line(x1+1,y2-1,x2-1,y2-1,c2)
    Line(x2-1,y1+1,x2-1,y2-1,c2)
    IF double
    Line(x1+2,y2-2,x2-1,y2-2,c2)
    Line(x2-2,y1+2,x2-2,y2-1,c2)
    ENDIF
ENDPROC*/

->Block setup.
PROC setupBlocks() HANDLE -> Loads blocks.

    
    loadBlocks(rockF,1,82)
    loadBlocks(dunesF,101,182)
    loadBlocks(spiceF,201,282)
    loadBlocks(desertF,301,301)
    
    loadDark(darkF,1,82)
    loadShade(shadeF,1,82)

    dark[0]:=0
    shade[0]:=0

EXCEPT DO
    ReThrow()
ENDPROC
PROC loadBlocks(filename,f,t) HANDLE ->Loads a series of 'ground' raw images.
    DEF fh,c

    IF (fh:=Open(filename,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,filename)
    FOR c:=f TO t
        block[c]:=New(BLOCKLEN)
        IF block[c]=NIL THEN Raise(ERR_MEM)
        IF (Read(fh,block[c],BLOCKLEN))=NIL THEN Throw(ERR_IO,filename)
    ENDFOR

EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC
PROC loadDark(filename,f,t) HANDLE ->Loads a series of 'dark' raw images.
    DEF fh,c,ptr

    IF (fh:=Open(filename,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,filename)
    FOR c:=f TO t
        dark[c]:=New(BLOCKLEN)
        IF dark[c]=NIL THEN Raise(ERR_MEM)
        IF (Read(fh,dark[c],BLOCKLEN))=NIL THEN Throw(ERR_IO,filename)
        ptr:=dark[c]
        MOVE.L ptr,A0
        MOVE.L #BLOCKLEN/4-1,D0
   sie1:MOVE.L (A0),D1
        NOT.L  D1
        MOVE.L D1,(A0)+
   DBRA.W D0,sie1
    ENDFOR

EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC
PROC loadShade(filename,f,t) HANDLE ->Loads a series of 'shade' raw images.
    DEF fh,c,ptr

    IF (fh:=Open(filename,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,filename)
    FOR c:=f TO t
        shade[c]:=New(BLOCKLEN)
        IF shade[c]=NIL THEN Raise(ERR_MEM)
        IF (Read(fh,shade[c],BLOCKLEN))=NIL THEN Throw(ERR_IO,filename)
        ptr:=shade[c]
        MOVE.L ptr,A0
        MOVE.L #BLOCKLEN/4-1,D0
   sie2:MOVE.L (A0),D1
        NOT.L  D1
        MOVE.L D1,(A0)+
   DBRA.W D0,sie2
    ENDFOR
        
EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC

->Unit setup.
/*PROC setupUnits()

    loadUnit('Tank.GFX')
ENDPROC
PROC loadUnit(filename) HANDLE
    DEF c,fh=0,str[256]:STRING,str1[256]:STRING,ptr

    StrCopy(str,'Gfx/Units/Planar/',ALL)
    StrAdd(str,filename,ALL)
    StrCopy(str1,'Gfx/Units/Planar/',ALL)
    StrAdd(str1,'TankMask.GFX',ALL)

    IF (fh:=Open(str,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,str)
    
    IF fh THEN Close(fh)

    IF (fh:=Open(str1,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,str1)
    

EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC*/

->Map stuff.
PROC loadMap() HANDLE ->Load map. 16-bit entries.
    DEF x,y,c=NIL,fh

    ->IF fReq('Choose mapfile...','Load','Quit','Data')

    StrCopy(fString,'Data/Map.DAT',ALL)

    IF (fh:=Open(fString,MODE_OLDFILE))=NIL THEN Throw(ERR_IO,fString)

->    mapSX:=FgetC(fh)
->    mapSY:=FgetC(fh)

    NEW map[mapSX*mapSY]

    FOR y:=0 TO mapSY-1
        FOR x:=0 TO mapSX-1
            map[c].imageorg:=FgetC(fh)
            INC c
        ENDFOR
    ENDFOR

    FOR y:=0 TO mapSY-1
        FOR x:=0 TO mapSX-1
            makeBorder(x,y)
->            makeDarkBorder(x,y)
        ENDFOR
    ENDFOR

    ->ELSE
    ->    Throw(ERR_IO,fString)
    ->ENDIF
EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC
PROC saveMap() HANDLE ->Save map. 16-bit entries.
    DEF x,y,c=NIL,fh

    IF fReq('Choose mapfile name','Save','Cancel','Data')

    IF (fh:=Open(fString,MODE_NEWFILE))=NIL THEN Throw(ERR_IO,fString)

    FputC(fh,mapSX)
    FputC(fh,mapSY)

    FOR y:=1 TO mapSY
        FOR x:=1 TO mapSX
            FputC(fh,map[c].imageorg)
            INC c
        ENDFOR
    ENDFOR
    
    ENDIF

EXCEPT DO
    IF fh THEN Close(fh)
    ReThrow()
ENDPROC
PROC moveMap() ->Takes care of 'moving' the map.
    DEF x:REG,y:REG,c=0:REG,stuff:REG

    FOR y:=offY TO offY+blockY-1
        stuff:=yP[y]
        FOR x:=offX TO offX+blockX-1
            blockDB[c]:=block[map[stuff+x].image]
            darkDB[c]:=dark[map[stuff+x].dark]
            shadeDB[c]:=shade[map[stuff+x].shade]
            INC c
        ENDFOR
    ENDFOR

    copyBlocks()
ENDPROC
PROC makeBorder(x,y) -> Calculates border piece.
    DEF val,xx1,xx2,yy1,yy2

    IF (x>-1) AND (x<(mapSX+1)) AND (y>-1) AND (y<(mapSY+1))
    IF map[yP[y]+x].imageorg=ROCK

    xx1:=x+1 ; xx2:=x-1 ; yy1:=y+1 ; yy2:=y-1

    IF (xx1>(mapSX)) THEN xx1:=mapSX
    IF (xx2<0)       THEN xx2:=0
    IF (yy1>(mapSY)) THEN yy1:=mapSY
    IF (yy2<0)       THEN yy2:=0
    
    val:= IF map[yP[yy2]+x].imageorg=ROCK THEN 1 ELSE 0

    IF map[yP[yy2]+xx1].imageorg=ROCK THEN val:=Or(val,Shl(1,1)) ELSE val:=Or(val,Shl(0,1))
    IF map[yP[y]+xx1].imageorg=ROCK   THEN val:=Or(val,Shl(1,2)) ELSE val:=Or(val,Shl(0,2))      
    IF map[yP[yy1]+xx1].imageorg=ROCK THEN val:=Or(val,Shl(1,3)) ELSE val:=Or(val,Shl(0,3))      
    IF map[yP[yy1]+x].imageorg=ROCK   THEN val:=Or(val,Shl(1,4)) ELSE val:=Or(val,Shl(0,4))
    IF map[yP[yy1]+xx2].imageorg=ROCK THEN val:=Or(val,Shl(1,5)) ELSE val:=Or(val,Shl(0,5))
    IF map[yP[y]+xx2].imageorg=ROCK   THEN val:=Or(val,Shl(1,6)) ELSE val:=Or(val,Shl(0,6))
    IF map[yP[yy2]+xx2].imageorg=ROCK THEN val:=Or(val,Shl(1,7)) ELSE val:=Or(val,Shl(0,7))

    map[yP[y]+x].image:=bitDef[val]

    ELSEIF map[yP[y]+x].imageorg=DUNE

    xx1:=x+1 ; xx2:=x-1 ; yy1:=y+1 ; yy2:=y-1

    IF (xx1>(mapSX)) THEN xx1:=mapSX
    IF (xx2<0)       THEN xx2:=0
    IF (yy1>(mapSY)) THEN yy1:=mapSY
    IF (yy2<0)       THEN yy2:=0

    val:= IF map[yP[yy2]+x].imageorg=DUNE THEN 1 ELSE 0

    IF map[yP[yy2]+xx1].imageorg=DUNE THEN val:=Or(val,Shl(1,1)) ELSE val:=Or(val,Shl(0,1))
    IF map[yP[y]+xx1].imageorg=DUNE   THEN val:=Or(val,Shl(1,2)) ELSE val:=Or(val,Shl(0,2))
    IF map[yP[yy1]+xx1].imageorg=DUNE THEN val:=Or(val,Shl(1,3)) ELSE val:=Or(val,Shl(0,3))
    IF map[yP[yy1]+x].imageorg=DUNE   THEN val:=Or(val,Shl(1,4)) ELSE val:=Or(val,Shl(0,4))
    IF map[yP[yy1]+xx2].imageorg=DUNE THEN val:=Or(val,Shl(1,5)) ELSE val:=Or(val,Shl(0,5))
    IF map[yP[y]+xx2].imageorg=DUNE   THEN val:=Or(val,Shl(1,6)) ELSE val:=Or(val,Shl(0,6))
    IF map[yP[yy2]+xx2].imageorg=DUNE THEN val:=Or(val,Shl(1,7)) ELSE val:=Or(val,Shl(0,7))

    map[yP[y]+x].image:=bitDef[val]+100

    ELSEIF map[yP[y]+x].imageorg=SPICE

    xx1:=x+1 ; xx2:=x-1 ; yy1:=y+1 ; yy2:=y-1

    IF (xx1>(mapSX)) THEN xx1:=mapSX
    IF (xx2<0)       THEN xx2:=0
    IF (yy1>(mapSY)) THEN yy1:=mapSY
    IF (yy2<0)       THEN yy2:=0

    val:= IF map[yP[yy2]+x].imageorg=SPICE THEN 1 ELSE 0

    IF map[yP[yy2]+xx1].imageorg=SPICE THEN val:=Or(val,Shl(1,1)) ELSE val:=Or(val,Shl(0,1))
    IF map[yP[y]+xx1].imageorg=SPICE   THEN val:=Or(val,Shl(1,2)) ELSE val:=Or(val,Shl(0,2))
    IF map[yP[yy1]+xx1].imageorg=SPICE THEN val:=Or(val,Shl(1,3)) ELSE val:=Or(val,Shl(0,3))
    IF map[yP[yy1]+x].imageorg=SPICE   THEN val:=Or(val,Shl(1,4)) ELSE val:=Or(val,Shl(0,4))
    IF map[yP[yy1]+xx2].imageorg=SPICE THEN val:=Or(val,Shl(1,5)) ELSE val:=Or(val,Shl(0,5))
    IF map[yP[y]+xx2].imageorg=SPICE   THEN val:=Or(val,Shl(1,6)) ELSE val:=Or(val,Shl(0,6))
    IF map[yP[yy2]+xx2].imageorg=SPICE THEN val:=Or(val,Shl(1,7)) ELSE val:=Or(val,Shl(0,7))

    map[yP[y]+x].image:=bitDef[val]+200

    ELSEIF map[yP[y]+x].imageorg=DESERT
    map[yP[y]+x].image:=301

    ENDIF
    ENDIF
ENDPROC
PROC makeShadeBorder(x,y) -> Calculates shade-border piece.
    DEF val,xx1,xx2,yy1,yy2

    IF (x>-1) AND (x<(mapSX+1)) AND (y>-1) AND (y<(mapSY+1))
    IF map[yP[y]+x].orgshade=SHADED

    xx1:=x+1 ; xx2:=x-1 ; yy1:=y+1 ; yy2:=y-1

    IF (xx1>(mapSX)) THEN xx1:=mapSX
    IF (xx2<0)       THEN xx2:=0
    IF (yy1>(mapSY)) THEN yy1:=mapSY
    IF (yy2<0)       THEN yy2:=0

    val:= IF map[yP[yy2]+x].orgshade=SHADED THEN 1 ELSE 0

    IF map[yP[yy2]+xx1].orgshade=SHADED THEN val:=Or(val,Shl(1,1)) ELSE val:=Or(val,Shl(0,1))
    IF map[yP[y]+xx1].orgshade=SHADED   THEN val:=Or(val,Shl(1,2)) ELSE val:=Or(val,Shl(0,2))
    IF map[yP[yy1]+xx1].orgshade=SHADED THEN val:=Or(val,Shl(1,3)) ELSE val:=Or(val,Shl(0,3))
    IF map[yP[yy1]+x].orgshade=SHADED   THEN val:=Or(val,Shl(1,4)) ELSE val:=Or(val,Shl(0,4))
    IF map[yP[yy1]+xx2].orgshade=SHADED THEN val:=Or(val,Shl(1,5)) ELSE val:=Or(val,Shl(0,5))
    IF map[yP[y]+xx2].orgshade=SHADED   THEN val:=Or(val,Shl(1,6)) ELSE val:=Or(val,Shl(0,6))
    IF map[yP[yy2]+xx2].orgshade=SHADED THEN val:=Or(val,Shl(1,7)) ELSE val:=Or(val,Shl(0,7))
    
    map[yP[y]+x].shade:=bitDef[val]
    ELSE
    map[yP[y]+x].shade:=VISIBLE
    ENDIF
    ENDIF
ENDPROC
PROC makeDarkBorder(x,y) -> Calculates dark-border piece.
    DEF val,xx1,xx2,yy1,yy2

    IF (x>-1) AND (x<(mapSX+1)) AND (y>-1) AND (y<(mapSY+1))
    IF map[yP[y]+x].orgdark=DARK

    xx1:=x+1 ; xx2:=x-1 ; yy1:=y+1 ; yy2:=y-1

    IF (xx1>(mapSX)) THEN xx1:=mapSX
    IF (xx2<0)       THEN xx2:=0
    IF (yy1>(mapSY)) THEN yy1:=mapSY
    IF (yy2<0)       THEN yy2:=0

    val:=IF map[yP[yy2]+x].orgdark=DARK THEN 1 ELSE 0

    IF map[yP[yy2]+xx1].orgdark=DARK THEN val:=Or(val,Shl(1,1)) ELSE val:=Or(val,Shl(0,1))
    IF map[yP[y]+xx1].orgdark=DARK   THEN val:=Or(val,Shl(1,2)) ELSE val:=Or(val,Shl(0,2))
    IF map[yP[yy1]+xx1].orgdark=DARK THEN val:=Or(val,Shl(1,3)) ELSE val:=Or(val,Shl(0,3))
    IF map[yP[yy1]+x].orgdark=DARK   THEN val:=Or(val,Shl(1,4)) ELSE val:=Or(val,Shl(0,4))
    IF map[yP[yy1]+xx2].orgdark=DARK THEN val:=Or(val,Shl(1,5)) ELSE val:=Or(val,Shl(0,5))
    IF map[yP[y]+xx2].orgdark=DARK   THEN val:=Or(val,Shl(1,6)) ELSE val:=Or(val,Shl(0,6))
    IF map[yP[yy2]+xx2].orgdark=DARK THEN val:=Or(val,Shl(1,7)) ELSE val:=Or(val,Shl(0,7))

    map[yP[y]+x].dark:=bitDef[val]

    ELSE
    map[yP[y]+x].dark:=VISIBLE
    ENDIF
    ENDIF
ENDPROC
PROC drawTiny() ->Draws tiny global map.
    DEF x:REG,y:REG,ox=0,oy=0,temp,oldcol,sx,sy

    FOR y:=0 TO mapSY-1
        
        FOR x:=0 TO mapSX-1
            sx:=tinyUX+map2TinyX(x)
            sy:=tinyUY+map2TinyY(y)

            col:=block2pen(map[yP[y]+x].imageorg)
            plot(sx,sy,col)

            ox:=map2TinyX(x)-ox
            IF (ox>1)
                FOR temp:=2 TO ox
                    plot(sx-(temp-1),sy,oldcol)
                ENDFOR
            ENDIF

            IF ((map2TinyY(y)-oy)>1)
                FOR temp:=2 TO map2TinyY(y)-oy
                    plot(sx,sy-(temp-1),col)
                ENDFOR
            ENDIF

            ox:=map2TinyX(x)
            oldcol:=col
        ENDFOR
        oy:=map2TinyY(y)
    ENDFOR
    
ENDPROC
PROC map2TinyX(nr)
ENDPROC Shr(Shl(nr,8)/mapSX*tinyX,8)
PROC map2TinyY(nr)
ENDPROC Shr(Shl(nr,8)/mapSY*tinyY,8)
PROC tiny2MapX(nr)
ENDPROC Shr(Shl(nr,8)/tinyX*mapSX,8)
PROC tiny2MapY(nr)
ENDPROC Shr(Shl(nr,8)/tinyY*mapSY,8)

->Single block operations.
PROC placeBlock(x,y,nr) -> Changes the block at the given (map)coordinates.

    map[yP[y]+x].imageorg:=nr
    makeBorder(x-1,y-1)
    makeBorder(x-1,y)
    makeBorder(x-1,y+1)
    makeBorder(x,y-1)
    makeBorder(x,y)
    makeBorder(x,y+1)
    makeBorder(x+1,y-1)
    makeBorder(x+1,y)
    makeBorder(x+1,y+1)

    plot(tinyUX+map2TinyX(x),tinyUY+map2TinyY(y),block2pen(nr))
ENDPROC
PROC placeDark(x,y,nr) -> Changes the dark at the given (map)coordinates.

    IF nr=TRUE
        nr:=DARK
        col:=black
        placeShade(x,y,TRUE)
    ELSE
        nr:=VISIBLE
        col:=block2pen(map[yP[y]+x].imageorg)
        placeShade(x,y,FALSE)
    ENDIF

    map[yP[y]+x].orgdark:=nr
    makeDarkBorder(x-1,y-1)
    makeDarkBorder(x-1,y)
    makeDarkBorder(x-1,y+1)
    makeDarkBorder(x,y-1)
    makeDarkBorder(x,y)
    makeDarkBorder(x,y+1)
    makeDarkBorder(x+1,y-1)
    makeDarkBorder(x+1,y)
    makeDarkBorder(x+1,y+1)

    plot(tinyUX+map2TinyX(x),tinyUY+map2TinyY(y),col)
ENDPROC
PROC placeShade(x,y,val) -> Changes the shade at the given (map)coordinates.
    DEF nr

    IF val=TRUE THEN nr:=SHADED ELSE nr:=VISIBLE
    map[yP[y]+x].orgshade:=nr
    makeShadeBorder(x-1,y-1)
    makeShadeBorder(x-1,y)
    makeShadeBorder(x-1,y+1)
    makeShadeBorder(x,y-1)
    makeShadeBorder(x,y)
    makeShadeBorder(x,y+1)
    makeShadeBorder(x+1,y-1)
    makeShadeBorder(x+1,y)
    makeShadeBorder(x+1,y+1)

    plotShade(tinyUX+map2TinyX(x),tinyUY+map2TinyY(y),val)
ENDPROC
PROC block2pen(block) -> Returns pixelcolor for specified block.
    DEF ret

    IF block=DESERT THEN ret:=desert
    IF block=ROCK   THEN ret:=rock
    IF block=DUNE   THEN ret:=dunes
    IF block=SPICE  THEN ret:=spice

ENDPROC ret
/*PROC square(x,y,pen,size) -> Paints a 'box' over the specified blockcoord.
    IF ((y>(offY-1)) AND (y<(offY+blockY))) AND ((x>(offX-1)) AND (x<(offX+blockX)))
        line(x-offX*BLSIZE                ,y-offY*BLSIZE,
             x-offX*BLSIZE+(BLSIZE*size)-1,y-offY*BLSIZE,pen,FALSE)
        line(x-offX*BLSIZE,y-offY*BLSIZE,
             x-offX*BLSIZE,y-offY*BLSIZE+(BLSIZE*size)-1,pen,FALSE)
        line(x-offX*BLSIZE+(BLSIZE*size)-1,y-offY*BLSIZE,
             x-offX*BLSIZE+(BLSIZE*size)-1,y-offY*BLSIZE+(BLSIZE*size)-1,pen,FALSE)
        line(x-offX*BLSIZE                ,y-offY*BLSIZE+(BLSIZE*size)-1,
             x-offX*BLSIZE+(BLSIZE*size)-1,y-offY*BLSIZE+(BLSIZE*size)-1,pen,FALSE)
        line(x-offX*BLSIZE                ,y-offY*BLSIZE,
             x-offX*BLSIZE+(BLSIZE*size)-1,y-offY*BLSIZE+(BLSIZE*size)-1,pen,FALSE)
        line(x-offX*BLSIZE+(BLSIZE*size)-1,y-offY*BLSIZE,
             x-offX*BLSIZE                ,y-offY*BLSIZE+(BLSIZE*size)-1,pen,FALSE)
    ENDIF
ENDPROC*/

CHAR '$VER: Dune III Beta 0.2 (15.6.99)'

