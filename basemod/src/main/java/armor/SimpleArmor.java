package armor;

import net.minecraft.inventory.EquipmentSlotType;
import utils.quickarmor.QuickArmor;

public class SimpleArmor extends QuickArmor {
	
	{
		
		enchantability = 0;
		// Specify the durabilities of each piece, otherwise defaults to 1
		durabilities.put(EquipmentSlotType.CHEST, 7);
		durabilities.put(EquipmentSlotType.FEET, 3);
		durabilities.put(EquipmentSlotType.LEGS, 5);
		durabilities.put(EquipmentSlotType.HEAD, 3);
		
		// Specify the model texture.
		// This is the prefix for the 2 layer files in textures:
		// {texture}_layer_1.png
		// {texture}_layer_2.png
		texture = "armor_template";
		
		// Specify the name of each of the armor pieces
		chestName = "Simple Chest";
		legsName = "Simple Leggings";
		headName = "Simple Helmet";
		feetName = "Simple Feet";
		
		// Specify the item texture for each of the armor pieces
		// This is what is displayed carrying the item / in the
		// inventory
		textureChest = "banana";
		textureHead = "banana";
		textureLegs = "banana";
		textureFeet = "banana";
		
		// Specify the toughness of this armor
		toughness = 3.2F;
		
	}

}
