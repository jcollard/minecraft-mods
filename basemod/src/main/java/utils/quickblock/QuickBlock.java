package utils.quickblock;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.OffsetType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;
import utils.JSONManager;
import utils.PackageManager;
import utils.quickblock.QuickBlockRegistry.QuickBlockBuilder;
import utils.quickitem.QuickItemRegistry.QuickItemBuilder;

public class QuickBlock {

	public static List<String> generateErrors = new LinkedList<>();
	private static List<QuickBlock> blocks = null;

	/**
	 * This method is called when generating JSON objects and creating the resources
	 * directory.
	 */
	public static Map<String, String> generateResources() {
		List<QuickBlock> blocks = QuickBlock.getAllBlocks();
		Map<String, String> blockMapping = new HashMap<>();
		for (QuickBlock block : blocks) {
			JSONManager.generateBlock(block.texture, block.getSafeRegistryName(), block);
			blockMapping.put(block.getSafeRegistryName(), block.blockName);
		}
		return blockMapping;
	}

	// Memoized function for getting all items in the items package
	/**
	 * This method returns an immutable list of all QuickItems that are present in
	 * the items package.
	 * 
	 * @return an immutable list of QuickItems
	 */
	public static List<QuickBlock> getAllBlocks() {
		if (QuickBlock.blocks != null) {
			return QuickBlock.blocks;
		}
		QuickBlock.blocks = new LinkedList<>();
		Set<Class<?>> classes = PackageManager.loadClassesInPackage("blocks");		
		for (Class<?> klass : classes) {
			if (QuickBlock.class.isAssignableFrom(klass) && QuickBlock.class != klass) {
				try {
					QuickBlock item = (QuickBlock) klass.newInstance();
					if (item.include) {
						QuickBlock.blocks.add(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {
					QuickBlock.generateErrors.add("Could not create " + klass.getSimpleName()
							+ ". Likely cause: code in initialization block needs to be put in initializeProperties method.");
					e.printStackTrace();
				}
			}
		}
		return QuickBlock.getAllBlocks();
	}

	/**
	 * Quick access to the LOGGER for quick debugging
	 */
	protected final Logger LOGGER = LogManager.getLogger();

	/**
	 * The name that will be displayed for this block in Minecraft
	 */
	protected String blockName = "";

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
	public String parentModel = "block/cube_all";

	/**
	 * A reference to the current World object.
	 */
	public World world;

	/**
	 * A reference to the QuickBlockBuilder that created this QuickBlock. This is used
	 * as a delegate for all methods that are used for the Block class. Essentially,
	 * this Object should be an extension of Block but it cannot exist without an
	 * instance of Minecraft running. This results in this method forwarding /
	 * delegation to be necessary.
	 */
	private QuickBlockBuilder block;

	/**
	 * Generates a registry safe name to be used while generating JSON
	 * 
	 * @return a registry safe name to be used while generating JSON
	 */
	public String getSafeRegistryName() {
		StringBuilder newName = new StringBuilder();
		String name = blockName.toLowerCase();
		for (char c : name.toCharArray()) {
			if (Character.isLetter(c) || Character.isDigit(c)) {
				newName.append(c);
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

	public void initializeProperties() {
		
	}

	public void setBlock(QuickBlockBuilder builder) {
		this.block = builder;
	}
	
	// METHODS BELOW ARE DELEGATE METHODS
	
	
	public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
		// TODO Auto-generated method stub
		return this.block._canEntitySpawn(state, worldIn, pos, type);
	}

	
	public boolean isAir(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._isAir(state);
	}

	
	public int getLightValue(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getLightValue(state);
	}

	
	public Material getMaterial(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getMaterial(state);
	}

	
	public MaterialColor getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getMaterialColor(state, worldIn, pos);
	}

	
	public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags) {
		// TODO Auto-generated method stub
		this.block._updateNeighbors(stateIn, worldIn, pos, flags);
	}

	
	public boolean isIn(Tag<Block> tagIn) {
		// TODO Auto-generated method stub
		return this.block._isIn(tagIn);
	}

	
	public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {
		// TODO Auto-generated method stub
		this.block._updateDiagonalNeighbors(state, worldIn, pos, flags);
	}

	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
			IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		// TODO Auto-generated method stub
		return this.block._updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	
	public BlockState rotate(BlockState state, Rotation rot) {
		// TODO Auto-generated method stub
		return this.block._rotate(state, rot);
	}

	
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		// TODO Auto-generated method stub
		return this.block._mirror(state, mirrorIn);
	}

	
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._isNormalCube(state, worldIn, pos);
	}

	
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._causesSuffocation(state, worldIn, pos);
	}

	
	public boolean isViewBlocking(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._isViewBlocking(state, worldIn, pos);
	}

	
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		// TODO Auto-generated method stub
		return this.block._allowsMovement(state, worldIn, pos, type);
	}

	
	public BlockRenderType getRenderType(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getRenderType(state);
	}

	
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		// TODO Auto-generated method stub
		return this.block._isReplaceable(state, useContext);
	}

	
	public boolean isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {
		// TODO Auto-generated method stub
		return this.block._isReplaceable(p_225541_1_, p_225541_2_);
	}

	
	public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getBlockHardness(blockState, worldIn, pos);
	}

	
	public boolean ticksRandomly(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._ticksRandomly(state);
	}

	
	public boolean hasTileEntity() {
		// TODO Auto-generated method stub
		return this.block._hasTileEntity();
	}

	
	public boolean needsPostProcessing(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._needsPostProcessing(state, worldIn, pos);
	}

	
	public boolean isEmissiveRendering(BlockState p_225543_1_) {
		// TODO Auto-generated method stub
		return this.block._isEmissiveRendering(p_225543_1_);
	}

	
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		// TODO Auto-generated method stub
		return this.block._isSideInvisible(state, adjacentBlockState, side);
	}

	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		// TODO Auto-generated method stub
		return this.block._getShape(state, worldIn, pos, context);
	}

	
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {
		// TODO Auto-generated method stub
		return this.block._getCollisionShape(state, worldIn, pos, context);
	}

	
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getRenderShape(state, worldIn, pos);
	}

	
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getRaytraceShape(state, worldIn, pos);
	}

	
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._propagatesSkylightDown(state, reader, pos);
	}

	
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getOpacity(state, worldIn, pos);
	}

	
	public boolean isTransparent(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._isTransparent(state);
	}

	
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		// TODO Auto-generated method stub
		this.block._randomTick(state, worldIn, pos, random);
	}

	
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		// TODO Auto-generated method stub
		this.block._tick(state, worldIn, pos, rand);
	}

	
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		// TODO Auto-generated method stub
		this.block._animateTick(stateIn, worldIn, pos, rand);
	}

	
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		this.block._onPlayerDestroy(worldIn, pos, state);
	}

	
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		// TODO Auto-generated method stub
		this.block._neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
	}

	
	public int tickRate(IWorldReader worldIn) {
		// TODO Auto-generated method stub
		return this.block._tickRate(worldIn);
	}

	
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getContainer(state, worldIn, pos);
	}

	
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		// TODO Auto-generated method stub
		this.block._onBlockAdded(state, worldIn, pos, oldState, isMoving);
	}

	
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		// TODO Auto-generated method stub
		this.block._onReplaced(state, worldIn, pos, newState, isMoving);
	}

	
	public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn,
			BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}

	
	public void spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {
		// TODO Auto-generated method stub
		this.block._spawnAdditionalDrops(state, worldIn, pos, stack);
	}

	
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return this.block._getLootTable();
	}

	
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		// TODO Auto-generated method stub
		return this.block._getDrops(state, builder);
	}

	
	public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
		// TODO Auto-generated method stub
		this.block._dropXpOnBlockBreak(worldIn, pos, amount);
	}

	
	public float getExplosionResistance() {
		// TODO Auto-generated method stub
		return this.block._getExplosionResistance();
	}

	
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		// TODO Auto-generated method stub
		this.block._onExplosionDestroy(worldIn, pos, explosionIn);
	}

	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._isValidPosition(state, worldIn, pos);
	}

	
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		// TODO Auto-generated method stub
		return this.block._onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		// TODO Auto-generated method stub
		this.block._onEntityWalk(worldIn, pos, entityIn);
	}

	
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		// TODO Auto-generated method stub
		return this.block._getStateForPlacement(context);
	}

	
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		// TODO Auto-generated method stub
		this.block._onBlockClicked(state, worldIn, pos, player);
	}

	
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		// TODO Auto-generated method stub
		return this.block._getWeakPower(blockState, blockAccess, pos, side);
	}

	
	public boolean canProvidePower(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._canProvidePower(state);
	}

	
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		// TODO Auto-generated method stub
		this.block._onEntityCollision(state, worldIn, pos, entityIn);
	}

	
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		// TODO Auto-generated method stub
		return this.block._getStrongPower(blockState, blockAccess, pos, side);
	}

	
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te,
			ItemStack stack) {
		// TODO Auto-generated method stub
		this.block._harvestBlock(worldIn, player, pos, state, te, stack);
	}

	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
			ItemStack stack) {
		// TODO Auto-generated method stub
		this.block._onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	
	public boolean canSpawnInBlock() {
		// TODO Auto-generated method stub
		return this.block._canSpawnInBlock();
	}

	
	public ITextComponent getNameTextComponent() {
		// TODO Auto-generated method stub
		return this.block._getNameTextComponent();
	}

	
	public String getTranslationKey() {
		// TODO Auto-generated method stub
		return this.block._getTranslationKey();
	}

	
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		// TODO Auto-generated method stub
		return this.block._eventReceived(state, worldIn, pos, id, param);
	}

	
	public PushReaction getPushReaction(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getPushReaction(state);
	}

	
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getAmbientOcclusionLightValue(state, worldIn, pos);
	}

	
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// TODO Auto-generated method stub
		this.block._onFallenUpon(worldIn, pos, entityIn, fallDistance);
	}

	
	public void onLanded(IBlockReader worldIn, Entity entityIn) {
		// TODO Auto-generated method stub
		this.block._onLanded(worldIn, entityIn);
	}

	
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getItem(worldIn, pos, state);
	}

	
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		// TODO Auto-generated method stub
		this.block._fillItemGroup(group, items);
	}

	
	public IFluidState getFluidState(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getFluidState(state);
	}

	
	public float getSlipperiness() {
		// TODO Auto-generated method stub
		return this.block._getSlipperiness();
	}

	
	public float getSpeedFactor() {
		// TODO Auto-generated method stub
		return this.block._getSpeedFactor();
	}

	
	public float getJumpFactor() {
		// TODO Auto-generated method stub
		return this.block._getJumpFactor();
	}

	
	public long getPositionRandom(BlockState state, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getPositionRandom(state, pos);
	}

	
	public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
		// TODO Auto-generated method stub
		this.block._onProjectileCollision(worldIn, state, hit, projectile);
	}

	
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		// TODO Auto-generated method stub
		this.block._onBlockHarvested(worldIn, pos, state, player);
	}

	
	public void fillWithRain(World worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		this.block._fillWithRain(worldIn, pos);
	}

	
	public boolean canDropFromExplosion(Explosion explosionIn) {
		// TODO Auto-generated method stub
		return this.block._canDropFromExplosion(explosionIn);
	}

	
	public boolean hasComparatorInputOverride(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._hasComparatorInputOverride(state);
	}

	
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getComparatorInputOverride(blockState, worldIn, pos);
	}

	
	protected void fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
		// TODO Auto-generated method stub
		this.block._fillStateContainer(builder);
	}

	
	public StateContainer<Block, BlockState> getStateContainer() {
		// TODO Auto-generated method stub
		return this.block._getStateContainer();
	}

	
	public OffsetType getOffsetType() {
		// TODO Auto-generated method stub
		return this.block._getOffsetType();
	}

	
	public Vec3d getOffset(BlockState state, IBlockReader worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return this.block._getOffset(state, worldIn, pos);
	}

	
	public SoundType getSoundType(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getSoundType(state);
	}

	
	public Item asItem() {
		// TODO Auto-generated method stub
		return this.block._asItem();
	}

	
	public boolean isVariableOpacity() {
		// TODO Auto-generated method stub
		return this.block._isVariableOpacity();
	}

	
	public String toString() {
		// TODO Auto-generated method stub
		return this.block._toString();
	}

	
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		// TODO Auto-generated method stub
		this.block._addInformation(stack, worldIn, tooltip, flagIn);
	}

	
	public float getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		// TODO Auto-generated method stub
		return this.block._getSlipperiness(state, world, pos, entity);
	}

	
	public ToolType getHarvestTool(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getHarvestTool(state);
	}

	
	public int getHarvestLevel(BlockState state) {
		// TODO Auto-generated method stub
		return this.block._getHarvestLevel(state);
	}

	
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing,
			IPlantable plantable) {
		// TODO Auto-generated method stub
		return this.block._canSustainPlant(state, world, pos, facing, plantable);
	}


}
