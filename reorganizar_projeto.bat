@echo off
chcp 65001 >nul
echo ================================================
echo 🗂️  REORGANIZAÇÃO DO PROJETO FARM VALLEY
echo ================================================
echo.

:: Criar backup antes de reorganizar
echo [1/10] Criando backup...
set BACKUP_DIR=backup_%date:~-4,4%%date:~-7,2%%date:~-10,2%_%time:~0,2%%time:~3,2%%time:~6,2%
set BACKUP_DIR=%BACKUP_DIR: =0%
mkdir "%BACKUP_DIR%" 2>nul
echo Backup criado em: %BACKUP_DIR%
echo.

:: Criar nova estrutura de diretórios
echo [2/10] Criando estrutura de diretórios...

:: Código fonte
mkdir src\core 2>nul
mkdir src\entities 2>nul
mkdir src\world 2>nul
mkdir src\systems 2>nul
mkdir src\items 2>nul
mkdir src\types 2>nul
mkdir src\utils 2>nul

:: Assets
mkdir assets\sprites\player 2>nul
mkdir assets\sprites\tiles 2>nul
mkdir assets\sprites\decorations\trees 2>nul
mkdir assets\sprites\decorations\bushes 2>nul
mkdir assets\sprites\decorations\ruins 2>nul
mkdir assets\sprites\icons 2>nul
mkdir assets\sprites\ui 2>nul
mkdir assets\sounds\music 2>nul
mkdir assets\sounds\sfx 2>nul
mkdir assets\fonts 2>nul

:: Documentação
mkdir docs 2>nul

:: Ferramentas
mkdir tools\extractors 2>nul

:: Scripts
mkdir scripts\build 2>nul
mkdir scripts\setup 2>nul

:: Build e temp
mkdir build 2>nul
mkdir temp\extracted_sprites 2>nul
mkdir temp\extracted_tiles 2>nul
mkdir temp\logs 2>nul

:: Releases
mkdir releases 2>nul

echo ✅ Estrutura criada!
echo.

:: Mover código fonte
echo [3/10] Movendo código fonte...

:: Core
move /Y GameEngine.java src\core\ 2>nul
move /Y GameWindow.java src\core\ 2>nul

:: Entities
move /Y Player.java src\entities\ 2>nul
move /Y Decoration.java src\entities\ 2>nul
move /Y Crop.java src\entities\ 2>nul

:: World
move /Y Farm.java src\world\ 2>nul
move /Y Tile.java src\world\ 2>nul
move /Y TileType.java src\world\ 2>nul
move /Y TimeSystem.java src\world\ 2>nul

:: Systems
move /Y HarvestSystem.java src\systems\ 2>nul
move /Y DecorationManager.java src\systems\ 2>nul
move /Y Inventory.java src\systems\ 2>nul
move /Y UIManager.java src\systems\ 2>nul

:: Items
move /Y Item.java src\items\ 2>nul
move /Y ItemType.java src\items\ 2>nul
move /Y ResourceType.java src\items\ 2>nul

:: Types
move /Y CropType.java src\types\ 2>nul
move /Y DecorationType.java src\types\ 2>nul

:: Utils
move /Y SpriteLoader.java src\utils\ 2>nul

echo ✅ Código fonte movido!
echo.

:: Mover assets
echo [4/10] Movendo assets...

:: Player sprites
move /Y sprites\player*.png assets\sprites\player\ 2>nul

:: Tile sprites
move /Y sprites\grass.png assets\sprites\tiles\ 2>nul
move /Y sprites\dirt.png assets\sprites\tiles\ 2>nul

:: Decorations
xcopy /Y /Q sprites\trees\*.png assets\sprites\decorations\trees\ 2>nul
xcopy /Y /Q sprites\bushes\*.png assets\sprites\decorations\bushes\ 2>nul
xcopy /Y /Q sprites\ruins\*.png assets\sprites\decorations\ruins\ 2>nul

