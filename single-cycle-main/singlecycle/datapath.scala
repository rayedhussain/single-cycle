package singlecycle
import chisel3._
import chisel3.util._

class datapath extends Module{
    val io = IO(new Bundle{
    
    val out1 =Output(UInt(32.W))
    // val out2 =Output(UInt(32.W))
    val Instype =Input(Bool())
    })
         val pc=Module(new programcounter )
        val memory=Module(new imem("/home/rayedhussain/Desktop/Scala-Chisel-Learning-Journey/src/main/scala/gcd/single-cycle-main/singlecycle/imem.txt"))
        val aluu=Module(new alu)
        val file=Module(new registerfile)
        val cu=Module(new controlunit )
        val dmem=Module(new datamem)
        dontTouch (dmem.io)
        dontTouch (file.io.data_in)
        
        val sab=Module(new branch)
         dontTouch (sab.io)
        val opcode=memory.io.data_out(6,0)
        file.io.data_in:=0.U
        memory.io.address:=pc.io.pc4.asUInt //pc pc_inp assigned to imem
        io.out1 := 0.U
        dmem.io.storetype:=0.U
        
       
        aluu.io.aluop:=cu.io.aluop
        
        cu.io.ins := memory.io.data_out //imem output assigned to cu 

        pc.io.immidiate_enable := cu.io.jalout
        pc.io.pc_inp:=pc.io.pc4       

        file.io.rs1 := cu.io.rs1  //register assigned in registerfile
           
        when(cu.io.ins(6,0) ==="b0100011".U || cu.io.ins(6,0) ==="b1100011".U){  // opcode of sb and store
          file.io.wen:=0.B  
        }.otherwise{
            file.io.wen:=1.B  
        }
        
        file.io.rs1 := cu.io.rs1  //register assigned in registerfile 
        file.io.rs2:=cu.io.rs2
        file.io.rd := cu.io.rd

        aluu.io.in_A:=file.io.outdata1   
        aluu.io.in_B:=file.io.outdata2   

        when(cu.io.ins(6,0) === "b0010011".U)
        {
          aluu.io.in_A:=file.io.outdata1    //I-Type inp A
          aluu.io.in_B:=cu.io.Imm     //I-Type inp Imm
        } 
       
        dmem.io.Addr:= 0.U
        dmem.io.dmem_datain:=0.U
        //store
        when(cu.io.ins(6,0) === "b0100011".U)
        {
          aluu.io.aluop := 0.U
          aluu.io.in_A:=file.io.outdata1    //store inp A
          aluu.io.in_B:=cu.io.storeimm.asUInt    //store inp B
          dmem.io.storetype:=cu.io.ins(14,12)
          dmem.io.dmem_datain:=file.io.outdata2  // data input for data mem
          dmem.io.MemWrite:=cu.io.MemWrite
          dmem.io.Addr:=aluu.io.out    // memory pc_inp for dmem
        }        
       
         //branch ka hisab kitab
        sab.io.aluop:=0.U
        sab.io.inpA:=0.U
        sab.io.inpB:=0.U
        sab.io.branch := 0.B

        when(opcode === "b1100011". U ) {
          sab.io.branch := 1.B
          pc.io.immidiate_enable :=false.B

          sab.io.aluop:=cu.io.ins(14,12)
          sab.io.inpA:=file.io.outdata1
          sab.io.inpB:=file.io.outdata2
          when(sab.io.br_taken)
          {
            pc.io.immidiate_enable :=true.B
            pc.io.pc_inp := cu.io.sb_imm + pc.io.pc4
          }
            
        } .otherwise{
          pc.io.pc_inp:=0.S
        }
          
                                          //jal ka hisab kitab
          when(opcode ==="b1101111". U ){
              // aluu.io.aluop := 0.U
              aluu.io.in_A:= pc.io.pc4.asUInt  //store inp A
              aluu.io.in_B:=cu.io.jal_imm .asUInt()
              pc.io.pc_inp:=aluu.io.out.asSInt()
          }

          //auipc
          when(cu.io.auipc_out===1.B){
             aluu.io.in_A:=cu.io.auipc_imm .asUInt()
             aluu.io.in_B:=pc.io.pc4.asUInt
             //file.io.data_in:=aluu.io.out
              }

              
              pc.io.jalr_enable := false.B       

