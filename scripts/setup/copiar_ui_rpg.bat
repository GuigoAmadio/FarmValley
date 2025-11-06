@echo off
echo ========================================
echo   COPIAR SPRITES DE UI - RPG PACK
echo ========================================
echo.

set UI_PACK=C:\Users\Guillermo\Downloads\craftpix-net-255216-free-basic-pixel-art-ui-for-rpg\PNG

echo [1/2] Verificando estrutura...
if not exist "sprites\ui" mkdir sprites\ui
echo      OK!
echo.

echo [2/2] Copiando sprites de UI...
echo.

REM Copiar arquivos principais de UI
if exist "%UI_PACK%\Inventory.png" (
    copy "%UI_PACK%\Inventory.png" "sprites\ui\" >nul 2>&1
    echo [OK] Inventory.png
) else (
    echo [  ] Inventory.png (nao encontrado)
)

if exist "%UI_PACK%\Main_tiles.png" (
    copy "%UI_PACK%\Main_tiles.png" "sprites\ui\" >nul 2>&1
    echo [OK] Main_tiles.png
) else (
    echo [  ] Main_tiles.png (nao encontrado)
)

if exist "%UI_PACK%\Icons.png" (
    copy "%UI_PACK%\Icons.png" "sprites\ui\" >nul 2>&1
    echo [OK] Icons.png
) else (
    echo [  ] Icons.png (nao encontrado)
)

if exist "%UI_PACK%\Buttons.png" (
    copy "%UI_PACK%\Buttons.png" "sprites\ui\" >nul 2>&1
    echo [OK] Buttons.png
) else (
    echo [  ] Buttons.png (nao encontrado)
)

if exist "%UI_PACK%\character_panel.png" (
    copy "%UI_PACK%\character_panel.png" "sprites\ui\" >nul 2>&1
    echo [OK] character_panel.png
) else (
    echo [  ] character_panel.png (nao encontrado)
)

if exist "%UI_PACK%\Craft.png" (
    copy "%UI_PACK%\Craft.png" "sprites\ui\" >nul 2>&1
    echo [OK] Craft.png
) else (
    echo [  ] Craft.png (nao encontrado)
)

if exist "%UI_PACK%\Shop.png" (
    copy "%UI_PACK%\Shop.png" "sprites\ui\" >nul 2>&1
    echo [OK] Shop.png
) else (
    echo [  ] Shop.png (nao encontrado)
)

if exist "%UI_PACK%\Text1.png" (
    copy "%UI_PACK%\Text1.png" "sprites\ui\" >nul 2>&1
    echo [OK] Text1.png
) else (
    echo [  ] Text1.png (nao encontrado)
)

if exist "%UI_PACK%\Text2.png" (
    copy "%UI_PACK%\Text2.png" "sprites\ui\" >nul 2>&1
    echo [OK] Text2.png
) else (
    echo [  ] Text2.png (nao encontrado)
)

if exist "%UI_PACK%\Numbers.png" (
    copy "%UI_PACK%\Numbers.png" "sprites\ui\" >nul 2>&1
    echo [OK] Numbers.png
) else (
    echo [  ] Numbers.png (nao encontrado)
)

echo.
echo ========================================
echo   CONCLUIDO!
echo ========================================
echo.
echo Sprites copiados para: sprites\ui\
echo.
echo PROXIMOS PASSOS:
echo 1. Execute: extrair_icones_ui.bat
echo    (para extrair icones individuais)
echo.
echo 2. Ou comece a usar os sprites completos:
echo    - Inventory.png (fundo do inventario)
echo    - Main_tiles.png (slots e tiles de UI)
echo    - Icons.png (icones de itens)
echo.
echo 3. Consulte MELHORIAS_UI.md para detalhes
echo.
pause

