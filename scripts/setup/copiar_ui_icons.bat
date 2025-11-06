@echo off
echo ========================================
echo   COPIAR SPRITES DE UI E ICONES
echo ========================================
echo.

set DOWNLOADS=C:\Users\Guillermo\Downloads

REM Criar estrutura de pastas
echo [1/2] Criando estrutura de pastas...
if not exist "sprites\ui" mkdir sprites\ui
if not exist "sprites\icons" mkdir sprites\icons
echo      OK!
echo.

echo [2/2] Copiando sprites de UI...

REM UI Elements
copy "%DOWNLOADS%\craftpix-net-255216-free-basic-pixel-art-ui-for-rpg\PNG\Inventory\Inventory.png" "sprites\ui\inventory_bg.png" 2>nul
copy "%DOWNLOADS%\craftpix-net-255216-free-basic-pixel-art-ui-for-rpg\PNG\Main_tiles\Main_tiles1.png" "sprites\ui\slot.png" 2>nul

echo      UI copiada!
echo.

echo ========================================
echo   AVISO: ICONES AINDA NAO DISPONÍVEIS
echo ========================================
echo.
echo Os ícones de itens não estão no pacote de UI.
echo O jogo usará cores como fallback.
echo.
echo Você pode adicionar seus próprios ícones em:
echo   sprites\icons\
echo.
echo Nomes esperados:
echo   wheat_seed.png, tomato_seed.png, corn_seed.png, carrot_seed.png
echo   wheat.png, tomato.png, corn.png, carrot.png
echo   wood.png, stone.png, fiber.png
echo   axe.png, pickaxe.png, hoe.png, watering_can.png
echo   bread.png, apple.png, coin.png
echo.
echo Agora compile e teste o jogo!
echo   javac *.java
echo   java GameWindow
echo.
echo Pressione [I] no jogo para abrir o inventário!
echo.
pause

