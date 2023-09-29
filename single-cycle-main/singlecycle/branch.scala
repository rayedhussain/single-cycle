package singlecycle
import chisel3 . _
import chisel3 . util . _
class branchinputs extends Bundle {
    val aluop = Input ( UInt (3. W ) )
    val branch = Input ( Bool () )
    val inpA = Input ( UInt (32. W ) )
    val inpB = Input ( UInt (32. W ) )
    val br_taken = Output ( Bool() )
}
class branch extends Module {
        val io = IO (new branchinputs )
   
       
        val out=Wire(Bool())  
        out:= 0.B
        io.br_taken := 0.B
            
        when(io.branch)
        {
            switch(io.aluop){
                is("b000".U){
                    out:=io.inpA.asSInt === io.inpB.asSInt        
                    }      
                is("b001".U){
                    out:=io.inpA.asSInt =/= io.inpB.asSInt      
                }
                is("b100".U){
                    out:=io.inpA.asSInt < io.inpB.asSInt       
                }
                is("b101".U){
                    out:=io.inpA.asSInt >= io.inpB.asSInt     
                }
                is("b110".U){
                    out:=io.inpA < io.inpB       
                }
                is("b111".U){
                    out:=io.inpA >= io.inpB}     
            
            }
        }
        io.br_taken := out 
                }
