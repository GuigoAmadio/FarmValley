# 🚀 ROADMAP - FARM VALLEY (Inspirado em Stardew Valley)

Lista completa de tarefas organizadas por prioridade e categoria.

---

## 📊 **PRIORIDADE ALTA - Funcionalidades Core**

### 🗄️ **1. Sistema de Save/Load**
- [ ] Criar classe `SaveManager.java`
- [ ] Salvar estado da fazenda (tiles, cultivos)
- [ ] Salvar inventário do jogador
- [ ] Salvar progressão de tempo (dia, estação)
- [ ] Salvar posição do jogador
- [ ] Salvar dinheiro e energia
- [ ] Salvar decorações colocadas
- [ ] Sistema de múltiplos saves
- [ ] Menu de carregar jogo
- [ ] Auto-save periódico

**Arquivos:** `src/systems/SaveManager.java`, `src/data/GameData.java`

---

### 🏪 **2. Sistema de Loja/Venda**
- [ ] Criar classe `Shop.java` e `ShopManager.java`
- [ ] NPC vendedor (Pierre's General Store equivalente)
- [ ] Interface de loja com categorias
- [ ] Venda de sementes (todas as estações)
- [ ] Venda de ferramentas
- [ ] Venda de itens especiais
- [ ] Sistema de venda (caixa de venda na fazenda)
- [ ] Preços dinâmicos baseados em qualidade
- [ ] Animações de compra/venda
- [ ] Histórico de vendas

**Arquivos:** `src/systems/ShopManager.java`, `src/entities/NPC.java`, `src/systems/ShippingBox.java`

---

### 🔨 **3. Sistema de Crafting**
- [ ] Criar classe `CraftingSystem.java`
- [ ] Banco de trabalho (crafting station)
- [ ] Sistema de receitas
- [ ] Interface de crafting
- [ ] Categorias: Ferramentas, Construções, Consumíveis
- [ ] Desbloqueio progressivo de receitas
- [ ] Feedback visual ao craftar
- [ ] Sons/efeitos de crafting
- [ ] Receitas básicas:
  - [ ] Cerca (Fence)
  - [ ] Portão (Gate)
  - [ ] Sprinkler básico
  - [ ] Tronco de madeira
  - [ ] Tabuas de madeira

**Arquivos:** `src/systems/CraftingSystem.java`, `src/types/Recipe.java`, `src/items/CraftingStation.java`

---

### 🐄 **4. Animais de Fazenda**
- [ ] Criar classe `Animal.java` e `AnimalType.java`
- [ ] Sistema de pastos/cercas
- [ ] Alimentação de animais
- [ ] Animais básicos:
  - [ ] Galinha (ovo)
  - [ ] Vaca (leite)
  - [ ] Porco (trufa)
- [ ] Afeição com animais
- [ ] Coleta de produtos (leite, ovos)
- [ ] Animais seguem jogador quando alimentados
- [ ] Animação de animais pastando
- [ ] Sistema de reprodução

**Arquivos:** `src/entities/Animal.java`, `src/types/AnimalType.java`, `src/systems/AnimalManager.java`

---

### 🏠 **5. Casa do Jogador**
- [ ] Criar estrutura de casa
- [ ] Entrada/saída da casa
- [ ] Interior da casa (tiles próprios)
- [ ] Cama para dormir/restaurar energia
- [ ] Upgrade de casa (3 níveis)
- [ ] Móveis decorativos
- [ ] Cozinha para receitas
- [ ] Armário para armazenamento extra
- [ ] Transição suave entre interior/exterior

**Arquivos:** `src/entities/House.java`, `src/world/InteriorMap.java`

---

## 📊 **PRIORIDADE MÉDIA - Expansão de Conteúdo**

### 🎣 **6. Sistema de Pesca**
- [ ] Criar classe `FishingSystem.java`
- [ ] Pontos de pesca no mapa (rios, lagos)
- [ ] Mini-jogo de pesca
- [ ] Diferentes tipos de peixes por estação
- [ ] Variação de peixes raros
- [ ] Vara de pescar como ferramenta
- [ ] Iscas e melhorias
- [ ] Peixe-troféu
- [ ] Biblioteca de peixes coletados

**Arquivos:** `src/systems/FishingSystem.java`, `src/types/FishType.java`, `src/entities/FishingSpot.java`

---

### ⛏️ **7. Sistema de Mineração/Cavernas**
- [ ] Criar classe `MiningSystem.java`
- [ ] Mapa de cavernas/mina
- [ ] Pedras mineráveis com recursos
- [ ] Múltiplos níveis de caverna
- [ ] Inimigos básicos nas cavernas
- [ ] Minerais e gemas raras
- [ ] Elevador entre níveis (save progresso)
- [ ] Ferramenta de escada para descer
- [ ] Ouro, ferro, cobre, carvão

**Arquivos:** `src/systems/MiningSystem.java`, `src/world/Mine.java`, `src/types/OreType.java`

---

### 👥 **8. NPCs e Diálogos**
- [ ] Criar classe `NPC.java` e `DialogueSystem.java`
- [ ] Sistema de diálogos com escolhas
- [ ] NPCs básicos:
  - [ ] Prefeito
  - [ ] Vendedor (Pierre)
  - [ ] Ferreiro (Clint)
  - [ ] Médico (Harvey)
  - [ ] Biblioteca (Gunther)
- [ ] Horários dos NPCs (rotina diária)
- [ ] Expressões faciais
- [ ] Presentes para NPCs
- [ ] Eventos especiais (festivais)

**Arquivos:** `src/entities/NPC.java`, `src/systems/DialogueSystem.java`, `src/types/Dialogue.java`

---

### ❤️ **9. Sistema de Relacionamentos**
- [ ] Criar classe `RelationshipSystem.java`
- [ ] Hearts/afeição com NPCs (0-10 corações)
- [ ] Conversas diárias aumentam afeição
- [ ] Presentes aumentam afeição
- [ ] Eventos de cortejo (romance)
- [ ] Casamento (com NPC romanceável)
- [ ] Presentes preferidos/odiados
- [ ] Aniversários dos NPCs

**Arquivos:** `src/systems/RelationshipSystem.java`, `src/data/NPCData.java`

---

### 🌦️ **10. Estações e Clima**
- [ ] Expandir `TimeSystem.java` para 4 estações
- [ ] Primavera (temperado, chuvas)
- [ ] Verão (quente, seco)
- [ ] Outono (frio, chuvas)
- [ ] Inverno (frio, neve)
- [ ] Cultivos específicos por estação
- [ ] Sistema de clima (chuva, tempestade, neve)
- [ ] Efeitos visuais de clima
- [ ] Clima afeta crescimento de cultivos
- [ ] Calendário anual (28 dias/estação)

**Arquivos:** `src/world/WeatherSystem.java`, `src/types/Season.java`, `src/types/WeatherType.java`

---

### 📜 **11. Sistema de Quests/Missões**
- [ ] Criar classe `QuestSystem.java`
- [ ] Board de missões na cidade
- [ ] Quests diárias e semanais
- [ ] Quests principais (história)
- [ ] Recompensas por completar quests
- [ ] Sistema de progresso visual
- [ ] Quests de coleta
- [ ] Quests de entrega
- [ ] Quests de matar monstros

**Arquivos:** `src/systems/QuestSystem.java`, `src/types/Quest.java`, `src/types/QuestType.java`

---

## 📊 **PRIORIDADE BAIXA - Melhorias e Polimento**

### 🍳 **12. Sistema de Cozinhar**
- [ ] Criar classe `CookingSystem.java`
- [ ] Fogão na casa
- [ ] Receitas de comida
- [ ] Comida restaura energia/HP
- [ ] Bônus temporários de comida
- [ ] Receitas desbloqueadas progressivamente
- [ ] Ingredientes especiais

**Arquivos:** `src/systems/CookingSystem.java`, `src/types/FoodType.java`, `src/types/CookingRecipe.java`

---

### 🗺️ **13. Múltiplos Mapas/Biomas**
- [ ] Sistema de transição entre mapas
- [ ] Mapa da cidade
- [ ] Mapa da floresta
- [ ] Mapa da praia
- [ ] Mapa da montanha (minas)
- [ ] Portal/porta de transição
- [ ] Diferentes tilesets por bioma
- [ ] Fade in/out entre mapas

**Arquivos:** `src/world/MapManager.java`, `src/world/Map.java`, `src/world/WorldMap.java`

---

### ⬆️ **14. Sistema de Upgrades**
- [ ] Upgrade de ferramentas (bronze, ferro, ouro)
- [ ] Upgrade de mochila (espaço extra)
- [ ] Upgrade de casa
- [ ] Upgrade de ferramentas na forja
- [ ] Ferreiro (Clint) oferece upgrades
- [ ] Custo de upgrade por nível

**Arquivos:** `src/systems/UpgradeSystem.java`, `src/types/ToolUpgrade.java`

---

### ✨ **15. Efeitos Visuais e Polimento**
- [ ] Partículas ao quebrar pedras/árvores
- [ ] Efeitos de chuva/neve
- [ ] Animações de ferramentas
- [ ] Sombras dinâmicas
- [ ] Efeitos de luz (dia/noite)
- [ ] Animações de colheita
- [ ] Efeitos de fadiga
- [ ] Transições suaves de cena
- [ ] Feedback visual melhorado

**Arquivos:** `src/systems/ParticleSystem.java`, `src/systems/LightingSystem.java`

---

### 🎵 **16. Sistema de Áudio**
- [ ] Música de fundo por área
- [ ] Efeitos sonoros (coletar, quebrar, plantar)
- [ ] Música por estação
- [ ] Sistema de volume (configurações)
- [ ] Música ambiente suave

**Arquivos:** `src/systems/AudioManager.java`, `src/utils/SoundLoader.java`

---

### 🎮 **17. Menu Principal e Configurações**
- [ ] Tela de título com menu
- [ ] Menu de pausa in-game
- [ ] Configurações:
  - [ ] Volume música/SFX
  - [ ] Resolução
  - [ ] Fullscreen
  - [ ] Controles customizáveis
- [ ] Créditos
- [ ] Menu de ajuda/tutorial

**Arquivos:** `src/core/MainMenu.java`, `src/core/SettingsMenu.java`, `src/core/PauseMenu.java`

---

## 🔧 **MELHORIAS TÉCNICAS**

### 🗃️ **18. Organização de Código**
- [ ] Refatorar código duplicado
- [ ] Documentação JavaDoc completa
- [ ] Otimização de performance
- [ ] Sistema de eventos (EventBus)
- [ ] Factory patterns para criação de entidades
- [ ] Gerenciamento de recursos melhorado

---

### 🐛 **19. Qualidade e Testes**
- [ ] Sistema de logs
- [ ] Tratamento de erros robusto
- [ ] Testes unitários básicos
- [ ] Validação de saves corrompidos
- [ ] Performance profiling
- [ ] Memory leak checks

---

## 📈 **PROGRESSO GERAL**

### ✅ **Completado:**
- Sistema básico de fazenda
- Plantio e colheita
- Inventário e hotbar
- Sistema de energia
- Sistema de tempo (dia)
- Decorações e coleta de recursos
- UI melhorada
- Autotiling (terreno contínuo)

### 🔄 **Em Progresso:**
- Sistema de crafting (estrutura base)
- Melhorias visuais

### ⏳ **Pendente:**
- Todo o resto da lista acima

---

## 🎯 **ORDEM RECOMENDADA DE IMPLEMENTAÇÃO:**

1. **Save/Load** (crítico para progressão)
2. **Loja/Venda** (economia do jogo)
3. **Crafting** (expande gameplay)
4. **Animais** (conteúdo importante)
5. **NPCs e Diálogos** (mundo vivo)
6. **Estações** (ciclo de gameplay)
7. **Pesca** (minigame divertido)
8. **Mineração** (progressão vertical)
9. **Quests** (objetivos claros)
10. **Relacionamentos** (conteúdo social)
11. **Polimento e efeitos** (último toque)

---

**Última atualização:** 2024
**Total de tarefas:** ~150+ itens


