package dev.rebby.yumecraft.util;

import dev.rebby.yumecraft.YumeCraft;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DimensionalTeleportationHandler {

    public static final int TELEPORTATION_SUCCESSFUL = 0;
    public static final int FAILED_TELEPORTATION = 1;
    public static final int TELEPORTED_WITHIN_WORLD = 2;

    private static List<DimensionalTeleportationHandler> handlers;

    private State state;
    private final ServerPlayerEntity user;

    public DimensionalTeleportationHandler(ServerPlayerEntity user, ServerWorld world) {
        this.user = user;
        this.state = findWorldState(world);
    }

    public static void init(){
        handlers = new ArrayList<>();

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                handlers.removeIf(handler -> handler.user.equals(entity));
                handlers.add(new DimensionalTeleportationHandler((player), world));
            }
        });

//        ServerWorldEvents.LOAD.register((server, world) -> {
//            List<ServerPlayerEntity> players = world.getPlayers();
//            for (int i = 0; i < players.size(); i++) {
//                boolean add = true;
//
//                for (int j = 0; j < handlers.size(); j++) {
//                    if (handlers.get(j).user.equals(players.get(i))) {
//                        add = false;
//                        break;
//                    }
//                }
//
//                if (add) {
//                    handlers.add(new DimensionalTeleportationHandler(players.get(i)));
//                }
//            }
//        });

//        ServerTickEvents.START_SERVER_TICK.register((server) -> {
//            List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
//            for (int i = 0; i < players.size(); i++) {
//
//                handlers.add(new DimensionalTeleportationHandler(players.get(i)));
//            }
//        });

        EntitySleepEvents.START_SLEEPING.register((entity, pos) -> {
            if (entity.getWorld().isClient) {
                return;
            }

            System.out.println("Finding players in size " + handlers.size());
            for (int i = 0; i < handlers.size(); i++) {
                DimensionalTeleportationHandler handler = handlers.get(i);
                if (handler.user.equals(entity)) {
                    System.out.println("Found player!");
                    handler.state = handler.state.loadDimension(ModDimensions.POINT_NEMO);
                    System.out.println("Loaded Dimension");
                    return;

                }
            }
        });

        EntitySleepEvents.STOP_SLEEPING.register((entity, pos) ->{
            if (entity.getWorld().isClient) {
                return;
            }

            System.out.println("Finding players in size " + handlers.size());
            for (int i = 0; i < handlers.size(); i++) {
                DimensionalTeleportationHandler handler = handlers.get(i);
                if (handler.user.equals(entity)) {
                    System.out.println("Found player!");
                    handler.state = handler.state.teleportToDimension(ModDimensions.POINT_NEMO);
                    System.out.println("New Dimension");
                    return;

                }
            }
        });

//        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> {
//            if (player.getWorld().isClient) {
//                return true;
//            }
//
//            System.out.println("Finding players in size " + handlers.size());
//            for (int i = 0; i < handlers.size(); i++) {
//                DimensionalTeleportationHandler handler = handlers.get(i);
//                if (handler.user.equals(player)) {
//                    System.out.println("Found player!");
//                    System.out.println(handler.state.teleportToDimension(ModDimensions.POINT_NEMO));
//                    System.out.println("New Dimension");
//                    return true;
//
//                }
//            }
//
//            return true;
//        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            for (int i = 0; i < handlers.size(); i++) {
                DimensionalTeleportationHandler handler = handlers.get(i);
                if (handler.user.equals(player)) {
                    handler.state = handler.state.enterDimension();
                    return;
                }
            }
        });

