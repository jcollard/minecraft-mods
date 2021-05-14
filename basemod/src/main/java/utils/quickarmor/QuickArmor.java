package utils.quickarmor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
import utils.JSONManager;
import utils.PackageManager;
import utils.quickitem.QuickItem;

public class QuickArmor {
	
	public static List<String> generateErrors = new LinkedList<>();
	private static List<QuickArmor> armor = null;

	/**
	 * This method is called when generating JSON objects and creating the resources
	 * directory.
	 */
	public static Map<String, String> generateResources() {
		List<QuickArmor> all = QuickArmor.getAll();
		System.out.println("Generating Armor Resources for " + all.size() + " QuickArmor classes.");
		Map<String, String> itemMapping = new HashMap<>();
		for (QuickArmor armor : all) {
			JSONManager.generateArmor(armor);
			itemMapping.put(armor.getSafeRegistryName() + "_chestplate", armor.chestName);
			itemMapping.put(armor.getSafeRegistryName() + "_leggings", armor.legsName);
			itemMapping.put(armor.getSafeRegistryName() + "_helmet", armor.headName);
			itemMapping.put(armor.getSafeRegistryName() + "_boots", armor.feetName);
		}
		return itemMapping;
	}

	// Memoized function for getting all items in the items package
	/**
	 * This method returns an immutable list of all QuickItems that are present in
	 * the items package.
	 * 
	 * @return an immutable list of QuickItems
	 */
	public static List<QuickArmor> getAll() {
		if (QuickArmor.armor != null) {
			return QuickArmor.armor;
		}
		QuickArmor.armor = new LinkedList<>();
		Set<Class<?>> classes = PackageManager.loadClassesInPackage("armor");
		for (Class<?> klass : classes) {
			if (QuickArmor.class.isAssignableFrom(klass) && QuickArmor.class != klass) {
				try {
					QuickArmor armor = (QuickArmor) klass.getConstructor().newInstance();
					if (armor.enabled) {
						QuickArmor.armor.add(armor);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {
					QuickItem.generateErrors.add("Could not create " + klass.getSimpleName()
							+ ". Likely cause: code in initialization block needs to be placed in initializeProperties method.");
					e.printStackTrace();
				}
			}
		}
		return QuickArmor.getAll();
	}
	
	protected boolean enabled = true;
	protected int durability = 10;
	protected int enchantability = 0;
	protected Map<EquipmentSlotType, Integer> armorValue = new TreeMap<>();
	//protected SoundEvent soundEvent = SoundEvents.BLOCK_METAL_BREAK;
	//protected Set<Item> repairMaterials = new TreeSet<>();
	protected float toughness = 1.0F;
	protected String texture = null;
	protected String chestName = null;
	protected String legsName = null;
	protected String headName = null;
	protected String feetName = null;
	protected String textureChestplate = null;
	protected String textureHelmet = null;
	protected String textureLeggings = null;
	protected String textureBoots = null;
	
	protected String parentModel = "item/generated";

	{
		armorValue.put(EquipmentSlotType.CHEST, 1);
		armorValue.put(EquipmentSlotType.FEET, 1);
		armorValue.put(EquipmentSlotType.HEAD, 1);
		armorValue.put(EquipmentSlotType.LEGS, 1);
	}

	public String getSafeRegistryName() {
		StringBuilder newName = new StringBuilder();
		//TODO Consider using class name?
		String name = this.getClass().getSimpleName().toLowerCase();
		for (char c : name.toCharArray()) {
			if (Character.isLetter(c) || Character.isDigit(c)) {
				newName.append(c);
			} else {
				newName.append("_");
			}
		}
		return newName.toString();
	}
	
	public List<String> getErrors() {
		List<String> errors = new LinkedList<>();
		//TODO check errors
		return errors;
	}
	


	public void initializeProperties(Properties p) {
		// TODO Auto-generated method stub
		
	}

	public IArmorMaterial getMaterial() {
		return new QuickArmorMaterial(this);
	}

	public String getParentModel() {
		return parentModel;
	}

	private static class QuickArmorMaterial implements IArmorMaterial {

		private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
		private final int maxDamageFactor;
		private final int enchantability;
		private final Map<EquipmentSlotType, Integer> damageReductionAmount;
		private final SoundEvent soundEvent;
		private final Ingredient repairMaterial;
		private final String name;
		private final float toughness;

		public QuickArmorMaterial(QuickArmor quickArmor) {
			this.maxDamageFactor = quickArmor.durability;
			this.enchantability = quickArmor.enchantability;
			this.damageReductionAmount = Collections.unmodifiableMap(quickArmor.armorValue);
			this.soundEvent = quickArmor.getSoundEvent();
			// TODO: Implement from builder.repairMaterials
			this.repairMaterial = Ingredient.fromItems();
			this.name = "basemod:" + quickArmor.getSafeRegistryName();
			this.toughness = quickArmor.toughness;
			System.out.println();
			System.out.println("Created Material for " + this.name);

		}

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return this.damageReductionAmount.get(slotIn);
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

	public int getDurability() {
		return durability;
	}


	public int getEnchantability() {
		return enchantability;
	}


	public Map<EquipmentSlotType, Integer> getDurabilities() {
		return armorValue;
	}


	public SoundEvent getSoundEvent() {
		return SoundEvents.BLOCK_METAL_BREAK;
	}


	public Set<Item> getRepairMaterials() {
		return new TreeSet<>();
	}


	public float getToughness() {
		return toughness;
	}


	public String getTexture() {
		return texture;
	}


	public String getChestName() {
		return chestName;
	}


	public String getLegsName() {
		return legsName;
	}


	public String getHeadName() {
		return headName;
	}


	public String getFeetName() {
		return feetName;
	}


	public String getTextureChest() {
		return textureChestplate;
	}


	public String getTextureHead() {
		return textureHelmet;
	}


	public String getTextureLegs() {
		return textureLeggings;
	}


	public String getTextureFeet() {
		return textureBoots;
	}

}
