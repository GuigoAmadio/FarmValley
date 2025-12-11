package core;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

import entities.Player;
import entities.Crop;
import world.Farm;
import world.Tile;
import world.TileType;
import world.TimeSystem;
import types.CropType;
import types.CropQuality;
import systems.UIManager;
import systems.HarvestSystem;
import systems.Inventory;
import systems.GameStats;
import systems.LevelSystem;
import systems.EntityManager;
import systems.ShopSystem;
import systems.QuestSystem;
import systems.ParticleSystem;
import systems.WeatherSystem;
import systems.Minimap;
import entities.NPC;
import entities.Enemy;
import items.ItemType;
import java.util.List;

/**
 * Motor principal do jogo
 * Gerencia todos os sistemas e o loop de atualização
 */
public class GameEngine {
    // Sistemas principais
    private Farm farm;
    private Player player;
    private TimeSystem timeSystem;
    private UIManager uiManager;
    private HarvestSystem harvestSystem;
    
    // Novos sistemas
    private GameStats gameStats;
    private LevelSystem levelSystem;
    private EntityManager entityManager;
    private ShopSystem shopSystem;
    private QuestSystem questSystem;
    private ParticleSystem particleSystem;
    private WeatherSystem weatherSystem;
    private Minimap minimap;
    
    // Input
    private Set<KeyCode> pressedKeys;
    private boolean[] actionFlags;
    
    // Mensagens
    private String statusMessage;
    private long messageTime;
    
    // Movimento tile-por-tile mais fluido
    private long lastMoveTime;
    private static final long MOVE_DELAY = 100_000_000; // 100ms entre movimentos

    // Constantes de ações
    private static final int ACTION_TILL = 0;
    private static final int ACTION_PLANT = 1;
    private static final int ACTION_HARVEST = 2;
    private static final int ACTION_SLEEP = 3;
    private static final int ACTION_CHANGE_SEED = 4;
    private static final int ACTION_COLLECT_RESOURCE = 5;
    private static final int ACTION_SHOW_STATS = 6;
    private static final int ACTION_INTERACT = 7;
    private static final int ACTION_ATTACK = 8;
    private static final int ACTION_USE_POTION = 9;
    private static final int ACTION_OPEN_SHOP = 10;
    private static final int ACTION_SHOW_QUESTS = 11;

    public GameEngine() {
        // ============================================
        // CONFIGURAÇÃO DO MAPA - ALTERE AQUI!
        // ============================================
        // Pequeno: 50x50, Médio: 100x100, Grande: 200x200
        int mapWidth = 150;
        int mapHeight = 150;
        
        System.out.println("🎮 Iniciando Farm Valley...");
        System.out.println("🗺️  Tamanho do mapa: " + mapWidth + "x" + mapHeight + " (" + (mapWidth * mapHeight) + " tiles)");
        
        // Inicializar sistemas
        farm = new Farm(mapWidth, mapHeight);
        player = new Player(mapWidth / 2, mapHeight / 2);
        timeSystem = new TimeSystem();
        uiManager = new UIManager();
        harvestSystem = new HarvestSystem(farm, player);
        
        // Novos sistemas
        gameStats = new GameStats();
        levelSystem = new LevelSystem();
        entityManager = new EntityManager(farm);
        entityManager.generateEntities();
        shopSystem = new ShopSystem(player);
        questSystem = new QuestSystem();
        particleSystem = new ParticleSystem();
        weatherSystem = new WeatherSystem();
        minimap = new Minimap();
        
        pressedKeys = new HashSet<>();
        actionFlags = new boolean[12];  // Aumentado para 12 ações
        statusMessage = "Bem-vindo ao Farm Valley! WASD=mover, F=interagir, R=poção, J=quests";
        messageTime = System.currentTimeMillis();
        lastMoveTime = 0;
        
        System.out.println("✅ Jogo iniciado com sucesso!");
        System.out.println("📜 Missões ativas: " + questSystem.getActiveQuestCount());
        System.out.println("🌤️ Clima: " + weatherSystem.getWeatherName());
    }

