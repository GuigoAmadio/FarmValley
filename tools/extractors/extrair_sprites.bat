@echo off
echo ========================================
echo   EXTRATOR DE SPRITES - FARM VALLEY
echo ========================================
echo.

REM Compilar o extrator
echo [1/3] Compilando extrator...
javac SpriteSheetExtractor.java
if %errorlevel% neq 0 (
    echo ERRO ao compilar!
    pause
    exit /b 1
)
echo      OK!
echo.

echo [2/3] Copiando spritesheet...
echo.
echo Cole o caminho COMPLETO do arquivo PNG aqui:
echo Exemplo: C:\Users\Guillermo\Downloads\...\orc2_walk_without_shadow.png
echo.
set /p SPRITE_FILE="Arquivo: "

if not exist "%SPRITE_FILE%" (
    echo.
    echo ERRO: Arquivo nao encontrado!
    pause
    exit /b 1
)

echo.
echo [3/3] Extraindo frames...
echo.
echo Configuracao padrao:
echo   - Largura do frame: 64 pixels
echo   - Altura do frame: 64 pixels
echo   - Colunas: 6
echo   - Linhas: 4
echo.
echo Pressione ENTER para continuar ou Ctrl+C para cancelar...
pause > nul

java SpriteSheetExtractor "%SPRITE_FILE%" 64 64 6 4

echo.
echo ========================================
echo   CONCLUIDO!
echo ========================================
echo.
echo Os frames foram salvos em: extracted_sprites\
echo.
echo PROXIMOS PASSOS:
echo 1. Veja os frames extraidos em: extracted_sprites\
echo 2. Renomeie os que voce quer usar:
echo    - frame_2_0.png -^> player_down_1.png
echo    - frame_2_1.png -^> player_down_2.png
echo    - frame_0_0.png -^> player_up_1.png
echo    - frame_0_1.png -^> player_up_2.png
echo    - frame_1_0.png -^> player_left_1.png
echo    - frame_1_1.png -^> player_left_2.png
echo    - frame_3_0.png -^> player_right_1.png
echo    - frame_3_1.png -^> player_right_2.png
echo 3. Copie para a pasta sprites\
echo 4. Execute o jogo!
echo.
pause

