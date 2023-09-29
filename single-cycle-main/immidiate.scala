// package singlecycle
// import chisel3 . _
// import chisel3 . util . _
// class immediateIO extends Bundle {
//     val instr = Input (UInt (32. W ) )
//     val Imm = Output ( UInt (32. W ) )
//     val store_imm = Output ( UInt (32. W ) )
//     val sb_imm  = Output ( UInt (32. W ) )
//     val uimmd_se = Output ( UInt(32. W ) )
//     val ujimmd_se = Output ( UInt (32. W ) )
//     val pc=Input(UInt(32.W))
// }
// class immediate extends Module {
//     val io = IO (new immediateIO )
//     val opcodes =io.instr(6,0)
//     io.Imm:=0.U
//     io.store_imm:=0.U
//     io.sb_imm :=0.U
//     io.ujimmd_se:=0.U
//     io.uimmd_se:=0.U

//     io.Imm:=(Cat(Fill(20,io.instr(31)),io.instr(31,20))).asSInt//i type
        
//     io.store_imm:=(Cat(Fill(20,io.instr(31)),io.instr(31,25),io.instr(11,7))).asSInt//s type
        
//     io.sb_imm :=(Cat(Fill(19,io.instr(31)),io.instr(31),io.instr(7),io.instr(30,25),io.instr(11,8), "b0".U) + io.pc).asSInt//sb type

//     io.uimmd_se:=(Cat(Fill(12,io.instr(31)),io.instr(31,12))).asSInt//u type // lui & auipc

//     io.ujimmd_se:=(Cat(Fill(12,io.instr(31)),io.instr(31),io.instr(19,12),io.instr(20),io.instr(31,21),"b0".U) +io.pc).asSInt  //uj//jal type
// }

