@echo off
chcp 65001 >nul
echo ================================================
echo 🔨 COMPILANDO FARM VALLEY
echo ================================================
echo.

:: Verificar JavaFX
echo [1/3] Verificando ambiente...
where javac >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java não encontrado! Instale JDK 21+
    pause
    exit /b 1
)

:: Limpar build anterior
echo [2/3] Limpando build anterior...
del /Q build\*.class 2>nul

:: Compilar todos os arquivos
echo [3/3] Compilando código fonte...
echo.

javac -d build ^
    -sourcepath src ^
    src/core/*.java ^
    src/entities/*.java ^
    src/world/*.java ^
    src/systems/*.java ^
    src/items/*.java ^
    src/types/*.java ^
    src/utils/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================
    echo ✅ COMPILAÇÃO CONCLUÍDA COM SUCESSO!
    echo ================================================
    echo.
    echo 📦 Arquivos compilados em: build\
    echo.
    echo 🚀 Para executar:
    echo    scripts\build\run.bat
    echo.
) else (
    echo.
    echo ================================================
    echo ❌ ERRO NA COMPILAÇÃO!
    echo ================================================
    echo.
    echo Verifique os erros acima
    echo.
)

pause

