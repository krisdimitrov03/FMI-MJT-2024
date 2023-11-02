public class BrokenKeyboard {
    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        if(message.isEmpty())
            return 0;
 
        String[] words = message.split(" ");
        int count = 0;
 
        for (var word : words) {
            if(word.isEmpty() || word.isBlank())
                continue;
 
            boolean isWritable = true;
            for(char ch : brokenKeys.toCharArray()) {
                if(word.contains("" + ch)) {
                    isWritable = false;
                    break;
                }
            }
 
            if(isWritable)
                count++;
        }
 
        return count;
    }
}
 