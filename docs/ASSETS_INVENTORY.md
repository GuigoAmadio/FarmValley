# 📦 INVENTÁRIO COMPLETO DE ASSETS - FARM VALLEY

## 🎨 **ASSETS DISPONÍVEIS:**

### ✅ **1. PERSONAGENS**

**Pasta:** `craftpix-net-363992-free-top-down-orc-game-character-pixel-art`

**Orcs (3 variações):**

- Orc1 (machado)
- Orc2 (espada) ← **JÁ IMPLEMENTADO!**
- Orc3 (lança)

**Animações disponíveis:**

- ✅ Walk (caminhar) - **IMPLEMENTADO**
- ⏳ Attack (atacar)
- ⏳ Run (correr)
- ⏳ Walk Attack (caminhar atacando)
- ⏳ Run Attack (correr atacando)
- ⏳ Death (morte)
- ⏳ Hurt (dano)
- ⏳ Idle (parado)

**Tamanho:** 64x64px por frame
**Grid:** 6 colunas x 4 linhas (4 direções)

---

### 🌳 **2. ÁRVORES**

**Pasta:** `craftpix-net-385863-free-top-down-trees-pixel-art`

**Tipos disponíveis (3 estágios cada):**

- Tree (árvore normal) - verde
- Autumn_tree (outono) - laranja/vermelho
- Snow_tree (neve) - branco
- Fruit_tree (frutas) - verde com frutas
- Flower_tree (flores) - rosa/branco
- Moss_tree (musgo) - verde escuro
- Palm_tree (palmeira tipo 1 e 2)
- Christmas_tree (natal) - pinheiro
- Snow_christmass_tree (natal neve)
- Burned_tree (queimada) - preta
- Broken_tree (quebrada) - troncos

**Total:** ~42 variações de árvores!
**Uso:** Decoração, obstáculos, coleta de madeira

---

### 🌿 **3. ARBUSTOS E PLANTAS**

**Pasta:** `craftpix-net-141354-free-top-down-bushes-pixel-art`

**Tipos disponíveis (3 estágios cada):**

- Bush_simple (arbusto simples tipos 1 e 2)
- Bush_red_flowers (flores vermelhas)
- Bush_blue_flowers (flores azuis)
- Bush_orange_flowers (flores laranja)
- Bush_pink_flowers (flores rosas)
- Autumn_bush (outono)
- Snow_bush (neve)
- Cactus (tipos 1 e 2)
- Fern (samambaia tipos 1 e 2)
- Broken_tree (árvore quebrada)
- Burned_tree (árvore queimada)

**Total:** ~42 variações de arbustos!
**Uso:** Decoração, obstáculos menores, forrageamento

---

### 🏛️ **4. RUÍNAS**

**Pasta:** `craftpix-net-934618-free-top-down-ruins-pixel-art`

**Estilos disponíveis (5 formas cada):**

- Blue-gray_ruins (azul-cinza)
- Brown_ruins (marrom)
- Brown-gray_ruins (marrom-cinza)
- Sand_ruins (areia)
- Snow_ruins (neve)
- Water_ruins (água)
- White_ruins (branco)
- Yellow_ruins (amarelo)

**Total:** 40 peças de ruínas!
**Uso:** Decoração, dungeons, áreas de exploração, obstáculos

---

### 🌺 **5. PLANTAS MONSTROS**

**Pasta:** `craftpix-net-284465-free-predator-plant-mobs-pixel-art-pack`

**Plantas disponíveis (3 tipos):**

- Plant1 (planta carnívora verde)
- Plant2 (planta carnívora roxa)
- Plant3 (planta carnívora vermelha)

**Animações completas:**

- Idle (parado)
- Walk (andar)
- Run (correr)
- Attack (atacar)
- Hurt (dano)
- Death (morte)

**Total:** 3 monstros × 6 animações = 18 spritesheets!
**Uso:** Inimigos, combate, dungeons, áreas perigosas

---

### 🎨 **6. INTERFACE/UI**

**Pasta:** `craftpix-net-255216-free-basic-pixel-art-ui-for-rpg`

**Elementos disponíveis:**

- ✅ **Inventory** (inventário completo com grid)
- ✅ **Shop** (loja)
- ✅ **Craft** (crafting/fabricação)
- ✅ **Equipment** (equipamentos)
- ✅ **Character_panel** (painel de personagem)
- ✅ **Action_panel** (painel de ações)
- ✅ **Main_menu** (menu principal)
- ✅ **Settings** (configurações)
- ✅ **Circle_menu** (menu circular)
- ✅ **Buttons** (botões diversos)
- ✅ **Icons** (ícones de itens)
- ✅ **Win_loose** (vitória/derrota)
- ✅ **Levels** (sistema de níveis)
- ✅ **Numbers** (números estilizados)
- ✅ **Text1/Text2** (painéis de texto)
- ✅ **Main_tiles** (tiles de interface)
- ✅ **Decorative_cracks** (rachaduras decorativas)

