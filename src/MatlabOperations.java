import javafx.util.Pair;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 777 on 10.04.2015.
 */
public final class MatlabOperations {
    public static Pair<MatlabProxy, Properties> openConnection() throws MatlabConnectionException {
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true).setHidden(true).build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        MatlabProxy proxy = factory.getProxy();

        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            properties.load(input);

            return new Pair<MatlabProxy, Properties>(proxy, properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
