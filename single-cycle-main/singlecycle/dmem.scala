package singlecycle
import chisel3._
import chisel3.util._

class datamem extends Module {
  val io = IO (new Bundle {
	val Addr = Input(UInt(32.W))
	val dmem_datain = Input (UInt(32.W))
    val data_out= Output (Vec (4 , UInt ( 32.W ) ) )
	val storetype = Input(UInt(3.W))
	val MemWrite = Input(Bool())
	//val MemRead = Input(Bool())
  })

	// io.data_out(0):=0.U
	// io.data_out(1):=0.U
	// io.data_out(2):=0.U
	// io.data_out(3):=0.U
	val mem = Mem(1024 , Vec (4 , UInt ( 32.W ) ) )

    val bytloc=io.Addr(1,0)
     val mask = Wire(Vec(4,Bool()))
	val data=Wire(Vec (4 , UInt ( 8.W ) ))
	data(0):=io.dmem_datain(7,0)
	data(1):=0.U
				data(2):=0.U
				data(3):=0.U
				mask(0):=true.B
				mask(1):=false.B
				mask(2):=false.B
				mask(3):=false.B


  switch (io.storetype){
    is("b000".U){            //store byte     
       
            when(bytloc==="b00".U){
				
				data(0):=io.dmem_datain(7,0)
				data(1):=0.U
				data(2):=0.U
				data(3):=0.U

				mask(0):=true.B
				mask(1):=false.B
				mask(2):=false.B
				mask(3):=false.B
            }
             .elsewhen(bytloc==="b01".U){
              
				data(0):=0.U
				data(1):=io.dmem_datain(7,0)
				data(2):=0.U
				data(3):=0.U

			    mask(0):=false.B
				mask(1):=true.B
				mask(2):=false.B
				mask(3):=false.B
			}
            .elsewhen(bytloc==="b10".U){
			   
				data(0):=0.U
				data(1):=0.U
				data(2):=io.dmem_datain(7,0)
				data(3):=0.U
				
			    mask(0):=false.B
				mask(1):=false.B
				mask(2):=true.B
				mask(3):=false.B
			
			} .elsewhen(bytloc==="b11".U){
				data(0):=0.U
				data(1):=0.U
				data(2):=0.U
				data(3):=io.dmem_datain(7,0)
				
				mask(0):=false.B
				mask(1):=false.B
				mask(2):=false.B
				mask(3):=true.B
			}.otherwise{
				mask(0):=false.B
				mask(1):=false.B
				mask(2):=false.B
				mask(3):=0.B
			}
        }
	 is( "b001".U){            //store halfword                 
         when(bytloc==="b00".U || bytloc==="b01".U){
				data(0):=io.dmem_datain(7,0)
				data(1):=io.dmem_datain(15,8)
				data(2):=0.U
				data(3):=0.U
				
				mask(0):=true.B
				mask(1):=true.B
				mask(2):=false.B
				mask(3):=false.B
            }
			 .elsewhen(bytloc==="b10".U||bytloc==="b11".U){
				data(0):=0.U
				data(1):=0.U
				data(2):=io.dmem_datain(23,16)
				data(3):=io.dmem_datain(31,24)

				mask(0):=false.B
				mask(1):=false.B
				mask(2):=true.B
				mask(3):=true.B
            }.otherwise{
				mask(0):=false.B
				mask(1):=false.B
				mask(2):=false.B
				mask(3):=0.B
			}}
		  
        
     is( "b010".U){           //store word  
			
				data(0):=io.dmem_datain(7,0)
				data(1):=io.dmem_datain(15,8)
				data(2):=io.dmem_datain(23,16)
				data(3):=io.dmem_datain(31,24)

				mask(0):=true.B
				mask(1):=true.B
				mask(2):=true.B
				mask(3):=true.B
            }
			
  }

	when(io.MemWrite===true.B){
	mem.write(io.Addr,data,mask)
	}

	io.data_out:=mem.read(io.Addr)//io.Addr)
	

						}