    public void update() {
        // Verificar se jogador morreu
        if (player.isDead()) {
            handlePlayerDeath();
            return;
        }
        
        // Atualizar sistema de tempo
        timeSystem.update();
        
        // Atualizar clima
        weatherSystem.update();
        
        // Atualizar partículas
        particleSystem.update();
        
        // Atualizar dinheiro máximo nas estatísticas
        gameStats.updateHighestMoney(player.getMoney());
        
        // Atualizar NPCs e inimigos
        entityManager.update(player.getX(), player.getY());
        
        // Verificar ataques de inimigos
        checkEnemyAttacks();
        
        // Verificar missões prontas para resgatar
        checkQuestRewards();
        
        handleMovement();
        handleActions();
    }
    
    /**
     * Verifica se inimigos estão atacando o jogador
     */
    private void checkEnemyAttacks() {
        Enemy adjacentEnemy = entityManager.getAdjacentEnemy(player.getX(), player.getY());
        if (adjacentEnemy != null && adjacentEnemy.isAggressive() && adjacentEnemy.canAttack()) {
            int damage = adjacentEnemy.attack();
            player.takeDamage(damage); // Agora usa sistema de vida!
            
            // Efeito visual de dano
            particleSystem.spawnDamageEffect(player.getX(), player.getY());
            
            if (player.getHealth() <= 0) {
                showMessage("💀 Você foi derrotado por " + adjacentEnemy.getType().getName() + "!");
            } else {
                showMessage("⚔️ " + adjacentEnemy.getType().getName() + " atacou! -" + damage + " HP (Vida: " + player.getHealth() + ")");
            }
        }
    }
    
    /**
     * Gerencia morte do jogador
     */
    private void handlePlayerDeath() {
        // Respawn após 2 segundos
        if (System.currentTimeMillis() - messageTime > 2000) {
            int spawnX = farm.getWidth() / 2;
            int spawnY = farm.getHeight() / 2;
            player.respawn(spawnX, spawnY);
            showMessage("💫 Você renasceu! Perdeu 25% do dinheiro.");
        }
    }
    
    /**
     * Verifica e resgata recompensas de missões completas
     */
    private void checkQuestRewards() {
        List<QuestSystem.Quest> ready = questSystem.getQuestsReadyToClaim();
        for (QuestSystem.Quest quest : ready) {
            QuestSystem.QuestReward reward = questSystem.claimReward(quest.getId());
            if (reward != null) {
                player.earn(reward.money);
                levelSystem.addXP(reward.xp);
                showMessage("🎉 Missão '" + reward.questName + "' completa! +$" + reward.money + " +" + reward.xp + "XP");
            }
        }
    }

    private void handleMovement() {
        long currentTime = System.nanoTime();
        
        // Delay entre movimentos para controle melhor (mais rápido agora!)
        if (currentTime - lastMoveTime < MOVE_DELAY) {
            return;
        }
        
        int newX = player.getX();
        int newY = player.getY();
        int dx = 0, dy = 0;
        boolean moved = false;

        // MOVIMENTO DIAGONAL SUPORTADO! (removido else if)
        if (pressedKeys.contains(KeyCode.W) || pressedKeys.contains(KeyCode.UP)) {
            newY--;
            dy = -1;
            moved = true;
        }
        if (pressedKeys.contains(KeyCode.S) || pressedKeys.contains(KeyCode.DOWN)) {
            newY++;
            dy = 1;
            moved = true;
        }
        if (pressedKeys.contains(KeyCode.A) || pressedKeys.contains(KeyCode.LEFT)) {
            newX--;
            dx = -1;
            moved = true;
        }
        if (pressedKeys.contains(KeyCode.D) || pressedKeys.contains(KeyCode.RIGHT)) {
            newX++;
            dx = 1;
            moved = true;
        }

        if (moved) {
            // Atualizar direção (prioriza vertical para animação)
            player.updateDirection(dx, dy);
            
            // Verificar se há inimigo na posição destino
            boolean hasEnemy = entityManager.hasEnemyAt(newX, newY);
            
            // Tentar movimento diagonal ou simples (não pode andar em inimigos)
            if (farm.isWalkable(newX, newY) && !hasEnemy) {
                player.setX(newX);
                player.setY(newY);
                player.updateAnimation(); // Animar enquanto anda
                lastMoveTime = currentTime;
                gameStats.recordStep(); // Registrar passo
            } else if (dx != 0 && dy != 0) {
                // Se diagonal falhou, tentar movimento em X ou Y separadamente
                boolean hasEnemyX = entityManager.hasEnemyAt(newX, player.getY());
                boolean hasEnemyY = entityManager.hasEnemyAt(player.getX(), newY);
                
                if (farm.isWalkable(newX, player.getY()) && !hasEnemyX) {
                    player.setX(newX);
                    player.updateAnimation();
                    lastMoveTime = currentTime;
                } else if (farm.isWalkable(player.getX(), newY) && !hasEnemyY) {
                    player.setY(newY);
                    player.updateAnimation();
                    lastMoveTime = currentTime;
                }
            }
        } else {
            // Parado - resetar animação
            player.resetAnimation();
        }
    }

