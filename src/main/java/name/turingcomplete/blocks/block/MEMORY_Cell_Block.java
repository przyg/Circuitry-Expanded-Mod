package name.turingcomplete.blocks.block;

import name.turingcomplete.blocks.AbstractSimpleLogicGate;
import name.turingcomplete.init.propertyInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MEMORY_Cell_Block extends AbstractSimpleLogicGate {
    public static final BooleanProperty ENABLED = Properties.ENABLED;
    public static final BooleanProperty SWAP = propertyInit.SWAPPED_DIR;

    public MEMORY_Cell_Block(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(ENABLED,false)
                .with(SWAP,false)
        );
    }

    @Override
    protected void properties(StateManager.Builder<Block, BlockState> builder) {
        super.properties(builder);
        builder.add(ENABLED,SWAP);
    }

    @Override
    protected boolean gateConditionMet(World world, BlockPos pos, BlockState state) {
        boolean store = isInputPowered(world,state,pos,getStoreDirection(state));

        if (store) return isInputPowered(world,state,pos,InputDirection.BACK);

        return state.get(POWERED);
    }

    @Override
    protected BlockState updateExtras(World world, BlockPos pos, BlockState state) {
        boolean store = isInputPowered(world,state,pos,getStoreDirection(state));
        boolean enabled = state.get(ENABLED);

        if (enabled != store) return state.with(ENABLED,store);

        return state;
    }

    private InputDirection getStoreDirection(BlockState state){
        return state.get(SWAP) ? InputDirection.LEFT : InputDirection.RIGHT;
    }

    @Override
    public boolean supportsSideDirection(BlockState state, Direction direction)
    {return direction == getStoreDirection(state).getRelativeDirection(state.get(FACING)).getOpposite();}

    @Override
    public boolean supportsBackDirection()
    {return true;}


    @Override
    protected boolean shouldUpdateExtras(World world, BlockState state, BlockPos pos)
    {return state.get(ENABLED) != isInputPowered(world,state,pos,getStoreDirection(state));}

}