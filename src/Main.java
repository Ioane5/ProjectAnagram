import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        long startTimeNano = System.nanoTime();
        // Main part of anagram finding
        List<String> anagrams = findAnagrams(args[0], args[1]);
        long endTimeNano = System.nanoTime();
        long runningTimeMicro = (endTimeNano - startTimeNano) / 1000;
        // Now print results
        System.out.print(runningTimeMicro);
        for (String w : anagrams) {
            System.out.print(',');
            System.out.print(w);
        }
    }

    /**
     * Very simple and quick hash. (Not necessarily good one)
     */
    private static int hashString(String str) {
        int hash = Character.toLowerCase(str.charAt(0));
        hash *= hash;
        for (int i = 1; i < str.length(); i++) {
            hash += str.charAt(i) * str.charAt(i);
        }
        return hash;
    }

    private static Map<Character, Integer> fstCharMap = new HashMap<>();
    private static Map<Character, Integer> fstCharMapCopy = new HashMap<>();

    private static void fillCharMap(Map<Character, Integer> charMap, String str) {
        for (int i = 0; i < str.length(); i++) {
            Character c = str.charAt(i);
            int count = charMap.getOrDefault(c, 0);
            charMap.put(c, count + 1);
        }
    }

    private static boolean areAnagramsFull(String snd) {
        // Just copy
        for (Character c : fstCharMap.keySet()) {
            fstCharMapCopy.put(c, fstCharMap.get(c));
        }

        boolean result = true;
        for (int i = 0; i < snd.length(); i++) {
            Character c = Character.toLowerCase(snd.charAt(i));
            if (fstCharMap.getOrDefault(c, 0) > 0) {
                fstCharMap.put(c, fstCharMap.get(c) - 1);
            } else {
                result = false;
                break;
            }
        }
        fstCharMap.clear();
        // Swap references
        Map<Character, Integer> fst = fstCharMap;
        fstCharMap = fstCharMapCopy;
        fstCharMapCopy = fst;
        return result;
    }

    private static Character maxChar(String str) {
        Character minChar = Character.MIN_VALUE;
        for (int i = 0; i < str.length(); i++) {
            Character c = str.charAt(i);
            if (c > minChar) {
                minChar = c;
            }
        }
        return minChar;
    }

    private static List<String> findAnagrams(String path, String anagram) {
        fillCharMap(fstCharMap, anagram);
        List<String> anagrams = new ArrayList<>();
        Character maxChar = maxChar(anagram);
        int fstHash = hashString(anagram);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                if (line.length() < 1 || line.charAt(0) > maxChar) {
                    break;
                }
                if (anagram.length() == line.length() &&
                        fstHash == hashString(line) &&
                        areAnagramsFull(line)
                ) {
                    anagrams.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return anagrams;
    }
}