# 🔄 ATUALIZAÇÃO DO CÓDIGO - MUDANÇAS NECESSÁRIAS

Após a reorganização, algumas mudanças precisam ser feitas no código.

---

## 📝 MUDANÇAS OBRIGATÓRIAS:

### **1. SpriteLoader.java** - Atualizar Caminhos

**Localização:** `src/utils/SpriteLoader.java`

#### **ANTES:**

```java
private static final String SPRITES_FOLDER = "sprites/";
```

#### **DEPOIS:**

```java
private static final String SPRITES_FOLDER = "assets/sprites/";
```

#### **Atualizar Sprites Inner Class:**

**ANTES:**

```java
public static class Sprites {
    // Player
    public static final String PLAYER_DOWN_1 = "player_down_1.png";
    public static final String PLAYER_DOWN_2 = "player_down_2.png";
    // ... etc
}
```

**DEPOIS:**

```java
public static class Sprites {
    // Player
    public static final String PLAYER_DOWN_1 = "player/player_down_1.png";
    public static final String PLAYER_DOWN_2 = "player/player_down_2.png";
    public static final String PLAYER_UP_1 = "player/player_up_1.png";
    public static final String PLAYER_UP_2 = "player/player_up_2.png";
    public static final String PLAYER_LEFT_1 = "player/player_left_1.png";
    public static final String PLAYER_LEFT_2 = "player/player_left_2.png";
    public static final String PLAYER_RIGHT_1 = "player/player_right_1.png";
    public static final String PLAYER_RIGHT_2 = "player/player_right_2.png";

    // Tiles
    public static final String GRASS = "tiles/grass.png";
    public static final String DIRT = "tiles/dirt.png";
    public static final String WATER = "tiles/water.png";
    public static final String STONE = "tiles/stone.png";

    // Icons
    public static final String ICON_AXE = "icons/axe.png";
    public static final String ICON_PICKAXE = "icons/pickaxe.png";
    public static final String ICON_HOE = "icons/hoe.png";
    public static final String ICON_WHEAT = "icons/wheat.png";
    public static final String ICON_WHEAT_SEED = "icons/wheat_seed.png";
    public static final String ICON_TOMATO = "icons/tomato.png";
    public static final String ICON_TOMATO_SEED = "icons/tomato_seed.png";
    public static final String ICON_WOOD = "icons/wood.png";
    public static final String ICON_STONE = "icons/stone.png";
    public static final String ICON_FIBER = "icons/fiber.png";
    public static final String ICON_COIN = "icons/coin.png";
}
```

---

### **2. DecorationType.java** - Atualizar Caminhos de Sprites

**Localização:** `src/types/DecorationType.java`

#### **ATUALIZAR TODOS OS ENUMS:**

**ANTES:**

```java
TREE1("trees/Tree1.png", false, 3, 64, 96),
BUSH_SIMPLE1("bushes/Bush_simple1_1.png", true, 1, 64, 64),
RUINS1("ruins/Brown_ruins1.png", false, 3, 64, 96),
```

**DEPOIS:**

```java
TREE1("decorations/trees/Tree1.png", false, 3, 64, 96),
BUSH_SIMPLE1("decorations/bushes/Bush_simple1_1.png", true, 1, 64, 64),
RUINS1("decorations/ruins/Brown_ruins1.png", false, 3, 64, 96),
```

**Lista completa de mudanças:**

```java
// Árvores
TREE1("decorations/trees/Tree1.png", false, 3, 64, 96),
TREE2("decorations/trees/Tree2.png", false, 3, 64, 96),
TREE3("decorations/trees/Tree3.png", false, 3, 64, 96),
AUTUMN_TREE1("decorations/trees/Autumn_tree1.png", false, 3, 64, 96),
AUTUMN_TREE2("decorations/trees/Autumn_tree2.png", false, 3, 64, 96),
FRUIT_TREE1("decorations/trees/Fruit_tree1.png", false, 3, 64, 96),
FRUIT_TREE2("decorations/trees/Fruit_tree2.png", false, 3, 64, 96),
FRUIT_TREE3("decorations/trees/Fruit_tree3.png", false, 3, 64, 96),
PALM_TREE1("decorations/trees/Palm_tree1_1.png", false, 3, 64, 96),
PALM_TREE2("decorations/trees/Palm_tree2_1.png", false, 3, 64, 96),

// Arbustos
BUSH_SIMPLE1("decorations/bushes/Bush_simple1_1.png", true, 1, 64, 64),
BUSH_SIMPLE2("decorations/bushes/Bush_simple1_2.png", true, 1, 64, 64),
BUSH_SIMPLE3("decorations/bushes/Bush_simple2_1.png", true, 1, 64, 64),
BUSH_BLUE("decorations/bushes/Bush_blue_flowers1.png", true, 1, 64, 64),
BUSH_ORANGE("decorations/bushes/Bush_orange_flowers1.png", true, 1, 64, 64),
BUSH_PINK("decorations/bushes/Bush_pink_flowers1.png", true, 1, 64, 64),
BUSH_RED("decorations/bushes/Bush_red_flowers1.png", true, 1, 64, 64),
FERN1("decorations/bushes/Fern1_1.png", true, 1, 64, 64),
FERN2("decorations/bushes/Fern2_1.png", true, 1, 64, 64),

// Ruínas
RUINS1("decorations/ruins/Brown_ruins1.png", false, 3, 64, 96),
RUINS2("decorations/ruins/Brown_ruins2.png", false, 3, 64, 96),
RUINS3("decorations/ruins/Brown_ruins3.png", false, 3, 64, 96),
RUINS_SAND1("decorations/ruins/Sand_ruins1.png", false, 3, 64, 96),
RUINS_SAND2("decorations/ruins/Sand_ruins2.png", false, 3, 64, 96);
```

