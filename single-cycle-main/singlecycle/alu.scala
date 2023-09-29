package singlecycle
import chisel3._
import chisel3.util._

class ALUIO extends Bundle {
    val in_A = Input ( UInt (32 .W ) )
    val in_B = Input ( UInt ( 32 . W ) )
    val aluop = Input ( UInt ( 4 . W ) )
    val out = Output ( UInt ( 32 . W ) )
}
class alu extends Module  {
    val io = IO (new ALUIO )
    io.out:=0.U
   
    switch (io.aluop){
        is("b0000".U){                      //addi  add
            io.out:=io.in_A + io.in_B}
        
        is( "b0001".U){                       //sub
            io.out:=io.in_A - io.in_B}
        
        is( "b1000".U){                    // xori xor
            io.out :=io.in_A ^ io.in_B}
        
        is("b1100".U ){                    //or ori
            io.out:=io.in_A|io.in_B}
        
        is("b1110".U ){                   //and andi
            io.out:=io.in_A & io.in_B}
        
        is("b0010".U ){                   //sll slli
            io.out:=io.in_A << io.in_B(4,0) }
        
        is("b1010".U){
            io.out:=io.in_A >> io.in_B(4,0)}   //srl srli 
        
         is("b1011".U){
            io.out:=io.in_A.asUInt >>io.in_B(4,0) }   // sra srai
        
        is("b0100".U){

            when(io.in_A < io.in_B){ //slt  
                io.out:=1.U}}
            
        is("b0110".U){
                
            when(io.in_A < io.in_B){      //sltu 
                io.out:=1.U
            }}

        
             }
         
    }
        
    
