package emasher.sockets.pipes;

import cofh.api.energy.IEnergyHandler;
import emasher.sockets.SocketsMod;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileMJAdapter extends TileAdapterBase implements IPowerReceptor, IEnergyHandler
{
	public PowerHandler capacitor;
	
	public TileMJAdapter()
	{
		capacitor = new PowerHandler(this, Type.STORAGE);
		capacitor.configure(0, 250, 0, 250);

		//minimum 1 mj per tic is freaking crazy as a default!
		capacitor.setPerdition(new PowerHandler.PerditionCalculator(0.05f));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		capacitor.readFromNBT(data);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		capacitor.writeToNBT(data);
	}
	
	@Override
	public void updateEntity()
	{
		ForgeDirection d;
		if(! worldObj.isRemote) for(int i = 0; i < 6; i++) if(outputs[i])
		{
			d = ForgeDirection.getOrientation(i);
			int xo = xCoord + d.offsetX;
			int yo = yCoord + d.offsetY;
			int zo = zCoord + d.offsetZ;
			
			TileEntity te = worldObj.getBlockTileEntity(xo, yo, zo);
			
			if(te != null)
			{
				if(capacitor.getEnergyStored() <= 0) return;
				
				if(te instanceof IEnergyHandler)
				{
					IEnergyHandler ieh = (IEnergyHandler)te;

					int rfStored = (int) (capacitor.getEnergyStored() * SocketsMod.RFperMJ);
					float mjConsumed = ieh.receiveEnergy(d.getOpposite(), rfStored, false) / (float)SocketsMod.RFperMJ;
					capacitor.useEnergy(0, mjConsumed, true);
				}
				else if(te instanceof IPowerReceptor)
				{
					IPowerReceptor ipr = (IPowerReceptor)te;
					PowerReceiver pr = ipr.getPowerReceiver(d.getOpposite());
					
					if(pr != null)
					{
						float mjConsumed = pr.receiveEnergy(Type.STORAGE, capacitor.getEnergyStored(), d.getOpposite());
						capacitor.useEnergy(0, mjConsumed, true);
					}
				}
			}
		}
	}
	
	// IEnergyHandler
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		if(outputs[from.ordinal()]) return 0;

		float maxMjReceive = maxReceive / SocketsMod.RFperMJ;
		float maxMjAccept = capacitor.getMaxEnergyStored() - capacitor.getEnergyStored();
		maxMjReceive = maxMjAccept < maxMjReceive ? maxMjAccept : maxMjReceive;

		if(!simulate) capacitor.addEnergy(maxMjReceive);

		return (int)(maxMjReceive * SocketsMod.RFperMJ);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		if(!outputs[from.ordinal()]) return 0;

		float maxMj = maxExtract / (float) SocketsMod.RFperMJ;
		int sentRf = (int)(capacitor.useEnergy(0, maxMj, false) * SocketsMod.RFperMJ); // handles amounts less than 1/10th an MJ
		capacitor.useEnergy(0,(float) sentRf / SocketsMod.RFperMJ, true); //only use what is sent
		return sentRf;
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		return (int)(capacitor.getEnergyStored() * SocketsMod.RFperMJ);
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		return (int)(capacitor.getMaxEnergyStored() * SocketsMod.RFperMJ);
	}

	// IPowerReceptor
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return capacitor.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {}

	@Override
	public World getWorld()
	{
		return worldObj;
	}
	
}
