import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
public class App {
    
    public static void main(String[] args) throws Exception {
       
        System.out.println("hello world");
        FlatCobalt2IJTheme.setup();

        new Gui();

    }
}
