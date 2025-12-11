import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Extrai frames individuais de spritesheets do Swordsman
 * Formato esperado: 4 linhas (direções) x 6 colunas (frames)
 * Linha 0 = Baixo, Linha 1 = Cima, Linha 2 = Esquerda, Linha 3 = Direita
 */
public class ExtractSwordsmanSprites {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java ExtractSwordsmanSprites <spritesheet.png> <pasta_destino> [tipo]");
            System.out.println("Exemplo: java ExtractSwordsmanSprites Swordsman_lvl1_Walk_without_shadow.png extracted_sprites/merchant npc");
            System.out.println();
            System.out.println("Tipos: npc, enemy");
            System.exit(1);
        }
        
        String spritesheetPath = args[0];
        String destino = args[1];
        String tipo = args.length > 2 ? args[2] : "npc";
        
        try {
            extractSpritesheet(spritesheetPath, destino, tipo);
            System.out.println("✅ Extração concluída!");
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void extractSpritesheet(String spritesheetPath, String destino, String tipo) throws IOException {
        File spritesheetFile = new File(spritesheetPath);
        if (!spritesheetFile.exists()) {
            throw new IOException("Spritesheet não encontrado: " + spritesheetPath);
        }
        
        BufferedImage spritesheet = ImageIO.read(spritesheetFile);
        int width = spritesheet.getWidth();
        int height = spritesheet.getHeight();
        
        // Assumir 4 linhas (direções) x 6 colunas (frames)
        // Formato: Linha 0 = Baixo, Linha 1 = Cima, Linha 2 = Esquerda, Linha 3 = Direita
        int framesPerRow = 6;
        int directions = 4;
        
        int frameWidth = width / framesPerRow;
        int frameHeight = height / directions;
        
        System.out.println("📐 Dimensões calculadas:");
        System.out.println("   Frame width: " + frameWidth);
        System.out.println("   Frame height: " + frameHeight);
        
        System.out.println("📐 Dimensões do spritesheet: " + width + "x" + height);
        System.out.println("📐 Tamanho de cada frame: " + frameWidth + "x" + frameHeight);
        System.out.println("📐 Extraindo " + framesPerRow + " frames x " + directions + " direções");
        System.out.println();
        
        // Criar pasta destino
        File destinoDir = new File(destino);
        if (!destinoDir.exists()) {
            destinoDir.mkdirs();
            System.out.println("📁 Criada pasta: " + destino);
        }
        
        // Extrair cada frame
        int count = 0;
        for (int dir = 0; dir < directions; dir++) {
            for (int frame = 0; frame < framesPerRow; frame++) {
                // Calcular posição no spritesheet
                int x = frame * frameWidth;
                int y = dir * frameHeight;
                
                // Extrair frame
                BufferedImage frameImage = spritesheet.getSubimage(x, y, frameWidth, frameHeight);
                
                // Nome do arquivo: frame_[direção]_[frame].png
                String filename = String.format("frame_%d_%d.png", dir, frame);
                File outputFile = new File(destinoDir, filename);
                
                // Salvar
                ImageIO.write(frameImage, "png", outputFile);
                count++;
                
                System.out.println("✅ [" + count + "] Extraído: " + filename + " (direção " + dir + ", frame " + frame + ")");
            }
        }
        
        System.out.println();
        System.out.println("📊 Total: " + count + " frames extraídos para " + destino);
        
        // Se tipo especificado, copiar para assets/sprites
        if (tipo.equals("npc") || tipo.equals("enemy")) {
            String nome = new File(destino).getName();
            String assetsDestino = "assets/sprites/" + tipo + "s/" + nome;
            File assetsDir = new File(assetsDestino);
            
            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }
            
            System.out.println();
            System.out.println("📋 Copiando para: " + assetsDestino);
            
            // Copiar todos os frames
            for (File frameFile : destinoDir.listFiles((dir, name) -> name.endsWith(".png"))) {
                File destFile = new File(assetsDir, frameFile.getName());
                ImageIO.write(ImageIO.read(frameFile), "png", destFile);
            }
            
            System.out.println("✅ Frames copiados para assets/sprites!");
        }
    }
}

