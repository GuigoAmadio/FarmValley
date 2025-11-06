@echo off
echo ========================================
echo   EXTRATOR DE TILES - FARM VALLEY
echo ========================================
echo.

REM Compilar o extrator
echo [1/3] Compilando extrator de tiles...
javac TileSetExtractor.java
if %errorlevel% neq 0 (
    echo ERRO ao compilar!
    pause
    exit /b 1
)
echo      OK!
echo.

echo [2/3] Preparando extração...
echo.
echo Cole o caminho COMPLETO do arquivo de tileset PNG aqui:
echo.
set /p TILESET_FILE="Arquivo: "

if not exist "%TILESET_FILE%" (
    echo.
    echo ERRO: Arquivo nao encontrado!
    pause
    exit /b 1
)

echo.
echo [3/3] Qual o tamanho de cada tile?
echo Opcoes comuns:
echo   16 - Tiles pequenos (16x16 pixels)
echo   32 - Tiles medios (32x32 pixels)
echo   48 - Tiles grandes (48x48 pixels)
echo   64 - Tiles extra grandes (64x64 pixels)
echo.
set /p TILE_SIZE="Tamanho do tile (padrao 32): "

if "%TILE_SIZE%"=="" set TILE_SIZE=32

echo.
echo Extraindo tiles de %TILE_SIZE%x%TILE_SIZE% pixels...
echo.

java TileSetExtractor "%TILESET_FILE%" %TILE_SIZE% tile

echo.
echo ========================================
echo   CONCLUIDO!
echo ========================================
echo.
echo Os tiles foram salvos em: extracted_tiles\
echo.
echo PROXIMOS PASSOS:
echo 1. Veja os tiles extraidos em: extracted_tiles\
echo 2. Identifique os tiles que voce quer usar:
echo    - Grass (grama)
echo    - Dirt (terra)
echo    - Water (agua)
echo    - Stone (pedra)
echo 3. Anote os numeros dos tiles (ex: tile_0_0.png)
echo 4. Vamos configurar o jogo para usar esses tiles!
echo.
pause

