package matlab;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

public final class MatlabOperations {
//    public static Pair<MatlabProxy, Properties> openConnection() throws MatlabConnectionException {
//        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
//                .setUsePreviouslyControlledSession(true).setHidden(true).build();
//        MatlabProxyFactory factory = new MatlabProxyFactory(options);
//        MatlabProxy proxy = factory.getProxy();
//
//        Properties properties = new Properties();
//        InputStream input = null;
//
//        try {
//            input = new FileInputStream("config.properties");
//            properties.load(input);
//
//            return new Pair<MatlabProxy, Properties>(proxy, properties);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
    public static MatlabProxy openConnection() throws MatlabConnectionException {
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true).setHidden(true).build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);

        return factory.getProxy();
    }
}
