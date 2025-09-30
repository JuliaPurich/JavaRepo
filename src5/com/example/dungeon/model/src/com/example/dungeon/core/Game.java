package src.com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
        });
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));

        // КОМАНДА MOVE
        commands.put("move", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите направление: move <north|south|east|west>");
            }

            String direction = a.get(0).toLowerCase();
            Room currentRoom = ctx.getCurrent();
            Room nextRoom = currentRoom.getNeighbors().get(direction);

            if (nextRoom == null) {
                throw new InvalidCommandException("Нет пути в направлении: " + direction);
            }

            ctx.setCurrent(nextRoom);
            System.out.println("Вы перешли в: " + nextRoom.getName());
            System.out.println(nextRoom.describe());
        });

        // КОМАНДА TAKE
        commands.put("take", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите предмет: take <item name>");
            }

            String itemName = String.join(" ", a);
            Room currentRoom = ctx.getCurrent();
            Player player = ctx.getPlayer();

            Item foundItem = null;
            for (Item item : currentRoom.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    foundItem = item;
                    break;
                }
            }

            if (foundItem == null) {
                throw new InvalidCommandException("Предмет '" + itemName + "' не найден в комнате");
            }

            currentRoom.getItems().remove(foundItem);
            player.getInventory().add(foundItem);
            System.out.println("Взято: " + foundItem.getName());
        });

        // КОМАНДА INVENTORY
        commands.put("inventory", (ctx, a) -> {
            Player player = ctx.getPlayer();
            List<Item> inventory = player.getInventory();

            if (inventory.isEmpty()) {
                System.out.println("Инвентарь пуст");
                return;
            }

            inventory.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getClass().getSimpleName(),
                            Collectors.toList()
                    ))
                    .forEach((type, items) -> {
                        String itemNames = items.stream()
                                .map(Item::getName)
                                .sorted()
                                .collect(Collectors.joining(", "));
                        System.out.println("- " + type + " (" + items.size() + "): " + itemNames);
                    });
        });

        // КОМАНДА USE
        commands.put("use", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите предмет: use <item name>");
            }

            String itemName = String.join(" ", a);
            Player player = ctx.getPlayer();

            Item foundItem = null;
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    foundItem = item;
                    break;
                }
            }

            if (foundItem == null) {
                throw new InvalidCommandException("Предмет '" + itemName + "' не найден в инвентаре");
            }

            foundItem.apply(ctx);
        });

        // КОМАНДА FIGHT
        commands.put("fight", (ctx, a) -> {
            Room currentRoom = ctx.getCurrent();
            Monster monster = currentRoom.getMonster();
            Player player = ctx.getPlayer();

            if (monster == null) {
                throw new InvalidCommandException("В этой комнате нет монстров для битвы");
            }

            System.out.println("Начинается битва с " + monster.getName() + "!");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
                while (player.getHp() > 0 && monster.getHp() > 0) {
                    System.out.println("\n--- Ваш ход ---");
                    System.out.println("Ваше HP: " + player.getHp() + ", HP монстра: " + monster.getHp());
                    System.out.println("Команды: attack, use <item>, run");
                    System.out.print("бой> ");

                    String input = in.readLine();
                    if (input == null) break;

                    String[] parts = input.trim().split("\\s+");
                    String command = parts[0].toLowerCase();

                    switch (command) {
                        case "attack":
                            int playerDamage = player.getAttack();
                            monster.setHp(monster.getHp() - playerDamage);
                            System.out.println("Вы бьёте " + monster.getName() + " на " + playerDamage +
                                    ". HP монстра: " + monster.getHp());
                            break;

                        case "use":
                            if (parts.length < 2) {
                                System.out.println("Укажите предмет для использования");
                                continue;
                            }
                            String useItemName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));

                            Item useItem = null;
                            for (Item item : player.getInventory()) {
                                if (item.getName().equalsIgnoreCase(useItemName)) {
                                    useItem = item;
                                    break;
                                }
                            }

                            if (useItem != null) {
                                useItem.apply(ctx);
                                player.getInventory().remove(useItem);
                            } else {
                                System.out.println("Предмет не найден");
                            }
                            continue;

                        case "run":
                            System.out.println("Вы сбежали из битвы!");
                            return;

                        default:
                            System.out.println("Неизвестная команда в бою");
                            continue;
                    }

                    if (monster.getHp() <= 0) {
                        System.out.println("Вы победили " + monster.getName() + "!");
                        currentRoom.getItems().add(new Potion("Зелье здоровья", 10));
                        currentRoom.setMonster(null);
                        System.out.println("Монстр выронил: Зелье здоровья");
                        ctx.addScore(50);
                        return;
                    }

                    System.out.println("\n--- Ход монстра ---");
                    int monsterDamage = monster.getLevel();
                    player.setHp(player.getHp() - monsterDamage);
                    System.out.println(monster.getName() + " отвечает на " + monsterDamage +
                            ". Ваше HP: " + player.getHp());

                    if (player.getHp() <= 0) {
                        System.out.println("Вы погибли! Игра окончена.");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Ошибка ввода во время боя", e);
            }
        });

        commands.put("save", (ctx, a) -> SaveLoad.save(ctx));
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока!");
            System.exit(0);
        });
    }

    private void bootstrapWorld() {
        Player hero = new Player("Герой", 20, 5);
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);

        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 1, 8));

        state.setCurrent(square);
    }

    public void run() {
        System.out.println("DungeonMini (РЕАЛИЗОВАНО). 'help' — команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> parts = Arrays.asList(line.split("\\s+"));
                String cmd = parts.get(0).toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}