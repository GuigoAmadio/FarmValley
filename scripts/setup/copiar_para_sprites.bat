@echo off
echo ========================================
echo   COPIAR FRAMES PARA O JOGO
echo ========================================
echo.

if not exist "extracted_sprites" (
    echo ERRO: Pasta extracted_sprites nao encontrada!
    echo Execute primeiro: extrair_sprites.bat
    pause
    exit /b 1
)

if not exist "sprites" (
    mkdir sprites
    echo [OK] Pasta sprites criada!
)

echo Copiando frames para sprites/...
echo.

REM Copiar frames de animação (DOWN)
if exist "extracted_sprites\frame_2_0.png" (
    copy "extracted_sprites\frame_2_0.png" "sprites\player_down_1.png" > nul
    echo [OK] player_down_1.png
)
if exist "extracted_sprites\frame_2_1.png" (
    copy "extracted_sprites\frame_2_1.png" "sprites\player_down_2.png" > nul
    echo [OK] player_down_2.png
)

REM Copiar frames de animação (UP)
if exist "extracted_sprites\frame_0_0.png" (
    copy "extracted_sprites\frame_0_0.png" "sprites\player_up_1.png" > nul
    echo [OK] player_up_1.png
)
if exist "extracted_sprites\frame_0_1.png" (
    copy "extracted_sprites\frame_0_1.png" "sprites\player_up_2.png" > nul
    echo [OK] player_up_2.png
)

REM Copiar frames de animação (LEFT)
if exist "extracted_sprites\frame_1_0.png" (
    copy "extracted_sprites\frame_1_0.png" "sprites\player_left_1.png" > nul
    echo [OK] player_left_1.png
)
if exist "extracted_sprites\frame_1_1.png" (
    copy "extracted_sprites\frame_1_1.png" "sprites\player_left_2.png" > nul
    echo [OK] player_left_2.png
)

REM Copiar frames de animação (RIGHT)
if exist "extracted_sprites\frame_3_0.png" (
    copy "extracted_sprites\frame_3_0.png" "sprites\player_right_1.png" > nul
    echo [OK] player_right_1.png
)
if exist "extracted_sprites\frame_3_1.png" (
    copy "extracted_sprites\frame_3_1.png" "sprites\player_right_2.png" > nul
    echo [OK] player_right_2.png
)

REM Copiar sprite padrão (parado, olhando para baixo)
if exist "extracted_sprites\frame_2_0.png" (
    copy "extracted_sprites\frame_2_0.png" "sprites\player.png" > nul
    echo [OK] player.png (sprite padrão)
)

echo.
echo ========================================
echo   CONCLUIDO!
echo ========================================
echo.
echo Sprites copiados com sucesso para: sprites\
echo.
echo Arquivos criados:
echo   - player.png (padrão)
echo   - player_down_1.png, player_down_2.png
echo   - player_up_1.png, player_up_2.png
echo   - player_left_1.png, player_left_2.png
echo   - player_right_1.png, player_right_2.png
echo.
echo Agora execute: java GameWindow
echo.
pause

