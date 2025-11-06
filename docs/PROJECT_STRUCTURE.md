# 🗂️ NOVA ESTRUTURA DE DIRETÓRIOS - FARM VALLEY

Estrutura profissional e organizada para o projeto.

---

## 📁 ESTRUTURA PROPOSTA:

```
FarmValley/
├── 📂 src/                     (Código fonte Java)
│   ├── core/                   (Classes principais do jogo)
│   │   ├── GameEngine.java
│   │   ├── GameWindow.java
│   │   └── Main.java
│   ├── entities/               (Entidades do jogo)
│   │   ├── Player.java
│   │   ├── Decoration.java
│   │   └── Crop.java
│   ├── world/                  (Mundo e mapas)
│   │   ├── Farm.java
│   │   ├── Tile.java
│   │   ├── TileType.java
│   │   └── TimeSystem.java
│   ├── systems/                (Sistemas de jogo)
│   │   ├── HarvestSystem.java
│   │   ├── DecorationManager.java
│   │   ├── Inventory.java
│   │   └── UIManager.java
│   ├── items/                  (Sistema de itens)
│   │   ├── Item.java
│   │   ├── ItemType.java
│   │   └── ResourceType.java
│   ├── types/                  (Enums e tipos)
│   │   ├── CropType.java
│   │   └── DecorationType.java
│   └── utils/                  (Utilitários)
│       └── SpriteLoader.java
│
├── 📂 assets/                  (Recursos visuais e sonoros)
│   ├── sprites/
│   │   ├── player/             (Sprites do jogador)
│   │   ├── tiles/              (Tiles do chão)
│   │   ├── decorations/        (Árvores, arbustos, ruínas)
│   │   │   ├── trees/
│   │   │   ├── bushes/
│   │   │   └── ruins/
│   │   ├── icons/              (Ícones de itens)
│   │   └── ui/                 (Interface do usuário)
│   ├── sounds/                 (Sons e música - futuro)
│   │   ├── music/
│   │   └── sfx/
│   └── fonts/                  (Fontes customizadas - futuro)
│
├── 📂 docs/                    (Documentação)
│   ├── README.md               (Documentação principal)
│   ├── SPRITES_GUIDE.md        (Guia de sprites)
│   ├── ICONS_CHECKLIST.md      (Checklist de ícones)
│   ├── UI_IMPROVEMENTS.md      (Melhorias de UI)
│   └── DEVELOPMENT_PLAN.md     (Plano de desenvolvimento)
│
├── 📂 tools/                   (Ferramentas de desenvolvimento)
│   ├── SpriteSheetExtractor.java
│   ├── TileSetExtractor.java
│   └── extractors/             (Scripts de extração)
│       ├── extrair_sprites.bat
│       ├── extrair_tiles.bat
│       └── extrair_icones_ui.bat
│
├── 📂 scripts/                 (Scripts de build e setup)
│   ├── build/
│   │   ├── compile.bat
│   │   ├── run.bat
│   │   └── clean.bat
│   └── setup/
│       ├── copiar_assets.bat
│       ├── criar_estrutura.bat
│       └── verificar_ambiente.bat
│
├── 📂 build/                   (Arquivos compilados .class)
│   └── .gitkeep
│
├── 📂 temp/                    (Arquivos temporários)
│   ├── extracted_sprites/
│   ├── extracted_tiles/
│   └── logs/
│
├── 📂 releases/                (Versões compiladas do jogo)
│   └── .gitkeep
│
├── 📄 .gitignore              (Ignorar arquivos desnecessários)
├── 📄 README.md               (Documentação principal)
├── 📄 LICENSE                 (Licença do projeto)
└── 📄 CHANGELOG.md            (Histórico de mudanças)
```

---

## 🎯 BENEFÍCIOS DA NOVA ESTRUTURA:

### **1. Organização por Funcionalidade**

✅ Código separado por responsabilidade  
✅ Fácil encontrar arquivos específicos  
✅ Escalabilidade para novos sistemas

### **2. Separação de Assets**

