# 🌾 Farm Valley

**Um jogo de fazenda RPG desenvolvido em Java com JavaFX**

> Inspirado em Stardew Valley, Farm Valley é um jogo onde você cultiva plantações, coleta recursos, explora o mundo e constrói sua fazenda dos sonhos!

---

## 🎮 Como Jogar

### **Controles:**

- **WASD** ou **Setas** - Mover o jogador
- **E** ou **Espaço** - Coletar recursos/interagir
- **I** - Abrir/fechar inventário
- **1-6** - Seleção rápida de itens (hotbar)
- **T** - Arar terra
- **P** - Plantar sementes
- **H** - Colher plantações
- **Z** - Dormir (avançar dia)
- **C** - Trocar tipo de semente

### **Objetivo:**

- 🌱 Plante e cultive diferentes tipos de culturas
- 🪓 Colete madeira de árvores
- 🪨 Quebre pedras para obter recursos
- 🌿 Colete fibras de arbustos
- 💰 Venda suas colheitas para ganhar dinheiro
- 🏠 Expanda e melhore sua fazenda

---

## 🚀 Como Executar

### **Requisitos:**

- **Java Development Kit (JDK) 21+** com JavaFX
  - Recomendado: [Liberica JDK Full](https://bell-sw.com/pages/downloads/) (inclui JavaFX)

### **Instalação Rápida:**

1. **Clone ou baixe o projeto:**

   ```bash
   git clone https://github.com/seu-usuario/farm-valley.git
   cd farm-valley
   ```

2. **Compile o projeto:**

   ```batch
   scripts\build\compile.bat
   ```

3. **Execute o jogo:**
   ```batch
   scripts\build\run.bat
   ```

### **Comandos Úteis:**

| Comando                     | Descrição                   |
| --------------------------- | --------------------------- |
| `scripts\build\compile.bat` | Compila todo o código fonte |
| `scripts\build\run.bat`     | Executa o jogo              |
| `scripts\build\clean.bat`   | Limpa arquivos compilados   |

---

## 📂 Estrutura do Projeto

```
FarmValley/
├── src/                    # Código fonte Java
│   ├── core/              # GameEngine, GameWindow
│   ├── entities/          # Player, Decoration, Crop
│   ├── world/             # Farm, Tile, TimeSystem
│   ├── systems/           # HarvestSystem, Inventory, UIManager
│   ├── items/             # Item, ItemType, ResourceType
│   ├── types/             # CropType, DecorationType
│   └── utils/             # SpriteLoader
│
├── assets/                # Recursos visuais
│   └── sprites/           # Imagens do jogo
│       ├── player/        # Sprites do jogador
│       ├── tiles/         # Tiles do terreno
│       ├── decorations/   # Árvores, arbustos, ruínas
│       ├── icons/         # Ícones de itens
│       └── ui/            # Interface do usuário
│
├── docs/                  # Documentação
│   ├── SPRITES_GUIDE.md   # Guia de sprites
│   ├── ICONS_CHECKLIST.md # Checklist de ícones
│   └── UI_IMPROVEMENTS.md # Melhorias de UI
│
├── tools/                 # Ferramentas de desenvolvimento
│   └── extractors/        # Scripts de extração de sprites
│
├── scripts/               # Scripts de build
│   ├── build/             # compile.bat, run.bat, clean.bat
│   └── setup/             # Scripts de configuração
│
└── build/                 # Arquivos compilados (.class)
```

**Ver documentação completa:** [`docs/PROJECT_STRUCTURE.md`](docs/PROJECT_STRUCTURE.md)

---

## 🎨 Assets e Sprites

### **Sprites Atuais:**

- ✅ Player (animação 4 direções)
- ✅ Tiles (grama, terra)
- ✅ Árvores (10 tipos)
- ✅ Arbustos (9 tipos)
- ✅ Ruínas (5 tipos)
- ✅ Ícones de itens (parcial)
- ✅ UI básica

### **Adicionar Novos Sprites:**

Consulte [`docs/SPRITES_GUIDE.md`](docs/SPRITES_GUIDE.md) para instruções detalhadas.

**Quick Start:**

1. Coloque arquivos PNG em `assets/sprites/[categoria]/`
2. Use ferramentas em `tools/extractors/` para extrair de spritesheets
3. O jogo carrega automaticamente sem recompilação!

---

## 🎯 Funcionalidades

### **✅ Implementadas:**

- Sistema de movimentação suave do jogador
- Animações de caminhada (4 direções)
- Sistema de fazenda e plantio
- 4 tipos de culturas (trigo, tomate, milho, cenoura)
- Sistema de coleta de recursos
- Árvores, arbustos e pedras coletáveis
- Inventário completo (24 slots)
- Hotbar (6 slots rápidos)
- Sistema de ferramentas (machado, picareta, enxada)
- Sistema de energia
- Sistema de dia/noite
- Câmera centralizada no jogador
- Renderização em camadas (parallax)
- Colisões com decorações
- Sistema de UI básico

### **🚧 Em Desenvolvimento:**

- Sistema de crafting
- Loja/vendedor
- Painel de personagem
- Sistema de quests
- NPCs e diálogos
- Sistema de som/música
- Mais tipos de culturas
- Melhorias visuais (partículas, efeitos)

### **💡 Planejadas:**

- Animais de fazenda
- Casa do jogador (upgrade)
- Diferentes biomas
- Sistema de clima
- Pesca
- Mineração (cavernas)
- Multiplayer local

---

## 🛠️ Desenvolvimento

### **Ferramentas Incluídas:**

| Ferramenta                               | Função                                    |
| ---------------------------------------- | ----------------------------------------- |
| `SpriteSheetExtractor.java`              | Extrai frames individuais de spritesheets |
| `TileSetExtractor.java`                  | Extrai tiles de tilesets                  |
| `tools/extractors/extrair_sprites.bat`   | Automatiza extração de sprites            |
| `tools/extractors/extrair_tiles.bat`     | Automatiza extração de tiles              |
| `tools/extractors/extrair_icones_ui.bat` | Extrai ícones de UI                       |

### **Como Contribuir:**

1. **Fork o projeto**
2. **Crie uma branch para sua feature:**
   ```bash
   git checkout -b feature/nova-funcionalidade
   ```
3. **Commit suas mudanças:**
   ```bash
   git commit -m "Adiciona nova funcionalidade X"
   ```
4. **Push para o branch:**
   ```bash
   git push origin feature/nova-funcionalidade
   ```
5. **Abra um Pull Request**

---

## 📚 Documentação

| Documento                                                | Descrição                        |
| -------------------------------------------------------- | -------------------------------- |
| [`docs/PROJECT_STRUCTURE.md`](docs/PROJECT_STRUCTURE.md) | Estrutura completa do projeto    |
| [`docs/SPRITES_GUIDE.md`](docs/SPRITES_GUIDE.md)         | Como adicionar e criar sprites   |
| [`docs/ICONS_CHECKLIST.md`](docs/ICONS_CHECKLIST.md)     | Lista de ícones necessários      |
| [`docs/ICONS_PRIORITY.md`](docs/ICONS_PRIORITY.md)       | Prioridades de criação de ícones |
| [`docs/UI_IMPROVEMENTS.md`](docs/UI_IMPROVEMENTS.md)     | Melhorias planejadas para UI     |
| [`docs/DEVELOPMENT_PLAN.md`](docs/DEVELOPMENT_PLAN.md)   | Plano de desenvolvimento         |
| [`docs/ASSETS_INVENTORY.md`](docs/ASSETS_INVENTORY.md)   | Inventário completo de assets    |

---

## 🐛 Problemas Conhecidos

- [ ] Alguns ícones de itens ainda não foram criados (usando fallback)
- [ ] Sistema de som não implementado
- [ ] Sem menu principal ainda
- [ ] Performance pode variar com muitas decorações

**Reporte bugs:** [Issues](https://github.com/seu-usuario/farm-valley/issues)

---

## 📝 Licença

Este projeto está sob a licença MIT. Ver arquivo [`LICENSE`](LICENSE) para mais detalhes.

---

## 🙏 Créditos

### **Desenvolvimento:**

- Guillermo - Desenvolvedor principal

### **Assets:**

- Sprites base: [CraftPix.net](https://craftpix.net/)
- Sprites customizados: Criados especificamente para o projeto

### **Inspiração:**

- [Stardew Valley](https://www.stardewvalley.net/) - Inspiração principal
- Harvest Moon series

---

## 📞 Contato

- **GitHub:** [seu-usuario](https://github.com/seu-usuario)
- **Email:** seu-email@example.com

---

## 🎮 Screenshots

_(Adicione screenshots do jogo aqui)_

---

## ⭐ Agradecimentos

Obrigado por jogar Farm Valley! Se gostou do projeto, considere dar uma ⭐ no repositório!

---

**🌾 Bom farming! 🌾**
