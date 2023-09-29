package singlecycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class imemtester extends FreeSpec with ChiselScalatestTester{
    " imem tester " in {
        test(new imem("C:/Users/Indus/OneDrive/Desktop/Scala-Chisel-Learning-Journey-main/src/main/scala/gcd/singlecycle/imem.txt")){a=>
        a.io.address.poke(0.U)

        a.clock.step(1)
        a.io.data_out.expect(1.U)
         }
         }}