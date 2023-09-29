package singlecycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class alutester extends FreeSpec with ChiselScalatestTester{
    " alutester " in {
        test(new alu){a=>
        a.io.in_A.poke(5.U)
        a.io.in_B.poke(6.U)
        a.io.aluop.poke("b000".U)
        a.clock.step(5)
        a.io.out.expect(11.U)
        }}}