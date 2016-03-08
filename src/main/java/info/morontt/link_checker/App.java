package info.morontt.link_checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Spider link-checker
 */
public class App {
    public static void main(String[] args) {
        System.out.println("");
        System.out.println("#");
        System.out.println("#           #    #    #  #    #           ####   #    #  ######   ####   #    #");
        System.out.println("#           #    ##   #  #   #           #    #  #    #  #       #    #  #   #");
        System.out.println("#           #    # #  #  ####    #####   #       ######  #####   #       ####");
        System.out.println("#           #    #  # #  #  #            #       #    #  #       #       #  #");
        System.out.println("#           #    #   ##  #   #           #    #  #    #  #       #    #  #   #");
        System.out.println("#######     #    #    #  #    #           ####   #    #  ######   ####   #    #");
        System.out.println("");

        Config config;

        try {
            InputStream input = new FileInputStream(new File("./config.yaml"));
            Yaml yaml = new Yaml(new Constructor(Config.class));
            config = (Config) yaml.load(input);
            input.close();
        } catch (IOException e) {
            System.err.println("file config.yaml not found");
            return;
        } catch (YAMLException e) {
            System.err.println("unrecognized format config.yaml");
            return;
        }

        System.out.println(config);
    }
}
