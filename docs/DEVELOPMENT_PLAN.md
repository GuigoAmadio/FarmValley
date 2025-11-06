# 🚀 PLANO DE AÇÃO IMEDIATO - FARM VALLEY

## 🎯 **OBJETIVO:** Transformar o jogo básico em um RPG complexo

---

## 📋 **FASE 1: MUNDO VIVO (IMPLEMENTAR AGORA!)**

### **1️⃣ Criar Sistema de Decorações**

**Arquivos a criar:**

- `Decoration.java` - Classe base para decorações
- `DecorationType.java` - Enum com tipos de decorações
- `DecorationManager.java` - Gerencia todas decorações

**Funcionalidades:**

- Armazenar posição (x, y)
- Armazenar sprite
- Collision box
- Layer (camada de renderização)
- Walkable/não walkable

---

### **2️⃣ Adicionar Árvores**

**Tipos implementar primeiro:**

- Tree1, Tree2, Tree3 (árvores normais)
- Fruit_tree1, Fruit_tree2, Fruit_tree3 (árvores frutíferas)

**Sistema:**

- Colocar árvores aleatórias no mapa
- Árvores bloqueiam movimento
- Renderizar árvores SOBRE o player (layer 3)

---

### **3️⃣ Adicionar Arbustos**

**Tipos implementar primeiro:**

- Bush_simple (arbustos simples)
- Bush_red_flowers (arbustos com flores)

**Sistema:**

- Colocar arbustos aleatórios
- Arbustos NÃO bloqueiam movimento
- Renderizar arbustos ABAIXO do player (layer 1)

---

### **4️⃣ Sistema de Layers**

```
Layer 0: Chão (grass, dirt, water)
Layer 1: Decorações baixas (arbustos, grama alta)
Layer 2: Player + Cultivos
Layer 3: Decorações altas (árvores, ruínas)
Layer 4: Efeitos/UI
```

---

## 🔧 **IMPLEMENTAÇÃO TÉCNICA:**

### **Estrutura de Classes:**

```java
// Decoration.java
public class Decoration {
    private int x, y;
    private DecorationType type;
    private Image sprite;
    private boolean walkable;
    private int layer; // 1 ou 3

    public Decoration(int x, int y, DecorationType type, boolean walkable, int layer)
    public boolean isWalkable()
    public Image getSprite()
    // ...
}

// DecorationType.java
public enum DecorationType {
    // Árvores
    TREE1("Tree1.png", false, 3),
    TREE2("Tree2.png", false, 3),
    FRUIT_TREE("Fruit_tree1.png", false, 3),

    // Arbustos
    BUSH_SIMPLE("Bush_simple1_1.png", true, 1),
    BUSH_FLOWERS("Bush_red_flowers1.png", true, 1),

    // Ruínas (futuro)
    RUINS_BROWN("Brown_ruins1.png", false, 3);

    private String spriteFile;
    private boolean walkable;
    private int layer;

    // ...
}

// DecorationManager.java
public class DecorationManager {
    private List<Decoration> decorations;

    public void generateDecorations(Farm farm)
    public List<Decoration> getDecorationsByLayer(int layer)
    public boolean isPositionWalkable(int x, int y)
    // ...
}
```

---

### **Modificações no código existente:**

#### **Farm.java:**

```java
// Adicionar referência ao DecorationManager
private DecorationManager decorationManager;

// Modificar isWalkable() para checar decorações
public boolean isWalkable(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) return false;
    if (getTile(x, y).getType() == TileType.WATER) return false;
    if (!decorationManager.isPositionWalkable(x, y)) return false; // NOVO!
    return true;
}
```

#### **GameWindow.java:**

```java
// Modificar render() para desenhar em camadas
private void render() {
    // ... (código existente para desenhar fundo)

    // Layer 0: Tiles (chão)
    for (int x = 0; x < farm.getWidth(); x++) {
        for (int y = 0; y < farm.getHeight(); y++) {
            drawTile(...);
        }
    }

    // Layer 1: Decorações baixas (arbustos)
    for (Decoration deco : decorationManager.getDecorationsByLayer(1)) {
        drawDecoration(deco, offsetX, offsetY);
    }

    // Layer 2: Player
    drawPlayer(...);

    // Layer 3: Decorações altas (árvores)
    for (Decoration deco : decorationManager.getDecorationsByLayer(3)) {
        drawDecoration(deco, offsetX, offsetY);
    }

    // ... (resto do código)
}

private void drawDecoration(Decoration deco, int offsetX, int offsetY) {
    Image sprite = deco.getSprite();
    if (sprite != null) {
        int x = deco.getX() * TILE_SIZE + offsetX;
        int y = deco.getY() * TILE_SIZE + offsetY;
        gc.drawImage(sprite, x, y, TILE_SIZE, TILE_SIZE);
    }
}
```

---

## 📦 **COPIAR ASSETS:**

### **Criar pasta estruturada:**

```
FarmValley/
├─ sprites/
│  ├─ player/
│  │  ├─ player.png
│  │  ├─ player_down_1.png
│  │  └─ ...
│  ├─ trees/
│  │  ├─ Tree1.png
│  │  ├─ Tree2.png
│  │  ├─ Fruit_tree1.png
│  │  └─ ...
│  ├─ bushes/
│  │  ├─ Bush_simple1_1.png
│  │  ├─ Bush_red_flowers1.png
│  │  └─ ...
│  └─ ui/
│     ├─ Inventory.png
│     └─ ...
```

### **Script para copiar:**

Criar `copiar_decoracoes.bat` para copiar árvores e arbustos dos downloads para sprites/

---

## 🎮 **RESULTADO ESPERADO:**

Depois da Fase 1, o jogo terá:

- ✅ Mundo vivo com árvores e arbustos
- ✅ Árvores que bloqueiam passagem
- ✅ Arbustos decorativos que não bloqueiam
- ✅ Renderização em camadas (árvores sobre player)
- ✅ Geração procedural de decorações
- ✅ Mapa mais interessante e visualmente rico

---

## 🔜 **DEPOIS DA FASE 1:**

### **Fase 2: Inventário (próximo)**

- Sistema de itens
- Interface de inventário usando UI pack
- Coleta de recursos

### **Fase 3: Ferramentas**

- Machado para cortar árvores
- Enxada (já existe)
- Sistema de durabilidade

---

## ⏱️ **ESTIMATIVA:**

- **Fase 1 (Decorações):** ~30-45 minutos
- **Fase 2 (Inventário):** ~45-60 minutos
- **Fase 3 (Ferramentas):** ~30 minutos

**Total primeira sessão:** ~2 horas para transformar drasticamente o jogo!

---

## 📝 **ORDEM DE EXECUÇÃO:**

1. ✅ Criar `DecorationType.java`
2. ✅ Criar `Decoration.java`
3. ✅ Criar `DecorationManager.java`
4. ✅ Modificar `Farm.java`
5. ✅ Modificar `GameWindow.java`
6. ✅ Copiar sprites de árvores/arbustos
7. ✅ Testar!

---

**VAMOS COMEÇAR AGORA!** 🚀
