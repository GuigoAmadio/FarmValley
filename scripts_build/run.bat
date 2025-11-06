@echo off
chcp 65001 >nul
echo ================================================
echo 🎮 EXECUTANDO FARM VALLEY
echo ================================================
echo.

:: Mudar para o diretório raiz do projeto (onde está o build/)
cd /d "%~dp0.."

:: Verificar se está compilado
if not exist "build\*.class" (
    echo ⚠️  Projeto não compilado!
    echo.
    echo Compilando automaticamente...
    call "scripts_build\compile.bat"
    if %ERRORLEVEL% NEQ 0 (
        echo ❌ Erro na compilação!
        pause
        exit /b 1
    )
)

:: Executar o jogo
echo 🚀 Iniciando jogo...
echo.

java -cp build core.GameWindow

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ================================================
    echo ❌ ERRO NA EXECUÇÃO!
    echo ================================================
    echo.
    pause
) else (
    echo.
    echo ================================================
    echo ✅ JOGO ENCERRADO
    echo ================================================
    echo.
)

pause