**Total:** Interface RPG completa!
**Uso:** Menus, HUD, inventário, loja, crafting

---

## 📊 **ESTATÍSTICAS TOTAIS:**

| Categoria        | Quantidade     | Status             |
| ---------------- | -------------- | ------------------ |
| **Personagens**  | 3 tipos        | ✅ 1 implementado  |
| **Árvores**      | ~42 variações  | ⏳ Pendente        |
| **Arbustos**     | ~42 variações  | ⏳ Pendente        |
| **Ruínas**       | 40 peças       | ⏳ Pendente        |
| **Monstros**     | 3 tipos        | ⏳ Pendente        |
| **UI/Interface** | 17 componentes | ⏳ Pendente        |
| **TOTAL**        | ~150+ assets   | ✅ 5% implementado |

---

## 🎯 **PLANO DE IMPLEMENTAÇÃO:**

### **FASE 1: MUNDO VIVO** 🌍

- [ ] Sistema de terrenos (grass, dirt, stone)
- [ ] Adicionar árvores decorativas
- [ ] Adicionar arbustos decorativos
- [ ] Sistema de colisão com decorações
- [ ] Sistema de layers (chão → decorações → player)

### **FASE 2: INVENTÁRIO & UI** 🎒

- [ ] Sistema de inventário completo
- [ ] HUD melhorado com UI do pacote
- [ ] Menu de pausa
- [ ] Sistema de itens (classe Item)
- [ ] Ícones de itens

### **FASE 3: RECURSOS & COLETA** ⛏️

- [ ] Sistema de ferramentas (machado, enxada, etc)
- [ ] Coletar madeira de árvores
- [ ] Coletar recursos de arbustos
- [ ] Mineração de pedras
- [ ] Forrageamento

### **FASE 4: CRAFTING & CONSTRUÇÃO** 🔨

- [ ] Sistema de crafting
- [ ] Receitas de itens
- [ ] Interface de crafting
- [ ] Construção de estruturas
- [ ] Upgrade de ferramentas

### **FASE 5: LOJA & ECONOMIA** 💰

- [ ] Interface de loja
- [ ] NPC vendedor
- [ ] Compra/venda de itens
- [ ] Sistema de preços dinâmico

### **FASE 6: COMBATE & MONSTROS** ⚔️

- [ ] Sistema de vida do player
- [ ] Sistema de ataque
- [ ] Adicionar plantas monstros
- [ ] IA básica de inimigos
- [ ] Sistema de dano
- [ ] Animação de morte

### **FASE 7: EXPLORAÇÃO & DUNGEONS** 🗺️

- [ ] Múltiplos mapas
- [ ] Ruínas exploráveis
- [ ] Sistema de transição entre mapas
- [ ] Tesouros escondidos
- [ ] Áreas secretas

### **FASE 8: POLIMENTO** ✨

- [ ] Sistema de save/load
- [ ] Música e sons
- [ ] Partículas e efeitos
- [ ] Animações melhoradas
- [ ] Tutorial

---

## 🚀 **PRÓXIMOS PASSOS IMEDIATOS:**

### **1. IMPLEMENTAR DECORAÇÕES (FASE 1):**

- Criar classe `Decoration`
- Adicionar árvores no mapa
- Adicionar arbustos no mapa
- Sistema de colisão

### **2. MELHORAR TILES DO CHÃO:**

- Grass texture
- Dirt texture
- Stone texture
- Water texture

### **3. SISTEMA DE LAYERS:**

- Layer 0: Chão
- Layer 1: Decorações (arbustos)
- Layer 2: Player
- Layer 3: Árvores (sobrepõe player)

---

## 📝 **NOTAS IMPORTANTES:**

1. **Todos os assets têm versões com/sem sombra**

   - Use "Without_shadow" para objetos (jogo já desenha sombras)
   - Use "With_shadow" para testes

2. **Tamanhos variados:**

   - Personagens: 64x64px
   - Árvores: ~64-128px (altura variável)
   - Arbustos: ~32-48px
   - Ruínas: ~64-96px
   - UI: Diversos tamanhos

3. **Faltam ainda:**
   - ⚠️ Tiles de chão (grass, dirt, etc) - IMPORTANTE!
   - ⚠️ NPCs amigáveis
   - ⚠️ Animais (galinhas, vacas, etc)

---

**Criado em:** 17/10/2025
**Assets:** CraftPix.net (Licença Free)
**Projeto:** Farm Valley RPG