:: Icons
xcopy /Y /Q sprites\icons\*.png assets\sprites\icons\ 2>nul

:: UI
xcopy /Y /Q sprites\ui\*.png assets\sprites\ui\ 2>nul

echo ✅ Assets movidos!
echo.

:: Mover documentação
echo [5/10] Movendo documentação...

move /Y SPRITES_NECESSARIOS.md docs\SPRITES_GUIDE.md 2>nul
move /Y ICONES_CHECKLIST.md docs\ICONS_CHECKLIST.md 2>nul
move /Y ICONES_PRIORITARIOS.md docs\ICONS_PRIORITY.md 2>nul
move /Y MELHORIAS_UI.md docs\UI_IMPROVEMENTS.md 2>nul
move /Y PLANO_ACAO_IMEDIATO.md docs\DEVELOPMENT_PLAN.md 2>nul
move /Y ASSETS_INVENTORY.md docs\ASSETS_INVENTORY.md 2>nul
move /Y NOVA_ESTRUTURA.md docs\PROJECT_STRUCTURE.md 2>nul
copy /Y README.md docs\README_ORIGINAL.md 2>nul

echo ✅ Documentação movida!
echo.

:: Mover ferramentas
echo [6/10] Movendo ferramentas...

move /Y SpriteSheetExtractor.java tools\ 2>nul
move /Y TileSetExtractor.java tools\ 2>nul
move /Y extrair_sprites.bat tools\extractors\ 2>nul
move /Y extrair_tiles.bat tools\extractors\ 2>nul
move /Y extrair_icones_ui.bat tools\extractors\ 2>nul

echo ✅ Ferramentas movidas!
echo.

:: Mover scripts
echo [7/10] Movendo scripts...

move /Y run.bat scripts\build\run_old.bat 2>nul
move /Y run-javafx.bat scripts\build\run-javafx_old.bat 2>nul
move /Y copiar_*.bat scripts\setup\ 2>nul
move /Y criar_*.bat scripts\setup\ 2>nul

echo ✅ Scripts movidos!
echo.

:: Mover arquivos compilados
echo [8/10] Movendo arquivos compilados...

move /Y *.class build\ 2>nul

echo ✅ Arquivos compilados movidos!
echo.

:: Mover arquivos temporários
echo [9/10] Movendo temporários...

xcopy /E /Y /Q extracted_sprites\*.* temp\extracted_sprites\ 2>nul
xcopy /E /Y /Q extracted_tiles\*.* temp\extracted_tiles\ 2>nul
move /Y *.log temp\logs\ 2>nul

echo ✅ Temporários movidos!
echo.

:: Criar .gitkeep em pastas vazias
echo [10/10] Finalizando...

echo. > build\.gitkeep
echo. > temp\.gitkeep
echo. > releases\.gitkeep
echo. > assets\sounds\music\.gitkeep
echo. > assets\sounds\sfx\.gitkeep
echo. > assets\fonts\.gitkeep

echo ✅ Reorganização concluída!
echo.

:: Gerar relatório
echo ================================================
echo 📊 RELATÓRIO DE MIGRAÇÃO
echo ================================================
echo.
echo ✅ Estrutura de diretórios criada
echo ✅ %temp% arquivos Java organizados em src/
echo ✅ Assets organizados em assets/
echo ✅ Documentação movida para docs/
echo ✅ Ferramentas movidas para tools/
echo ✅ Scripts movidos para scripts/
echo ✅ Build files movidos para build/
echo ✅ Temporários movidos para temp/
echo.
echo ⚠️  PRÓXIMOS PASSOS:
echo    1. Verifique a nova estrutura
echo    2. Execute: scripts\build\compile.bat
echo    3. Execute: scripts\build\run.bat
echo    4. Delete pastas antigas vazias (sprites, extracted_*)
echo.
echo 📁 Backup salvo em: %BACKUP_DIR%
echo.
echo ================================================

pause

