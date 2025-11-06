# 🗺️ Otimização para Mapas Grandes

## 📊 **Análise de Memória Atual**

**Mapa atual:** 20x15 = 300 tiles
- Cada `Tile` objeto: ~200 bytes (estimado)
- Memória total: ~60 KB para tiles

**Mapa grande exemplo:** 200x200 = 40.000 tiles
- Sem otimização: ~8 MB apenas para tiles
- Com chunks: ~200-400 KB (apenas chunks visíveis)

---

## 🎯 **Soluções Implementadas**

### **1. View Frustum Culling (Renderização Otimizada)**
Renderizar APENAS os tiles visíveis na tela.

**Economia:** De renderizar 40.000 tiles → ~200 tiles por frame

### **2. Sistema de Chunks (Carregamento Sob Demanda)**
Dividir mapa em chunks de 32x32 tiles e carregar apenas quando necessário.

**Economia:** De 8 MB → ~200-400 KB em memória

---

## 📐 **Tamanhos Recomendados**

- **Pequeno:** 50x50 tiles = 2.500 tiles
- **Médio:** 100x100 tiles = 10.000 tiles  
- **Grande:** 200x200 tiles = 40.000 tiles
- **Muito Grande:** 500x500 tiles = 250.000 tiles (requer chunks obrigatório)

---

## ⚡ **Performance Esperada**

Com otimizações:
- ✅ Mapa 100x100: ~60 FPS constante
- ✅ Mapa 200x200: ~55-60 FPS
- ✅ Mapa 500x500: ~50-60 FPS (com chunks)

Sem otimizações:
- ❌ Mapa 200x200: ~10-15 FPS
- ❌ Mapa 500x500: Não jogável


