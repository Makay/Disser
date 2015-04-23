package configOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Operations {
    public static Integer getInitCreaturesNumber() {
        return getPropertyValue("initCreaturesNumber");
    }

    public static Integer getIterationsNumber() {
        return getPropertyValue("iterationsNumber");
    }

    private static Integer getPropertyValue(String propertyName) {
        Properties properties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
            return Integer.valueOf(properties.getProperty(propertyName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
