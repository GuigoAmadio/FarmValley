# 🎨 LISTA DE SPRITES NECESSÁRIOS - FARM VALLEY

Este documento lista todos os arquivos de imagem que o jogo espera encontrar na pasta `sprites/`.

---

## 📁 ESTRUTURA DE PASTAS:

```
sprites/
├── icons/          (Ícones de itens - 32x32 ou 48x48)
├── trees/          (Árvores - variados)
├── bushes/         (Arbustos - variados)
├── ruins/          (Ruínas - variados)
├── ui/             (Interface)
├── grass.png       (Tile de grama - 60x60)
├── dirt.png        (Tile de terra - 60x60)
├── water.png       (Tile de água - 60x60)
├── stone.png       (Tile de pedra - 60x60)
└── player_*.png    (Sprites do jogador - 64x64)
```

---

## ✅ JÁ IMPLEMENTADOS:

### 🧍 PERSONAGEM (64x64 pixels)

- ✅ `player_down_1.png` - Jogador andando para baixo (frame 1)
- ✅ `player_down_2.png` - Jogador andando para baixo (frame 2)
- ✅ `player_up_1.png` - Jogador andando para cima (frame 1)
- ✅ `player_up_2.png` - Jogador andando para cima (frame 2)
- ✅ `player_left_1.png` - Jogador andando para esquerda (frame 1)
- ✅ `player_left_2.png` - Jogador andando para esquerda (frame 2)
- ✅ `player_right_1.png` - Jogador andando para direita (frame 1)
- ✅ `player_right_2.png` - Jogador andando para direita (frame 2)

### 🟩 TILES (60x60 pixels)

- ✅ `grass.png` - Grama
- ✅ `dirt.png` - Terra arada

### 🌳 DECORAÇÕES

- ✅ `trees/Tree1.png` - Árvore normal 1
- ✅ `trees/Tree2.png` - Árvore normal 2
- ✅ `trees/Tree3.png` - Árvore normal 3
- ✅ `trees/Fruit_tree1.png` - Árvore frutífera 1
- ✅ `trees/Fruit_tree2.png` - Árvore frutífera 2
- ✅ `trees/Fruit_tree3.png` - Árvore frutífera 3
- ✅ `trees/Autumn_tree1.png` - Árvore de outono 1
- ✅ `trees/Autumn_tree2.png` - Árvore de outono 2
- ✅ `trees/Palm_tree1_1.png` - Palmeira 1
- ✅ `trees/Palm_tree2_1.png` - Palmeira 2

### 🌿 ARBUSTOS

- ✅ `bushes/Bush_simple1_1.png` - Arbusto simples 1
- ✅ `bushes/Bush_simple1_2.png` - Arbusto simples 2
- ✅ `bushes/Bush_simple2_1.png` - Arbusto simples 3
- ✅ `bushes/Bush_red_flowers1.png` - Arbusto flores vermelhas
- ✅ `bushes/Bush_blue_flowers1.png` - Arbusto flores azuis
- ✅ `bushes/Bush_pink_flowers1.png` - Arbusto flores rosas
- ✅ `bushes/Bush_orange_flowers1.png` - Arbusto flores laranjas
- ✅ `bushes/Fern1_1.png` - Samambaia 1
- ✅ `bushes/Fern2_1.png` - Samambaia 2

### 🏛️ RUÍNAS

- ✅ `ruins/Brown_ruins1.png` - Ruína marrom 1
- ✅ `ruins/Brown_ruins2.png` - Ruína marrom 2
- ✅ `ruins/Brown_ruins3.png` - Ruína marrom 3
- ✅ `ruins/Sand_ruins1.png` - Ruína de areia 1
- ✅ `ruins/Sand_ruins2.png` - Ruína de areia 2

---

## ⏳ PENDENTES (IMPORTANTES):

### 🟦 TILES FALTANTES (60x60 pixels)

