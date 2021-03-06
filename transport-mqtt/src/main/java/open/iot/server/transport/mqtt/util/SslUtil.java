package open.iot.server.transport.mqtt.util;

import open.iot.server.dao.EncryptionUtil;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;


public class SslUtil {

    private SslUtil() {
    }

    public static String getX509CertificateString(X509Certificate cert)
            throws CertificateEncodingException, IOException {
        Base64Utils.encodeToString(cert.getEncoded());
        return EncryptionUtil.trimNewLines(Base64Utils.encodeToString(cert.getEncoded()));
    }

    public static String getX509CertificateString(javax.security.cert.X509Certificate cert)
            throws javax.security.cert.CertificateEncodingException, IOException {
        Base64Utils.encodeToString(cert.getEncoded());
        return EncryptionUtil.trimNewLines(Base64Utils.encodeToString(cert.getEncoded()));
    }
}
