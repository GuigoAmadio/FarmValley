@echo off
echo ========================================
echo   EXTRATOR DE ICONES - UI RPG
echo ========================================
echo.

set UI_PACK=C:\Users\Guillermo\Downloads\craftpix-net-255216-free-basic-pixel-art-ui-for-rpg\PNG

echo [1/4] Verificando arquivos...
if not exist "%UI_PACK%\Icons.png" (
    echo ERRO: Icons.png nao encontrado!
    pause
    exit /b 1
)
echo      OK - Icons.png encontrado!
echo.

echo [2/4] Compilando extrator...
javac TileSetExtractor.java
if %errorlevel% neq 0 (
    echo ERRO ao compilar!
    pause
    exit /b 1
)
echo      OK!
echo.

echo [3/4] Extraindo icones...
echo.
echo Tentando varios tamanhos de grid...
echo.

REM Testar 32x32
echo Testando 32x32 pixels...
java TileSetExtractor "%UI_PACK%\Icons.png" 32 icon_32

REM Testar 48x48
echo.
echo Testando 48x48 pixels...
java TileSetExtractor "%UI_PACK%\Icons.png" 48 icon_48

REM Testar 64x64
echo.
echo Testando 64x64 pixels...
java TileSetExtractor "%UI_PACK%\Icons.png" 64 icon_64

echo.
echo [4/4] Extraindo outros elementos de UI...
echo.

REM Main tiles
if exist "%UI_PACK%\Main_tiles.png" (
    echo Extraindo Main_tiles.png...
    java TileSetExtractor "%UI_PACK%\Main_tiles.png" 32 tile
)

REM Buttons
if exist "%UI_PACK%\Buttons.png" (
    echo Extraindo Buttons.png...
    java TileSetExtractor "%UI_PACK%\Buttons.png" 32 button
)

echo.
echo ========================================
echo   CONCLUIDO!
echo ========================================
echo.
echo Icones extraidos em: extracted_tiles\
echo.
echo PROXIMOS PASSOS:
echo 1. Abra extracted_tiles\ e veja os resultados
echo 2. Identifique quais icones voce quer usar
echo 3. Copie para sprites\icons\ com nomes corretos
echo 4. Exemplos de nomes esperados:
echo    - axe.png (machado)
echo    - pickaxe.png (picareta)
echo    - wood.png (madeira)
echo    - stone.png (pedra)
echo    - etc (veja ICONES_CHECKLIST.md)
echo.
pause

