@echo off
echo ========================================
echo   COPIAR SPRITES DE DECORACOES
echo ========================================
echo.

set DOWNLOADS=C:\Users\Guillermo\Downloads

REM Criar estrutura de pastas
echo [1/3] Criando estrutura de pastas...
if not exist "sprites\trees" mkdir sprites\trees
if not exist "sprites\bushes" mkdir sprites\bushes
if not exist "sprites\ruins" mkdir sprites\ruins
echo      OK!
echo.

echo [2/3] Copiando sprites de arvores...

REM Árvores normais
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Tree1.png" "sprites\trees\" 2>nul
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Tree2.png" "sprites\trees\" 2>nul
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Tree3.png" "sprites\trees\" 2>nul

REM Árvores frutíferas
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Fruit_tree1.png" "sprites\trees\" 2>nul
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Fruit_tree2.png" "sprites\trees\" 2>nul
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Fruit_tree3.png" "sprites\trees\" 2>nul

REM Árvores de outono
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Autumn_tree1.png" "sprites\trees\" 2>nul
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Autumn_tree2.png" "sprites\trees\" 2>nul

REM Palmeiras
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Palm_tree1_1.png" "sprites\trees\" 2>nul
copy "%DOWNLOADS%\craftpix-net-385863-free-top-down-trees-pixel-art\PNG\Assets_separately\Trees\Palm_tree2_1.png" "sprites\trees\" 2>nul

echo      Arvores copiadas!
echo.

echo [3/3] Copiando sprites de arbustos...

REM Arbustos simples
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_simple1_1.png" "sprites\bushes\" 2>nul
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_simple1_2.png" "sprites\bushes\" 2>nul
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_simple2_1.png" "sprites\bushes\" 2>nul

REM Arbustos com flores
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_red_flowers1.png" "sprites\bushes\" 2>nul
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_blue_flowers1.png" "sprites\bushes\" 2>nul
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_pink_flowers1.png" "sprites\bushes\" 2>nul
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Bush_orange_flowers1.png" "sprites\bushes\" 2>nul

REM Samambaias
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Fern1_1.png" "sprites\bushes\" 2>nul
copy "%DOWNLOADS%\craftpix-net-141354-free-top-down-bushes-pixel-art\PNG\Assets\Fern2_1.png" "sprites\bushes\" 2>nul

echo      Arbustos copiados!
echo.

echo [BONUS] Copiando ruinas...

REM Ruínas
copy "%DOWNLOADS%\craftpix-net-934618-free-top-down-ruins-pixel-art\PNG\Assets\Brown_ruins1.png" "sprites\ruins\" 2>nul
copy "%DOWNLOADS%\craftpix-net-934618-free-top-down-ruins-pixel-art\PNG\Assets\Brown_ruins2.png" "sprites\ruins\" 2>nul
copy "%DOWNLOADS%\craftpix-net-934618-free-top-down-ruins-pixel-art\PNG\Assets\Brown_ruins3.png" "sprites\ruins\" 2>nul
copy "%DOWNLOADS%\craftpix-net-934618-free-top-down-ruins-pixel-art\PNG\Assets\Sand_ruins1.png" "sprites\ruins\" 2>nul
copy "%DOWNLOADS%\craftpix-net-934618-free-top-down-ruins-pixel-art\PNG\Assets\Sand_ruins2.png" "sprites\ruins\" 2>nul

echo      Ruinas copiadas!
echo.

echo ========================================
echo   CONCLUIDO!
echo ========================================
echo.
echo Sprites copiados com sucesso!
echo.
echo Estrutura criada:
echo   sprites\trees\    (10 arvores)
echo   sprites\bushes\   (9 arbustos)
echo   sprites\ruins\    (5 ruinas)
echo.
echo Agora compile e teste o jogo!
echo   javac *.java
echo   java GameWindow
echo.
pause