    private void handleActions() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Arar terra (T)
        if (actionFlags[ACTION_TILL]) {
            if (player.hasEnergy(5)) {
                Tile tile = farm.getTile(playerX, playerY);
                if (tile != null && tile.getType() == TileType.GRASS) {
                    farm.tillSoil(playerX, playerY);
                    player.useEnergy(5);
                    
                    // Estatísticas e XP
                    gameStats.recordTilePlowed();
                    if (levelSystem.onPlow()) {
                        showMessage("Terra arada! 🎉 LEVEL UP! Nível " + levelSystem.getLevel());
                    } else {
                        showMessage("Terra arada! (+" + 3 + " XP)");
                    }
                } else {
                    showMessage("Não posso arar aqui!");
                }
            } else {
                showMessage("Sem energia! Durma (Z)");
            }
            actionFlags[ACTION_TILL] = false;
        }

        // Plantar (P) - usando sementes do inventário
        if (actionFlags[ACTION_PLANT]) {
            CropType seedType = player.getSelectedSeed();
            
            // Determinar qual item de semente usar
            ItemType seedItem;
            if (seedType == CropType.TOMATO) {
                seedItem = ItemType.TOMATO_SEED;
            } else if (seedType == CropType.CORN) {
                seedItem = ItemType.CORN_SEED;
            } else {
                seedItem = ItemType.TOMATO_SEED; // fallback
            }
            
            // Verificar se tem semente no inventário
            if (player.getInventory().getItemCount(seedItem) <= 0) {
                showMessage("❌ Sem sementes de " + seedType.getName() + "! Compre na loja.");
            } else if (!player.hasEnergy(5)) {
                showMessage("Sem energia! Durma (Z)");
            } else {
                if (farm.plantCrop(playerX, playerY, seedType)) {
                    player.getInventory().removeItem(seedItem, 1); // Consumir semente
                    player.useEnergy(5);
                    
                    // Estatísticas e XP
                    gameStats.recordCropPlanted(seedType);
                    if (levelSystem.onPlant()) {
                        showMessage("🌱 " + seedType.getName() + " plantado! 🎉 LEVEL UP!");
                    } else {
                        showMessage("🌱 " + seedType.getName() + " plantado! (+5 XP)");
                    }
                } else {
                    showMessage("Are a terra primeiro (T)!");
                }
            }
            actionFlags[ACTION_PLANT] = false;
        }

