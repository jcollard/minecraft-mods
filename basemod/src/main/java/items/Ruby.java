package items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import utils.quickitem.QuickItem;

public class Ruby extends QuickItem {
	
	{
		texture = "ruby";
		itemName = "Ruby of Flight";
	}

	@Override
	public void onRightClick(PlayerEntity p, Hand handIn) {
		debug("I am right clicking my Ruby of Flight!");
		
		// Get the x, y, and z forward vector
		double x = p.getForward().x;
		double y = p.getForward().y;
		double z = p.getForward().z;
		
		// Set the player to move in the direction they are facing
		p.setVelocity(x, y, z);
		
	}

}