✅ Sprites organizados por categoria  
✅ Fácil adicionar novos recursos  
✅ Preparado para sons e músicas

### **3. Documentação Centralizada**

✅ Todos os .md em um lugar  
✅ Fácil consulta  
✅ Profissionalismo

### **4. Ferramentas Isoladas**

✅ Tools separadas do código principal  
✅ Scripts organizados por função  
✅ Fácil manutenção

### **5. Build System**

✅ Arquivos .class separados do código  
✅ Sistema de build organizado  
✅ Fácil limpar e recompilar

---

## 🔄 MAPEAMENTO: Antigo → Novo

### **CÓDIGO FONTE:**

```
[RAIZ]/*.java → src/

Especificamente:
├── GameEngine.java      → src/core/GameEngine.java
├── GameWindow.java      → src/core/GameWindow.java
├── Player.java          → src/entities/Player.java
├── Decoration.java      → src/entities/Decoration.java
├── Crop.java            → src/entities/Crop.java
├── Farm.java            → src/world/Farm.java
├── Tile.java            → src/world/Tile.java
├── TileType.java        → src/world/TileType.java
├── TimeSystem.java      → src/world/TimeSystem.java
├── HarvestSystem.java   → src/systems/HarvestSystem.java
├── DecorationManager.java → src/systems/DecorationManager.java
├── Inventory.java       → src/systems/Inventory.java
├── UIManager.java       → src/systems/UIManager.java
├── Item.java            → src/items/Item.java
├── ItemType.java        → src/items/ItemType.java
├── ResourceType.java    → src/items/ResourceType.java
├── CropType.java        → src/types/CropType.java
├── DecorationType.java  → src/types/DecorationType.java
└── SpriteLoader.java    → src/utils/SpriteLoader.java
```

### **ASSETS:**

```
sprites/ → assets/sprites/

Especificamente:
├── player_*.png         → assets/sprites/player/
├── grass.png, dirt.png  → assets/sprites/tiles/
├── trees/               → assets/sprites/decorations/trees/
├── bushes/              → assets/sprites/decorations/bushes/
├── ruins/               → assets/sprites/decorations/ruins/
├── icons/               → assets/sprites/icons/
└── ui/                  → assets/sprites/ui/
```

### **DOCUMENTAÇÃO:**

```
*.md → docs/

Especificamente:
├── SPRITES_NECESSARIOS.md → docs/SPRITES_GUIDE.md
├── ICONES_CHECKLIST.md    → docs/ICONS_CHECKLIST.md
├── ICONES_PRIORITARIOS.md → docs/ICONS_PRIORITY.md
├── MELHORIAS_UI.md        → docs/UI_IMPROVEMENTS.md
├── PLANO_ACAO_IMEDIATO.md → docs/DEVELOPMENT_PLAN.md
└── ASSETS_INVENTORY.md    → docs/ASSETS_INVENTORY.md
```

### **FERRAMENTAS:**

```
Extractors → tools/

Especificamente:
├── SpriteSheetExtractor.java → tools/SpriteSheetExtractor.java
├── TileSetExtractor.java     → tools/TileSetExtractor.java
├── extrair_sprites.bat       → tools/extractors/extrair_sprites.bat
├── extrair_tiles.bat         → tools/extractors/extrair_tiles.bat
└── extrair_icones_ui.bat     → tools/extractors/extrair_icones_ui.bat
```

### **SCRIPTS:**

```
*.bat → scripts/

Especificamente:
├── run.bat              → scripts/build/run.bat
├── copiar_*.bat         → scripts/setup/
└── criar_*.bat          → scripts/setup/
```

### **ARQUIVOS TEMPORÁRIOS:**

```
Temporários → temp/

├── extracted_sprites/   → temp/extracted_sprites/
├── extracted_tiles/     → temp/extracted_tiles/
├── *.class             → build/
└── *.log               → temp/logs/
```

---

## 🚀 MIGRAÇÃO AUTOMÁTICA:

Criei um script para fazer a reorganização automaticamente!

### **Executar:**

```batch
.\reorganizar_projeto.bat
```