              // jalr
          when(cu.io.jalr_out===1.B){
            aluu.io.in_A:=cu.io.jalr_imm.asUInt()
            aluu.io.in_B:=file.io.outdata1
            pc.io.pc_inp:=aluu.io.out.asSInt
            pc.io.jalr_enable:=true.B
            
                }              
                      
        
   when(opcode==="b0000011".U){
        aluu.io.aluop := 0.U
        aluu.io.in_A:=file.io.outdata1    
        aluu.io.in_B:=cu.io.Imm.asUInt    
            // dmem
        dmem.io.Addr := aluu.io.out
        
        when(cu.io.func3==="b000".U){//Load byte
          when(aluu.io.out(1,0)===0.U){
            file.io.data_in:=Cat(Fill(24,dmem.io.data_out(0)(7)),(dmem.io.data_out(0)))
          }.elsewhen(aluu.io.out(1,0)===1.U){
            file.io.data_in:=Cat(Fill(24,dmem.io.data_out(1)(7)),(dmem.io.data_out(1)))
          }.elsewhen(aluu.io.out(1,0)===2.U){
            file.io.data_in:=Cat(Fill(24,dmem.io.data_out(2)(7)),(dmem.io.data_out(2)))
          }.otherwise{
            file.io.data_in:=Cat(Fill(24,dmem.io.data_out(3)(7)),(dmem.io.data_out(3)))
          }
    }.elsewhen(cu.io.func3==="b001".U){ //load half word
          when(aluu.io.out(1,0)===0.U){
              file.io.data_in:=Cat(Fill(16,dmem.io.data_out(1)(7)),(Cat(dmem.io.data_out(1),dmem.io.data_out(0))))
          }.elsewhen(aluu.io.out(1,0)===1.U){
              file.io.data_in:=Cat(Fill(16,dmem.io.data_out(2)(7)),(Cat(dmem.io.data_out(2),dmem.io.data_out(1))))
          }.elsewhen(aluu.io.out(1,0)===2.U){
              file.io.data_in:=Cat(Fill(16,dmem.io.data_out(3)(7)),(Cat(dmem.io.data_out(3),dmem.io.data_out(2))))
          }.otherwise{
              file.io.data_in:=Cat(Fill(16,dmem.io.data_out(0)(7)),(Cat(dmem.io.data_out(0),dmem.io.data_out(3))))
          }
  }.elsewhen(cu.io.func3==="b010".U){ //load word
        when(aluu.io.out(1,0)===0.U){
            file.io.data_in:=  Cat((Cat(dmem.io.data_out(3),dmem.io.data_out(2))),(Cat(dmem.io.data_out(1),dmem.io.data_out(0))))
          }.otherwise{
            file.io.data_in:= Cat((Cat(dmem.io.data_out(3),dmem.io.data_out(2))),(Cat(dmem.io.data_out(1),dmem.io.data_out(0))))
          }
  }.elsewhen(cu.io.func3==="b100".U){  //load bu
        when(aluu.io.out(1,0)===0.U){
            file.io.data_in:=Cat(Fill(24,0.U),(dmem.io.data_out(0)))
        }.elsewhen(aluu.io.out(1,0)===1.U){
            file.io.data_in:=Cat(Fill(24,0.U),(dmem.io.data_out(1)))
        }.elsewhen(aluu.io.out(1,0)===2.U){
            file.io.data_in:=Cat(Fill(24,0.U),(dmem.io.data_out(2)))
        }.otherwise{
            file.io.data_in:=Cat(Fill(24,0.U),(dmem.io.data_out(3)))
        }
  }.elsewhen(cu.io.func3==="b101".U){  // load hu
          when(aluu.io.out(1,0)===0.U){
            file.io.data_in:=Cat(Fill(16,0.U),(Cat(dmem.io.data_out(1),dmem.io.data_out(0))))
        }.elsewhen(aluu.io.out(1,0)===1.U){
            file.io.data_in:=Cat(Fill(16,0.U),(Cat(dmem.io.data_out(2),dmem.io.data_out(1))))
        }.elsewhen(aluu.io.out(1,0)===2.U){
            file.io.data_in:=Cat(Fill(16,0.U),(Cat(dmem.io.data_out(3),dmem.io.data_out(2))))
        }.otherwise{
            file.io.data_in:=Cat(Fill(16,0.U),(Cat(dmem.io.data_out(0),dmem.io.data_out(3))))
        }
    }
        }

        .elsewhen(opcode==="b0110111".U){ // lui
            file.io.data_in:=cu.io.luipc_imm.asUInt()
        }.elsewhen(opcode==="b0010111".U){   //auipc
            file.io.data_in:=cu.io.auipc_imm.asUInt + ((pc.io.pc4)-4.S).asUInt()
        }.elsewhen(opcode==="b1101111".U){  // jalr
            file.io.data_in:=(pc.io.pc4).asUInt()
        }.otherwise{
            file.io.data_in:=aluu.io.out
        }
    when(opcode==="b0100011".U){//DEMEM wen
          dmem.io.MemWrite:=1.B
    }.otherwise{
         dmem.io.MemWrite:=0.B
    }
    //io.WB:=aluu.io.out  
    io.out1:=aluu.io.out
    //io.out2:=dmem.io.data_out(0)
}
