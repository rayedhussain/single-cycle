package singlecycle
import chisel3._
import chisel3.util._

class registerfile extends Module{
    val io = IO(new Bundle{
    val rs1 = Input ( UInt (5. W ) )
    val rs2 = Input ( UInt (5. W ) )
    val outdata1 = Output ( UInt ( 32. W ) )
    val outdata2 = Output ( UInt ( 32. W ) )
    val wen = Input ( Bool () )
    val rd = Input ( UInt (5. W ) )
    val data_in = Input ( UInt ( 32 . W ) )

    })
   
        val regfile=RegInit(VecInit(Seq.fill(32)(0.U(32.W))))

        io.outdata1:=regfile(io.rs1)
        io.outdata2:=regfile(io.rs2)

            when ( io.wen === 1.B & io.rd.orR ) {
            regfile ( io.rd ) := io.data_in
            dontTouch (io.wen)
            }
}
        