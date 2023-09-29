package singlecycle
import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class datapathtester extends FreeSpec with ChiselScalatestTester{
    " dataPath testing" in {
        test(new datapath){a=>
        
        a.clock.step(150)
         //a.io.out1.expect(1.U)
        //a.io.out2(0).expect((1.B))
        // a.io.out2(1).expect((1.B))
        // a.io.out2(2).expect((1.B))
        // a.io.out2(3).expect((1.B))
         }
         }}