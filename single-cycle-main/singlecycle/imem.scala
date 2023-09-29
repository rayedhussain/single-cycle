package singlecycle
import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile
import scala.io.Source

 class imembundle extends Bundle {
    val data_out = Output (UInt (32. W ))
    val address = Input (UInt (8. W ))

    
    }
 class imem(initFIle :String)  extends Module {

        val io = IO ( new imembundle )
        val memory = Mem (256 , UInt (32. W ))
        
        loadMemoryFromFile(memory , initFIle)
         val wren = true.B
      //   when (wren) {
      //   memory . write ( io . address , io . data_in  )
         
        io . data_out := memory.read (io.address/4.U)
        }