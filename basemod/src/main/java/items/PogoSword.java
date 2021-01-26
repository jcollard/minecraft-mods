package items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import utils.quickitem.QuickItem;

public class PogoSword extends QuickItem {

	{
		itemName = "Pogo Sword";
		texture = "iron_sword";
		// This makes the sword look like it is being held like a sword
		parentModel = "item/iron_sword";
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		
		// Create a variable to store if the player is on the ground
		boolean playerIsOnGround;
		// Set the playerIsOnGround variable to be true or false
		playerIsOnGround = playerIn.onGround;
		
		// If the player is on the ground, execute the bounce!
		if (playerIsOnGround) {
			// Make the player launch forward the direction they are facing
			double x, z;
			x = playerIn.getForward().x;
			z = playerIn.getForward().z;
			// When the player right clicks, they should be launched upward.
			playerIn.addVelocity(x, 1, z);
		}
		
		// The line below tells the game what the result of using the item is
		// in this case, the default action is fine. We leave the default return
		// that was generated to use the default action
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	
	
}
