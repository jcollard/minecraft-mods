package utils.quickitem;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import utils.JSONManager;
import utils.PackageManager;
import utils.quickitem.QuickItemRegistry.QuickItemBuilder;

public class QuickItem {

	public static List<String> generateErrors = new LinkedList<>();
	private static List<QuickItem> items = null;

	/**
	 * This method is called when generating JSON objects and creating the resources
	 * directory.
	 */
	public static void generateResources() {
		List<QuickItem> items = QuickItem.getAllItems();
		Map<String, String> itemMapping = new HashMap<>();
		for (QuickItem item : items) {
			JSONManager.generateItem(item.texture, item.getSafeRegistryName(), item);
			itemMapping.put(item.getSafeRegistryName(), item.itemName);
		}
		try {
			JSONManager.generateLangFile(itemMapping);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	// Memoized function for getting all items in the items package
	/**
	 * This method returns an immutable list of all QuickItems that are present in
	 * the items package.
	 * 
	 * @return an immutable list of QuickItems
	 */
	public static List<QuickItem> getAllItems() {
		if (QuickItem.items != null) {
			return QuickItem.items;
		}
		QuickItem.items = new LinkedList<>();
		Set<Class<?>> classes = PackageManager.loadClassesInPackage("items");		
		for (Class<?> klass : classes) {
			if (QuickItem.class.isAssignableFrom(klass) && QuickItem.class != klass) {
				try {
					QuickItem item = (QuickItem) klass.newInstance();
					if (item.include) {
						QuickItem.items.add(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {
					QuickItem.generateErrors.add("Could not create " + klass.getSimpleName()
							+ ". Likely cause: code in initialization block needs to be put in initializeProperties method.");
					e.printStackTrace();
				}
			}
		}
		return QuickItem.getAllItems();
	}

	/**
	 * Quick access to the LOGGER for quick debugging
	 */
	protected final Logger LOGGER = LogManager.getLogger();

	/**
	 * The name that will be displayed for this item in Minecraft
	 */
	protected String itemName = "";

	/**
	 * The name of the texture that should be used. A file name
	 * `assets/texture/{texture}.png` must exist.
	 */
	protected String texture = "";
		
	/**
	 * Set this to false if you do not want it to be added to the Mod
	 */
	protected boolean include = true;
	
	/**
	 * The parent model to use
	 */
	public String parentModel = "item/generated";

	/**
	 * A reference to the current World object.
	 */
	public World world;

	/**
	 * A reference to the QuickItemBuilder that created this QuickItem. This is used
	 * as a delegate for all methods that are used for the Item class. Essentially,
	 * this Object should be an extension of Item but it cannot exist without an
	 * instance of Minecraft running. This results in this method forwarding /
	 * delegation to be necessary.
	 */
	private QuickItemBuilder item;

	/**
	 * Generates a registry safe name to be used while generating JSON
	 * 
	 * @return a registry safe name to be used while generating JSON
	 */
	public String getSafeRegistryName() {
		StringBuilder newName = new StringBuilder();
		String name = itemName.toLowerCase();
		for (char c : name.toCharArray()) {
			if (Character.isLetter(c) || Character.isDigit(c)) {
				newName.append(c);
				System.out.println("Here!");
			} else {
				newName.append("_");
			}
		}
		return newName.toString();
	}

	/**
	 * Writes the string to the LOGGER's debug method
	 * 
	 * @param s The message to send
	 */
	public void debug(String s) {
		LOGGER.debug(s);
	}

	/**
	 * Sets the associated QuickItemBuilder that created this QuickItem. This method
	 * should almost never be used.
	 * 
	 * @param quickItemBuilder The QuickItemBuilder that created this QuickItem
	 */
	public void setItem(QuickItemBuilder quickItemBuilder) {
		this.item = quickItemBuilder;
	}

	// Override methods

	/**
	 * This method is fired when the player right clicks the mouse while holding
	 * this QuickItem. The world object is updated prior to this method being
	 * called.
	 * 
	 * @param playerIn The PlayerEntity using this item
	 * @param handIn   The hand that htis item is in
	 */
	public void onRightClick(PlayerEntity playerIn, Hand handIn) {

	}

	/**
	 * This method is used when the item is used on a block.
	 * 
	 * @param context The context of the item's use. This object contains
	 *                information about the player, world, and block that the item
	 *                is being used on.
	 */
	public void onUse(ItemUseContext context) {
	}

	/**
	 * This method is called to initialize the QuickItem when MineCraft is
	 * launching. All initializations should go here. For example, all
	 * properties.methods should be run in this method.
	 * 
	 * @param properties The Properties object that will be used to intialize this
	 *                   Item
	 * 
	 */
	public void initializeProperties(Properties properties) {
		properties.group(ItemGroup.MATERIALS);
	}

	/**
	 * Called as the item is being used by an entity.
	 */
	public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		item._onUse(worldIn, livingEntityIn, stack, count);
	}

	/**
	 * Called when an ItemStack with NBT data is read to potentially that
	 * ItemStack's NBT data
	 */
	public boolean updateItemStackNBT(CompoundNBT nbt) {
		return item._updateItemStackNBT(nbt);
	}

	/**
	 * Returns the actual Item that this QuickItem is associated with
	 * 
	 * @return the Item this QuickItem is shadowing
	 */
	public Item asItem() {
		return item._asItem();
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when
	 * this item is used on a Block, see {@link #onItemUse}.
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		return item._onItemRightClick(worldIn, playerIn, handIn);
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not
	 * called when the player stops using the Item before the action is complete.
	 */
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		return item._onItemUseFinish(stack, worldIn, entityLiving);
	}

	/**
	 * Current implementations of this method in child classes do not use the entry
	 * argument beside ev. They just raise the damage on the stack.
	 */
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return item._hitEntity(stack, target, attacker);
	}

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the
	 * "Use Item" statistic.
	 */
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
			LivingEntity entityLiving) {
		return item._onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	/**
	 * Check whether this Item can harvest the given Block
	 */
	public boolean canHarvestBlock(BlockState blockIn) {
		return item._canHarvestBlock(blockIn);
	}

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on
	 * sheep.
	 */
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		return item._itemInteractionForEntity(stack, playerIn, target, hand);
	}

	/**
	 * If this function returns true (or the item is damageable), the ItemStack's
	 * NBT tag will be sent to the client.
	 */
	public boolean shouldSyncTag() {
		return item._shouldSyncTag();
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to
	 * check if is on a player hand and update it's contents.
	 */
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		item._inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		item._onCreated(stack, worldIn, playerIn);
	}

	/**
	 * Returns {@code true} if this is a complex item.
	 */
	public boolean isComplex() {
		return item._isComplex();
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	public UseAction getUseAction(ItemStack stack) {
		return item._getUseAction(stack);
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getUseDuration(ItemStack stack) {
		return item._getUseDuration(stack);
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse
	 * button).
	 */
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		item._onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		item._addInformation(stack, worldIn, tooltip, flagIn);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public ITextComponent getDisplayName(ItemStack stack) {
		return item._getDisplayName(stack);
	}

	/**
	 * Returns true if this item has an enchantment glint. By default, this returns
	 * <code>stack.isItemEnchanted()</code>, but other items can override it (for
	 * instance, written books always return true).
	 * 
	 * Note that if you override this method, you generally want to also call the
	 * super version (on {@link Item}) to get the glint for enchanted items. Of
	 * course, that is unnecessary if the overwritten version always returns true.
	 */
	public boolean hasEffect(ItemStack stack) {
		return item._hasEffect(stack);
	}

	/**
	 * Return an item rarity from EnumRarity
	 */
	public Rarity getRarity(ItemStack stack) {
		return item._getRarity(stack);
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	public boolean isEnchantable(ItemStack stack) {
		return item._isEnchantable(stack);
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on
	 * material.
	 */
	public int getItemEnchantability() {
		return item._getItemEnchantability();
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns
	 * 16 items)
	 */
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		item._fillItemGroup(group, items);
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return item._getIsRepairable(toRepair, repair);
	}

	/**
	 * If this itemstack's item is a crossbow
	 */
	public boolean isCrossbow(ItemStack stack) {
		return item._isCrossbow(stack);
	}

	/**
	 * Returns true if this QuickItem is a Food and false otherwise.
	 * 
	 * @return
	 */
	public boolean isFood() {
		return item._isFood();
	}

	/**
	 * Returns the Food that this QuickItem acts as or null if it is not a Food.
	 * 
	 * @return the Food that this QuickItem acts as
	 */
	public Food getFood() {
		return item._getFood();
	}

	/**
	 * Called when this item is used when targetting a Block
	 */
	public ActionResultType onItemUse(ItemUseContext context) {
		return item._onItemUse(context);
	}

	// TODO: Write comment
	public boolean isDamageable() {
		return item._isDamageable();
	}

	// TODO: Write comment
	public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return item._canPlayerBreakBlockWhileHolding(state, worldIn, pos, player);
	}

	// TODO: Write comment
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return item._getDestroySpeed(stack, state);
	}

	// TODO: Write comment
	protected boolean isInGroup(ItemGroup group) {
		return item._isInGroup(group);
	}

	// TODO: Write comment
	public boolean isRepairable(ItemStack stack) {
		return item._isRepairable(stack);
	}

	// TODO: Write comment
	public Set<ToolType> getToolTypes(ItemStack stack) {
		return item._getToolTypes(stack);
	}

	// TODO: Write comment
	public int getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
		return item._getHarvestLevel(stack, tool, player, blockState);
	}

	// TODO: Write comment
	public Set<ResourceLocation> getTags() {
		return item._getTags();
	}

	// TODO: Write comment
	public SoundEvent getDrinkSound() {
		return item._getDrinkSound();
	}

	// TODO: Write comment
	public SoundEvent getEatSound() {
		return item._getEatSound();
	}

	// TODO: No comments? Consider removing / writing my own comments
	public ITextComponent getName() {
		return item._getName();
	}

	// TODO: No comments? Consider removing / writing my own comments
	public IItemPropertyGetter getPropertyGetter(ResourceLocation key) {
		return item._getPropertyGetter(key);
	}

	// TODO: No comments? Consider removing / writing my own comments
	public boolean isIn(Tag<Item> tagIn) {
		return item._isIn(tagIn);
	}

	// TODO: No comments? Consider removing / writing my own comments
	public ItemStack getDefaultInstance() {
		return item._getDefaultInstance();
	}

	// TODO: No comments? Consider removing / writing my own comments
	public boolean hasCustomProperties() {
		return item._hasCustomProperties();
	}

	// TODO: No comments? Consider removing / writing my own comments
	protected String getDefaultTranslationKey() {
		return item._getDefaultTranslationKey();
	}

	// TODO: No comments? Consider removing / writing my own comments
	public String getTranslationKey() {
		return item._getTranslationKey();
	}

	// TODO: No comments? Consider removing / writing my own comments
	public String getTranslationKey(ItemStack stack) {
		return item._getTranslationKey(stack);
	}

}