### **O que o script faz:**

1. ✅ Cria toda a nova estrutura de pastas
2. ✅ Move arquivos Java para src/
3. ✅ Move assets para assets/
4. ✅ Move documentação para docs/
5. ✅ Move ferramentas para tools/
6. ✅ Move scripts para scripts/
7. ✅ Move arquivos .class para build/
8. ✅ Cria backup antes de mover
9. ✅ Gera relatório de migração
10. ✅ Cria .gitignore apropriado

---

## 📋 ARQUIVOS ESPECIAIS:

### **.gitignore**

```gitignore
# Arquivos compilados
build/
*.class

# Arquivos temporários
temp/
extracted_sprites/
extracted_tiles/
*.log

# IDE
.vscode/
.idea/
*.iml

# Sistema
.DS_Store
Thumbs.db
desktop.ini

# Backups
*.bak
*~
```

### **README.md Atualizado**

````markdown
# 🌾 Farm Valley - Farming RPG Game

Jogo de fazenda estilo Stardew Valley desenvolvido em Java com JavaFX.

## 🎮 Como Jogar

### Requisitos:

- Java 21+ com JavaFX

### Compilar:

```batch
scripts\build\compile.bat
```
````

### Executar:

```batch
scripts\build\run.bat
```

## 📂 Estrutura do Projeto

Ver documentação completa em `docs/`

## 🎨 Assets

Sprites em `assets/sprites/`
Consulte `docs/SPRITES_GUIDE.md` para adicionar novos sprites.

## 🔧 Desenvolvimento

Ferramentas em `tools/`
Scripts de setup em `scripts/setup/`

````

---

## 🔄 COMANDOS ATUALIZADOS:

### **COMPILAÇÃO:**
```batch
# Antes:
javac *.java

# Depois:
scripts\build\compile.bat
````

### **EXECUÇÃO:**

```batch
# Antes:
java GameWindow

# Depois:
scripts\build\run.bat
```

### **LIMPEZA:**

```batch
# Novo:
scripts\build\clean.bat
```

---

## ✅ CHECKLIST DE MIGRAÇÃO:

```
PREPARAÇÃO:
[ ] Fazer backup do projeto inteiro
[ ] Verificar que todos os arquivos estão salvos
[ ] Fechar o jogo se estiver rodando

EXECUTAR MIGRAÇÃO:
[ ] Executar: reorganizar_projeto.bat
[ ] Verificar logs de migração
[ ] Confirmar que todos os arquivos foram movidos

ATUALIZAÇÃO:
[ ] Atualizar caminhos em SpriteLoader.java
[ ] Testar compilação: scripts\build\compile.bat
[ ] Testar execução: scripts\build\run.bat
[ ] Verificar se sprites carregam corretamente

PÓS-MIGRAÇÃO:
[ ] Deletar arquivos temporários da raiz
[ ] Configurar .gitignore
[ ] Criar repositório Git (opcional)
[ ] Atualizar documentação customizada
```

---

## 💡 VANTAGENS A LONGO PRAZO:

### **Para Desenvolvimento:**

- ✅ Código modularizado e testável
- ✅ Fácil adicionar novos sistemas
- ✅ Reduz conflitos em equipe
- ✅ Debug mais simples

### **Para Assets:**

- ✅ Organização clara de recursos
- ✅ Fácil encontrar sprites
- ✅ Preparado para expansão
- ✅ Versionamento eficiente

### **Para Colaboração:**

- ✅ Estrutura profissional
- ✅ Fácil onboarding
- ✅ Documentação acessível
- ✅ Padrão da indústria

### **Para Entrega:**

- ✅ Sistema de build organizado
- ✅ Releases versionadas
- ✅ Documentação completa
- ✅ Fácil distribuição

---

## 🎯 PRÓXIMOS PASSOS:

1. **Execute o script de reorganização**
2. **Teste a compilação e execução**
3. **Atualize qualquer caminho hardcoded**
4. **Configure .gitignore**
5. **Crie primeiro release!**

---

**🚀 Projeto profissional e escalável pronto!**
