->*** Gfx Plug-In Module for Dune III.

->History.
->DuneGfxAGA 1.0 - Initial.
->DuneGfxAGA 1.1 - Next? :)
->DuneGfx    2.0 - Retargable.
->DuneGfx    2.1 - Graphicsboards only.

OPT MODULE
OPT EXPORT
OPT PREPROCESS

->PROC Modules
MODULE 'dos/dos',
       'intuition/intuition',
       'intuition/screens',
       'exec/execbase',
       'exec/ports',
       'exec/memory',
       'exec/ports',
       'graphics/gfx',
       'hardware/custom',
       'graphics/rastport',
       'graphics/gfx',
       'graphics/gfxbase',
       'graphics/modeid',
       'graphics/sprite',
       'graphics/rastport',
       'graphics/view',
       'graphics/videocontrol',
       'rtgmaster','rtgmaster/rtgmaster',
       '*DuneDevs'
->ENDPROC

->PROC Defines (Globvars)

    DEF rtgmasterbase
    DEF row,rows
    DEF blockrow
    DEF blockX,blockY
    DEF pixX,pixY
    DEF blockDB:PTR TO LONG,shadeDB:PTR TO LONG,darkDB:PTR TO LONG
    DEF frontBuf
    DEF mainS:PTR TO LONG
->ENDPROC

-> Main gfx routines. (planar/chunky)
PROC clear(col)
    FillRtgRect(mainS,frontBuf,col,0,0,pixX,pixY)   
ENDPROC
PROC line(x1,y1,x2,y2,col) -> Line.
    DrawRtgLine(mainS,frontBuf,col,x1,y1,x2,y2)
ENDPROC
PROC plot(x,y,col) -> Plot.
    WriteRtgPixel(mainS,frontBuf,x,y,col)
ENDPROC
PROC fill(x,y,w,h,col)
    FillRtgRect(mainS,frontBuf,col,x,y,w,h)
ENDPROC
/*PROC text(x,y,string,both) -> Text. (N/A)
    TextF(x,y,string)
    IF both

    ENDIF
ENDPROC*/
PROC plotShade(x,y,val) -> Plot halfbrite. (both planar/chunky)

    /*MOVE.L frontBuf,A0
    MOVE.L bitmapS,A1

    MOVE.L x,D0
    MOVE.L y,D1
    MULU.W row.W,D1
    ADD.L  D0,D1      -> X+Y offset

    MOVEQ.L #0,D0

    ADD.L  D1,A0
    ADD.L  D1,A1

    MOVE.B (A0),D0
    ADD.B  #128,D0
    MOVE.B D0,(A0)

    MOVE.B (A1),D0
    ADD.B  #128,D0
    MOVE.B D0,(A1)*/

ENDPROC
PROC placeMarker() -> Draw marker

ENDPROC

-> Block gfx routines. (asm)
PROC copyBlocks() ->Draws blocks.
    DEF blockptr,darkptr,shadeptr

    MOVE.L  frontBuf,A0       ->Bitmap in A0
    MOVE.L  blockDB,blockptr ->Imagepointerarray in var
    MOVE.L  darkDB,darkptr   ->Darkointerarray in var
    MOVE.L  shadeDB,shadeptr ->Shadepointerarray in var
    MOVEQ   #0,D7            ->Init offset to 0.

    MOVE.L  blockY,D2        ->Number of blocks in Y
    SUBQ.L  #1,D2
cblocky:
    MOVE.L  blockX,D1        ->Number of blocks in X
    SUBQ.L  #1,D1
cblockx:
    ADD.L   D7,A0            ->Add offset to bitmap.
    MOVE.L  blockptr,A1      ->Next block in A1
    MOVE.L  (A1),A1
    ADD.L   #4,blockptr
    MOVE.L  darkptr,A2       ->Next dark in A2
    MOVE.L  (A2),A2
    ADD.L   #4,darkptr
    MOVE.L  shadeptr,A3      ->Next shade in A3
    MOVE.L  (A3),A3
    ADD.L   #4,shadeptr

    CMPA.L  #0,A3            ->If no shade then do blocks
    BEQ.W   cblock           

    MOVE.L  #BLSIZE-1,D0     ->Number of lines.
csdcounter:
    MOVE.L  (A1)+,D3         ->Copy 16 pixels from block.
    MOVE.L  (A1)+,D4
    MOVE.L  (A1)+,D5
    MOVE.L  (A1)+,D6
    CMPA.L  #0,A2
    BEQ.W  csdshade
    AND.L  (A2)+,D3          ->Add 16 pixels from dark.
    AND.L  (A2)+,D4
    AND.L  (A2)+,D5
    AND.L  (A2)+,D6
 csdshade:
    AND.L  (A3)+,D3          ->Add 16 pixels from shade.
    AND.L  (A3)+,D4
    AND.L  (A3)+,D5
    AND.L  (A3)+,D6
    MOVE.L  D3,(A0)+         ->Copy 16 pixels to bitmap
    MOVE.L  D4,(A0)+
    MOVE.L  D5,(A0)+
    MOVE.L  D6,(A0)
    SUB.L   #12,A0           ->Back to start. (12 pixels.)
    ADD.L   row,A0           ->Add line.
 DBRA.W     D0,csdcounter
    BRA.W   nextblock

cblock:
    MOVE.L  #BLSIZE-1,D0     ->Number of lines.
cblockcounter:
    MOVE.L  (A1)+,D3         ->Copy 16 pixels from block.
    MOVE.L  (A1)+,D4
    MOVE.L  (A1)+,D5
    MOVE.L  (A1)+,D6
    MOVE.L  D3,(A0)+         ->Copy 16 pixels to bitmap
    MOVE.L  D4,(A0)+         
    MOVE.L  D5,(A0)+         
    MOVE.L  D6,(A0)          
    SUB.L   #12,A0           ->Back to start. (12 pixels.)
    ADD.L   row,A0           ->Add line.
 DBRA.W     D0,cblockcounter

nextblock:
    MOVE.L  frontBuf,A0       ->Back to start of bitmap.
    ADD.L   #BLSIZE,D7       ->Add block to offset.
 DBRA.W     D1,cblockx       ->Next block.
 
    SUB.L   blockrow,D7      ->Jump back to where we started.
    ADD.L   rows,D7          ->Add 16 lines to offset.
 DBRA.W     D2,cblocky       ->Next row.

ENDPROC

CHAR '[DuneGfx Plug-In] 2.1 (15.6.99)'

