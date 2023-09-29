package singlecycle
import chisel3._
import chisel3.util._

class controlunit extends Module{
    val io = IO(new Bundle{

    val ins = Input(UInt(32.W))

    val rs1 =Output(UInt(5.W))
    val rs2 =Output(UInt(5.W))
    val rd =Output(UInt(5.W))
    val Imm = Output(UInt(32.W))
    val aluop =Output(UInt(4.W))
    val branch_alu=Output(UInt(3.W))
    val func3=Output(UInt(3.W))
    val storeimm =Output(SInt(32.W))
    val auipc_imm =Output(SInt(32.W))
    val luipc_imm =Output(UInt(32.W))
    val jal_imm =Output(SInt(32.W))
    val jalr_imm =Output(SInt(32.W))
    val sb_imm =Output(SInt(32.W))
   
    val branchout=Output(Bool())
    val jalr_out=Output(Bool())
    val auipc_out=Output(Bool())
    val jalout=Output(Bool())
    val RegWrite = Output(Bool())
    val MemWrite = Output(Bool())
    val MemRead = Output(Bool())
    })
    val Opcode = io.ins(6,0)
    io.func3:=io.ins(14,12)
     dontTouch (io.luipc_imm)
   

    io.aluop:=0.U
    
    io.sb_imm:=0.S
    io.auipc_imm:=0.S
    io.luipc_imm:=0.U
    io.jalr_imm:=0.S
    io.branch_alu:=0.U
    io.jalr_out:=0.U
    io.auipc_out:=0.U
   //  io.luipc_out:=0.U
    io.jalout:=0.U
    io.branchout:=0.U
    io.storeimm:=0.S
    io.func3:=0.U
    io.RegWrite := false.B
    io.MemRead:=false.B
    io.MemWrite := false.B
    io.Imm :=0.U
    io.rd:= io.ins(11,7)
    io.rs1 := io.ins(19,15)
    io.rs2 := io.ins(24,20)
   
    when(Opcode === "b1101111".U){ // jal ka kaam
     io.jal_imm := Cat(Fill(11,io.ins(31)),io.ins(19,12),io.ins(20),io.ins(30,21), 0.U).asSInt
     io.jalout := 1.B
       io.aluop:="b0000".U
    }.otherwise{
       io.jal_imm:=0.S
    }

      when(Opcode === "b0110011".U){  //Rtype
      io.aluop := Cat(io.ins(14,12),io.ins(29))
      io.RegWrite := true.B
      io.MemWrite := false.B
      
      }

      .elsewhen(Opcode === "b0010011".U){  // Itype

         io.aluop := Cat(io.ins(14,12),0.U)
         when(io.ins(14,12) === "b101".U){
            io.aluop := Cat(io.ins(14,12),io.ins(30))
         }.otherwise{
            io.aluop := io.ins(14,12)
         }

         io.RegWrite := true.B
         io.MemWrite := false.B
         val immidiate=Cat(Fill(20,io.ins(31)),io.ins(31,20))
         io.Imm := immidiate
       
      }
                   
         .elsewhen(Opcode === "b1100011". U ) {      //SB type
          io.sb_imm:= Cat(Cat(Fill(19, io.ins(31)),(Cat(io.ins(31),io.ins(7)))),(Cat(io.ins(30,25),(Cat(io.ins(11,8),0.U))))).asSInt()
          io.branchout:=1.B
          io.RegWrite := false.B
         //   io.aluop:="b0000".U

         }
         
         //   .elsewhen(Opcode ==="b1101111". U ) {      //UJ type jal

         //    //   io.jal_imm:=(Cat(Fill(11,io.ins(31)),io.ins(31),io.ins(19,12),io.ins(20),io.ins(30,21), "b0".U ))//uj type
         //      io.jalout:=1.B
         //    //   io.aluop:="b0000".U
         //   }

           .elsewhen(Opcode ==="b1100111". U ) {       //i type jalr

            io.jalr_imm:=(Cat(Fill(20,io.ins(31)),io.ins(31,20))).asSInt
            io.jalr_out:=1.B
            io.aluop:="b0000".U
           }

           .elsewhen(Opcode ==="b0010111". U ) {       //U type auipc
        
              io.auipc_imm:=Cat(io.ins(31,12),(Fill(12,0.U))).asSInt////u type x
              io.auipc_out:=1.B
              
           }
           
              .elsewhen(Opcode ==="b0110111". U ) {       //U type lui
        
                io.luipc_imm := Cat(io.ins(31,12),(Fill(12,0.U)))//u type
            }
       

          .elsewhen(Opcode === "b0100011".U){         // S type
           io.storeimm:=(Cat(Fill(20, io.ins(31)), Cat(io.ins(31,25),io.rd))).asSInt
           io.MemWrite:=true.B
           io.MemRead:=false.B
           io.aluop :="b0000".U    
              }


        .elsewhen(Opcode === "b0000011".U){     // load type
        io.MemWrite:=false.B
        io.MemRead:=true.B
        io.RegWrite := true.B
        val immidiate=Cat(Fill(20,io.ins(31)),io.ins(31,20))
         io.Imm := immidiate
      }
        
            }