import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Extrator de Sprites - Corta uma spritesheet em frames individuais
 * 
 * USO: java SpriteSheetExtractor <arquivo_entrada> <largura_frame> <altura_frame> <colunas> <linhas>
 */
public class SpriteSheetExtractor {
    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("=== EXTRATOR DE SPRITES ===");
            System.out.println();
            System.out.println("USO:");
            System.out.println("  java SpriteSheetExtractor <arquivo> <largura> <altura> <colunas> <linhas>");
            System.out.println();
            System.out.println("EXEMPLO:");
            System.out.println("  java SpriteSheetExtractor orc2_walk.png 48 64 6 4");
            System.out.println();
            System.out.println("Isso vai extrair 24 frames (6x4) de 48x64 pixels cada");
            System.out.println("Salvando como: frame_0_0.png, frame_0_1.png, etc.");
            return;
        }

        String inputFile = args[0];
        int frameWidth = Integer.parseInt(args[1]);
        int frameHeight = Integer.parseInt(args[2]);
        int columns = Integer.parseInt(args[3]);
        int rows = Integer.parseInt(args[4]);

        extractSprites(inputFile, frameWidth, frameHeight, columns, rows);
    }

    public static void extractSprites(String inputFile, int frameWidth, int frameHeight, int columns, int rows) {
        try {
            System.out.println("=== EXTRATOR DE SPRITES ===");
            System.out.println("Arquivo: " + inputFile);
            System.out.println("Frame: " + frameWidth + "x" + frameHeight + " pixels");
            System.out.println("Grid: " + columns + " colunas x " + rows + " linhas");
            System.out.println();

            // Carregar spritesheet
            BufferedImage spriteSheet = ImageIO.read(new File(inputFile));
            System.out.println("✓ Spritesheet carregada: " + spriteSheet.getWidth() + "x" + spriteSheet.getHeight());

            // Criar pasta de saída
            String outputDir = "extracted_sprites";
            new File(outputDir).mkdirs();
            System.out.println("✓ Pasta criada: " + outputDir);
            System.out.println();

            int frameCount = 0;

            // Extrair cada frame
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int x = col * frameWidth;
                    int y = row * frameHeight;

                    // Verificar se está dentro dos limites
                    if (x + frameWidth > spriteSheet.getWidth() || 
                        y + frameHeight > spriteSheet.getHeight()) {
                        System.out.println("⚠ Frame " + row + "_" + col + " fora dos limites, pulando...");
                        continue;
                    }

                    // Extrair subimagem
                    BufferedImage frame = spriteSheet.getSubimage(x, y, frameWidth, frameHeight);

                    // Salvar frame
                    String outputFile = outputDir + "/frame_" + row + "_" + col + ".png";
                    ImageIO.write(frame, "PNG", new File(outputFile));

                    frameCount++;
                    System.out.println("✓ Extraído: frame_" + row + "_" + col + ".png (linha " + row + ", coluna " + col + ")");
                }
            }

            System.out.println();
            System.out.println("=== CONCLUÍDO! ===");
            System.out.println("Total de frames extraídos: " + frameCount);
            System.out.println("Localização: " + outputDir + "/");
            System.out.println();
            System.out.println("MAPEAMENTO DE DIREÇÕES:");
            System.out.println("  Linha 0 (frame_0_X.png) = UP (cima)");
            System.out.println("  Linha 1 (frame_1_X.png) = LEFT (esquerda)");
            System.out.println("  Linha 2 (frame_2_X.png) = DOWN (baixo)");
            System.out.println("  Linha 3 (frame_3_X.png) = RIGHT (direita)");

        } catch (IOException e) {
            System.err.println("ERRO ao processar imagem: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