- ⏳ `water.png` - Água
- ⏳ `stone.png` - Pedra

### 🎒 ÍCONES DE ITENS (32x32 ou 48x48 pixels)

#### 🌱 SEMENTES:

- ⏳ `icons/wheat_seed.png` - Semente de Trigo (marrom claro)
- ⏳ `icons/tomato_seed.png` - Semente de Tomate (vermelho escuro)
- ⏳ `icons/corn_seed.png` - Semente de Milho (amarelo escuro)
- ⏳ `icons/carrot_seed.png` - Semente de Cenoura (laranja escuro)

#### 🌾 COLHEITAS:

- ⏳ `icons/wheat.png` - Trigo colhido (amarelo dourado)
- ⏳ `icons/tomato.png` - Tomate (vermelho brilhante)
- ⏳ `icons/corn.png` - Milho (amarelo brilhante)
- ⏳ `icons/carrot.png` - Cenoura (laranja)

#### 🪵 RECURSOS:

- ⏳ `icons/wood.png` - Madeira (marrom, tronco)
- ⏳ `icons/stone.png` - Pedra (cinza)
- ⏳ `icons/fiber.png` - Fibra (verde claro, fibras vegetais)

#### ⚒️ FERRAMENTAS:

- ⏳ `icons/axe.png` - Machado (cabo marrom, lâmina cinza)
- ⏳ `icons/pickaxe.png` - Picareta (cabo marrom, ponta cinza)
- ⏳ `icons/hoe.png` - Enxada (cabo marrom, lâmina marrom)
- ⏳ `icons/watering_can.png` - Regador (azul/cinza)

#### 🍞 CONSUMÍVEIS:

- ⏳ `icons/bread.png` - Pão (marrom claro)
- ⏳ `icons/apple.png` - Maçã (vermelho/verde)

#### 💰 ESPECIAIS:

- ⏳ `icons/coin.png` - Moeda de Ouro (dourado brilhante)

### 🎨 UI OPCIONAL:

- ⏳ `ui/inventory_bg.png` - Fundo do inventário
- ⏳ `ui/slot.png` - Moldura de slot

---

## 📐 ESPECIFICAÇÕES TÉCNICAS:

### **TAMANHOS RECOMENDADOS:**

| Tipo                | Tamanho          | Formato          |
| ------------------- | ---------------- | ---------------- |
| **Tiles (chão)**    | 60x60 px         | PNG transparente |
| **Ícones de itens** | 48x48 px         | PNG transparente |
| **Personagem**      | 64x64 px         | PNG transparente |
| **Árvores**         | 64-128 px altura | PNG transparente |
| **Arbustos**        | 32-48 px         | PNG transparente |
| **Ruínas**          | 64-96 px         | PNG transparente |

### **DICAS DE PIXEL ART:**

1. **Ícones simples** - Formas reconhecíveis
2. **Alto contraste** - Bordas escuras
3. **Cores vibrantes** - Fácil identificação
4. **Fundo transparente** - PNG com alpha channel
5. **Estilo consistente** - Mesma paleta de cores

---

## 🎨 SUGESTÕES DE CORES:

### **SEMENTES:**

- Trigo: `#8B7355` (marrom claro)
- Tomate: `#8B0000` (vermelho escuro)
- Milho: `#B8860B` (dourado escuro)
- Cenoura: `#FF8C00` (laranja escuro)

### **COLHEITAS:**

- Trigo: `#FFD700` (dourado)
- Tomate: `#FF6347` (vermelho tomate)
- Milho: `#FFFF00` (amarelo)
- Cenoura: `#FF8C00` (laranja)

### **RECURSOS:**

- Madeira: `#8B4513` (marrom saddle)
- Pedra: `#A0A0A0` (cinza)
- Fibra: `#90EE90` (verde claro)

### **FERRAMENTAS:**

- Cabo: `#8B4513` (marrom)
- Metal: `#C0C0C0` (prata)
- Madeira: `#D2691E` (chocolate)

