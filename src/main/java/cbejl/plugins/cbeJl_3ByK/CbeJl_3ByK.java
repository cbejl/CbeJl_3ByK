package cbejl.plugins.cbeJl_3ByK;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CbeJl_3ByK extends JavaPlugin implements Listener {
    private List<String> nametags = new ArrayList<>();
    private List<String> blacklist = new ArrayList<>();
    private List<String> unmuteItems = new ArrayList<>();
    private boolean cancelRenameEvent;

    private final LiteralCommandNode<CommandSourceStack> root = Commands
            .literal("cbejlzvuk")
            .requires(sender -> sender.getSender().isOp() || sender.getSender().hasPermission("cbejl_3byk.reload"))
            .then(
                    Commands.literal("reload")
                            .executes((ctx) -> {
                                init();
                                if (ctx.getSource().getExecutor() != null)
                                    ctx.getSource().getExecutor().sendMessage(
                                            Component.text("Конфиг ПОЖИРАТЕЛЯ ЗВУКОВ перезагружен!")
                                                    .color(TextColor.color(0, 240, 0))
                                    );
                                return 1;
                            })
            )
            .build();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        init();
        getServer().getPluginManager().registerEvents(this, this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(root));
    }

    public void init() {
        nametags = getConfig().getStringList("mute_nametags");
        unmuteItems = getConfig().getStringList("unmute_items");
        blacklist = getConfig().getStringList("mute_blacklist");
        cancelRenameEvent = getConfig().getBoolean("cancel_rename_event");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerNameEntity(PlayerNameEntityEvent event) {
        if (
                !(event.getName() instanceof TextComponent name)
                || !nametags.contains(name.content())
                || blacklist.contains(event.getEntity().getType().getKey().toString())
                || event.getEntity().isSilent()
        ) return;

        var entity = event.getEntity();

        entity.setSilent(true);

        if (cancelRenameEvent) {
            var oldName = entity.customName();
            //этот колхоз существует потому что моджанги долбаебы
            getServer().getScheduler().runTaskLater(this, task ->
                    entity.customName(oldName), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (
                !event.getRightClicked().isSilent()
                || !unmuteItems.contains(event.getPlayer().getInventory().getItem(event.getHand()).getType().getKey().toString())
        ) return;

        event.getRightClicked().setSilent(false);
        if (!cancelRenameEvent) event.getRightClicked().customName(null);
    }
}