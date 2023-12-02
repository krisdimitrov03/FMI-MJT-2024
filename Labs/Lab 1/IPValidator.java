public class IPValidator {
    public static boolean validateIPv4Address(String str) {
        String[] parts = str.split("[.]");
 
        if(parts.length != 4)
            return false;
 
        for (var part : parts) {
            if(part.isEmpty())
 
                return false;
 
            if(part.length() > 1 && part.charAt(0) == '0')
                return false;
 
            for (char ch : part.toCharArray())
                if(!Character.isDigit(ch))
                    return false;
 
            int partNum = Integer.parseInt(part);
 
            if(partNum < 0 || partNum > 255)
                return false;
        }
 
        return true;
    }
}