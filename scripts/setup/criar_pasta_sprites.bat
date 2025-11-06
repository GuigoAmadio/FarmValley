@echo off
echo ========================================
echo   CRIANDO ESTRUTURA DE SPRITES
echo ========================================
echo.

REM Criar pasta sprites
if not exist "sprites" (
    mkdir sprites
    echo [OK] Pasta 'sprites' criada!
) else (
    echo [!] Pasta 'sprites' ja existe!
)

echo.
echo ========================================
echo   ESTRUTURA CRIADA COM SUCESSO!
echo ========================================
echo.
echo Agora:
echo 1. Acesse: https://www.piskelapp.com/
echo 2. Crie um sprite 32x32 pixels
echo 3. Desenhe seu personagem
echo 4. Exporte como PNG
echo 5. Salve como: sprites\player.png
echo.
echo Depois execute: run.bat
echo.
pause

