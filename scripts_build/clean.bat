@echo off
chcp 65001 >nul
echo ================================================
echo 🧹 LIMPANDO PROJETO FARM VALLEY
echo ================================================
echo.

echo Limpando arquivos compilados...
del /Q build\*.class 2>nul
del /Q build\core\*.class 2>nul
del /Q build\entities\*.class 2>nul
del /Q build\world\*.class 2>nul
del /Q build\systems\*.class 2>nul
del /Q build\items\*.class 2>nul
del /Q build\types\*.class 2>nul
del /Q build\utils\*.class 2>nul

echo Limpando arquivos temporários...
del /Q temp\logs\*.log 2>nul

echo Limpando backups antigos...
for /d %%i in (backup_*) do (
    set /p confirm="Deletar backup %%i? (S/N): "
    if /i "!confirm!"=="S" (
        rmdir /S /Q "%%i"
        echo   ✅ Deletado: %%i
    )
)

echo.
echo ================================================
echo ✅ LIMPEZA CONCLUÍDA!
echo ================================================
echo.
echo 📁 Build limpo
echo 📁 Temporários limpos
echo.

pause

