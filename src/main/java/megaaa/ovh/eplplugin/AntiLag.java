package megaaa.ovh.eplplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AntiLag implements Listener {
    HashMap<Player, Animals> selectedAnimal = new HashMap<>();
    HashMap<Animals, Long> breedTimer = new HashMap<>();
    HashMap<Animals, Integer> breedCount = new HashMap<>();
    HashMap<Animals, Integer> breedMax = new HashMap<>();

    Plugin plugin = EplPlugin.getPlugin();

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        Entity dead = e.getEntity();
        if (!(dead instanceof Animals)) { return; }
        Animals animal = (Animals) dead;
        Integer animalStack = getAnimalStack(animal);
        if (!(animalStack > 1)) { return; }

        String[] animalNameSplit = String.valueOf(animal.getClass()).split("Craft");
        String animalName = animalNameSplit[animalNameSplit.length - 1];
        LivingEntity entity = (LivingEntity) Bukkit.getWorld("world").spawnEntity(animal.getLocation(), animal.getType());
        entity.setCustomName((animalStack-1) + "x " + animalName);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        RayTraceResult res = Bukkit.getWorld("world").rayTraceEntities(
                player.getEyeLocation().add(player.getLocation().getDirection()), player.getEyeLocation().getDirection(), 4.5, entity -> !entity.getUniqueId().equals(player.getUniqueId())
        );

        if (res != null) {return;}

        ItemStack inHand = player.getInventory().getItemInMainHand();
        if (inHand.getType() == Material.AIR) {return;}
        if (!(inHand.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Mob merger"))) { return; }

        player.sendMessage(ChatColor.RED + "Vous avez désélectionné le mob à merge!");
        selectedAnimal.put(player, null);
    }

    @EventHandler()
    public void onPlayerInteract(final PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();

        if (!(e.getRightClicked() instanceof Animals)) { return; }
        Animals animal = (Animals) e.getRightClicked();

        Integer animalStack = getAnimalStack(animal);
        ItemStack inHand = player.getInventory().getItemInMainHand();

        if (inHand.getType() == Material.NAME_TAG && (animalStack > 1 || inHand.getItemMeta().getDisplayName().contains("x "))) {
            String oldName = e.getRightClicked().getCustomName();
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getRightClicked().setCustomName(oldName);
                }
            }.runTaskLater(plugin, 1);
            return;
        }

        String[] animalNameSplit = String.valueOf(animal.getClass()).split("Craft");
        String animalName = animalNameSplit[animalNameSplit.length - 1];

        if (!(animalName.equals("Cow") || animalName.equals("Chicken") || animalName.equals("Sheep") || animalName.equals("Pig"))) { return; }

        // Si on a le bon élément en main pour le bon animal et que c'est un animal stack
        // On check si l'animal est déjà nourri récemment, si oui on compare le breedCount et breedMax
        // Sinon on initialise breedCount, breedMax et on reset le compteur
        if (inHand.getType() == Material.WHEAT && animalName.equals("Cow") && animalStack > 1) {
            e.setCancelled(true);
            reproduceAnimals(animal, Material.WHEAT, player);
            return;
        } else if (inHand.getType() == Material.WHEAT && animalName.equals("Sheep") && animalStack > 1) {
            e.setCancelled(true);
            reproduceAnimals(animal, Material.WHEAT, player);
            return;
        } else if ((inHand.getType() == Material.WHEAT_SEEDS || inHand.getType() == Material.MELON_SEEDS || inHand.getType() == Material.PUMPKIN_SEEDS || inHand.getType() == Material.BEETROOT_SEEDS) && animalName.equals("Chicken") && animalStack > 1) {
            e.setCancelled(true);
            reproduceAnimals(animal, inHand.getType(), player);
            return;
        } else if ((inHand.getType() == Material.POTATO || inHand.getType() == Material.CARROT || inHand.getType() == Material.BEETROOT) && animalName.equals("Pig") && animalStack > 1) {
            e.setCancelled(true);
            reproduceAnimals(animal, inHand.getType(), player);
            return;
        }

        if (inHand.getType() != Material.STICK) {return;}
        if (!(inHand.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Mob merger"))) { return; }

        Animals selected = getSelectedAnimal(player);
        if (animal.equals(selected)) { return; }
        if (selected == null) {
            selectedAnimal.put(player, animal);
            player.sendMessage(ChatColor.GREEN + "Vous avez sélectionné le mob à merge, clickez sur un second du même type pour les combiner!");
        } else if (selected.getClass() == animal.getClass()) {
            Integer new_stack = getAnimalStack(selected) + animalStack;
            animal.setCustomName(new_stack + "x " + animalName);
            player.sendMessage(ChatColor.GREEN + "Les animaux ont été combinés!");
            selectedAnimal.remove(player);
            selected.remove();
        } else {
            selectedAnimal.put(player, animal);
        }
    }

    public void reproduceAnimals(Animals animal, Material food, Player player) {
        Date date = new Date();
        long msec = date.getTime();
        Integer animalStack = getAnimalStack(animal);
        String[] animalNameSplit = String.valueOf(animal.getClass()).split("Craft");
        String animalName = animalNameSplit[animalNameSplit.length - 1];

        if (breedTimer.containsKey(animal) && msec < breedTimer.get(animal) + 300*1000) {
            if (getBreedCount(animal) < getMaxBreed(animal)) {
                boolean consumed = consumeItem(player, 2, food);
                if (!consumed) {
                    player.sendMessage(ChatColor.RED + "Vous avez besoin de 2 items pour reproduire");
                    return;
                }
                animal.setCustomName((animalStack+1) + "x " + animalName);
                breedCount.put(animal, getBreedCount(animal)+1);
            } else {
                long tempsRestant = breedTimer.get(animal) + 300*1000 - msec;
                player.sendMessage(ChatColor.RED + "Cet animal s'est déjà trop reproduit! Temps restant: " + tempsRestant/1000 + "s.");
            }
            return;
        }

        boolean consumed = consumeItem(player, 2, food);
        if (!consumed) {
            player.sendMessage(ChatColor.RED + "Vous avez besoin de 2 items pour reproduire");
            return;
        }
        animal.setCustomName((animalStack+1) + "x " + animalName);
        breedTimer.put(animal, msec);
        breedCount.put(animal, 1);
        breedMax.put(animal, animalStack / 2);
    }

    public Animals getSelectedAnimal(Player player) {
        if (selectedAnimal.containsKey(player)) {
            return selectedAnimal.get(player);
        }
        return null;
    }

    public Integer getBreedCount(Animals animal) {
        if (breedCount.containsKey(animal)) {
            return breedCount.get(animal);
        }
        return 1;
    }

    public Integer getMaxBreed(Animals animal) {
        if (breedMax.containsKey(animal)) {
            return breedMax.get(animal);
        }
        return 1;
    }

    public Integer getAnimalStack(Animals animal) {
        String name = animal.getCustomName();
        if (name == null) { return 1; }
        if (name.indexOf('x') == -1) { return 1; }

        String num = name.split("x")[0];
        if (!isNumeric(num)) { return 1; }

        return Integer.parseInt(num);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean consumeItem(Player player, int count, Material mat) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;
    }
}
