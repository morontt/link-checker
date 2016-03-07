package info.morontt.link_checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

/**
 * Spider link-checker
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("");
        System.out.println("#");
        System.out.println("#           #    #    #  #    #           ####   #    #  ######   ####   #    #");
        System.out.println("#           #    ##   #  #   #           #    #  #    #  #       #    #  #   #");
        System.out.println("#           #    # #  #  ####    #####   #       ######  #####   #       ####");
        System.out.println("#           #    #  # #  #  #            #       #    #  #       #       #  #");
        System.out.println("#           #    #   ##  #   #           #    #  #    #  #       #    #  #   #");
        System.out.println("#######     #    #    #  #    #           ####   #    #  ######   ####   #    #");
        System.out.println("");

        InputStream input = new FileInputStream(new File("./config.yaml"));
        Yaml yaml = new Yaml();
        Object data = yaml.load(input);
        input.close();

        System.out.println(data);
    }
}