        // Colher (H)
        if (actionFlags[ACTION_HARVEST]) {
            Crop harvested = farm.harvestCrop(playerX, playerY);
            if (harvested != null) {
                CropType cropType = harvested.getType();
                
                // Determinar qualidade baseada no nível
                CropQuality quality = CropQuality.determineQuality(levelSystem.getCropQualityBonus());
                
                // Calcular preço com bônus de nível e qualidade
                int basePrice = cropType.getSellPrice();
                int qualityPrice = quality.calculatePrice(basePrice);
                int finalPrice = levelSystem.calculateSellPrice(qualityPrice);
                
                player.earn(finalPrice);
                player.useEnergy(3);
                
                // Estatísticas e XP
                gameStats.recordCropHarvested(cropType);
                gameStats.recordMoneyEarned(finalPrice);
                
                // Atualizar missões
                questSystem.onCropHarvested();
                questSystem.onMoneyEarned(finalPrice);
                
                // Efeito visual de colheita
                particleSystem.spawnHarvestEffect(playerX, playerY);
                
                String qualityStr = quality != CropQuality.NORMAL ? " " + quality.getSymbol() + " " + quality.getName() : "";
                if (levelSystem.onHarvest()) {
                    particleSystem.spawnLevelUpEffect(playerX, playerY);
                    showMessage(cropType.getName() + qualityStr + " colhido! +$" + finalPrice + " 🎉 LEVEL UP!");
                } else {
                    showMessage(cropType.getName() + qualityStr + " colhido! (+$" + finalPrice + ", +15 XP)");
                }
            } else {
                Tile tile = farm.getTile(playerX, playerY);
                if (tile != null && tile.hasCrop()) {
                    showMessage("Planta ainda não está madura!");
                } else {
                    showMessage("Nada para colher aqui!");
                }
            }
            actionFlags[ACTION_HARVEST] = false;
        }

        // Dormir (Z)
        if (actionFlags[ACTION_SLEEP]) {
            sleep();
            actionFlags[ACTION_SLEEP] = false;
        }

        // Trocar semente (C)
        if (actionFlags[ACTION_CHANGE_SEED]) {
            changeSeed();
            actionFlags[ACTION_CHANGE_SEED] = false;
        }
        
        // Coletar recursos (E ou SPACE)
        if (actionFlags[ACTION_COLLECT_RESOURCE]) {
            collectResource();
            actionFlags[ACTION_COLLECT_RESOURCE] = false;
        }
        
        // Mostrar estatísticas (Tab)
        if (actionFlags[ACTION_SHOW_STATS]) {
            showStats();
            actionFlags[ACTION_SHOW_STATS] = false;
        }
        
        // Interagir com NPC (F)
        if (actionFlags[ACTION_INTERACT]) {
            interactWithNPC();
            actionFlags[ACTION_INTERACT] = false;
        }
        
        // Atacar inimigo (Q)
        if (actionFlags[ACTION_ATTACK]) {
            attackEnemy();
            actionFlags[ACTION_ATTACK] = false;
        }
        
        // Usar poção (R)
        if (actionFlags[ACTION_USE_POTION]) {
            usePotion();
            actionFlags[ACTION_USE_POTION] = false;
        }
        
