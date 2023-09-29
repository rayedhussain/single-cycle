package singlecycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class pctester extends FreeSpec with ChiselScalatestTester{
    " pctester " in {
        test(new programcounter){a=>
        //a.io.reset.poke(false.B)
        a.clock.step(12)
         a.io.pc4.expect(2.S)
        }}}