package open.iot.server.service.security.device;

import open.iot.server.common.data.Device;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.security.DeviceCredentials;
import open.iot.server.common.data.security.DeviceCredentialsFilter;
import open.iot.server.common.transport.auth.DeviceAuthResult;
import open.iot.server.common.transport.auth.DeviceAuthService;
import open.iot.server.dao.device.DeviceCredentialsService;
import open.iot.server.dao.device.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultDeviceAuthService implements DeviceAuthService {

    private static final Logger log = LoggerFactory.getLogger("DefaultDeviceAuthService");

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceCredentialsService deviceCredentialsService;

    @Override
    public DeviceAuthResult process(DeviceCredentialsFilter credentialsFilter) {
        log.trace("Lookup device credentials using filter {}", credentialsFilter);
        DeviceCredentials credentials = deviceCredentialsService.findDeviceCredentialsByCredentialsId(credentialsFilter.getCredentialsId());
        if (credentials != null) {
            log.trace("Credentials found {}", credentials);
            if (credentials.getCredentialsType() == credentialsFilter.getCredentialsType()) {
                switch (credentials.getCredentialsType()) {
                    case ACCESS_TOKEN:
                        // Credentials ID matches Credentials value in this
                        // primitive case;
                        return DeviceAuthResult.of(credentials.getDeviceId());
                    case X509_CERTIFICATE:
                        return DeviceAuthResult.of(credentials.getDeviceId());
                    default:
                        return DeviceAuthResult.of("Credentials Type is not supported yet!");
                }
            } else {
                return DeviceAuthResult.of("Credentials Type mismatch!");
            }
        } else {
            log.trace("Credentials not found!");
            return DeviceAuthResult.of("Credentials Not Found!");
        }
    }

    @Override
    public Optional<Device> findDeviceById(DeviceId deviceId) {
        return Optional.ofNullable(deviceService.findDeviceById(deviceId));
    }
}