//        ServerTickEvents.START_WORLD_TICK.register(serverWorld->{
//            for (int i = 0; i < handlers.size(); i++) {
//                DimensionalTeleportationHandler handler = handlers.get(i);
//                if (serverWorld.getPlayers().contains(handler.user)) {
//                    handler.state.teleportedIntoDimension(5, handler.user.getYaw(), handler.user.getPitch());
//                }
//            }
//        });




    }

    public static Identifier idOf(String path) {
        return Identifier.of(YumeCraft.MOD_ID, path);
    }

    private static BlockPos getFlattestGroundFromTop(ServerWorld world, BlockPos pos, int distance, int c){
        int min = getMaxCurveFromTop(world, pos, c);
        BlockPos minPos = pos;

        for (int x = -distance; x <= distance; x++) {
            for (int z = -distance; z <= distance; z++) {
                BlockPos valuePos = pos.add(x, 0, z);
                int value = getMaxCurveFromTop(world, valuePos, c);
                System.out.print(value + " ");
                if ((x != 0 || z != 0) && min > value) {
                    min = value;
                    minPos = valuePos;
                }
            }
            System.out.println();
        }

        return getHightestBlockPos(world, minPos);
    }

    private static int getMaxCurveFromTop(ServerWorld world, BlockPos pos, int r) {

        int max = getHightestBlockPos(world, pos).getY();
        int min = getHightestBlockPos(world, pos).getY();

        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                if (x != 0 || z != 0) {
                    int value = getHightestBlockPos(world, pos.add(x, 0, z)).getY();

                    max = Math.max(max, value);
                    min = Math.min(min, value);
                }
            }
        }

        return max - min;
    }

    private static BlockPos getHightestBlockPos(ServerWorld world, BlockPos pos) {
        return new BlockPos(pos.getX(), world.getTopY(Heightmap.Type.WORLD_SURFACE,
                pos.getX(), pos.getZ()), pos.getZ());
    }

    private State findWorldState(World world) {
        System.out.println("Finding world state...");
        System.out.println(world.getRegistryKey().getValue().getPath());
        if (world.getRegistryKey().getValue().getPath().equals("overworld")) {
            System.out.println("OverworldState");
            return new OverworldState();
        }

        return new UnknownDimensionState();
    }

    private State findWorldState() {
        return findWorldState(user.getWorld());
    }

    private abstract class State{

        public abstract State teleportToDimension(String dimId);

        public abstract State loadDimension(String dimId);

        public State enterDimension() {
            return findWorldState();
        }
    }

    private class OverworldState extends State {
        private OverworldState() {

        }

        @Override
        public State teleportToDimension(String dimId) {
            return this;
        }

        @Override
        public State loadDimension(String dimId) {
            State newState;

            if (dimId.equals(ModDimensions.POINT_NEMO)) {
                newState = new TeleportToSetTopState(new BlockPos(0,0,0), 12,
                        user.getYaw(), user.getPitch());
            }
            else {
                return this;
            }

            System.out.println("Loading Dimension");

            return newState.loadDimension(dimId);
        }
    }

    private class UnknownDimensionState extends State{

        private UnknownDimensionState(){}


        @Override
        public State teleportToDimension(String dimId) {
            return this;
        }

        @Override
        public State loadDimension(String dimId) {
            return this;
        }
    }

    private class TeleportToSetTopState extends State {

        private BlockPos setPos;
        private int distance;
        private float yaw, pitch;

        private TeleportToSetTopState(BlockPos setPos, int distance, float yaw, float pitch) {
            this.setPos = setPos;
            this.distance = distance;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        @Override
        public State teleportToDimension(String dimId) {

            MinecraftServer server = ((ServerWorld)user.getWorld()).getServer();
            RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, idOf(dimId));
            ServerWorld worldTo = server.getWorld(dimKey);

            System.out.println("trying to teleport...");

            if (worldTo == null || user.getWorld().getRegistryKey().equals(dimKey)) {
                return this;
            }

            Vec3d teleportedLoc = getFlattestGroundFromTop(worldTo, setPos, distance, 2)
                    .add(0,1,0).toCenterPos();

            System.out.println("Flattest Point: "+ teleportedLoc.y);

            TeleportTarget target = new TeleportTarget(worldTo,
                    teleportedLoc,
                    user.getVelocity(),
                    user.getYaw(),
                    user.getPitch(),
                    TeleportTarget.NO_OP);

            user.teleportTo(target);


            return findWorldState(worldTo);
        }

        @Override
        public State loadDimension(String dimId) {

            System.out.println("Trying to load");

            MinecraftServer server = ((ServerWorld)user.getWorld()).getServer();
            RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, idOf(dimId));
            ServerWorld world = server.getWorld(dimKey);

            if (world == null || user.getWorld().getRegistryKey().equals(dimKey)) {
                return this;
            }

            System.out.println("Can load!");

            world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(setPos), distance, setPos);

            return this;
        }
    }

}
