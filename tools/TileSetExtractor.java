import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Extrator de Tilesets - Corta um tileset em tiles individuais
 * Específico para tiles de chão (grass, dirt, water, etc)
 */
public class TileSetExtractor {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("=== EXTRATOR DE TILES ===");
            System.out.println();
            System.out.println("USO:");
            System.out.println("  java TileSetExtractor <arquivo> <tile_size> <prefixo>");
            System.out.println();
            System.out.println("EXEMPLO:");
            System.out.println("  java TileSetExtractor tileset.png 32 tile");
            System.out.println();
            System.out.println("Isso vai extrair todos os tiles de 32x32 pixels");
            System.out.println("Salvando como: tile_0_0.png, tile_0_1.png, etc.");
            return;
        }

        String inputFile = args[0];
        int tileSize = Integer.parseInt(args[1]);
        String prefix = args[2];

        extractTiles(inputFile, tileSize, prefix);
    }

    public static void extractTiles(String inputFile, int tileSize, String prefix) {
        try {
            System.out.println("=== EXTRATOR DE TILES ===");
            System.out.println("Arquivo: " + inputFile);
            System.out.println("Tamanho do tile: " + tileSize + "x" + tileSize + " pixels");
            System.out.println("Prefixo: " + prefix);
            System.out.println();

            // Carregar tileset
            BufferedImage tileset = ImageIO.read(new File(inputFile));
            System.out.println("✓ Tileset carregado: " + tileset.getWidth() + "x" + tileset.getHeight());

            // Calcular grid
            int columns = tileset.getWidth() / tileSize;
            int rows = tileset.getHeight() / tileSize;
            
            System.out.println("✓ Grid detectado: " + columns + " colunas x " + rows + " linhas");

            // Criar pasta de saída
            String outputDir = "extracted_tiles";
            new File(outputDir).mkdirs();
            System.out.println("✓ Pasta criada: " + outputDir);
            System.out.println();

            int tileCount = 0;

            // Extrair cada tile
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int x = col * tileSize;
                    int y = row * tileSize;

                    // Verificar se está dentro dos limites
                    if (x + tileSize > tileset.getWidth() || 
                        y + tileSize > tileset.getHeight()) {
                        continue;
                    }

                    // Extrair subimagem
                    BufferedImage tile = tileset.getSubimage(x, y, tileSize, tileSize);

                    // Verificar se o tile não é completamente transparente/vazio
                    if (!isEmpty(tile)) {
                        // Salvar tile
                        String outputFile = outputDir + "/" + prefix + "_" + row + "_" + col + ".png";
                        ImageIO.write(tile, "PNG", new File(outputFile));

                        tileCount++;
                        
                        // Identificar tipo de tile
                        String tileType = identifyTileType(tile, row, col);
                        System.out.println("✓ " + prefix + "_" + row + "_" + col + ".png" + 
                                         " (" + tileType + ")");
                    }
                }
            }

            System.out.println();
            System.out.println("=== CONCLUÍDO! ===");
            System.out.println("Total de tiles extraídos: " + tileCount);
            System.out.println("Localização: " + outputDir + "/");
            System.out.println();
            System.out.println("SUGESTÕES DE USO:");
            System.out.println("  Grass: Tiles com verde predominante");
            System.out.println("  Dirt:  Tiles com marrom predominante");
            System.out.println("  Water: Tiles com azul predominante");
            System.out.println("  Stone: Tiles com cinza predominante");

        } catch (IOException e) {
            System.err.println("ERRO ao processar imagem: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Verificar se o tile está vazio
    private static boolean isEmpty(BufferedImage tile) {
        for (int y = 0; y < tile.getHeight(); y++) {
            for (int x = 0; x < tile.getWidth(); x++) {
                int pixel = tile.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha > 10) { // Se tiver alguma opacidade
                    return false;
                }
            }
        }
        return true;
    }
    
    // Tentar identificar o tipo de tile pela cor predominante
    private static String identifyTileType(BufferedImage tile, int row, int col) {
        long redSum = 0, greenSum = 0, blueSum = 0;
        int pixelCount = 0;
        
        for (int y = 0; y < tile.getHeight(); y++) {
            for (int x = 0; x < tile.getWidth(); x++) {
                int pixel = tile.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                if (alpha > 10) {
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;
                    
                    redSum += red;
                    greenSum += green;
                    blueSum += blue;
                    pixelCount++;
                }
            }
        }
        
        if (pixelCount == 0) return "Empty";
        
        int avgRed = (int)(redSum / pixelCount);
        int avgGreen = (int)(greenSum / pixelCount);
        int avgBlue = (int)(blueSum / pixelCount);
        
        // Identificar tipo pela cor predominante
        if (avgBlue > avgGreen && avgBlue > avgRed && avgBlue > 100) {
            return "Water";
        } else if (avgGreen > avgRed && avgGreen > avgBlue && avgGreen > 80) {
            return "Grass";
        } else if (avgRed > 100 && avgGreen > 80 && avgBlue < 80) {
            return "Dirt";
        } else if (avgRed > 100 && avgGreen > 100 && avgBlue > 100) {
            return "Stone";
        } else {
            return "Mixed/Transition";
        }
    }
}

