package systems;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Sistema de diálogos carregados de JSON
 * Gerencia todas as falas de NPCs do jogo
 */
public class DialogueSystem {
    private static DialogueSystem instance;
    private Random random;
    
    // Diálogos por categoria
    private Map<String, List<String>> greetings;
    private Map<String, List<String>> dialogues;
    private List<String> tips;
    private List<String> gossip;
    private List<String> stories;
    private List<String> warnings;
    private List<String> farewells;
    
    // Fallback dialogues (caso JSON não carregue)
    private static final String[] DEFAULT_GREETINGS = {"Olá!", "Oi!", "Saudações!"};
    private static final String[] DEFAULT_DIALOGUES = {"...", "Belo dia.", "Como vai?"};
    private static final String[] DEFAULT_TIPS = {"Dica: Explore o mundo!"};
    
    private DialogueSystem() {
        this.random = new Random();
        this.greetings = new HashMap<>();
        this.dialogues = new HashMap<>();
        this.tips = new ArrayList<>();
        this.gossip = new ArrayList<>();
        this.stories = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.farewells = new ArrayList<>();
        
        loadDialogues();
    }
    
    public static DialogueSystem getInstance() {
        if (instance == null) {
            instance = new DialogueSystem();
        }
        return instance;
    }
    
    /**
     * Carrega diálogos do arquivo JSON
     */
    private void loadDialogues() {
        try {
            // Parse manual do JSON (sem dependência externa)
            String jsonContent = readFile("assets/data/dialogues.json");
            parseJson(jsonContent);
            System.out.println("✅ Diálogos carregados com sucesso!");
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao carregar diálogos, usando padrões: " + e.getMessage());
            loadDefaultDialogues();
        }
    }
    
    private String readFile(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(path)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }
        }
        return content.toString();
    }
    
    /**
     * Parser JSON simples (sem dependências externas)
     */
    private void parseJson(String json) {
        // Remover espaços e quebras de linha extras
        json = json.trim();
        
        // Extrair seções
        greetings = parseNestedSection(json, "greetings");
        dialogues = parseNestedSection(json, "dialogues");
        tips = parseArraySection(json, "tips");
        gossip = parseArraySection(json, "gossip");
        stories = parseArraySection(json, "stories");
        warnings = parseArraySection(json, "warnings");
        farewells = parseArraySection(json, "farewells");
    }
    
    private Map<String, List<String>> parseNestedSection(String json, String sectionName) {
        Map<String, List<String>> result = new HashMap<>();
        
        String searchKey = "\"" + sectionName + "\"";
        int start = json.indexOf(searchKey);
        if (start == -1) return result;
        
        // Encontrar o início do objeto
        int braceStart = json.indexOf("{", start);
        if (braceStart == -1) return result;
        
        // Encontrar o fim do objeto (contando chaves)
        int braceCount = 1;
        int braceEnd = braceStart + 1;
        while (braceCount > 0 && braceEnd < json.length()) {
            char c = json.charAt(braceEnd);
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;
            braceEnd++;
        }
        
        String section = json.substring(braceStart + 1, braceEnd - 1);
        
        // Extrair sub-categorias (merchant, farmer, etc.)
        String[] categories = {"merchant", "farmer", "villager", "wanderer"};
        for (String category : categories) {
            List<String> items = parseArraySection(section, category);
            if (!items.isEmpty()) {
                result.put(category, items);
            }
        }
        
        return result;
    }
    
    private List<String> parseArraySection(String json, String sectionName) {
        List<String> result = new ArrayList<>();
        
        String searchKey = "\"" + sectionName + "\"";
        int start = json.indexOf(searchKey);
        if (start == -1) return result;
        
        // Encontrar o início do array
        int bracketStart = json.indexOf("[", start);
        if (bracketStart == -1) return result;
        
        // Encontrar o fim do array
        int bracketEnd = json.indexOf("]", bracketStart);
        if (bracketEnd == -1) return result;
        
        String arrayContent = json.substring(bracketStart + 1, bracketEnd);
        
        // Extrair strings do array
        boolean inString = false;
        StringBuilder currentString = new StringBuilder();
        
        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);
            
            if (c == '"' && (i == 0 || arrayContent.charAt(i - 1) != '\\')) {
                if (inString) {
                    // Fim da string
                    String str = currentString.toString()
                        .replace("\\\"", "\"")
                        .replace("\\n", "\n");
                    if (!str.trim().isEmpty()) {
                        result.add(str);
                    }
                    currentString = new StringBuilder();
                }
                inString = !inString;
            } else if (inString) {
                currentString.append(c);
            }
        }
        
        return result;
    }
    
    private void loadDefaultDialogues() {
        // Greetings padrão
        for (String type : new String[]{"merchant", "farmer", "villager", "wanderer"}) {
            List<String> list = new ArrayList<>();
            for (String g : DEFAULT_GREETINGS) list.add(g);
            greetings.put(type, list);
            
            List<String> dlist = new ArrayList<>();
            for (String d : DEFAULT_DIALOGUES) dlist.add(d);
            dialogues.put(type, dlist);
        }
        
        for (String t : DEFAULT_TIPS) tips.add(t);
        gossip.add("Nada de novo por aqui...");
        stories.add("Não tenho histórias para contar.");
        warnings.add("Cuidado por aí!");
        farewells.add("Até mais!");
    }
    
    // ===== GETTERS PARA DIÁLOGOS =====
    
    public String getGreeting(String npcType) {
        List<String> list = greetings.get(npcType.toLowerCase());
        if (list == null || list.isEmpty()) {
            return DEFAULT_GREETINGS[random.nextInt(DEFAULT_GREETINGS.length)];
        }
        return list.get(random.nextInt(list.size()));
    }
    
    public String getRandomDialogue(String npcType) {
        List<String> list = dialogues.get(npcType.toLowerCase());
        if (list == null || list.isEmpty()) {
            return DEFAULT_DIALOGUES[random.nextInt(DEFAULT_DIALOGUES.length)];
        }
        return list.get(random.nextInt(list.size()));
    }
    
    public String getTip() {
        if (tips.isEmpty()) return DEFAULT_TIPS[0];
        return tips.get(random.nextInt(tips.size()));
    }
    
    public String getGossip() {
        if (gossip.isEmpty()) return "Nada de novo...";
        return gossip.get(random.nextInt(gossip.size()));
    }
    
    public String getStory() {
        if (stories.isEmpty()) return "Não tenho histórias...";
        return stories.get(random.nextInt(stories.size()));
    }
    
    public String getWarning() {
        if (warnings.isEmpty()) return "Cuidado!";
        return warnings.get(random.nextInt(warnings.size()));
    }
    
    public String getFarewell() {
        if (farewells.isEmpty()) return "Até mais!";
        return farewells.get(random.nextInt(farewells.size()));
    }
}
