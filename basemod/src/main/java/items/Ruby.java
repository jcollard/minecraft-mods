package items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import utils.quickitem.QuickItem;

public class Ruby extends QuickItem {
	
	{
		texture = "ruby";
		itemName = "Ruby of Flight";
	}

	@Override
	public void onRightClick(PlayerEntity playerIn, Hand handIn) {
		// Find the direction the player is facing
		Vec3d v = playerIn.getForward();
		
		// Move the player in the direction they are facing
		playerIn.setVelocity(v.x, v.y, v.z);
	}

}
