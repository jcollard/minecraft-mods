package utils.quickarmor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class QuickArmor {
	
	public static List<QuickArmor> getAllArmor() {
		// TODO Auto-generated method stub
		return null;
	}

	protected int durability = 10;
	protected int enchantability = 0;
	protected Map<EquipmentSlotType, Integer> durabilities = new TreeMap<>();
	protected SoundEvent soundEvent = SoundEvents.BLOCK_METAL_BREAK;
	protected Set<Item> repairMaterials = new TreeSet<>();
	protected String name = null;
	protected float toughness = 1.0F;

	{
		durabilities.put(EquipmentSlotType.CHEST, 1);
		durabilities.put(EquipmentSlotType.FEET, 1);
		durabilities.put(EquipmentSlotType.HEAD, 1);
		durabilities.put(EquipmentSlotType.LEGS, 1);
	}
	


	public String getSafeRegistryName() {
		// TODO Auto-generated method stub
		return null;
	}
	


	public void initializeProperties(Properties p) {
		// TODO Auto-generated method stub
		
	}

	public IArmorMaterial getMaterial() {
		return new QuickArmorMaterial(this);
	}

	private static class QuickArmorMaterial implements IArmorMaterial {

		private final int durability;
		private final int enchantability;
		private final Map<EquipmentSlotType, Integer> durabilities;
		private final SoundEvent soundEvent;
		private final Ingredient repairMaterial;
		private final String name;
		private final float toughness;

		public QuickArmorMaterial(QuickArmor quickArmor) {
			this.durability = quickArmor.durability;
			this.enchantability = quickArmor.enchantability;
			this.durabilities = Collections.unmodifiableMap(quickArmor.durabilities);
			this.soundEvent = quickArmor.soundEvent;
			// TODO: Implement from builder.repairMaterials
			this.repairMaterial = Ingredient.fromItems();
			this.name = quickArmor.name;
			this.toughness = quickArmor.toughness;

		}

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return this.durability;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return this.durabilities.get(slotIn);
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return this.soundEvent;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public float getToughness() {
			return this.toughness;
		}

	}

}