        // Mostrar missões (J)
        if (actionFlags[ACTION_SHOW_QUESTS]) {
            showQuests();
            actionFlags[ACTION_SHOW_QUESTS] = false;
        }
    }
    
    /**
     * Usa poção (prioriza vida se estiver baixa)
     */
    private void usePotion() {
        // Priorizar poção de vida se estiver com vida baixa
        if (player.getHealthPercent() < 0.5 && player.useHealthPotion()) {
            particleSystem.spawnHealEffect(player.getX(), player.getY());
            showMessage("❤️ Usou Poção de Vida! Vida restaurada.");
            return;
        }
        
        // Tentar poção de energia
        if (player.useEnergyPotion()) {
            particleSystem.spawnHealEffect(player.getX(), player.getY());
            showMessage("⚡ Usou Poção de Energia! +50 energia");
            return;
        }
        
        // Tentar poção de vida mesmo se não prioritária
        if (player.useHealthPotion()) {
            particleSystem.spawnHealEffect(player.getX(), player.getY());
            showMessage("❤️ Usou Poção de Vida! Vida restaurada.");
            return;
        }
        
        showMessage("Você não tem poções!");
    }
    
    /**
     * Mostra missões ativas
     */
    private void showQuests() {
        List<QuestSystem.Quest> quests = questSystem.getActiveQuests();
        if (quests.isEmpty()) {
            showMessage("📜 Nenhuma missão ativa!");
            return;
        }
        
        StringBuilder sb = new StringBuilder("📜 Missões: ");
        int shown = 0;
        for (QuestSystem.Quest quest : quests) {
            if (shown >= 2) break; // Mostrar apenas 2 para caber na tela
            if (shown > 0) sb.append(" | ");
            sb.append(quest.getName()).append(" (").append(quest.getProgressString()).append(")");
            if (quest.isCompleted()) sb.append(" ✅");
            shown++;
        }
        
        showMessage(sb.toString());
    }
    
    /**
     * Interagir com NPC próximo
     */
    private void interactWithNPC() {
        NPC npc = entityManager.getAdjacentNPC(player.getX(), player.getY());
        if (npc != null) {
            // Se for comerciante, abrir loja
            if (npc.canTrade()) {
                openShop(npc);
            } else {
                String dialogue = npc.getNextDialogue();
                showMessage("💬 " + npc.getName() + ": \"" + dialogue + "\"");
            }
        } else {
            showMessage("Ninguém por perto para conversar.");
        }
    }
    
    /**
     * Abre a loja com um comerciante
     */
    private void openShop(NPC merchant) {
        uiManager.openShop(merchant.getName());
        showMessage("🛒 Loja aberta! TAB=trocar aba, 1-6=comprar/vender, ESC=fechar");
    }
    
    /**
     * Processa compra de item na loja
     */
    public void shopBuyItem(int index) {
        if (!uiManager.isShopOpen() || uiManager.getShopSelectedTab() != 0) return;
        
        ItemType[] buyItems = {ItemType.TOMATO_SEED, ItemType.CORN_SEED, 
                               ItemType.HEALTH_POTION, ItemType.ENERGY_POTION};
        int[] prices = {15, 20, 50, 30};
        
        if (index >= 0 && index < buyItems.length) {
            if (player.canAfford(prices[index])) {
                player.spend(prices[index]);
                player.getInventory().addItem(buyItems[index], 1);
                showMessage("✅ Comprou " + buyItems[index].getDisplayName() + " por $" + prices[index]);
            } else {
                showMessage("❌ Dinheiro insuficiente!");
            }
        }
    }
    
    /**
     * Processa venda de item na loja
     */
    public void shopSellItem(int index) {
        if (!uiManager.isShopOpen() || uiManager.getShopSelectedTab() != 1) return;
        
        ItemType[] sellItems = {ItemType.TOMATO, ItemType.CORN, ItemType.WOOD, ItemType.STONE};
        
        if (index >= 0 && index < sellItems.length) {
            ItemType type = sellItems[index];
            int count = player.getInventory().getItemCount(type);
            if (count > 0) {
                int price = (int)(type.getValue() * 0.7);
                player.getInventory().removeItem(type, 1);
                player.earn(price);
                showMessage("✅ Vendeu " + type.getDisplayName() + " por $" + price);
                questSystem.onMoneyEarned(price);
            } else {
                showMessage("❌ Você não tem " + type.getDisplayName() + "!");
            }
        }
    }
    
    /**
     * Atacar inimigo próximo
     */
    private void attackEnemy() {
        if (!player.hasEnergy(10)) {
            showMessage("Sem energia para atacar!");
            return;
        }
        
        Enemy enemy = entityManager.getAdjacentEnemy(player.getX(), player.getY());
        if (enemy != null && !enemy.isDead()) {
            int damage = player.getAttackDamage() + levelSystem.getLevel() * 2; // Dano base + bônus por nível
            entityManager.attackEnemy(enemy, damage);
            player.useEnergy(10);
            
            // Efeito visual de ataque
            particleSystem.spawnAttackEffect(enemy.getX(), enemy.getY(), player.getFacing().name());
            particleSystem.spawnDamageEffect(enemy.getX(), enemy.getY());
            
            if (enemy.isDead()) {
                int xpReward = enemy.getMaxHealth() / 2;
                levelSystem.addXP(xpReward);
                
                // Atualizar missões de combate
                questSystem.onEnemyKilled();
                
                // Efeito de vitória
                particleSystem.spawnHarvestEffect(enemy.getX(), enemy.getY());
                
                showMessage("⚔️ " + enemy.getType().getName() + " derrotado! +" + xpReward + " XP");
            } else {
                showMessage("⚔️ Atacou " + enemy.getType().getName() + "! (-" + damage + " HP, restam " + enemy.getHealth() + ")");
            }
        } else {
            showMessage("Nenhum inimigo por perto.");
        }
    }

    private void sleep() {
        // Penalidade se muito tarde
        if (timeSystem.isVeryLate()) {
            player.useEnergy(20); // Penalidade de energia
            showMessage("Você dormiu muito tarde... -20 energia");
        }
        
        player.sleep();
        farm.growAllCrops();
        timeSystem.nextDay();
        gameStats.recordDayPassed();
        questSystem.onDayPassed();
        
        showMessage("Bom dia! " + timeSystem.getShortDateString() + " | Nível " + levelSystem.getLevel());
    }

    private void changeSeed() {
        // Apenas TOMATO e CORN disponíveis
        CropType current = player.getSelectedSeed();
        
        if (current == CropType.TOMATO) {
            player.setSelectedSeed(CropType.CORN);
            showMessage("🌱 Semente: Milho");
        } else {
            player.setSelectedSeed(CropType.TOMATO);
            showMessage("🌱 Semente: Tomate");
        }
    }
    
    private void collectResource() {
        String result = harvestSystem.attemptHarvest();
        int px = player.getX();
        int py = player.getY();
        
        // Registrar nas estatísticas e missões se coletou algo
        if (result.contains("madeira")) {
            gameStats.recordWoodCollected(1);
            levelSystem.onChopTree();
            questSystem.onWoodCollected(1);
            particleSystem.spawnResourceEffect(px, py, "wood");
        } else if (result.contains("pedra")) {
            gameStats.recordStoneCollected(1);
            levelSystem.onMineRock();
            questSystem.onStoneCollected(1);
            particleSystem.spawnResourceEffect(px, py, "stone");
        } else if (result.contains("fibra")) {
            gameStats.recordFiberCollected(1);
            particleSystem.spawnResourceEffect(px, py, "fiber");
        }
        
        showMessage(result);
    }
    
    private void onDayPassed() {
        questSystem.onDayPassed();
    }
    
    private void showStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 ");
        sb.append(levelSystem.getProgressString());
        sb.append(" | Plantados: ").append(gameStats.getTotalCropsPlanted());
        sb.append(" | Colhidos: ").append(gameStats.getTotalCropsHarvested());
        sb.append(" | Lucro: $").append(gameStats.getNetProfit());
        showMessage(sb.toString());
    }
    
    private void showMessage(String message) {
        this.statusMessage = message;
        this.messageTime = System.currentTimeMillis();
    }
    
    public String getStatusMessage() {
        // Mensagem desaparece após 3 segundos
        if (System.currentTimeMillis() - messageTime > 3000) {
            return "";
        }
        return statusMessage;
    }

    public void keyPressed(KeyCode code) {
        pressedKeys.add(code);

        // UI - Inventário
        if (code == KeyCode.I) {
            if (uiManager.isShopOpen()) {
                uiManager.closeShop();
            }
            uiManager.toggleInventory();
            return;
        }
        
        // UI - Loja
        if (uiManager.isShopOpen()) {
            if (code == KeyCode.ESCAPE) {
                uiManager.closeShop();
                return;
            }
            if (code == KeyCode.TAB) {
                uiManager.toggleShopTab();
                return;
            }
            // Comprar/Vender com números
            if (code == KeyCode.DIGIT1) { if (uiManager.getShopSelectedTab() == 0) shopBuyItem(0); else shopSellItem(0); }
            if (code == KeyCode.DIGIT2) { if (uiManager.getShopSelectedTab() == 0) shopBuyItem(1); else shopSellItem(1); }
            if (code == KeyCode.DIGIT3) { if (uiManager.getShopSelectedTab() == 0) shopBuyItem(2); else shopSellItem(2); }
            if (code == KeyCode.DIGIT4) { if (uiManager.getShopSelectedTab() == 0) shopBuyItem(3); else shopSellItem(3); }
            if (code == KeyCode.DIGIT5) { if (uiManager.getShopSelectedTab() == 0) shopBuyItem(4); else shopSellItem(4); }
            if (code == KeyCode.DIGIT6) { if (uiManager.getShopSelectedTab() == 0) shopBuyItem(5); else shopSellItem(5); }
            return;
        }
        
        // Se inventário estiver aberto, não processar ações de jogo
        if (uiManager.isInventoryOpen()) {
            if (code == KeyCode.ESCAPE) {
                uiManager.closeInventory();
                return;
            }
            // Teclas numéricas para selecionar slots (1-6)
            if (code == KeyCode.DIGIT1) player.getInventory().setSelectedSlot(0);
            if (code == KeyCode.DIGIT2) player.getInventory().setSelectedSlot(1);
            if (code == KeyCode.DIGIT3) player.getInventory().setSelectedSlot(2);
            if (code == KeyCode.DIGIT4) player.getInventory().setSelectedSlot(3);
            if (code == KeyCode.DIGIT5) player.getInventory().setSelectedSlot(4);
            if (code == KeyCode.DIGIT6) player.getInventory().setSelectedSlot(5);
            return;
        }

        // Teclas numéricas para selecionar slots da hotbar
        if (code == KeyCode.DIGIT1) player.getInventory().setSelectedSlot(0);
        if (code == KeyCode.DIGIT2) player.getInventory().setSelectedSlot(1);
        if (code == KeyCode.DIGIT3) player.getInventory().setSelectedSlot(2);
        if (code == KeyCode.DIGIT4) player.getInventory().setSelectedSlot(3);
        if (code == KeyCode.DIGIT5) player.getInventory().setSelectedSlot(4);
        if (code == KeyCode.DIGIT6) player.getInventory().setSelectedSlot(5);
        
        // Ações instantâneas de jogo
        if (code == KeyCode.T) actionFlags[ACTION_TILL] = true;
        if (code == KeyCode.P) actionFlags[ACTION_PLANT] = true;
        if (code == KeyCode.H) actionFlags[ACTION_HARVEST] = true;
        if (code == KeyCode.Z) actionFlags[ACTION_SLEEP] = true;
        if (code == KeyCode.C) actionFlags[ACTION_CHANGE_SEED] = true;
        if (code == KeyCode.E || code == KeyCode.SPACE) actionFlags[ACTION_COLLECT_RESOURCE] = true;
        if (code == KeyCode.TAB) actionFlags[ACTION_SHOW_STATS] = true;
        if (code == KeyCode.F) actionFlags[ACTION_INTERACT] = true;
        if (code == KeyCode.Q) actionFlags[ACTION_ATTACK] = true;
        if (code == KeyCode.R) actionFlags[ACTION_USE_POTION] = true;
        if (code == KeyCode.J) actionFlags[ACTION_SHOW_QUESTS] = true;
    }

    public void keyReleased(KeyCode code) {
        pressedKeys.remove(code);
    }

    // ===== GETTERS =====
    
    public Farm getFarm() { return farm; }
    public Player getPlayer() { return player; }
    public TimeSystem getTimeSystem() { return timeSystem; }
    public UIManager getUIManager() { return uiManager; }
    public GameStats getGameStats() { return gameStats; }
    public LevelSystem getLevelSystem() { return levelSystem; }
    public EntityManager getEntityManager() { return entityManager; }
    public ShopSystem getShopSystem() { return shopSystem; }
    public QuestSystem getQuestSystem() { return questSystem; }
    public ParticleSystem getParticleSystem() { return particleSystem; }
    public WeatherSystem getWeatherSystem() { return weatherSystem; }
    public Minimap getMinimap() { return minimap; }
}

