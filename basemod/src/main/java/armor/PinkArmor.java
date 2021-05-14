package armor;

import net.minecraft.inventory.EquipmentSlotType;
import utils.quickarmor.QuickArmor;

public class PinkArmor extends QuickArmor {
	
	{
		// Flags if the armor should be generated with the build command.
		// Defaults to true. Set to false if you don't want the Armor to be in the game.
		enabled = true;
		
		// Specify the enchantability of this armor
		enchantability = 0;
		
		// Specifies the durability of this armor
		durability = 3;
		
		// Specify the +Armor value (damage reduction) of each piece, otherwise defaults to 1
		armorValue.put(EquipmentSlotType.CHEST, 7);
		armorValue.put(EquipmentSlotType.FEET, 3);
		armorValue.put(EquipmentSlotType.LEGS, 5);
		armorValue.put(EquipmentSlotType.HEAD, 4);
		
		// Specify the model texture.
		// This is the prefix for the 2 layer files in textures:
		// {texture}_layer_1.png
		// {texture}_layer_2.png
		texture = "pink";
		
		// Specify the name of each of the armor pieces
		chestName = "Pink Chest";
		legsName = "Pink Leggings";
		headName = "Pink Helmet";
		feetName = "Pink Feet";
		
		// Specify the item texture for each of the armor pieces
		// This is what is displayed carrying the item / in the
		// inventory
		textureChestplate = "pink_chestplate";
		textureHelmet = "pink_helmet";
		textureLeggings = "pink_leggings";
		textureBoots = "pink_boots";
		
		// Specify the toughness of this armor
		toughness = 3.2F;
		
	}

}
