package singlecycle
import chisel3._

class programcounter extends Module {
  val io=IO (new Bundle{
	val pc_inp = Input(SInt(32.W))
	val immidiate_enable = Input( Bool() )
	val jalr_enable=Input(Bool())
	val pc4 = Output(SInt(32.W))
	
  })
	val reg = RegInit(0.S(32.W))
	
	when(io.immidiate_enable)
	{		// reg := io.pc_inp
		// io.pc4 := reg
		reg := io.pc_inp
	}.elsewhen(io.jalr_enable)
	{
	 	reg := io.pc_inp
	}.otherwise{
		reg := io.pc4 + 4.S
	}
    
	io.pc4 := reg
}