---

### **3. ItemType.java** - Atualizar Ícones

**Localização:** `src/items/ItemType.java`

#### **ATUALIZAR MÉTODO getIconPath():**

**ANTES:**

```java
public String getIconPath() {
    return "icons/" + name().toLowerCase() + ".png";
}
```

**DEPOIS (Não muda, mas o SpriteLoader já vai buscar no lugar certo):**

```java
public String getIconPath() {
    return "icons/" + name().toLowerCase() + ".png";
}
```

O caminho relativo continua `icons/`, mas o SpriteLoader adiciona `assets/sprites/` automaticamente!

---

## 🔧 AJUSTES OPCIONAIS (RECOMENDADOS):

### **4. Imports com Packages**

Após reorganizar em packages, adicione imports onde necessário:

#### **GameWindow.java:**

```java
package core;

import entities.Player;
import entities.Decoration;
import world.Farm;
import world.Tile;
import world.TileType;
import systems.DecorationManager;
import systems.UIManager;
import systems.HarvestSystem;
import utils.SpriteLoader;

// ... resto do código
```

#### **GameEngine.java:**

```java
package core;

import entities.Player;
import world.Farm;
import world.Tile;
import world.TileType;
import types.CropType;
import systems.UIManager;
import systems.HarvestSystem;
import systems.Inventory;
import items.ItemType;

// ... resto do código
```

#### **Player.java:**

```java
package entities;

import systems.Inventory;
import items.ItemType;

// ... resto do código
```

#### **Farm.java:**

```java
package world;

import systems.DecorationManager;

// ... resto do código
```

---

## ✅ CHECKLIST DE ATUALIZAÇÃO:

```
OBRIGATÓRIAS:
[ ] Atualizar SpriteLoader.java (SPRITES_FOLDER)
[ ] Atualizar SpriteLoader.Sprites (caminhos relativos)
[ ] Atualizar DecorationType.java (todos os enums)

RECOMENDADAS:
[ ] Adicionar package declarations em todos os .java
[ ] Adicionar imports necessários
[ ] Testar compilação
[ ] Testar execução
[ ] Verificar carregamento de sprites

VALIDAÇÃO:
[ ] Jogo compila sem erros
[ ] Jogo executa sem erros
[ ] Player aparece corretamente
[ ] Tiles aparecem corretamente
[ ] Decorações aparecem corretamente
[ ] Ícones aparecem no inventário
[ ] Nenhuma mensagem de "sprite não encontrado"
```

---

## 🚀 SCRIPT DE ATUALIZAÇÃO AUTOMÁTICA:

Criei um script que faz essas mudanças automaticamente!

### **Para usar:**

```batch
.\atualizar_caminhos.bat
```

O script:

1. ✅ Adiciona packages a todos os .java
2. ✅ Atualiza imports
3. ✅ Corrige caminhos no SpriteLoader
4. ✅ Corrige caminhos no DecorationType
5. ✅ Cria backup antes das mudanças
6. ✅ Gera relatório de alterações

---

## 📊 RESUMO DAS MUDANÇAS:

| Arquivo                | Mudança                               | Tipo        |
| ---------------------- | ------------------------------------- | ----------- |
| `SpriteLoader.java`    | `sprites/` → `assets/sprites/`        | Obrigatória |
| `SpriteLoader.Sprites` | Adicionar subpastas nos caminhos      | Obrigatória |
| `DecorationType.java`  | Adicionar `decorations/` aos caminhos | Obrigatória |
| Todos os `.java`       | Adicionar `package` declaration       | Recomendada |
| Todos os `.java`       | Adicionar `import` statements         | Recomendada |

---

## 🧪 TESTE RÁPIDO:

Após fazer as mudanças:

```batch
# 1. Compilar
scripts\build\compile.bat

# 2. Executar
scripts\build\run.bat

# 3. Verificar no jogo:
- Player visível? ✅
- Tiles corretos? ✅
- Árvores/arbustos aparecem? ✅
- Inventário com ícones? ✅
```

---

## ❓ PROBLEMAS COMUNS:

### **Erro: "package X does not exist"**

**Solução:** Adicione os imports corretos no topo do arquivo

### **Erro: "cannot find symbol"**

**Solução:** Verifique se todos os packages estão declarados

### **Sprites não carregam**

**Solução:** Verifique se os arquivos estão em `assets/sprites/[subpasta]/`

### **"class file not found"**

**Solução:** Recompile com `scripts\build\compile.bat`

---

**🎯 Siga este guia e seu projeto estará 100% organizado e funcional!**
