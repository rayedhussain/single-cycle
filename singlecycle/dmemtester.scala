package singlecycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class dmemtester extends FreeSpec with ChiselScalatestTester{
    " dmemtesting " in {
        test(new datamem){a=>
        a.io.Addr.poke(311.U)
       
        a.io.storetype.poke("b010".U)
        a.io.MemWrite.poke(true.B)
        //a.io.MemRead .poke(true.B)

        a.clock.step(3)
        a.io.data_out(0).expect((1.U))  
         }
         }}