### **ALIMENTOS:**

- Pão: `#D2B48C` (tan)
- Maçã: `#DC143C` (crimson)

### **ESPECIAIS:**

- Moeda: `#FFD700` (dourado)

---

## 🖼️ FERRAMENTAS SUGERIDAS:

### **Para criar pixel art:**

- **Aseprite** (pago, ~$20) - Melhor para animação
- **Piskel** (grátis, online) - https://www.piskelapp.com/
- **GraphicsGale** (grátis) - Simples e eficaz
- **Pixelorama** (grátis, open source) - Godot-based
- **Photoshop/GIMP** - Com grid e pencil tool

### **Para editar sprites existentes:**

- **GIMP** (grátis) - https://www.gimp.org/
- **Paint.NET** (grátis, Windows) - https://www.getpaint.net/
- **Krita** (grátis) - https://krita.org/

---

## 📝 PRIORIDADE DE IMPLEMENTAÇÃO:

### **🔥 ALTA PRIORIDADE (Jogo está usando):**

1. ⏳ `icons/wood.png` - Madeira (está coletando)
2. ⏳ `icons/stone.png` - Pedra (está coletando)
3. ⏳ `icons/fiber.png` - Fibra (está coletando)
4. ⏳ `icons/axe.png` - Machado (no inventário inicial)
5. ⏳ `icons/pickaxe.png` - Picareta (no inventário inicial)
6. ⏳ `icons/hoe.png` - Enxada (no inventário inicial)
7. ⏳ `icons/wheat_seed.png` - Semente de Trigo (no inventário inicial)
8. ⏳ `icons/tomato_seed.png` - Semente de Tomate (no inventário inicial)

### **⚡ MÉDIA PRIORIDADE (Melhorias visuais):**

9. ⏳ `water.png` - Água
10. ⏳ `stone.png` - Pedra (tile)
11. ⏳ `icons/wheat.png` - Trigo colhido
12. ⏳ `icons/tomato.png` - Tomate colhido
13. ⏳ `icons/coin.png` - Moeda

### **🔵 BAIXA PRIORIDADE (Futuras features):**

14. ⏳ Outros ícones de sementes
15. ⏳ Outros ícones de colheitas
16. ⏳ Consumíveis
17. ⏳ UI customizada

---

## 🚀 COMO ADICIONAR:

1. **Criar o ícone** (48x48 pixels, PNG transparente)
2. **Salvar em** `sprites/icons/nome_do_item.png`
3. **Recompilar** (se necessário): `javac *.java`
4. **Testar no jogo**: `java GameWindow`
5. **Verificar** se aparece no inventário/hot bar

---

## 💡 FALLBACK ATUAL:

**Se o ícone não for encontrado, o jogo mostra:**

- **Cor sólida** baseada no tipo de item
- **Sistema funcional** mesmo sem ícones customizados
- **Sem erros ou crashes**

---

## 📦 CHECKLIST RÁPIDA:

```
ÍCONES PRIORITÁRIOS:
[ ] wood.png       - Madeira
[ ] stone.png      - Pedra
[ ] fiber.png      - Fibra
[ ] axe.png        - Machado
[ ] pickaxe.png    - Picareta
[ ] hoe.png        - Enxada
[ ] wheat_seed.png - Semente de Trigo
[ ] tomato_seed.png - Semente de Tomate

TILES:
[ ] water.png      - Água
[ ] stone.png      - Pedra (tile)

EXTRAS:
[ ] wheat.png      - Trigo colhido
[ ] tomato.png     - Tomate colhido
[ ] corn.png       - Milho colhido
[ ] carrot.png     - Cenoura colhida
[ ] coin.png       - Moeda
```

---

**Criado em:** 17/10/2025  
**Projeto:** Farm Valley RPG  
**Formato:** PNG com transparência  
**Estilo:** Pixel Art consistente
