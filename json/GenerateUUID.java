import java.util.UUID;

public class GenerateUUID {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println("UUID=" + uuid.toString() );
    }
}