package singlecycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class regandmemtester extends FreeSpec with ChiselScalatestTester{
    " register file " in {
        test(new registerfile){a=>
        a.io.wen.poke(true.B)
        a.io.rs1.poke(2.U)
        a.io.rs2.poke(1.U)
        a.io.rd.poke(5.U)
        a.io.data_in.poke(8.U)
        
        
        a.clock.step(2)
        
        a.io.outdata1.expect(0.U)
        a.io.outdata2.expect(0.U)

        }
    }
}
